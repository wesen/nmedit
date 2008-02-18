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
package net.sf.nmedit.jsynth.nomad;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import net.sf.nmedit.jsynth.Bank;
import net.sf.nmedit.jsynth.Port;
import net.sf.nmedit.jsynth.Slot;
import net.sf.nmedit.jsynth.SynthException;
import net.sf.nmedit.jsynth.Synthesizer;
import net.sf.nmedit.jsynth.event.BankUpdateEvent;
import net.sf.nmedit.jsynth.event.BankUpdateListener;
import net.sf.nmedit.jsynth.event.SlotEvent;
import net.sf.nmedit.jsynth.event.SlotListener;
import net.sf.nmedit.jsynth.event.SynthesizerEvent;
import net.sf.nmedit.jsynth.event.SynthesizerStateListener;
import net.sf.nmedit.jsynth.worker.PatchLocation;
import net.sf.nmedit.jsynth.worker.StorePatchWorker;
import net.sf.nmedit.nmutils.Platform;
import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.forms.ExceptionDialog;
import net.sf.nmedit.nomad.core.swing.explorer.ContainerNode;
import net.sf.nmedit.nomad.core.swing.explorer.ExplorerTree;
import net.sf.nmedit.nomad.core.swing.explorer.LeafNode;
import net.sf.nmedit.nomad.core.swing.explorer.RootNode;


public class SynthObjectForm<S extends Synthesizer> extends JPanel
{
    
    private S synth;
    private List<SlotObject<S>> slotObjects = new ArrayList<SlotObject<S>>();
    
    public SynthObjectForm(S synth)
    {
        this.synth = synth;
        create();
        
        (createEventHandler()).install();
    }
    
    protected EventHandler createEventHandler()
    {
        return new EventHandler();
    }
    
    protected class EventHandler implements SynthesizerStateListener, PropertyChangeListener
    {

        public void install()
        {
            synth.addSynthesizerStateListener(this);
            synth.addPropertyChangeListener(this);
        }

        public void synthConnectionStateChanged(SynthesizerEvent e)
        {
            connectionStateChanged(synth.isConnected());
        }

        public void propertyChange(PropertyChangeEvent evt)
        {
            if ("icon".equals(evt.getPropertyName()))
            {
                Icon icon = (Icon) evt.getNewValue();
                setSynthIcon(icon);
            }
            else if ("name".equals(evt.getPropertyName()))
            {
                setSynthName((String)evt.getNewValue());
            }
            else if (Synthesizer.DSP_GLOBAL.equals(evt.getPropertyName()))
            {
                updateDSPUsageGlobalLabel();
            }
        }
        
    }
/*
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
    }*/

    protected void bankPatchLoadEvent( Bank<S> bank,  int index)
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
    protected void connectionStateChanged(boolean connected)
    {
        if (connected)
        {   
            if (synth.getSlotCount()>0)
                createSlotComponents();
            installBanks();
        }
        else
        {
            uninstallBanks();
            removeSlotComponents();
        }

        tbConnect.setSelected(connected);
    }

    protected void createSlotComponents()
    {
        synchronized (slotContainer.getTreeLock())
        {
            for (int s = 0; s<synth.getSlotCount(); s++)
            {
                SlotObject<S> si = createSlotObject(synth.getSlot(s));
                slotContainer.add(si.root);
                slotContainer.add(Box.createVerticalStrut(2)); 
                slotObjects.add(si);
            }
            slotContainer.add(Box.createVerticalStrut(10));
        }
        revalidate();
        repaint();
    }

    protected void removeSlotComponents()
    {
        synchronized (slotContainer.getTreeLock())
        {
            for (SlotObject so: slotObjects)
            {
                so.uninstall();
            }
            slotObjects.clear();    
            slotContainer.removeAll(); 
        }
        revalidate();
        repaint();
    }

    public void setSynthName(String name)
    {
        if (name == null) name = synth.getDeviceName();
        synthNameLabel.setText(name);
        
        // TODO set tab bar title
    }

    public void setSynthIcon(Icon icon)
    {
        synthIconLabel.setIcon(icon);
        revalidate();
        repaint();
    }
    
    public S getSynthesizer()
    {
        return synth;
    }

