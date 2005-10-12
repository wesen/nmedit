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

import nomad.application.ui.Nomad;
import nomad.gui.NomadModuleSection;
import nomad.gui.NomadModuleSection.ModulesSectionType;

public class Patch {
    private Header header;
    private Modules modules;
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
    private NomadModuleSection desktopPanePoly;
    private NomadModuleSection desktopPaneCommon;

	public Patch() {
		patchPanel = new JPanel();
		patchPanel.setLayout(new BorderLayout());

        header = new Header();
        modules = new Modules(this);
        cables = new Cables();
        keyboardAssignment = new KeyboardAssignment();
        knobAssignMap = new KnobAssignMap();
        currentNotes = new CurrentNotes();
        controlMap = new ControlMap();
        patchNotes = new PatchNotes();
        morphMap = new MorphMap();
	}
	
    public Modules getModules() {
        return modules;
    }

    public Cables getCables() {
        return cables;
    }

	public JPanel createPatch(String patchFile) {

	    loadPatch(patchFile);

	    desktopPanePoly = new NomadModuleSection(ModulesSectionType.POLY);
        desktopPaneCommon = new NomadModuleSection(ModulesSectionType.COMMON);

		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(header.getSeperator() + 1);

        desktopPanePoly.setPreferredSize(new Dimension(modules.getMaxWidth(ModulesSectionType.POLY), modules.getMaxHeight(ModulesSectionType.POLY)));
        desktopPaneCommon.setPreferredSize(new Dimension(modules.getMaxWidth(ModulesSectionType.COMMON), modules.getMaxHeight(ModulesSectionType.COMMON)));

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

        for (Enumeration e = modules.getPoly().keys(); e.hasMoreElements();) { 
            i = ((Integer) e.nextElement()).intValue();
            desktopPanePoly.add(modules.getModule(ModulesSectionType.POLY, i).getModuleGUI(), JLayeredPane.DEFAULT_LAYER.intValue());
        }

        for (Enumeration e = modules.getCommon().keys(); e.hasMoreElements();) {
            i = ((Integer) e.nextElement()).intValue();
            desktopPaneCommon.add(modules.getModule(ModulesSectionType.COMMON, i).getModuleGUI(), JLayeredPane.DEFAULT_LAYER.intValue());
        }
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
                        modules.readModuleDump(pchFile);
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
