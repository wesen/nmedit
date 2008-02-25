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


import javax.swing.SwingUtilities;

import org.java.plugin.PluginManager;
import org.java.plugin.boot.Application;
import org.java.plugin.boot.ApplicationPlugin;
import org.java.plugin.util.ExtendedProperties;

public class NomadPlugin extends ApplicationPlugin implements Application
{
    /**
     * This plug-in ID.
     */
    public static final String PLUGIN_ID = "net.sf.nmedit.nomad.core";
    
    private Nomad nomad = null;
    
    public NomadPlugin()
    {
        super();
    }
    
    @Override
    protected Application initApplication( ExtendedProperties config,
            String[] args ) throws Exception
    {
        return this;
    }

    @Override
    protected void doStart() throws Exception
    {
        JPFUtil.setPluginManager(PluginManager.lookup(this));

        
    }

    @Override
    protected void doStop() throws Exception
    {
        // nothing to do
    }

    public void startApplication() throws Exception
    {
        final NomadLoader loader = new NomadLoader();
        nomad = loader.createNomad(NomadPlugin.this);
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                // invoke on event dispatch thread
                nomad.setupUI();
                loader.initServices();
                nomad.setupMenu(); // after service so they can install custom menu items
                nomad.getWindow().setVisible(true);
            }
        });
        
    }
}
