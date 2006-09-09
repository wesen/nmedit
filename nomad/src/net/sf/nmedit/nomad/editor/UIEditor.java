package net.sf.nmedit.nomad.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.sf.nmedit.jmisc.xml.XMLFileWriter;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DModule;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.ModuleDescriptions;
import net.sf.nmedit.jtheme.ComponentEditorView;
import net.sf.nmedit.nomad.core.nomad.NomadEnvironment;
import net.sf.nmedit.nomad.editor.views.ModuleListView;
import net.sf.nmedit.nomad.editor.views.WorkspacePanel;
import net.sf.nmedit.nomad.editor.views.classes.NomadClassesView;
import net.sf.nmedit.nomad.main.action.ThemePluginSelector;
import net.sf.nmedit.nomad.main.dialog.NomadTaskDialog;
import net.sf.nmedit.nomad.main.dialog.TaskModel;
import net.sf.nmedit.nomad.theme.plugin.ThemePluginProvider;
import net.sf.nmedit.nomad.util.NomadUtilities;


public class UIEditor extends JFrame implements ListSelectionListener {
	
    // properties
	
	private NomadEnvironment env = null;
	private WorkspacePanel workspace = null;
	private NomadClassesView classesView = null;
    private DModule modified = null;
    private ThemePluginSelector tps;
    private ModuleListView listView;
    
    private ComponentEditorView ceView = null;

	// constructor
	
	public UIEditor(String name) {
		
		// title
		
		super(name);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
		// environment
		
		env = NomadEnvironment.sharedInstance();
		env.setCachingEnabled(false);

		// components
		
		classesView = new NomadClassesView();
		classesView.setFactory(env.getTheme());

		workspace = new WorkspacePanel();
		workspace.setSize(new Dimension(250,400));

		JSplitPane split = new JSplitPane( JSplitPane.VERTICAL_SPLIT, new JPanel() /*propertyEditor*/, classesView );
		split.setDividerLocation(0.5);

		getContentPane().setLayout(new BorderLayout());
        // TODO add module tree
        listView = new ModuleListView();
        listView.addListSelectionListener(this);
		getContentPane().add(listView, BorderLayout.WEST);
		getContentPane().add(split, 			BorderLayout.EAST);
		getContentPane().add(workspace, 		BorderLayout.CENTER);
		
		// menu

		JMenuBar mnBar = new JMenuBar();
		JMenu mnMenu = null;

		// file
		mnMenu = new JMenu("File");
		mnMenu.add("Open..."	).addActionListener(new FileOpenAction());
		mnMenu.add("Save as..."	).addActionListener(new SaveFileAsAction());
		mnMenu.addSeparator();
		mnMenu.add("Exit"		).addActionListener(new AppExitAction());
		mnBar.add(mnMenu);

		// theme
		mnMenu = new JMenu("Theme");
        tps = new ThemePluginSelector();
        tps.createMenu(mnMenu);
		tps.addPluginSelectionListener(new ThemeChangeAction());		
		mnBar.add(mnMenu);
		
		// dom
		mnMenu = new JMenu("DOM");
		mnMenu.add("rebuild").addActionListener(new RebuildDomAction());
		mnBar.add(mnMenu);

		this.setJMenuBar(mnBar);

		pack();
	}
    JFileChooser fileChooser = null;
	
