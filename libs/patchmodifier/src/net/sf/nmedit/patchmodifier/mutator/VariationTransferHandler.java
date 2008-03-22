/* Copyright (C) 2006-2007 Julien Pauty
 * 
 * This file is part of Nomad.
 * 
 * Nomad is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Nomad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Nomad; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package net.sf.nmedit.patchmodifier.mutator;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class VariationTransferHandler extends TransferHandler {
    
    private static final Log log = LogFactory.getLog(VariationTransferHandler.class);
   
	private static final long serialVersionUID = -7388283079611345524L;
	
	public static final DataFlavor variationFlavor = new DataFlavor(VariationTransferData.class, "variation");

    public VariationTransferHandler() {
//        serialArrayListFlavor = new DataFlavor(ArrayList.class,
//                                              "ArrayList");
    }

    public boolean importData(JComponent c, Transferable t) {
    	//System.out.println("import data");
        Variation target;
    	Vector<Integer> data = null;
        
        if (!canImport(c, t.getTransferDataFlavors())) {
            return false;
        }
        
        try {
            target = (Variation)c;
            
            if (t.isDataFlavorSupported(variationFlavor)) {
                data = ((VariationTransferData)t.getTransferData(variationFlavor)).getVariationData();
            } else {
                return false;
            }
        } catch (UnsupportedFlavorException ufe) {
            if (log.isErrorEnabled())
            {
                log.error("importData: unsupported data flavor", ufe);
            }
            return false;
        } catch (IOException ioe) {
            if (log.isErrorEnabled())
            {
                log.error("importData: I/O exception", ioe);
            }
            return false;
        }

        target.getState().updateValues(new Vector<Integer>(data));
         
        c.repaint();
        return true;
    }

    protected void exportDone(JComponent c, Transferable data, int action) {
    	
        if ((action == MOVE)) {
        	c.repaint();
        }
    }

    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        for (DataFlavor candidate: flavors)
            if (variationFlavor.equals(candidate))
                return true;
        return false;
    }
//
    @Override
    protected Transferable createTransferable(JComponent c) {

        if (c instanceof Variation) {
            Variation source = (Variation)c;
            VariationState s = source.getState();
            if (s!=null) 
            	return new VariationTransferable(source.getState().getValues());
            else 
            	return null;
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
    public final class VariationTransferData
    {
        private Vector<Integer> data;
        
        public VariationTransferData(Vector<Integer> data)
        {
            this.data = data;
        }
        
        public Vector<Integer> getVariationData()
        {
            return data;
        }
        
    }
    
    
    public class VariationTransferable implements Transferable {
        VariationTransferData data;

        public VariationTransferable(Vector<Integer> data) {
            this.data = new VariationTransferData(data);
        }

        public Object getTransferData(DataFlavor flavor)
                                 throws UnsupportedFlavorException {
            if (!isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return data;
        }

        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { variationFlavor };
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return variationFlavor.equals(flavor);
        }
    }
}