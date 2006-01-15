package org.nomad.editor.app;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.nomad.editor.views.classes.NomadClassesView;
import org.nomad.editor.views.property.NomadPropertyEditor;
import org.nomad.editor.views.visual.NomadVisualEditor;
import org.nomad.main.ModuleToolbar;
import org.nomad.main.run.Run;
import org.nomad.patch.ModuleSection;
import org.nomad.plugin.PluginManager;
import org.nomad.theme.ModuleGUIBuilder;
import org.nomad.theme.NomadClassicColors;
import org.nomad.theme.UIFactory;
import org.nomad.util.misc.ImageTracker;
import org.nomad.xml.XMLFileWriter;
import org.nomad.xml.dom.module.DModule;
import org.nomad.xml.dom.module.ModuleDescriptions;
import org.nomad.xml.dom.substitution.XMLSubstitutionReader;

import plugin.classictheme.ClassicThemeFactory;

public class UIEditor extends JFrame {
	
//	public ImageTracker defaultImageTracker = new ImageTracker();

	public static void main(String[] args) {
		UIEditor frame = new UIEditor();
		frame.validate();
	    // center window
	    Dimension screensz  = Toolkit.getDefaultToolkit().getScreenSize();
	    Dimension framesz   = frame.getSize();
	
	    framesz.height = Math.min(framesz.height, screensz.height);
	    framesz.width  = Math.min(framesz.width,  screensz.width);
	
	    frame.setLocation(
	      (screensz.width-framesz.width)/2,
	      (screensz.height-framesz.height)/2
	    );
	
	    // set close operation, then show window
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    frame.setVisible(true);
	}
	

	JPanel workspace = new JPanel() {
		
		{
			addComponentListener(new ComponentAdapter(){
				public void componentResized(ComponentEvent event) {checkChild();}
			});
			addContainerListener(new ContainerListener(){

				public void componentAdded(ContainerEvent arg0) {
					checkChild();
				}

				public void componentRemoved(ContainerEvent arg0) {
					repaint();
				}});
		}
		
		public void checkChild() {
			if (getComponentCount()>0) {
				Component c = getComponent(0);
				c.setLocation((getWidth()-c.getWidth())/2,(getHeight()-c.getHeight())/2);
			}
		}
	};

	JMenuBar menuBar = null;
	JMenu menuFile = null;
	JMenuItem menuExitItem,	menuNewItem, menuOpenItem, menuCloseItem = null;
	JMenuItem menuSaveItem, menuSaveAsItem = null;
	JMenu menuAlign = null;
	
	NomadClassesView classesView = new NomadClassesView();
	NomadPropertyEditor propertyEditor = new NomadPropertyEditor(this);
	NomadVisualEditor visualEditor = null;
	
	UIFactory theUIFactory = new ClassicThemeFactory();
	
	ImageTracker theImageTracker = null;

	private String fileName = "";
	
