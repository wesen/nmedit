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
 * Created on Nov 22, 2006
 */
package net.sf.nmedit.nomad.core;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;

import net.sf.nmedit.nomad.core.i18n.LocaleConfiguration;
import net.sf.nmedit.nomad.core.jpf.JPFServiceInstallerTool;
import net.sf.nmedit.nomad.core.menulayout.MenuBuilder;
import net.sf.nmedit.nomad.core.menulayout.MenuLayout;
import net.sf.nmedit.nomad.core.misc.NMUtilities;
import net.sf.nmedit.nomad.core.service.ServiceRegistry;
import net.sf.nmedit.nomad.core.service.initService.InitService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.java.plugin.Plugin;
import org.java.plugin.PluginLifecycleException;
import org.java.plugin.PluginManager;
import org.java.plugin.boot.Boot;
import org.java.plugin.boot.SplashHandler;
import org.java.plugin.registry.PluginDescriptor;

import com.jgoodies.looks.FontPolicies;
import com.jgoodies.looks.Options;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;

public class NomadLoader
{

    public static final String NOMAD_PROPERTIES="nomad.properties";
    public static final String NOMAD_FRAME_BOUNDS="nomad.frame.bounds";
    public static final String NOMAD_CURRENT_LOCALE="nomad.locale.current";
    
    // menu
    private MenuLayout menuLayout = null;
    
    private static NomadPlugin plugin;
    
    public Nomad createNomad(final NomadPlugin plugin) throws Exception
    {
        NomadLoader.plugin = plugin;
        
        // first get the boot progress callbeck
        final SplashHandler progress = Boot.getSplashHandler();
        // initializing ...
        progress.setText("Initializing Nomad...");
        progress.setProgress(0.1f);
        // now we read all property files
        // 1. nomad.properties
        final Properties nomadProperties = new Properties();
        getProperties(nomadProperties, NOMAD_PROPERTIES);
        // 1.2 init locale
        initLocale(nomadProperties);
        // 1.4 menu layout configuration
        InputStream mlIn = null;
        try
        {
            ClassLoader loader = getClass().getClassLoader();
            mlIn = loader.getResourceAsStream("./MenuLayout.xml");
            
            menuLayout = MenuLayout.getLayout(new BufferedInputStream(mlIn));
        }
        catch (Exception e)
        {
            Log log = LogFactory.getLog(getClass());
            if (log.isFatalEnabled())
            {
                log.fatal("could not read MenuLayout", e);
            }
            throw e;
        }
        finally
        {
            try
            {
                if (mlIn != null)
                    mlIn.close();
            }
            catch (IOException e)
            {
                Log log = LogFactory.getLog(NomadLoader.class);
                if (log.isWarnEnabled())
                {
                    log.warn("closing menu layout", e);
                }
            }
        }
        // increase progress
        progress.setProgress(0.2f);
        // 1.5 initialize look and feel (important: before creating any swing components)
        
        String lafClassName = plugin.getDescriptor().getAttribute("javax.swing.LookAndFeel").getValue();
        String themeClassName = plugin.getDescriptor().getAttribute("javax.swing.plaf.metal.MetalTheme").getValue();
        
        initLookAndFeel(lafClassName, themeClassName);
        // 1.6 initialize main window's menu
        progress.setProgress(0.3f);
        

  /*     NomadActionControl nomadActions = new NomadActionControl( Nomad.sharedInstance() );
        nomadActions.installActions(menuBuilder);
    */    
        progress.setProgress(0.8f);
        progress.setText("Initializing main window...");

        activatePlugins();
        
        //Runnable run = new Runnable(){public void run(){
        
        JPFServiceInstallerTool.activateAllServices(plugin);
        
        for (Iterator<InitService> i=ServiceRegistry.getServices(InitService.class); i.hasNext();)
        {
            i.next().initService();
        }
        
        //}};

       // SwingUtilities.invokeLater(run);

        Nomad nomad = new Nomad(plugin,menuLayout);
        getPreferredWindowBounds(nomad.getWindow(), nomadProperties);
        return nomad;
    }
    
    
    private void activatePlugins()
    {
        PluginManager manager = plugin.getManager();
        
        Collection<PluginDescriptor> descriptors = 
            manager.getRegistry().getPluginDescriptors();

        SplashHandler splash = Boot.getSplashHandler();
        
        int pos = 1;
        for (PluginDescriptor pd: descriptors)
        {
            splash.setProgress( pos/(float)descriptors.size() );
            splash.setText(pd.getId());
            
            if (!(manager.isPluginActivated(pd) || manager.isBadPlugin(pd) ))
            {
                try
                {
                    manager.activatePlugin(pd.getId());
                }
                catch (PluginLifecycleException e)
                {
                    e.printStackTrace();
                }
            }
            pos ++;
        }
    }
    /*  
    private void loadPlugins()
    {        
        ObjectFactory jpfFactory = ObjectFactory.newInstance();
        PluginManager manager = jpfFactory.createManager();
        
        manager.publishPlugins(new PluginManager.PluginLocation[]{
                new PluginManager.PluginLocation.        });
    }
*/
    private static boolean getProperties(Properties p, String location)
    {
        InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(location);
        
        boolean success = false;
        if (in != null)
        {
            in = new BufferedInputStream(in);
            try
            {
                p.load(in);
                success = true;
                in.close();
            }
            catch (IOException e)
            {
                // ignore
            }
        }
        return success;
    }

