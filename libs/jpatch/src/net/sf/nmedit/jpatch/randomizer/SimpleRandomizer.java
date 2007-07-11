package net.sf.nmedit.jpatch.randomizer;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.PPatch;

public class SimpleRandomizer {
	// Randomize all paramaters, except the morph parameters
	private static SimpleRandomizer randomizer = null;
	public static SimpleRandomizer getRandomizer(){
		if (randomizer == null)
			randomizer = new SimpleRandomizer();
		return randomizer;
	}
	
	public void randomize(PPatch patch){
		for (int i = 0; i < patch.getModuleContainerCount() ; i++)
    	{
    		PModuleContainer container = patch.getModuleContainer(i);
    		//System.out.println(container.getName());
    		
    		for (int m = 1; m <=container.getModuleCount()  ; m ++)
    		{  
    			
    			//System.out.println(m+ " "+ patch.getModuleContainer(i).getModuleCount() );
    			PModule module = container.getModule(m);
    			if (module!=null)
	    			for (int p = 0 ; p < module.getParameterCount(); p++)
	    			{
	    				PParameter param= module.getParameter(p);
	    				if (param.getName().startsWith("morph") == false)
	    				{
	    				
	    					float max = param.getMaxValue();
	    					float min = param.getMinValue();
	    				//System.out.println((int)(Math.random()*(max-min)));
	    					//if (max-min ==127)
	    					param.setValue((int)(Math.random()*(max-min)));
	    				}
	    				
	    			}
    			
    	 		//System.out.println(patch.getModuleContainer(i).getName());
    		}
    		
    	}
	}
	
}
