package editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import nomad.application.Run;
import nomad.application.ui.ModuleToolbar;
import nomad.gui.model.ModuleGUIBuilder;
import nomad.gui.model.UIFactory;
import nomad.gui.model.component.AbstractUIComponent;
import nomad.misc.ImageTracker;
import nomad.misc.SliceImage;
import nomad.model.descriptive.DModule;
import nomad.model.descriptive.ModuleDescriptions;
import nomad.model.descriptive.substitution.XMLSubstitutionReader;
import nomad.plugin.PluginManager;
import nomad.plugin.cache.XMLUICacheWriter;
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
	
	private HashMap modules = new HashMap();

	JMenuBar menuBar = null;
	JMenu menuFile = null;
	JMenuItem menuExitItem,	menuNewItem, menuOpenItem, menuCloseItem = null;
	JMenuItem menuSaveItem, menuSaveAsItem = null;
	
	WorkBenchPane workBench = null;
	ValueTablePane valuePane = null;
	PropertyTablePane propertyPane = null;
	JPanel livePane = null;
	ClassPane classPane = null;
	UIFactory theUIFactory = new ClassicThemeFactory();
	
	ImageTracker theImageTracker = null;
	
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
		SliceImage.createSliceImage("src/data/images/toolbar-icons.gif").feedImageTracker(theImageTracker);
		SliceImage.createSliceImage("src/data/images/io-icons.gif").feedImageTracker(theImageTracker);
		SliceImage.createSliceImage("src/data/images/button-icons.gif").feedImageTracker(theImageTracker);

		theUIFactory = PluginManager.getDefaultUIFactory();
		theUIFactory.getImageTracker().addFrom(theImageTracker);

		// load module/connector icons
		ModuleDescriptions.model.loadImages(theImageTracker);

		// build toolbar
		Run.statusMessage("building toolbar");
		ModuleToolbar moduleToolbar = new ModuleToolbar(false);
		moduleToolbar.addModuleButtonClickListener(new ModuleButtonClickListener());

		// create gui builder
		Run.statusMessage("ui builder");	
		ModuleGUIBuilder.createGUIBuilder(theUIFactory);

		
		
		
		
		/*
		
		// load substitutions
		String loadfile = "src/data/xml/substitutions.xml"; 
		Run.statusMessage(loadfile);
		XMLSubstitutionReader subsReader = new XMLSubstitutionReader(loadfile);
		
		// load module descriptors
		loadfile = "src/data/xml/modules.xml";
		Run.statusMessage(loadfile);
		ModuleDescriptions.init(loadfile, subsReader);

		// load connector icons
		Run.statusMessage("slice:io-icons.gif");
		ModuleDescriptions.model.loadConnectorIconsFromSlice("src/data/images/io-icons.gif");
		
		// load toolbar icons
		Run.statusMessage("slice:toolbar-icons.gif");
		ModuleDescriptions.model.loadModuleIconsFromSlice("src/data/images/toolbar-icons.gif");

		// load plugin names
		Run.statusMessage("Loading Plugin Manager");
		PluginManager.init();*/
	/*	
		theUIFactory = PluginManager.getDefaultUIFactory(); 

		// image tracker
		Run.statusMessage("loading imagetracker");
		SliceImage.createSliceImage("src/data/images/toolbar-icons.gif").feedImageTracker(theUIFactory.getImageTracker());
		SliceImage.createSliceImage("src/data/images/io-icons.gif").feedImageTracker(theUIFactory.getImageTracker());
		SliceImage.createSliceImage("src/data/images/button-icons.gif").feedImageTracker(theUIFactory.getImageTracker());

		// create gui builder
		Run.statusMessage("GUIBuilder");	
		ModuleGUIBuilder.createGUIBuilder(theUIFactory);

		// build toolbar
		Run.statusMessage("building toolbar");
		ModuleToolbar moduleToolbar = new ModuleToolbar(false);
*/
		this.add(BorderLayout.NORTH, moduleToolbar);

		valuePane = new ValueTablePane();
		propertyPane = new PropertyTablePane();
		workBench = new WorkBenchPane(valuePane, propertyPane);
		propertyPane.setWorkBench(workBench);
		
		livePane = new JPanel();
		livePane.setLayout(new BorderLayout());
		livePane.add(BorderLayout.SOUTH, valuePane);
		livePane.add(BorderLayout.CENTER, propertyPane);

		classPane = new ClassPane(theUIFactory);
		classPane.addCreateUIElementListener(workBench);
		
		this.add(BorderLayout.EAST, livePane);
		this.add(BorderLayout.WEST, classPane);
		this.add(BorderLayout.CENTER, workBench);
		
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
		this.setJMenuBar(menuBar);

        menuExitItem.addActionListener(new ExitListener());
		this.addWindowListener(new ExitWindowListener());
		
		// create all modules
		Collection moduleCollection = ModuleDescriptions.model.getModules();
		Iterator iter = moduleCollection.iterator();
		while (iter.hasNext()) {
			DModule info = (DModule) iter.next();
			Run.statusMessage("module:"+info.getName());
			modules.put(info, ModuleUIBuilder.buildModuleUI(theUIFactory, info));
		}
	}
	
	class SaveItemListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String filename = "./src/plugin/classictheme/ui.xml";

			if (JOptionPane.showConfirmDialog(UIEditor.this,
				    "The file '"+filename+"' will be written.\nShall I go on ?",
				    "Warning",
				    JOptionPane.YES_NO_OPTION) // info message
		       != JOptionPane.YES_OPTION
		    )
				return;
			
			
			UIXMLFileWriter xml = null;
			try {
				xml = new UIXMLFileWriter(filename);

				Collection moduleCollection = ModuleDescriptions.model.getModules();
				Iterator iter = moduleCollection.iterator();
				while (iter.hasNext()) {
					DModule module = (DModule) iter.next();
					xml.beginModuleElement(module); // begin module element
					// get module pane
					ModulePane mpane = (ModulePane) modules.get(module);
					Iterator iterc = mpane.getModuleComponents().getAllComponents();
					while (iterc.hasNext()) {
						xml.writeComponent((AbstractUIComponent)iterc.next());	
					}
					
					xml.endTag(); // finish module element
				}
			
				xml.close();
				
				//TODO let nomad handle cache itself
				// done - now build cache
				
				XMLUICacheWriter.writeCache(filename, filename.replaceAll("xml","cache"));
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class LoadItemListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			/*
			String filename = "./src/plugin/classictheme/ui.xml";

			Document doc = XMLReader.readDocument(filename, false/*false=no validating*);

			if (doc!=null) {
				try {
					
				NodeList nlist = doc.getElementsByTagName("module");
				for (int i=0;i<nlist.getLength();i++) {
					// for each element 'module'
					Node node = nlist.item(i);
					String moduleid = (new XMLAttributeReader(node)).getAttribute("id");
					DModule moduleinfo = ModuleDescriptions.model.getModuleByKey(moduleid);
					
					ModulePane mpane = (ModulePane) modules.get(moduleinfo);
					
					NodeList nlist2 = node.getChildNodes();
					
					for (int j=0;j<nlist2.getLength();j++){
						Node componentnode = nlist2.item(j);				
						if (componentnode!=null && componentnode.getNodeType()==Node.ELEMENT_NODE) {
							// for each component
							String componentid = (new XMLAttributeReader(componentnode)).getAttribute("id");
							AbstractUIComponent componentui = mpane.getUIComponent(componentid);
							
							NodeList nlist3 = componentnode.getChildNodes();
							for (int k=0;k<nlist3.getLength();k++) {
								Node propertynode = nlist3.item(k);
								if (propertynode!=null && propertynode.getNodeType()==Node.ELEMENT_NODE) {
									String propertyname = (new XMLAttributeReader(propertynode)).getAttribute("name");
									String propertyvalue = (new XMLAttributeReader(propertynode)).getAttribute("value");
									// 	for each property
									try {
										componentui.getPropertyByName(propertyname).setValue(propertyvalue);
									} catch (InvalidValueException e) {
										System.out.println("Invalid pair ("+propertyname+","+propertyvalue+")");
									} catch (Exception e) {
										System.out.println("Property not found: '"+propertyname+"'");
									}
								}
							}
						}
					}
				}
				
				} catch (XMLAttributeValidationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}*/
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

	private class ModuleButtonClickListener implements nomad.application.ui.ModuleToolbarEventListener {
		public void toolbarModuleSelected(DModule module) {
			workBench.setModule((ModulePane)modules.get(module));
		}
	}

}
