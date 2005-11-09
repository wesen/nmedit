package nomad.plugin.cache;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * A class to read the ui cache file.
 * 
 * @author Christian Schneider
 */
public class UICache {

	private ArrayList lines = new ArrayList();
	
	// array that translates a module-id (array-index) to the index in the linestarters array
	// that contains the file position of the module
	private int[] moduleToLineStarters = new int[]{};
	
	/**
	 * Creates the cache accessor object. 
	 * @param file the cache file name
	 * @throws FileNotFoundException
	 */
	public UICache(String file) throws FileNotFoundException {
		try {
			createIndex(new BufferedReader(new InputStreamReader(new FileInputStream(file))));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// resets the arrays
	private void reset() {
		moduleToLineStarters = new int[]{};
	}
	
	// builds the index for the arrays
	private void createIndex(BufferedReader in) throws IOException {
		reset();
		String line = null;
		ArrayList tmpModuleLineIndex = new ArrayList();
		while ((line=in.readLine())!=null) {
			if (line.startsWith("module")) {
				// remember pair (module id, lineStarters.index)
				tmpModuleLineIndex.add(new Point(Integer.parseInt(line.split(" ")[1]), lines.size()));
			}	
			lines.add(line);
		};

		// fill array with invalid indices
		moduleToLineStarters = new int[128];
		for (int i=0;i<moduleToLineStarters.length;i++) 
			moduleToLineStarters[i] = -1;

		// add known indices
		for (int i=0;i<tmpModuleLineIndex.size();i++) {
			Point p = (Point) tmpModuleLineIndex.get(i);
			moduleToLineStarters[p.x/*module-id*/] = p.y/*array index*/;
		}
		
	}
	
	// returns the array index of module with given id, or -1 if the id does not exist
	private int getArrayIndexForModule(int moduleId) {
		return (moduleId>=0&&moduleId<moduleToLineStarters.length)
			? moduleToLineStarters[moduleId]
			: -1;
	}
	
	/**
	 * Loads the module with given moduleId by triggering the callback object.
	 * @param moduleId the searched module
	 * @param callback 
	 * @return false if the module does not exist, or an error has occured.
	 */
	public boolean loadModule(int moduleId, ModulePropertyCallback callback) {
		int moduleStartIndex = getArrayIndexForModule(moduleId);
		if (moduleStartIndex<0)
			// module not found
			return false;

		// read module section
		try {
			readModuleProperties(callback, moduleStartIndex);
		} catch (UICacheException e) {
			// TODO throw exception
			System.err.println("** Error in properties for module (id="+moduleId+")");
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Reads the module section. The file position must moved to the begin
	 * of such a section before. All properties are passed through the
	 * callback object
	 * 
	 * @param callback the callback object
	 * @param moduleStartIndex 
	 * @throws UICacheException
	 */
	private void readModuleProperties(ModulePropertyCallback callback, int moduleStartIndex)
		throws UICacheException {
		moduleStartIndex++; // read line 'module ...'
		while (moduleStartIndex<lines.size()) {
			String line = (String) lines.get(moduleStartIndex);
			if (line.startsWith("module"))
				// finished
				return;
			else if (line.startsWith("component")) {
				callback.readComponent(line.substring("component".length()).trim());
			}
			else if (line.startsWith("property")) {
				line = line.substring("property".length()).trim();
				int firstStarterline = line.indexOf(" ");
				String propertyId;
				String value;

				if (firstStarterline>0) {
					propertyId = line.substring(0,firstStarterline);
					value = line.substring(firstStarterline);
					propertyId = propertyId.trim();
					value = value.trim();
				} else {
					propertyId = line;
					value = " ";
				} 
				callback.readComponentProperty(propertyId, value);
			}
			else {
				System.err.println("Unknown entry:'"+line+"'");
				//throw new UICacheException("Unknown entry:'"+line+"'");
			}
			moduleStartIndex++;
		}
	}
	
}
