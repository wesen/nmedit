package main;

import java.io.BufferedReader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JDesktopPane;
import javax.swing.JLayeredPane;

public class Cables {
	private Vector poly, common = null;
	
    private static Cable dragCable = null;
    private static Cable tempCable = null;
	private static boolean dragBeginWindow = false; // Of er aan het begin of het eind van het Window getrokken wordt...
	private static boolean dragBeginCable = false; // Of er aan het begin of het eind van de Kabel getrokken wordt...
	  
	JDesktopPane desktopPanePoly = null;
	JDesktopPane desktopPaneCommon = null;
    
    private PatchData patchData = null; 
  
	Cables(PatchData patchData) {
		poly = new Vector();
		common = new Vector();
        this.patchData = patchData;
	}

  public static void determBeginWindowDrag(int newX, int newY) { // Of er aan de input of de output getrokken wordt...
    if (dragCable != null) dragBeginWindow = dragCable.determBeginOrEndWindowDrag(newX, newY);
//    Debug.println("Window: " + (dragBeginWindow?"Begin":"End"));
  }

  public static void determBeginCableDrag(int newMod, int newIndex, int newInput) { // Of er aan de input of de output getrokken wordt...
      if (dragCable != null) dragBeginCable = dragCable.determBeginOrEndCableDrag(newMod, newIndex, newInput);
//    Debug.println("Cable:  " + (dragBeginCable?"Begin":"End"));
  }

	static void newDragCable(int newX, int newY, int newType, int newBeginType) {
		dragCable = new Cable(newX, newY, newType);
        dragCable.setBeginConnectorType(newBeginType);
	}
  
    static void setDragCable(Cable newCable) {
        dragCable = newCable;
    }
        
    static Cable getDragCable() {
        return dragCable;
    }

    static void setTempCable(Cable newCable) {
        if (newCable != null)
        {
            int modBIndex = newCable.getBeginModule();
            int conBIndex = newCable.getBeginConnector();
            int inpBIndex = newCable.getBeginConnectorType(); // snul?
    
            int modEIndex = newCable.getEndModule();
            int conEIndex = newCable.getEndConnector();
            int inpEIndex = newCable.getEndConnectorType();
    
            tempCable = new Cable(modBIndex, conBIndex, inpBIndex, modEIndex, conEIndex, inpEIndex);
            tempCable.setColor(newCable.getColor());
        }
    }
            
    static Cable getTempCable(Cable cab) {
        int modBIndex = tempCable.getBeginModule();
        int conBIndex = tempCable.getBeginConnector();
        int inpBIndex = tempCable.getBeginConnectorType(); // snul?

        int modEIndex = tempCable.getEndModule();
        int conEIndex = tempCable.getEndConnector();
        int inpEIndex = tempCable.getEndConnectorType();
        
        int colIndex = tempCable.getColor();

        cab.setCableData(modBIndex, conBIndex, inpBIndex, modEIndex, conEIndex, inpEIndex, colIndex);
        return cab;
    }

  static void setDragBeginWindow(boolean newDragInput) {
    dragBeginWindow = newDragInput;
  }
  
  public static boolean getDragBeginWindow() {
    return dragBeginWindow;
  }
  
  static void setDragBeginCable(boolean newDragInput) {
    dragBeginCable = newDragInput;
  }
  
  public static boolean getDragBeginCable() {
    return dragBeginCable;
  }
  
  public void selectChain(Cable cab, boolean bPoly) {
      Hashtable chainTable = new Hashtable();
      getChain(cab, bPoly, chainTable);
      Enumeration enum = chainTable.keys();
      cab = null;
      while (enum.hasMoreElements()) {
          cab = (Cable) enum.nextElement();
          cab.setSelectColor1();
      }
  }

  public void unSelectChain(Cable cab, boolean bPoly) {
      Hashtable chainTable = new Hashtable();
      getChain(cab, bPoly, chainTable);
      Enumeration enum = chainTable.keys();
      cab = null;
      while (enum.hasMoreElements()) {
          cab = (Cable) enum.nextElement();
          cab.restoreColor();
      }
  }
  
