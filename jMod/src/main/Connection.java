// http://www.javaworld.com/javaworld/jw-03-1999/jw-03-dragndrop-p1.html

package main;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class Connection extends JLabel implements DragGestureListener, DragSourceListener, DropTargetListener, Transferable, Serializable {
	private int index, type;
	private String name;
	private boolean input;
	private int conX, conY;
	private boolean open; // Of er een kabel in zit...
	private Module module;
	private ImageIcon icon;
    private Connection _this = null;

    public final static int imageWidth = 13/2; // DE HELFT ERVAN!!!
    
    Cables cables = null;
    
    private static boolean moving = false;
	
//--- drag
	
	private DragSource dragSource = null;
	private int dragAction = DnDConstants.ACTION_COPY_OR_MOVE;

//--- drop
    
	private DropTarget dropTarget = null;
	private int dropAction = DnDConstants.ACTION_COPY_OR_MOVE;

//--- data	
	public static final DataFlavor inConnectionFlavor = new DataFlavor("jmod/inConnection", "jMod In Connection");
	public static final DataFlavor outConnectionFlavor = new DataFlavor("jmod/outConnection", "jMod Out Connection");

	private static final DataFlavor[] inFlavors = {inConnectionFlavor, outConnectionFlavor};
	private static final DataFlavor[] outFlavors = {outConnectionFlavor, inConnectionFlavor};

	private DataFlavor[] flavors = null;
	
	private int oldDragX = -1, oldDragY = -1;
	
  // endDrag zorgt ervoor dat de kabel die zojuist gedropt is, opnieuw getekend wordt met een curve.
  // Volgens mij worden alle kabels opnieuw getekend na een drop... ach ja...
    
	private boolean endDrag = true;
	
	Connection(Module newModule, boolean bInput, int newIndex, String newName, int newType, int newX, int newY) {
	    _this = this;
		index = newIndex;
		type = newType;
		input = bInput;
		name = newName;
		conX = newX;
		conY = newY;
		open = true;
		module = newModule;
		
		if (input){
			if (type == 0)
				icon = new ImageIcon("./grafix/_con_in_red.gif");
			if (type == 1)
				icon = new ImageIcon("./grafix/_con_in_blue.gif");
			if (type == 2)
				icon = new ImageIcon("./grafix/_con_in_yellow.gif");
			if (type == 3)
				icon = new ImageIcon("./grafix/_con_in.gif");
			flavors = inFlavors;
		}
		else {
			if (type == 0)
				icon = new ImageIcon("./grafix/_con_out_red.gif");
			if (type == 1)
				icon = new ImageIcon("./grafix/_con_out_blue.gif");
			if (type == 2)
				icon = new ImageIcon("./grafix/_con_out_yellow.gif");
			if (type == 3)
				icon = new ImageIcon("./grafix/_con_out.gif");
			flavors = outFlavors;
		}

		this.setIcon(icon);
		this.setLocation(conX, conY);
		this.setSize(icon.getIconWidth(), icon.getIconHeight());
		this.setToolTipText(name);
		module.add(this);
	
		dragSource = DragSource.getDefaultDragSource();
		dragSource.createDefaultDragGestureRecognizer(this, dragAction, this);

		dropTarget = new DropTarget(this, dropAction, this, true);
    
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
                moving = false;
                
    				// TODO ?Replace lgic to Cables 
    				Cable cab = null;
            
                    int modIndex = module.getModuleData().getModIndex();
                    int conIndex = _this.getConnectionIndex();
                    int inpIndex = _this.input?0:1;
    
                    boolean poly = module.getModuleData().getPoly();
                    int loopSize = poly?module.getModules().getCables().getPolySize():module.getModules().getCables().getCommonSize();

                    if (loopSize > 0) {
                      for (int i=0; i < loopSize; i++) {
                        cab = module.getModules().getCables().getCable(poly, i);
                        if (((cab.getBeginModule() == modIndex) && (cab.getBeginConnector() == conIndex) && (cab.getBeginConnectorType() == inpIndex)) ||
                            ((cab.getEndModule() == modIndex) && (cab.getEndConnector() == conIndex) && (cab.getEndConnectorType() == inpIndex))) {
                            Cables.setDragCable(cab);
                            Cables.getDragCable().setSelectColor1();
                            break;
                        }
                      }
                    }
                    if (e.isAltDown()) {
                        module.getModules().getCables().selectChain(cab, poly);
                    }
                    if (e.getClickCount() > 1 || e.isControlDown()) {
                        moving = true;
                        Cables.determBeginWindowDrag(module.getX() + conX, module.getY() + conY);
                        Cables.determBeginCableDrag(modIndex, conIndex, inpIndex);
                        Cables.getDragCable().setSelectColor2();
                }
			}

			public void mouseReleased(MouseEvent e)
			{
			    if (e.isAltDown()) {
			        Cable cab = Cables.getDragCable();
			        module.getModules().getCables().unSelectChain(cab, module.getModuleData().getPoly());
			    }
			    if (!e.isAltDown()) {
			        if (Cables.getDragCable() != null) Cables.getDragCable().restoreColor();
			    }
			}

		});
	}

