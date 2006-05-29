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

import net.sf.nmedit.nomad.core.application.Application;
import net.sf.nmedit.nomad.core.application.ApplicationInstantiationException;
import net.sf.nmedit.nomad.core.application.ProgressMeter;
import net.sf.nmedit.nomad.main.designer.Designer;
import net.sf.nmedit.nomad.main.ui.ModuleToolbar;
import net.sf.nmedit.nomad.plugin.PluginManager;
import net.sf.nmedit.nomad.theme.ModuleBuilder;
import net.sf.nmedit.nomad.theme.UIFactory;
import net.sf.nmedit.nomad.util.graphics.ImageTracker;
import net.sf.nmedit.nomad.xml.dom.module.DConnector;
import net.sf.nmedit.nomad.xml.dom.module.ModuleDescriptions;
import net.sf.nmedit.nomad.xml.dom.substitution.Substitutions;


public abstract class NomadEnvironment extends Application
{
    public final static String KEY_CUSTOM_PROPERTIES = "custom-property-file";

    private ImageTracker       imageTracker;

    private UIFactory          factory;

    private ModuleToolbar      moduleToolbar;

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
        this.factory = null;
        this.moduleToolbar = null;
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
            System.err.println( "Failed loading custom properties, defaults will be used: " + e );
        }

        progress.increment( "loading media: images" );
        loadDefaultImageTracker();

        progress.increment( "loading: module definitions" );
        loadModuleDefinitions();

        progress.increment( "loading: plugin manager" );
        loadPluginManager();

        progress.increment( "loading: ui builder" );
        loadDefaultBuilder();

        progress.increment( "loading: plugin factory" );
        loadDefaultFactory();

        progress.increment( "loading: initialising designer" );
        Designer.init();
        
        progress.increment( "loading: module toolbar" );
        loadModuleToolbar();
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

    public ModuleToolbar getToolbar()
    {
        return moduleToolbar;
    }

    public void loadModuleDefinitions()
    {
        Substitutions subs = new Substitutions( "data/xml/substitutions.xml" );
        ModuleDescriptions.init( "data/xml/modules.xml", subs );
        ModuleDescriptions.sharedInstance().loadImages( imageTracker );
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

        DConnector.setImageTracker( imageTracker );
    }

    public void setFactory( UIFactory factory )
    {
        this.factory = factory;
        factory.getImageTracker().addFrom( imageTracker );
        builder.setUIFactory( factory );
    }

    public void loadDefaultFactory()
    {
        setFactory( PluginManager.getDefaultUIFactory() );
    }

    public void loadDefaultBuilder()
    {
        builder = new ModuleBuilder( this );
    }

    public void loadPluginManager()
    {
        PluginManager.init();
    }

    public void loadModuleToolbar()
    {
        moduleToolbar = new ModuleToolbar();
    }

}
