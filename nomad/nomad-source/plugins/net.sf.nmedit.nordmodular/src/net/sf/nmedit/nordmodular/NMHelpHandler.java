/* Copyright (C) 2008 Christian Schneider
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
package net.sf.nmedit.nordmodular;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.nmedit.jpatch.PModule;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.impl.PBasicModule;
import net.sf.nmedit.jtheme.JTContext;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.component.JTModule;
import net.sf.nmedit.jtheme.help.HelpHandler;
import net.sf.nmedit.jtheme.help.ModuleHelpPage;
import net.sf.nmedit.jtheme.store2.ModuleElement;
import net.sf.nmedit.jtheme.util.ModuleImageRenderer;
import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.jpf.TempDir;
import net.sf.nmedit.nomad.core.swing.document.Document;

public class NMHelpHandler implements HelpHandler
{
    private static final Log log = LogFactory.getLog(NMHelpHandler.class);
    
    
    private ImageIcon helpIcon = getIcon("help.png");

    private static ImageIcon getIcon(String name)
    {
        name = "/icons/tango/16x16/"+name;
        URL url = Nomad.sharedInstance().getClass().getResource(name);
        
        if (url == null)
            return null;
        
        return new ImageIcon(url);
    }
    
    public boolean hasHelpFor(Object o)
    {
        if (o instanceof PModuleDescriptor)
            return true;
        else if (o instanceof PModule)
            return true;
        return false;
    }

    public void showHelpFor(Object o)
    {
        PModuleDescriptor dmodule = null;
        if (o instanceof PModuleDescriptor)
        {
            dmodule = (PModuleDescriptor) o;
        }
        else if (o instanceof PModule)
        {
            dmodule = ((PModule)o).getDescriptor();
        }   
        if (dmodule == null)
            return;
        
        // render image
        
        ModuleHelpPage helpPage = new ModuleHelpPage(
                NMContextData.sharedInstance().getJTContext(),  
                dmodule, getModuleImageURL(dmodule));
        
        Nomad.sharedInstance().getDocumentManager().add(new HelpDoc(helpPage));

        
        
    }
    
    private URL getModuleImageURL(PModuleDescriptor dmodule)
    {
        if (dmodule.getComponentId() == null)
        {
            if (log.isWarnEnabled())
            {
                log.warn("module has no componentid "+dmodule);
            }
            return null;
        }
        String name = dmodule.getComponentId()+".png";
        
        TempDir tempdir = TempDir.forObject(this);
        if (tempdir == null) 
        {
            if (log.isWarnEnabled())
            {
                log.warn("TempDir.forObject(this) returned null");
            }
            return null;
        }
        File dir = tempdir.getTempFile("help/modules");
        
        if (!dir.exists())
        {
            try
            {
                dir.mkdirs();
            }
            catch (Throwable t)
            {
                if (log.isErrorEnabled())
                {
                    log.error("could not create dir for module images", t);
                }
                return null;
            }
        }
        
        File imgFile = new File(dir, name);
        if (!imgFile.exists())
        {
            // create image
            JTContext context = NMContextData.sharedInstance().getJTContext();
            ModuleElement mstore = context.getStorageContext().getModuleStoreById(dmodule.getComponentId());
    
            JTModule component;
            try
            {
                component = mstore.createModule(context, new PBasicModule(dmodule));
            }
            catch (JTException e)
            {
                if (log.isErrorEnabled())
                {
                    log.error("could not create module component for "+dmodule, e);
                }
                return null;
            }
            
            ModuleImageRenderer mir = new ModuleImageRenderer();
            mir.add(component);
            mir.setForDragAndDrop(false);
            mir.setPaintExtraBorder(false);
            mir.setRenderCablesEnabled(false);
            Image image = mir.render();
            if (image == null)
            {
                if (log.isWarnEnabled())
                {
                    log.warn("rendering image failed");
                }
                return null;
            }
            
            if (!(image instanceof RenderedImage))
            {
                if (log.isWarnEnabled())
                {
                    log.warn("rendered image not instanceof "+RenderedImage.class);
                }
            }
            
            // store image
            try
            {
                ImageIO.write((RenderedImage)image, "PNG", imgFile);
            } 
            catch (IOException e)
            {
                if (log.isErrorEnabled())
                {
                    log.error("could not write image "+imgFile, e);
                }
                return null;
            }
        }

        try
        {
            return imgFile.toURI().toURL();
        }
        catch (Throwable t)
        {
            if (log.isErrorEnabled())
            {
                log.error("could not resolve URL "+imgFile, t);
            }
            return null;
        }
    }

    private class HelpDoc implements Document
    {

        private ModuleHelpPage helpPage;
        private Icon icon;

        public HelpDoc(ModuleHelpPage helpPage)
        {
            this.helpPage = helpPage;
        }
        
        public void dispose()
        {
            // no op
        }

        public JComponent getComponent()
        {
            return helpPage;
        }

        public <T> T getFeature(Class<T> featureClass)
        {
            return null;
        }

        public File getFile()
        {
            return null;
        }

        public Icon getIcon()
        {
            if (icon == null)
            {
                icon = helpPage.getIcon();
                if (icon == null)
                    icon = helpIcon;
            }
            return icon;
        }

        public Object getProperty(String name)
        {
            // TODO Auto-generated method stub
            return null;
        }

        public String getTitle()
        {
            return helpPage.getTitle();
        }

        public String getTitleExtended()
        {
            return getTitle();
        }

        public boolean isModified()
        {
            return false;
        }
        
    }
    
}
