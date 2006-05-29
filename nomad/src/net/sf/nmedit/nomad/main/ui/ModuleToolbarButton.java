package net.sf.nmedit.nomad.main.ui;

import java.awt.BorderLayout;
import java.awt.Insets;
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

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToolTip;

import net.sf.nmedit.nomad.core.nomad.NomadEnvironment;
import net.sf.nmedit.nomad.patch.ui.ModuleUI;
import net.sf.nmedit.nomad.theme.ModuleBuilder;
import net.sf.nmedit.nomad.util.NomadUtilities;
import net.sf.nmedit.nomad.xml.dom.module.DModule;

/**
 * @author Christian Schneider
 */
public class ModuleToolbarButton extends JButton implements DragGestureListener, DragSourceListener, Transferable {
	private DModule module;

	private DragSource dragSource = null;
	private int dragAction = DnDConstants.ACTION_COPY;
	private ModuleToolbar toolbar = null;
	
	public static final DataFlavor ModuleToolbarButtonFlavor = new DataFlavor("nomad/ModuleToolbarButtonFlavor", "Nomad ModuleToolbarButton");

	public ModuleToolbarButton(ModuleToolbar toolbar, DModule module) {
		super(new ImageIcon(module.getIcon()));
        setToolTipText(" "); // enables tool tips
		this.toolbar = toolbar;
		this.module = module;
		dragSource = DragSource.getDefaultDragSource();
		dragSource.createDefaultDragGestureRecognizer(this, dragAction, this);
        setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
	}

	public ModuleToolbar getToolbar() {
		return toolbar;
	}
	
	public DModule getModuleDescription() {
		return module;
	}
    
	public void dragGestureRecognized(DragGestureEvent dge) {
		if (toolbar.hasDraggingSupport())
			dge.startDrag(DragSource.DefaultCopyDrop, this, this);
	}

	public void dragEnter(DragSourceDragEvent dsde) { }
	public void dragOver(DragSourceDragEvent dsde) { }
	public void dropActionChanged(DragSourceDragEvent dsde) { }
    public void dragExit(DragSourceEvent dse) { }

	public void dragDropEnd(DragSourceDropEvent dsde) 
    {
		doClick();
	}

	public DataFlavor[] getTransferDataFlavors() {
		DataFlavor[] flavors = {ModuleToolbarButtonFlavor};
		return flavors;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		// DropTarget gives his flavors. DropSource looks if it can be dropped on the target.
		System.out.println("Plop");
		return true;
	}

	public Object getTransferData(DataFlavor flavor) {
		return this;
	}

    public JToolTip createToolTip()
    {
        return new JToolTip()
        {
            ModuleUI moduleUI = null;
            
            {
                setLayout(new BorderLayout());
                setMargin(new Insets(4,4,4,4));
                setBorder(BorderFactory.createCompoundBorder(getBorder(), BorderFactory.createEmptyBorder(4,4,4,4)));
                
                ModuleBuilder builder = NomadEnvironment.sharedInstance().getBuilder();
                if (builder != null) 
                {
                    moduleUI = builder.compose(module, null);
                    if (moduleUI!=null) 
                    {
                        moduleUI.setMinimumSize(moduleUI.getSize());
                        moduleUI.setMaximumSize(moduleUI.getSize());
                        moduleUI.setPreferredSize(moduleUI.getSize());
                        add(moduleUI, BorderLayout.NORTH);
                    }
                }

                String name = module.getName();
                String category = module.getParent().getParent().getName();
                String cycles = ""+NomadUtilities.roundTo(module.getCycles(), -2) + "%";
                
                String text = "<html><body>"
                        +"<table>"
                        +"<tr><td><b>Name:</b></td><td>"+name+"</td></tr>"
                        +"<tr><td><b>Category:</b></td><td>"+category+"</td></tr>"
                        +"<tr><td><b>Cycles:</b></td><td>"+cycles+"</td></tr>"
                        +"</table></body></html>"; 
                
                add(new JLabel(text), BorderLayout.CENTER);
                setPreferredSize(getLayout().preferredLayoutSize(this));
            }
            
            public void setTipText(String text)
            {
                // ignore text
            }
            
        };
    }
    
}