    private static boolean writeProperties(Properties p, String location, String comments)
    {
        boolean success = false;
        if (location != null)
        {
            try
            {
                OutputStream out = new BufferedOutputStream(new FileOutputStream(location));
                try
                {
                    p.store(out, comments);
                    success = true;
                    out.flush();
                    out.close();
                }
                catch (IOException e)
                {
                    // ignore
                }
            }
            catch (FileNotFoundException e)
            {
                // ignore
            }
        }
        return success;
    }
    
    private void getPreferredWindowBounds(JFrame target, Properties properties)
    {
        Rectangle b = NMUtilities.getRectangleProperty(properties, NOMAD_FRAME_BOUNDS, null);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        if (b == null)
        {
            b = new Rectangle(0, 0, 640, 480);
            NMUtilities.fitRectangle(b, screen);
            NMUtilities.centerRectangle(b, screen);
        }
        else
        {
            
        }
     
        target.setBounds(b);
    }

    private void initLookAndFeel(String lafClassName, String themeClassName)
    {

        // jgoodies specific properties
        
        PlasticLookAndFeel.setTabStyle(PlasticLookAndFeel.TAB_STYLE_METAL_VALUE);
        UIManager.put(Options.POPUP_DROP_SHADOW_ENABLED_KEY, Boolean.FALSE);
        
        //PlasticLookAndFeel.setFontPolicy(FontPolicies.getDefaultWindowsPolicy());

        UIManager.put("MenuItem.margin", new InsetsUIResource(2,2,1,2));
        UIManager.put("Menu.margin", new InsetsUIResource(1,2,1,2));
        
        // set the metal theme
        MetalTheme theme = null;
        if (themeClassName != null)
        {
            try
            {
                theme = (MetalTheme) Class.forName(themeClassName).newInstance();
                MetalLookAndFeel.setCurrentTheme(theme);
            }
            catch (Throwable e)
            {
                Log log = LogFactory.getLog(getClass());
                log.warn("could not set look and feel theme" ,e);
            }
        }
        // set the look and feel
        if (lafClassName != null)
        {
            try
            {
                LookAndFeel LAF = (LookAndFeel)Class.forName(lafClassName).newInstance();
                // it is very important to set the classloader  
                UIManager.getDefaults().put("ClassLoader",getClass().getClassLoader());
                UIManager.setLookAndFeel(LAF);
            }
            catch (Throwable e)
            {
                Log log = LogFactory.getLog(getClass());
                log.warn("could not set custom look and feel" ,e);
            }
        }    

    }
    
    private void initLocale(Properties properties)
    {
        String locale = properties.getProperty(NOMAD_CURRENT_LOCALE);
        if (locale != null)
        {
            for (Locale l:Locale.getAvailableLocales())
            {
                if (locale.equals(l.toString()))
                {
                    LocaleConfiguration conf =
                    LocaleConfiguration.getLocaleConfiguration();
                    if (!conf.getCurrentLocale().equals(l))
                        conf.setCurrentLocale(l);
                    
                    break ;
                }
            }
        }
    }

    static ResourceBundle getResourceBundle()
    {
        LocaleConfiguration lc = LocaleConfiguration.getLocaleConfiguration();
        return ResourceBundle.getBundle("i18n/MessageBundle", lc.getCurrentLocale(),
                getPluginClassLoader());
    }
    
    private static ClassLoader pluginClassLoader;

    protected static ClassLoader getPluginClassLoader()
    {
        if (pluginClassLoader == null)
        {
            pluginClassLoader = getPluginClassLoader(plugin);
        }
        return pluginClassLoader;
    }

    public static ClassLoader getPluginClassLoader(Plugin plugin)
    {
        return
            plugin.getManager().getPluginClassLoader(plugin.getDescriptor());
    }
    
    public static class LocaleHandler implements PropertyChangeListener
    {
        private MenuBuilder menuBuilder;

        public LocaleHandler( MenuBuilder menuBuilder )
        {
            this.menuBuilder = menuBuilder;
        }

        public void propertyChange( PropertyChangeEvent evt )
        {
            if (LocaleConfiguration.LOCALE_PROPERTY==evt.getPropertyName())
            {
                menuBuilder.setResourceBundle(NomadLoader.getResourceBundle());
            }
        }
    }
    
    private static void storeNomadProperties()
    {/*
        Nomad nomad = Nomad.sharedInstance();
        Properties target = nomad.getProperties();
        // update window bounds property
        target.put(NOMAD_FRAME_BOUNDS, NMUtilities.toString(nomad.getWindow().getBounds()));
        // update locale property
        LocaleConfiguration conf = LocaleConfiguration.getLocaleConfiguration();
        Locale current = conf.getCurrentLocale();
        Locale system = conf.getVMDefaultLocale();
        if (!system.equals(current))
        {
            target.setProperty(NOMAD_CURRENT_LOCALE, current.toString());
        }
        else
        {
            target.remove(NOMAD_CURRENT_LOCALE);
        }
        
        writeProperties(target, NOMAD_PROPERTIES, 
                "nomad.properties - runtime configuration\n please do not edit this file");
                */
    }
    
}


