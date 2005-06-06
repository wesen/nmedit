package main;

import java.util.Properties;
import java.util.Vector;

public class ModuleData {
    final public static int pixWidth = 256;
    final public static int pixHeight = 16;

    final public static int pixWidthD2 = 128;
    final public static int pixHeightD2 = 8;
    
    private int gridX, gridY;

    // .pch info
	private Module module;
	private String name;
	private String nameShort;
	private String nameLong;
	private int modType;
	private Integer modIndex = null;
    
	private double cycles = 0;
	private double progMem = 0;
	private double xMem = 0;
	private double yMem = 0;
	private double zeroPage = 0;
	private double dynMem = 0;
	private int gridHeight = 2; // minimale hoogte 2;
	private int noParameters = 0;
	private int noInputs = 0;
	private int noOutputs = 0;
	private int noCustoms = 0;
	
	private boolean poly = true;
    // .pch info

	private Vector parametersVector = null;
	private Vector inPutsVector, outPutsVector = null;
    private Vector customsVector = null;

	ModuleData(Module newModule, Integer newIndex, int newModType, int newGridX, int newGridY, boolean newPoly) {
        name = new String();
		nameShort = new String();
		nameLong = new String();
		inPutsVector = new Vector();
		outPutsVector = new Vector();
		parametersVector = new Vector();
		customsVector = new Vector();

        poly = newPoly;
		modIndex = newIndex;
		module = newModule;
		modType = newModType;
		gridX = newGridX;
		gridY = newGridY;

		readProps();
	}

// Setters	

	public void setModuleName(String newName) {
		name = newName;
	}

	public void setPoly(boolean newPoly) {
		poly = newPoly;
	}

	protected int addConnection(Module newModule, boolean bInput, String newName, String newTypeS) {
		if (bInput)
			return addConnection(newModule, bInput, newName, newTypeS, (inPutsVector.size() * 16) + 5, ModuleData.pixHeight);
		return addConnection(newModule, bInput, newName, newTypeS, (ModuleData.pixWidth - 3) - (((noOutputs) - outPutsVector.size()) * 16), ModuleData.pixHeight-2);
	}

	protected int addConnection(Module newModule, boolean bInput, String newName, String newTypeS, int newX, int newY) {
		int newType = 0;
		if (newTypeS.trim().compareToIgnoreCase("audio") == 0)
			newType = 0; 
		else 
			if (newTypeS.trim().compareToIgnoreCase("control") == 0)
				newType = 1; 
		else 
			if (newTypeS.trim().compareToIgnoreCase("logic") == 0)
				newType = 2; 
		else 
			if (newTypeS.trim().compareToIgnoreCase("slave") == 0)
				newType = 3; 
		else {
			newType = -1;
			System.out.println("Wrong type of connection... " + newTypeS + " :" + modType);
		}
		
		Connection con = null;
		if (bInput) {
			con = new Connection(newModule, bInput, inPutsVector.size(), newName, newType, newX, newY);
			inPutsVector.add(con);
		}
		else {
			con = new Connection(newModule, bInput, outPutsVector.size(), newName, newType, newX, newY);
			outPutsVector.add(con);
		}
		return newType; 
	}
	
    protected void addParameter(int newValue, String newName) {
        Parameter par = new Parameter(newValue, newName);
        parametersVector.add(par);
    }
    
    protected void addParameter(int newValue, String newName, int x, int y) {
        Parameter par = new Parameter(newValue, newName);
        parametersVector.add(par);
    }

	protected void setParameters(String params) {
//		int paramCount;
		Parameter par = null;
		String[] sa = new String[39]; // 3+36, 0 = index, 1 = type, 2 = aantal, 3..x = parameter: max 36 in (17) EventSeq
		sa = params.split(" ");
		for (int i = 0; i < noParameters; i++) {
			par = (Parameter) parametersVector.get(i);
			par.setValue(Integer.parseInt(sa[i+3])); // pas vanaf de derde positie staat de parameter
		}
	}

    protected void addCustom(int newValue, String newName) {
        Custom cus = new Custom(newValue, newName);
        customsVector.add(cus);
    }

