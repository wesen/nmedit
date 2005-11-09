package nomad.plugin.cache;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import nomad.xml.XMLAttributeReader;
import nomad.xml.XMLAttributeValidationException;
import nomad.xml.XMLReader;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A class that writes the cache file for the user interface xml file.  
 * 
 * @author Christian Schneider
 * @see nomad.plugin.cache.UICache
 */
public class XMLUICacheWriter {

	/**
	 * Writes the cache file for uixmlfile to out file
	 * @param uixmlFile Source file
	 * @param outfile Target file
	 * @throws FileNotFoundException
	 */
	public static void writeCache(String uixmlFile, String outfile)
		throws FileNotFoundException {
		writeCache(uixmlFile, new FileOutputStream(outfile));
	}

	/**
	 * Writes the cache file for uixmlfile to the outputstream
	 * @param uixmlFile Source file
	 * @param ostream Target stream
	 */
	public static void writeCache(String uixmlFile, OutputStream ostream) {
		Document doc = XMLReader.readDocument(uixmlFile, false);
		PrintStream out = new PrintStream(ostream);

		if (doc!=null){
			// read <module id="..." tags
			NodeList moduleNodeList = doc.getElementsByTagName("module");
			for (int i=0;i<moduleNodeList.getLength();i++) {
				Node moduleNode = moduleNodeList.item(i);
				if (moduleNode.getNodeType()==Node.ELEMENT_NODE) {
					int moduleId = 0;
					try {
						moduleId = (new XMLAttributeReader(moduleNode)).getIntegerAttribute("id");
					} catch (XMLAttributeValidationException e) {
						e.printStackTrace();
						break;
					}
					
					out.println("module "+moduleId);
					handleModuleNodeChildren(out, moduleNode);
				}
			}
		}
		
	}
	
	private static void handleModuleNodeChildren(PrintStream out, Node moduleNode) {
		NodeList componentNodeList = moduleNode.getChildNodes();
		for (int i=0;i<componentNodeList.getLength();i++) {
			Node componentNode = componentNodeList.item(i);
			if (componentNode.getNodeType()==Node.ELEMENT_NODE) {
				String componentClassName = null;
				try {
					componentClassName = (new XMLAttributeReader(componentNode)).getAttribute("class");
				} catch (XMLAttributeValidationException e) {
					e.printStackTrace();
					return ;
				}
				
				out.println("component "+componentClassName);
				
				//  properties
				NodeList propertyNodeList = componentNode.getChildNodes();
				for (int j=0;j<propertyNodeList.getLength();j++) {
					Node propertyNode = propertyNodeList.item(j);
					if (propertyNode.getNodeType()==Node.ELEMENT_NODE) {
						String propertyID = null;
						String propertyValue = null;
						try {
							XMLAttributeReader ar = new XMLAttributeReader(propertyNode);
							propertyID = ar.getAttribute("id");
							propertyValue = ar.getAttribute("value");
						} catch (XMLAttributeValidationException e) {
							e.printStackTrace();
							return;
						}
						
						out.println("property "+propertyID+" "+propertyValue);
					}
				}
			}
		}
	}
	
}
