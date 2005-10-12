package nomad.application.ui;

import java.awt.GridLayout;
import java.awt.Insets;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import nomad.model.descriptive.DGroup;
import nomad.model.descriptive.DSection;
import nomad.model.descriptive.ModuleDescriptions;

public class ModuleToolbar extends JTabbedPane
{
  private JPanel[] paneGroups;
  private Vector buttons;

  public ModuleToolbar(ModuleDescriptions moduleDescriptions)
  {
    super(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
    paneGroups = new JPanel[moduleDescriptions.getGroupCount()];
    buttons = new Vector();
    
    for (int gi=0;gi<moduleDescriptions.getGroupCount();gi++) {
    	DGroup group = moduleDescriptions.getGroup(gi);
    	
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
        	DSection section = group.getSection(si);
        	
        	for (int mi=0;mi<section.getModuleCount();mi++) {
                JButton btn = new ModuleToolbarButton(section.getModule(mi));
                toolbar.add(btn);
                buttons.add(btn);
        	}

        	if (si<group.getSectionCount()-1)
        		toolbar.addSeparator();        		
        }
       
    }

    //setupDragModule();

  }

  public Vector getButtons()
  { return buttons; }
/*
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