  public boolean loopCheck(Cable originalCable, Cable checkCable, Hashtable chainTable, boolean begin) {
      Enumeration enum;
      enum = chainTable.keys();
      Cable cab = null;
      
      boolean ret = true;

//      int modIndexO = originalCable.getBeginModule();
//      int conIndexO = originalCable.getBeginConnector();
//      int inpTypeO = originalCable.getBeginConnectorType();

//      int modEIndexO = originalCable.getEndModule();
//      int conEIndexO = originalCable.getEndConnector();
//      int inpETypeO = originalCable.getEndConnectorType();

      int modIndexC = 0;
      int conIndexC = 0;
      int inpTypeC = 0;

      if (begin) {
          modIndexC = checkCable.getEndModule();
          conIndexC = checkCable.getEndConnector();
          inpTypeC = checkCable.getEndConnectorType();
      }
      else {
          modIndexC = checkCable.getBeginModule();
          conIndexC = checkCable.getBeginConnector();
          inpTypeC = checkCable.getBeginConnectorType();
      }

      int modBIndexN = 0;
      int conBIndexN = 0;
      int inpBTypeN = 0;

      int modEIndexN = 0;
      int conEIndexN = 0;
      int inpETypeN = 0;

      while (enum.hasMoreElements()) {
          cab = (Cable) enum.nextElement();
          if (!cab.equals(checkCable)) { // the checkCable will allways match itself, skip it
          
              modBIndexN = cab.getBeginModule();
              conBIndexN = cab.getBeginConnector();
              inpBTypeN = cab.getBeginConnectorType();
    
              modEIndexN = cab.getEndModule();
              conEIndexN = cab.getEndConnector();
              inpETypeN = cab.getEndConnectorType();
              
              // if we found a cable that 'fits', than call the same function
              // if the cable that is found, is the same as the start cable (match), than we have a loop.
              //    the first time we enter the function this is true, but than the match is at the 'end', which is ok.
              
              // if we found a cable that is connected to the current cable, redo the check.
              if (modIndexC == modBIndexN && conIndexC == conBIndexN && inpTypeC == inpBTypeN) {
                  // if the begin matches AND its the original cable, than we have a loop 
                  if (originalCable.equals(cab)) return false;
                  ret = loopCheck(originalCable, cab, chainTable, true);
                  if (!ret) break;
              } else
                  if (modIndexC == modEIndexN && conIndexC == conEIndexN && inpTypeC == inpETypeN) {
                      // if the end matches, it can not be the original cable...
    //                  if (modIndexO == modBIndexN && conIndexO == conBIndexN && inpTypeO == inpBTypeN) {
    //                      // loop!
    //                      if (!originalCable.equals(checkCable)) return false;
    //                  }
                      // als hij aan het einde matched EN het is de originele kabel, dan willen we hem ook niet testen
                      if (!originalCable.equals(cab)) ret = loopCheck(originalCable, cab, chainTable, false);
                      if (!ret) break;
                  }
                  
          }
      }
      return ret;
  }
  
  public int checkChain(Cable checkCable, boolean bPoly) {
      int color = Cable.LOOSE;
      
      int inpBTypecheckCable = checkCable.getBeginConnectorType();
      int inpETypecheckCable = checkCable.getEndConnectorType();
      
      Hashtable chainTable = new Hashtable();
      
      // get whole chain
      getChain(checkCable, bPoly, chainTable);

      // walk trought cables and search outputs
      Enumeration enum;
      enum = chainTable.keys();
      Connection con = null;
      Connection newOutCon = null;
      Connection outCon = null;
      Cable cab = null;
      while (enum.hasMoreElements()) {
          cab = (Cable) enum.nextElement();
          
          int modBIndex = cab.getBeginModule();
          int conBIndex = cab.getBeginConnector();
          int inpBType = cab.getBeginConnectorType();

          int modEIndex = cab.getEndModule();
          int conEIndex = cab.getEndConnector();
          int inpEType = cab.getEndConnectorType();
          
          if (inpBType==1 && inpEType==1) {
              return -1; // we connected an output with an output 
          }
          if (inpBType==1) {
              con = patchData.getModules().getModule(bPoly, modBIndex).getModuleData().getConnection(inpBType, conBIndex);
          }
          if (inpEType==1) {
              con = patchData.getModules().getModule(bPoly, modEIndex).getModuleData().getConnection(inpEType, conEIndex);
          }
          if (con != null) { // We have got an output
              color = con.getConnectionType(); // Cable will get te color of the output
              if (inpBTypecheckCable==0 && inpETypecheckCable==0) { // unless is was an input-input cable
                  color = cab.getColor();
              }
              if (!cab.equals(checkCable)) { // We have found our already existing output
                  if (outCon == null) outCon = con;
              }
              if ((outCon != null) && (newOutCon != null) && (!newOutCon.equals(con))) // if it is NOT our out-connection on the new cable
                  return -1; // Reject
              newOutCon = con; // the out-connection is on our new cable
          }
          con = null;
      }
      
      if (!loopCheck(checkCable, checkCable, chainTable, true)) return -1;
      
      if ((inpBTypecheckCable==1 || inpETypecheckCable==1) || (inpBTypecheckCable==0 && inpETypecheckCable==0)) {
          // if our cable is connected to an output (or output is gone), the whole chain must be renewed to the new color :S
          enum = chainTable.keys();
          cab = null;
          while (enum.hasMoreElements()) {
              cab = (Cable) enum.nextElement();
              cab.setColor(color);
          }
      }
      return color; // < 0 fout; >= 0 color
  }
  
