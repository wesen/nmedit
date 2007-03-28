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
import java.awt.event.ActionEvent;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import net.sf.nmedit.jpatch.Connection;
import net.sf.nmedit.jpatch.Connector;
import net.sf.nmedit.jpatch.LightweightIterator;
import net.sf.nmedit.jpatch.Module;
import net.sf.nmedit.jpatch.Signal;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMConnector;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jpatch.clavia.nordmodular.VoiceArea;
import net.sf.nmedit.jpatch.event.ConnectionEvent;
import net.sf.nmedit.jpatch.event.ConnectionListener;
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
    private JTPatchSettingsBar settings;
    private NMPatch patch;
    private ModuleContainerEventHandler ehPoly;
    private ModuleContainerEventHandler ehCommon;

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
        split.setResizeWeight(1);
        split.setDividerLocation(patch.getHeader().getSeparatorPosition());
        settings = new JTPatchSettingsBar(this);
        setLayout(new BorderLayout());
        add(split, BorderLayout.CENTER);
        add(settings, BorderLayout.NORTH);
    }
    
    protected static class ModuleAction extends AbstractAction
    {
        public static final String REMOVE = "remove";
        public static final String RENAME = "rename";
        public static final String HELP = "help";
        public static final String COLOR_DEFAULT = "color.default";
        public static final String COLOR_R = "Red";
        public static final String COLOR_B = "Blue";
        public static final String COLOR_G = "Green";
        public static final String COLOR_Y = "Yellow";
        
        private JTModule module;
        
        public static JPopupMenu createPopup(JTModule module)
        {
            JPopupMenu popup = new JPopupMenu();
            popup.add(new ModuleAction(REMOVE, module));
            popup.add(new ModuleAction(RENAME, module));
            popup.addSeparator();
            JMenu colorMenu = new JMenu("Color");
            popup.add(colorMenu);
            colorMenu.add(new ModuleAction(COLOR_R, module));
            colorMenu.add(new ModuleAction(COLOR_B, module));
            colorMenu.add(new ModuleAction(COLOR_G, module));
            colorMenu.add(new ModuleAction(COLOR_Y, module));
            
            popup.addSeparator();
            popup.add(new ModuleAction(HELP, module));
            return popup;
        }
        
        public ModuleAction(String action, JTModule module)
        {
            this.module = module;
            putValue(ACTION_COMMAND_KEY, action);

            if (action == REMOVE)
            {
                putValue(NAME, "Remove");
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
            else if (action == COLOR_R || action == COLOR_G || action == COLOR_B || action == COLOR_Y)
            {
                putValue(NAME, action);
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
            if (command == REMOVE)
                removeModule();
            else if (command == RENAME)
                throw new UnsupportedOperationException("command not supported: "+command);
            else if (command == HELP)
                throw new UnsupportedOperationException("command not supported: "+command);
            else setColor(command);
        }
        
        private void setColor(String color)
        {
            Color c = null;
            if (color == COLOR_R) c = net.sf.nmedit.jpatch.clavia.nordmodular.Signal.AUDIO.getColor().brighter().brighter();
            else if (color == COLOR_G) c = Color.CYAN;
            else if (color == COLOR_B) c = net.sf.nmedit.jpatch.clavia.nordmodular.Signal.CONTROL.getColor().brighter();
            else if (color == COLOR_Y) c = net.sf.nmedit.jpatch.clavia.nordmodular.Signal.LOGIC.getColor().brighter().brighter();
            else return;
            if (c != null)
                setColor(c);
        }
        
        private void setColor(Color color)
        {
            for (Component c : module.getParent().getComponents())
            {
                if (c instanceof JTModule)
                {
                    JTModule m = (JTModule) c;
                    if (m.isSelected() || m == module)
                        m.setBackground(color);
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
            Module nm = m.getModule();
            nm.removeConnections();
            nm.getParent().remove(nm);
        }
        
    }

    protected static class ConnectorAction extends AbstractAction
    {
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
        
        public static JPopupMenu createPopup(JTConnector connector)
        {
            JPopupMenu popup = new JPopupMenu();
            popup.add(new ConnectorAction(DISCONNECT, connector));
            popup.add(new ConnectorAction(BREAK, connector));
            popup.addSeparator();
            
            JMenu colorItem = new JMenu("Color");
            
            popup.add(colorItem);
            colorItem.add(new ConnectorAction(CAUDIO, connector));
            colorItem.add(new ConnectorAction(CCTRL, connector));
            colorItem.add(new ConnectorAction(CLOGIC, connector));
            colorItem.add(new ConnectorAction(CSLAVE, connector));
            colorItem.add(new ConnectorAction(CU1, connector));
            colorItem.add(new ConnectorAction(CU2, connector));
            
            popup.addSeparator();
            popup.add(new ConnectorAction(DELETE, connector));
            return popup;
        }
        
        public ConnectorAction(String action, JTConnector connector)
        {
            this.connector = connector;
            putValue(ACTION_COMMAND_KEY, action);

            putValue(NAME, action);
            
            if (action == DISCONNECT);
            else 
            {
                net.sf.nmedit.jpatch.clavia.nordmodular.Signal s = toSignal(action);
                if (s == null)
                {
                    setEnabled(false);
                }
                else
                {
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

        private net.sf.nmedit.jpatch.clavia.nordmodular.Signal toSignal(String name)
        {
            if (name == CAUDIO) return net.sf.nmedit.jpatch.clavia.nordmodular.Signal.AUDIO;
            else if (name == CCTRL) return net.sf.nmedit.jpatch.clavia.nordmodular.Signal.CONTROL;
            else if (name == CLOGIC) return net.sf.nmedit.jpatch.clavia.nordmodular.Signal.LOGIC;
            else if (name == CSLAVE) return net.sf.nmedit.jpatch.clavia.nordmodular.Signal.SLAVE;
            else if (name == CU1) return net.sf.nmedit.jpatch.clavia.nordmodular.Signal.USER1;
            else if (name == CU2) return net.sf.nmedit.jpatch.clavia.nordmodular.Signal.USER2;
            else return null;
        }

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
                net.sf.nmedit.jpatch.clavia.nordmodular.Signal s = toSignal(command);
                if (s != null)
                    setColor(s);
            }
        }

        private void setColor(net.sf.nmedit.jpatch.clavia.nordmodular.Signal signal)
        {
            if (connector == null) return;
            NMConnector c = (NMConnector) connector.getConnector();
            if (c == null) return;
            c.setConnectionColor(signal);
        }
        
        private void deleteCables()
        {
            if (connector == null) return;
            Connector c = connector.getConnector();
            if (c != null) c.disconnect();
        }

        private void breakCables()
        {
            if (connector == null) return;
            Connector c = connector.getConnector();
            if (c != null) ((NMConnector)c).breakCable();
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
        implements ConnectionListener, ContainerListener
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
            if (!SwingUtilities.isRightMouseButton(e)) return;
            
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
        
        public void connectionAdded(ConnectionEvent e)
        {
            JTConnector src = findConnector(cont, e.getSource());
            JTConnector dst = findConnector(cont, e.getDestination());

            if (src == null || dst == null) 
                return;
            
            Cable cable = cont.getCableManager().createCable(src, dst);
            
            Signal signal = e.getSource().getSignal();
            if (signal != null)
            {
                cable.setColor(signal.getColor());
                adjustColor(src, dst);
            }
            
            cont.getCableManager().add(cable);
            cont.getCableManager().notifyRepaintManager();
        }

        public void connectionRemoved(ConnectionEvent e)
        {
            for (Cable cable : cont.getCableManager())
            {
                if (compareEq(cable.getSource(), cable.getDestination(), e.getSource(), e.getDestination()))
                {
                    cont.getCableManager().remove(cable);
                    cont.getCableManager().notifyRepaintManager();
                    break;
                }
            }
        }
        
        private boolean compareEq(Connector a1, Connector b1, Connector a2, Connector b2)
        {
            return (a1==a2&&b1==b2)||(a1==b2&&b1==a2);
        }
        
        private static JTConnector findConnector(JTModuleContainer cont, Connector c)
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

        private static JTConnector findConnector(JTModule module, Connector c)
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
    
    private Component createVoiceArea(VoiceArea va) throws Exception
    {
        JTModuleContainer cont = getContext().createModuleContainer();
        cont.setPatchContainer(this);
        cont.setModuleContainer(va);
        
        ModuleContainerEventHandler eh = new ModuleContainerEventHandler(cont);
        
        if (va.isPolyVoiceArea()) ehPoly = eh; else ehCommon = eh; 
        
        eh.install();
        
        populateVoiceArea(cont, va);
        eh.adjustModuleColors();
        
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
            
            //Image image = mstore.getStaticLayer();
            mstore.setStaticLayer(null);
            
            JTModule jtmodule =
            mstore.createModule(context, module, true);
            
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

    public void dispose()
    {
        // TODO
        
        settings.dispose();
        ehPoly.uninstall();
        ehCommon.uninstall();
    }

}

