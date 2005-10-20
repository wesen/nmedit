package nomad.patch;

import javax.swing.JPanel;

import nomad.patch.Cables.CableType;

public class Cable extends JPanel {
	/**
	 * 0 6 0 0 4 0 1 = colour, module, connector, in (0), module, connector, in (0) or uit (1).
     * v3.0: first connector must be an input
	 */

    // .pch info
	private int colour;
	private int beginArray[], endArray[];
    // .pch info

	Cable(String params) {
		super();
//		[0] = module index, [1] = connector index, [2] = input or output

		beginArray = new int[3];
		endArray = new int[3];

		String[] paramArray = new String[7];
		paramArray = params.split(" ");
		
		colour = Integer.parseInt(paramArray[0]);

// The 4th parameter must be a 0(in), the 7th can be an in(0) or an out(1)
    	beginArray[0] = Integer.parseInt(paramArray[1]);
    	beginArray[1] = Integer.parseInt(paramArray[2]);
    	beginArray[2] = Integer.parseInt(paramArray[3]);
        if (beginArray[2] != 0) System.out.println("IN CONNECTOR EXPECTED!!! PATCH NON 3.0 complient!");

		endArray[0] = Integer.parseInt(paramArray[4]);
		endArray[1] = Integer.parseInt(paramArray[5]);
		endArray[2] = Integer.parseInt(paramArray[6]);
	}

	Cable(int newBeginMod, int newBeginConnector, int newBeginConnectorType, int newEndMod, int newEndConnector, int newEndConnectorType) {
		beginArray = new int[3];
		endArray = new int[3];

        setCableData(newBeginMod, newBeginConnector, newBeginConnectorType, newEndMod, newEndConnector, newEndConnectorType, CableType.LOOSE);
	}
	
	Cable(int newType) {
        beginArray = new int[3];
        endArray = new int[3];
    
        colour = newType;
	}

	public void setCableData(int newInMod, int newInConnector, int newInInput, int newOutMod, int newOutConnector, int newOutInput, int newType) {
    
        if (newInMod > -1) { // if -1, keep the old connector values
      		beginArray[0] = newInMod;
      		beginArray[1] = newInConnector;
      		beginArray[2] = newInInput;
        }
        if (newOutMod > -1) { // if -1, keep the old connector values
      		endArray[0] = newOutMod;
      		endArray[1] = newOutConnector;
      		endArray[2] = newOutInput; // In of Uit
        }
    
        if (newInInput == 1) {// swap when there is an out connector in the first connection (v3.0) 
            int newTempMod, newTempConnector, newTempInput = -1;
                
            newTempMod = beginArray[0];
            newTempConnector = beginArray[1];
            newTempInput = beginArray[2];
              
            beginArray[0] = endArray[0];
            beginArray[1] = endArray[1];
            beginArray[2] = endArray[2];
            
            endArray[0] = newTempMod;
            endArray[1] = newTempConnector;
            endArray[2] = newTempInput;
        }
   
		colour = newType;
	}
	
// Getters

    public int getBeginModule() {
        return beginArray[0];
    }

    public int getBeginConnector() {
        return beginArray[1];
    }

    public int getBeginConnectorType() {
        return beginArray[2];
    }

	public int getEndModule() {
		return endArray[0];
	}

	public int getEndConnector() {
		return endArray[1];
	}

	public int getEndConnectorType() {
		return endArray[2];
	}

	public int getColor() {
		return colour;
	}

	public String getName() {
		switch (colour) {
			case CableType.AUDIO: return "Audio";		// 24bit, min = -64, max = +64 - 96kHz.
			case CableType.CNTRL: return "Control";		// 24bit, min = -64, max = +64 - 24kHz.
			case CableType.LOGIC: return "Logic";		//  1bit, low =  0, high = +64.
			case CableType.SLAVE: return "Slave";		// frequentie referentie between master and slave modules
			case CableType.USER1: return "User1";
			case CableType.USER2: return "User2";
			case CableType.LOOSE: return "Loose";
			default: return "Wrong type...";
		}
	}

//	 Setters

    public void setBeginModule(int i) {
        beginArray[0] = i;
    }
   
    public void setBeginConnector(int i) {
        beginArray[1] = i;
    }
   
    public void setBeginConnectorType(int i) {
        beginArray[2] = i;
    }

    public void setEndModule(int i) {
        endArray[0] = i;
    }
   
    public void setEndConnector(int i) {
        endArray[1] = i;
    }
   
    public void setEndConnectorType(int i) {
        endArray[2] = i;
    }

	public void setColor(int i) {
		colour = i;
	}
}