  public void getChain(Cable checkCable, boolean bPoly, Hashtable checkTable) {
      
      checkTable.put(checkCable, new Integer(1));
      
      Cable cab = null;
      
      int modBIndex = checkCable.getBeginModule();
      int conBIndex = checkCable.getBeginConnector();
      int inpBIndex = checkCable.getBeginConnectorType();

      int modEIndex = checkCable.getEndModule();
      int conEIndex = checkCable.getEndConnector();
      int inpEIndex = checkCable.getEndConnectorType();
      
      for (int i=0; i < (bPoly?getPolySize():getCommonSize()); i++) {
          cab = getCable(bPoly, i);

          if (
              ((cab.getBeginModule() == modBIndex) && (cab.getBeginConnector() == conBIndex) && (cab.getBeginConnectorType() == inpBIndex)) ||
              ((cab.getEndModule() == modEIndex) && (cab.getEndConnector() == conEIndex) && (cab.getEndConnectorType() == inpEIndex)) ||
              ((cab.getBeginModule() == modEIndex) && (cab.getBeginConnector() == conEIndex) && (cab.getBeginConnectorType() == inpEIndex)) ||
              ((cab.getEndModule() == modBIndex) && (cab.getEndConnector() == conBIndex) && (cab.getEndConnectorType() == inpBIndex))
          ) {
                if (!checkTable.containsKey(cab)) // if cable is not already in chain
                    checkTable.put(cab, new Integer(0)); // add unprocessed cable
          }
      }

      // zijn er nog 0en?
      if (checkTable.containsValue(new Integer(0)))
      {
          // get de kabel die als eerste 0 is.
          Enumeration enum;
          enum = checkTable.keys();
          while (enum.hasMoreElements()) {
              checkCable = (Cable) enum.nextElement();
              if ( ((Integer)checkTable.get(checkCable)).equals(new Integer(0)) ) {
                  break;
              }
          }
          getChain(checkCable, bPoly, checkTable);
      }
  }
  
  public void removeCablesFromModule(int modIndex, boolean bPoly) {
    int i = 0;
    Cable cab = null;
    Vector plopVector = new Vector();

    for (i=0; i < (bPoly?getPolySize():getCommonSize()); i++) {
        cab = getCable(bPoly, i);
        if (cab.getBeginModule() == modIndex || cab.getEndModule() == modIndex) {
//            removeCable(cab, bPoly);
            plopVector.add(cab);
        }
    }
    
    for (i=0; i < plopVector.size(); i++) {
        removeCable((Cable)plopVector.get(i), bPoly);
    }
  }
  
  public void removeCable(Cable cab, boolean bPoly) {
    if (bPoly) {
      poly.remove(cab);
      desktopPanePoly.remove(cab);
      desktopPanePoly.repaint();
    }
    else {
      common.remove(cab);
      desktopPaneCommon.remove(cab);
      desktopPaneCommon.repaint();
    }
  }

