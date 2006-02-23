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
 * Created on Feb 22, 2006
 */
package org.nomad.main;

import java.awt.Cursor;

import org.nomad.dialog.NomadTaskDialog;
import org.nomad.dialog.TaskModel;
import org.nomad.env.Environment;
import org.nomad.patch.ui.PatchUI;
import org.nomad.plugin.NomadPlugin;
import org.nomad.theme.UIFactory;
import org.nomad.util.view.DocumentManager;

public class ThemePluginSwitcher implements TaskModel {

	private Nomad nomad;
	private NomadPlugin plugin;
	private Environment env;
	private DocumentManager documents;
	private int selectionIndex;
	private int documentCount;
	
	public ThemePluginSwitcher(Nomad nomad, NomadPlugin plugin) {
		this.nomad = nomad;
		this.plugin = plugin;
		env = Environment.sharedInstance();
		documents = nomad.getDocumentManager();
		selectionIndex = documents.getSelectedDocumentIndex();
		documentCount = documents.getDocumentCount();
	}
	
	public Nomad getNomad() {
		return nomad;
	}

	public NomadPlugin getPlugin() {
		return plugin;
	}
	
	public DocumentManager getDocumentManager() {
		return documents;
	}
	
	public void switchPlugin() {
		if (!nomad.isDisplayable()) {
			env.setFactory((UIFactory) plugin.getFactoryInstance());
		} else {
			NomadTaskDialog dlg = new NomadTaskDialog(this);
			dlg.invoke();
		}
	}

	public String getDescription() {
		return "Switching theme plugin";
	}

	public int getTaskCount() {
		return documentCount+3;
	}

	public String getTaskName(int taskIndex) {
		switch (taskIndex) {
			case 0: return "Preparing...";
			case 1: return "Loading factory...";
			default: {
				if (taskIndex-2>=documentCount) return "Finishing...";
				else return "Patch:" +documents.getTitleAt(0);
			}
		}
	}

	public void run(int taskIndex) {
		switch (taskIndex) {
			case 0: init(); break;
			case 1: init2(); break;
			default: {
				if (taskIndex==documentCount+2) finish();
				else migrate(taskIndex-2);
			}
		}
	}

	public void init() {
		nomad.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		env.getToolbar().setEnabled(false);
	}
	
	public void init2() {
		env.setFactory((UIFactory) plugin.getFactoryInstance());
	}
	
	public void finish() {
		if (selectionIndex>=0)
			documents.setSelectedDocument(selectionIndex);

		nomad.setCursor(Cursor.getDefaultCursor());
		env.getToolbar().setEnabled(true);
		documents.getDocumentContainer().validate();
		documents.getDocumentContainer().repaint();
	}
	
	public void migrate(int document) {
		((PatchUI) documents.getDocumentAt(document)).rebuild();
	}
}
