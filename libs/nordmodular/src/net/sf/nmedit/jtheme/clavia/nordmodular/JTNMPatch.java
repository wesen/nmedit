/* Copyright (C) 2006 Christian Schneider
 * 
 * This file is part of Nomad.
 * 
 * Nomad is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Nomad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Nomad; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package net.sf.nmedit.jtheme.clavia.nordmodular;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import net.sf.nmedit.jpatch.Connection;
import net.sf.nmedit.jpatch.Connector;
import net.sf.nmedit.jpatch.LightweightIterator;
import net.sf.nmedit.jpatch.Module;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jpatch.clavia.nordmodular.VoiceArea;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.cable.Cable;
import net.sf.nmedit.jtheme.cable.JTCableManager;
import net.sf.nmedit.jtheme.cable.ScrollListener;
import net.sf.nmedit.jtheme.component.JTConnector;
import net.sf.nmedit.jtheme.component.JTModule;
import net.sf.nmedit.jtheme.component.JTModuleContainer;
import net.sf.nmedit.jtheme.component.JTPatch;
import net.sf.nmedit.jtheme.store.DefaultStorageContext;
import net.sf.nmedit.jtheme.store.ModuleStore;

public class JTNMPatch extends JTPatch
{

    private DefaultStorageContext storage;
    private JComponent top;
    private NMPatch patch;

    public JTNMPatch(DefaultStorageContext dsc, JTContext context, NMPatch patch) throws Exception
    {
        super(context);
        this.storage = dsc;
        this.patch = patch;
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setOneTouchExpandable(true);
        split.setContinuousLayout(false);
        split.setTopComponent(createVoiceArea(patch.getPolyVoiceArea()));
        split.setBottomComponent(createVoiceArea(patch.getCommonVoiceArea()));
        
        top = new JTPatchSettingsBar(this);
        
        setLayout(new BorderLayout());
        add(split, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);
    }
    
    public NMPatch getPatch()
    {
        return patch;
    }
    
    private Component createVoiceArea(VoiceArea va) throws Exception
    {
        JTModuleContainer cont = getContext().createModuleContainer();
        cont.setPatchContainer(this);
        
        populateVoiceArea(cont, va);
        
        JScrollPane scrollPane = new JScrollPane(cont);
        
        // scrollPane.setAutoscrolls(true);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(10);
        new ScrollListener(scrollPane, cont.getCableManager());
        return scrollPane;
    }

    private void populateVoiceArea(JTModuleContainer cont, VoiceArea va) throws Exception
    {
        JTContext context = getContext();
        
        int width = 0;
        int height = 0;
        
        for (Module module : va)
        {
            
            
            ModuleStore mstore =
            storage.getModuleStoreById(""+module.getDescriptor().getIndex());
            
            Image image = mstore.getStaticLayer();
            
            JTModule jtmodule =
            mstore.createModule(context, module);
            
            jtmodule.setLocation( module.getScreenX(), module.getScreenY() );

            width = Math.max(width, jtmodule.getX()+jtmodule.getWidth());
            height = Math.max(height, jtmodule.getY()+jtmodule.getHeight());
            
            cont.add(jtmodule);
            
            if (image == null)
            {
                image = jtmodule.renderStaticLayerImage();
                mstore.setStaticLayer(image);
            }
            
            jtmodule.setStaticLayerBackingStore(image);
        }
        
        cont.setPreferredSize(new Dimension(width, height));
        cont.setSize(new Dimension(width, height));
        
        JTCableManager cm = cont.getCableManager();
        LightweightIterator<Connection> lwiter =
        va.getConnectionManager().getConnections();
        while (lwiter.hasNext())
        {
            Connection c = lwiter.next();

            JTConnector con1 = find(cont, c.getSource());
            JTConnector con2 = find(cont, c.getDestination());
            
            if (con1 != null && con2 != null)
            { 
                Cable cable = cm.createCable(con1, con2);
                cable.setColor(c.getSource().getSignal().getColor());
                cm.add(cable);
            }
        }
        
    }

    private JTConnector find(JTModuleContainer cont, Connector c)
    {
        Module m = c.getOwner();
        for (int i=cont.getComponentCount()-1;i>=0;i--)
        {
            Component cc = cont.getComponent(i);
            if (cc instanceof JTModule && ((JTModule) cc).getModule() == m )
            {
                JTModule mc = (JTModule)cc;
                for (int j=mc.getComponentCount()-1;j>=0;j--)
                {
                    cc = mc.getComponent(j);
                    
                    if (cc instanceof JTConnector)
                    {
                        JTConnector jtc = (JTConnector) cc;
                        if (jtc.getConnector() == c)
                            return jtc;
                    }
                    
                }
                
                break;
            }
        }
        return null;
    }

}

