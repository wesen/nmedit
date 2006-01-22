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
 * Created on Jan 13, 2006
 */
package plugin.classictheme;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

public class RefactorXML {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		(new RefactorXML()).refactor();
	}

	private void refactor() throws IOException {
		String path_prefix = "plugin/classictheme";
		String fileIn = path_prefix+"/old-ui.xml";
		String fileOut = path_prefix+"/ui.xml";
		
		
		BufferedReader in = new BufferedReader(new FileReader(fileIn));
		PrintStream out = new PrintStream(new FileOutputStream(fileOut));
		
		String line = "";
		
		int moduleId = 0;
		
		while ((line=in.readLine())!=null) {
			
			if (line.contains("<module")) {
				int index = line.indexOf("\"");;
				String tmp= line.substring(index+1);
				tmp = tmp.substring(0, tmp.indexOf("\""));
				moduleId = Integer.parseInt(tmp);
			}
			
			if (line.matches("\\s*<\\s*control\\s*class=.*")) {
			//	isButtonArray = line.contains("ButtonGroupUI");
				
				line = line.replaceFirst("class", "name");
				line = line.replaceFirst("control", "component");
			
				line = line.replaceFirst("nomad.gui.model.component.builtin.DefaultConnectorUI", "connector");

				line = line.replaceFirst("nomad.gui.model.component.builtin.ButtonGroupUI", "button");

				line = line.replaceFirst("nomad.gui.model.component.builtin.DefaultControlUI", "knob");
				line = line.replaceFirst("nomad.gui.model.component.builtin.DefaultTextDisplay", "display.text");
				line = line.replaceFirst("nomad.gui.model.component.builtin.DefaultLabelUI", "label");

				line = line.replaceFirst("nomad.gui.model.component.builtin.SliderArrayUI", "slider");
				line = line.replaceFirst("nomad.gui.model.component.builtin.VocoderUI", "display.vocoder");
				
				
				
				if (line.contains("nomad.gui"))
					System.out.println("not done:: "+line);
				
			} else if (line.matches("\\s*</\\s*control.*")) {
				line = line.replaceFirst("control", "component");
			} else if (line.matches("\\s*<\\s*property\\s*id=.*")) {
				line = line.replaceFirst("id", "name");
				line = line.replaceFirst("location", "x,y"); 
			}
			
			if (line.matches("\\s*<\\s*property\\s*name=\"label.text\".*")) {
				line = line.replaceFirst("label.text", "text");
			}
			
			if (line.matches("\\s*<\\s*property\\s*name=\"connector\".*")) {
				
				String tmp = line.substring(0,line.lastIndexOf("\""));
				tmp = tmp.substring(tmp.lastIndexOf("\"")+1);
				
				String[] pieces = tmp.split("\\.");
				
				boolean isOutput = tmp.contains("output");
				
				int connectorId = Integer.parseInt(pieces[1]);
				
				line = line.replaceFirst("connector","connector#0");
				
				line = line.substring(0, line.indexOf("value"));
				line = line+"value=\""+moduleId+"."+connectorId+"."+(isOutput?"out":"in")+".noname\" />";
				
				//line= line.replaceFirst("value=\"connector", "value=\""+moduleId+".");
				
				//(\\d)+\\.(\\d)+\\.(in|out)\\..*
				
				
				
//				connector,value=connector.2.output
				
			}
			
			if (line.matches("\\s*<\\s*property\\s*name=\"knob.size\".*")) {
				line=line.replace("knob.size","size");
				line=line.replace("true","26,26");
				line=line.replace("false","30,30");
			}
			
			if ( line.contains("port") &  line.contains("parameter")) {
			    //<property id="port.0" value="parameter.17" />
				line = line.replace("port.","parameter#");
				//<property id="btn#0" value="parameter.17" />
				if (line.contains("parameter")) {
					
					line = line.substring(0,line.lastIndexOf("\""));
					//<property id="btn#0" value="parameter.17
					String tmp = line.substring(line.lastIndexOf("\"")+1,line.length());
					//parameter.17
					line = line.substring(0,line.length()-tmp.length());
					tmp = tmp.substring(tmp.indexOf(".")+1);
					
					int paramId = Integer.parseInt(tmp);
					
					line+=moduleId+"."+paramId+".p.noname\" />";
					//System.out.println("line"+line);	
				}				
			}

			if (line.contains("component.size")) {
				line=line.replace("component.size","size") ;
			}
			
			if (line.contains("btn.text.")) {
				line = line.replaceFirst(".text.","#");
				
				int id = line.indexOf("#")+1;
				String idx = line.substring(id);
				String prefix = line.substring(0,id);
				int last = idx.indexOf("\"");
				String suffix = idx.substring(last);
				idx = idx.substring(0,last);
				
				int theIndex = Integer.parseInt(idx);//+1;
				
				line = prefix+theIndex+suffix;
			}
			
			if (line.contains("bg.orientation")) {
				line = line.replace("bg.orientation","landscape");
			}
			
			if (!line.contains("btn.size"))
				out.println(line);
			
		}
		
		out.flush();
		out.close();
	
		
		System.out.println("Done");
		
	}

}
