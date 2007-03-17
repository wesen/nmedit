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
 * Created on Nov 1, 2006
 */
package net.sf.nmedit.jsynth.nomad;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import net.sf.nmedit.jsynth.Plug;
import net.sf.nmedit.jsynth.Port;
import net.sf.nmedit.jsynth.Slot;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.Synthesizer;
import net.sf.nmedit.jsynth.event.PortAttachmentEvent;
import net.sf.nmedit.jsynth.event.PortAttachmentListener;
import net.sf.nmedit.jsynth.event.SlotEvent;
import net.sf.nmedit.jsynth.event.SlotListener;
import net.sf.nmedit.jsynth.event.SlotManagerListener;
import net.sf.nmedit.jsynth.event.SynthesizerEvent;
import net.sf.nmedit.jsynth.event.SynthesizerStateListener;
import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.menulayout.MLEntry;
import net.sf.nmedit.nomad.core.menulayout.MenuBuilder;
import net.sf.nmedit.nomad.core.swing.explorer.ContainerNode;
import net.sf.nmedit.nomad.core.swing.explorer.ExplorerTree;
import net.sf.nmedit.nomad.core.swing.explorer.LeafNode;
import net.sf.nmedit.nomad.core.swing.explorer.TreeContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SynthDeviceContext extends ContainerNode
    implements TreeContext, SynthesizerStateListener,
    SlotManagerListener
{
    
    public static enum State
    {
        RUNNING,
        STOPPED,
        WARNINGS
    };
    
    private static Icon getIcon(String name)
    {
        URL location =
            SynthDeviceContext.class.getResource(name);
     
        return new ImageIcon(location);
    }
    
    private static final Icon iconRunning = getIcon("synth-st-running.png");
    private static final Icon iconStopped = getIcon("synth-st-stopped.png");
    private static final Icon iconWarnings = getIcon("synth-st-warnings.png");

    private ExplorerTree etree;
    private State state = State.STOPPED;
    private Synthesizer synth;
    private boolean warn = false;
    
    private ContainerNode slotsRoot;
    private ContainerNode portsRoot;
    private EventHandler eventHandler;

    public SynthDeviceContext(ExplorerTree etree, String title)
    {
        super(etree.getRoot(), title);
        this.etree = etree;
        setIcon(iconStopped);
        portsRoot = new ContainerNode(this, "Ports");
        slotsRoot = new ContainerNode(this, "Slots");
        
        addChild(portsRoot);
        
        eventHandler = createEventHandler();
        eventHandler.install();
    }
    
    public ExplorerTree getTree()
    {
        return etree;
    }
    
    protected EventHandler createEventHandler()
    {
        return new EventHandler(this);
    }

    public Synthesizer getSynth()
    {
        return synth;
    }
    
    protected void setSynth(Synthesizer synth)
    {
        Synthesizer oldValue = this.synth;
        Synthesizer newValue = synth;
        if (oldValue != newValue)
        {
            if (this.synth != null)
                uninstall();
            
            this.synth = synth;
            
            if (synth != null)
                install();
    
            eventHandler.synthChanged(oldValue, newValue);
            
            updateState();
        }
    }

    private void install()
    {
        synth.addSynthesizerStateListener(this);
        synth.getSlotManager().addSlotManagerListener(this);

        for (int i=0;i<synth.getSlotCount();i++)
            installSlot(synth.getSlot(i));
        
        portsRoot.clear();
        
        for (int i=0;i<synth.getPortCount();i++)
        {
            Port port = synth.getPort(i);
            PortLeaf leaf = new PortLeaf(portsRoot, port);
            portsRoot.addChild(leaf);
        }
        
    }
    
    private void uninstall()
    {
        synth.removeSynthesizerStateListener(this);
        synth.getSlotManager().removeSlotManagerListener(this);
        
        for (int i=synth.getSlotCount()-1;i>=0;i--)
            uninstallSlot(synth.getSlot(i));

        for (int i=portsRoot.getChildCount()-1;i>=0;i--)
            ((PortLeaf)portsRoot.getChildAt(i)).uninstall();        
        portsRoot.clear();
    }
    
    public boolean isWarningFlagSet()
    {
        return warn;
    }
    
    public void setWarningFlag(boolean warn)
    {
        if (this.warn != warn)
        {
            this.warn = warn;
            
            etree.fireNodeChanged(this);
        }
    }

    protected void updateState()
    {
        if (synth == null)
        {
            setState(State.STOPPED);
            return;
        }
        
        if (warn)
        {
            setState(State.WARNINGS);
            return;
        }   

        setState( synth.isConnected() ? State.RUNNING : State.STOPPED );
    }

    public void synthConnectionStateChanged(SynthesizerEvent e)
    {
        updateState();
    }

    public State getState()
    {
        return state;
    }
    
    public void setState(State state)
    {
        if (state == null)
            throw new NullPointerException();
        
        if (!this.state.equals(state))
        {
            this.state = state;
            
            switch (state)
            {
                case RUNNING:
                    setIcon(iconRunning);
                    break;
                case STOPPED:
                    setIcon(iconStopped);
                    break;
                case WARNINGS:
                    setIcon(iconWarnings);
                    break;
                default:
                    setIcon(iconStopped);
                    break;
            }

            etree.fireNodeChanged(this);
        }
    }
    
    public void setTitle(String title)
    {
        String oldValue = super.getTitle();
        String newValue = title;
        
        if (oldValue != newValue && (newValue != null && (!newValue.equals(oldValue))))
        {
            super.setTitle(title);
            etree.fireNodeChanged(this);
        }
    }
    
    public void processEvent(Event event)
    {
        // TODO there have to be different types of events
        
        TreePath path = etree.getSelectionPath();
        if (path != null)
        {
            Object last = path.getLastPathComponent();
            if (last instanceof TreeNode)
            {
                processEvent(event, path, (TreeNode) last);
            }
        }
        
    }

    protected void processEvent(Event event, TreePath path, TreeNode node)
    {
        if (node instanceof SlotLeaf && slotsRoot.contains(node))
        {
            SlotLeaf s = (SlotLeaf) node;
            processEvent(event, s.getSlot());
        }
    }

    protected void processEvent(Event event, Slot slot)
    {
        // TODO 
    }

    public void slotAdded(SlotEvent e)
    {
        installSlot(e.getSlot());
    }

    public void slotRemoved(SlotEvent e)
    {
        uninstallSlot(e.getSlot());
    }

    private void uninstallSlot(Slot slot)
    {
        for (int i=slotsRoot.getChildCount()-1;i>=0;i--)
        {
            SlotLeaf leaf = (SlotLeaf) slotsRoot.getChildAt(i);
            if (leaf.getSlot() == slot)
            {
                slotsRoot.remove(i);
                
                leaf.uninstall();
                
                break;
            }
        }
        
        if (slotsRoot.isLeaf())
            remove(slotsRoot);
        
        etree.fireNodeStructureChanged(this);
    }
    
    private void installSlot(Slot slot)
    {
        SlotLeaf leaf = new SlotLeaf(slotsRoot, slot);
        
        if (slotsRoot.isLeaf())
            addChild(slotsRoot);
        
        slotsRoot.addChild(leaf);

        etree.fireNodeStructureChanged(leaf);
        etree.fireNodeStructureChanged(this);
    }
    
    private class PortLeaf extends LeafNode implements PortAttachmentListener 
    {
        private Port port;

        public PortLeaf(TreeNode parent, Port port)
        {
            super(parent);
            this.port = port;
            updatePortText();
            install();
        }
        
        private void install()
        {
            port.addPortAttachmentListener(this);
        }
        
        public void uninstall()
        {
            port.removePortAttachmentListener(this);
        }

        private void updatePortText()
        {
            Plug plug = port.getPlug();
            if (plug == null)
                setText(port.getName()+": ?");
            else
                setText(port.getName()+": "+plug.getName());

            etree.fireNodeChanged(this);
        }

        public void plugAttachmentChanged(PortAttachmentEvent e)
        {
            updatePortText();
        }
        
    }
    
    private class SlotLeaf extends LeafNode implements SlotListener
    {

        private Slot slot;

        public SlotLeaf(TreeNode parent, Slot slot)
        {
            super(parent);
            this.slot = slot;
            install();
            updateSlotText();
        }
        
        public void install()
        {
            slot.addSlotListener(this);
        }
        
        public void uninstall()
        {
            slot.removeSlotListener(this);
        }

        public Slot getSlot()
        {
            return slot;
        }

        public void newPatchInSlot(SlotEvent e)
        {
            updateSlotText();
        }

        private void updateSlotText()
        {
            String patch = slot.getPatchName();
            if (patch == null)
                patch = "?";
            setText(patch+" ("+slot.getName()+")");
            
            etree.fireNodeChanged(this);
        }
        
    }

    public static final String EXPLORER_SYNTH_KEY = "nomad.explorer.synth";
    public static final String CONNECT_KEY = EXPLORER_SYNTH_KEY+".control.connect";
    public static final String DISCONNECT_KEY = EXPLORER_SYNTH_KEY+".control.disconnect";
    public static final String SETTINGS_KEY = EXPLORER_SYNTH_KEY+".settings";
    
    private static class EventHandler implements MouseListener,
      SynthesizerStateListener, ActionListener
    {

        protected SynthDeviceContext context;
        private boolean installed = false;
        private transient JPopupMenu popup;
        private MenuBuilder menuBuilder;

        public EventHandler(SynthDeviceContext context)
        {
            this.context = context;
        }
        
        public void synthChanged(Synthesizer oldValue, Synthesizer newValue)
        {
            if (oldValue != null)
                uninstall(oldValue);
            if (newValue != null)
                install(newValue);
        }

        private void install(Synthesizer synth)
        {
            synth.addSynthesizerStateListener(this);
            updateMenu();
        }

        private void uninstall(Synthesizer synth)
        {
            synth.removeSynthesizerStateListener(this);
            updateMenu();
        }

        public void install()
        {
            if (installed) return;
            
            ExplorerTree tree = context.getTree();
            
            tree.addMouseListener(this);
        }
        
        public void uninstall()
        {
            if (!installed) return;   
            ExplorerTree tree = context.getTree();
            
            tree.removeMouseListener(this);
            
            Synthesizer synth = context.getSynth();
            if (synth != null)
                uninstall(synth);
        }
        
        protected boolean isMouseOver(TreeNode node, int x, int y)
        {
            ExplorerTree tree = context.getTree();
            TreePath path = tree.getPathForLocation(x, y);
            
            if (path == null)
                return false;
            
            return node == path.getLastPathComponent();
        }

        protected boolean isMouseOver(TreeNode node, MouseEvent e)
        {
            return isMouseOver(node, e.getX(), e.getY());
        }
        
        protected JPopupMenu getPopUp()
        {
            if (popup != null)
                return popup;
            
            if (menuBuilder == null)
            {
                // we have to clone the menu items because we need different
                // instances for each synth context
                menuBuilder = Nomad.sharedInstance().getMenuBuilder()
                    .getClonedTree("nomad.explorer.synth");

                installActionCommand(CONNECT_KEY);
                installActionCommand(DISCONNECT_KEY);
                installActionCommand(SETTINGS_KEY);
                
                installMenuActionListeners();
                
                updateMenu();
            }
            popup = menuBuilder.createPopup(EXPLORER_SYNTH_KEY);
            return popup;
        }
        
        private void installActionCommand(String entryPoint)
        {
            MLEntry e = menuBuilder.getEntry(entryPoint);
            if (e == null)
                return ;
            
            e.putValue(MLEntry.ACTION_COMMAND_KEY, e.getGlobalEntryPoint());
        }
        
        public void mouseClicked(MouseEvent e)
        {
            if (SwingUtilities.isRightMouseButton(e)
                    && isMouseOver(context, e))
            {
                // create & show popup menu
         
                JPopupMenu menu = getPopUp();
                if (menu == null)
                    return;
                
                menu.show(context.getTree(), e.getX(), e.getY());
            }
        }

        public void mouseEntered(MouseEvent e)
        {
            // TODO Auto-generated method stub
            
        }

        public void mouseExited(MouseEvent e)
        {
            // TODO Auto-generated method stub
            
        }

        public void mousePressed(MouseEvent e)
        {
            // TODO Auto-generated method stub
            
        }

        public void mouseReleased(MouseEvent e)
        {
            // TODO Auto-generated method stub
            
        }

        public void synthConnectionStateChanged(SynthesizerEvent e)
        {
            updateMenu();
        }

        public void updateMenu()
        {
            if (menuBuilder == null)
                return;
            
            Synthesizer synth = context.getSynth();
            boolean exists = synth != null;
            boolean connected = exists ? synth.isConnected() : false;

            menuBuilder.getEntry(CONNECT_KEY).setEnabled(exists && (!connected));
            menuBuilder.getEntry(DISCONNECT_KEY).setEnabled(connected);
            menuBuilder.getEntry(SETTINGS_KEY).setEnabled(exists);
        }

        public void installMenuActionListeners()
        {
            menuBuilder.getEntry(CONNECT_KEY).addActionListener(this);
            menuBuilder.getEntry(DISCONNECT_KEY).addActionListener(this);
            menuBuilder.getEntry(SETTINGS_KEY).addActionListener(this);
        }

        public void actionPerformed(ActionEvent e)
        {
            SwingUtilities.invokeLater(new DelayedAction(context, e.getActionCommand()));
        }

    }

    private static class DelayedAction implements Runnable
    {
        
        private SynthDeviceContext context;
        private String actionCommand;

        public DelayedAction(SynthDeviceContext context, String actionCommand)
        {
            this.context = context;
            this.actionCommand = actionCommand;
        }

        public void run()
        {
            try
            {
                if (CONNECT_KEY.equals(actionCommand))
                {
                    Synthesizer synth = context.getSynth();
                    if (synth != null) context.connect();
                    return;
                }
                if (DISCONNECT_KEY.equals(actionCommand))
                {
                    Synthesizer synth = context.getSynth();
                    if (synth != null) synth.setConnected(false);
                    return;
                }
                if (SETTINGS_KEY.equals(actionCommand))
                {
                    Synthesizer synth = context.getSynth();
                    if (synth != null) context.showSettings();
                    return;
                }
            }
            catch (Exception e)
            {
                
                Log log = LogFactory.getLog(SynthDeviceContext.class);
                if (log.isWarnEnabled())
                    log.warn("error while performing action", e);
                
                showError(e);
            }
        }
    
    }

    protected void connect()
    {
        boolean plugsNotConfigured = false;
        for (Port port: synth.getPorts())
        {
            if (port.getPlug() == null)
            {
                plugsNotConfigured = true;
                break;
            }
        }
        
        boolean connect = true;
        
        if (plugsNotConfigured)
            connect = showSettings();
        
        if (connect)
        {
            try
            {
                synth.setConnected(true);
            }
            catch (SynthException e)
            {
                Log log = LogFactory.getLog(SynthDeviceContext.class);
                if (log.isWarnEnabled())
                {
                    log.warn("trying to connect to "+synth, e);
                }
            }
        }
    }
    
    protected boolean showSettings()
    {
        throw new UnsupportedOperationException();
        
    }
    
    private static void showError(Exception e)
    {
        JOptionPane.showMessageDialog(Nomad.sharedInstance().getWindow(),
                "Warning: "+e.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
    }
    
    public static void createMidiDialog()
    {/*
        JOptionPane pan = new JOptionPane();
        pan.*/
    }
    
}
