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

/*
 * Created on Feb 24, 2006
 */
package net.sf.nmedit.nomad.patch.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.swing.SwingUtilities;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Connector;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Module;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Patch;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.Event;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.event.ModuleListener;
import net.sf.nmedit.nomad.theme.component.NomadComponent;
import net.sf.nmedit.nomad.theme.component.NomadConnector;
import net.sf.nmedit.nomad.theme.graphics.CableRenderer;
import net.sf.nmedit.nomad.util.collection.ListEntry;
import net.sf.nmedit.nomad.util.graphics.shape.RenderOp;
import net.sf.nmedit.nomad.util.graphics.shape.ShapeDisplay2;


public class CableDisplay extends ShapeDisplay2<Curve> {

    private ModuleSectionUI moduleSectionUI;
    private CDEventHandler cdEventHandler = new CDEventHandler();

    public CableDisplay(ModuleSectionUI sectionUI) {
        super(sectionUI, new CableRenderOp(sectionUI.getModuleSection().getPatch()));
        this.moduleSectionUI = sectionUI;
        moduleSectionUI.addContainerListener(cdEventHandler);
    }
    
    public ModuleSectionUI getModuleSectionUI() {
        return moduleSectionUI;
    }

    public void updateCables(Module m)
    {
        beginUpdate();
        for (int i=m.getConnectorCount()-1;i>=0;i--)
        {
            ListEntry<Cable> list = map.get(m.getConnector(i));

            while (list!=null)
            {
                Cable c = list.item;
                updateLocation(c);
                repaint(c);
                list = list.remaining;
            }
        }
        endUpdate();
    }
  
    private Map<Connector,ListEntry<Cable>> map =
        new java.util.Hashtable<Connector,ListEntry<Cable>>();
    
    private Cable find(Connector a, Connector b)
    {
        ListEntry<Cable> list = map.get(a);
        while (list!=null)
        {
            if (list.item.contains(b))
                return list.item;
            list = list.remaining;
        }
        return null;
    }
    
    private void updateLocation(Cable cable)
    {
        NomadComponent ui1 = (NomadComponent) cable.getC1().getUI();
        NomadComponent ui2 = (NomadComponent) cable.getC2().getUI();
        if (ui1!=null && ui2!=null)
        {
            Point p1 = new Point();
            Point p2 = new Point();
            getLocation(ui1, p1);
            getLocation(ui2, p2);
            cable.setCurve(p1, p2);
            repaint(cable);
        }
    }
    
    public void add(Connector a, Connector b)
    {
        Cable cable = new Cable(a, b);
        updateLocation(cable);
        map.put(a, new ListEntry<Cable>(cable, map.get(a)));
        map.put(b, new ListEntry<Cable>(cable, map.get(b)));
        add(cable);
    }
    
    private void remove(Connector c, Cable cable)
    {
        ListEntry<Cable> list = map.get(c);
        if (list.item == cable)
        {
            if (list.remaining==null)
                map.remove(c);
            else
                map.put(c, list.remaining);
        }
        else
        {
            ListEntry<Cable> prev = list;
            ListEntry<Cable> pos = list.remaining;
            while (pos!=null)
            {
                if (pos.item==cable)
                {
                    prev.remaining = pos.remaining;
                    return;
                }
                prev = pos;
                pos = pos.remaining;
            }
        }
    }
    
    public void remove(Connector a, Connector b)
    {
        Cable cable = find(a,b);
        if (cable!=null)
        {
            remove(a, cable);
            remove(b, cable);
            remove(cable);
        }
    }
    
    public void update(Connector a, Connector b)
    {
        Cable cable = find(a, b);
        if (cable!=null)
        {
            updateLocation(cable);
            repaint(cable);
        }
    }
    
    public void populate()
    {
        beginUpdate();
        for (Module m : moduleSectionUI.getModuleSection())
        {
            for (int i=m.getConnectorCount()-1;i>=0;i--)
            {
                Connector a = m.getConnector(i);
                for (Iterator<Connector> iter = a.childIterator(); iter.hasNext(); )
                {
                    add(a, iter.next());
                }
            }
        }
        endUpdate();
    }
    
