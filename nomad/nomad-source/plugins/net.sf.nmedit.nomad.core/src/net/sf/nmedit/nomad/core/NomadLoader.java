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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;

import net.sf.nmedit.nmutils.Platform;
import net.sf.nmedit.nmutils.properties.RootSystemProperties;
import net.sf.nmedit.nmutils.properties.SystemProperties;
import net.sf.nmedit.nmutils.properties.SystemPropertyFactory;
import net.sf.nmedit.nomad.core.i18n.LocaleConfiguration;
import net.sf.nmedit.nomad.core.jpf.JPFServiceInstallerTool;
import net.sf.nmedit.nomad.core.menulayout.MenuBuilder;
import net.sf.nmedit.nomad.core.menulayout.MenuLayout;
import net.sf.nmedit.nomad.core.service.ServiceRegistry;
import net.sf.nmedit.nomad.core.service.initService.InitService;
import net.sf.nmedit.nomad.core.utils.NomadPropertyFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.java.plugin.Plugin;
import org.java.plugin.PluginLifecycleException;
import org.java.plugin.PluginManager;
import org.java.plugin.boot.Boot;
import org.java.plugin.boot.SplashHandler;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginRegistry;

import com.jgoodies.looks.Options;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.PlasticTheme;

public class NomadLoader
{

    public static final String NOMAD_FRAME_BOUNDS="nomad.frame.bounds";
    public static final String NOMAD_CURRENT_LOCALE="nomad.locale.current";
    
    // menu
    private MenuLayout menuLayout = null;
    
    private static NomadPlugin plugin;
    
    public Nomad createNomad(final NomadPlugin plugin) 
    {
        NomadLoader.plugin = plugin;
        
        // first get the boot progress callbeck
        final SplashHandler progress = Boot.getSplashHandler();
        // initializing ...
        progress.setText("Initializing Nomad...");
        progress.setProgress(0.1f);
        // now we read all property files
        // 1. nomad.properties
        final Properties nProperties = new Properties();
        getProperties(nProperties, Nomad.getCorePropertiesFile());
       
        RootSystemProperties sproperties = new RootSystemProperties(nProperties);
        SystemPropertyFactory.sharedInstance().setFactory(new NomadPropertyFactory(sproperties));
        
        // 1.2 init locale
        initLocale();
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
            throw new RuntimeException(e);
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
        String defaultLafOnPlatform = plugin.getDescriptor().getAttribute("nomad.plaf.usePlatformDefault").getValue();
        
        initLookAndFeel(lafClassName, themeClassName, defaultLafOnPlatform);
        // 1.6 initialize main window's menu
        progress.setProgress(0.3f);


  /*     NomadActionControl nomadActions = new NomadActionControl( Nomad.sharedInstance() );
        nomadActions.installActions(menuBuilder);
    */    
        progress.setProgress(0.5f);
        progress.setText("Initializing main window...");

        activatePlugins();
        
        progress.setText("Initializing services...");

        JPFServiceInstallerTool.activateAllServices(plugin);
        progress.setText("Starting Nomad...");
        
       // SwingUtilities.invokeLater(run);

        Nomad nomad = new Nomad(plugin,menuLayout);
        return nomad;
    }
    
    public static <T> Collection<T> jpfOrderByDepencies(Iterator<T> iter)
    {
        List<T> list = new ArrayList<T>();
        while (iter.hasNext())
            list.add(iter.next());
        
        if (list.size()<=1)
            return list;

        Collections.sort(list, new DependenciesComparator(PluginManager.lookup(list.get(0))));
        return list;
    }
    
    private static class DependenciesComparator implements Comparator<Object>
    {
        private PluginManager manager;
        private PluginRegistry registry;

        public DependenciesComparator(PluginManager manager)
        {
            this.manager = manager;
            this.registry = manager.getRegistry();
        }

        public int compare(Object a, Object b)
        {
            Plugin pa = manager.getPluginFor(a);
            Plugin pb = manager.getPluginFor(b);
            PluginDescriptor da = pa.getDescriptor();
            PluginDescriptor db = pb.getDescriptor();

            if (pa == pb || pa.equals(pb))
                return 0;

            if (registry.getDependingPlugins(da).contains(db))
                return -1;
            if (registry.getDependingPlugins(db).contains(da))
                return +1;
            return 0;
        }
    }
    
