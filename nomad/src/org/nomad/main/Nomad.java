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
import org.nomad.util.misc.NomadUtilities;
import org.nomad.util.view.DocumentManager;
import org.nomad.util.view.SelectableDocumentManager;


public class Nomad extends JFrame implements SynthConnectionStateListener {

    public static String creatorProgram = "nomad";
    public static String creatorVersion = "v0.1";
    public static String creatorRelease = "development build";
  
    private Environment env = null;
    private Synth synth = null;
    private JFileChooser fileChooser = new JFileChooser("data/patches/");
    private JPanel panelMain = null;
    private DocumentManager documents = null;
    private PatchLoader loader = new PatchLoader(this);

	JMenuItem menuSynthConnectionMenuItem = null;
	JMenuItem menuSynthUploadPatchFromCurrentSlot = null;
	JRadioButtonMenuItem menuDocumentViewTabbed = null;
	JRadioButtonMenuItem menuDocumentViewMDI = null;
	
	public Nomad() {
		// frame setup

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(creatorProgram + " " + creatorVersion + " " + creatorRelease);
		
		// load environment 
		env = Environment.sharedInstance();
		env.loadAll();

        // components
		panelMain = new JPanel();
		panelMain.setLayout(new BorderLayout());
		this.getContentPane().add(panelMain,BorderLayout.CENTER);
		this.getContentPane().add(env.getToolbar(), BorderLayout.NORTH);
		this.setSize(1024, 768);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// further setup
		setJMenuBar(createMenu());
		ToolTipManager.sharedInstance().setInitialDelay(0);
		newSynth(ComPort.getDefaultComPortInstance());
		documents = new SelectableDocumentManager(panelMain);
		
		addWindowListener(new AutomaticSynthShutdownAction());
    }
	
	public void initialLoading() {
		loader.loadPatch("data/patches/all.pch");
	}
	
