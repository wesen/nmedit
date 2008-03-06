package net.sf.nmedit.jtheme.clavia.nordmodular;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.clavia.nordmodular.Knob;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jpatch.clavia.nordmodular.event.PAssignmentEvent;
import net.sf.nmedit.jpatch.clavia.nordmodular.event.PAssignmentListener;
import net.sf.nmedit.jpatch.impl.PBasicLight;
import net.sf.nmedit.jpatch.impl.PBasicLightDescriptor;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.component.JTControl;
import net.sf.nmedit.jtheme.component.JTLabel;
import net.sf.nmedit.jtheme.component.JTLight;
import net.sf.nmedit.jtheme.component.JTParameterControlAdapter;

public class NMKnobAssignmentWindow implements PropertyChangeListener, PAssignmentListener
{
    
    private JFrame frame;
    private JTNM1Context context;
    private KnobInfo[] EighteenKnobs = new KnobInfo[18];
    private KnobInfo[] SpecialKnobs;
    private NMPatch patch;
    
    private static String MAIN_TITLE = "Knob Assignments";
    
    public NMKnobAssignmentWindow(JTNM1Context context)
    {
        this.context = context;
        createFrame();
    }
    
    private NMKnobAssignmentWindow()
    {
        createFrame();
    }
    
    public void setPatch(NMPatch patch)
    {
        NMPatch oldValue = this.patch;
        NMPatch newValue = patch;
        if (oldValue == newValue) return;
        
        if (oldValue!=null)
            uninstall(oldValue);
        this.patch = newValue;
        if (newValue!=null)
            install(newValue);
    }
    
    private KnobInfo getKnobInfo( int id )
    {
        if (id<18)
            return EighteenKnobs[id];
        for (KnobInfo k: SpecialKnobs)
            if (k.id == id)
                return k;
        return null;
    }

    private void install(NMPatch patch)
    {
        for (Knob k: patch.getKnobs())
        {
            KnobInfo ki = getKnobInfo(k.getID());
            if (ki != null) ki.setAssignedTo(k.getParameter());
        }
        
        patch.addAssignmentListener(this);
    }

    private void uninstall(NMPatch patch)
    {
        for (Knob k: patch.getKnobs())
        {
            KnobInfo ki = getKnobInfo(k.getID());
            if (ki != null) ki.setAssignedTo(null);
        }
        patch.removeAssignmentListener(this);
    }

    private void createFrame()
    {
        frame = new JFrame(MAIN_TITLE);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        if (context == null)
        {
            NMStorageContext nmsc = new NMStorageContext(null, null);
            context = new JTNM1Context(null);
            context.setStorageContext(nmsc);
        }
        /*kam = new JTNMKnobAssignmentModule(context);
        frame.getContentPane().add(kam);*/
        try
        {
            JPanel knobPane = createKnobPane();
            frame.getContentPane().add(knobPane);
        } catch (JTException e)
        {
            e.printStackTrace();
        }
        
        frame.setBounds(10, 10, 380, 240);
        frame.setResizable(false);
    }
    
    public static void main(String[] args)
    {
        NMKnobAssignmentWindow w = new NMKnobAssignmentWindow();
        
        w.frame.setVisible(true);
    }
    
