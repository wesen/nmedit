/**
 * 
 */
package net.sf.nmedit.jtheme.component.plaf.mcui;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.undo.UndoableEditSupport;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.PatchUtils;
import net.sf.nmedit.jtheme.component.JTModule;
import net.sf.nmedit.jtheme.component.JTModuleContainer;

public class ContainerAction extends AbstractAction
{
    
    /**
	 * 
	 */
	private JTModuleContainer jmc;
	/**
     * 
     */
    private static final long serialVersionUID = 7135918324094843867L;
    public static final String DELETE_UNUSED = "delete.unused";
    public static final String DELETE = "delete";
    public static final String SELECT_ALL = "selectAll";

    public ContainerAction(JTModuleContainer moduleContainer, String command)
    {
        this.jmc = moduleContainer;
        putValue(ACTION_COMMAND_KEY, command);
        if (command == DELETE_UNUSED)
        {
            putValue(NAME, "Delete Unused Modules");
        } else {
        	putValue(NAME, command);
        }
    }
    
    protected PModuleContainer getTarget()
    {
        return this.jmc.getModuleContainer();
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if (isEnabled())
        {
        	Object key = getValue(ACTION_COMMAND_KEY);
            if (key==DELETE_UNUSED)
            {
            	deleteUnusedModules();
            } else if (key == SELECT_ALL) {
            	if (jmc != null) {
            		for (Component component : jmc.getComponents())
            			if (component instanceof JTModule)
            				jmc.addSelection((JTModule)component);
            	}
            		
            } else if (key == DELETE) {
            	if (jmc == null) {
            		Object o = e.getSource();
            		if (o instanceof JTModuleContainer) {
            			jmc = (JTModuleContainer)o;
            		} else if (o instanceof JTModule){
            			jmc = (JTModuleContainer)((JTModule)o).getParent();
            		} else {
            			return;
            		}
            	}
                for (Component c : jmc.getComponents())
                {
                    if (c instanceof JTModule)
                    {
                        JTModule mm = (JTModule) c;
                        
                        if (mm.isSelected())
                            removeModule(mm);
                    }
                }
            }
            
        }
    }
    
    public boolean isEnabled(Object sender) 
    {
    	Object key = getValue(ACTION_COMMAND_KEY);
        if (key==DELETE_UNUSED) {
            PModuleContainer t = getTarget();
            return t != null && PatchUtils.hasUnusedModules(t);
        } else if (key == DELETE) {
        	if (sender != null && (sender instanceof JTModule)
        			&& !((JTModule)sender).isEnabled())
        		return false;
        	else
        		return true;
        } else if (key == SELECT_ALL) {
        	return true;
        } else {
        	return false;
        }
    }

    private void deleteUnusedModules() {
        PModuleContainer mc = getTarget();
        if (mc != null)
        {
            UndoableEditSupport ues = mc.getPatch().getUndoableEditSupport();
            try
            {
                if (ues != null)
                    ues.beginUpdate();
            
                while (PatchUtils.removeUnusedModules(mc)>0);
            } 
            finally
            {
                if (ues != null)
                    ues.endUpdate();
            }
        }
    }
    
    private void removeModule(JTModule m)
    {
        PModule nm = m.getModule();
        
        if (nm != null && nm.getParentComponent() != null)
        {
            nm.getParentComponent().remove(nm);
        }
    }


    
}