// Getters

	public Module getModule() {
		return module;
	}

	public String getConnectionName() {
		return name;
	}

	public int getConnectionType() {
		return type;
	}

	public int getConnectionIndex() {
		return index;
	}

	public int getConnectionLocationX() {
		return conX;
	}
	public int getConnectionLocationY() {
		return conY;
	}

	public String getConnectionTypeName() {
		String returnType;
		switch (type) {
			case 0: returnType = "Audio"; break;		// 24bit, min = -64, max = +64 - 96kHz.
			case 1: returnType = "Control"; break;	// 24bit, min = -64, max = +64 - 24kHz.
			case 2: returnType = "Logic"; break;		//  1bit, low =  0, high = +64.
			case 3: returnType = "Slave"; break;		// frequentie referentie tussen master en slave modulen
			default: returnType = "Wrong type..."; break;
		}
		return returnType;
	}

//--- drag

	public void setEndDrag(boolean newEndDrag) {
		endDrag = newEndDrag;
	}

	public void dragGestureRecognized(DragGestureEvent e) {
	    endDrag = false;
        if (Cables.getDragCable() != null) Cables.getDragCable().restoreColor();
	    if (Cables.getDragCable() != null) module.getModules().getCables().unSelectChain(Cables.getDragCable(), module.getModuleData().getPoly());

	    // Het ligt aan de Action (shift/control) wat we gaan doen...
	    if (e.getDragAction() == 2 && !moving) { // new
	        moving = false;
	        e.startDrag(DragSource.DefaultCopyDrop, this, this);
	        Cables.newDragCable(module.getX() + conX, module.getY() + conY, Cable.SELECTCOLOR1, input?0:1);
	        module.getDesktopPane().add(Cables.getDragCable());
	    }
	    else {  // dan is de kabel die in de mousePressed gevonden is de juiste.
            moving = true;
            e.startDrag(DragSource.DefaultMoveDrop, this, this);
            Cables.setTempCable(Cables.getDragCable());
            Cables.getDragCable().setSelectColor2();
	    }

		module.getDesktopPane().setLayer(Cables.getDragCable(), JLayeredPane.DEFAULT_LAYER.intValue() + 10); // Ietskes hoger
//		Debug.println("Connection dragGestureRecognized(source)");
 	}

	public void dragEnter(DragSourceDragEvent e) {
//		Debug.println("dragEnter(source)?");
	}

	public void dragOver(DragSourceDragEvent e) {
//		Debug.println("dragOver(source)?");
	}

	public void dragExit(DragSourceEvent e) {
	  //		Debug.println("Connection dragExit(source)");
	}

	public void dragDropEnd(DragSourceDropEvent e) {

	    if (e.getDropSuccess() == false) {
	        if (moving) {
                if (e.getDropAction() == 2) { // rejected by wron connection 
                    module.getModules().getCables().addCable(module.getModuleData().getPoly(), Cables.getTempCable(Cables.getDragCable()));
                    module.getModules().getCables().redrawCables(module.getModules(), module.getModuleData().getPoly());
                }
                else { // rejected by 'module' -> delete
                    module.getModules().getCables().removeCable(Cables.getDragCable(), module.getModuleData().getPoly());
                }
	        }
	        else {
	            module.getModules().getCables().removeCable(Cables.getDragCable(), module.getModuleData().getPoly());
	        }
	        return;
	    }
		
	    int dropAction = e.getDropAction();
	    if (dropAction == DnDConstants.ACTION_MOVE) {
	        // wat te doen als het een move was... etc
	    }
		this.open = false; // dit moet ook in de target gebeuren
	}

	public void dropActionChanged(DragSourceDragEvent e) {
		//Bij het indrukken van Shift en Ctrl		
		//Debug.println("dropActionChanged(source)");
		
//		DragSourceContext context = e.getDragSourceContext();      
//		context.setCursor(DragSource.DefaultCopyNoDrop);	  	
	}

