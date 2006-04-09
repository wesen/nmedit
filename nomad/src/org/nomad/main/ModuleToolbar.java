package org.nomad.main;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import org.nomad.xml.dom.module.DGroup;
import org.nomad.xml.dom.module.DSection;
import org.nomad.xml.dom.module.ModuleDescriptions;


/**
 * @author Christian Schneider
 */
public class ModuleToolbar extends JTabbedPane implements ActionListener
{
  private JToolBar[] paneGroups;
  private ArrayList<ModuleToolbarButton> buttons;
  //private boolean draggingSupport = true;
  private ArrayList<ModuleToolbarEventListener> 
  	moduleButtonClickListeners = new ArrayList<ModuleToolbarEventListener>();

  private boolean draggingSupport = true;

  /**
   * Creates the ModuleToolbar using ModuleDescriptions.model
   * @see ModuleDescriptions#model
   */
  public ModuleToolbar() {
	  this(true);
  }
  
  public ModuleToolbar(boolean draggingSupport) {
	  this(ModuleDescriptions.sharedInstance(), draggingSupport);
  }

  public void setAllowDragging(boolean enabled) {
	this.draggingSupport = enabled;
  }
	
	public boolean hasDraggingSupport() {
		return draggingSupport;
	}

  /**
   * Creates the toolbar using the specifed module descriptions.
   * @param moduleDescriptions the descriptions
   * @param draggingSupport if true, dragging support is enabled,
   * otherwise a client can listen to button click events.  
   */
  public ModuleToolbar(ModuleDescriptions moduleDescriptions, boolean draggingSupport)
  {
    super(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
    this.draggingSupport = draggingSupport;
    //this.draggingSupport = draggingSupport;
    paneGroups = new JToolBar[moduleDescriptions.getGroupCount()];
    buttons = new ArrayList<ModuleToolbarButton>();

    for (int gi=0;gi<moduleDescriptions.getGroupCount();gi++) {
    	DGroup group = moduleDescriptions.getGroup(gi);
    	
        // add tab
        JToolBar toolbar = new JToolBar();
        addTab(group.getShortName(), null, toolbar, "Group "+group.getName());
        paneGroups[gi] = toolbar;
        toolbar.setMargin(new Insets(0, 0, 0, 0));
        toolbar.setFloatable(false);
        
        for (int si=0;si<group.getSectionCount();si++) {
        	DSection section = group.getSection(si);
        	
        	for (int mi=0;mi<section.getModuleCount();mi++) {
                ModuleToolbarButton btn = new ModuleToolbarButton(this, section.getModule(mi));
                btn.addActionListener(this);
                toolbar.add(btn);
                buttons.add(btn);
        	}

        	if (si<group.getSectionCount()-1)
        		toolbar.addSeparator();        		
        }
       
    }
  }
  
  public void addModuleButtonClickListener(ModuleToolbarEventListener listener) {
	  moduleButtonClickListeners.add(listener);
  }
  
  public void removeModuleButtonClickListener(ModuleToolbarEventListener listener) {
	  moduleButtonClickListeners.remove(listener);
  }
  
	public void actionPerformed(ActionEvent event) {
		if (!draggingSupport) {
			ModuleToolbarButton btn = (ModuleToolbarButton) event.getSource();
			for (int i=0;i<moduleButtonClickListeners.size();i++) {
				moduleButtonClickListeners.get(i)
					.toolbarModuleSelected(btn.getModuleDescription());
			}
		}
	}
}

