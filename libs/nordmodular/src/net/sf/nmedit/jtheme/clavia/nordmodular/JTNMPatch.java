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
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JViewport;
import javax.swing.UIDefaults;

import net.sf.nmedit.jpatch.PConnection;
import net.sf.nmedit.jpatch.PConnector;
import net.sf.nmedit.jpatch.PConnectorDescriptor;
import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PPatch;
import net.sf.nmedit.jpatch.PSignal;
import net.sf.nmedit.jpatch.PSignalTypes;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jpatch.clavia.nordmodular.VoiceArea;
import net.sf.nmedit.jpatch.clavia.nordmodular.parser.ParseException;
import net.sf.nmedit.jpatch.event.PConnectionEvent;
import net.sf.nmedit.jpatch.event.PConnectionListener;
import net.sf.nmedit.jsynth.clavia.nordmodular.utils.NmUtils;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.cable.Cable;
import net.sf.nmedit.jtheme.cable.JTCableManager;
import net.sf.nmedit.jtheme.cable.ScrollListener;
import net.sf.nmedit.jtheme.clavia.nordmodular.misc.NMPatchImageExporter;
import net.sf.nmedit.jtheme.component.JTConnector;
import net.sf.nmedit.jtheme.component.JTModule;
import net.sf.nmedit.jtheme.component.JTModuleContainer;
import net.sf.nmedit.jtheme.component.JTPatch;
import net.sf.nmedit.jtheme.store2.ModuleElement;
import net.sf.nmedit.nmutils.Platform;
import net.sf.nmedit.nmutils.graphics.GraphicsToolkit;

public class JTNMPatch extends JTPatch implements Transferable
{

    /**
     * 
     */
    private static final long serialVersionUID = -2196920112843451844L;
    private JTPatchSettingsBar settings;
    private NMPatch patch;
    private ModuleContainerEventHandler ehPoly;
    private ModuleContainerEventHandler ehCommon;
    private JScrollPane spPoly;
    private JScrollPane spCommon;

    public JTNMPatch(JTNM1Context context, NMPatch patch) throws Exception
    {
        super(context);
        this.patch = patch;
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setOneTouchExpandable(true);
        split.setContinuousLayout(false);
        split.setTopComponent(spPoly = createVoiceArea(patch.getPolyVoiceArea()));
        split.setBottomComponent(spCommon = createVoiceArea(patch.getCommonVoiceArea()));
        split.setResizeWeight(1);
        split.setDividerLocation(patch.getHeader().getSeparatorPosition());
        
        settings = new JTPatchSettingsBar(this);
        setLayout(new BorderLayout());
        add(split, BorderLayout.CENTER);
        add(settings, BorderLayout.NORTH);
    }
    
    public NMPatchImageExporter createPatchImageExporter()
    {
        return new NMPatchImageExporter(this);
    }
    
    protected static class ModuleAction extends AbstractAction
    {
        /**
         * 
         */
        private static final long serialVersionUID = -3424440102953404258L;
        public static final String DELETE = "remove";
        public static final String RENAME = "rename";
        public static final String HELP = "help";
        public static final String COLOR = "color";
        
        private JTModule module;
        
        private static ImageIcon[] moduleColors;
        
        private static ImageIcon[] getModuleColors(JTModule module)
        {
            if (moduleColors != null)
                return moduleColors;

            List<ImageIcon> list = new ArrayList<ImageIcon>();
            
            UIDefaults def = module.getContext().getUIDefaults();

            Color defaultColor = def.getColor("module.background$default");
            if (defaultColor != null)
                list.add(GraphicsToolkit.renderColorIcon(defaultColor));
            int i=1;
            while (true)
            {
                Color c = def.getColor("module.background$"+i);
                if (c == null) break;
                list.add(GraphicsToolkit.renderColorIcon(c));
                i++;
            }
            
            moduleColors = list.<ImageIcon>toArray(new ImageIcon[list.size()]);
            
            return moduleColors;
        }
        