    public void initServices()
    {
        Collection<InitService> orderedServices =
            jpfOrderByDepencies(ServiceRegistry.getServices(InitService.class));

        final SplashHandler progress = Boot.getSplashHandler();
        List<InitService> serviceList = new ArrayList<InitService>();
        
        for (Iterator<InitService> i=orderedServices.iterator(); i.hasNext();)
        {
            serviceList.add(i.next());
        }
        
        PluginManager manager = PluginManager.lookup(this);
        
        for (int i=0;i<serviceList.size();i++)
        {
            InitService s = serviceList.get(i);

            float progressValue = 0.5f+(((float)i/(serviceList.size()-1))/2f);   
            PluginDescriptor descriptor = manager.getPluginFor(s).getDescriptor();
            String text = "init "+descriptor.getId()+" "+descriptor.getVersion();
            // does not update splash ???:
           // progress.setProgress(progressValue);
           // progress.setText(text);
            
            try
            {
                s.init();
            }
            catch (Throwable t)
            {
                t.printStackTrace();
            }
        }
        
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
    private static boolean getProperties(Properties p, File file)
    {
        if (file == null)
            return false;
        
        InputStream in;
        try
        {
            in = new BufferedInputStream(new FileInputStream(file));
            p.load(in);
            in.close();
            return true;
        } 
        catch (IOException e)
        {
            return false;
        }
    }

    private void initLookAndFeel(String lafClassName, String themeClassName, String defaultLafOnPlatform)
    {
        EnumSet<Platform.OS> defaultLafPlatforms = EnumSet.noneOf(Platform.OS.class);
        {   
            // remove whitespace + lowercase
            defaultLafOnPlatform = defaultLafOnPlatform.replaceAll("\\s", "").toLowerCase();
            // split comma separated list
            String[] dlop = defaultLafOnPlatform.split(",");
            // check items
            for (String s: dlop)
            {
                if (s.equals("all"))
                {
                    // on all platforms
                    defaultLafPlatforms.addAll(EnumSet.allOf(Platform.OS.class));
                    break;
                }
                else if (s.equals("mac"))
                {
                    defaultLafPlatforms.add(Platform.OS.MacOSFlavor);
                }
                else if (s.equals("unix"))
                {
                    defaultLafPlatforms.add(Platform.OS.UnixFlavor);
                }
                else if (s.equals("windows"))
                {
                    defaultLafPlatforms.add(Platform.OS.WindowsFlavor);
                }
            }   
        }
        

        // jgoodies specific properties
        
        PlasticLookAndFeel.setTabStyle(PlasticLookAndFeel.TAB_STYLE_METAL_VALUE);
        //UIManager.put(Options.POPUP_DROP_SHADOW_ENABLED_KEY, Boolean.FALSE);
        Options.setPopupDropShadowEnabled(false);
        Options.setUseNarrowButtons(true);
        
        //UIManager.put(Options.PLASTIC_MENU_FONT_KEY, new FontUIResource("Verdana", Font.PLAIN, 9));
        //PlasticLookAndFeel.setFontPolicy(FontPolicies.getDefaultWindowsPolicy());
/*
        UIManager.put("MenuItem.margin", new InsetsUIResource(2,2,1,2));
        UIManager.put("Menu.margin", new InsetsUIResource(1,2,1,2));
        */
        // set the metal theme
        
        if (defaultLafPlatforms.contains(Platform.flavor()))
        {
            // use default LAF on current platform

            try
            {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Throwable e) {
                Log log = LogFactory.getLog(getClass());
                log.warn("could not set look and feel theme" ,e);

            }
            
            if (Platform.isFlavor(Platform.OS.MacOSFlavor))
            {
                System.setProperty("apple.laf.useScreenMenuBar", "true");
            }
            
        } 
        else 
        {
            // use LAF setting

        	MetalTheme theme = null;
        	if (themeClassName != null)
        	{
        		try
        		{
        			theme = (MetalTheme) Class.forName(themeClassName).newInstance();
        			UIManager.put("Plastic.theme", themeClassName);

        			if (theme instanceof PlasticTheme) 
        			{
        				PlasticLookAndFeel.setPlasticTheme((PlasticTheme)theme);
        				// PlasticLookAndFeel.setTabStyle(settings.getPlasticTabStyle());
        			} 
        			else if (theme instanceof MetalTheme) 
        			{
        				MetalLookAndFeel.setCurrentTheme(theme);
        			}


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
        
    }
    
    private void initLocale()
    {
        SystemProperties properties =
            SystemPropertyFactory.getProperties(NomadLoader.class);
        
        properties.defineStringProperty(NOMAD_CURRENT_LOCALE, null);
        String locale = properties.stringValue(NOMAD_CURRENT_LOCALE);
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
    
}


