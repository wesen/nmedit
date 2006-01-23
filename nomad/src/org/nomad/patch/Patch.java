package org.nomad.patch;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.nomad.main.Nomad;
import org.nomad.theme.ModuleSectionGUI;
import org.nomad.theme.PatchGUI;


public class Patch {
    private Header header;
    private ModuleSection modulesPoly;
    private ModuleSection modulesCommon;
    private Cables cables;
    private KeyboardAssignment keyboardAssignment;
    private KnobAssignMap knobAssignMap;
    private CurrentNotes currentNotes;
    private ControlMap controlMap;
    private PatchNotes patchNotes;
    private MorphMap morphMap;
	
    String patchFileName = "";
    
    private PatchGUI patchPanel;
	private JSplitPane splitPane;
	private JScrollPane scrollPanePoly;
	private JScrollPane scrollPaneCommon;
    private ModuleSectionGUI desktopPanePoly;
    private ModuleSectionGUI desktopPaneCommon;

	public Patch() {
        header = new Header();
        modulesPoly = new ModuleSection(this, ModuleSection.ModulesSectionType.POLY);
        modulesCommon = new ModuleSection(this, ModuleSection.ModulesSectionType.COMMON);
//        cables = new Cables();
        keyboardAssignment = new KeyboardAssignment();
        knobAssignMap = new KnobAssignMap();
        currentNotes = new CurrentNotes();
        controlMap = new ControlMap();
        patchNotes = new PatchNotes();
        morphMap = new MorphMap();

		patchPanel = new PatchGUI(this);
		patchPanel.setLayout(new BorderLayout());

		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        patchPanel.add(splitPane, BorderLayout.CENTER);
	}

    public ModuleSection getModulesPoly() {
        return modulesPoly;
    }
    
    public ModuleSection getModulesCommon() {
        return modulesPoly;
    }

    public Cables getCables() {
        return cables;
    }

	public static JPanel createPatch(Reader in, Patch patch) {
        Patch.loadPatch(in, patch);
		return Patch.createPatchUI(patch);
	}

	public static JPanel createPatch(InputStream in, Patch patch) {
		return createPatch(new InputStreamReader(in), patch);
	}
    
