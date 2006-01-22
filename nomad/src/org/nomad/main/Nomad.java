// http://nmedit.sourceforge.net
    
package org.nomad.main;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
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

import org.nomad.dialog.JTaskDialog;
import org.nomad.dialog.TaskModel;
import org.nomad.main.run.Run;
import org.nomad.patch.Patch;
import org.nomad.plugin.NomadPlugin;
import org.nomad.plugin.PluginManager;
import org.nomad.port.ComPort;
import org.nomad.port.ComPortFactory;
import org.nomad.port.NullComPort;
import org.nomad.port.Synth;
import org.nomad.port.SynthConnectionStateListener;
import org.nomad.port.SynthException;
import org.nomad.theme.ModuleGUIBuilder;
import org.nomad.theme.PatchGUI;
import org.nomad.theme.UIFactory;
import org.nomad.util.graphics.ImageTracker;
import org.nomad.xml.dom.module.DConnector;
import org.nomad.xml.dom.module.ModuleDescriptions;
import org.nomad.xml.dom.substitution.Substitutions;


public class Nomad extends JFrame implements SynthConnectionStateListener {
    public static String creatorProgram = "nomad";
    public static String creatorVersion = "v0.1";
    public static String creatorRelease = "development build";
  
    Synth synth = null;

	JFileChooser fileChooser = new JFileChooser("data/patches/");
	
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
		JMenu menuDocumentViewMode = null;
			JRadioButtonMenuItem menuDocumentViewTabbed = null;
			JRadioButtonMenuItem menuDocumentViewMDI = null;
		JMenu menuPatchTheme = null;
		
	JMenu menuHelp = null;
		JMenu menuHelpLookAndFeel = null;
		JMenuItem menuHelpPluginsList = null;
		JMenuItem menuHelpAbout = null;

//	JPanel toolPanel = null;
	ModuleToolbar moduleToolbar = null;

	JButton button = null;

	JPanel panelMain = null;
	ImageTracker theImageTracker = null;
	
	private DocumentManager viewManager = null;
	private static UIFactory uifactory;

	public static UIFactory getUIFactory() {
		return uifactory;
	}
	
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
			fileChooser.setMultiSelectionEnabled(true);
			if (fileChooser.showOpenDialog(Nomad.this) == JFileChooser.APPROVE_OPTION) {
				File[] files = fileChooser.getSelectedFiles();
				loader.loadPatch(files);
			}
			fileChooser.setMultiSelectionEnabled(false);
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
			if (viewManager.getSelectedDocument()!=null) {
				viewManager.removeDocumentAt(viewManager.getSelectedDocumentIndex());
			}
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
	
	class PatchLoader implements Runnable {

		private ArrayList panelList = new ArrayList();
		private ArrayList nameList = new ArrayList();
		
		public PatchLoader() { }
		public void loadPatch(String file) {
			loadPatch(new String[]{file});
		}

		public void loadPatch(File[] files) {
			String[] sfiles = new String[files.length];
			for (int i=files.length-1;i>=0;i--)
				sfiles[i]=files[i].getAbsolutePath();
			
			loadPatch(sfiles);
		}

		public void loadPatch(final String[] files) {
			JTaskDialog.processTasks(Nomad.this, new TaskModel(){
				public String getDescription() {
					return "Loading Nord Modular patch...";
				}

				public int getTaskCount() {
					return files.length;
				}

				public String getTaskName(int taskIndex) {
					String name = files[taskIndex];
					return name.substring(name.lastIndexOf('/')+1);
				}

				public void run(int taskIndex) {
					String file = files[taskIndex];
					
					Patch patch = new Patch();				
			        JPanel tab = Patch.createPatch(file, patch);
					String name = file.substring(0,file.lastIndexOf(".pch"));
					name = name.substring(name.lastIndexOf('/')+1);
					panelList.add(tab);
					nameList.add(name);
					
			        if (taskIndex==getTaskCount()-1)
			        	SwingUtilities.invokeLater(PatchLoader.this);
				}});
		}
		
		public void run() {

			JComponent tab = null;

			while (panelList.size()>0) {
				viewManager.addDocument((String)nameList.remove(0), tab = (JComponent) panelList.remove(0));
			}

			if (tab!=null)
				viewManager.setSelectedDocument(tab);
	        
		}
		
	}
	
	PatchLoader loader = new PatchLoader();
	
