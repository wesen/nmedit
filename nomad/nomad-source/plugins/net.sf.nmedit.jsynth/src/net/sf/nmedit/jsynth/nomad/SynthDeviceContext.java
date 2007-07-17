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

import java.awt.Dimension;
import java.awt.Event;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import net.sf.nmedit.jsynth.Bank;
import net.sf.nmedit.jsynth.Plug;
import net.sf.nmedit.jsynth.Port;
import net.sf.nmedit.jsynth.Slot;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.Synthesizer;
import net.sf.nmedit.jsynth.event.BankUpdateEvent;
import net.sf.nmedit.jsynth.event.BankUpdateListener;
import net.sf.nmedit.jsynth.event.PortAttachmentEvent;
import net.sf.nmedit.jsynth.event.PortAttachmentListener;
import net.sf.nmedit.jsynth.event.SlotEvent;
import net.sf.nmedit.jsynth.event.SlotListener;
import net.sf.nmedit.jsynth.event.SlotManagerListener;
import net.sf.nmedit.jsynth.event.SynthesizerEvent;
import net.sf.nmedit.jsynth.event.SynthesizerStateListener;
import net.sf.nmedit.jsynth.worker.PatchLocation;
import net.sf.nmedit.jsynth.worker.StorePatchWorker;
import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.forms.ExceptionDialog;
import net.sf.nmedit.nomad.core.menulayout.MLEntry;
import net.sf.nmedit.nomad.core.menulayout.MenuBuilder;
import net.sf.nmedit.nomad.core.swing.explorer.ContainerNode;
import net.sf.nmedit.nomad.core.swing.explorer.EventDispatcher;
import net.sf.nmedit.nomad.core.swing.explorer.ExplorerTree;
import net.sf.nmedit.nomad.core.swing.explorer.LeafNode;
import net.sf.nmedit.nomad.core.swing.explorer.TreeContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SynthDeviceContext extends ContainerNode
    implements TreeContext, SynthesizerStateListener,
    SlotManagerListener, PropertyChangeListener
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

    protected ExplorerTree etree;
    private State state = State.STOPPED;
    private Synthesizer synth;
    private boolean warn = false;
    
    protected ContainerNode slotsRoot;
    protected ContainerNode portsRoot;
    protected ContainerNode banksRoot;
    private EventHandler eventHandler;

    public SynthDeviceContext(ExplorerTree etree, String title)
    {
        super(etree.getRoot(), title);
        this.etree = etree;
        setIcon(iconStopped);
        portsRoot = new ContainerNode(this, "Ports");
        slotsRoot = new ContainerNode(this, "Slots");
        banksRoot = new ContainerNode(this, "Banks");
        
        addChild(portsRoot);
        
        eventHandler = createEventHandler();
        eventHandler.install();
    }

    public void removeContext()
    {
        eventHandler.uninstall();
        if (synth != null)
        {
            try
            {
                synth.setConnected(false);
            }
            catch (SynthException e)
            {
                //e.printStackTrace();
            }
        }
        if (etree.getRoot().getIndex(this)>=0)
        {
            etree.getRoot().remove(this);
            etree.fireRootChanged();
        }
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
        synth.addPropertyChangeListener("name", this);
        synth.getSlotManager().addSlotManagerListener(this);
        
        for (int i=0;i<synth.getSlotCount();i++)
            installSlot(synth.getSlot(i));

        installBanks();
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
        synth.removePropertyChangeListener(this);
        synth.removeSynthesizerStateListener(this);
        synth.getSlotManager().removeSlotManagerListener(this);
        
        for (int i=synth.getSlotCount()-1;i>=0;i--)
        {
            uninstallSlot(synth.getSlot(i));
        }

        uninstallBanks();
        
        for (int i=portsRoot.getChildCount()-1;i>=0;i--)
            ((PortLeaf)portsRoot.getChildAt(i)).uninstall();        
        portsRoot.clear();
    }
    
    private void installBanks()
    {
        banksRoot.clear();
        for (int i=0;i<synth.getBankCount();i++)
        {
            banksRoot.addChild(new BankLeaf(banksRoot, synth.getBank(i)));
        }
    }

    private void uninstallBanks()
    {
        for (int i=0;i<banksRoot.getChildCount();i++)
        {
            BankLeaf l = (BankLeaf) banksRoot.getChildAt(i);
            l.uninstall();
        }
        banksRoot.clear();

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
        
        if (synth.isConnected())
        {
            installBanks();
            addChild(banksRoot);
        }
        else
        {
            uninstallBanks();
            remove(banksRoot);   
        }
        etree.fireNodeStructureChanged(this);
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
        else if (node instanceof LeafNode && banksRoot.contains(node.getParent()))
        {
            Bank<? extends Synthesizer> b = ((BankLeaf)node.getParent()).bank;
            processEvent(event, b, node.getParent().getIndex(node));
        }
    }

    protected void processEvent(Event event, 
            Bank<? extends Synthesizer> bank, 
            int index)
    {
        // no op

        List<Synthesizer> list = new LinkedList<Synthesizer>();
        list.add(bank.getSynthesizer());
        
        SaveInSynthDialog ssd = new SaveInSynthDialog(list);
        ssd.setTitle("Save In Slot");
        ssd.setSaveInBankAllowed(false);
        ssd.invoke();
        
        if (!ssd.isSaveOption())
            return;

        Slot dst = ssd.getSelectedSlot();
    
        if (dst == null)
            return;
        
        StorePatchWorker w = synth.createStorePatchWorker();
        w.setSource(new PatchLocation(bank.getBankIndex(), index));
        w.setDestination(new PatchLocation(dst.getSlotIndex()));
        try
        {
            w.store();
        }
        catch (SynthException e)
        {
            e.printStackTrace();
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

    private class BankLeaf extends ContainerNode implements BankUpdateListener
    {

        private Bank<? extends Synthesizer> bank;
        private boolean dropped = true;

        public BankLeaf(TreeNode parent, Bank<? extends Synthesizer> bank)
        {
            super(parent, bank.getName());
            this.bank = bank;
            for (int i=0;i<bank.getPatchCount();i++)
                addChild(new LeafNode(this, "?"));
            bank.addBankUpdateListener(this);
        }

        public TreeNode getChildAt(int childIndex)
        {
            if (dropped)
            {
                if (synth.isConnected())
                {
                    bank.update();
                    dropped = false;
                }
            }
            return super.getChildAt(childIndex);
        }

        public void notifyDropChildren()
        {
            dropped = true;
            super.notifyDropChildren();
        }
        
        public void uninstall()
        {
            bank.removeBankUpdateListener(this);
        }

        public void bankUpdated(BankUpdateEvent e)
        {
            if (dropped) return;
            for (int i=e.getBeginIndex();i<e.getEndIndex();i++)
            {
                LeafNode l = (LeafNode) getChildAt(i);
                String name;
                if (!bank.isPatchInfoAvailable(i))
                    name = "?";
                else
                if (!bank.containsPatch(i))
                    name = "<empty>";
                else 
                    name = bank.getPatchName(i);
                l.setText(name);
            }
            etree.fireNodeStructureChanged(this);
        }
        
    }
    
    private class PortLeaf extends LeafNode implements PortAttachmentListener 
    {
        private Port port;
        private transient String tooltip;

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

        public void update()
        {
            tooltip = null;
            updatePortText();
        }

        public void plugAttachmentChanged(PortAttachmentEvent e)
        {
            update();
        }
        
        public String getToolTipText()
        {
            if (tooltip == null)
            {
                Plug p = port.getPlug();

                tooltip = "<html><body>";
                tooltip += "<b>"+port.getName()+"</b><br />";
                if (p != null)
                {
                    tooltip += "<table>";
                    tooltip += "<tr><td>name</td><td>"+p.getName()+"</td></tr>";
                    tooltip += "<tr><td>description</td><td>"+p.getDescription()+"</td></tr>";
                    tooltip += "<tr><td>version</td><td>"+p.getVersion()+"</td></tr>";
                    tooltip += "<tr><td>vendor</td><td>"+p.getVendor()+"</td></tr>";
                    tooltip += "</table>";
                }
                tooltip += "</body></html>";
            }
            
            return tooltip;
        }
        
    }
    
    private String getTitleFor(Slot slot)
    {
        String patch = slot.getPatchName();
        if (patch == null)
            patch = "?";
        return patch+" ("+slot.getName()+")";
    }
    
    protected class SlotLeaf extends LeafNode implements SlotListener
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
            setText(getTitleFor(slot));
            etree.fireNodeChanged(this);
        }
        
    }

    protected ContainerNode getSlotNodeContainer()
    {
        return slotsRoot;
    }

    protected ContainerNode getPortNodeContainer()
    {
        return portsRoot;
    }
    
    public static final String EXPLORER_SYNTH_KEY = "nomad.explorer.synth";
    public static final String CONNECT_KEY = EXPLORER_SYNTH_KEY+".control.connect";
    public static final String DISCONNECT_KEY = EXPLORER_SYNTH_KEY+".control.disconnect";
    public static final String SETTINGS_KEY = EXPLORER_SYNTH_KEY+".settings";
    public static final String REMOVE_KEY = EXPLORER_SYNTH_KEY+".general.remove";
    

    public void processEvent(MouseEvent e)
    {
        if (eventHandler!=null)
            EventDispatcher.dispatchEvent(eventHandler, e);
    }

    protected static class EventHandler implements MouseListener,
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

        protected ContainerNode getSlotNodeContainer()
        {
            return context.slotsRoot;
        }

        protected ContainerNode getPortNodeContainer()
        {
            return context.portsRoot;
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
        }
        
        public void uninstall()
        {
            if (!installed) return;   
            //ExplorerTree tree = context.getTree();
            
            Synthesizer synth = context.getSynth();
            if (synth != null)
                uninstall(synth);
        }

        public JPopupMenu getSlotPopup(SlotLeaf leaf)
        {
            return null;
        }

        public JPopupMenu getPortPopup(PortLeaf leaf)
        {
            return null;
        }

        protected JPopupMenu getSynthNodePopup()
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
                installActionCommand(REMOVE_KEY);
                
                installMenuActionListeners();
                
                updateMenu();
                menuBuilder.getEntry(REMOVE_KEY).setEnabled(true);
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
            TreePath path = context.getTree().getSelectionPath();
            if (path == null)
                return;
            
            Object o = path.getLastPathComponent();
            if (o instanceof TreeNode)
            {
                TreeNode node = (TreeNode) o;
                if (SwingUtilities.isRightMouseButton(e))
                    context.showContextMenu(e, node);
            }
        }

        public void mouseReleased(MouseEvent e)
        {
            // TODO Auto-generated method stub
            
        }

        public void synthConnectionStateChanged(SynthesizerEvent e)
        {
            updateMenu();
            
            ContainerNode cn = context.portsRoot;
            for (int i=cn.getChildCount()-1;i>=0;i--)
                ((PortLeaf)cn.getChildAt(i)).update();
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
            menuBuilder.getEntry(REMOVE_KEY).addActionListener(this);
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
                if (REMOVE_KEY.equals(actionCommand))
                {
                    context.removeContext();
                    return;
                }
            }
            catch (Exception e)
            {
                
                Log log = LogFactory.getLog(SynthDeviceContext.class);
                if (log.isWarnEnabled())
                    log.warn("error while performing action", e);
                
                showError(context.etree, e);
            }
        }
    
    }

    protected boolean arePlugsConfigured()
    {
        for (Port port: synth.getPorts())
        {
            if (port.getPlug() == null)
            {
                return false;
            }
        }
        return true;
    }
    
    protected void connect()
    {
        boolean connect = true;
        
        if (!arePlugsConfigured())
            connect = showSettings();
        
        if (connect)
        {
            if (!arePlugsConfigured())
                return;
            
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
                ExceptionDialog.showErrorDialog(etree, e,
                        synth.getDeviceName()+", connect"
                        , e);
            }
        }
    }
    
    protected void showContextMenu(MouseEvent e, TreeNode node)
    {
        JPopupMenu popup = null;
        if (node == this)
        {
            popup = eventHandler.getSynthNodePopup();
        }
        else if (slotsRoot.contains(node) && node instanceof SlotLeaf)
        {
            popup = eventHandler.getSlotPopup((SlotLeaf) node);
        }
        else if (portsRoot.contains(node) && node instanceof PortLeaf)
        {
            popup = eventHandler.getPortPopup((PortLeaf) node);
        }
        
        if (popup != null)
        {
            popup.show(getTree(), e.getX(), e.getY());
        }
    }

    protected boolean showSettings()
    {
        final SynthPropertiesDialog spd = new SynthPropertiesDialog(synth);
        
        addForms(spd);
        spd.setSelectedPath("connection");
        
        final JDialog d = new JDialog(Nomad.sharedInstance().getWindow(), 
                "Properties for "+synth.getDeviceName())
        {
            {
                enableEvents(WindowEvent.WINDOW_EVENT_MASK);
            }
            protected void processWindowEvent(WindowEvent e)
            {
                if (e.getID()==WindowEvent.WINDOW_CLOSED)
                {
                    spd.dispose();
                }
                super.processWindowEvent(e);
            }
        };
        d.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ds = new Dimension(ss.width/2, ss.height*2/5);
        
        d.getContentPane().setLayout(new BorderLayout());
        d.getContentPane().add(spd);
        d.setBounds((ss.width-ds.width)/2, (ss.height-ds.height)/2, ds.width, ds.height);
      
        spd.addButton(new AbstractAction(){
            /**
             * 
             */
            private static final long serialVersionUID = 6856066575494473810L;
            {
                putValue(NAME, "Close");
            }
            public void actionPerformed(ActionEvent e)
            {
                d.setVisible(false);
                d.dispose();
            }});
        d.setVisible(true);
        
        return true;
    }
    
    
    protected void addForms(SynthPropertiesDialog spd)
    {
        spd.addSynthInfo();
        spd.addPortSettings();
        spd.addSynthSettings();
    }
    
    private static void showError(JComponent parent, Exception e)
    {
        ExceptionDialog.showErrorDialog(parent, "Warning:"+e.getMessage(), "Warning", e);
    }
    
    public static void createMidiDialog()
    {/*
        JOptionPane pan = new JOptionPane();
        pan.*/
    }

    public void propertyChange(PropertyChangeEvent evt)
    {
        if (evt.getSource() == synth)
        {
            if ("name".equals(evt.getPropertyName()))
            {
                String name = synth.getName();
                String dev = synth.getDeviceName();
                setTitle(name.equals(dev) ? name : (name+" ("+dev+")"));
                etree.fireNodeChanged(this);
            }
        }
    }
    
}
