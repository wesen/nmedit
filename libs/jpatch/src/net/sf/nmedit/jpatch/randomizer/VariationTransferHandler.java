package net.sf.nmedit.jpatch.randomizer;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

public class VariationTransferHandler extends TransferHandler {
   
	private static final long serialVersionUID = -7388283079611345524L;
	
	DataFlavor variationFlavor;
    String variationType = DataFlavor.javaJVMLocalObjectMimeType +
                                ";class=net.sf.nmedit.jpatch.randomizer.Variation";

    public VariationTransferHandler() {
        try {
            variationFlavor = new DataFlavor(variationType);
        } catch (ClassNotFoundException e) {
            System.out.println(
             "VariationTransferHandler: unable to create data flavor");
        }
//        serialArrayListFlavor = new DataFlavor(ArrayList.class,
//                                              "ArrayList");
    }

    public boolean importData(JComponent c, Transferable t) {
    	//System.out.println("import data");
        Variation target;
    	int data[]= null;
        
        if (!canImport(c, t.getTransferDataFlavors())) {
            return false;
        }
        
        try {
            target = (Variation)c;
            if (hasVariationFlavor(t.getTransferDataFlavors())) {
                data = (int[])t.getTransferData(variationFlavor);
            } else {
                return false;
            }
        } catch (UnsupportedFlavorException ufe) {
            System.out.println("importData: unsupported data flavor");
            return false;
        } catch (IOException ioe) {
            System.out.println("importData: I/O exception");
            return false;
        }

        target.setValues(new int[data.length]);
        
        for (int i = 0; i < data.length; i++)
        {
        	target.getValues()[i]= data[i];
        }
        
        c.repaint();
        return true;
    }

    protected void exportDone(JComponent c, Transferable data, int action) {
//    	System.out.println("export done" + data);
    	
        if ((action == MOVE)) {
        	System.out.println("Move");
        	c.repaint();
        }
    }

    private boolean hasVariationFlavor(DataFlavor[] flavors) {
        if (variationFlavor == null) {
            return false;
        }

        for (int i = 0; i < flavors.length; i++) {
            if (flavors[i].equals(variationFlavor)) {
                return true;
            }
        }
        return false;
    }

    public boolean canImport(JComponent c, DataFlavor[] flavors) {
    	//System.out.println("can import");
        if (hasVariationFlavor(flavors))  { return true; }
        else 
        	return false;
    }
//
    @Override
    protected Transferable createTransferable(JComponent c) {
//    	System.out.println("transferable creation");
        if (c instanceof Variation) {
            Variation source = (Variation)c;
            //System.out.println("transferable creation");
            return new VariationTransferable(source.getValues());
        }
        return null;
    }
    
    @Override
    public int getSourceActions(JComponent c){
    	return TransferHandler.COPY_OR_MOVE;
    }
//
//    public int getSourceActions(JComponent c) {
//        return COPY_OR_MOVE;
//    }
//
    public class VariationTransferable implements Transferable {
    	int data[];

        public VariationTransferable(int data[]) {
            this.data = data;
        }

        public Object getTransferData(DataFlavor flavor)
                                 throws UnsupportedFlavorException {
            if (!isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return data;
        }

        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { variationFlavor
                                     };
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            if (variationFlavor.equals(flavor)) {
                return true;
            }
            else
            	return false;
        }
    }
}