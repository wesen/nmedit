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
package net.sf.nmedit.jsynth.nomad.forms;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
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
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
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
import net.sf.nmedit.jsynth.event.ComStatusEvent;
import net.sf.nmedit.jsynth.event.ComStatusListener;
import net.sf.nmedit.jsynth.event.SlotEvent;
import net.sf.nmedit.jsynth.event.SlotListener;
import net.sf.nmedit.jsynth.event.SynthesizerEvent;
import net.sf.nmedit.jsynth.event.SynthesizerStateListener;
import net.sf.nmedit.jsynth.nomad.SimpleTextFilter;
import net.sf.nmedit.jsynth.worker.PatchLocation;
import net.sf.nmedit.jsynth.worker.StorePatchWorker;
import net.sf.nmedit.nmutils.Platform;
import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.forms.ExceptionDialog;
import net.sf.nmedit.nomad.core.swing.Factory;
import net.sf.nmedit.nomad.core.swing.JDropDownButtonControl;
import net.sf.nmedit.nomad.core.swing.SelectedAction;
import net.sf.nmedit.nomad.core.swing.explorer.ContainerNode;
import net.sf.nmedit.nomad.core.swing.explorer.ExplorerTree;
import net.sf.nmedit.nomad.core.swing.explorer.LeafNode;
import net.sf.nmedit.nomad.core.swing.explorer.RootNode;


