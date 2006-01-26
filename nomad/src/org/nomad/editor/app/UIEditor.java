package org.nomad.editor.app;

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

import org.nomad.dialog.JTaskDialog;
import org.nomad.dialog.TaskModel;
import org.nomad.editor.views.WorkspacePanel;
import org.nomad.editor.views.classes.NomadClassesView;
import org.nomad.editor.views.property.NomadPropertyEditor;
import org.nomad.editor.views.visual.VisualEditor;
import org.nomad.env.Environment;
import org.nomad.main.ThemePluginMenu;
import org.nomad.patch.ModuleSection;
import org.nomad.plugin.NomadPlugin;
import org.nomad.theme.UIFactory;
import org.nomad.util.misc.NomadUtilities;
import org.nomad.xml.XMLFileWriter;
import org.nomad.xml.dom.module.DModule;
import org.nomad.xml.dom.module.ModuleDescriptions;

public class UIEditor extends JFrame {
	
	// main()
	
	public static void main(String[] args) {
		NomadUtilities.setupAndShow(new UIEditor(), 0.4, 0.5);
	}
	
	// properties
	
	private Environment env = null;
	private WorkspacePanel workspace = null;
	private NomadClassesView classesView = null;
	private NomadPropertyEditor propertyEditor = null;
	private VisualEditor visualEditor = null;
    private DModule modified = null;

	// constructor
	
	public UIEditor() {
		
		// title
		
		super("Nomad UI Editor");

		// environment
		
		env = Environment.sharedInstance();
		env.loadAll();
		env.getToolbar().setAllowDragging(false);
		env.getToolbar().addModuleButtonClickListener(new ModuleButtonClickListener());

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
		getContentPane().add(env.getToolbar(), 	BorderLayout.NORTH);
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
		mnMenu = new ThemePluginMenu("Theme");
		((ThemePluginMenu)mnMenu).addPluginSelectionListener(new ThemeChangeAction());		
		mnBar.add(mnMenu);
		
		// dom
		mnMenu = new JMenu("DOM");
		mnMenu.add("rebuild").addActionListener(new RebuildDomAction());
		mnBar.add(mnMenu);

		this.setJMenuBar(mnBar);

		pack();
	}
	
	public void saveFileAs() {
		String uiFile = env.getFactory().getUIDescriptionFileName();
		JFileChooser fileChooser = new JFileChooser(uiFile);
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			uiFile = fileChooser.getSelectedFile().getAbsolutePath();
			if (NomadUtilities.isConfirmedByUser(this, "The file '"+uiFile+"' will be overwritten.\nShall I go on ?" )) {
				XMLFileWriter xml;
				try {
					xml = new XMLFileWriter(uiFile,
						"<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
						"<!DOCTYPE ui-description SYSTEM \"ui-description.dtd\">"
					);
					env.getBuilder().exportDom(xml);			
					xml.flush();
					xml.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}		
			}
		}
	}
	
	public void loadFile() {
		String uiFile = env.getFactory().getUIDescriptionFileName();		
		JFileChooser fileChooser = new JFileChooser(uiFile);
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			uiFile = fileChooser.getSelectedFile().getAbsolutePath();
			if (NomadUtilities.isConfirmedByUser(this, "Changes will be lost.\nShall I go on ?" )) {
				env.getBuilder().load(uiFile);
			}
		}
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
			env.getBuilder().createGUIComponents(visualEditor, module, false);
			visualEditor.setSize(ModuleSection.ModulePixDimension.PIXWIDTH,ModuleSection.ModulePixDimension.PIXHEIGHT*module.getHeight());
			visualEditor.setVisible(true);
			workspace.add(visualEditor);
		}
    }

	void changeTheme(final NomadPlugin plugin) {
		DModule current = getModule();
		setModule(null);
		env.setFactory((UIFactory) plugin.getFactoryInstance());
		classesView.setFactory(env.getFactory());
		propertyEditor.setEditingPropertySet(null);
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
			
			JTaskDialog.processTasks(
				this, new TaskModel() {

					DModule tmp = getModule();
					DModule[] modules = 
						ModuleDescriptions.sharedInstance().getModules()
							.toArray(new DModule[ModuleDescriptions.sharedInstance().getModules().size()]);
					
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

	private class ModuleButtonClickListener implements org.nomad.main.ModuleToolbarEventListener {
		public void toolbarModuleSelected(DModule module) {
			setModule(module);
		}
	}
	
	class ThemeChangeAction implements ChangeListener {
		public void stateChanged(ChangeEvent event) {
			
			if (event.getSource() instanceof ThemePluginMenu) {
				ThemePluginMenu menuThemePlugin = (ThemePluginMenu) event.getSource();
				NomadPlugin plugin = menuThemePlugin.getSelectedPlugin();
				if (plugin!=null) changeTheme(plugin);
			}
		}
	}
	
	class RebuildDomAction implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			rebuildDOM();
		}
	}
	
}