	public void recalcCableLocations(Modules modules, boolean bPoly) {
		int size;
		
		if (bPoly)
			size = this.getPolySize();
		else
			size = this.getCommonSize();
		
		for (int i=0; i < size;i++) {
			Connection tempConnectionIn;
			Connection tempConnectionOut;
			int tempConnectionOutInput;
			Cable tempCable;
			Module tempModule;
			ModuleData tempModuleDataIn;
			ModuleData tempModuleDataOut;
			int xIn = 0, yIn = 0, xOut = 0, yOut = 0;
	
			tempCable = this.getCable(bPoly, i);
			
// In Connector			

			tempModule =  modules.getModule(bPoly, tempCable.getBeginModule());
			try {
				tempModuleDataIn = tempModule.getModuleData();
				tempConnectionIn = (Connection)tempModuleDataIn.getInputs().get(tempCable.getBeginConnector());
				xIn = tempModuleDataIn.getPixLocationX() + tempConnectionIn.getConnectionLocationX();
				yIn = tempModuleDataIn.getPixLocationY() + tempConnectionIn.getConnectionLocationY();
			}
			catch (Exception e) {
			    Debug.println("In connector data in module type " + tempModule.getModuleData().getModType() + " is invalid.\n" + e.getStackTrace());
			}
			
	
//	Out Connector
			tempConnectionOutInput = tempCable.getEndConnectorType();
			try {
				tempModule =  modules.getModule(bPoly, tempCable.getEndModule());
				tempModuleDataOut = tempModule.getModuleData();
				if (tempConnectionOutInput == 0)
					tempConnectionOut = (Connection)tempModuleDataOut.getInputs().get(tempCable.getEndConnector());
				else
					tempConnectionOut = (Connection)tempModuleDataOut.getOutputs().get(tempCable.getEndConnector());
				xOut = tempModuleDataOut.getPixLocationX() + tempConnectionOut.getConnectionLocationX();
				yOut = tempModuleDataOut.getPixLocationY() + tempConnectionOut.getConnectionLocationY();
			}
			catch (Exception e) {
			    Debug.println("Out conector data in module type " + tempModule.getModuleData().getModType() + " is invalid.\n" + e.getStackTrace());
			}
			tempCable.setCableWindowLayout(xIn, yIn, xOut, yOut, true);
			tempCable.reInitCables();
		}
	}

	public void redrawCables(Modules modules, boolean bPoly) {
		int size;

		this.recalcCableLocations(modules, bPoly);
		
		if (bPoly)
			size = this.getPolySize();
		else
			size = this.getCommonSize();
		
		for (int i=0; i < size; i++) {
			this.getCable(bPoly, i).repaint();
		}
	}

	public void drawCables(JDesktopPane desktopPane, boolean bPoly) {
		int size;
		Cable tempCable; 
		if (bPoly) {
			size = getPolySize();
			desktopPanePoly = desktopPane;
    }
		else {
			size = getCommonSize();
      desktopPaneCommon = desktopPane;
    }
			
		for (int i=0; i < size; i++) {
			tempCable = getCable(bPoly, i);
			desktopPane.add(tempCable);
			desktopPane.setLayer(tempCable, JLayeredPane.DEFAULT_LAYER.intValue() + 10); // Ietskes hoger
		}
	}

// Setters

   public Cable addCable(boolean bPoly, String params) {
	   Cable cab = new Cable(params);
	   if (bPoly)
		   poly.add(cab);
	   else
		   common.add(cab);
	   return cab;
   }

   public void addCable(boolean bPoly, Cable newCable) {
	   if (bPoly)
		   poly.add(newCable);
	   else
		   common.add(newCable);
   }

// Getters

	public int getPolySize() {
		return poly.size();
	}

	public int getCommonSize() {
		return common.size();
	}

	public Cable getCable(boolean bPoly, int index) {
		Cable returnCab;
		returnCab = (Cable) null;
		if (bPoly)
			returnCab = (Cable) poly.get(index);
		else
			returnCab = (Cable) common.get(index);
		return returnCab;
	}

// Inlezen patch gegevens

	public void readCableDump(BufferedReader pchFile) {
		String dummy;
		boolean bPoly;
		try {
			if (pchFile.readLine().trim().compareTo("1") == 0)
				bPoly = true;
			else
				bPoly = false;
			
			while ((dummy = pchFile.readLine()) != null) {
				if (dummy.compareToIgnoreCase("[/CableDump]") != 0)
					addCable(bPoly, dummy);
				else
					return;
			}
			return; // Einde file?
		}
		catch(Exception e) {
			System.out.println(e + " in readCableDump");
		}
	}

// Creeren patch gegevens

