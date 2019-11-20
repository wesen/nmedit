package main;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.JSplitPane;

class PatchData {
		private Header header;
		private Modules modules;
		private Cables cables;
		private KeyboardAssignment keyboardAssignment;
		private KnobAssignMap knobAssignMap;
		private CurrentNotes currentNotes;
		private ControlMap controlMap;
		private PatchNotes patchNotes;
		private MorphMap morphMap;
        
        private String patchFileName = "";
        private static Properties props;
        
        JModAreaPane desktopPanePoly;
        JModAreaPane desktopPaneCommon;
		
		PatchData () {
			header = new Header(this);
			cables = new Cables(this);
			modules = new Modules(this);
			keyboardAssignment = new KeyboardAssignment(this);
			knobAssignMap = new KnobAssignMap(this);
			currentNotes = new CurrentNotes(this);
			controlMap = new ControlMap(this);
			patchNotes = new PatchNotes(this);
			morphMap = new MorphMap(this);
            
            openProps();
		}
		
        public static Properties getProperties() {
            return props;
        }

        public static void openProps(){
            props = new Properties();
            InputStream in = null;
            try {
                in = new FileInputStream("modules.properties");
                props.load(in);
                in.close();
            }
            catch (Exception e) {
                System.out.print(e + " in loading module.properties");
            }
        }

        public void setPanes(JModAreaPane newDesktopPanePoly, JModAreaPane newDesktopPaneCommon) {
            desktopPanePoly = newDesktopPanePoly;
            desktopPaneCommon = newDesktopPaneCommon;
        }
        
		public Modules getModules() {
			return modules;
		}

		public Cables getCables() {
			return cables;
		}

        public JModAreaPane getDesktopPane(boolean poly) {
            return poly?desktopPanePoly:desktopPaneCommon;
        }
        
        public void setPreferredSizes() {
            desktopPanePoly.setPreferredSize(new Dimension(modules.getMaxWidth(true), modules.getMaxHeight(true)));
            desktopPaneCommon.setPreferredSize(new Dimension(modules.getMaxWidth(false), modules.getMaxHeight(false)));
        }
        
        // Moet naar Patch.java toe...
		public void showPatch(JSplitPane splitPane) {
			int i = 0;

			splitPane.setDividerLocation(header.getSeperator() + 1);

			for (Enumeration e = modules.getPoly().keys(); e.hasMoreElements();) { 
				i = ((Integer) e.nextElement()).intValue();
				modules.getModule(true, i).drawModule();
			}

			for (Enumeration e = modules.getCommon().keys(); e.hasMoreElements();) {
				i = ((Integer) e.nextElement()).intValue();
				modules.getModule(false, i).drawModule();
			}

			cables.drawCables(desktopPanePoly, true);
			cables.drawCables(desktopPaneCommon, false);
		}
		