	public Nomad() {

		// feed image tracker
		Run.statusMessage("images");
		theImageTracker = new ImageTracker();
		try {
			theImageTracker.loadFromDirectory("data/images/slice/");
			theImageTracker.loadFromDirectory("data/images/single/");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// load plugin names
		Run.statusMessage("Plugin Manager");
		PluginManager.init();

		// load substitutions
		Run.statusMessage("parameter substitutions");
		Substitutions subs = new Substitutions("data/xml/substitutions.xml");
		
		// load module descriptors
		Run.statusMessage("module description");
		ModuleDescriptions.init("data/xml/modules.xml", subs);

		uifactory = PluginManager.getDefaultUIFactory();
		uifactory.setEditing(false);
		uifactory.getImageTracker().addFrom(theImageTracker);

		// load module/connector icons
		ModuleDescriptions.model.loadImages(theImageTracker);

		// build toolbar
		Run.statusMessage("building toolbar");
		moduleToolbar = new ModuleToolbar();

		// create gui builder
		Run.statusMessage("ui builder");	
		ModuleGUIBuilder.createGUIBuilder(uifactory);

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

		viewManager = new SelectableDocumentManager(panelMain);
    }
	
	public void initialLoading() {
		loader.loadPatch("data/patches/all.pch");
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

		menuAppearance = new JMenu("Appearance");
			menuDocumentViewMode = new JMenu("Document View");
			menuDocumentViewTabbed = new JRadioButtonMenuItem("Tabbed");
			menuDocumentViewTabbed.setSelected(true);
			menuDocumentViewMDI = new JRadioButtonMenuItem("MDI");
			menuDocumentViewMode.add(menuDocumentViewTabbed);
			menuDocumentViewMode.add(menuDocumentViewMDI);
			menuDocumentViewTabbed.addActionListener(new SwitchDocumentViewAction());
			menuDocumentViewMDI.addActionListener(new SwitchDocumentViewAction());
						
			ButtonGroup docViewGroup = new ButtonGroup();
			docViewGroup.add(menuDocumentViewTabbed);
			docViewGroup.add(menuDocumentViewMDI);
		
			menuPatchTheme = new JMenu("Theme");
			ButtonGroup themeGroup = new ButtonGroup();
			for (int i=0;i<PluginManager.getPluginCount();i++) {
				NomadPlugin plugin = PluginManager.getPlugin(i);
				if (plugin.getFactoryType()==NomadPlugin.NOMAD_FACTORY_TYPE_UI) {
					JRadioButtonMenuItem menuItem = new ThemeChanger(plugin);
					menuPatchTheme.add(menuItem);
					themeGroup.add(menuItem);
					if (plugin.getName().equals("Classic Theme"))
						menuItem.setSelected(true);
					
					// TODO remove - only for testing
					/*else
						menuItem.doClick();*/
					// -- end --
				}
			}	

			menuAppearance.add(menuDocumentViewMode);
			menuAppearance.add(menuPatchTheme);
		
		menuBar.add(menuAppearance);
			
		menuHelp = new JMenu("Help");
			menuHelpLookAndFeel = new JMenu("Look and Feel");
			menuHelp.add(menuHelpLookAndFeel);
			menuHelp.addSeparator();
			menuHelpPluginsList = menuHelp.add("Plugins");
			menuHelp.addSeparator();
			menuHelp.add("Call gc()").addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent event) {
					System.gc();
					ModuleGUIBuilder.info();
				}});
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

	private class ThemeChanger extends JRadioButtonMenuItem
		implements ActionListener {
		private NomadPlugin plugin = null;
		public ThemeChanger(NomadPlugin plugin) {
			super(plugin.getName());
			setToolTipText(plugin.getDescription());
			this.plugin = plugin;

			addActionListener(this);
		}
		
		public void actionPerformed(ActionEvent event) {
			changeSynthInterface(plugin);
		}
	}

	void changeSynthInterface(final NomadPlugin plugin) {
		
		JTaskDialog.processTasks(Nomad.this, new TaskModel(){

			final int selectionIndex = viewManager.getSelectedDocumentIndex();
			final int documentCount = viewManager.getDocumentCount();
			
			public String getDescription() {
				return "Switching theme plugin";
			}

			public int getTaskCount() {
				return documentCount+3;
			}

			public String getTaskName(int taskIndex) {
				switch (taskIndex) {
					case 0: return "Preparing...";
					case 1: return "Loading factory...";
					default: {
						if (taskIndex-2>=documentCount) return "Finishing...";
						else return "Patch:" +viewManager.getTitleAt(0);
					}
				}
			}

			public void run(int taskIndex) {
				switch (taskIndex) {
					case 0: init(); break;
					case 1: init2(); break;
					default: {
						if (taskIndex==documentCount+2) finish();
						else migrate();
					}
				}
			}

			public void init() {
				Nomad.this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				Nomad.this.menuBar.setEnabled(false);
				Nomad.this.moduleToolbar.setEnabled(false);
			}
			
			public void init2() {
				uifactory = (UIFactory) plugin.getFactoryInstance();
				uifactory.setEditing(false);
				uifactory.getImageTracker().addFrom(theImageTracker);
				ModuleGUIBuilder.instance.setUIFactory(uifactory);
				DConnector.setImageTracker(uifactory.getImageTracker());
			}
			
			public void finish() {
				if (selectionIndex>=0)
					viewManager.setSelectedDocument(selectionIndex);

				Nomad.this.setCursor(Cursor.getDefaultCursor());
				Nomad.this.menuBar.setEnabled(true);
				Nomad.this.moduleToolbar.setEnabled(true);
				viewManager.getDocumentContainer().validate();
				viewManager.getDocumentContainer().repaint();
			}
			
			public void migrate() {
				String name = viewManager.getTitleAt(0);
				PatchGUI oldPatchGUI = (PatchGUI) viewManager.getDocumentAt(0);
				viewManager.removeDocument(oldPatchGUI);

				Patch patch = new Patch();
				Reader r = new StringReader(oldPatchGUI.getPatch().savePatch().toString());
				PatchGUI newPatchGUI = (PatchGUI) Patch.createPatch(r, patch);

				if (newPatchGUI!=null)
					viewManager.addDocument(name, newPatchGUI);
			}
			
		});

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
