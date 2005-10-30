package nomad.plugin.cache;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class UICache {

	RandomAccessFile raf = null;
	private long[] linestarters = new long[]{};
	private int[] moduleToLineStarters = new int[]{};
	
	public UICache(String file) throws FileNotFoundException {
		raf = new RandomAccessFile(file, "r" /*only for reading*/);
		
		createIndex();
	}
	
	private void reset() {
		linestarters = new long[]{};
		moduleToLineStarters = new int[]{};
	}
	
	private void createIndex() {
		String line = null;
		long pos = 0;
		ArrayList tmpLineIndex = new ArrayList();
		ArrayList tmpModuleLineIndex = new ArrayList();
		
		try {
			raf.seek(0);
		} catch (IOException e1) {
			e1.printStackTrace();
			reset(); // reset arrays
			return ;
		}
		
		try {
			while (raf.getFilePointer()<raf.length()) {
				line=raf.readLine();
				if (line.startsWith("module")) 
					// remember pair (module id, lineStarters.index)
					tmpModuleLineIndex.add(new Point(Integer.parseInt(line.split(" ")[1]), tmpLineIndex.size()));

				tmpLineIndex.add(new Long(pos)); // remember line start position
				// remeber position for next line
				pos = raf.getFilePointer();
			}
		} catch (IOException e) {
			e.printStackTrace();
			reset(); // reset arrays
			return ;
		}

		// store line positions
		linestarters = new long[tmpLineIndex.size()];
		for (int i=0;i<tmpLineIndex.size();i++)
			linestarters[i]=((Long)tmpLineIndex.get(i)).longValue();
		
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
	
	private int getArrayIndexForModule(int moduleId) {
		return (moduleId>=0&&moduleId<moduleToLineStarters.length)
			? moduleToLineStarters[moduleId]
			: -1;
	}
	
	public boolean loadModule(int moduleId, ModulePropertyCallback callback) {
		int moduleStartIndex = getArrayIndexForModule(moduleId);
		if (moduleStartIndex<0)
			// module not found
			return false;
	
		try {
			// go to position in file
			raf.seek(linestarters[moduleStartIndex]);
		} catch (IOException e) {
			//TODO throw exception
			e.printStackTrace();
			return false;
		}
		
		// read module section
		try {
			readModuleProperties(callback);
		} catch (UICacheException e) {
			// TODO throw exception
			System.err.println("** Error in properties for module (id="+moduleId+")");
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	private void readModuleProperties(ModulePropertyCallback callback)
		throws UICacheException {
		try {
			raf.readLine(); // read line 'module ...'
			while (raf.getFilePointer()<raf.length()) {
				String line = raf.readLine();
				if (line.startsWith("module"))
					// finished
					return;
				else if (line.startsWith("component")) {
					line = line.substring("component".length()).trim();
					callback.readComponent(line);
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
			}
		} catch (IOException e) {
			throw new UICacheException(e);
		}
	}
	
}
