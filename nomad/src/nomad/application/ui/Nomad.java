// http://nmedit.sourceforge.net
    
package nomad.application.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

import nomad.application.Run;
import nomad.com.ComPort;
import nomad.com.ComPortFactory;
import nomad.com.NullComPort;
import nomad.com.Synth;
import nomad.com.SynthConnectionStateListener;
import nomad.com.SynthException;
import nomad.gui.PatchGUI;
import nomad.gui.model.ModuleGUIBuilder;
import nomad.gui.model.UIFactory;
import nomad.misc.ImageTracker;
import nomad.model.descriptive.ModuleDescriptions;
import nomad.model.descriptive.substitution.XMLSubstitutionReader;
import nomad.patch.Patch;
import nomad.plugin.NomadPlugin;
import nomad.plugin.PluginManager;

public class Nomad extends JFrame implements SynthConnectionStateListener {
    public static String creatorProgram = "nomad";
    public static String creatorVersion = "v0.1";
    public static String creatorRelease = "development build";
    
    Synth synth = null;

	JFileChooser fileChooser = new JFileChooser("./src/data/patches/");
	
	JMenuBar menuBar = null;
	JMenu menuFile = null;
	JMenuItem menuExitItem,	menuNewItem, menuOpenItem, menuCloseItem, menuCloseAllItem = null;
	JMenuItem menuSaveItem, menuSaveAsItem, menuSaveAllItem = null;
	JMenu menuSynth = null;
	JMenuItem menuSynthSetup = null;
	JMenuItem menuSynthConnectionMenuItem = null;
	JMenuItem menuSynthUploadPatchFromCurrentSlot = null;
		JMenu menuSelectComport = null;
		
	JMenu menuAppearance = null;
		JMenu documentViewMode = null;
			JRadioButtonMenuItem menuDocumentViewTabbed = null;
			JRadioButtonMenuItem menuDocumentViewMDI = null;
		
	JMenu menuHelp = null;
		JMenu menuHelpLookAndFeel = null;
		JMenuItem menuHelpPluginsList = null;
		JMenuItem menuHelpAbout = null;

	JPanel toolPanel = null;

	JButton button = null;

	JPanel panelMain = null;
	ImageTracker theImageTracker = null;
	
	private DocumentManager viewManager = null;

    class NewListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	Patch patch = new Patch();
            JPanel tab = Patch.createPatch("", patch);
            viewManager.addDocument("new" + (viewManager.getDocumentCount()+1), tab);
            viewManager.setSelectedDocument(tab);
            viewManager.getSelectedDocument().setName(viewManager.getTitleAt(viewManager.getSelectedDocumentIndex()));
        }
    }

    class ExitListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	Nomad.this.processEvent(new WindowEvent(Nomad.this, WindowEvent.WINDOW_CLOSING));
        }
    }

    class UploadPatchFromCurrentSlotListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	Patch p = synth.getCompPort().getPatchFromActiveSlot();
        	
        	if (p==null) {
        		System.err.println("no patch data");
        		return;
        	}
        	
            JPanel tab = Patch.createPatchUI(p);
            viewManager.addDocument("<uploaded>",tab);
            viewManager.setSelectedDocument(tab);
            viewManager.getSelectedDocument().setName(viewManager.getTitleAt(viewManager.getSelectedDocumentIndex()));
        	
        	//System.out.println(p);
        }
    }
    
    class SwitchDocumentViewAction implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (event.getSource()==menuDocumentViewMDI)
				((SelectableDocumentManager)viewManager).switchDocumentManager(false);
			else if (event.getSource()==menuDocumentViewTabbed)
				((SelectableDocumentManager)viewManager).switchDocumentManager(true);
		}
    }

	class FileLoadListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					String name = fileChooser.getSelectedFile().getName();

					name = name.substring(0,name.indexOf(".pch"));
					Patch patch = new Patch();
			        JPanel tab = Patch.createPatch(fileChooser.getSelectedFile().getPath(), patch);
			        viewManager.addDocument(name, tab);
			        viewManager.setSelectedDocument(tab);
			        viewManager.getSelectedDocument().setName(
			        		viewManager.getTitleAt(viewManager.getSelectedDocumentIndex())
			        );
			}
		}
	}

	class FileSaveAsListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			fileChooser.setSelectedFile(new File(viewManager.getSelectedDocument().getName() + "_new.pch"));
			if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
				OutputStream stream;
				try {
					stream = new FileOutputStream(fileChooser.getSelectedFile());
					PatchGUI patchGUI = (PatchGUI) viewManager.getSelectedDocument();
					
					stream.write(patchGUI.getPatch().savePatch().toString().getBytes());
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
			if (viewManager.getSelectedDocument()!=null)
				viewManager.removeDocumentAt(viewManager.getSelectedDocumentIndex());
		}
	}

	class MenuShowPluginListListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			PluginDialog.invokeDialog();
		}
	}
	
	class MenuShowAboutDialogListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(Nomad.this,
				    "The About Dialog is not implemented, yet.",
				    "Warning",
				    JOptionPane.INFORMATION_MESSAGE); // info message
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
		
		UIFactory theUIFactory = PluginManager.getDefaultUIFactory();
		theUIFactory.getImageTracker().addFrom(theImageTracker);

		// load module/connector icons
		ModuleDescriptions.model.loadImages(theImageTracker);

		// build toolbar
		Run.statusMessage("building toolbar");
		ModuleToolbar moduleToolbar = new ModuleToolbar();

		// create gui builder
		Run.statusMessage("ui builder");	
		ModuleGUIBuilder.createGUIBuilder(theUIFactory);

        ToolTipManager.sharedInstance().setInitialDelay(0);
        
        this.setTitle(creatorProgram + " " + creatorVersion + " " + creatorRelease);

        this.setJMenuBar(createMenu());

