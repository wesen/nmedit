// http://nmedit.sourceforge.net
    
package main;

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

public class jMod extends JFrame {
    public static String creatorP = "jMod";
    public static String creatorV = "v0.5";
    public static String creatorR = "beta r2";
	
	JFileChooser fileChooser = new JFileChooser("./patches/");
	
	JMenuBar menuBar = null;
	JMenu menuFile = null;
	JMenuItem menuExitItem,	menuNewItem, menuOpenItem, menuCloseItem, menuCloseAllItem = null;
	JMenuItem menuSaveItem, menuSaveAsItem, menuSaveAllItem = null;

	JPanel toolPanel = null;
	JTabbedPane tabbedPane = null;

	JButton button = null;

	JPanel panelMain = null;

    class NewListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Patch patch = new Patch();
            tabbedPane.add("new" + (tabbedPane.getTabCount()+1),patch.createPatch(""));
            tabbedPane.setSelectedComponent(patch);
			tabbedPane.getSelectedComponent().setName(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
			
        }
    }

    class ExitListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

	class FileLoadListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				Patch patch = new Patch();
				String name = fileChooser.getSelectedFile().getName();
				name = name.substring(0,name.indexOf(".pch"));
				tabbedPane.add(name,patch.createPatch(fileChooser.getSelectedFile().getPath()));
				tabbedPane.setSelectedComponent(patch);
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
					stream.write(((Patch)tabbedPane.getSelectedComponent()).savePatch().toString().getBytes());
					stream.flush();
					stream.close();
				}
				catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	class FileCloseListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			tabbedPane.remove(tabbedPane.getSelectedComponent());
		}
	}

	class ExitWindowListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
	
	jMod() {
		Debug.set(true);
		
        ToolTipManager.sharedInstance().setInitialDelay(0);
        
        this.setTitle(creatorP + " " + creatorV + " " + creatorR);

//        this.setTitle(creatorP + " " + creatorV + " " + creatorR + " [" + System.getProperty("os.name") + " OS on " + System.getProperty("os.arch") + " machine, java version: " + System.getProperty("java.version") + "]");
//    
//        if (
//            (System.getProperty("os.name").equalsIgnoreCase("Windows 2000")
//                    && System.getProperty("os.arch").equalsIgnoreCase("x86")
//                    && System.getProperty("java.version").equalsIgnoreCase("1.4.2_07"))
//                    ||
//    	    (System.getProperty("os.name").equalsIgnoreCase("Windows 2000")
//    	            && System.getProperty("os.arch").equalsIgnoreCase("x86")
//    	            && System.getProperty("java.version").equalsIgnoreCase("1.4.2_06"))
//    	            ||
//    		(System.getProperty("os.name").equalsIgnoreCase("Windows 2000")
//    		        && System.getProperty("os.arch").equalsIgnoreCase("x86")
//    		        && System.getProperty("java.version").equalsIgnoreCase("1.5.0_01"))
//        ) {
//            this.setTitle(this.getTitle() + " tested");
//        }
//        else {
//            this.setTitle(this.getTitle() + " not tested");
//        }

        this.setJMenuBar(createMenu());

// Hoofd panel = Toolbar (panel) + TabbedPane
		panelMain = new JPanel();
		panelMain.setLayout(new BorderLayout());

// Toolbar
		toolPanel = new JPanel();
		
//		toolPanel.setLayout(new BorderLayout());

//		button = new JButton("Just a tool button");
//		button.setMnemonic('b');
//
//		toolPanel.add(button);
//		panelMain.add(toolPanel, BorderLayout.NORTH);

// TabbedPane
		tabbedPane = new JTabbedPane();
		panelMain.add(tabbedPane, BorderLayout.CENTER);

		this.getContentPane().add(panelMain,BorderLayout.CENTER);

		this.addWindowListener(new ExitWindowListener());

//		button.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//			}
//		});
		
		this.setSize(1024, 768);
//		this.setSize(800, 600);
		this.setVisible(true);
		
		Patch patch = new Patch();
        tabbedPane.add("new" + (tabbedPane.getTabCount()+1), patch.createPatch("./patches/all.pch"));
		tabbedPane.setSelectedComponent(patch);
		tabbedPane.getSelectedComponent().setName(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()));
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

		new jMod();
	}
}