//	--- drop

	private boolean isDragOk(DropTargetDragEvent e) {
		DataFlavor chosen = null;

        // do we need this anymore?
		for (int i = 0; i < flavors.length; i++) {
			if (e.isDataFlavorSupported(flavors[i])) {
				chosen = flavors[i];
				break;
			}
		}
        
		/* if the src does not support any of the Connection flavors */
		if (chosen == null) {
			return false;
		}

		// the actions specified when the source
		// created the DragGestureRecognizer
		int sa = e.getSourceActions(); // Copy or Move

		// we're saying that these actions are necessary      
		if ((sa & dragAction) == 0)
			return false;

		return true;
	}
	
	public void dragEnter(DropTargetDragEvent e) {
//  	Debug.println("dragEnter(target)");
	}

	public void dragOver(DropTargetDragEvent e) {
	    if(!isDragOk(e)) {
	        e.rejectDrag();
	        return;
	    }

	    int newDragX = e.getLocation().x - Connection.imageWidth;
		int newDragY = e.getLocation().y - Connection.imageWidth;

		if ((newDragX != oldDragX) || (newDragY != oldDragY)) {
            if (Cables.getDragCable() != null) {
    		    Cables.getDragCable().setNewDragWindowLayout((module.getX() + conX) + (newDragX), (module.getY() + conY) + (newDragY));
    			Cables.getDragCable().repaint();
    			oldDragX = newDragX;
    			oldDragY = newDragY;
            }
		}
		e.acceptDrag(dragAction);
	}

	public void dragExit(DropTargetEvent e)
	{
//	    Debug.println("dragExit(target)");
    }

	public void dropActionChanged(DropTargetDragEvent e) {
//		Debug.println(new Integer(e.getDropAction()).toString());
	}

	public void drop(DropTargetDropEvent e) {
        
		DataFlavor chosen = null;
		if (e.isLocalTransfer() == false) {
//			chosen = StringTransferable.plainTextFlavor;
		}
		else {
			for (int i = 0; i < flavors.length; i++)
				if (e.isDataFlavorSupported(flavors[i])) {
					chosen = flavors[i];
					break;
				}		
		}

		if (chosen == null) {
			e.rejectDrop();      
			return;
		}

		// the actions that the source has specified with DragGestureRecognizer
		int sa = e.getSourceActions();      
		if ((sa & dropAction) == 0 ) { // Als een Move op Copy niet mag...
			e.rejectDrop();      
			return;
		}

		Object data = null;
		
		try {
			e.acceptDrop(dropAction);
			data = e.getTransferable().getTransferData(chosen);
			if (data == null)
				throw new NullPointerException();
	  	}
	  	catch (Throwable t) {
			t.printStackTrace();
			e.dropComplete(false);
			return;
	  	}
		
		if (data instanceof Connection) {
			Connection con = (Connection) data;
			int newColour = 6;
			
			con.endDrag = true;
			
            if (_this.equals(con)) {
                e.dropComplete(false);         
                return;
            }
            
            int modIndex = module.getModuleData().getModIndex();
            int conModIndex = con.getModule().getModuleData().getModIndex();
            
			if (!moving) {
			    if (con.input)
			        Cables.getDragCable().setCableData(conModIndex, con.index, 0, modIndex, index, input?0:1, newColour);
			    else 
			        Cables.getDragCable().setCableData(modIndex, index, this.input?0:1, conModIndex, con.index, con.input?0:1, newColour);
			}
			else {
			    if (Cables.getDragBeginCable())
//			        if (input) {
//			            Debug.println("a");
			            Cables.getDragCable().setCableData(modIndex, index, input?0:1, -1, 0, 0, newColour);
//			        }
//			        else {
//			            Debug.println("b");
//			            Cables.getDragCable().setCableData(modIndex, index, input?0:1, -1, 0, 0, newColour);
//			        }
			    else {
//			        if (con.input) {
//			            Debug.println("c");
			            Cables.getDragCable().setCableData(-1, 0, 0, modIndex, index, input?0:1, newColour);
//			        }
//			        else {
//			            Debug.println("d");
//			            Cables.getDragCable().setCableData(-1, 0, 0, modIndex, index, input?0:1, newColour);
//			        }
			    }
			}

            // Dragged to other end of cable
            if ((Cables.getDragCable().getBeginModule() == Cables.getDragCable().getEndModule()) && 
                    (Cables.getDragCable().getBeginConnector() == Cables.getDragCable().getEndConnector()) &&
                    (Cables.getDragCable().getBeginConnectorType() == Cables.getDragCable().getEndConnectorType())) {
                e.dropComplete(false);         
                return;
            }
            
			newColour = module.getModules().getCables().checkChain(Cables.getDragCable(), module.getModuleData().getPoly());
			if (newColour < 0) // reject cable;
			{
			    e.dropComplete(false);         
			    return;
			}
            
			Cables.getDragCable().setColor(newColour);

			if (!moving) module.getModules().getCables().addCable(module.getModuleData().getPoly(), Cables.getDragCable());
			module.getModules().getCables().redrawCables(module.getModules(), module.getModuleData().getPoly());
		}
		else {
			e.dropComplete(false);         
			return;
		}
		e.dropComplete(true);
	}

//--- data
	
	public DataFlavor[] getTransferDataFlavors() {
        return flavors; 
	}

	public boolean isDataFlavorSupported(DataFlavor targetFlavor) {
		// het dataobject (nu zichzelf) krijgt flavours mee van de source.
		return true;
	}

	public synchronized Object getTransferData(DataFlavor flavor) {
	    // afhankelijk van welke flavour de target(?) graag wil, geven we het juiste terug
	    
//	    if (flavor.equals(inConnectionFlavor))
//	        return this;
//	    if (flavor.equals(outConnectionFlavor))
//	        return this;
//        throw new UnsupportedFlavorException(flavor);

		// we vinden alles goed
		return this;
	}
    
    public String toString() {
        return (input?"input ":"output ") + index + " " + getConnectionTypeName() + "(" + type + ") " + name;
    }
}
