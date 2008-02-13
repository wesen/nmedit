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
package net.sf.nmedit.jtheme.component;

import java.util.ArrayList;

import javax.swing.JComponent;

import net.sf.nmedit.jpatch.PPatch;
import net.sf.nmedit.jtheme.JTContext;

public class JTPatch extends JComponent
{

    /**
     * 
     */
    private static final long serialVersionUID = 794652007213898614L;
    private JTContext context;
    protected ArrayList<JTModuleContainer> moduleContainers;

    public JTPatch(JTContext context)
    {
    	moduleContainers = new ArrayList<JTModuleContainer>();
        this.context = context;
    }
    
    public JTModuleContainer[] getModuleContainers() {
    	JTModuleContainer []res = new JTModuleContainer[1];
    	return (JTModuleContainer[]) moduleContainers.toArray(res);
    }
    
    public void addModuleContainer(JTModuleContainer container) {
    	moduleContainers.add(container);
    }

    public void removeModuleContainer(JTModuleContainer container) {
    	moduleContainers.remove(container);
    }

    public JTContext getContext()
    {
        return context;
    }

    public PPatch getPatch()
    {
        return null;
    }
    
    public PPatch newPatchWithModules(JTModule modules[]) {
    	return null;
    }

}