    private JPanel createKnobPane() throws JTException
    {
        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));

        // Main Box: 18Knobs
        Box MainBox = Box.createHorizontalBox();
        
        MainBox.add(Box.createHorizontalGlue()); // for centering
        Box col = null;

        for (int i=0;i<EighteenKnobs.length;i++)
        {
            if (i%3==0) 
            {
                col = Box.createVerticalBox();
                MainBox.add(col);
         
                int column = i/3;
                if (column==1||column==3||column==4)
                {
                    MainBox.add(Box.createRigidArea(new Dimension(3, 0)));
                    MainBox.add(new JSeparator(JSeparator.VERTICAL));
                    MainBox.add(Box.createRigidArea(new Dimension(3, 0)));
                }
                else
                {
                    MainBox.add(Box.createRigidArea(new Dimension(8, 0)));
                }
            }
            
            KnobInfo ki = new KnobInfo(i, context);
            EighteenKnobs[i] = ki;
            col.add(ki.root);
            col.add(Box.createVerticalGlue());
        }
        MainBox.add(Box.createHorizontalGlue());   // for centering

        root.add(MainBox);
        
        KnobInfo s1 = new KnobInfo(19, context);
        KnobInfo s2 = new KnobInfo(20, context);
        KnobInfo s3 = new KnobInfo(22, context);
        
        SpecialKnobs = new KnobInfo[]{s1, s2, s3};
        
        Box SpecialBox = Box.createHorizontalBox();
        SpecialBox.add(Box.createHorizontalGlue()); // center
        for (int i=0;i<SpecialKnobs.length;i++)
        {
            if (i>0) SpecialBox.add(Box.createRigidArea(new Dimension(4, 0)));
            SpecialBox.add(SpecialKnobs[i].root); 
        }
        SpecialBox.add(Box.createHorizontalGlue()); // center
        
        root.add(SpecialBox);
        root.add(Box.createVerticalGlue());
        
        return root;        
    }
    
    private static class KnobInfo
    {
        private final int id;
        private JTControl knobctrl;
        private JTLabel lblModule;
        private JTLabel lblParam;
        private JTLabel lblId;
        private JTLight liAssigned;

        private JComponent root;
        
        private AssignedLED LED;
        
        public KnobInfo(int id, JTContext context) throws JTException
        {
            this.id = id;
            createComponents(context);
            createRoot();
            setAssignedTo(null);
        }
        
        public boolean isSpecialKnob()
        {
            return id>18;
        }

        private JTLabel createLabel(JTContext context) throws JTException
        {
            JTLabel label = context.createLabel();
            label.enableJTFlags();
            return label;
        }
        
        private void createComponents(JTContext context) throws JTException
        {
            lblModule = createLabel(context);
            lblParam = createLabel(context);
            
            if (!isSpecialKnob())
            {
                LED = new AssignedLED();
                knobctrl = context.createKnob();
                lblId = createLabel(context);
                lblId.setText(Integer.toString(id+1));

                final int KS = 24;
                final int LS = 6;
                
                knobctrl.setSize(KS,KS);
                knobctrl.setMinimumSize(new Dimension(KS,KS));
                knobctrl.setPreferredSize(new Dimension(KS,KS));
                knobctrl.setMaximumSize(new Dimension(KS,KS));
                liAssigned = context.createLight();
                liAssigned.setLight(LED);
                liAssigned.setLEDOnValue(1);
                liAssigned.setMinimumSize(new Dimension(LS,LS));
                liAssigned.setPreferredSize(new Dimension(LS,LS));
                liAssigned.setSize(new Dimension(LS,LS));
                liAssigned.setMaximumSize(new Dimension(LS,LS));
            }
        }
        
        public void setAssignedTo(PParameter param)
        {
            if (param != null)
            {
                setText(lblModule, param.getParentComponent().getName());
                setText(lblParam, param.getName());
                if (!isSpecialKnob())
                {
                    knobctrl.setAdapter(new JTParameterControlAdapter(param));
                    if (param.getExtensionParameter() != null)
                    knobctrl.setExtensionAdapter(new JTParameterControlAdapter(param.getExtensionParameter()));
                    LED.setValue(1);
                }
            }
            else
            {
                setText(lblModule, null);
                setText(lblParam, null);
                if (!isSpecialKnob())
                {
                    JTParameterControlAdapter pca = (JTParameterControlAdapter) knobctrl.getControlAdapter();
                    if (pca != null) pca.uninstall();
                    pca = (JTParameterControlAdapter) knobctrl.getExtensiondapter();
                    if (pca != null) pca.uninstall();
                    knobctrl.setAdapter(null);
                    knobctrl.setExtensionAdapter(null);
                    knobctrl.setValue(0);
                    LED.setValue(0);
                }
            }
            root.revalidate();
            root.repaint();
            //setText(lblModule, "mmm4mmm4mmm4mmmX");
            //setText(lblParam, "Pa"+(Math.random()*1000));
        }
        
        private void setText(JTLabel label, String t)
        {
            label.setToolTipText(null);
            if (t == null)
            {
                label.setText("");
                return;
            }
            
            if (t.length()+3<=10)
            {
                label.setText(t);
            }
            else
            {
                String shortText = t.substring(0, 6)+"...";
                label.setText(shortText);
                label.setToolTipText(t);
            }
            
            label.setSize(label.getPreferredSize());
        }
        
        public void createRoot()
        {

            /* layout:
             * +-------------------------------+
             * |      Module Name              |
             * +-------------------------------+
             * |      Parameter Name           |
             * +---------------+-+-------------+
             * |   ----------- | |    LED      |
             * |  /           \| +-------------+
             * |  |    Knob   || |   Knob-Num. |
             * |  \           /| |             |
             * |   ----------- | |             |
             * +-----------------+-------------+
             */
            
            root = Box.createVerticalBox();
            {
                Box line = Box.createHorizontalBox(); // enforced line height
                line.add(Box.createRigidArea(new Dimension(0, 7)));
                line.add(lblModule);
                root.add(line);
                line = Box.createHorizontalBox();
                line.add(Box.createRigidArea(new Dimension(0, 7)));
                line.add(lblParam);
                root.add(line);
            }
            root.add(Box.createRigidArea(new Dimension(0, 2)));
            if (!isSpecialKnob())
            {
                Box hrz = Box.createHorizontalBox();
                hrz.add(Box.createHorizontalGlue());
                root.add(hrz);
                Box vrt = Box.createVerticalBox();
                vrt.add(liAssigned);
                vrt.add(lblId);
                hrz.add(knobctrl);
                hrz.add(Box.createRigidArea(new Dimension(2, 0)));
                hrz.add(vrt);
                hrz.add(Box.createHorizontalGlue());
            }
            root.add(Box.createRigidArea(new Dimension(0, 2)));
        }
        
    }
    
    private static class AssignedLED extends PBasicLight
    {
        
        private static PBasicLightDescriptor descriptor
            = new PBasicLightDescriptor(null, "LED", null);

        public AssignedLED()
        {
            super(descriptor, null, 0);
            descriptor.setMinValue(0);
            descriptor.setMaxValue(1);
            descriptor.setDefaultValue(0);
            setValue(0);
        }
        
    }

    public JFrame getFrame()
    {
        return frame;
    }

    public void propertyChange(PropertyChangeEvent evt)
    {
    }

    public void parameterAssigned(PAssignmentEvent e)
    {
        handleAssignmentEvent(e);
    }

    public void parameterDeassigned(PAssignmentEvent e)
    {
        handleAssignmentEvent(e);
    }

    private void handleAssignmentEvent(PAssignmentEvent e)
    {
        if (patch == null) return; // should never happen
        switch (e.getId())
        {
            case PAssignmentEvent.KNOB_ASSIGNED:
                knobChanged(e, true);
                break;
            case PAssignmentEvent.KNOB_DEASSIGNED:
                knobChanged(e, false);
                break;
        }
    }

    private void knobChanged(PAssignmentEvent e, boolean assigned)
    {
        KnobInfo ki = getKnobInfo(e.getKnobId());
        if (ki == null) return;
        ki.setAssignedTo(assigned ? e.getParameter() : null);
    }
    
}
