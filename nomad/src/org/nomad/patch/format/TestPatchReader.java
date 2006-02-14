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
 * Created on Feb 12, 2006
 */
package org.nomad.patch.format;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;


public class TestPatchReader {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 * @throws PatchFileException 
	 */
	public static void main(String[] args) throws FileNotFoundException, PatchFileException {
		String dir = "/home/christian/CVS-Arbeitsbereich/nmedit/nomad/src/data/patches/";
		String patch = dir+"pwbass.pch";
		
		test(patch);
		
	}

	private static void test(String patch) throws FileNotFoundException, PatchFileException {
		
		BufferedReader in = new BufferedReader(new FileReader(patch));

		PatchFileCallback303.Adapter cons = new PatchFileCallback303.Adapter();
		
		PatchFile303 reader = new PatchFile303(in, cons);
		
		reader.readAll();
		
		System.out.println("Version: "+cons.getVersionName());
		System.out.println("Notes: "+cons.getNotes());
		
		
		
	}

}
