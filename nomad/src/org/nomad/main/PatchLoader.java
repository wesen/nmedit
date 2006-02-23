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
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import org.nomad.dialog.NomadTaskDialog;
import org.nomad.dialog.TaskModel;
import org.nomad.patch.format.PatchConstructionException;
import org.nomad.patch.format.PatchBuilder;
import org.nomad.patch.format.PatchFile303;
import org.nomad.patch.ui.PatchUI;

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

		NomadTaskDialog dlg = new NomadTaskDialog(new TaskModel(){
			
			int taskCount = fileList.size();
			int selection = nomad.getDocumentManager().getDocumentCount();
			
			public String getDescription() {
				return "Loading Nord Modular patch...";
			}

			public int getTaskCount() {
				return taskCount;
			}

			public String getTaskName(int taskIndex) {
				return fileList.get(taskIndex).patchName;
			}

			public void run(int taskIndex) throws Throwable {
				final PatchFile task = fileList.get(taskIndex);
				try {
					PatchBuilder cons = new PatchBuilder();
					PatchFile303 reader = new PatchFile303(task.fileName, cons);
					reader.readAll();
					
					final PatchUI tab = PatchUI.newInstance(cons.getPatch());
					
					/**
					 * Because Swing's not thread save.
					 */
					SwingUtilities.invokeAndWait(
						new Runnable() {
							public void run() {
								nomad.getDocumentManager().addDocument(task.patchName, tab);
							}
						}
					);
				} catch (FileNotFoundException e) {
					throw e;
				} catch (PatchConstructionException e) {
					throw e;
				} catch (Throwable t) {
					System.out.println("Error while loading patch.");
					t.printStackTrace();
				}

				if (taskIndex==getTaskCount()-1) {
					if (selection>=0 && selection<nomad.getDocumentManager().getDocumentCount())
						nomad.getDocumentManager().setSelectedDocument(selection);
					fileList.clear();
				}
			}
			
		});
		
		dlg.invoke();
		
	}
		
}