	public static JPanel createPatch(String patchFile, Patch patch) {
		try {
			if (!patchFile.equals("")) {
                Patch.loadPatch(new FileReader(patchFile), patch);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        patch.patchFileName = patchFile;
        return Patch.createPatchUI(patch);
	}
	
	public static JPanel createPatchUI(Patch patch) {
		patch.rebuildUI();
		return patch.patchPanel;
	}

	public void rebuildUI() {

		// remove
		if (scrollPaneCommon!=null) {
			splitPane.remove(scrollPaneCommon);
			// important, unlinks the displays
			desktopPaneCommon.removeModuleDisplays();
		}

		if (scrollPanePoly!=null) {
			splitPane.remove(scrollPanePoly);
			// important, unlinks the displays
			desktopPanePoly.removeModuleDisplays();
		}
		
		// new
		
		// create a new gui
		modulesPoly.resetModuleSectionGUI();
		modulesCommon.resetModuleSectionGUI();
		
	    desktopPanePoly = modulesPoly.getModuleSectionGUI();
        desktopPaneCommon = modulesCommon.getModuleSectionGUI();

        desktopPanePoly.setPreferredSize(new Dimension(modulesPoly.getMaxGridPixWidth(), modulesPoly.getMaxGridPixHeight()));
        desktopPaneCommon.setPreferredSize(new Dimension(modulesCommon.getMaxGridPixWidth(), modulesCommon.getMaxGridPixHeight()));

        scrollPanePoly = new JScrollPane(desktopPanePoly); 
        scrollPaneCommon = new JScrollPane(desktopPaneCommon);

        splitPane.add(scrollPanePoly, JSplitPane.TOP);
        splitPane.add(scrollPaneCommon, JSplitPane.BOTTOM);

        splitPane.setDividerLocation(header.getSeperator() + 1);

        desktopPanePoly.populate();
        desktopPaneCommon.populate();		
	}

//    public void loadPatch(String fileName) {
//    	try {
//            if (!fileName.equals("")) {
//            	loadPatch(new FileReader(fileName));
//                patchFileName = fileName;
//            }
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//    }
//    
//    public void loadPatch(InputStream in) {
//    	loadPatch(new InputStreamReader(in));
//    }
    
    public static void loadPatch(Reader reader, Patch patch) {
        BufferedReader pchFile;
        String tag = new String();

        try {
            pchFile = new BufferedReader(reader);
            while ((tag = pchFile.readLine()) != null) {
                if (tag.compareToIgnoreCase("[Header]") == 0)
                    patch.header.readHeader(pchFile);
                else
                if (tag.compareToIgnoreCase("[ModuleDump]") == 0)
        			if (pchFile.readLine().trim().compareTo("1") == 0)
                        patch.modulesPoly.readModuleDump(pchFile);
        			else
                        patch.modulesCommon.readModuleDump(pchFile);
                else
                if (tag.compareToIgnoreCase("[CurrentNoteDump]") == 0)
                    patch.currentNotes.readCurrentNoteDump(pchFile);
                else
                if (tag.compareToIgnoreCase("[CableDump]") == 0)
        			if (pchFile.readLine().trim().compareTo("1") == 0)
                        patch.modulesPoly.readCableDump(pchFile);
        			else
                        patch.modulesCommon.readCableDump(pchFile);
//                    patch.cables.readCableDump(pchFile);
                else
                if (tag.compareToIgnoreCase("[ParameterDump]") == 0)
        			if (pchFile.readLine().trim().compareTo("1") == 0)
                        patch.modulesPoly.readParameterDump(pchFile);
        			else
                        patch.modulesCommon.readParameterDump(pchFile);
                else
                if (tag.compareToIgnoreCase("[CustomDump]") == 0)
        			if (pchFile.readLine().trim().compareTo("1") == 0)
                        patch.modulesPoly.readCustomDump(pchFile);
        			else
                        patch.modulesCommon.readCustomDump(pchFile);
                else
                if (tag.compareToIgnoreCase("[MorphMapDump]") == 0)
                    patch.morphMap.readMorphMapDump(pchFile);
                else
                if (tag.compareToIgnoreCase("[KeyboardAssignment]") == 0)
                    patch.keyboardAssignment.readKeyboardAssignment(pchFile);
                else
                if (tag.compareToIgnoreCase("[KnobMapDump]") == 0)
                    patch.knobAssignMap.readKnobMapDump(pchFile);
                else
                if (tag.compareToIgnoreCase("[CtrlMapDump]") == 0)
                    patch.controlMap.readCtrlMapDump(pchFile);
                else
                if (tag.compareToIgnoreCase("[NameDump]") == 0)
        			if (pchFile.readLine().trim().compareTo("1") == 0)
                        patch.modulesPoly.readNameDump(pchFile);
        			else
                        patch.modulesCommon.readNameDump(pchFile);
                else
                if (tag.compareToIgnoreCase("[Notes]") == 0)
                    patch.patchNotes.readPatchNotes(pchFile);
            }
            pchFile.close();
        }
        catch(Exception e) {
            System.out.println(e + " in loadPatch");
        }
    }
    
    public StringBuffer savePatch() {
        StringBuffer result = new StringBuffer("");

        result.append("[Info]\r\n");
        result.append("Creator=" + Nomad.creatorProgram + ", version " + Nomad.creatorVersion + ", release " + Nomad.creatorRelease + "\r\n");
        result.append("[/Info]\r\n\r\n");

        result = header.createHeader(result, splitPane);
        
        result = modulesPoly.createModuleDump(result);
        result = modulesCommon.createModuleDump(result);
        
        result = currentNotes.createCurrentNoteDump(result);
        
        result = modulesPoly.createCableDump(result);
        result = modulesCommon.createCableDump(result);
        
        result = modulesPoly.createParameterDump(result);
        result = modulesCommon.createParameterDump(result);
        
        result = modulesPoly.createCustomDump(result);
        result = modulesCommon.createCustomDump(result);
        
        result = morphMap.createMorphMapDump(result);
        
        result = keyboardAssignment.createKeyboardAssignment(result);
        
        result = knobAssignMap.createKnobMapDump(result);
        
        result = controlMap.createControlMapDump(result);
        
        result = modulesPoly.createNameDump(result);
        result = modulesCommon.createNameDump(result);
        
        result = patchNotes.createNotes(result);

        return result;
    }
}