	public UIEditor() {
		
		super("Nomad UI Editor");
		this.setSize(640, 480);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		// load plugin names
		Run.statusMessage("Plugin Manager");
		PluginManager.init();

		// load substitutions
		Run.statusMessage("parameter substitutions");
		XMLSubstitutionReader subsReader = 
			new XMLSubstitutionReader("src/data/xml/substitutions.xml");
		
		// load module descriptors
		Run.statusMessage("module description");
		ModuleDescriptions.init("src/data/xml/modules.xml", subsReader);

		// feed image tracker
		Run.statusMessage("images");
		theImageTracker = new ImageTracker();
		try {
			theImageTracker.loadFromDirectory("src/data/images/slice/");
			theImageTracker.loadFromDirectory("src/data/images/single/");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		theUIFactory = PluginManager.getDefaultUIFactory();
		theUIFactory.setEditing(true);
		theUIFactory.getImageTracker().addFrom(theImageTracker);
		fileName = theUIFactory.getUIDescriptionFileName();

		// load module/connector icons
		ModuleDescriptions.model.loadImages(theImageTracker);

		// build toolbar
		Run.statusMessage("building toolbar");
		ModuleToolbar moduleToolbar = new ModuleToolbar(false);
		moduleToolbar.addModuleButtonClickListener(new ModuleButtonClickListener());

		// create gui builder
		Run.statusMessage("ui builder");	
		ModuleGUIBuilder.createGUIBuilder(theUIFactory);

		this.add(BorderLayout.NORTH, moduleToolbar);
		
		classesView.setFactory(theUIFactory);
		
		propertyEditor.setPreferredSize(new Dimension(200, 400));
				
		this.add(BorderLayout.EAST, propertyEditor);
		this.add(BorderLayout.WEST, classesView);
		this.add(BorderLayout.CENTER, workspace);
		workspace.setLayout(null);
		workspace.setSize(new Dimension(400,400));
		
		// menu

		menuBar = new JMenuBar();

		menuFile = new JMenu("File");
			//menuNewItem = menuFile.add("New");
			menuOpenItem = menuFile.add("Open...");
			//menuCloseItem = menuFile.add("Close");
			//menuFile.addSeparator();
			menuSaveItem = menuFile.add("Save");
			//menuSaveAsItem = menuFile.add("Save As...");
			menuFile.addSeparator();
			menuExitItem = menuFile.add("Exit");
			
			menuSaveItem.addActionListener(new SaveItemListener());
			menuOpenItem.addActionListener(new LoadItemListener());

		menuBar.add(menuFile);
		
		menuAlign = new JMenu("Align");
		/*
			// centers all selected components vertically
			menuAlign.add("Vertical (Center)").addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent event) {
					ModulePane mpane = workBench.getModulePane();
					ArrayList components = mpane.getSelectedComponents();
					for (int i=0;i<components.size();i++) {
						Component c = (Component) components.get(i);
						c.setLocation(c.getX(),
								(mpane.getHeight()-c.getHeight())/2
						);
					}
				}});
			
			// aligns all selected components at highest component
			menuAlign.add("Vertical (Highest)").addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent event) {
					ModulePane mpane = workBench.getModulePane();
					ArrayList components = mpane.getSelectedComponents();
					int ymin = mpane.getHeight();
					for (int i=0;i<components.size();i++) {
						Component c = (Component) components.get(i);
						if (c.getY()<ymin)
							ymin=c.getY();
					}

					for (int i=0;i<components.size();i++) {
						Component c = (Component) components.get(i);
						c.setLocation(c.getX(), ymin);
					}
				}});
			
			// aligns all selected components at lowest component
			menuAlign.add("Vertical (Lowest)").addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent event) {
					ModulePane mpane = workBench.getModulePane();
					ArrayList components = mpane.getSelectedComponents();
					int ymax = 0;
					for (int i=0;i<components.size();i++) {
						Component c = (Component) components.get(i);
						int bottom = c.getY()+c.getHeight();
						if (bottom>ymax)
							ymax=bottom;
					}

					for (int i=0;i<components.size();i++) {
						Component c = (Component) components.get(i);
						c.setLocation(c.getX(), ymax-c.getHeight());
					}
				}});
			
			// aligns all selected components at lowest component
			menuAlign.add("Vertical (Median)").addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent event) {
					ModulePane mpane = workBench.getModulePane();
					ArrayList components = mpane.getSelectedComponents();
					if (components.size()>1) {
						int ymedian = 0;

						for (int i=0;i<components.size();i++) {
							Component c = (Component) components.get(i);
							ymedian+=c.getY()+c.getHeight()/2;
						}
						
						ymedian /=components.size();
						
						for (int i=0;i<components.size();i++) {
							Component c = (Component) components.get(i);
							int current = c.getY()+c.getHeight()/2;
							c.setLocation(
								c.getLocation().x,
								c.getLocation().y+(ymedian-current)
							);
						}
					}
				}});
			
		menuAlign.addSeparator();

		// aligns all selected components at highest component
		menuAlign.add("Horizontal (Left)").addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				ModulePane mpane = workBench.getModulePane();
				ArrayList components = mpane.getSelectedComponents();
				int xmin = mpane.getWidth();
				for (int i=0;i<components.size();i++) {
					Component c = (Component) components.get(i);
					if (c.getX()<xmin)
						xmin=c.getX();
				}

				for (int i=0;i<components.size();i++) {
					Component c = (Component) components.get(i);
					c.setLocation(xmin, c.getY());
				}
			}});
		
		// aligns all selected components at lowest component
		menuAlign.add("Horizontal (Right)").addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				ModulePane mpane = workBench.getModulePane();
				ArrayList components = mpane.getSelectedComponents();
				int xmax = 0;
				for (int i=0;i<components.size();i++) {
					Component c = (Component) components.get(i);
					int right = c.getX()+c.getWidth();
					if (right>xmax)
						xmax=right;
				}

				for (int i=0;i<components.size();i++) {
					Component c = (Component) components.get(i);
					c.setLocation(xmax-c.getWidth(),c.getY());
				}
			}});
		
		// aligns all selected components at lowest component
		menuAlign.add("Horizontal (Median)").addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				ModulePane mpane = workBench.getModulePane();
				ArrayList components = mpane.getSelectedComponents();
				if (components.size()>1) {
					int xmedian = 0;

					for (int i=0;i<components.size();i++) {
						Component c = (Component) components.get(i);
						xmedian+=c.getX()+c.getWidth()/2;
					}
					
					xmedian /=components.size();
					
					for (int i=0;i<components.size();i++) {
						Component c = (Component) components.get(i);
						int current = c.getX()+c.getWidth()/2;
						c.setLocation(
							c.getLocation().x+(xmedian-current),
							c.getLocation().y
						);
					}
				}
			}});
		*/
		menuBar.add(menuAlign);
		
		this.setJMenuBar(menuBar);

        menuExitItem.addActionListener(new ExitListener());
		this.addWindowListener(new ExitWindowListener());
		
		pack();
	}
	
	
	class SaveItemListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			JFileChooser fileChooser = new JFileChooser(fileName);
			
			if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				fileName = fileChooser.getSelectedFile().getAbsolutePath();
				
				if (JOptionPane.showConfirmDialog(UIEditor.this,
					    "The file '"+fileName+"' will be written.\nShall I go on ?",
					    "Warning",
					    JOptionPane.YES_NO_OPTION) // info message
			       != JOptionPane.YES_OPTION
			    ) {
					return;
				} else {
	
					XMLFileWriter xml;
					try {
						xml = new XMLFileWriter(fileName,
							"<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
							"<!DOCTYPE ui-description SYSTEM \"ui-description.dtd\">"
						);
						ModuleGUIBuilder.instance.exportDom(xml);			
						xml.flush();
						xml.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}		
				}
			}			
		}
	}
	
	class LoadItemListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			JFileChooser fileChooser = new JFileChooser(fileName);
			
			if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				fileName = fileChooser.getSelectedFile().getAbsolutePath();

				if (JOptionPane.showConfirmDialog(UIEditor.this,
					    "Changes will be lost.\nShall I go on ?",
					    "Warning",
					    JOptionPane.YES_NO_OPTION) // info message
			       != JOptionPane.YES_OPTION
			    ) {
					return;
				} else {
					if (!ModuleGUIBuilder.instance.load(fileName)) {
						System.err.println("Failed!!!");
					}
				}
			}
		}
	}
	
	class ExitWindowListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}

    class ExitListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	UIEditor.this.processEvent(new WindowEvent(UIEditor.this, WindowEvent.WINDOW_CLOSING));
        }
    }

	private class ModuleButtonClickListener implements org.nomad.main.ModuleToolbarEventListener {
		public void toolbarModuleSelected(DModule module) {
			
			//workBench.setModule((ModulePane)modules.get(module));
			
			if (visualEditor!=null) {
				// write back
				ModuleGUIBuilder.rewriteDOM(visualEditor, visualEditor.getModuleInfo());
				
				visualEditor.setComponentPropertyEditor(null);
				workspace.remove(visualEditor);
			}

			visualEditor = new NomadVisualEditor(module);
			visualEditor.setComponentPropertyEditor(propertyEditor);
			visualEditor.setBackground(NomadClassicColors.MODULE_BACKGROUND);

			ModuleGUIBuilder.instance._createGUIComponents(visualEditor,null, module, false);
			
			//visualEditor.setLocation(0,0);
			visualEditor.setSize(ModuleSection.ModulePixDimension.PIXWIDTH,ModuleSection.ModulePixDimension.PIXHEIGHT*module.getHeight());
			//visualEditor.setNameLabel(module.getName());
			visualEditor.setVisible(true);
			
			workspace.add(visualEditor); 
			
		}
	}
	
}
