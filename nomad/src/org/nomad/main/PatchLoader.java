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
package org.nomad.main;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.nomad.dialog.JTaskDialog;
import org.nomad.dialog.TaskModel;
import org.nomad.patch.Patch;

class PatchLoader {

	private final Nomad nomad;
	private ArrayList<PatchFile> fileList = new ArrayList<PatchFile>();
	
	public PatchLoader(Nomad nomad) {
		this.nomad = nomad; }
	public void loadPatch(String file) {
		loadPatch(new String[]{file});
	}

	public void loadPatch(File[] files) {
		String[] sfiles = new String[files.length];
		for (int i=files.length-1;i>=0;i--)
			sfiles[i]=files[i].getAbsolutePath();
		
		loadPatch(sfiles);
	}

	private class PatchFile {
		String fileName;
		String patchName;
		public PatchFile(String fileName) {
			this.fileName = fileName;
			patchName = fileName.substring(0,fileName.lastIndexOf(".pch"));
			patchName = patchName.substring(patchName.lastIndexOf('/')+1);
		}
	}
	
	public void loadPatch(final String[] files) {
		for (String fileName : files) {
			fileList.add(new PatchFile(fileName));
		}

		JTaskDialog.processTasks(this.nomad, new TaskModel(){
			
			int taskCount = fileList.size();
			int selection = nomad.viewManager.getDocumentCount();
			
			public String getDescription() {
				return "Loading Nord Modular patch...";
			}

			public int getTaskCount() {
				return taskCount;
			}

			public String getTaskName(int taskIndex) {
				return fileList.get(taskIndex).patchName;
			}

			public void run(int taskIndex) {
				final PatchFile task = fileList.get(taskIndex);
				Patch patch = new Patch();
				final JPanel tab = Patch.createPatch(task.fileName, patch);

				try {
					/**
					 * Because Swing's not thread save.
					 */
					SwingUtilities.invokeAndWait(
						new Runnable() {
							public void run() {
								nomad.viewManager.addDocument(task.patchName, tab);
							}
						}
					);
				} catch (Throwable t) {
					// t.printStackTrace();
					System.err.println("Problem while loading patch: "+t);
				}

				if (taskIndex==getTaskCount()-1) {
					nomad.viewManager.setSelectedDocument(selection);
					fileList.clear();
				}
			}
			
		});
		
	}
		
}