	public void saveFileAs() {
        if (fileChooser == null)
            fileChooser = new JFileChooser();
		if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			String uiFile = fileChooser.getSelectedFile().getAbsolutePath();
			if (NomadUtilities.isConfirmedByUser(this, "The file '"+uiFile+"' will be overwritten.\nShall I go on ?" )) {
				XMLFileWriter xml;
				try {
					xml = new XMLFileWriter(uiFile,
						"<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
						"<!DOCTYPE theme SYSTEM \"theme-v10.dtd\">"
					);
					env.getTheme().getDom().exportDocument(xml);			
					xml.flush();
					xml.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}		
			}
		}
	}
	
	public void loadFile() {/*
		String uiFile = env.getFactory().getUIDescriptionFileName();		
		JFileChooser fileChooser = new JFileChooser(uiFile);
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			uiFile = fileChooser.getSelectedFile().getAbsolutePath();
			if (NomadUtilities.isConfirmedByUser(this, "Changes will be lost.\nShall I go on ?" )) {
				env.getBuilder().load(uiFile);
			}
		}*/
	}
	
	public void quit() {
    	UIEditor.this.processEvent(new WindowEvent(UIEditor.this, WindowEvent.WINDOW_CLOSING));
	}
    
    public DModule getModule() {
    	return modified;
    }
    
    public void setModule(DModule module) 
    {
    	this.modified = module;
        
        if (ceView != null)
        {
            ceView.uninstallHook();
            workspace.remove(ceView);
        }
        
        if (module!=null)
        {
            JComponent c = env.getTheme().buildModule(module);
            ceView = new ComponentEditorView(env.getTheme().getThemeConfiguration(), c);
            workspace.add(ceView);
            ceView.setPreferredSize(c.getSize());
            ceView.setSize(c.getSize());
        }
        
        /*
		if (visualEditor!=null) {
			// write back
			env.getBuilder().rewriteDOM(visualEditor, visualEditor.getModuleInfo());
			visualEditor.setComponentPropertyEditor(null);
			workspace.remove(visualEditor);
		}

		if (module!=null) {
			visualEditor = new VisualEditor(module);
			visualEditor.setComponentPropertyEditor(propertyEditor);
			env.getBuilder().createGUIComponentsNoCaching(visualEditor, module);
			visualEditor.setSize(ModuleUI.Metrics.WIDTH, ModuleUI.Metrics.getHeight(module));
			visualEditor.setVisible(true);
			workspace.add(visualEditor);
		}
        */
    }
    
	void changeTheme(final ThemePluginProvider plugin) {
		DModule current = getModule();
		setModule(null);
		env.setFactory(plugin.getFactory());
		classesView.setFactory(env.getTheme());
		//propertyEditor.setEditingPropertySet(null,null);
		setModule(current);
	}

	public void rebuildDOM() {
		if (JOptionPane.showConfirmDialog(UIEditor.this,
			    "This will rebuild the complete DOM.\nShall I go on ?",
			    "Warning",
			    JOptionPane.YES_NO_OPTION) // info message
	       != JOptionPane.YES_OPTION
	    ) {
			return;
		} else {
			
			NomadTaskDialog dialog = new NomadTaskDialog(
				new TaskModel() {

					DModule tmp = getModule();
					DModule[] modules = 
						ModuleDescriptions.sharedInstance().getModuleList();
					
					public String getDescription() {
						return "Rewriting DOM...";
					}

					public int getTaskCount() {
						return modules.length+1;
					}

					public String getTaskName(int taskIndex) {
						if (taskIndex < modules.length )
							return "Module: "+ modules[taskIndex].getName();
						else return "Finished.";
					}

					public void run(int taskIndex) {
						if (taskIndex < modules.length )
							setModule(modules[taskIndex]);
						else
							setModule(tmp);
					}} );
			
			dialog.setInfo("Info","Rewriting DOM");
			dialog.invoke();
		}
		
	}

	class SaveFileAsAction implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			saveFileAs();
		}
	}
	
	class FileOpenAction implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			loadFile();
		}
	}
	
    class AppExitAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	quit();
        }
    }

	private class ModuleButtonClickListener implements net.sf.nmedit.nomad.main.ui.ModuleToolbarEventListener {
		public void toolbarModuleSelected(DModule module) {
			setModule(module);
		}
	}
	
	class ThemeChangeAction implements ChangeListener {
		public void stateChanged(ChangeEvent event) 
        {
			ThemePluginProvider plugin = tps.getSelectedPlugin();
			if (plugin!=null) changeTheme(plugin);
		}
	}
	
	class RebuildDomAction implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			rebuildDOM();
		}
	}

    public void valueChanged( ListSelectionEvent e )
    {
        DModule sel = listView.getSelection(e);
        setModule(sel);
    }
	
}