        public static JPopupMenu createPopup(JTModule module)
        {
            JPopupMenu popup = new JPopupMenu();
            popup.add(new ModuleAction(RENAME, module));
            popup.addSeparator();
            
            ImageIcon[] colors = getModuleColors(module);
        
            if (colors.length>0)
            {
                JMenu colorMenu = new JMenu("Color");
                popup.add(colorMenu);
                
                for (int i=0;i<colors.length;i++)
                {
                    ModuleAction a = new ModuleAction(COLOR, module);
                    a.putValue(ModuleAction.SMALL_ICON, colors[i]);
                    a.putValue("bg.colorindex", i);
                    if (i == 0)
                        a.putValue(ModuleAction.NAME, "Default Color");
                    else
                        a.putValue(ModuleAction.NAME, "Color "+(i+1));
                    colorMenu.add(a);
                }
                
                popup.addSeparator();
            }
            popup.add(new ModuleAction(HELP, module));
            popup.addSeparator();
            popup.add(new ModuleAction(DELETE, module));
            return popup;
        }
        
        public ModuleAction(String action, JTModule module)
        {
            
            this.module = module;
            putValue(ACTION_COMMAND_KEY, action);

            if (action == DELETE)
            {
                // TODO rely on actionmap see: ModuleAction.ACTION_COMMAND_KEY
                putValue(NAME, "Delete");
            }
            else if (action == RENAME)
            {
                putValue(NAME, "Rename");
                setEnabled(false);
            }
            else if (action == HELP)
            {
                putValue(NAME, "Help");
                setEnabled(false);
            }
            else if (action == COLOR)
            {
                putValue(NAME, "color");
            }
            else setEnabled(false);
        }

        public void actionPerformed(ActionEvent e)
        {
            if (!isEnabled())
                return;
            
            if (module == null)
                return;
            
            String command = e.getActionCommand();
            if (command == DELETE)
                removeModule();
            else if (command == RENAME)
                throw new UnsupportedOperationException("command not supported: "+command);
            else if (command == HELP)
                throw new UnsupportedOperationException("command not supported: "+command);
            else setColor();
        }
        
        private void setColor()
        {
            Integer colorIndex = (Integer) getValue("bg.colorindex");
            if (colorIndex != null)
                setColor(colorIndex.intValue());
        }
        
        private void setColor(int colorIndex)
        {
            for (Component c : module.getParent().getComponents())
            {
                if (c instanceof JTModule)
                {
                    JTModule m = (JTModule) c;
                    if (m.isSelected() || m == module)
                    {
                        PModule pm = m.getModule();
                        if (pm != null)
                        {
                            String t = pm.getTitle();
                            if (t == null)
                                continue;
                            int sep = t.lastIndexOf('$');
                            
                            if (sep>=0)
                                t = t.substring(0, sep);
                            
                            if (colorIndex>0)
                                t += "$"+colorIndex;
                            
                            pm.setTitle(t);
                        }
                    }
                }
            }
        }

        private void removeModule()
        {
            for (Component c : module.getParent().getComponents())
            {
                if (c instanceof JTModule)
                {
                    JTModule m = (JTModule) c;
                    
                    if (m == module)
                    {
                        module = null;
                        removeModule(m);
                    }
                    else if (m.isSelected())
                        removeModule(m);
                }
            }
            
            if (module != null)
                removeModule(module);
        }
        
        private static void removeModule(JTModule m)
        {
            PModule nm = m.getModule();
            
            if (nm != null && nm.getParentComponent() != null)
            {
                nm.getParentComponent().remove(nm);
            }
        }
        
    }

    protected static class ConnectorAction extends AbstractAction
    {
        /**
         * 
         */
        private static final long serialVersionUID = -6831064642989411996L;
        public static final String DISCONNECT = "Disconnect";
        public static final String BREAK = "Break";
        public static final String CAUDIO = "Audio";
        public static final String CCTRL = "Control";
        public static final String CLOGIC = "Logic";
        public static final String CSLAVE = "Slave";
        public static final String CU1 = "User1";
        public static final String CU2 = "User2";
        public static final String DELETE = "Delete";
        
        private JTConnector connector;
        private PSignal signalType;
        
        public static JPopupMenu createPopup(JTConnector connector)
        {
            JPopupMenu popup = new JPopupMenu();
            popup.add(new ConnectorAction(DISCONNECT, connector, null));
            popup.add(new ConnectorAction(BREAK, connector, null));
            popup.addSeparator();
            
            PSignalTypes signalTypes = getSignalTypes(connector);
            
            JMenu colorItem = new JMenu("Color");
            
            popup.add(colorItem);
            
            for (PSignal s: signalTypes)
            {
                colorItem.add(new ConnectorAction(s.getName(), connector, s));
            }
            popup.addSeparator();
            popup.add(new ConnectorAction(DELETE, connector, null));
            return popup;
        }

