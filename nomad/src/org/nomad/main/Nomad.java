// http://nmedit.sourceforge.net
    
package org.nomad.main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.ToolTipManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.nomad.dialog.NomadAboutDialog;
import org.nomad.dialog.PatchSettingsDialog;
import org.nomad.env.Environment;
import org.nomad.main.run.SplashWindow;
import org.nomad.patch.Patch;
import org.nomad.patch.format.PatchFilePreviewComponent;
import org.nomad.patch.format.PatchFileWriter;
import org.nomad.patch.ui.PatchUI;
import org.nomad.plugin.NomadPlugin;
import org.nomad.synth.Slot;
import org.nomad.synth.SlotListener;
import org.nomad.synth.SynthDevice;
import org.nomad.synth.SynthDeviceStateListener;
import org.nomad.util.misc.NomadUtilities;
import org.nomad.util.view.DocumentManager;
import org.nomad.util.view.SelectableDocumentManager;

public class Nomad extends JFrame implements SynthDeviceStateListener, SlotListener {

    private Environment env = null;
    private SynthDevice synthConnection = null;
    private JFileChooser fileChooser = new JFileChooser("data/patches/");
    private JPanel panelMain = null;
    private DocumentManager documents = null;
    private PatchLoader loader = new PatchLoader(this);

    private JMenuItem menuSynthConnectionMenuItem = null;
    private JMenuItem menuSynthUploadPatchFromCurrentSlot = null;
    private JRadioButtonMenuItem menuDocumentViewTabbed = null;
    private JRadioButtonMenuItem menuDocumentViewMDI = null;
    private LookAndFeelMenu lafm = null;
    private ThemePluginMenu tpm = null;
    private PatchUI[] slotDocs = new PatchUI[4];
    
	public Nomad() {

		// load environment 
		env = Environment.sharedInstance();
		env.loadAll();
		
		// frame setup
        setTitle("Nomad v"+env.getProperty("nomad.version"));

        // components
		panelMain = new JPanel();
		panelMain.setLayout(new BorderLayout());
		
		this.getContentPane().add(panelMain,BorderLayout.CENTER);
		this.getContentPane().add(env.getToolbar(), BorderLayout.NORTH);
		this.setSize(1024, 768);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// further setup
		setJMenuBar(createMenu());
		ToolTipManager.sharedInstance().setInitialDelay(100);
		//newSynth(ComPort.getDefaultComPortInstance());
		synthConnection = new SynthDevice();
		synthConnection.addStateListener(this);
		documents = new SelectableDocumentManager(panelMain);
		synthConnection.getSynth().addSlotListener(this);
		
		addWindowListener(new AutomaticSynthShutdownAction());
		
		// file chooser
		fileChooser.setAccessory(new PatchFilePreviewComponent(fileChooser));

		lafm.loadSelectedLaf();
		tpm.reselectPlugin();
		
		Arrays.fill(slotDocs, null);
		
		validate();
    }
	
	public void initialLoading() {
		
		SplashWindow.disposeSplash();
		loader.loadPatch("data/patches/simple.pch");
		
	}
	
	private void menuItem(JMenu menu, String text, ActionListener action, KeyStroke ks) {
		JMenuItem item = menu.add(text);
		item.addActionListener(action);
		item.setAccelerator(ks);
	}
	
	public JMenuBar createMenu() {
		JMenuBar mnBar = new JMenuBar();
		JMenu mnMenu = null;

		// file
		
		mnMenu = new JMenu("File");
		menuItem(mnMenu, "New", new NewPatchAction(), KeyStroke.getKeyStroke('N', InputEvent.CTRL_MASK));
		menuItem(mnMenu, "Open...", new LoadPatchAction(), KeyStroke.getKeyStroke('O', InputEvent.CTRL_MASK));
				
		mnMenu.add("Close").addActionListener(new ClosePatchAction());
		mnMenu.add("Close all").addActionListener(new CloseAllAction());
		menuItem(mnMenu, "Save As...", new SavePatchAsAction(), KeyStroke.getKeyStroke('S', InputEvent.CTRL_MASK));
		
		mnMenu.addSeparator();
		mnMenu.add("Exit").addActionListener(new ExitNomadAction());
		mnBar.add(mnMenu);
		
		// synth
		
		mnMenu = new JMenu("Synth");
		mnMenu.add("Setup...").addActionListener(new SetupSynthAction());
		menuSynthConnectionMenuItem = mnMenu.add("Connect");
		menuSynthConnectionMenuItem.addActionListener(new SynthConnectionMenuItemListener());
		mnMenu.addSeparator();
		menuSynthUploadPatchFromCurrentSlot = mnMenu.add("Upload Active Slot");
		menuSynthUploadPatchFromCurrentSlot.addActionListener(new UploadPatchFromCurrentSlotAction());
		mnBar.add(mnMenu);
		
		// patch
		mnMenu = new JMenu("Patch");
		mnMenu.add("Settings").addActionListener(new PatchSettingsAction());
		mnBar.add(mnMenu);
		
		// appearance

		mnMenu = new JMenu("Appearance");
		
		// appearance -> theme
		
		tpm = new ThemePluginMenu("Theme");
		tpm.addPluginSelectionListener(new SelectThemeAction());
			
		mnMenu.add(tpm);
		
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
		
		// appearance -> look and feel
		
		lafm = new LookAndFeelMenu("Look and Feel");
		// add components that should be updated if laf changes
		lafm.addAffectedComponent(this);
		lafm.addAffectedComponent(fileChooser);
		mnMenu.add(lafm);
		
		mnBar.add(mnMenu);

		// help
		
		mnMenu = new JMenu("Help");
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
    	PatchUI tab = PatchUI.newInstance(new Patch());
        documents.addDocument("new" + (documents.getDocumentCount()+1), tab);
        documents.setSelectedDocument(tab);
        documents.getSelectedDocument().setName(documents.getTitleAt(documents.getSelectedDocumentIndex()));
	}
	
