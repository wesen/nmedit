// http://nmedit.sourceforge.net
    
package org.nomad.main;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
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
import javax.swing.ToolTipManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.nomad.dialog.ComPortSettingsDialog;
import org.nomad.dialog.JTaskDialog;
import org.nomad.dialog.TaskModel;
import org.nomad.env.Environment;
import org.nomad.patch.Patch;
import org.nomad.plugin.NomadPlugin;
import org.nomad.port.ComPort;
import org.nomad.port.ComPortFactory;
import org.nomad.port.Synth;
import org.nomad.port.SynthConnectionStateListener;
import org.nomad.port.SynthException;
import org.nomad.theme.PatchGUI;
import org.nomad.theme.UIFactory;
import org.nomad.util.view.DocumentManager;
import org.nomad.util.view.SelectableDocumentManager;


public class Nomad extends JFrame implements SynthConnectionStateListener {
    public static String creatorProgram = "nomad";
    public static String creatorVersion = "v0.1";
    public static String creatorRelease = "development build";
  
    Environment env = null;
    
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
		LookAndFeelMenu menuHelpLookAndFeel = null;
		JMenuItem menuHelpPluginsList = null;
		JMenuItem menuHelpAbout = null;

	JButton button = null;

	JPanel panelMain = null;

	DocumentManager viewManager = null;

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
			noDialogYet();
		}
	}
	
	class MenuShowAboutDialogListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			noDialogYet();
		}
	}

	void noDialogYet() {
		JOptionPane.showMessageDialog(Nomad.this,
			    "The About Dialog is not implemented, yet.",
			    "Warning",
			    JOptionPane.INFORMATION_MESSAGE); // info message
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
	
	PatchLoader loader = new PatchLoader(this);
	
	public Nomad() {
		env = Environment.sharedInstance();
		env.loadAll();

		ToolTipManager.sharedInstance().setInitialDelay(0);
        
        this.setTitle(creatorProgram + " " + creatorVersion + " " + creatorRelease);

        this.setJMenuBar(createMenu());

// Main panel
		panelMain = new JPanel();
		panelMain.setLayout(new BorderLayout());

		this.getContentPane().add(panelMain,BorderLayout.CENTER);
		this.getContentPane().add(env.getToolbar(), BorderLayout.NORTH);

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
			menuSelectComport = new ComPortPluginMenu(this, "ComPort interface");
			menuSynth.add(menuSelectComport);
			
			((ComPortPluginMenu)menuSelectComport).addPluginSelectionListener(new ChangeListener(){
				public void stateChanged(ChangeEvent event) {
					NomadPlugin plugin = ((ComPortPluginMenu)menuSelectComport).getSelectedPlugin();
					
					newSynth(((ComPortFactory)plugin.getFactoryInstance()).getInstance());
					
					JOptionPane.showMessageDialog(Nomad.this,
						    "Changed ComPort interface to '"+plugin.getName()+"'.",
						    "Info",
						    JOptionPane.INFORMATION_MESSAGE); // info message
					
					
				}});
				
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
		
			menuPatchTheme = new ThemePluginMenu("Theme");
			((ThemePluginMenu)menuPatchTheme).addPluginSelectionListener(new ChangeListener(){

				public void stateChanged(ChangeEvent event) {
					NomadPlugin plugin = ((ThemePluginMenu)menuPatchTheme).getSelectedPlugin();
					if (plugin!=null)
						changeSynthInterface(plugin);
				}});
			
			menuAppearance.add(menuDocumentViewMode);
			menuAppearance.add(menuPatchTheme);
		
		menuBar.add(menuAppearance);
			
		menuHelp = new JMenu("Help");
			menuHelpLookAndFeel = new LookAndFeelMenu("Look and Feel");
			// add components that should be updated
			menuHelpLookAndFeel.addAffectedComponent(this);
			menuHelpLookAndFeel.addAffectedComponent(fileChooser);
			menuHelp.add(menuHelpLookAndFeel);
			
			menuHelp.addSeparator();
			menuHelpPluginsList = menuHelp.add("Plugins");
			menuHelp.addSeparator();
			menuHelp.add("Call gc()").addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent event) {
					System.gc();
					env.getBuilder().info();
				}});
			menuHelpAbout = menuHelp.add("About");


		menuBar.add(menuHelp);
		menuHelpPluginsList.addActionListener(new MenuShowPluginListListener());
		menuHelpAbout.addActionListener(new MenuShowAboutDialogListener());
		return menuBar; 
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
						else migrate(taskIndex-2);
					}
				}
			}

			public void init() {
				setCursor(new Cursor(Cursor.WAIT_CURSOR));
				menuBar.setEnabled(false);
				env.getToolbar().setEnabled(false);
			}
			
			public void init2() {
				env.setFactory((UIFactory) plugin.getFactoryInstance());
			}
			
			public void finish() {
				if (selectionIndex>=0)
					viewManager.setSelectedDocument(selectionIndex);

				setCursor(Cursor.getDefaultCursor());
				menuBar.setEnabled(true);
				env.getToolbar().setEnabled(true);
				viewManager.getDocumentContainer().validate();
				viewManager.getDocumentContainer().repaint();
			}
			
			public void migrate(int document) {
				((PatchGUI) viewManager.getDocumentAt(document)).rebuildUI();
			}
			
		});

	}

	public void synthConnectionStateChanged(Synth synth) {
		this.menuSynthConnectionMenuItem.setText(
			synth.getCompPort().isPortOpen()?"Disconnect":"Connect"
		);
		this.menuSynthUploadPatchFromCurrentSlot.setEnabled(
			synth.getCompPort().isPortOpen()
		);
	}
}
