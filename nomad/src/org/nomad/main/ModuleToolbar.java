package org.nomad.main;

import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import org.nomad.xml.dom.module.DToolbarGroup;
import org.nomad.xml.dom.module.DToolbarSection;
import org.nomad.xml.dom.module.ModuleDescriptions;


/**
 * @author Christian Schneider
 */
public class ModuleToolbar extends JTabbedPane
{
  private JPanel[] paneGroups;
  private Vector buttons;
  //private boolean draggingSupport = true;
  private Vector moduleButtonClickListeners = new Vector();

  /**
   * Creates the ModuleToolbar using ModuleDescriptions.model
   * @see ModuleDescriptions#model
   */
  public ModuleToolbar() {
	  this(true);
  }
  
  public ModuleToolbar(boolean draggingSupport) {
	  this(ModuleDescriptions.model, draggingSupport);
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
    //this.draggingSupport = draggingSupport;
    paneGroups = new JPanel[moduleDescriptions.getGroupCount()];
    buttons = new Vector();
    
    ButtOnClickListener buttonClickListener = new ButtOnClickListener();
    
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
                if (!draggingSupport)
                	btn.addActionListener(buttonClickListener);
                	
                btn.setAllowDragging(draggingSupport);
                toolbar.add(btn);
                buttons.add(btn);
        	}

        	if (si<group.getSectionCount()-1)
        		toolbar.addSeparator();        		
        }
       
    }

    //setupDragModule();

  }
  
  public void addModuleButtonClickListener(ModuleToolbarEventListener listener) {
	  moduleButtonClickListeners.add(listener);
  }
  
  public void removeModuleButtonClickListener(ModuleToolbarEventListener listener) {
	  moduleButtonClickListeners.remove(listener);
  }
  
  private class ButtOnClickListener implements ActionListener {

	public void actionPerformed(ActionEvent event) {
		ModuleToolbarButton btn = (ModuleToolbarButton) event.getSource();
		for (int i=0;i<moduleButtonClickListeners.size();i++) {
			ModuleToolbarEventListener listener = 
				(ModuleToolbarEventListener) moduleButtonClickListeners.get(i);
			listener.toolbarModuleSelected(btn.getModuleDescription());
		}
	}
	  
  }
  /*
  public Vector getButtons()
  { return buttons; }

  private void setupDragModule()
  {
    private

    for (Iterator iterator = getButtons().iterator();iterator.hasNext();)
    {
      ModuleButton btn = (ModuleButton) iterator.next();
      btn.addActionListener();
    }
  }*/
	
}

