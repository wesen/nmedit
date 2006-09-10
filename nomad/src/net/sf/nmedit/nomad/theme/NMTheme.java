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
 * Created on Sep 9, 2006
 */
package net.sf.nmedit.nomad.theme;

import java.io.InputStream;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.Module;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.VoiceArea;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DModule;
import net.sf.nmedit.jtheme.Theme;
import net.sf.nmedit.jtheme.dom.ThemeDom;
import net.sf.nmedit.nomad.patch.ui.ModuleSectionUI;
import net.sf.nmedit.nomad.patch.ui.ModuleUI;
import net.sf.nmedit.nomad.util.graphics.ImageTracker;

public class NMTheme extends Theme
{
    private ImageTracker imageTracker = 
        new ImageTracker(ImageTracker.IMAGE_TRACKER_DISALLOW_REPLACE);


    public NMTheme()
    {
        putComponentClass(ModuleUI.class, CONTAINER_ALIAS);
    }
    
    public void init(String xmlFile)
    {
        InputStream in = getClass().getResourceAsStream(xmlFile);
        ThemeDom.importDocument( getDom(), in );
    }
    
    public ModuleUI buildModule(Module module)
    {
        ModuleUI m = buildModule(module.getDefinition());
        m.setModule(module);
        return m;
    }

    public ImageTracker getImageTracker() 
    {
        return imageTracker;
    }
    
    public ModuleUI buildModule(DModule module)
    {
        ModuleUI m = (ModuleUI) build(module.getModuleID());
        m.setModuleSpec(module);
        m.setSize(ModuleUI.Metrics.WIDTH, ModuleUI.Metrics.getHeight(module));
        return m;
    }

    public ModuleSectionUI getModuleSectionUI(VoiceArea moduleSection) {
        return new ModuleSectionUI(moduleSection);
    }

}
