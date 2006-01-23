package org.nomad.main;

import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import org.nomad.xml.dom.module.DToolbarGroup;
import org.nomad.xml.dom.module.DToolbarSection;
import org.nomad.xml.dom.module.ModuleDescriptions;


/**
 * @author Christian Schneider
 */
public class ModuleToolbar extends JTabbedPane implements ActionListener
{
  private JPanel[] paneGroups;
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
    paneGroups = new JPanel[moduleDescriptions.getGroupCount()];
    buttons = new ArrayList<ModuleToolbarButton>();
    
    for (int gi=0;gi<moduleDescriptions.getGroupCount();gi++) {
    	DToolbarGroup group = moduleDescriptions.getGroup(gi);
    	
    	JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(1,1));
        paneGroups[gi] = pane;

        // add tab
        addTab(group.getShortName(), null, pane, "Group "+group.getName());
        
        JToolBar toolbar = new JToolBar();
        toolbar.setMargin(new Insets(0, 0, 0, 0));
        toolbar.setFloatable(false);
        pane.add(toolbar);
        
        for (int si=0;si<group.getSectionCount();si++) {
        	DToolbarSection section = group.getSection(si);
        	
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

