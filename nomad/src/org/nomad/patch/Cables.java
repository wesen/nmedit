package org.nomad.patch;

import java.util.ArrayList;

public class Cables {
	private ArrayList cables = null;

	public final static class CableType {
	    public final static int AUDIO = 0;
	    public final static int CNTRL = 1;
	    public final static int LOGIC = 2;
	    public final static int SLAVE = 3;
	    public final static int USER1 = 4;
	    public final static int USER2 = 5;
	    public final static int LOOSE = 6;
	    public final static int CREATE = 7; // When user creates a Cable  
	    public final static int MOVE = 8;   // When user moves a Cable
	}

	Cables() {
		cables = new ArrayList();
	}

  public void removeCablesFromModule(int modIndex) {
    int i = 0;
    Cable cab = null;
    ArrayList tempArrayList = new ArrayList();

    for (i=0; i < getCablesSize(); i++) {
        cab = getCable(i);
        if (cab.getBeginModule() == modIndex || cab.getEndModule() == modIndex) {
            tempArrayList.add(cab);
        }
    }
    
    for (i=0; i < tempArrayList.size(); i++) {
        removeCable((Cable)tempArrayList.get(i));
    }
  }
  
  public void removeCable(Cable cab) {
      cables.remove(cab);
  }

// Setters

   public Cable addCable(String params) {
	   Cable cab = new Cable(params);
	   cables.add(cab);
	   return cab;
   }

   public void addCable(Cable newCable) {
	   cables.add(newCable);
   }

// Getters

	public int getCablesSize() {
		return cables.size();
	}

	public Cable getCable(int index) {
		Cable returnCab;
		returnCab = (Cable) cables.get(index);
		return returnCab;
	}

}
