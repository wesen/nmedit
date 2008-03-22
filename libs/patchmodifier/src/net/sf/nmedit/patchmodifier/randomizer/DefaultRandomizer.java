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
package net.sf.nmedit.patchmodifier.randomizer;

import java.util.ArrayList;
import java.util.List;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleContainer;
import net.sf.nmedit.jpatch.PParameter;
import net.sf.nmedit.jpatch.PPatch;
import net.sf.nmedit.jpatch.PRoles;
import net.sf.nmedit.jpatch.event.PModuleContainerEvent;
import net.sf.nmedit.jpatch.event.PModuleContainerListener;
import net.sf.nmedit.jpatch.impl.PBasicRoles;

public class DefaultRandomizer 
  implements Randomizer, PModuleContainerListener
{

    private static final PRoles RejectedModules = PBasicRoles.setOf("morph");
    private static final PRoles RejectedParameters = PBasicRoles.setOf(
            "level", 
            "mute", // level 
            "morph", // morph
            "ui" // modifies user interface but not the sound
    );
    
    private PPatch patch;
    private List<PParameter> parameters = new ArrayList<PParameter>(100);
    private RandomizerAlgorithm algo;
    
    public DefaultRandomizer()
    {
        super();
    }
    
    public DefaultRandomizer(RandomizerAlgorithm algo)
    {
        this.algo = algo;
    }
    
    public RandomizerAlgorithm getAlgorithm()
    {
        return algo;
    }
    
    public void setAlgorithm(RandomizerAlgorithm ra)
    {
        this.algo = ra;
    }
    
    public PPatch getPatch()
    {
        return patch;
    }

    public void randomize()
    {
        if (algo == null || parameters.isEmpty())
            return;
        algo.randomize(parameters);
    }

    public void setPatch(PPatch patch)
    {
        PPatch oldValue = this.patch;
        PPatch newValue = patch;
        if (oldValue == newValue) return;
        if (oldValue != null) uninstall(oldValue);
        this.patch = newValue;
        if (newValue != null) install(newValue);
    }

    private boolean acceptParameter(PParameter p)
    {
        return !RejectedParameters.intersects(p.getRoles());
    }
    
    protected boolean acceptModule(PModule module)
    {
        return !RejectedModules.intersects(module.getRoles());
    }

    protected boolean acceptContainer(PModuleContainer c)
    {
        return true;
    }

    protected void uninstall(PPatch patch)
    {
        for (int i=0;i<patch.getModuleContainerCount();i++)
        {
            PModuleContainer c = patch.getModuleContainer(i);
            c.removeModuleContainerListener(this);
            parameters.clear();
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

    private void install(PParameter parameter)
    {
        parameters.add(parameter);
    }

    private void uninstall(PParameter parameter)
    {
        parameters.remove(parameter);
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

}
