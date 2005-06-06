package main;

//import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JPopupMenu.Separator;

public class Module extends JPanel implements DropTargetListener {
    // .pch info
	protected ModuleData moduleData = null;
    // .pch info

	private JLabel nameLabel = null;

	private JModAreaPane desktopPane = null;
	private Modules modules = null;
    private Module _this = null;

    private DropTarget dropTarget = null;
    private int dropAction = DnDConstants.ACTION_COPY_OR_MOVE;

    JPopupMenu menu = new JPopupMenu();
    JMenuItem removeItem = new JMenuItem("Remove");
    JMenuItem redrawItem = new JMenuItem("Redraw");

    int oldCableDragX = 0;
    int oldCableDragY = 0;
    int oldModuleDragX = 0;
    int oldModuleDragY = 0;
	int dragX = 0, dragY = 0;
	
	public Module(Integer newIndex, int newType, int newX, int newY, Modules newModules, boolean newPoly, JModAreaPane newDesktopPane) {
	    _this = this;
	
	    desktopPane = newDesktopPane;
			
		modules = newModules;
		moduleData = new ModuleData(_this, newIndex, newType, newX, newY, newPoly);
		nameLabel = new JLabel(""); 
        
	    dropTarget = new DropTarget(this, dropAction, this, true);
	
	    removeItem.addActionListener(new RemoveModule());
	    menu.add(removeItem);
        
        if (Debug.on) {
            menu.add(new Separator());
            redrawItem.addActionListener(new RedrawModule());
            menu.add(redrawItem);
        }
    
		// via 'implements MouseListener, MouseMotionListener' -> listener appart toevoegen aan object?
        this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
			    if (!e.isPopupTrigger()) {
			        dragX = e.getX();
			        dragY = e.getY();
			        oldModuleDragX = dragX;
			        oldModuleDragY = dragY;
			        desktopPane.setLayer(_this, JLayeredPane.DRAG_LAYER.intValue());
			    }
			}

			public void mouseReleased(MouseEvent e) {
			    if (!e.isPopupTrigger()) { 
			        desktopPane.setLayer(_this, JLayeredPane.DEFAULT_LAYER.intValue());
			        setPixLocation(_this, getLocation().x, getLocation().y);
			        modules.rearangeModules(desktopPane, _this, moduleData.getPoly());
			    }

			    if (e.isPopupTrigger()) {
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
			}
		});
        
		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
                if (!e.isPopupTrigger()) { // TODO +BUG??? e.getButton() always 0 ???!
                    setLocation(getLocation().x + (e.getX() - dragX), getLocation().y + (e.getY() - dragY));
                }
			}
		});
	}

// setters	
	public void setPixLocation(Module newModule, int newPixLocationX, int newPixLocationY) {
		// op het grid graag...
		newModule.getModuleData().setPixLocationX(((newPixLocationX + ModuleData.pixWidthD2) / ModuleData.pixWidth) * ModuleData.pixWidth);
		newModule.getModuleData().setPixLocationY(((newPixLocationY + ModuleData.pixHeightD2) / ModuleData.pixHeight) * ModuleData.pixHeight);
		
		newModule.setLocation(newModule.getModuleData().getPixLocationX(), newModule.getModuleData().getPixLocationY());

		modules.recalcGridXY(_this.getModuleData().getPoly());
		desktopPane.setPreferredSize(new Dimension(modules.getMaxWidth(_this.getModuleData().getPoly()), modules.getMaxHeight(_this.getModuleData().getPoly())));
		desktopPane.revalidate();
	}

    class RemoveModule implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            modules.removeModule(_this.moduleData.getModIndexInteger(), _this.moduleData.getPoly());
            desktopPane.remove(_this);
            desktopPane.repaint();

            modules.recalcGridXY(_this.getModuleData().getPoly());
            desktopPane.setPreferredSize(new Dimension(modules.getMaxWidth(_this.getModuleData().getPoly()), modules.getMaxHeight(_this.getModuleData().getPoly())));
            desktopPane.revalidate();
        }
    }

    class RedrawModule implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            PatchData.openProps();
            _this.removeAll();
            _this.moduleData.readProps();
            
            nameLabel.setText(moduleData.getModuleName());
            nameLabel.setLocation(3,0);
            nameLabel.setSize(moduleData.getPixWidth(),16);
            nameLabel.setFont(new Font("Dialog", Font.PLAIN, 10));
            _this.add(nameLabel);

            desktopPane.setLayer(_this, JLayeredPane.DEFAULT_LAYER.intValue());
            desktopPane.repaint();
            
            repaint();
            
            addObjects();
        }
    }

    public void addObjects() {
    }

    
// getters

	public JDesktopPane getDesktopPane() {
		return desktopPane;
	}

	public ModuleData getModuleData() {
		return moduleData;
	}

	public Modules getModules() {
		return modules;
	}

	public void drawModule() {
		this.setLayout(null);
		this.setBorder(BorderFactory.createRaisedBevelBorder());
		this.setLocation(moduleData.getPixLocationX(), moduleData.getPixLocationY());
		this.setSize(moduleData.getPixWidth(), moduleData.getPixHeight());

//		nameLabel.setText("#[" + moduleData.getModIndex() + "] n[" + moduleData.getModuleName() + "] t[" + moduleData.getModType() + "] s[" + moduleData.getTypeNameShort() + "]");
        nameLabel.setText(moduleData.getModuleName());
		nameLabel.setLocation(3,0);
		nameLabel.setSize(moduleData.getPixWidth(),16);
		nameLabel.setFont(new Font("Dialog", Font.PLAIN, 10));
//		nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
		this.add(nameLabel);

        this.setVisible(true);
		
		desktopPane.add(this, JLayeredPane.DEFAULT_LAYER.intValue());
	}

  public void dragOver(DropTargetDragEvent e)
  {
    int newDragX = e.getLocation().x - Connection.imageWidth;
    int newDragY = e.getLocation().y - Connection.imageWidth;

    if ((newDragX != oldCableDragX) || (newDragY != oldCableDragY)) {
      Cables.getDragCable().setNewDragWindowLayout(_this.getX() + (newDragX), _this.getY() + (newDragY));
      Cables.getDragCable().repaint();
      oldCableDragX = newDragX;
      oldCableDragY = newDragY;
    }
    e.rejectDrag();
    
//    Debug.println("Module dragOver");
  }

  public void dragEnter(DropTargetDragEvent arg0) {}
  public void dropActionChanged(DropTargetDragEvent arg0) {}
  public void dragExit(DropTargetEvent arg0) {}
  public void drop(DropTargetDropEvent arg0) {}
}
