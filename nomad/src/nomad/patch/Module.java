package nomad.patch;

import java.util.Vector;

import nomad.gui.NomadModule;
import nomad.gui.helpers.NomadModuleFactory;
import nomad.patch.Modules.ModulePixDimension;

/**
 * @author Ian Hoogeboom
 *
 * The Module class holds all the internal information of the module.
 * An Factory is used to create it's representation.
 * TODO Create an interface to pass to the Factory.
 */
public class Module {

    // fixed information
    private double cycles = 0;
    private double progMem = 0;
    private double xMem = 0;
    private double yMem = 0;
    private double zeroPage = 0;
    private double dynMem = 0;
    private int gridHeight = 2; // minimal
    private int noParameters = 0;
    private int noInputs = 0;
    private int noOutputs = 0;
    private int noCustoms = 0;

    private String name = "";
    private String nameShort = "";
    private String nameLong = "";
    private int modType = 0;
    private Integer modIndex = null;
    
    private Vector parametersVector = null;
    private Vector inPutsVector, outPutsVector = null;
    private Vector customsVector = null;

    // where am I on grid?
    private int gridX, gridY;

	public Module(Integer newIndex, int newType, int newGridX, int newGridY) {
		
        name = new String();
        nameShort = new String();
        nameLong = new String();
        inPutsVector = new Vector();
        outPutsVector = new Vector();
        parametersVector = new Vector();
        customsVector = new Vector();

        modIndex = newIndex;
        modType = newType;
        gridX = newGridX;
        gridY = newGridY;
	}
    
    public int getModIndex() {
        return modIndex.intValue();
    }
    
    public int getModType() {
        return modType;
    }

    public String getModuleName() {
        return name;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public int getGridWidth() {
        return 1;
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public int getPixLocationX() {
        return gridX * ModulePixDimension.PIXWIDTH;
    }

    public int getPixLocationY() {
        return gridY * ModulePixDimension.PIXHEIGHT;
    }

    public int getPixWidth() {
        return ModulePixDimension.PIXWIDTH;
    }

    public int getPixHeight() {
        return gridHeight * ModulePixDimension.PIXHEIGHT;
    }

    public void setModuleName(String newName) {
        name = newName;
    }

    public int getNoParameters() {
        return noParameters;
    }
    
    public int getParameterValue(int index) {
        Parameter par = (Parameter) parametersVector.get(index);
        return par.getValue();
    }

    public int getNoCustoms() {
        return noCustoms;
    }

    public int getCustomValue(int index) {
        Custom cus = null;
        cus = (Custom) customsVector.get(index);
        return cus.getValue();
    }

    protected void setParameters(String params) {
        Parameter par = null;
        String[] sa = new String[39]; // 3+36, 0 = index, 1 = type, 2 = param count, 3..x = parameter: max 36 in (17) EventSeq
        sa = params.split(" ");
        for (int i = 0; i < noParameters; i++) {
            par = (Parameter) parametersVector.get(i);
            par.setValue(Integer.parseInt(sa[i+3])); // from the 3rd position are the parameters 
        }
    }

    public void setCustoms(String params) {
        Custom cus = null;
        String[] sa = new String[4]; // 2+2, 0 = index, 1 = custom count, 2..x = ustoms: max 2 in (90) NoteSeqB
        sa = params.split(" ");
        for (int i = 0; i < noCustoms; i++) {
            cus = (Custom) customsVector.get(i);
            cus.setValue(Integer.parseInt(sa[i+2]));
        }
    }
    
    public NomadModule getModuleGUI() {
        return NomadModuleFactory.createGUI(this);
    }
}
