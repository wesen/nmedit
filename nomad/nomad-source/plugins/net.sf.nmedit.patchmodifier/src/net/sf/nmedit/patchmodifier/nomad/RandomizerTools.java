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

package net.sf.nmedit.patchmodifier.nomad;

import java.util.Collection;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PPatch;
import net.sf.nmedit.jpatch.history.PUndoableEditSupport;
import net.sf.nmedit.jtheme.component.JTPatch;
import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.menulayout.ActionHandler;
import net.sf.nmedit.nomad.core.menulayout.MLEntry;
import net.sf.nmedit.nomad.core.menulayout.MenuLayout;
import net.sf.nmedit.nomad.core.swing.document.Document;
import net.sf.nmedit.patchmodifier.randomizer.Randomizer;
import net.sf.nmedit.patchmodifier.randomizer.RandomizerFactory;

public class RandomizerTools
{

    private static final String MENU_PATCH_RANDOMIZE = "nomad.menu.patch.randomize";
    
    private static RandomizerTools instance;
    
    public static RandomizerTools sharedInstance()
    {
        if (instance == null)
            instance = new RandomizerTools();
        return instance;
    }
    
    public void install()
    {
        // add randomize action
        MenuLayout menuLayout = Nomad.sharedInstance().getMenuLayout();        
        MLEntry e = menuLayout.getEntry(MENU_PATCH_RANDOMIZE);
        e.setEnabled(true);
        // ActionHandler invokes randomizePatch()
        e.addActionListener(new ActionHandler(this, true, "randomizePatch"));
    }
    
    public PPatch getSelectedPatch()
    {
        JTPatch jtpatch = getSelectedJTPatch();
        return jtpatch != null ? jtpatch.getPatch() : null;
    }
    
    public JTPatch getSelectedJTPatch()
    {
        Document doc = Nomad.sharedInstance().getDocumentManager().getSelection();
        if (doc == null) 
            return null;
        try
        {
            return (JTPatch) doc.getComponent();
        }
        catch (ClassCastException e)
        {
            return null; 
        }
    }

    private static Randomizer randomizer = RandomizerFactory.createGaussianRandomizer();
    
    public void randomizePatch() 
    {    
    	JTPatch jtpatch = getSelectedJTPatch();
		PPatch patch = getSelectedPatch();
    	Collection<? extends PModule> modules = jtpatch.getSelectedPModules();
    	
        PUndoableEditSupport ues = patch.getEditSupport();

    	if (!modules.isEmpty()) {
    		ues.beginUpdate("Randomize modules");
    		try {
    			randomizeModules(modules);
    		} finally {
    			ues.endUpdate();
    		}
    	} else {
    		if (patch == null)
    			return;

    		if (randomizer.getPatch()!=patch)
    		{
    			randomizer.setPatch(patch);
    		}
    		ues.beginUpdate("Randomize patch");
    		try {
    			randomizer.randomize();
    		} finally {
    			ues.endUpdate();
    		}
    	}
    }
    
    public void randomizeModules(Collection <? extends PModule> modules) {
    	Randomizer tmpRandom = RandomizerFactory. createGaussianRandomizer();
    	for (PModule module : modules) {
    		tmpRandom.addModule(module);
    	}
    	tmpRandom.randomize();
    }
}