    private static ImageIcon getIcon(String name)
    {
        URL url = SynthObjectForm.class.getClassLoader().getResource(name);
        
        if (url == null)
            return null;
        
        return new ImageIcon(url);
    }
    
    private void installBanks()
    {
        RootNode banksRoot = banksTree.getRoot();
        
        banksRoot.removeAllChildren();
        for (int i=0;i<synth.getBankCount();i++)
        {
            banksRoot.add(new BankLeaf(banksRoot, synth.getBank(i)));
        }
        banksTree.fireRootChanged();
    }

    private void uninstallBanks()
    {
        RootNode banksRoot = banksTree.getRoot();
        for (int i=0;i<banksRoot.getChildCount();i++)
        {
            BankLeaf l = (BankLeaf) banksRoot.getChildAt(i);
            l.uninstall();
        }
        banksRoot.removeAllChildren();
        banksTree.fireRootChanged();
    }

    private class BankLeaf extends ContainerNode implements BankUpdateListener
    {

        private Bank<S> bank;
        private boolean dropped = true;

        public BankLeaf(TreeNode parent, Bank<S> bank)
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
            banksTree.fireNodeStructureChanged(this);
        }
        
    }
    

    static ImageIcon icfolder = getIcon("tango-icon-theme/16x16/places/folder.png");
    static ImageIcon icfolderopen = getIcon("tango-icon-theme/16x16/status/folder-open.png");
    static ImageIcon icstart = getIcon("tango-icon-theme/16x16/actions/media-playback-start.png");
    static ImageIcon icstop = getIcon("tango-icon-theme/16x16/actions/media-playback-stop.png");
    static ImageIcon icsystem = getIcon("tango-icon-theme/16x16/emblems/emblem-system.png");
    // TODO find a good connect/disconnected icon
    static ImageIcon icconnected = getIcon("tango-icon-theme/16x16/emotes/face-angel.png");
    static ImageIcon icdisconnected = icconnected;

    private JLabel synthIconLabel;
    private JLabel synthNameLabel;
    private JComponent slotContainer;
    private JToggleButton tbConnect; 
    private ExplorerTree banksTree;
    private JLabel dspUsageGlobal = null;
    
    private void updateDSPUsageGlobalLabel()
    {
        if (dspUsageGlobal != null)
            dspUsageGlobal.setText("DSP: "+getDSPPercentage());
    }

    private String getDSPPercentage()
    {
        int idsp = (int)(synth.getDoubleProperty(Synthesizer.DSP_GLOBAL)*10000);
        double ddsp = ((double)idsp)/100;
        
        return ddsp+"%";
    }
    
    public JComponent create()
    {
        JPanel synthpane = this;
        synthpane.setLayout(new BoxLayout(synthpane, BoxLayout.Y_AXIS));
        
        // icon
        
        Icon icon = (Icon) synth.getClientProperty("icon");
        
        synthIconLabel = new JLabel(icon);
        TopLeft(synthIconLabel);

        // name
        synthNameLabel = new JLabel(synth.getName());
        TopLeft(synthNameLabel);

        Box mainLine = Box.createHorizontalBox();
        TopLeft(mainLine);

        Box lastLine = Box.createHorizontalBox();
        TopLeft(lastLine);

        Box propertyBox = Box.createVerticalBox();
        propertyBox.add(synthNameLabel);
        
        if (synth.hasProperty(Synthesizer.DSP_GLOBAL))
        {
            dspUsageGlobal = new JLabel();
            updateDSPUsageGlobalLabel();
            TopLeft(dspUsageGlobal);
            propertyBox.add(dspUsageGlobal);
        }
        propertyBox.add(Box.createVerticalGlue());
        propertyBox.add(lastLine);
        TopLeft(propertyBox);
        Box iconBox = Box.createVerticalBox();
        iconBox.add(synthIconLabel);
        TopLeft(iconBox);

        
        JButton btnSystem = new JButton(icsystem);
        TopLeft(btnSystem);
        Right(btnSystem);
        
        tbConnect = new JToggleButton(icdisconnected);
        TopLeft(tbConnect);
        Right(tbConnect);
        
        tbConnect.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                final boolean tbDoConnect = tbConnect.isSelected();
                
                SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        trySetConnectedState(tbDoConnect);
                    }
                }
                );
            }});
        
        lastLine.add(btnSystem);
        lastLine.add(tbConnect);
        
        mainLine.add(iconBox);  // left
        mainLine.add(propertyBox);// rightx
        
        slotContainer = Box.createVerticalBox();
        TopLeft(slotContainer);
        
        synthpane.add(mainLine);
        synthpane.add(slotContainer);

        Box banksBox = Box.createHorizontalBox();
        TopLeft(banksBox);
        
        JLabel lblBanks = new JLabel();
        lblBanks.setText("Banks");
        lblBanks.setIcon(icfolder);
        
        JTextField filter = new JTextField("*");
        

        banksBox.add(lblBanks);
        banksBox.add(Box.createHorizontalStrut(10));
        banksBox.add(filter);
        
        synthpane.add(banksBox);
        synthpane.add(Box.createVerticalStrut(2));
        
        Box box = Box.createHorizontalBox();
        TopLeft(box);
        
        
        banksTree = new ExplorerTree();
        banksTree.addMouseListener(new MouseAdapter()
        {

            public void mousePressed(MouseEvent e)
            {
                if (!Platform.isFlavor(Platform.OS.MacOSFlavor))
                    handleDblClick(e);
            }

            public void mouseReleased(MouseEvent e)
            {
                if (Platform.isFlavor(Platform.OS.MacOSFlavor))
                    handleDblClick(e);
            }
            
            public void handleDblClick(MouseEvent e)
            {
                if (e.getClickCount()!=2)
                    return;
                
                TreePath path =
                banksTree.getClosestPathForLocation(e.getX(), e.getY());
                if (path == null)
                    return;

                Object node = path.getLastPathComponent();

                if (!(node instanceof LeafNode))
                    return;

                LeafNode leaf = (LeafNode) node;
                if (!BankLeaf.class.isInstance(leaf.getParent()))
                    return;

                BankLeaf bl = (BankLeaf)leaf.getParent();
                
                int index = bl.getIndex(leaf);
                
                bankPatchLoadEvent(bl.bank, index);
            }
            
        });
        
        
        JScrollPane sp = new JScrollPane(banksTree);
      
        TopLeft(sp);
        
        box.add(sp);

        synthpane.add(box);
        
        synthpane.setBorder(BorderFactory.createEmptyBorder(2,8,2,8));
        return synthpane;
    }
    
    protected static class ClickableLabel extends JLabel
    {
        public static final String CLICK = "click";
        private boolean clickAble;
        private Color defaultForeground;
        private ActionListener l;
        
        public ClickableLabel(ActionListener l)
        {
            this.l = l;
            defaultForeground = getForeground();
            enableEvents(AWTEvent.MOUSE_EVENT_MASK);
            setClickable(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        
        protected void processMouseEvent(MouseEvent e)
        {
            if (e.getID() == MouseEvent.MOUSE_CLICKED && SwingUtilities.isLeftMouseButton(e))
            {
                // click
                l.actionPerformed(new ActionEvent(this, 0, CLICK));
            }
            else if (e.getID() == MouseEvent.MOUSE_ENTERED)
            {
                
            }
            else if (e.getID() == MouseEvent.MOUSE_EXITED)
            {
                
            }
            
            super.processMouseEvent(e);
        }
        
        public void setClickable(boolean enabled)
        {
            this.clickAble = enabled;
            if (clickAble)
            {
                setForeground(Color.BLUE);
            }
            else
            {
                setForeground(defaultForeground);
            }
        }
        
    }
    
    protected void trySetConnectedState(boolean connect)
    {
        if (connect && (!arePlugsConfigured()))
        {
            showSettings();
        }
        
        try
        {
            synth.setConnected(connect);
        } 
        catch (SynthException e)
        {
            tbConnect.setSelected(false);
            
            ExceptionDialog.showErrorDialog(
                    Nomad.sharedInstance().getWindow().getRootPane(),
                    e.getMessage(),
                    "Connect/Disconnect", e);
            // TODO log error
        }
    }

    protected void addForms(SynthPropertiesDialog spd)
    {
        spd.addSynthInfo();
        spd.addPortSettings();
        spd.addSynthSettings();
    }
    
    protected SlotObject<S> createSlotObject(Slot slot)
    {
        return new SlotObject<S>(this, slot);
    }
    
    protected static class SlotObject<S extends Synthesizer> implements SlotListener, ActionListener, PropertyChangeListener
    {
        JComponent root;
        private Slot slot;
        private ClickableLabel lblSlotPatchName;
        private SynthObjectForm<S> form;
        private JToggleButton tbToggleSlotEnabledState;
        
        public SlotObject(SynthObjectForm<S> form, Slot slot)
        {
            this.form = form;
            this.slot = slot;
            createRootComponent();
            install();
        }

        protected SynthObjectForm<S> getForm()
        {
            return form;
        }
        
        protected void install()
        {
            slot.addSlotListener(this);
            slot.addPropertyChangeListener(this);
        }

        public void uninstall()
        {
            slot.removePropertyChangeListener(this);
            slot.removeSlotListener(this);
        }

        public Slot getSlot()
        {
            return slot;
        }
        
        public void createRootComponent()
        {
            // slot box 1
            Box slotBox = Box.createHorizontalBox();
            TopLeft(slotBox);
            
            lblSlotPatchName = new ClickableLabel(this);
            lblSlotPatchName.setIcon(icfolder);
            Left(lblSlotPatchName);
            Font f = lblSlotPatchName.getFont();
            lblSlotPatchName.setFont(new Font(f.getName(), f.getStyle()|Font.ITALIC, f.getSize()));
            updateSlotPatchName();

            slotBox.add(lblSlotPatchName);
            slotBox.add(Box.createHorizontalGlue());
            
            if (slot.isPropertyModifiable(Slot.ENABLED_PROPERTY))
            {   
                JToggleButton tb = new JToggleButton();
                Right(tb);
                tb.setToolTipText("Enable/Disable Slot");
                tb.setBorder(null);
                tb.setOpaque(false);
                tb.setBorderPainted(false);
                tbToggleSlotEnabledState = tb;

                tb.setSelected(slot.isEnabled());
                tb.setIcon(slot.isEnabled()?icstop:icstart);
                
                tb.setActionCommand(Slot.ENABLED_PROPERTY);
                tb.addActionListener(this);

                slotBox.add(tb);
            }
            
            root = slotBox;
        }
        
        public void newPatchInSlot(SlotEvent e)
        {
            updateSlotPatchName();
        }
        
        private void updateSlotPatchName()
        {
            String name = slot.getPatchName();
            if (name == null)
                name = "";
            name = slot.getName()+" : "+name;
            lblSlotPatchName.setText(name);
        }

        public void actionPerformed(ActionEvent e)
        {
            if (Slot.ENABLED_PROPERTY.equals(e.getActionCommand()))
            {
                // received from toggle button
                tbToggleSlotEnabledState.setIcon(tbToggleSlotEnabledState.isSelected()?icstop:icstart);
                slot.setEnabled(tbToggleSlotEnabledState.isSelected());
            }
            else if (ClickableLabel.CLICK.equals(e.getActionCommand()))
            {
                form.openOrSelectPatch(slot);
            }
        }

        public void propertyChange(PropertyChangeEvent evt)
        {
            if (Slot.ENABLED_PROPERTY.equals(evt.getPropertyName()))
            {
                // received from slot
                tbToggleSlotEnabledState.setIcon(slot.isEnabled()?icstop:icstart);
                tbToggleSlotEnabledState.setSelected(slot.isEnabled());
            }
            else if (Slot.PATCHNAME_PROPERTY.equals(evt.getPropertyName()))
            {
                updateSlotPatchName();
            }
        }
    }
    
    private static String titleForSlot(int index)
    {
        return String.valueOf((char)('A'+index-1));
    }

    public void openOrSelectPatch(Slot slot)
    {
        //
    }

    private static void Right(JComponent component)
    {
        component.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
    }

    private static void Left(JComponent component)
    {
        component.setAlignmentX(JComponent.LEFT_ALIGNMENT);
    }

    private static void TopLeft(JComponent component)
    {
        component.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        component.setAlignmentY(JComponent.TOP_ALIGNMENT);
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
        d.setModal(true);
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
    
}