    protected void addLabel(String label, String text, int x, int y) {
        if (label.equalsIgnoreCase("label"))
            module.add(new JModLabel(text, x, y));
        else if (label.equalsIgnoreCase("image")) {
            module.add(new JModImage(text, x, y));
        }
    }

    public void setCustoms(String params) {
//		int customCount;
		Custom cus = null;
		String[] sa = new String[4]; // 2+2, 0 = index, 1 = aantal, 2..x = parameter: max 2 in (90) NoteSeqB
		sa = params.split(" ");
		for (int i = 0; i < noCustoms; i++) {
			cus = (Custom) customsVector.get(i);
			cus.setValue(Integer.parseInt(sa[i+2]));
		}
	}

    public void setPixLocationX(int newPixLocationX) {
		gridX = newPixLocationX / pixWidth;
		if (gridX < 0)
			gridX = 0;
	}

    public void setPixLocationY(int newPixLocationY) {
		gridY = newPixLocationY / pixHeight;
		if (gridY < 0)
			gridY = 0;
	}

// Getters

	public boolean getPoly() {
		return poly;
	}

    public int getModIndex() {
            return modIndex.intValue();
        }
    
    public Integer getModIndexInteger() {
            return modIndex;
        }

	public int getModType() {
		return modType;
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

	public int getPixWidth() {
		return pixWidth;
	}

	public int getPixHeight() {
		return gridHeight * pixHeight;
	}

	public int getPixLocationX() {
		return gridX * pixWidth;
	}

	public int getPixLocationY() {
		return gridY * pixHeight;
	}

	public double getCycles() {
		return cycles;
	}
	
	public double getProgMem() {
		return progMem;
	}
	
	public double getXMem() {
		return xMem;
	}
	
	public double getYMem() {
		return yMem;
	}
	
	public double getZeroPage() {
		return zeroPage;
	}
	
	public double getDynMem() {
		return dynMem;
	}

	public String getModuleName() {
		return name;
	}
	
	public int getNoParameters() {
		return noParameters;
	}
	
	public int getNoInputs() {
		return noInputs;
	}

	public Vector getInputs() {
		return inPutsVector;
	}

	public int getNoOutputs() {
		return noOutputs;
	}

	public Vector getOutputs() {
		return outPutsVector;
	}
	
    public Connection getConnection(int inOut, int index) {
        return (Connection) ((inOut==0)?getInputs().get(index):getOutputs().get(index)); 
    }
    
	public int getNoCustoms() {
		return noCustoms;
	}

/*
// vervangen door getNoParameters()
	public int getParamSize() {
		return parameters.size();
	}
*/

    public int getParameterValue(int index) {
        Parameter par = (Parameter) parametersVector.get(index);
        return par.getValue();
    }

    public Parameter getParameter(int index) {
        return (Parameter) parametersVector.get(index);
    }

/*
// vervangen door getNoCustoms()
	public int getCustomSize() {
		return customs.size();
	}
*/

	public int getCustomValue(int index) {
		Custom cus = null;
		cus = (Custom) customsVector.get(index);
		return cus.getValue();
	}

//	public String getTypeName() {
//		return getTypeNameShort(modType);
//	}

    public String getTypeNameShort() {
        return nameShort;
    }

    public String getTypeNameLong() {
        return nameLong;
    }

    public static String getTypeNameLong(int modType) {
        return PatchData.getProperties().getProperty(modType + ".namelong", "-1");
    }

    public static String getTypeName(int modType) {
        return PatchData.getProperties().getProperty(modType + ".name", "-1");
    }

    public static String getTypeCycles(int modType) {
        String cycles = PatchData.getProperties().getProperty(modType + ".cycles", "-1");
        float c = Math.round(Float.valueOf(cycles).floatValue()*100) / 100.0f;
        return c + "%";
    }
    
    public static String getTypeNLC(int modType) {
        return getTypeNameLong(modType) + " [" + getTypeCycles(modType) + "]";
    }

    protected void readProps() {
        int i = 0;
        boolean err = false;
        
        inPutsVector.clear();
        outPutsVector.clear();
        parametersVector.clear();
        customsVector.clear();
        
		String[] sa = new String[5];
        Properties props = PatchData.getProperties();
		nameShort = props.getProperty(modType + ".name", "nix_");
		if (!nameShort.equals("nix_")) {
			try {
                cycles = Float.parseFloat(props.getProperty(modType + ".cycles", "-1"));
                nameLong = props.getProperty(modType + ".namelong", "-1");
				progMem = Float.parseFloat(props.getProperty(modType + ".progMem", "-1"));
				xMem = Float.parseFloat(props.getProperty(modType + ".xmem", "-1"));
				yMem = Float.parseFloat(props.getProperty(modType + ".ymem", "-1"));
				zeroPage = Float.parseFloat(props.getProperty(modType + ".zeropage", "-1"));
				dynMem = Float.parseFloat(props.getProperty(modType + ".dynmem", "-1"));
				gridHeight = Integer.parseInt(props.getProperty(modType + ".height", "-1"));
				noParameters = Integer.parseInt(props.getProperty(modType + ".parameters", "-1"));
				noInputs = Integer.parseInt(props.getProperty(modType + ".inputs", "-1"));
				noOutputs = Integer.parseInt(props.getProperty(modType + ".outputs", "-1"));
                noCustoms = Integer.parseInt(props.getProperty(modType + ".custom", "-1"));
			}
			catch (Exception e) {
				System.out.println(e + " in reading props");
			}

			if (noParameters > 0) {
				for (i=0; i < noParameters; i++) {
					sa = props.getProperty(modType + ".p" + i).split(";");
					if (sa.length < 4)
						System.out.println("Error in props, parameter " + modType + ", length:" + sa.length);
					if (sa.length < 5)
						// geen default voor parameter 
						addParameter(0, sa[0]);				
					else
						addParameter(Integer.parseInt(sa[4].trim()), sa[0]);				
				}
			}

			if (noInputs > 0) {
				for (i=0; i < noInputs; i++) {
					sa = props.getProperty(modType + ".i" + i).split(";");
					switch (sa.length) {
						case 2: addConnection(module, true, sa[0], sa[1]); break;
                        case 3: {
                            String[] sa2 = sa[2].split(",");
                            addConnection(module, true, sa[0], sa[1], Integer.parseInt(sa2[0].trim()), Integer.parseInt(sa2[1].trim())); break;
                        }
						default: System.out.println("Error in props, inputs from module " + modType + ", length:" + sa.length);
					}
				}
			}

			if (noOutputs > 0) {
				for (i=0; i < noOutputs; i++) {
					sa = props.getProperty(modType + ".o" + i).split(";");
					switch (sa.length) {
						case 2: addConnection(module, false, sa[0], sa[1]); break;
                        case 3: {
                            String[] sa2 = sa[2].split(",");
                            addConnection(module, false, sa[0], sa[1], Integer.parseInt(sa2[0].trim()), Integer.parseInt(sa2[1].trim())); break;
                        }
						default: System.out.println("Error in props, outputs from module " + modType + ", length:" + sa.length);
					}
				}
			}

            if (noCustoms > 0) {
                for (i=0; i < noCustoms; i++) {
                    sa = props.getProperty(modType + ".c" + i).split(";");
                    if (sa.length < 3)
                        System.out.println("Error in props, customs " + modType + ", length:" + sa.length);
                    if (sa.length < 5)
                        // geen default voor custom 
                        addCustom(0, sa[0]);
                    else
                        addCustom(Integer.parseInt(sa[1].trim()), sa[0]);               
                }
            }

            i = 0;
            err = false;
            while (!err)
            {
                try {
                    sa = props.getProperty(modType + ".l" + i).split(";");
                    i++;
                    if (sa.length != 3)
                        System.out.println("Error label in props, module " + modType + ", length:" + sa.length + " expected lenghth: 3");
                    else {
                        String[] sa2 = sa[2].split(",");
                        addLabel(sa[0], sa[1].trim(), Integer.parseInt(sa2[0].trim()), Integer.parseInt(sa2[1].trim()));
                    }
                }
                catch (Exception e) {
                    err = true;
                }
            }
		}
		else
			System.out.println("Module " + modType + " is not defined in module.properties");
	}

}
