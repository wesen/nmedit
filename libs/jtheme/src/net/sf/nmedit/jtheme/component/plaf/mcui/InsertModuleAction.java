package net.sf.nmedit.jtheme.component.plaf.mcui;

import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.dnd.PModuleTransferDataWrapper;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.component.JTModuleContainer;
import net.sf.nmedit.jtheme.util.ModuleImageRenderer;

public class InsertModuleAction extends AbstractAction {
	private PModuleDescriptor descriptor;
	private JTModuleContainer jmc;
	
	public InsertModuleAction(PModuleDescriptor descriptor, JTModuleContainer jmc) {
		this.descriptor = descriptor;
		this.jmc = jmc;
		
		String name = descriptor.getStringAttribute("fullname");
		if (name == null)
		    name = descriptor.getName();
		
		putValue(NAME, name);
        
        Image iconImage = descriptor.getModules().getImage(descriptor.get16x16IconSource());
        if (iconImage != null)
            putValue(SMALL_ICON, new ImageIcon(iconImage));
	}

	public void actionPerformed(ActionEvent e) {
		PModuleContainer mc = jmc.getModuleContainer();
		ArrayList<PModule> modules = new ArrayList<PModule>();
		modules.add(mc.createModule(descriptor));
    	PModuleTransferDataWrapper tdata =new PModuleTransferDataWrapper(mc, modules, new Point(5, 5));
        Image transferImage = null;
        try
        {
            transferImage = ModuleImageRenderer.render(jmc.getContext(), modules.get(0), true);
        }
        catch (JTException jte)
        {
           transferImage = null; 
        }
        tdata.setTransferImage(transferImage);
        
		jmc.getUI().startPaste(tdata);
	}

}
