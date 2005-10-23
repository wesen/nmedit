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
import nomad.gui.BasicUI;
import nomad.gui.UIFactory;
import nomad.gui.property.Property;
import nomad.gui.property.PropertyMap;
import nomad.model.descriptive.DModule;
import nomad.model.descriptive.ModuleDescriptions;
import nomad.model.descriptive.substitution.XMLSubstitutionReader;
import plugin.classictheme.ClassicThemeFactory;

public class UIEditor extends JFrame {

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
	
	public UIEditor() {
		super("Nomad UI Editor");
		this.setSize(640, 480);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
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
		
		// build toolbar
		Run.statusMessage("building toolbar");
		ModuleToolbar moduleToolbar = new ModuleToolbar(false);
		moduleToolbar.addModuleButtonClickListener(new ModuleButtonClickListener());
		
		this.add(BorderLayout.NORTH, moduleToolbar);

		valuePane = new ValueTablePane();
		propertyPane = new PropertyTablePane();
		workBench = new WorkBenchPane(valuePane, propertyPane);
		
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
			//menuOpenItem = menuFile.add("Open...");
			//menuCloseItem = menuFile.add("Close");
			//menuFile.addSeparator();
			menuSaveItem = menuFile.add("Save");
			//menuSaveAsItem = menuFile.add("Save As...");
			menuFile.addSeparator();
			menuExitItem = menuFile.add("Exit");
			
			menuSaveItem.addActionListener(new SaveItemListener());

		menuBar.add(menuFile);
		this.setJMenuBar(menuBar);

        menuExitItem.addActionListener(new ExitListener());
		this.addWindowListener(new ExitWindowListener());
		
		// create all modules
		Collection moduleCollection = ModuleDescriptions.model.getModules();
		DModule info = null;
		for (Iterator iter = moduleCollection.iterator(); iter.hasNext(); info = (DModule) iter.next()) {
			if (info!=null) {
				Run.statusMessage("module:"+info.getName());
				modules.put(info, ModuleUIBuilder.buildModuleUI(theUIFactory, info));
			}
		}
	}
	
	class SaveItemListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
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
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (xml!=null) {
				Collection moduleCollection = ModuleDescriptions.model.getModules();
				DModule info = null;
				for (Iterator iter = moduleCollection.iterator(); iter.hasNext(); info = (DModule) iter.next()) {
					if (info!=null) {
						xml.beginTagStart("module");
						xml.addAttribute("id", ""+info.getModuleID());
						xml.beginTagFinish(true);
						ModulePane mpane = (ModulePane) modules.get(info);
						for (int i=0;i<mpane.getUIComponentCount();i++) {
							BasicUI uicomponent = mpane.getUIComponent(i);
							PropertyMap pmap = uicomponent.getProperties();
							xml.beginTagStart("component");
							xml.addAttribute("id", ""+uicomponent.getName());
							xml.beginTagFinish(true);
							
							String[] names = pmap.getPropertyNames();
							for (int j=0;j<names.length;j++) {
								Property p = pmap.getProperty(names[j]);
								xml.beginTagStart("property");
								xml.addAttribute("name", ""+names[j]);
								xml.addAttribute("value", ""+p.getValue());
								xml.beginTagFinish(false);
							}
							
							xml.endTag();
						}
						xml.endTag();
					}
				}

				
				xml.close();						
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

	private class ModuleButtonClickListener implements nomad.application.ui.ModuleToolbarEventListener {
		public void toolbarModuleSelected(DModule module) {
			workBench.setModule((ModulePane)modules.get(module));
		}
	}

}
