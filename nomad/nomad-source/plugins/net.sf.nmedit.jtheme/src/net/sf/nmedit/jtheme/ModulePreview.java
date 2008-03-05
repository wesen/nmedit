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
 * Created on Jun 29, 2006
 */
package net.sf.nmedit.jtheme;

import java.awt.Image;

import net.sf.nmedit.jpatch.ModuleDescriptions;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jtheme.store.StorageContext;
import net.sf.nmedit.jtheme.util.ModuleImageRenderer;

public class ModulePreview extends ImagePreview
{

    /**
     * 
     */
    private static final long serialVersionUID = -2330694492292729151L;
    private ModuleDescriptions moduleDescriptions;
    private JTContext uiContext;
    private StorageContext storageContext;
    private PModuleDescriptor moduleDescriptor;

    public void setModules(ModuleDescriptions md)
    {
        if (this.moduleDescriptions != md)
        {
            this.moduleDescriptions = md;
            update();
        }
    }

    public void setModule(PModuleDescriptor m)
    {
        if (this.moduleDescriptor != m)
        {
            this.moduleDescriptor = m;
            update();
        }
    }

    public void setUIContext(JTContext c)
    {
        if (this.uiContext != c)
        {
            this.uiContext = c;
            
            if (this.uiContext != null)
                storageContext = uiContext.getStorageContext();
            else
                storageContext = null;
            update();
        }
    }

    public ModuleDescriptions getModules()
    {
        return moduleDescriptions;
    }

    public PModuleDescriptor getModule()
    {
        return moduleDescriptor;
    }

    public JTContext getUIContext()
    {
        return uiContext;
    }

    private void update()
    {
        try
        {
            update1();
        }
        catch (JTException e)
        {
            setPreviewImage(null);
        }
    }

    private void update1() throws JTException
    {
        if (moduleDescriptions == null
          ||uiContext == null
          ||storageContext == null
          ||moduleDescriptor == null)
        {
            setPreviewImage(null);
            return;
        }
        
        Image image = ModuleImageRenderer.render(uiContext, moduleDescriptor, false);
        setPreviewImage(image);
    }

}