	public StringBuffer createCableDump(StringBuffer result) {
		int i = 0;
		Cable cab = null;
		result.append("[CableDump]\r\n");
		result.append("1\r\n");
		if (getPolySize() > 0) {
			for (i=0; i < getPolySize(); i++) {
				cab = getCable(true, i);
				result.append("" + cab.getColor() + " " + cab.getBeginModule() + " " + cab.getBeginConnector() + " 0 " + cab.getEndModule() + " " + cab.getEndConnector() + " " + cab.getEndConnectorType() + "\r\n");
			}
		}
		result.append("[/CableDump]\r\n");
		
		result.append("[CableDump]\r\n");
		result.append("0\r\n");
		if (getCommonSize() > 0) {
			for (i=0; i < getCommonSize(); i++) {
				cab = getCable(false, i);
				result.append("" + cab.getColor() + " " + cab.getBeginModule() + " " + cab.getBeginConnector() + " 0 " + cab.getEndModule() + " " + cab.getEndConnector() + " " + cab.getEndConnectorType() + "\r\n");
			}
		}
		result.append("[/CableDump]\r\n");
		return result;
	}
}

//public int checkChain(Cable checkCable, boolean bPoly, Hashtable checkTable, int index) {
//
//    //TODo !!Eruit halen van het 'output tellen'.
//    //Een lijst van kabels terug geven en daarmee gaan we de benodigde connectors aflopen.
//    //Met die lijst van connectors kunnen we de 'enige' output bepalen en of we cabel kunnen accepteren die we krijgen. 
//    
//    //      boolean ret = true;
//    int retVal = 0; //0 en 1 is ok, 2 is fout...
//    boolean stop = false;
//    
//    if (checkTable == null) {
//        checkTable = new Hashtable();
//    }
//    checkTable.put(checkCable, new Integer(1));
//    
//    Cable cab = null;
//    int i = 0;
//    
//    int modBIndex = checkCable.getBeginModule();
//    int conBIndex = checkCable.getBeginConnector();
//    int inpBIndex = checkCable.getBeginConnectorType();
//
//    int modEIndex = checkCable.getEndModule();
//    int conEIndex = checkCable.getEndConnector();
//    int inpEIndex = checkCable.getEndConnectorType();
//    
//    if ((inpBIndex==1) && (inpEIndex==1)) { retVal = 2; }
//
//    for (i=0; i < (bPoly?getPolySize():getCommonSize()); i++) {
//        cab = getCable(bPoly, i);
//
//        if (
//            ((cab.getBeginModule() == modBIndex) && (cab.getBeginConnector() == conBIndex) && (cab.getBeginConnectorType() == inpBIndex)) ||
//            ((cab.getEndModule() == modEIndex) && (cab.getEndConnector() == conEIndex) && (cab.getEndConnectorType() == inpEIndex)) ||
//            ((cab.getBeginModule() == modEIndex) && (cab.getBeginConnector() == conEIndex) && (cab.getBeginConnectorType() == inpEIndex)) ||
//            ((cab.getEndModule() == modBIndex) && (cab.getEndConnector() == conBIndex) && (cab.getEndConnectorType() == inpBIndex))
//        ) {
//            if (!checkTable.containsKey(cab))
//                checkTable.put(cab, new Integer(0));
//        }
//    }
//
//    // zijn er nog 0en?
//    if (checkTable.containsValue(new Integer(0)))
//    {
//        // get de kabel die als eerste 0 is.
//        Enumeration enum;
//        enum = checkTable.keys();
//        while (enum.hasMoreElements()) {
//            checkCable = (Cable) enum.nextElement();
//            if ( ((Integer)checkTable.get(checkCable)).equals(new Integer(0)) ) {
//                break;
//            }
//        }
//        if (checkChain(checkCable, bPoly, checkTable, index) == 2) stop = true;
//    }
//
//    if (!stop) {
//        Enumeration enum;
//        enum = checkTable.keys();
//        while (enum.hasMoreElements()) {
//            checkCable = (Cable) enum.nextElement();
//            
//            modBIndex = checkCable.getBeginModule();
//            conBIndex = checkCable.getBeginConnector();
//            inpBIndex = checkCable.getBeginConnectorType();
//  
//            modEIndex = checkCable.getEndModule();
//            conEIndex = checkCable.getEndConnector();
//            inpEIndex = checkCable.getEndConnectorType();
//            
//  //          Debug.println("[" + modBIndex + ", " + conBIndex + ", " + inpBIndex + "]-[" + modEIndex + ", " + conEIndex + ", " + inpEIndex + "]");
//            if ((inpBIndex==1) || (inpEIndex==1)) {
//                   retVal++;  
//                   if (retVal > 1)
//                       break;
//            }
//        }
//    } else retVal = 2;
//    return retVal;
//}