		public void loadPatch(String fileName) {
			BufferedReader pchFile;
			String tag = new String();
            
            patchFileName = fileName;

            if (!fileName.equals("")) {
    			try {
    			   	pchFile = new BufferedReader(new FileReader(fileName));
    				while ((tag = pchFile.readLine()) != null) {
    					if (tag.compareToIgnoreCase("[Header]") == 0)
    						header.readHeader(pchFile);
    					else
    					if (tag.compareToIgnoreCase("[ModuleDump]") == 0)
    						modules.readModuleDump(pchFile, desktopPanePoly, desktopPaneCommon);
    					else
    					if (tag.compareToIgnoreCase("[CurrentNoteDump]") == 0)
    						currentNotes.readCurrentNoteDump(pchFile);
    					else
    					if (tag.compareToIgnoreCase("[CableDump]") == 0)
    						cables.readCableDump(pchFile);
    					else
    					if (tag.compareToIgnoreCase("[ParameterDump]") == 0)
    						modules.readParameterDump(pchFile);
    					else
    					if (tag.compareToIgnoreCase("[CustomDump]") == 0)
    						modules.readCustomDump(pchFile);
    					else
    					if (tag.compareToIgnoreCase("[MorphMapDump]") == 0)
    						morphMap.readMorphMapDump(pchFile);
    					else
    					if (tag.compareToIgnoreCase("[KeyboardAssignment]") == 0)
    						keyboardAssignment.readKeyboardAssignment(pchFile);
    					else
    					if (tag.compareToIgnoreCase("[KnobMapDump]") == 0)
    						knobAssignMap.readKnobMapDump(pchFile);
    					else
    					if (tag.compareToIgnoreCase("[CtrlMapDump]") == 0)
    						controlMap.readCtrlMapDump(pchFile);
    					else
    					if (tag.compareToIgnoreCase("[NameDump]") == 0)
    						modules.readNameDump(pchFile);
    					else
    					if (tag.compareToIgnoreCase("[Notes]") == 0)
    						patchNotes.readPatchNotes(pchFile);
    				}
    				pchFile.close();
    //				modules.closeProp();
    				
    				cables.recalcCableLocations(modules, true);
    				cables.recalcCableLocations(modules, false);
    			}
    			catch(Exception e) {
    				System.out.println(e + " in loadPatch");
    			}
            }
		}
		
//		public void printPatch() {
//			int i = 0;
//
//			System.out.println(header.getVersion());
//
//			System.out.println("\nPoly modules:");
//			for (Enumeration e = modules.getPoly().keys(); e.hasMoreElements();) {
//				i = ((Integer) e.nextElement()).intValue();
//				System.out.println("\t" + modules.getModule(true, i).getModuleData().getModIndex() + " is a " + modules.getModule(true, i).getModuleData().getTypeNameShort() + " (" + modules.getModule(true, i).getModuleData().getModType() + "): \"" + modules.getModule(true, i).getName() + "\" with " + modules.getModule(true, i).getModuleData().getNoParameters() + " parameters.");
//			}
//
//			System.out.println("\nCommon modules:");
//			for (Enumeration e = modules.getCommon().keys(); e.hasMoreElements();) {
//				i = ((Integer) e.nextElement()).intValue();
//				System.out.println("\t" + modules.getModule(false, i).getModuleData().getModIndex() + " is a " + modules.getModule(false, i).getModuleData().getTypeNameShort() + " (" + modules.getModule(false, i).getModuleData().getModType() + "): \"" + modules.getModule(false, i).getName() + "\" with " + modules.getModule(false, i).getModuleData().getNoParameters() + " parameters.");
//			}
//
//			System.out.println("\nPoly cables:");
//			for (i=0; i < cables.getPolySize();i++) {
//				System.out.println("\tcable " + (i+1) + " (" + cables.getCable(true, i).getName() + "): \"" + modules.getModule(true,  cables.getCable(true, i).getEndModule()).getName() + "\" connected to \"" + modules.getModule(true, cables.getCable(true, i).getBeginModule()).getName() + "\"");
//			}
//
//			System.out.println("\nCommon cables:");
//			for (i=0; i < cables.getCommonSize();i++) {
//				System.out.println("\tcable " + (i+1) + " (" + cables.getCable(false, i).getName() + "): \"" + modules.getModule(false, cables.getCable(false, i).getEndModule()).getName() + "\" connected to \"" + modules.getModule(false, cables.getCable(false, i).getBeginModule()).getName() + "\"");
//			}
//		}
		
		public StringBuffer savePatch(JSplitPane splitPane) {
//			String creatorP = "jMod";
//			String creatorV = "0.3";

			StringBuffer result = new StringBuffer("");

			result.append("[Info]\r\n");
			result.append("Creator=" + jMod.creatorP + ", version " + jMod.creatorV + ", release " + jMod.creatorR + "\r\n");
			result.append("[/Info]\r\n");

			result = header.createHeader(result, splitPane);
			result = modules.createModuleDump(result);
			result = currentNotes.createCurrentNoteDump(result);
			result = cables.createCableDump(result);
			result = modules.createParameterDump(result);
			result = modules.createCustomDump(result);
			result = morphMap.createMorphMapDump(result);
			result = keyboardAssignment.createKeyboardAssignment(result);
			result = knobAssignMap.createKnobMapDump(result);
			result = controlMap.createControlMapDump(result);
			result = modules.createNameDump(result);
			result = patchNotes.createNotes(result);

			return result;
		}
}