        protected static PSignalTypes getSignalTypes(JTConnector conui)
        {
            PConnectorDescriptor d = conui.getConnectorDescriptor();
            if (d == null && conui.getConnector() != null)
                d = conui.getConnector().getDescriptor();
            if (d == null) return null;
            return d.getParentDescriptor().getModules().getDefinedSignals();
        }
        
        public ConnectorAction(String action, JTConnector connector, PSignal s)
        {
            this.connector = connector;
            putValue(ACTION_COMMAND_KEY, action);

            putValue(NAME, action);
            this.signalType = s;
            if (action == DISCONNECT)
            {
                setEnabled(connector.isConnected());
            }
            else 
            {
                if (s == null)
                {
                    setEnabled(false);
                }
                else
                {
                    putValue(NAME, s.getName());
                    putValue(SMALL_ICON, renderIcon(s.getColor()));
                }
            }
        }
        
        private ImageIcon renderIcon(Color color)
        {
            BufferedImage bi = new BufferedImage(16,16,BufferedImage.TYPE_INT_RGB);
            
            Graphics2D g2 = bi.createGraphics();
            try
            {
                g2.setColor(color);
                g2.fillRect(0, 0, 16, 16);
                g2.setColor(Color.BLACK);
                g2.drawRect(0, 0, 16-1, 16-1);
            }
            finally
            {
                g2.dispose();
            }
            return new ImageIcon(bi);
        }
/*
        private PSignalType toSignal(String name)
        {
            if (name == CAUDIO) return net.sf.nmedit.jpatch.clavia.nordmodular.Signal.AUDIO;
            else if (name == CCTRL) return net.sf.nmedit.jpatch.clavia.nordmodular.Signal.CONTROL;
            else if (name == CLOGIC) return net.sf.nmedit.jpatch.clavia.nordmodular.Signal.LOGIC;
            else if (name == CSLAVE) return net.sf.nmedit.jpatch.clavia.nordmodular.Signal.SLAVE;
            else if (name == CU1) return net.sf.nmedit.jpatch.clavia.nordmodular.Signal.USER1;
            else if (name == CU2) return net.sf.nmedit.jpatch.clavia.nordmodular.Signal.USER2;
            else return null;
        }*/

        public void actionPerformed(ActionEvent e)
        {
            if (!isEnabled())
                return;
            if (connector == null)
                return;
            
            String command = e.getActionCommand();
            if (command == DISCONNECT)
                disconnectCables();
            else if (command == BREAK)
                breakCables();
            else if (command == DELETE)
                deleteCables();
            else 
            {
                if (signalType != null) setColor(signalType);
            }
        }

        private void setColor(PSignal signal)
        {
            if (connector == null) return;
            PConnector c = connector.getConnector();
            if (c == null) return;
            
            // c.setConnectionColor(signal);
        }
        
        private void deleteCables()
        {
            if (connector == null) return;
            PConnector c = connector.getConnector();
            if (c != null) c.disconnect();
        }

        private void breakCables()
        {
            if (connector == null) return;
            PConnector c = connector.getConnector();
            if (c != null) c.breakConnection();
        }

        private void disconnectCables()
        {
            deleteCables();
        }
    }
    
    public NMPatch getPatch()
    {
        return patch;
    }
    
    public void helpForModule()
    {
        // TODO Auto-generated method stub
        
    }

