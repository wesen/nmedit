package nomad.patch;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Enumeration;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import nomad.application.Run;
import nomad.application.ui.Nomad;
import nomad.gui.ModuleSectionGUI;
import nomad.model.descriptive.ModuleDescriptions;
import nomad.model.descriptive.substitution.XMLSubstitutionReader;

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
    
    private JPanel patchPanel;
	private JSplitPane splitPane;
	private JScrollPane scrollPanePoly;
	private JScrollPane scrollPaneCommon;
    private ModuleSectionGUI desktopPanePoly;
    private ModuleSectionGUI desktopPaneCommon;

	public Patch() {
		patchPanel = new JPanel();
		patchPanel.setLayout(new BorderLayout());

        header = new Header();
        modulesPoly = new ModuleSection(this, ModuleSection.ModulesSectionType.POLY);
        modulesCommon = new ModuleSection(this, ModuleSection.ModulesSectionType.POLY);
        cables = new Cables();
        keyboardAssignment = new KeyboardAssignment();
        knobAssignMap = new KnobAssignMap();
        currentNotes = new CurrentNotes();
        controlMap = new ControlMap();
        patchNotes = new PatchNotes();
        morphMap = new MorphMap();
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

	public JPanel createPatch(String patchFile) {

	    loadPatch(patchFile);

	    desktopPanePoly = modulesPoly.getModuleSectionGUI();
        desktopPaneCommon = modulesCommon.getModuleSectionGUI();

		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(header.getSeperator() + 1);

        desktopPanePoly.setPreferredSize(new Dimension(modulesPoly.getMaxPixWidth(), modulesPoly.getMaxPixHeight()));
        desktopPaneCommon.setPreferredSize(new Dimension(modulesCommon.getMaxPixWidth(), modulesCommon.getMaxPixHeight()));

		scrollPanePoly = new JScrollPane(desktopPanePoly); 
		scrollPaneCommon = new JScrollPane(desktopPaneCommon);

		splitPane.add(scrollPanePoly, JSplitPane.TOP);
		splitPane.add(scrollPaneCommon, JSplitPane.BOTTOM);

        addModules();

        patchPanel.add(splitPane, BorderLayout.CENTER);

		return patchPanel;
	}

    public void addModules() {
        int i = 0;

        for (Enumeration e = modulesPoly.getModules().keys(); e.hasMoreElements();) { 
            i = ((Integer) e.nextElement()).intValue();
            desktopPanePoly.add(modulesPoly.getModule(i).getModuleGUI(), JLayeredPane.DEFAULT_LAYER.intValue());
        }

        for (Enumeration e = modulesCommon.getModules().keys(); e.hasMoreElements();) {
            i = ((Integer) e.nextElement()).intValue();
            desktopPaneCommon.add(modulesCommon.getModule(i).getModuleGUI(), JLayeredPane.DEFAULT_LAYER.intValue());
        }
    }

    public void loadPatch(String fileName) {
        BufferedReader pchFile;
        String tag = new String();

		String loadfile = "src/data/xml/substitutions.xml"; 
		Run.statusMessage(loadfile);
		XMLSubstitutionReader subsReader = new XMLSubstitutionReader(loadfile);
		loadfile = "src/data/xml/modules.xml"; 
		Run.statusMessage(loadfile);
		ModuleDescriptions moduleDescriptions = new ModuleDescriptions(loadfile, subsReader);

        patchFileName = fileName;

        if (!fileName.equals("")) {
            try {
                pchFile = new BufferedReader(new FileReader(fileName));
                while ((tag = pchFile.readLine()) != null) {
                    if (tag.compareToIgnoreCase("[Header]") == 0)
                        header.readHeader(pchFile);
                    else
                    if (tag.compareToIgnoreCase("[ModuleDump]") == 0)
            			if (pchFile.readLine().trim().compareTo("1") == 0)
            				modulesPoly.readModuleDump(pchFile, moduleDescriptions);
            			else
            				modulesCommon.readModuleDump(pchFile, moduleDescriptions);
                    else
                    if (tag.compareToIgnoreCase("[CurrentNoteDump]") == 0)
                        currentNotes.readCurrentNoteDump(pchFile);
                    else
                    if (tag.compareToIgnoreCase("[CableDump]") == 0)
                        cables.readCableDump(pchFile);
                    else
                    if (tag.compareToIgnoreCase("[ParameterDump]") == 0)
            			if (pchFile.readLine().trim().compareTo("1") == 0)
            				modulesPoly.readParameterDump(pchFile);
            			else
            				modulesCommon.readParameterDump(pchFile);
                    else
                    if (tag.compareToIgnoreCase("[CustomDump]") == 0)
            			if (pchFile.readLine().trim().compareTo("1") == 0)
            				modulesPoly.readCustomDump(pchFile);
            			else
            				modulesCommon.readCustomDump(pchFile);
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
            			if (pchFile.readLine().trim().compareTo("1") == 0)
            				modulesPoly.readNameDump(pchFile);
            			else
            				modulesCommon.readNameDump(pchFile);
                    else
                    if (tag.compareToIgnoreCase("[Notes]") == 0)
                        patchNotes.readPatchNotes(pchFile);
                }
                pchFile.close();
            }
            catch(Exception e) {
                System.out.println(e + " in loadPatch");
            }
        }
    }
    
    public StringBuffer savePatch() {
        StringBuffer result = new StringBuffer("");

        result.append("[Info]\r\n");
        result.append("Creator=" + Nomad.creatorProgram + ", version " + Nomad.creatorVersion + ", release " + Nomad.creatorRelease + "\r\n");
        result.append("[/Info]\r\n");

        result = header.createHeader(result, splitPane);
        result = modulesPoly.createModuleDump(result);
        result = modulesCommon.createModuleDump(result);
        result = currentNotes.createCurrentNoteDump(result);
        result = cables.createCableDump(result);
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
