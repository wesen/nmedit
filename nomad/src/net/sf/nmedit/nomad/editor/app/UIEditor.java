package net.sf.nmedit.nomad.editor.app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.jmisc.xml.XMLFileWriter;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DModule;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.ModuleDescriptions;
import net.sf.nmedit.nomad.core.nomad.NomadEnvironment;
import net.sf.nmedit.nomad.editor.views.WorkspacePanel;
import net.sf.nmedit.nomad.editor.views.classes.NomadClassesView;
import net.sf.nmedit.nomad.editor.views.property.NomadPropertyEditor;
import net.sf.nmedit.nomad.editor.views.visual.VisualEditor;
import net.sf.nmedit.nomad.main.action.ThemePluginSelector;
import net.sf.nmedit.nomad.main.dialog.NomadTaskDialog;
import net.sf.nmedit.nomad.main.dialog.TaskModel;
import net.sf.nmedit.nomad.patch.ui.ModuleUI;
import net.sf.nmedit.nomad.theme.UIFactory;
import net.sf.nmedit.nomad.theme.plugin.ThemePluginProvider;
import net.sf.nmedit.nomad.util.NomadUtilities;


public class UIEditor extends JFrame {
	
    // properties
	
	private NomadEnvironment env = null;
	private WorkspacePanel workspace = null;
	private NomadClassesView classesView = null;
	private NomadPropertyEditor propertyEditor = null;
	private VisualEditor visualEditor = null;
    private DModule modified = null;
    private ThemePluginSelector tps;

	// constructor
	
	public UIEditor(String name) {
		
		// title
		
		super(name);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
		// environment
		
		env = NomadEnvironment.sharedInstance();
		env.setCachingEnabled(false);
	//	env.loadAll();
        /* TODO 
		env.getToolbar().setAllowDragging(false);
		env.getToolbar().addModuleButtonClickListener(new ModuleButtonClickListener());*/

		// components
		
		classesView = new NomadClassesView();
		classesView.setFactory(env.getFactory());

		propertyEditor = new NomadPropertyEditor(this);
		propertyEditor.setPreferredSize(new Dimension(200, 100));
		propertyEditor.setMinimumSize(new Dimension(10, 100));

		workspace = new WorkspacePanel();
		workspace.setSize(new Dimension(250,400));

		JSplitPane split = new JSplitPane( JSplitPane.VERTICAL_SPLIT, propertyEditor, classesView );
		split.setDividerLocation(0.5);

		getContentPane().setLayout(new BorderLayout());
        // TODO add module tree
		//getContentPane().add(env.getToolbar(), 	BorderLayout.NORTH);
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
	
	public void saveFileAs() {/*
		String uiFile = env.getFactory().getUIDescriptionFileName();
		JFileChooser fileChooser = new JFileChooser(uiFile);
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			uiFile = fileChooser.getSelectedFile().getAbsolutePath();
			if (NomadUtilities.isConfirmedByUser(this, "The file '"+uiFile+"' will be overwritten.\nShall I go on ?" )) {
				XMLFileWriter xml;
				try {
					xml = new XMLFileWriter(uiFile,
						"<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
						"<!DOCTYPE theme SYSTEM \"theme-v10.dtd\">"
					);
					env.getBuilder().exportDom(xml);			
					xml.flush();
					xml.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}		
			}
		}*/
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
    
    public void setModule(DModule module) {
    	this.modified = module;
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
    }
    
	void changeTheme(final ThemePluginProvider plugin) {
		DModule current = getModule();
		setModule(null);
		env.setFactory((UIFactory) plugin.getFactory());
		classesView.setFactory(env.getFactory());
		propertyEditor.setEditingPropertySet(null,null);
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
	
}