	public JMenuBar createMenu() {
		JMenuBar mnBar = new JMenuBar();
		JMenu mnMenu = null;
		
		// file
		
		mnMenu = new JMenu("File");
		mnMenu.add("New").addActionListener(new NewPatchAction());
		mnMenu.add("Open...").addActionListener(new LoadPatchAction());
		mnMenu.add("Close").addActionListener(new ClosePatchAction());
		mnMenu.add("Close all").addActionListener(new CloseAllAction());
		mnMenu.add("Save As...").addActionListener(new SavePatchAsAction());
		mnMenu.addSeparator();
		mnMenu.add("Exit").addActionListener(new ExitNomadAction());
		mnBar.add(mnMenu);
		
		// synth
		
		mnMenu = new JMenu("Synth");
		ComPortPluginMenu cppm = new ComPortPluginMenu(this, "ComPort interface");
		cppm.addPluginSelectionListener(new SelectComportAction());
		mnMenu.add(cppm);			
		mnMenu.addSeparator();
		mnMenu.add("Setup...").addActionListener(new SetupSynthAction());
		menuSynthConnectionMenuItem = mnMenu.add("Connect");
		menuSynthConnectionMenuItem.addActionListener(new SynthConnectionMenuItemListener());
		mnMenu.addSeparator();
		menuSynthUploadPatchFromCurrentSlot = mnMenu.add("Upload Active Slot");
		menuSynthUploadPatchFromCurrentSlot.addActionListener(new UploadPatchFromCurrentSlotAction());
		mnBar.add(mnMenu);
		
		// appearance

		mnMenu = new JMenu("Appearance");
		
		// appearance -> document view
		
		JMenu mnSubMenu = new JMenu("Document View");
		menuDocumentViewTabbed = new JRadioButtonMenuItem("Tabbed");
		menuDocumentViewTabbed.setSelected(true);
		menuDocumentViewTabbed.addActionListener(new SwitchDocumentViewAction());
		mnSubMenu.add(menuDocumentViewTabbed);
		
		menuDocumentViewMDI = new JRadioButtonMenuItem("MDI");
		menuDocumentViewMDI.addActionListener(new SwitchDocumentViewAction());
		mnSubMenu.add(menuDocumentViewMDI);
		
		ButtonGroup group = new ButtonGroup();
		group.add(menuDocumentViewTabbed);
		group.add(menuDocumentViewMDI);
		
		mnMenu.add(mnSubMenu);
		
		// appearance -> theme
		
		ThemePluginMenu tpm = new ThemePluginMenu("Theme");
		tpm.addPluginSelectionListener(new SelectThemeAction());
			
		mnMenu.add(tpm);
		
		mnBar.add(mnMenu);

		// help
		
		mnMenu = new JMenu("Help");
		
		// help -> look and feel
		
		LookAndFeelMenu lafm = new LookAndFeelMenu("Look and Feel");
		// add components that should be updated if laf changes
		lafm.addAffectedComponent(this);
		lafm.addAffectedComponent(fileChooser);
		mnMenu.add(lafm);
		
		mnMenu.addSeparator();
		mnMenu.add("Plugins...").addActionListener(new ShowPluginDialogAction());

		mnMenu.addSeparator();
		mnMenu.add("Debug: gc()").addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				System.gc();
				env.getBuilder().info();
		}});
		
		mnMenu.add("About").addActionListener(new ShowAboutDialogAction());
		mnBar.add(mnMenu);
		
		return mnBar; 
	}

	public DocumentManager getDocumentManager() {
		return documents;
	}
	
	public void newPatch() {
    	Patch patch = new Patch();
        JPanel tab = Patch.createPatch("", patch);
        documents.addDocument("new" + (documents.getDocumentCount()+1), tab);
        documents.setSelectedDocument(tab);
        documents.getSelectedDocument().setName(documents.getTitleAt(documents.getSelectedDocumentIndex()));
	}
	
	public void exitNomad() {
    	Nomad.this.processEvent(new WindowEvent(Nomad.this, WindowEvent.WINDOW_CLOSING));
	}
    
    public void openPatchFile() {
		fileChooser.setMultiSelectionEnabled(true);
		if (fileChooser.showOpenDialog(Nomad.this) == JFileChooser.APPROVE_OPTION) {
			File[] files = fileChooser.getSelectedFiles();
			loader.loadPatch(files);
		}
		fileChooser.setMultiSelectionEnabled(false);
    }

	public void savePatchAs() {
		fileChooser.setSelectedFile(new File(documents.getSelectedDocument().getName() + "_new.pch"));
		if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			OutputStream stream;
			try {
				stream = new FileOutputStream(fileChooser.getSelectedFile());
				PatchGUI patchGUI = (PatchGUI) documents.getSelectedDocument();
				
				stream.write(patchGUI.getPatch().savePatch().toString().getBytes());
				stream.flush();
				stream.close();
			}
			catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	public void closePatch() {
		if (documents.getSelectedDocument()!=null) {
			documents.removeDocumentAt(documents.getSelectedDocumentIndex());
		}
	}
	
	public void closeEachPatch() {
		while (documents.getDocumentCount()>0) {
			documents.removeDocumentAt(0);
		}
	}

	void noDialogYet() {
		NomadUtilities.infoDialog(this, "The dialog is not implemented, yet.");
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
	
	void changeSynthInterface(final NomadPlugin plugin) {
		
		JTaskDialog.processTasks(Nomad.this, new TaskModel(){

			final int selectionIndex = documents.getSelectedDocumentIndex();
			final int documentCount = documents.getDocumentCount();
			
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
						else return "Patch:" +documents.getTitleAt(0);
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
				env.getToolbar().setEnabled(false);
			}
			
			public void init2() {
				env.setFactory((UIFactory) plugin.getFactoryInstance());
			}
			
			public void finish() {
				if (selectionIndex>=0)
					documents.setSelectedDocument(selectionIndex);

				setCursor(Cursor.getDefaultCursor());
				env.getToolbar().setEnabled(true);
				documents.getDocumentContainer().validate();
				documents.getDocumentContainer().repaint();
			}
			
			public void migrate(int document) {
				((PatchGUI) documents.getDocumentAt(document)).rebuildUI();
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

	class AutomaticSynthShutdownAction extends WindowAdapter {
		public void windowClosing(WindowEvent event) {
			if (synth.isConnected())
				try {
					System.out.println("Closing midi connection.");
					synth.disconnect();
				} catch (SynthException e) {
					System.out.println("Synth exception while exit:"+e);
					e.printStackTrace();
				}
		}
	}
	
	class CloseAllAction implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			closeEachPatch();
		}
	}
	
	class SelectComportAction implements ChangeListener {
		public void stateChanged(ChangeEvent event) {
			if (event.getSource() instanceof ComPortPluginMenu) {
				NomadPlugin plugin = ((ComPortPluginMenu)event.getSource()).getSelectedPlugin();
				
				newSynth(((ComPortFactory)plugin.getFactoryInstance()).getInstance());
				
				JOptionPane.showMessageDialog(Nomad.this,
					    "Changed ComPort interface to '"+plugin.getName()+"'.",
					    "Info",
					    JOptionPane.INFORMATION_MESSAGE); // info message
				
				
			}
		}
	}
	
	class SelectThemeAction implements ChangeListener {
		public void stateChanged(ChangeEvent event) {
			if (event.getSource() instanceof ThemePluginMenu) {
			NomadPlugin plugin = ((ThemePluginMenu)event.getSource()).getSelectedPlugin();
			if (plugin!=null)
				changeSynthInterface(plugin);
			
			}
		}
	}

    class NewPatchAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	newPatch();
        }
    }

    class ExitNomadAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	exitNomad();
        }
    }

    class UploadPatchFromCurrentSlotAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	Patch p = synth.getCompPort().getPatchFromActiveSlot();
        	
        	if (p==null) {
        		System.err.println("no patch data");
        		return;
        	}
        	
            JPanel tab = Patch.createPatchUI(p);
            documents.addDocument("<uploaded>",tab);
            documents.setSelectedDocument(tab);
            documents.getSelectedDocument().setName(documents.getTitleAt(documents.getSelectedDocumentIndex()));
        	
        }
    }
    
    class SwitchDocumentViewAction implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (event.getSource()==menuDocumentViewMDI)
				((SelectableDocumentManager)documents).switchDocumentManager(false);
			else if (event.getSource()==menuDocumentViewTabbed)
				((SelectableDocumentManager)documents).switchDocumentManager(true);
		}
    }

	class LoadPatchAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			openPatchFile();
		}
	}
	
	class SavePatchAsAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			savePatchAs();
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
	
	class SetupSynthAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			ComPortSettingsDialog.invokeDialog(synth.getCompPort());
		}
	}
	
	class ClosePatchAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			closePatch();
		}
	}
	class ShowPluginDialogAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			noDialogYet();
		}
	}
	
	class ShowAboutDialogAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			noDialogYet();
		}
	}
	
}
