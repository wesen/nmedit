/* Copyright (C) 2006 Christian Schneider
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

/*
 * Created on Feb 13, 2006
 */
package org.nomad.patch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import org.nomad.patch.ui.ModuleSectionListener;

public class ModuleSection implements Iterable<Module> {

	private int sectionIndex;
	private Hashtable<Integer, Module> moduleList ;

	private int maxGridX = 0;
	private int maxGridY = 0;

	private Cables transitionTable ;
	
	private ArrayList<ModuleSectionListener> sectionListenerList ;

	// todo set modulesection of modules
	
	public ModuleSection(int sectionIndex) {
		this.sectionIndex = sectionIndex;
		moduleList = new Hashtable<Integer, Module>();
		transitionTable = new Cables();
		sectionListenerList = new ArrayList<ModuleSectionListener>(2);
	}

	public Cables getCables() {
		return transitionTable;
	}

	public int getIndex() {
		return sectionIndex;
	}

	public boolean isCommonSection() {
		return getIndex() == Section.COMMON;
	}
	
	public boolean isPolySection() {
		return getIndex() == Section.POLY;
	}

	public void add(Module module) {        
        adjustGrid(module);
        if (module.getIndex()<=0)
        	module.setIndex( getModulesMaxIndex()+1 );
		moduleList.put(new Integer(module.getIndex()), module);
		module.setModuleSection(this);

        rearangeModules(module);
		fireModuleAddedEvent(module);
	}
	
	public void remove(Module module) {
		moduleList.remove(new Integer(module.getIndex()));
		module.setModuleSection(null);
		module.setIndex(-1);
		
		fireModuleRemovedEvent(module);
	}

	public Module get(int moduleIndex) {
		return moduleList.get(new Integer(moduleIndex));
	}
	
	public Module[] toArray() {
		Collection<Module> set = moduleList.values(); 
		return set.toArray(new Module[set.size()]);
	}

	public Iterator<Module> iterator() {
		return moduleList.values().iterator();
	}
	
	public Collection<Module> sortedModules() {
		Collection<Module> modules = moduleList.values();
		ArrayList<Module> list = new ArrayList<Module> (modules.size());
		
		int max = -1;
		for (Module m : modules) {
			if (m.getIndex()>=max) {
				max = m.getIndex();
				list.add(m);
			} else {
				for (int i=list.size()-1;i>=0;i--) {
					if (m.getIndex()>list.get(i).getIndex()) {
						list.add(i, m);
						break;
					}
				}
			}
		}
		
		return list;
	}
	
	public int getModuleCount() {
		return moduleList.values().size();
	}
	
	protected void adjustGrid(Module mod) {
		int w = Math.max(maxGridX, mod.getX()+1);
        int h = Math.max(maxGridY, mod.getY()+mod.getInfo().getHeight());

		setGrid(w, h);
	}
	
	protected void recalculateGrid() {
		int w=0;
		int h=0;
		for (Module m : this) {
			w = Math.max(w, m.getX()+1);
			h = Math.max(h, m.getY()+m.getInfo().getHeight());
		}

		setGrid(w, h);
	}
	
	protected void setGrid(int w, int h) {
		if (w!=maxGridX || h!=maxGridY) {
			maxGridX = w;
			maxGridY = h;
        	fireModuleSectionResized();
		}
	}

	protected int getModulesMaxIndex() {
		int max = 0;
		for (Integer n : moduleList.keySet() ) {
			max = Math.max(max, n.intValue());
		}
		return max;
	}

	public void rearangeModules(Module module) {
		// module must not be in list
		// stores all modules with same x grid coordinate and which are below module
		// sorted by the y value
		ArrayList<Module> col = new ArrayList<Module>(getModuleCount());
		col.add(module);
		
		for (Module m : this) {
			
			if ((m!= module) && (m.getX() == module.getX()) && (m.getY()+m.getInfo().getHeight()>=module.getY()))
			{
				// do insertion sort : sort(y)
				
				Module max = null;
				
				for (int i=0;i<col.size();i++) {
					Module c = col.get(i); 
					if (c.getY()>=m.getY()) {
						max = col.get(i);
						break;
					}
				}
				
				if (max!=null) 	// insert
					col.add(col.indexOf(max), m);
				else 			// append
					col.add(m);
			}
			
		}
		
		int bottom = Math.max(0, col.get(0).getY());
		boolean moved = false;
		
		for (Module m : col) {
			
			if (m.getY()<=bottom) {
				moved = true;
				m.setY(bottom);
				bottom = m.getY()+m.getInfo().getHeight();
			} else {
				if (moved) {
					// space - no more modules to move - we are done
					break;
				}
			}
			
		}
		
		recalculateGrid();
	}

	public int getMaxGridX() {
		return maxGridX;
	}

	public int getMaxGridY() {
		return maxGridY;
	}

	public void addSectionListener(ModuleSectionListener l) {
		if(!sectionListenerList.contains(l))
			sectionListenerList.add(l);
	}
	
	public void removeSectionListener(ModuleSectionListener l) {
		sectionListenerList.remove(l);
	}
	
	public void fireModuleRemovedEvent(Module m) {
		if (!sectionListenerList.isEmpty())
			for (ModuleSectionListener l:sectionListenerList)
				l.moduleRemoved(m);
	}
	
	public void fireModuleAddedEvent(Module m) {
		if (!sectionListenerList.isEmpty())
			for (ModuleSectionListener l:sectionListenerList)
				l.moduleAdded(m);
	}

	private void fireModuleSectionResized() {
		if (!sectionListenerList.isEmpty())
			for (ModuleSectionListener l:sectionListenerList)
				l.moduleSectionResized();
	}
	
}
 