    private static class ModuleContainerEventHandler extends MouseAdapter
        implements PConnectionListener, ContainerListener
    {
        
        private JTModuleContainer cont;

        public ModuleContainerEventHandler(JTModuleContainer cont)
        {
            this.cont = cont;
            
            for (int i=cont.getComponentCount()-1;i>=0;i--)
            {
                Component c = cont.getComponent(i);
                if (c instanceof JTModule)
                    installPopupListener((JTModule)c);
            }
            cont.addContainerListener(this);
        }
        
        public void uninstall()
        {
            // TODO
            
            cont.getModuleContainer().getConnectionManager().removeConnectionListener(this);
        }
        
        public void adjustModuleColors()
        {
            /* TODO implement coloring algorithm
            for (Cable cable: cont.getCableManager())
            {
                JTConnector a = cable.getSourceComponent();
                JTConnector b = cable.getDestinationComponent();
                
                if (a != null && b != null)
                    adjustColor(a, b);
            } */
        }

        private void adjustColor(JTConnector a, JTConnector b)
        {
            /* TODO implement coloring algorithm
            if (a.isOutput())
            {
                Signal s = b.getSignal();
                if (s != null) a.getParent().setBackground(s.getColor());
            }

            if (b.isOutput())
            {
                Signal s = a.getSignal();
                if (s != null) b.getParent().setBackground(s.getColor());
            }
            */                        
        }

        public void componentAdded(ContainerEvent e)
        {
            Component c = e.getChild();
            if (c instanceof JTModule)
                installPopupListener((JTModule)c);
        }

        public void componentRemoved(ContainerEvent e)
        {
            Component c = e.getChild();
            if (c instanceof JTModule)
                uninstallPopupListener((JTModule)c);
        }
        
        protected void installPopupListener(JTModule m)
        {
            m.addMouseListener(this);
            
            for (int i=m.getComponentCount()-1;i>=0;i--)
            {
                Component c = m.getComponent(i);
                if (c instanceof JTConnector)
                    c.addMouseListener(this);
            }
        }
        
        protected void uninstallPopupListener(JTModule m)
        {
            m.removeMouseListener(this);

            for (int i=m.getComponentCount()-1;i>=0;i--)
            {
                Component c = m.getComponent(i);
                if (c instanceof JTConnector)
                    c.removeMouseListener(this);
            }
        }

        public void mousePressed(MouseEvent e)
        {
            if (!Platform.isPopupTrigger(e)) return;
            
            Component c = e.getComponent();
            if (c instanceof JTModule)
            {
                ModuleAction
                .createPopup((JTModule)c)
                .show(c, e.getX(), e.getY());
            }
            else if (c instanceof JTConnector)
            {
                ConnectorAction
                .createPopup((JTConnector)c)
                .show(c, e.getX(), e.getY());
            }
        }
        
        public void install()
        {
            cont.getModuleContainer().getConnectionManager().addConnectionListener(this);
        }
        
        public void connectionAdded(PConnectionEvent e)
        {
            JTConnector src = findConnector(cont, e.getSource());
            JTConnector dst = findConnector(cont, e.getDestination());

            if (src == null || dst == null) 
                return;
            
            Cable cable = cont.getCableManager().createCable(src, dst);
            
            PSignal signal = e.getSource().getSignalType();
            if (signal != null)
            {
                cable.setColor(signal.getColor());
                adjustColor(src, dst);
            }
            
            cont.getCableManager().add(cable);
        }

        public void connectionRemoved(PConnectionEvent e)
        {
            for (Cable cable : cont.getCableManager())
            {
                if (compareEq(cable.getSource(), cable.getDestination(), e.getSource(), e.getDestination()))
                {
                    cont.getCableManager().remove(cable);
                    break;
                }
            }
        }
        
        private boolean compareEq(PConnector a1, PConnector b1, PConnector a2, PConnector b2)
        {
            return (a1==a2&&b1==b2)||(a1==b2&&b1==a2);
        }
        
        private static JTConnector findConnector(JTModuleContainer cont, PConnector c)
        {
            for (Component component: cont.getComponents())
            {
                if (component instanceof JTModule)
                {
                    JTConnector jtc = findConnector((JTModule) component, c);
                    if (jtc != null)
                        return jtc;
                }
            }
            return null;
        }

        private static JTConnector findConnector(JTModule module, PConnector c)
        {
            for (Component component: module.getComponents())
            {
                if (component instanceof JTConnector)
                {
                    if (((JTConnector)component).getConnector() == c)
                        return (JTConnector) component;
                }
            }
            return null;
        }

    }

    public JTModuleContainer getPolyVoiceArea()
    {
        return (JTModuleContainer) spPoly.getViewport().getView();
    }

    public JTModuleContainer getCommonVoiceArea()
    {
        return (JTModuleContainer) spCommon.getViewport().getView();
    }
    
