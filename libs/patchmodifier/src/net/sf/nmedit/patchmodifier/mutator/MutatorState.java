/* Copyright (C) 2008 Julien Pauty
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

import java.util.Vector;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.PPatch;
import net.sf.nmedit.jpatch.event.PModuleContainerEvent;
import net.sf.nmedit.jpatch.event.PModuleContainerListener;
import net.sf.nmedit.jpatch.event.PParameterEvent;
import net.sf.nmedit.jpatch.event.PParameterListener;

public class MutatorState implements PModuleContainerListener,
		 PParameterListener
{
	
	
	// overall vector containing all the variation of the state
	private Vector<VariationState> variations = new Vector<VariationState>();
	
	// array containing the var use to mutate, randomize and cross
	private Vector<VariationState> workingVariations = new Vector<VariationState>();
	
	private VariationState mother;
	private VariationState father;
	
	// array contains the parameters
	private Vector<PParameter> parameters = new Vector<PParameter>();
	
	private int variationNum = 18;
	private int workVariationNum = 4;
	
	private VariationState selectedState = null;
	
	public MutatorState(PPatch p) {
		for(int i = 0 ; i < variationNum; i ++) {
			variations.add(new VariationState());			
		}
			
		for(int i = 0 ; i < workVariationNum; i ++) {
			VariationState s = new VariationState();
			variations.add(s);
			workingVariations.add(s);	
		}
		
		mother = new VariationState();	
		variations.add(mother);
		father = new VariationState();		
		variations.add(father);
			
		install(p);
	}
	
	public Vector<PParameter> getParameters() {
		return parameters;
	}
	
//	public Vector<Vector<Integer>> getVariations() {
//		return variations;
//	}
	
	protected boolean acceptContainer(PModuleContainer c)
    {
        return true;
    }
	
	protected boolean acceptModule(PModule m)
    {
        return true;
    }
	
	protected void uninstall(PPatch patch)
	{
		for (int i=0;i<patch.getModuleContainerCount();i++)
		{
			PModuleContainer c = patch.getModuleContainer(i);
			c.removeModuleContainerListener(this);
			//parameters.clear();
		}
	}

	protected void install(PPatch patch)
	{
		for (int i=0;i<patch.getModuleContainerCount();i++)
		{
			PModuleContainer c = patch.getModuleContainer(i);
			if (acceptContainer(c))
			{
				c.addModuleContainerListener(this);
				install(c);
			}
		}
	}

	protected void uninstall(PModuleContainer c)
	{
		for (PModule module: c)
			uninstall(module);
	}

	protected void install(PModuleContainer c)
	{
		for (PModule module: c)
		{
			if (acceptModule(module))
			{
				install(module);
			}
		}
	}

	protected void uninstall(PModule module)
	{
		for (int i=0;i<module.getParameterCount();i++)
		{
			uninstall(module.getParameter(i));
		}
	}

	protected void install(PModule module)
	{
		for (int i=0;i<module.getParameterCount();i++)
		{
			PParameter p = module.getParameter(i);
			if (acceptParameter(p))
				install(p);
		}
	}

	private boolean acceptParameter(PParameter param) {
		 float max = param.getMaxValue();
         float min = param.getMinValue();
     
         if (max-min ==127)
        	 return true;
         
         // TODO: user filter there. 
         if (param.getName().startsWith("Morph"))
        	 return false;
         
         return false;
	}

	private void install(PParameter parameter)
	{
		parameters.add(parameter);

		for (VariationState s: variations) {
			s.addParameter(parameter);
		}
		
		parameter.addParameterListener(this);
	}

	private void uninstall(PParameter parameter)
	{
		parameters.remove(parameter);
		
		for (VariationState s: variations) {
			s.removeParameter(parameter);			
		}
		
		parameter.removeParameterListener(this);
	}

	public void moduleAdded(PModuleContainerEvent e)
	{
		if (acceptModule(e.getModule()))
			install(e.getModule());
	}

	public void moduleRemoved(PModuleContainerEvent e)
	{
		uninstall(e.getModule());
	}

	public void addModule(PModule module) {
		install(module);
	}

	public VariationState getFather() {
		return father;
	}

	public VariationState getMother() {
		return mother;
	}

	public Vector<VariationState> getVariations() {
		return variations;
	}

	public Vector<VariationState> getWorkingVariations() {
		return workingVariations;
	}

	public void focusRequested(PParameterEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void parameterValueChanged(PParameterEvent e) {		
		if (selectedState != null) {
			PParameter p = e.getParameter();
			selectedState.setParameterValue(p, p.getValue());			
		}
	}
	
	public void setSelectedVariation(VariationState s) {
		if (selectedState != null)
			selectedState.setSelected(false);
		
		selectedState = s;
		selectedState.setSelected(true);
	}
}
