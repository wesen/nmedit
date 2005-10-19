// http://nmedit.sourceforge.net
    
package nomad.application.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.ToolTipManager;

import nomad.application.Run;
import nomad.com.Synth;
import nomad.com.SynthConnectionStateListener;
import nomad.com.SynthException;
import nomad.model.descriptive.ModuleDescriptions;
import nomad.model.descriptive.substitution.XMLSubstitutionReader;
import nomad.patch.Patch;

public class Nomad extends JFrame implements SynthConnectionStateListener {
    public static String creatorProgram = "nomad";
    public static String creatorVersion = "v0.1";
    public static String creatorRelease = "";
    
    Synth synth = new Synth();

	JFileChooser fileChooser = new JFileChooser("./src/data/patches/");
	
	JMenuBar menuBar = null;
	JMenu menuFile = null;
	JMenuItem menuExitItem,	menuNewItem, menuOpenItem, menuCloseItem, menuCloseAllItem = null;
	JMenuItem menuSaveItem, menuSaveAsItem, menuSaveAllItem = null;
	JMenu menuSynth = null;
	JMenuItem menuSynthSetup = null;
	JMenuItem menuSynthConnectionMenuItem = null;

	JPanel toolPanel = null;
	JTabbedPane tabbedPane = null;

	JButton button = null;

	JPanel panelMain = null;

    class NewListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Patch patch = new Patch();
            JPanel tab = Patch.createPatch("", patch);
            tabbedPane.add("new" + (tabbedPane.getTabCount()+1), tab);
            tabbedPane.setSelectedComponent(tab);
			tabbedPane.getSelectedComponent().setName(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
        }
    }

    class ExitListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	Nomad.this.processEvent(new WindowEvent(Nomad.this, WindowEvent.WINDOW_CLOSING));
        }
    }

	class FileLoadListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				String name = fileChooser.getSelectedFile().getName();
				name = name.substring(0,name.indexOf(".pch"));
                Patch patch = new Patch();
	            JPanel tab = Patch.createPatch(fileChooser.getSelectedFile().getPath(), patch);
				tabbedPane.add(name,tab);
				tabbedPane.setSelectedComponent(tab);
				tabbedPane.getSelectedComponent().setName(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
			}
		}
	}

	class FileSaveAsListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			fileChooser.setSelectedFile(new File(tabbedPane.getSelectedComponent().getName() + "_new.pch"));
			if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
				OutputStream stream;
				try {
					stream = new FileOutputStream(fileChooser.getSelectedFile());
//					stream.write(((Patch)tabbedPane.getSelectedComponent()).savePatch().toString().getBytes());
					stream.flush();
					stream.close();
				}
				catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	class SynthConnectionMenuItemListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			try {
				if (!synth.isConnected())
					synth.connect();
				else
					synth.disconnect();
			} catch (SynthException cause) {
				cause.printStackTrace();
			}
		}
	}
	
	class SetupSynthListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			ComPortSettingsDialog.invokeDialog(synth.getCompPort());
		}
	}

	class FileCloseListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			tabbedPane.remove(tabbedPane.getSelectedComponent());
		}
	}

	class ExitWindowListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			if (synth.isConnected())
				try {
					System.out.println("Closing midi connection.");
					synth.disconnect();
				} catch (SynthException e1) {
					System.out.println("Synth exception while exit:"+e1);
					e1.printStackTrace();
				}
			System.exit(0);
		}
	}
	
	public Nomad() {
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
		ModuleToolbar moduleToolbar = new ModuleToolbar();
		
        ToolTipManager.sharedInstance().setInitialDelay(0);
        
        this.setTitle(creatorProgram + " " + creatorVersion + " " + creatorRelease);

        this.setJMenuBar(createMenu());

// Main panel
		panelMain = new JPanel();
		panelMain.setLayout(new BorderLayout());

// TabbedPane
		tabbedPane = new JTabbedPane();
		panelMain.add(tabbedPane, BorderLayout.CENTER);

		this.getContentPane().add(panelMain,BorderLayout.CENTER);
		this.getContentPane().add(moduleToolbar, BorderLayout.NORTH);

		this.addWindowListener(new ExitWindowListener());

		this.setSize(1024, 768);
		
        Patch patch = new Patch();
        JPanel tab = Patch.createPatch("src/data/patches/all.pch", patch);
        tabbedPane.add("all.pch", tab);
		tabbedPane.setSelectedComponent(tab);
		tabbedPane.getSelectedComponent().setName(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));

//        Patch patch = new Patch();
//        tabbedPane.add("new" + (tabbedPane.getTabCount()+1),patch.createPatch(""));
//        tabbedPane.setSelectedComponent(patch);
//        tabbedPane.getSelectedComponent().setName(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
		
		// subscribe for connection events
		synth.addSynthConnectionStateListener(this);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

	public JMenuBar createMenu() {
		menuBar = new JMenuBar();
	
		menuFile = new JMenu("File");
			menuNewItem = menuFile.add("New");
			menuOpenItem = menuFile.add("Open...");
			menuCloseItem = menuFile.add("Close");
//			menuCloseAllItem = menuFile.add("Close All");
//			menuFile.addSeparator();
//			menuSaveItem = menuFile.add("Save");
			menuSaveAsItem = menuFile.add("Save As...");
//			menuSaveAllItem = menuFile.add("Save All");
			menuFile.addSeparator();
			menuExitItem = menuFile.add("Exit");

		menuBar.add(menuFile);
		
        menuNewItem.addActionListener(new NewListener());
        menuExitItem.addActionListener(new ExitListener());
		menuOpenItem.addActionListener(new FileLoadListener());
		menuSaveAsItem.addActionListener(new FileSaveAsListener());
		menuCloseItem.addActionListener(new FileCloseListener());


		menuSynth = new JMenu("Synth");
			menuSynthSetup = menuSynth.add("Setup");
			menuSynthConnectionMenuItem = menuSynth.add("Connect");
		
		menuBar.add(menuSynth);
		menuSynthSetup.addActionListener(new SetupSynthListener());
		menuSynthConnectionMenuItem.addActionListener(new SynthConnectionMenuItemListener());
		
		return menuBar; 
	}

	public static void main(String[] args) {
		try {
//			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) {
		}

		/* Default theme */
		System.setProperty("swing.metalTheme", "steel");

		new Nomad();
	}

	public void synthConnectionStateChanged(Synth synth) {
		this.menuSynthConnectionMenuItem.setText(
			synth.getCompPort().isPortOpen()?"Disconnect":"Connect"
		);
	}
}
