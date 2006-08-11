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
 * Created on Apr 1, 2006
 */
package net.sf.nmedit.nomad.core.nomad;

import java.io.FileNotFoundException;
import java.util.regex.Pattern;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DGroup;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DModule;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.ModuleDescriptions;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.substitution.Substitutions;
import net.sf.nmedit.nomad.core.application.Application;
import net.sf.nmedit.nomad.core.application.ApplicationInstantiationException;
import net.sf.nmedit.nomad.core.application.Logger;
import net.sf.nmedit.nomad.core.application.ProgressMeter;
import net.sf.nmedit.nomad.main.designer.Designer;
import net.sf.nmedit.nomad.theme.ModuleBuilder;
import net.sf.nmedit.nomad.theme.UIFactory;
import net.sf.nmedit.nomad.theme.plugin.ThemePluginManager;
import net.sf.nmedit.nomad.util.graphics.ImageTracker;


public abstract class NomadEnvironment extends Application
{
    public final static String KEY_CUSTOM_PROPERTIES = "custom-property-file";

    private ImageTracker       imageTracker;

    private UIFactory          factory;/*

    private ModuleToolbar      moduleToolbar;*/

    private ModuleBuilder      builder;

    private boolean            cachingEnabled;

    private static NomadEnvironment sharedEnvironment = null;

    public static NomadEnvironment sharedInstance()
    {
        return sharedEnvironment;
    }
    
    public NomadEnvironment( String[] args )
    {
        super( args );
        sharedEnvironment = this;
        
        setProperty( KEY_CUSTOM_PROPERTIES, "conf/properties.xml" );
        this.imageTracker = null;
        this.factory = null;/*
        this.moduleToolbar = null;*/
        this.builder = null;
        this.cachingEnabled =  false;
    }

    protected void startInternal( ProgressMeter progress )
            throws ApplicationInstantiationException
    {
        try
        {
            putPropertiesFromXML( getProperty( KEY_CUSTOM_PROPERTIES ) );
        }
        catch (Exception e)
        {
            Logger l = Application.getLog();
            l.println(e.getMessage());
            l.println("Application seems to be started the first time: using default properties.");
        }

        progress.increment( "Initializing" );
        
        
        Designer.init();

        progress.increment( "Images" );
        loadDefaultImageTracker();

        progress.increment( "Modules" );
        loadModuleDefinitions();

        progress.increment( "Module builder" );
        loadDefaultBuilder();

        progress.increment( "Themes" );
        loadDefaultFactory();
    }
    
    protected void stopInternal()
    {
        try
        {
            storeProperties( getProperty( KEY_CUSTOM_PROPERTIES ) );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public boolean isCachingEnabled()
    {
        return cachingEnabled;
    }

    public void setCachingEnabled( boolean enable )
    {
        cachingEnabled = false;
    }

    public ImageTracker getImageTracker()
    {
        return imageTracker;
    }

    public ModuleBuilder getBuilder()
    {
        return builder;
    }

    public UIFactory getFactory()
    {
        return factory;
    }
/*
    public ModuleToolbar getToolbar()
    {
        return moduleToolbar;
    }*/

    public void loadModuleDefinitions()
    {
        Substitutions subs = new Substitutions();
        subs.load(subs.getClass().getResourceAsStream("/RESOURCE/xml/substitutions.xml"));
        
        ModuleDescriptions.init(subs);
        ModuleDescriptions md = ModuleDescriptions.sharedInstance();
        md.load(md.getClass().getResourceAsStream("/RESOURCE/xml/modules.xml"));

        loadImages( ModuleDescriptions.sharedInstance(), imageTracker );
    }

    /**
     * Loads the module icons
     * @param imageTracker the image source
     */
    public void loadImages(ModuleDescriptions md, ImageTracker imageTracker) {
        //DConnector.setImageTracker(imageTracker);

        for (DModule m : md.getModuleList())
        {
            m.setIcon(imageTracker.getImage(Integer.toString(m.getModuleID())));
        }

        // TODO better solution for new icons
        
        // load new icons
        Pattern p = Pattern.compile(".*-id-\\d*");
        
        for (String key : imageTracker.getKeys())
        {
            String ID = key;
            if (p.matcher(ID).matches())
            {
                ID = ID.substring(ID.lastIndexOf('-')+1);
                
                try
                {
                    DModule module = md.getModuleById(Integer.parseInt(ID));
                    module.setIcon(imageTracker.getImage(key));
                }
                catch (NumberFormatException e)
                { /* ignore fail */ }
                
            }
        }

    }

    public void loadDefaultImageTracker()
    {
        imageTracker = new ImageTracker();
        try
        {
            imageTracker.loadFromDirectory( "data/images/slice/" );
            imageTracker.loadFromDirectory( "data/images/single/" );
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        //DConnector.setImageTracker( imageTracker );
    }

    public void setFactory( UIFactory factory )
    {
        this.factory = factory;
        factory.getImageTracker().addFrom( imageTracker );
        builder.setUIFactory( factory );
    }

    public void loadDefaultFactory()
    {
        setFactory( ThemePluginManager.getDefaultUIFactory() );
    }

    public void loadDefaultBuilder()
    {
        builder = new ModuleBuilder( this );
    }


}
