package net.waldorf.miniworks4pole.jtheme;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.sf.nmedit.jpatch.spec.ModuleDescriptions;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.component.JTModule;
import net.sf.nmedit.jtheme.component.JTModuleContainer;
import net.sf.nmedit.jtheme.component.JTPatch;
import net.sf.nmedit.jtheme.store.DefaultStorageContext;
import net.sf.nmedit.jtheme.store.ModuleStore;
import net.waldorf.miniworks4pole.jpatch.MWPatch;

public class JTMWPatch extends JTPatch
{

    private ModuleDescriptions md;
    private MWPatch patch;
    private DefaultStorageContext storageContext;

    public JTMWPatch(DefaultStorageContext storageContext, 
            JTContext context, ModuleDescriptions md, MWPatch patch)
    {
        super(context);
     
        this.storageContext = storageContext;
        this.md = md;
        this.patch = patch;
        
        try
        {
            buildUI();
        }
        catch (JTException e)
        {
            e.printStackTrace();
        }
    }
    
    private void buildUI() throws JTException
    {
        // patch
        JTModuleContainer container = getContext().createModuleContainer();
        ModuleStore store = storageContext.getModuleStoreById("main");

        JTModule jtmodule = store.createModule(getContext(), patch.getMiniworksModule());
        jtmodule.setLocation(10, 10);
        container.add(jtmodule);
        
        store.setStaticLayer(jtmodule.renderStaticLayerImage());
        
        jtmodule.setStaticLayerBackingStore(store.getStaticLayer());
        
        
        container.setPreferredSize(new Dimension(jtmodule.getWidth()+20, jtmodule.getHeight()+20));
     
        // header
        
        JPanel panHeader = new JPanel();
        panHeader.setBorder(BorderFactory.createEmptyBorder(2,4,2,4));
        panHeader.setLayout(new BoxLayout(panHeader, BoxLayout.X_AXIS));
        JTextField txtBank = new JTextField("?");
        panHeader.add(new JLabel("Bank"));
        panHeader.add(Box.createHorizontalStrut(4));
        panHeader.add(txtBank);
        panHeader.add(Box.createHorizontalGlue());
        
        setLayout(new BorderLayout());
        add(panHeader, BorderLayout.NORTH);
        add(new JScrollPane(container), BorderLayout.CENTER);
    }

    public MWPatch getPatch()
    {
        return patch;
    }

}
