package editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import nomad.application.Run;
import nomad.application.ui.ModuleToolbar;
import nomad.model.descriptive.DModule;
import nomad.model.descriptive.ModuleDescriptions;
import nomad.model.descriptive.substitution.XMLSubstitutionReader;

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

	JMenuBar menuBar = null;
	JMenu menuFile = null;
	JMenuItem menuExitItem,	menuNewItem, menuOpenItem, menuCloseItem = null;
	JMenuItem menuSaveItem, menuSaveAsItem = null;
	
	WorkBenchPane workBench = null;
	
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
		
		workBench = new WorkBenchPane();
		this.add(BorderLayout.CENTER, workBench);
		
		// menu

		menuBar = new JMenuBar();

		menuFile = new JMenu("File");
			menuNewItem = menuFile.add("New");
			menuOpenItem = menuFile.add("Open...");
			menuCloseItem = menuFile.add("Close");
			menuFile.addSeparator();
			menuSaveItem = menuFile.add("Save");
			menuSaveAsItem = menuFile.add("Save As...");
			menuFile.addSeparator();
			menuExitItem = menuFile.add("Exit");

		menuBar.add(menuFile);
		this.setJMenuBar(menuBar);

        menuExitItem.addActionListener(new ExitListener());
		this.addWindowListener(new ExitWindowListener());
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
			workBench.setModule(module);
		}
		
	}

}
