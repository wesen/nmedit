package net.sf.nmedit.nomad.main.ui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.nomad.xml.dom.module.DGroup;
import net.sf.nmedit.nomad.xml.dom.module.DSection;
import net.sf.nmedit.nomad.xml.dom.module.ModuleDescriptions;


/**
 * @author Christian Schneider
 */
public class ModuleToolbar extends HeaderSection implements ActionListener,
ChangeListener
{
  private JComponent[] paneGroups;
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

   
    //private ClippedList<DGroup>groups;

    final static Color cl = Color.decode("#CED4E2");
    
  /**
   * Creates the toolbar using the specifed module descriptions.
   * @param moduleDescriptions the descriptions
   * @param draggingSupport if true, dragging support is enabled,
   * otherwise a client can listen to button click events.  
   */
  public ModuleToolbar(ModuleDescriptions moduleDescriptions, boolean draggingSupport)
  {
      super( new GlowTabbedPane() );
      JComponent panicons;
      GlowTabbedPane tabbedPane = (GlowTabbedPane) getHeader();
      tabbedPane.addChangeListener(this);
      
   // super(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
      
      panicons = getContentPane();
      panicons.setLayout(new CardLayout());
      /*
      panicons.setBackground(null);
      panicons.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
      panicons.setOpaque(false);*/
      
    this.draggingSupport = draggingSupport;
    //this.draggingSupport = draggingSupport;
    paneGroups = new JComponent[moduleDescriptions.getGroupCount()];
    buttons = new ArrayList<ModuleToolbarButton>();

    setBackground(null);
    setOpaque(false);
    //setLayout(clayout);

    
/*
    ClickLabel title = new ClickLabel(group.getShortName(), cardID);
    title.setToolTipText("Group "+group.getName());
*/

    tabbedPane.setLayout(new GridLayout(0,moduleDescriptions.getGroupCount()/2));

    for (int gi=0;gi<moduleDescriptions.getGroupCount();gi++) {
        DGroup group = moduleDescriptions.getGroup(gi);

        tabbedPane.addTab(group.getShortName(), group);
    }
    int maxWidth = tabbedPane.getPreferredSize().width;
    
    for (int gi=0;gi<moduleDescriptions.getGroupCount();gi++) {
    	DGroup group = moduleDescriptions.getGroup(gi);
        
        JComponent toolbar = new JPanel();
        
        toolbar.setLayout(new SpringLayout());
        paneGroups[gi] = toolbar;
        
        toolbar.setOpaque(false);
        toolbar.setBorder(null);

        for (int si=0;si<group.getSectionCount();si++) {
        	DSection section = group.getSection(si);
        	
        	for (int mi=0;mi<section.getModuleCount();mi++) {
                ModuleToolbarButton btn = new ModuleToolbarButton(this, section.getModule(mi));

                btn.setContentAreaFilled(false);
                btn.setBorderPainted(false);
                
                btn.addActionListener(this);
                toolbar.add(btn);
                buttons.add(btn);
        	}

            if (si+1<group.getSectionCount())
            {
                    JSeparator sep = new JSeparator(JSeparator.VERTICAL);
    
                    Dimension d = sep.getMinimumSize();
                    d.width = 4;
                    sep.setMinimumSize(d);
                    d = sep.getMaximumSize();
                    d.width = 4;
                    sep.setMaximumSize(d);
                    d = sep.getPreferredSize();
                    d.width = 4;
                    d.height=5;
                    sep.setPreferredSize(d);
                    toolbar.add(sep);
            }
        }

        makeCompactGrid(toolbar, maxWidth);
        
        panicons.add(toolbar, group.getShortName());        
    }

    //getContentPane().add(panicons);
    
  }

  public static void makeCompactGrid(Container parent, int maxWidth) 
  {
      SpringLayout layout;
      try 
      {
        layout = (SpringLayout) parent.getLayout();
      } 
      catch (ClassCastException exc) 
      {
          System.err.println("Not a SpringLayout.");
          return;
      }

      Spring xOffset = Spring.constant(0);
      Spring yOffset = Spring.constant(0);

      Spring width  = Spring.constant(0);
      Spring lineHeight = Spring.constant(0);
      
      int componentsInLine = 0;
      
      for (int i=0;i<parent.getComponentCount();i++)
      {
          componentsInLine++;
          Component c = parent.getComponent(i);
          SpringLayout.Constraints cons = layout.getConstraints(c);
          Dimension d = c.getPreferredSize();
          
          if (d.width<4)
          {
              d.width = 4;
          }
          
          cons.setWidth(Spring.constant(d.width));
          cons.setHeight(Spring.constant(d.height));

          lineHeight = Spring.max(Spring.constant(d.height), lineHeight);
          
          cons.setX(xOffset);
          cons.setY(yOffset);

          Spring nextX = Spring.sum(xOffset, Spring.constant(d.width));
          if (nextX.getValue()>maxWidth)
          {
              width = Spring.max(width, nextX);
              nextX = Spring.constant(0);
              yOffset = Spring.sum(yOffset, lineHeight);
              lineHeight = Spring.constant(0);
              componentsInLine = 0;
          }
          
          //width = Spring.max(width,cons.getWidth()); 
          //height = Spring.max(height,cons.getHeight());

          xOffset = nextX;
      }
      width = Spring.max(width, xOffset);

      if (componentsInLine>0)
      {
          yOffset = Spring.sum(yOffset, lineHeight);
      }
      
    //Set the parent's size.
    SpringLayout.Constraints pCons = layout.getConstraints(parent);
    pCons.setConstraint(SpringLayout.SOUTH, yOffset);
    pCons.setConstraint(SpringLayout.EAST, width);
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

    public void stateChanged( ChangeEvent e )
    {
        GlowTabbedPane tpane = (GlowTabbedPane) getHeader();
        DGroup selection = (DGroup)tpane.getSelection();
        
        if (selection!=null)
        {
            JComponent pane = getContentPane();
            ((CardLayout)pane.getLayout())
            .show(pane, selection.getShortName());
        }
    }
}

