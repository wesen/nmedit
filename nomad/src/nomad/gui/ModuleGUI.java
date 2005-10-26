package nomad.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import nomad.patch.Module;

public class ModuleGUI extends AbstractModuleGUI implements MouseListener, MouseMotionListener {

	private JLabel nameLabel = null;
	private ModuleSectionGUI parent = null;
	private Module module = null;

//  --- drag
	int dragX = 0, dragY = 0;
    int oldModuleDragX = 0;
    int oldModuleDragY = 0;

    JPopupMenu menu = new JPopupMenu();
    JMenuItem removeItem = new JMenuItem("Remove");
    
    public ModuleGUI(Module module, ModuleSectionGUI moduleSectionGUI) {
    	super();
    	
    	addMouseListener(this);
    	addMouseMotionListener(this);

	    removeItem.addActionListener(new RemoveModule());
	    menu.add(removeItem);
	    
    	parent = moduleSectionGUI;
    	this.module = module;
    	
        setLayout(null);
    	setBorder(BorderFactory.createRaisedBevelBorder());
    	
    	nameLabel = new JLabel("");
        nameLabel.setLocation(3,0);
        nameLabel.setFont(new Font("Dialog", Font.PLAIN, 10));
        add(nameLabel);

        // TODO ?Why is the icon empty here?
        if (module.getDModule().getIcon() != null)
        	add(new JLabel(new ImageIcon(module.getDModule().getIcon())));
    }
    
    public void setNameLabel(String name, int width) {
        nameLabel.setSize(width, 16);
    	nameLabel.setText(name);
    }
    
    public Module getModule() {
    	return module;
    }

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		// Sadly enough, e.isPopupTrigger() does always return false
//		System.out.println(e.isPopupTrigger()?"Popup":"Normal");		
//		System.out.println(e.getButton());
	    if ((e.getButton() & MouseEvent.BUTTON1) == MouseEvent.BUTTON1) {
	        dragX = e.getX();
	        dragY = e.getY();
	        oldModuleDragX = dragX;
	        oldModuleDragY = dragY;
	        parent.setLayer(this, JLayeredPane.DRAG_LAYER.intValue());
	    }
	}

	public void mouseReleased(MouseEvent e) {
		if (!e.isPopupTrigger()) { 
	    	parent.setLayer(this, JLayeredPane.DEFAULT_LAYER.intValue());
	        module.setNewPixLocation(getLocation().x, getLocation().y);
			module.getModuleSection().rearangeModules(module);
	    }

	    if (e.isPopupTrigger()) {
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
	}

	public void mouseDragged(MouseEvent e) {
		// Sadly enough, e.isPopupTrigger() or e.getButton() always return false or 0
//		System.out.println(e.isPopupTrigger()?"Popup":"Normal");		
//		System.out.println(e.getButton());
        if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK) {
            setLocation(getLocation().x + (e.getX() - dragX), getLocation().y + (e.getY() - dragY));
        }
	}

	public void mouseMoved(MouseEvent e) {
	}
	
    class RemoveModule implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	module.remove();
        	
//            modules.removeModule(_this.moduleData.getModIndexInteger(), _this.moduleData.getPoly());
//            desktopPane.remove(_this);
//            desktopPane.repaint();
//
//            modules.recalcGridXY(_this.getModuleData().getPoly());
//            desktopPane.setPreferredSize(new Dimension(modules.getMaxWidth(_this.getModuleData().getPoly()), modules.getMaxHeight(_this.getModuleData().getPoly())));
//            desktopPane.revalidate();
        }
    }
}
