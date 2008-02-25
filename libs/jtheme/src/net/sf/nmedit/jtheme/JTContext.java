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
 * Created on Jan 21, 2007
 */
package net.sf.nmedit.jtheme;

import java.awt.Image;

import javax.swing.JComponent;
import javax.swing.UIDefaults;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.nmedit.jtheme.cable.CableRenderer;
import net.sf.nmedit.jtheme.cable.JTCableManagerImpl;
import net.sf.nmedit.jtheme.cable.ShadowCableRenderer;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTKnob;
import net.sf.nmedit.jtheme.component.JTLabel;
import net.sf.nmedit.jtheme.component.JTLight;
import net.sf.nmedit.jtheme.component.JTModuleContainer;
import net.sf.nmedit.jtheme.component.JTSlider;
import net.sf.nmedit.jtheme.component.plaf.mcui.JTModuleContainerUI;
import net.sf.nmedit.jtheme.store.StorageContext;

public abstract class JTContext
{
    
    public static final String UIDefaultsClassLoaderKey = "ClassLoader";
    
    private final Object lock = new Object();
    
    public final Object getLock()
    {
        return lock;
    }
    
    private static transient Log _log;
    
    protected static Log getLogger()
    {
        if (_log == null)
            _log = LogFactory.getLog(JTContext.class);
        return _log;
    }
    
    public abstract UIDefaults getUIDefaults();
    public abstract Image getImage(String key);
    public abstract boolean hasModuleContainerOverlay();
    
    public abstract Class[] getComponentClasses();

    public abstract StorageContext getStorageContext();

    public static final String TYPE_KNOB = "knob";
    public static final String TYPE_SLIDER = "slider";
    public static final String TYPE_LABEL = "label";
    public static final String TYPE_IMAGE = "image";
    public static final String TYPE_MODULE = "module";
    public static final String TYPE_CONNECTOR = "connector";
    public static final String TYPE_BUTTONS = "buttons";
    public static final String TYPE_TEXTDISPLAY = "textDisplay";
    public static final String TYPE_LIGHT = "light";
    
    // TODO does not belong here - nm1 specific
    public static final String TYPE_RESET_BUTTON = "resetButton";
    
    public JTPopupHandler getPopupHandler(JTComponent component)
    {
        return null;
    }
    
    public JTKnob createKnob() throws JTException
    {
        return (JTKnob) createComponent(TYPE_KNOB);
    }
    
    public JTLight createLight() throws JTException
    {
        return (JTLight) createComponent(TYPE_LIGHT);
    }
    
    public JTLabel createLabel() throws JTException
    {
        return (JTLabel) createComponent(TYPE_LABEL);
    }
    
    public JTSlider createSlider() throws JTException
    {
        return (JTSlider) createComponent(TYPE_SLIDER);
    }

    public abstract Class<? extends JTComponent> getComponentType(String type);
    
    public abstract JTComponent createComponent(String type) throws JTException;
    
    public static boolean hasUIClass(JComponent component, UIDefaults defaults)
    {
        String uiClassID = component.getUIClassID();
        if (uiClassID == null)
            return false;
        
        Object cl = defaults.get(UIDefaultsClassLoaderKey);
        ClassLoader uiClassLoader =  (cl != null) ? (ClassLoader)cl : component.getClass().getClassLoader();
        
        Class uiClass = defaults.getUIClass(uiClassID, uiClassLoader);
        return uiClass != null;
    }
    
    /* public static boolean hasUIClass(JComponent component, UIDefaults defaults)
    {
        return component.getUIClassID() != null;
    }*/
    
    public void setUIDefaultsClassLoader(ClassLoader loader)
    {
        Log log = getLogger();
        if (log.isInfoEnabled())
        {
            Class<?> loaderClass = loader == null ? null : loader.getClass();
            
            log.info("Setting UIDefaults ClassLoader in "+this+": "+loader+" ("+loaderClass+")");
        }
        
        getUIDefaults().put(UIDefaultsClassLoaderKey, loader);
    }
    
    public ClassLoader getUIDefaultsClassLoader()
    {
        Object loader = getUIDefaults().get(UIDefaultsClassLoaderKey);
        if (loader == null || !(loader instanceof ClassLoader))
            return null;
        else
            return (ClassLoader) loader;
    }
    
    public <T extends JTComponent> T createComponentInstance(Class<T> clazz) throws JTException
    {
        T component;
        
        try
        {
            component = clazz
            .getConstructor(new Class<?>[] {JTContext.class})
            .newInstance(new Object[] {this});
            
            UIDefaults defaults = getUIDefaults();
            if (hasUIClass(component, defaults))
            {
                component.setUI(defaults.getUI(component));
            }
        }
        catch (Throwable t)
        {
            JTException e = new JTException("could not create instance of "+clazz);
            e.initCause(t);
            throw e;
        }
        
        return component;
    }
    
    
    protected CableRenderer createDefaultCableRenderer()
    {
        ShadowCableRenderer renderer = new ShadowCableRenderer();
        renderer.setAntialiasingEnabled(true);
        renderer.setShadowsEnabled(true);
        return renderer;
    }
    
    private transient CableRenderer cachedCableRenderer;
    
    protected CableRenderer getCableRenderer()
    {
        if (cachedCableRenderer == null)
            cachedCableRenderer = createDefaultCableRenderer();
        return cachedCableRenderer;
    }
    
    public JTModuleContainer createModuleContainer() throws JTException
    {
        JTModuleContainer moduleContainer = new JTModuleContainer(this, 
                new JTCableManagerImpl(getCableRenderer()));
        installUI(moduleContainer);
        return moduleContainer;
    }
    
    private void installUI(JTModuleContainer moduleContainer)
    {
        UIDefaults defaults = getUIDefaults();
        //if (hasUIClass(moduleContainer, defaults))
        {
            moduleContainer.setUI((JTModuleContainerUI)defaults.getUI(moduleContainer));
        }
    }

    public boolean isDnDAllowed()
    {
        return false;
    }

}