    public NomadConnector findConnectorAt( int x, int y )
    {
        Container cont = moduleSectionUI;
        for (int i=cont.getComponentCount()-1;i>=0;i--)
        {
            Component comp = cont.getComponent(i);
            if (comp instanceof ModuleUI)
            {
                Container cont2 = (Container) comp;
                int mx = comp.getX();
                int my = comp.getY();
                x-=mx;
                y-=my;
                
                for (int j=cont2.getComponentCount()-1;j>=0;j--)
                {
                    Component comp2 = cont2.getComponent(j);
                    if (comp2 instanceof NomadConnector)
                    {
                        if (comp2.contains(x-comp2.getX(), y-comp2.getY()))
                            return (NomadConnector) comp2;
                    }
                }
                
                x+=mx;
                y+=my;
            }
        }
        
        return null;
    }

    public void getLocation( NomadComponent nomadConnector, Point dst )
    {
        dst.x = nomadConnector.getX()+nomadConnector.getWidth()/2;
        dst.y = nomadConnector.getY()+nomadConnector.getHeight()/2;
        
        Module mod = nomadConnector.getModule();
        if (mod!=null)
        {
            dst.x += ModuleUI.Metrics.getPixelX(mod.getX());
            dst.y += ModuleUI.Metrics.getPixelY(mod.getY());
        }
    }

    public void paint( Graphics g )
    {
        paintBackground(null, g, 0, 0, moduleSectionUI.getWidth(), moduleSectionUI.getHeight());
    }

    public Cable[] getCables( Connector c )
    {
        ListEntry<Cable> list = map.get(c);
        if (list == null)
            return new Cable[0];
        
        ArrayList<Cable> tmp = new ArrayList<Cable>();
        while (list!=null)
        {
            tmp.add(list.item);
            list = list.remaining;
        }
        return tmp.toArray(new Cable[tmp.size()]);
    }

    public static class CableRenderOp implements RenderOp
    {
        
        CableRenderer painter;
        
        public CableRenderOp(Patch patch)
        {
            painter = new CableRenderer(patch);
        }

        public void configure( Graphics2D g2, int optimization )
        {
            if (optimization==OPTIMIZE_QUALITY)
            {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            }
            else
            {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
            }
        }

        public void render( Graphics2D g2, Shape shape )
        {
            painter.render(g2,(Curve)shape);
        }
        
    }
    
    private class CDEventHandler extends MouseAdapter implements ContainerListener, 
        ModuleListener
    {

        public void componentAdded(ContainerEvent event) 
        {
            if (event.getChild() instanceof ModuleUI) {
                ModuleUI m = (ModuleUI) event.getChild();
                Module mod = m.getModule();
                mod.addModuleListener(this);
                for (int i=0;i<mod.getConnectorCount();i++) 
                {
                    Component c =  mod.getConnector(i).getUI();
                    if(c!=null) c.addMouseListener(this);
                }
            }
        }

        public void componentRemoved(ContainerEvent event) 
        {
            if (event.getChild() instanceof ModuleUI) 
            {
                ModuleUI m = (ModuleUI) event.getChild();
                Module mod = m.getModule();
                mod.removeModuleListener(this);
                for (int i=0;i<mod.getConnectorCount();i++)
                {
                    Component c = mod.getConnector(i).getUI();
                    if(c!=null) c.removeMouseListener(this);
                }
            }
        }
        public void mousePressed(MouseEvent event) 
        {
            if (SwingUtilities.isLeftMouseButton(event) && event.getComponent() instanceof NomadConnector) 
            {                
                NomadConnector nc = (NomadConnector) event.getComponent();
                moduleSectionUI.createDragAction(nc, event);
            }
        }

        public void moduleRenamed( Event e )
        {
            
        }

        public void moduleMoved( Event e )
        {
            updateCables(e.getModule());
        }
        
    }

}