// Main panel
		panelMain = new JPanel();
		panelMain.setLayout(new BorderLayout());

		this.getContentPane().add(panelMain,BorderLayout.CENTER);
		this.getContentPane().add(moduleToolbar, BorderLayout.NORTH);

		this.addWindowListener(new ExitWindowListener());

		this.setSize(1024, 768);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		newSynth(ComPort.getDefaultComPortInstance());

		Run.statusMessage("Patch 'all.pch'");

		viewManager = new SelectableDocumentManager(panelMain);
		
		// now do loading
		Patch patch = new Patch();				
        JPanel tab = Patch.createPatch("src/data/patches/all.pch", patch);
        
        viewManager.addDocument("all.pch", tab);
        viewManager.setSelectedDocument(tab);
        
		//tabbedPane.getSelectedComponent().setName(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));

//        Patch patch = new Patch();
//        tabbedPane.add("new" + (tabbedPane.getTabCount()+1),patch.createPatch(""));
//        tabbedPane.setSelectedComponent(patch);
//        tabbedPane.getSelectedComponent().setName(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
		
		// subscribe for connection events;

    }
	
	private void newSynth(ComPort comPort) {
		if (synth != null) {
			if (synth.isConnected())
				try {
					synth.disconnect();
				} catch (SynthException e) {
					// should never reach this line
					e.printStackTrace();
				}
			
			synth.removeSynthConnectionStateListener(this);
		}
		
		synth=new Synth(comPort);
		// initialize
		synthConnectionStateChanged(synth);
		synth.addSynthConnectionStateListener(this);
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
			menuSelectComport = new JMenu("ComPort interface");
				menuSynth.add(menuSelectComport);
				createComPortMenuItems(menuSelectComport);
			menuSynth.addSeparator();
			menuSynthSetup = menuSynth.add("Setup");
			menuSynthConnectionMenuItem = menuSynth.add("Connect");
			menuSynth.addSeparator();
			menuSynthUploadPatchFromCurrentSlot = menuSynth.add("Upload Active Slot");
		
		menuBar.add(menuSynth);
		menuSynthSetup.addActionListener(new SetupSynthListener());
		menuSynthConnectionMenuItem.addActionListener(new SynthConnectionMenuItemListener());
		menuSynthUploadPatchFromCurrentSlot.addActionListener(new UploadPatchFromCurrentSlotListener());

		JMenu menuAppearance = new JMenu("Appearance");
			JMenu documentViewMode = new JMenu("Document View");
			menuDocumentViewTabbed = new JRadioButtonMenuItem("Tabbed");
			menuDocumentViewTabbed.setSelected(true);
			menuDocumentViewMDI = new JRadioButtonMenuItem("MDI");
			documentViewMode.add(menuDocumentViewTabbed);
			documentViewMode.add(menuDocumentViewMDI);
			menuDocumentViewTabbed.addActionListener(new SwitchDocumentViewAction());
			menuDocumentViewMDI.addActionListener(new SwitchDocumentViewAction());
			
			ButtonGroup docViewGroup = new ButtonGroup();
			docViewGroup.add(menuDocumentViewTabbed);
			docViewGroup.add(menuDocumentViewMDI);
			
			menuAppearance.add(documentViewMode);
		
		menuBar.add(menuAppearance);
			
		menuHelp = new JMenu("Help");
			menuHelpLookAndFeel = new JMenu("Look and Feel");
			menuHelp.add(menuHelpLookAndFeel);
			menuHelp.addSeparator();
			menuHelpPluginsList = menuHelp.add("Plugins");
			menuHelpAbout = menuHelp.add("About");

		// build look and feel menu
		ButtonGroup lookAndFeelGroup = new ButtonGroup();
		String current = UIManager.getLookAndFeel().getName();
		UIManager.LookAndFeelInfo[] lookAndFeelInfo = UIManager.getInstalledLookAndFeels();
		for (int i=0;i<lookAndFeelInfo.length;i++) {
			JRadioButtonMenuItem item = new JRadioButtonMenuItem(lookAndFeelInfo[i].getName());
		    item.addActionListener(new LookAndFeelChanger(lookAndFeelInfo[i]));
		    menuHelpLookAndFeel.add(item);
		    lookAndFeelGroup.add(item);
		    item.setSelected(lookAndFeelInfo[i].getName().equals(current));
		} // end build look and feel menu
			
		menuBar.add(menuHelp);
		menuHelpPluginsList.addActionListener(new MenuShowPluginListListener());
		menuHelpAbout.addActionListener(new MenuShowAboutDialogListener());
		return menuBar; 
	}

	private class LookAndFeelChanger implements ActionListener {
	    UIManager.LookAndFeelInfo info;
	    LookAndFeelChanger(UIManager.LookAndFeelInfo info) {
	      this.info = info;
	    }

	    public void actionPerformed(ActionEvent actionEvent) {
	      try {
	        UIManager.setLookAndFeel(info.getClassName());
	        SwingUtilities.updateComponentTreeUI(Nomad.this);
	      } catch (Exception e) {
	        e.printStackTrace();
	      }
	    }
	}
	
	private void createComPortMenuItems(JMenu menuSelectComport) {
		ButtonGroup group = new ButtonGroup();
		for (int i=0;i<PluginManager.getPluginCount();i++) {
			NomadPlugin plugin = PluginManager.getPlugin(i);
			if (plugin.getFactoryType()==NomadPlugin.NOMAD_FACTORY_TYPE_COMPORT) {
				JRadioButtonMenuItem mItem = new JRadioButtonMenuItem(plugin.getName());
				group.add(mItem);
				mItem.setToolTipText(plugin.getDescription());
				mItem.addActionListener(new ComPortSelectorListener(mItem, plugin));
				mItem.setSelected(NullComPort.class.getName().endsWith(plugin.getName()));
				menuSelectComport.add(mItem);
			}
		}
	}
	
	private class ComPortSelectorListener implements ActionListener {
		
		private NomadPlugin plugin = null;
		private JRadioButtonMenuItem mItem = null;
		
		public ComPortSelectorListener(JRadioButtonMenuItem mItem, NomadPlugin comPortPlugin) {
			this.plugin = comPortPlugin;
			this.mItem = mItem;
		}
		
		public void actionPerformed(ActionEvent event) {
			if (!plugin.supportsCurrentPlatform())
				JOptionPane.showMessageDialog(Nomad.this,
					    "The ComPort interface you selected does not support the current platform.",
					    "Warning",
					    JOptionPane.WARNING_MESSAGE); // error message
			else {
				newSynth(((ComPortFactory)plugin.getFactoryInstance()).getInstance());
				
				JOptionPane.showMessageDialog(Nomad.this,
					    "Changed ComPort interface to '"+plugin.getName()+"'.",
					    "Info",
					    JOptionPane.INFORMATION_MESSAGE); // info message
				
				mItem.setSelected(true);
			}
		}
	}

	/*
	public static void main(String[] args) {
		try {
//			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e) {
		}

		/* Default theme *
		System.setProperty("swing.metalTheme", "steel");

		new Nomad();
	}*/

	public void synthConnectionStateChanged(Synth synth) {
		this.menuSynthConnectionMenuItem.setText(
			synth.getCompPort().isPortOpen()?"Disconnect":"Connect"
		);
		this.menuSynthUploadPatchFromCurrentSlot.setEnabled(
			synth.getCompPort().isPortOpen()
		);
	}
}
