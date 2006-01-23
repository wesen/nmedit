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
 * Created on Jan 23, 2006
 */
package org.nomad.env;

import java.io.FileNotFoundException;

import org.nomad.main.ModuleToolbar;
import org.nomad.main.run.Run;
import org.nomad.plugin.PluginManager;
import org.nomad.theme.ModuleGUIBuilder;
import org.nomad.theme.UIFactory;
import org.nomad.util.graphics.ImageTracker;
import org.nomad.xml.dom.module.DConnector;
import org.nomad.xml.dom.module.ModuleDescriptions;
import org.nomad.xml.dom.substitution.Substitutions;

public class Environment {
	
	private final static Environment env = new Environment();
	
	public static Environment sharedInstance() {
		return env;
	}
	
	protected Environment() {
		super();
	}
	
	private ImageTracker imageTracker = null;
	private UIFactory factory = null;
	private ModuleToolbar moduleToolbar = null;
	private ModuleGUIBuilder builder = null;

	public ImageTracker getImageTracker(){
		return imageTracker;
	}
	
	public ModuleGUIBuilder getBuilder() {
		return builder;
	}
	
	public UIFactory getFactory() {
		return factory;
	}
	
	public ModuleToolbar getToolbar() {
		return moduleToolbar;
	}
	
	public void loadModuleDefinitions() {
		Substitutions subs = new Substitutions("data/xml/substitutions.xml");
		ModuleDescriptions.init("data/xml/modules.xml", subs);
		ModuleDescriptions.sharedInstance().loadImages(imageTracker);
	}
	
	public void loadDefaultImageTracker() {
		imageTracker = new ImageTracker();
		try {
			imageTracker.loadFromDirectory("data/images/slice/");
			imageTracker.loadFromDirectory("data/images/single/");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		DConnector.setImageTracker(imageTracker);
	}
	
	public void setFactory(UIFactory factory) {
		this.factory = factory;
		factory.getImageTracker().addFrom(imageTracker);
		builder.setUIFactory(factory);
	}
	
	public void loadDefaultFactory() {
		setFactory(PluginManager.getDefaultUIFactory());
	}
	
	public void loadDefaultBuilder() {
		builder = new ModuleGUIBuilder(this);
	}
	
	public void loadPluginManager() {
		PluginManager.init();
	}
	
	public void loadModuleToolbar() {
		moduleToolbar = new ModuleToolbar();
	}
	
	public void loadAll() {
		Run.statusMessage("images");
		loadDefaultImageTracker();

		Run.statusMessage("module definitions");
		loadModuleDefinitions();

		Run.statusMessage("plugins");
		loadPluginManager();

		Run.statusMessage("ui builder");
		loadDefaultBuilder();

		Run.statusMessage("ui plugin");
		loadDefaultFactory();

		Run.statusMessage("module toolbar");
		loadModuleToolbar();
	}

}