	public void exitNomad() {
		
		Environment.sharedInstance().saveProperties();
		
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
		PatchUI patchui = getActivePatch();
		if (patchui==null) {
			System.err.println("No patch selected.");
			return ;
		}

		fileChooser.setSelectedFile(new File(documents.getSelectedDocument().getName() + "_new.pch"));
		
		if (NomadUtilities.isConfirmedByUser(this, "Saving does not produce valid files yet. Shall I go on ?"))
		
		if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			try {
				
				PrintWriter out = new PrintWriter(new FileWriter(fileChooser.getSelectedFile()));
				PatchFileWriter writer = new PatchFileWriter(out, patchui.getPatch() );
				writer.write();
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
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
	
	void changeSynthInterface(NomadPlugin plugin) {
		ThemePluginSwitcher switcher = new ThemePluginSwitcher(this, plugin);
		switcher.switchPlugin();
	}

	public void synthConnectionStateChanged(SynthDevice synth) {
		this.menuSynthConnectionMenuItem.setText(
			synth.isConnected()?"Disconnect":"Connect"
		);
		this.menuSynthUploadPatchFromCurrentSlot.setEnabled(
			synth.isConnected()
		);
	}

	class AutomaticSynthShutdownAction extends WindowAdapter {
		public void windowClosing(WindowEvent event) {
			if (synthConnection.isConnected()) {
				System.out.println("Closing midi connection.");
				synthConnection.disconnect();
			}
		}
	}
	
	class CloseAllAction implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			closeEachPatch();
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

    PatchUI getActivePatch() {
    	return (PatchUI) getDocumentManager().getSelectedDocument();
    }
    
    class PatchSettingsAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	PatchUI patch = getActivePatch();
        	if (patch!=null)
        	{
        		PatchSettingsDialog dialog = new PatchSettingsDialog(patch.getPatch());
        		dialog.invoke();
        	}
        }
    }

    class ExitNomadAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	exitNomad();
        }
    }

    class UploadPatchFromCurrentSlotAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	Patch p = synthConnection.getSynth().getActiveSlot();
        	
        	if (p==null) {
        		System.err.println("no patch data");
        		return;
        	}
        	
        	PatchUI tab = PatchUI.newInstance(p);
            documents.addDocument(tab.getPatch().getName(),tab);
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
			if (!synthConnection.isConnected())
				synthConnection.connect();
			else
				synthConnection.disconnect();
		}
	}
	
	class SetupSynthAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			synthConnection.setup();
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
			NomadAboutDialog dlg = new NomadAboutDialog();
			dlg.invoke();
		}
	}

	public void newPatchInSlot(Slot slot) {
		int index = slot.getId();
		if (slotDocs[index]!=null) {
			documents.removeDocument(slotDocs[index]);
			slotDocs[index] = null;
		}
		Patch p = slot.getPatch();
		if (p!=null) {
	    	PatchUI tab = PatchUI.newInstance(p);
	        documents.addDocument(tab.getPatch().getName()+" (slot:"+index+")",tab);
	        documents.setSelectedDocument(tab);
	        documents.getSelectedDocument().setName(documents.getTitleAt(documents.getSelectedDocumentIndex()));
	        slotDocs[index] = tab;
		}
	}

	public void slotSelected(Slot slot) {
		PatchUI doc =slotDocs[slot.getId()]; 
		if (doc!=null) documents.setSelectedDocument(doc);
	}
	
}