    private JScrollPane createVoiceArea(VoiceArea va) throws Exception
    {
        JTModuleContainer cont = getContext().createModuleContainer();
        cont.setPatchContainer(this);
        addModuleContainer(cont);
        cont.setModuleContainer(va);
        
        ModuleContainerEventHandler eh = new ModuleContainerEventHandler(cont);
        
        if (va.isPolyVoiceArea()) ehPoly = eh; else ehCommon = eh; 
        
        eh.install();
        
        populateVoiceArea(cont, va);
        eh.adjustModuleColors();
        
        JScrollPane scrollPane = new JScrollPane(cont);
        
        scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        
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
        
        for (PModule module : va)
        {
            ModuleElement mstore = getContext().getStorageContext()
            .getModuleStoreById(module.getComponentId());
            
            if (mstore == null)
                throw new RuntimeException("no store found for "+module);
            
            //mstore.setStaticLayer(null);
            
            JTModule jtmodule = mstore.createModule(context, module);
            
            jtmodule.setLocation( module.getScreenX(), module.getScreenY() );

            width = Math.max(width, jtmodule.getX()+jtmodule.getWidth());
            height = Math.max(height, jtmodule.getY()+jtmodule.getHeight());
            
            cont.add(jtmodule);
            /*
            if (image == null)
            {
                image = jtmodule.renderStaticLayerImage();
                mstore.setStaticLayer(image);
            }
            
            jtmodule.setStaticLayerBackingStore(image);*/
        }
        
        cont.setPreferredSize(new Dimension(width, height));
        cont.setSize(new Dimension(width, height));
        
        JTCableManager cm = cont.getCableManager();
        
        for (PConnection c: va.getConnectionManager())
        {
            JTConnector con1 = cont.findJTConnector(c.getA());
            JTConnector con2 = cont.findJTConnector(c.getB());
            
            if (con1 != null && con2 != null)
            { 
                Cable cable = cm.createCable(con1, con2);
                cable.setColor(c.getA().getSignalType().getColor());
                cm.add(cable);
            }
        }
    }

    public void dispose()
    {
        // TODO
        
        settings.dispose();
        ehPoly.uninstall();
        ehCommon.uninstall();
    }

    // transferable
    

    private static final String charset = "ISO-8859-1";

    public static DataFlavor nmPatchFlavor = 
        new DataFlavor("ppatch/nmpatch", "Nord Modular patch 3.0");
    private static DataFlavor inputStreamFlavor =
        new DataFlavor("text/plain; charset="+charset+"", "Nord Modular patch 3.0");
    private static DataFlavor uriFlavor =
        new DataFlavor("text/uri-list; charset=utf-8", "uri list");
    private static DataFlavor imageFlavor =
        DataFlavor.imageFlavor;
    
    public PPatch newPatchWithModules(JTModule modules[]) {
    	NMPatch nPatch = new NMPatch(patch.getModuleDescriptions());
    	for (JTModule jm : modules) {
    		PModule module = jm.getModule();
    		PModule mod2 = module.cloneModule();
    		nPatch.getCommonVoiceArea().add(mod2);
    		// XXX add cables
    	}
    	return nPatch;
    }

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
    {
        if (uriFlavor.equals(flavor))
        {
            File file = patch.getFile();
            if (file == null)
                throw new UnsupportedFlavorException(flavor);
            return new ByteArrayInputStream(file.toURI().toString().getBytes());
            
        }

        if (inputStreamFlavor.equals(flavor))
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try
            {
                NmUtils.writePatch(patch, out);
            }
            catch (ParseException e)
            {
                throw new IOException(e.getMessage());
            }
            return new ByteArrayInputStream(out.toByteArray());
        }
        
        if (imageFlavor.equals(flavor))
        {
            return createPatchImageExporter().export();
        }
        
        if (nmPatchFlavor.equals(flavor))
        {
            return patch;
        }
        
        throw new UnsupportedFlavorException(flavor);
    }

    public DataFlavor[] getTransferDataFlavors()
    {
        List<DataFlavor> flavorList = new ArrayList<DataFlavor>(2);
        flavorList.add(inputStreamFlavor);
        flavorList.add(imageFlavor);
        if (patch.getFile() != null)
            flavorList.add(uriFlavor);
        flavorList.add(nmPatchFlavor);
        return flavorList.toArray(new DataFlavor[flavorList.size()]);
    }

    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
        for (DataFlavor f: getTransferDataFlavors())
        {
            if (f.equals(flavor))
                return true;
        }
        return false;
    }

    
}