public class SynthObjectForm<S extends Synthesizer> extends JPanel
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 9030717642000065819L;
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
    
    protected class EventHandler implements SynthesizerStateListener, PropertyChangeListener, ComStatusListener
    {

        public void install()
        {
            synth.addSynthesizerStateListener(this);
            synth.addPropertyChangeListener(this);
            synth.addComStatusListener(this);
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
            else if (Synthesizer.PROPERTY_NAME.equals(evt.getPropertyName()))
            {
                setSynthName((String)evt.getNewValue());
            }
            else if (Synthesizer.DSP_GLOBAL.equals(evt.getPropertyName()))
            {
                updateDSPUsageGlobalLabel();
            }
        }

        public void comStatusChanged(ComStatusEvent e)
        {
            updateSynthStatusLabel();
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
        Synthesizer synth = bank.getSynthesizer();
        Slot dst = null;
        list.add(synth);
        
        if (!bank.containsPatch(index))
        {
            // no patch => nothing to open
            return;
        }
        
        if (synth.getSlotCount() > 1) {

        	SaveInSynthDialog ssd = new SaveInSynthDialog(list);
        	ssd.setTitle("Open Patch...");
        	ssd.setSaveInBankAllowed(false);
        	ssd.invoke();

        	if (!ssd.isSaveOption())
        		return;

        	dst = ssd.getSelectedSlot();
        } else {
        	dst = synth.getSlot(0);
        }
    
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
        icon = ensureIsSmallIcon(icon);
        synthIconLabel.setIcon(icon);
        if (icon != null)
        {
            int w = icon.getIconWidth();
            int h = icon.getIconHeight();
            synthIconLabel.setPreferredSize(new Dimension(w, h));
            synthIconLabel.setMaximumSize(new Dimension(w, h));
            synthIconLabel.setVisible(true);
        }
        else
        {
            synthIconLabel.setVisible(false);
        }
        revalidate();
        repaint();
    }
    
    public S getSynthesizer()
    {
        return synth;
    }

    private static ImageIcon getIcon(String name)
    {
        name = "/icons/tango/16x16/"+name;
        URL url = Nomad.sharedInstance().getClass().getResource(name);
        
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

    private void setBanksFilter(String filter)
    {
        RootNode root = banksTree.getRoot();
        for (int i=0;i<root.getChildCount();i++)
        {
            TreeNode tn = root.getChildAt(i);
            if (BankLeaf.class.isInstance(tn))
            {
                ((BankLeaf)tn).setFilter(filter);
            }
        }
        for (int i=0;i<root.getChildCount();i++)
        {
            TreeNode tn = root.getChildAt(i);
            if (BankLeaf.class.isInstance(tn))
            {
                ((BankLeaf)tn).regenerate();
            }
        }
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

    private class BankPosition extends LeafNode
    {
        private int position;

        public BankPosition(BankLeaf bankNode, String text, int position)
        {
            super(bankNode, text);
            this.position = position;
        }
    }
    
    private class BankLeaf extends ContainerNode implements BankUpdateListener
    {

        private Bank<S> bank;
        private boolean dropped = true;
        private SimpleTextFilter nameFilter = new SimpleTextFilter();
        
        public BankLeaf(TreeNode parent, Bank<S> bank)
        {
            super(parent, bank.getName());
            this.bank = bank;
            bank.addBankUpdateListener(this);
            regenerate();
        }
        
        public void setFilter(String filter)
        {
            nameFilter.setFilter(filter);
            if (nameFilter.isFiltering())
            {
                // ensure that all patches are loaded
                
                for (int i=0;i<bank.getPatchCount();i++)
                {
                    if (!bank.isPatchInfoAvailable(i))
                    {
                        bank.update(i, bank.getPatchCount());
                        return;
                    }
                }
                // all patches available
            }
        }

        private void regenerate()
        {
            if (!synth.isConnected())
            {
                this.clear();
                banksTree.fireNodeStructureChanged(this);
                return;
            }
            
            this.clear();
            int firstEmptyIndex = -1;
            for (int i=0;i<bank.getPatchCount();i++)
            {
                if (bank.isPatchInfoAvailable(i))
                {
                    if (bank.containsPatch(i))
                        
                    {
                    String patchname = bank.getPatchName(i);
                    String name = bank.getPatchLocationName(i) +": "+ patchname;
                    if (nameFilter.contains(patchname))
                        addChild(new BankPosition(this, name, i));
                    }
                    else
                    {
                        if (firstEmptyIndex<0)
                            firstEmptyIndex = i;
                    }
                }
            }
            
            if (dropped)
            {
                // fake node
                if (getChildCount()==0)
                    addChild(0, new BankPosition(this, "", firstEmptyIndex /* -1 is ok */));
            }
            else
            {
                if (firstEmptyIndex>=0)
                {
                    String name = "<empty>";
                    if (nameFilter.contains(name))
                        addChild(0, new BankPosition(this, name, firstEmptyIndex));
                }
            }
            banksTree.fireNodeStructureChanged(this);
        }

        public boolean getAllowsChildren()
        {
            return true;
        }
        
        public TreeNode getChildAt(int index)
        {
            ensureBankLoaded();
            return super.getChildAt(index);
        }
        
        private void ensureBankLoaded()
        {
            if (!dropped) return;
            if (synth.isConnected())
            {
                dropped = false;
                bank.update();
            }
        }

        public boolean isLeaf()
        {
            return bank.getPatchCount()==0;
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
//        	System.out.println("update bank start " + Thread.currentThread()); 
//        	Throwable ex = new Throwable();
//        	ex.printStackTrace();
            if (dropped) return;
           
            SwingUtilities.invokeLater(new Runnable(){public void run(){
            regenerate();
            }});
            /*
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
            */
//            System.out.println("update bank stop " + Thread.currentThread());
        }
        
    }
    

    static ImageIcon icfolder = getIcon("places/folder.png");
    static ImageIcon icfolderopen = getIcon("status/folder-open.png");
    static ImageIcon icfolderDragAccept = getIcon("status/folder-drag-accept.png");
    static ImageIcon icstart = getIcon("actions/media-playback-start.png");
    static ImageIcon icstop = getIcon("actions/media-playback-stop.png");
    static ImageIcon icsystem = getIcon("actions/document-properties.png");
    // TODO find a good connect/disconnected icon
    static ImageIcon icconnected = getIcon("categories/applications-other.png");
    static ImageIcon icdisconnected = icconnected;


    static ImageIcon icComIdle = getIcon("status/network-idle.png");
    static ImageIcon icComOffline = getIcon("status/network-offline.png");
    static ImageIcon icComError = getIcon("status/network-error.png");
    static ImageIcon icComReceive = getIcon("status/network-receive.png");
    static ImageIcon icComTransmit = getIcon("status/network-transmit.png");
    static ImageIcon icComTransmitReceive = getIcon("status/network-transmit-receive.png");

    static ImageIcon icAppOther = getIcon("categories/applications-other.png");
    
    private JLabel synthStatusLabel;
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
        synthpane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        synthpane.setBorder(BorderFactory.createEmptyBorder(2,4,2,4));
        
        // create components

        Icon icon = (Icon) synth.getClientProperty("icon");
        synthIconLabel = new JLabel();
        setSynthIcon(ensureIsSmallIcon(icon));
        synthNameLabel = new JLabel(synth.getName());
        synthStatusLabel = new JLabel();
        updateSynthStatusLabel();
        JButton btnSystem = new JButton(icsystem);

        JLabel lblBanks = new JLabel();
        lblBanks.setText("Banks");
        lblBanks.setIcon(icfolder);
        
        tbConnect = new JToggleButton(icdisconnected);
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
        
        btnSystem.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e)
            {
                SynthObjectForm.this.showSettings();
            }
        });

        final JTextField filter = new JTextField("*");
        filter.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));
        filter.addActionListener(new ActionListener(){
            
            public void actionPerformed(ActionEvent e)
            {
                setBanksFilter(filter.getText());
                
            }
            
        });

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

                if (!BankPosition.class.isInstance(node))
                    return;
                BankPosition leaf = (BankPosition) node;
                BankLeaf bl = (BankLeaf)leaf.getParent();
                bankPatchLoadEvent(bl.bank, leaf.position);
            }
            
        });
        
        
        JScrollPane spBanksTree = new JScrollPane(banksTree);
      
        
        // layout components
        
        /* PropertiesMain:
         * 
         * +------------+------------------+
         * |   ^        |     ^            |
         * |   |        |     |            |
         * | SynIconBox | <- SynPropBox -> |
         * |   |        |     |            |
         * |   ^        |     ^            |
         * |------------+------------------+
         * 
         */
        
        Box PropertiesMain = Box.createHorizontalBox();
        Box SynIconBox = top(left(Box.createVerticalBox()));
        Box SynPropBox = top(left(Box.createVerticalBox()));

        PropertiesMain.add(SynIconBox);
        PropertiesMain.add(hgap());
        PropertiesMain.add(SynPropBox);
        
        // add syn icon label
        SynIconBox.add(top(left(synthIconLabel)));
        SynIconBox.add(vgap());
        //SynIconBox.add(Box.createVerticalGlue());
        {   /* SynPropBox :
             * +-----------------------------------------------------------------+
             * | SynthNameLabel <---------[SynTitleBox]------> SynthStatusIcon   |
             * |-----------------------------------------------------------------+
             * | ... dsp, other properties ...                                   |
             * |-----------------------------------------------------------------+
             * | <--- [SynActionBox] ---> | Properties        | Disconnect       |
             * |-----------------------------------------------------------------+
             */

            Box SynTitleBox = top(left(Box.createHorizontalBox()));
            SynTitleBox.add(top(left(synthNameLabel)));
            SynTitleBox.add(hgap());
            SynTitleBox.add(Box.createHorizontalGlue());
            SynTitleBox.add(top(right(synthStatusLabel)));

            Box SynActionBox = top(left(Box.createHorizontalBox()));
            SynActionBox.add(Box.createHorizontalGlue());
            Collection<Action> SpecialActions = getSpecialActions();
            if (!SpecialActions.isEmpty())
            {
                SynActionBox.add(hgap());
                SynActionBox.add(createSpecialActionsComponent(SpecialActions));
            }
            SynActionBox.add(hgap());
            SynActionBox.add(btnSystem);
            SynActionBox.add(hgap());
            SynActionBox.add(tbConnect);

            SynPropBox.add(SynTitleBox);
            SynPropBox.add(vgap());
            SynPropBox.add(SynActionBox);
            /* TODO support for other properties
            if (synth.hasProperty(Synthesizer.DSP_GLOBAL))
            {
                dspUsageGlobal = new JLabel();
                updateDSPUsageGlobalLabel();
                propertyBox.add(dspUsageGlobal);
            }*/
        }
        
        /*
         * synthpane:
         * +---------------------------+
         * | <-- PropertiesMain -->    |
         * +---------------------------+
         * |          ^                |
         * |          |                |
         * |        [vglue]            |
         * |          |                |
         * |          ^                |
         * +---------------------------+
         * |        Slot               |
         * +---------------------------+
         * |        Banks              |
         * +---------------------------+
         */

        synthpane.add(top(left(PropertiesMain)));
        synthpane.add(vgap());
        // Slot
        slotContainer = top(left(Box.createVerticalBox()));
        synthpane.add(slotContainer);
        
        synthpane.add(vgap());
        // Bank
        
        Box BanksExtra = Box.createHorizontalBox();
        BanksExtra.add(lblBanks);
        BanksExtra.add(hgap());
        BanksExtra.add(filter);
        synthpane.add(top(left(BanksExtra)));
        synthpane.add(vgap());
        synthpane.add(top(left(spBanksTree)));
        spBanksTree.setMaximumSize(null);
        spBanksTree.setPreferredSize(null);

        // ... layout complete!
        
        return synthpane;
    }

    private JComponent createSpecialActionsComponent(Collection<Action> specialActions)
    {
        SelectedAction sa = new SelectedAction();
        sa.putValue(AbstractAction.SMALL_ICON, icAppOther);

        JPopupMenu pop = new JPopupMenu();
        JRadioButtonMenuItem rfirst = null;
        for (Action a: specialActions)
        {
            JRadioButtonMenuItem rb = new JRadioButtonMenuItem(a);
            if (rfirst == null) rfirst = rb;
            sa.add(rb);
            pop.add(rb);
        }
        JButton btn = Factory.createToolBarButton(sa);
        new JDropDownButtonControl(btn, pop);
        return btn;
    }
    
    protected Collection<Action> getSpecialActions()
    {
        return Collections.emptyList();
    }


    final int GAP = 2;
    private Component hgap() { return Box.createRigidArea(new Dimension(GAP,0)); }
    private Component vgap() { return Box.createRigidArea(new Dimension(0, GAP)); }

    private static <T extends JComponent> T left(T c)
    {
        c.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        return c;
    }
    private static <T extends JComponent> T right(T c)
    {
        c.setAlignmentX(JComponent.RIGHT_ALIGNMENT);
        return c;
    }
    private static <T extends JComponent> T top(T c)
    {
        c.setAlignmentY(JComponent.TOP_ALIGNMENT);
        return c;
    }
    
    private Icon ensureIsSmallIcon(Icon icon)
    {
        final int SZ = 64;
        final float SZf = (float) SZ;
        
        BufferedImage b = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = b.createGraphics();
        try
        {
            icon.paintIcon(this, g2, 0, 0);
        }
        finally
        {
            g2.dispose();
            g2 = null;
        }
        // aspect ratio
        float wf = b.getWidth()/SZf;
        float hf = b.getHeight()/SZf;
        float maxf = Math.max(wf, hf);

        int nw = (int) Math.floor(b.getWidth()/maxf);
        int nh = (int) Math.floor(b.getHeight()/maxf);

        BufferedImage nb = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_ARGB);
        g2 = nb.createGraphics();
        try
        {
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.drawImage(b, 0, 0, nw, nh, 0, 0, b.getWidth(), b.getHeight(), null);
        }
        finally
        {
            g2.dispose();
            g2 = null;
        }
        return new ImageIcon(nb);
    }

    private void updateSynthStatusLabel()
    {
        switch (synth.getComStatus())
        {
            case Idle: 
                synthStatusLabel.setIcon(icComIdle);
                break;
            case Offline:
                synthStatusLabel.setIcon(icComOffline);
                break;
            case Error:
                synthStatusLabel.setIcon(icComError);
                break;
            case Receive:
                synthStatusLabel.setIcon(icComReceive);
                break;
            case Transmit:
                synthStatusLabel.setIcon(icComTransmit);
                break;
            case TransmitReceive:
                synthStatusLabel.setIcon(icComTransmitReceive);
                break;
        }    
    }

    protected static class ClickableLabel extends JLabel
    {
        /**
         * 
         */
        private static final long serialVersionUID = -6967218815458151086L;
        public static final String CLICK = "click";
        private boolean clickAble;
        private Color defaultForeground;

        private static final Color CLICKABLE_COLOR = Color.decode("#00286E");
        private static final Color HOVERED_COLOR = CLICKABLE_COLOR.brighter().brighter();
        
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
            if (e.getID() == MouseEvent.MOUSE_CLICKED && Platform.isLeftMouseButtonOnly(e))
            {
                // click
                l.actionPerformed(new ActionEvent(this, 0, CLICK));
            }
            else if (e.getID() == MouseEvent.MOUSE_ENTERED)
            {
                if (clickAble) setForeground(HOVERED_COLOR);
            }
            else if (e.getID() == MouseEvent.MOUSE_EXITED)
            {
                if (clickAble) setForeground(CLICKABLE_COLOR);   
            }
            
            super.processMouseEvent(e);
        }
        
        public void setClickable(boolean enabled)
        {
            this.clickAble = enabled;
            if (clickAble)
            {
                setForeground(CLICKABLE_COLOR);
            }
            else
            {
                setForeground(defaultForeground);
            }
        }
        
    }
    
    protected void trySetConnectedState(boolean connect)
    {
        boolean no = false;
        if (connect && (!arePlugsConfigured()))
        {
            no = showSettings();
        }
        
        if (no || (!arePlugsConfigured()))
        {
            tbConnect.setSelected(false);
            return;
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

    protected void addForms(SynthPropertiesDialog<S> spd)
    {
        spd.addSynthInfo();
        spd.addPortSettings();
        spd.addSynthSettings();
    }
    
    protected SlotObject<S> createSlotObject(Slot slot)
    {
        return new SlotObject<S>(this, slot);
    }
    
    protected static class SlotObject<S extends Synthesizer> implements SlotListener, ActionListener, PropertyChangeListener, DropTargetListener
    {
        JComponent root;
        private Slot slot;
        private ClickableLabel lblSlotPatchName;
        private SynthObjectForm<S> form;
        private JToggleButton tbToggleSlotEnabledState;
        private DropTarget dt;
        
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
            
            this.dt = new DropTarget(slotBox, this);
            
            left(top(slotBox));
            
            lblSlotPatchName = left(new ClickableLabel(this));
            updateSlotPatchName();

            slotBox.add(lblSlotPatchName);
            slotBox.add(Box.createHorizontalGlue());
            
            if (slot.isPropertyModifiable(Slot.ENABLED_PROPERTY))
            {   
                JToggleButton tb = new JToggleButton();
                right(tb);
                tb.setToolTipText("Enable/Disable Slot");
                /*
                tb.setOpaque(false);*/

                tb.setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
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
            
            name = "<html><body>"+slot.getName()+" : <u>"+name+"</u>";
            
            if (slot.isSelected())
            {
                name += " *";
                lblSlotPatchName.setIcon(icfolderopen);
            }
            else
            {
                lblSlotPatchName.setIcon(icfolder);
            }
            name += "</body></html>";
            
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
                slot.setSelected(true);
                updateSlotPatchName();
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
            else if (Slot.SELECTED_PROPERTY.equals(evt.getPropertyName()))
            {
                updateSlotPatchName();
                if (evt.getNewValue() != null && ((Boolean) evt.getNewValue()).booleanValue())
                  form.openOrSelectPatch(slot);
            }
        }

        public void dragEnter(DropTargetDragEvent dtde)
        {
            if (form.acceptsDropData(this, dtde.getCurrentDataFlavors()))
            {
                lblSlotPatchName.setIcon(icfolderDragAccept);
                dtde.acceptDrag(DnDConstants.ACTION_LINK);
            }
            else
            {
                dtde.rejectDrag();
            }
        }

        public void dragExit(DropTargetEvent dte)
        {
            updateSlotPatchName(); // update label
        }

        public void dragOver(DropTargetDragEvent dtde)
        {
            if (form.acceptsDropData(this, dtde.getCurrentDataFlavors()))
            {
                lblSlotPatchName.setIcon(icfolderDragAccept);
                dtde.acceptDrag(DnDConstants.ACTION_LINK);
            }
            else
            {
                dtde.rejectDrag();
            }
        }

        public void drop(DropTargetDropEvent dtde)
        {
            if (form.acceptsDropData(this, dtde.getCurrentDataFlavors()))
            {
                form.dropTransfer(this, dtde);
            }
            else
            {
                dtde.rejectDrop();
            }
        }

        public void dropActionChanged(DropTargetDragEvent dtde)
        {
            // TODO Auto-generated method stub
            
        }
    }
    
    protected void dropTransfer(SlotObject<S> s, DropTargetDropEvent dtde)
    {
        dtde.rejectDrop();
    }
    
    protected boolean acceptsDropData(SlotObject<S> s, DataFlavor[] flavors)
    {
        return false;
    }
    
    private static String titleForSlot(int index)
    {
        return String.valueOf((char)('A'+index-1));
    }

    public void openOrSelectPatch(Slot slot)
    {
        //
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
        final SynthPropertiesDialog<S> spd = new SynthPropertiesDialog<S>(synth);
        
        
        addForms(spd);
        spd.setSelectedPath("connection");
        
        final JDialog d = new JDialog(Nomad.sharedInstance().getWindow(), 
                "Properties for "+synth.getDeviceName())
        {
            /**
             * 
             */
            private static final long serialVersionUID = -8324075575952331093L;
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
