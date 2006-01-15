package org.nomad.xml;


import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * A helper class for accessing attribute nodes. 
 * 
 * @author Christian Schneider
 */
public class XMLAttributeReader {
	
	// the node
	private Node node;
	
	// the attributes
	private NamedNodeMap attributes;

	/**
	 * Creates a new attribute reader for the given node
	 * @param node the node
	 */
	public XMLAttributeReader(Node node) {
		this.node = node;
		this.attributes = node.getAttributes();
	}

	/**
	 * Returns a string containing the location of the current node in the xml tree
	 * @return a string containing the location of the current node in the xml tree
	 */
	private String locateElement() {
		Node pos = node;		
		String element = pos.getNodeName();

		String location = element;
		
		if (pos.getAttributes()==null)
			return location;
		
		// element id if exists
		Node idNode = pos.getAttributes().getNamedItem("id");
		if (idNode!=null && idNode.getNodeValue()!=null && idNode.getNodeValue().length()>0)
			element+="[id:"+idNode.getNodeValue()+"]";
		
		while (null!=(pos=pos.getParentNode())) {
			if (pos.getNodeType()==Node.DOCUMENT_NODE) {
				location = "/"+location;
				break;
			}
				
			// element name
			element = pos.getNodeName();
			// element id if exists
			idNode = pos.getAttributes().getNamedItem("id");
			if (idNode!=null && idNode.getNodeValue()!=null && idNode.getNodeValue().length()>0)
				element+="[id:"+idNode.getNodeValue()+"]";
			
			// add element to location
			location = element+"/"+location;
		}
		return location;
	}
	
	/**
	 * Returns a string containing the location of the current attribute in the xml tree
	 * @param attributeName the name of the attribute
	 * @return a string containing the location of the current attribute in the xml tree
	 */
	public String locateElement(String attributeName) {
		return locateElement()+":@"+attributeName;
	}

	/**
	 * Returns the attribute value or throws a XMLAttributeValidationException
	 * if the attribute does not exists 
	 * @param name the attribute name
	 * @return the attribute value
	 * @throws XMLAttributeValidationException the attribute does not exist
	 */
	public String getAttribute(String name) throws XMLAttributeValidationException {
		String value = getAttribute(name, null);
		if (value==null)
			throw new XMLAttributeValidationException(
			  "Missing attribute: "+locateElement(name)
			);
		return value;
	}

	/**
	 * Returns the attribute value or returns <code>defaultValue</code>
	 * if the attribute does not exists.
	 * @param name the attribute name
	 * @param defaultValue the default value that is returned if the attribute does not exist
	 * @return the attribute value
	 */
	public String getAttribute(String name, String defaultValue) {
		if (attributes==null)
			return defaultValue;
		
		Node node = attributes.getNamedItem(name);
		if (node==null) {
			return defaultValue;
		}
		
		String value = node.getNodeValue();
		if (value==null || value.length()==0) {
			System.err.println("** Warning, Attribute has no value, using default value ('"+defaultValue+"'): ");
			System.err.println("   "+locateElement(name));
			return defaultValue;
		}
		
		return value;
	}

	/**
	 * Returns the value that must be a floating point value.
	 * @param name the attribute name
	 * @return the value
	 * @throws XMLAttributeValidationException The attribute does not exist or is no
	 * floating point value.
	 */
	public double getDoubleAttribute(String name) throws XMLAttributeValidationException {
		String value = getAttribute(name);
		try {
			return Double.parseDouble(value);
		} 
		catch (NumberFormatException e) {
			throw new XMLAttributeValidationException(
			  "Attribute must be floating point number: "+locateElement(name)
			);
		}
	}

	/**
	 * Returns the value that must be a floating point value.
	 * @param name the attribute name
	 * @param defaultValue the default value returned if the attribute does not exist or is no floating point number
	 * @return the value
	 */
	public double getDoubleAttribute(String name, double defaultValue) {
		String value = getAttribute(name, null);
		if (value==null)
			return defaultValue;

		try {
			return Double.parseDouble(value);
		} 
		catch (NumberFormatException e) {
			System.err.println("** Warning, Attribute must be floating point number, using default value ('"+defaultValue+"'): ");
			System.err.println("   "+locateElement(name));
			return defaultValue;
		}
	}
	
	/**
	 * Returns the value that must be a number.
	 * @param name the attribute name
	 * @return the value
	 * @throws XMLAttributeValidationException The attribute does not exist or is no number
	 */
	public int getIntegerAttribute(String name) throws XMLAttributeValidationException {
		String value = getAttribute(name);
		try {
			return Integer.parseInt(value);
		} 
		catch (NumberFormatException e) {
			throw new XMLAttributeValidationException(
			  "Attribute must be number: "+locateElement(name)
			);
		}
	}

	/**
	 * Returns the value that must be a number.
	 * @param name the attribute name
	 * @param defaultValue the default value returned if the attribute does not exist or is no number
	 * @return the value
	 */
	public int getIntegerAttribute(String name, int defaultValue) {
		String value = getAttribute(name, null);
		if (value==null)
			return defaultValue;

		try {
			return Integer.parseInt(value);
		} 
		catch (NumberFormatException e) {
			System.err.println("** Warning, Attribute must be number, using default value ('"+defaultValue+"'): ");
			System.err.println("   "+locateElement(name));
			return defaultValue;
		}
	}

	/**
	 * Returns the value. If the value is not in the <code>candidates</code> list
	 * or does not exist, a XMLAttributeValidationException is thrown
	 * @param name the attribute name
	 * @param candidates a list containing the possible return values
	 * @return the value
	 * @throws XMLAttributeValidationException The attribute does not exist or is not found in the
	 * candidates list
	 */
	public String getNamedAttribute(String name, String[] candidates) throws XMLAttributeValidationException {
		String value = getAttribute(name);

		for (int i=0;i<candidates.length;i++)
			if (value.equals(candidates[i]))
				return value;
		
		String list="";
		for (int i=0;i<candidates.length;i++)
			list+=candidates[i]+((i<candidates.length-1)?",":"");
		
		throw new XMLAttributeValidationException(
			"Attribute value ('"+value+"') must be one of ["+list+"]: "
			+locateElement(name)
		);
	}

	/**
	 * Returns the value. If the value is not in the <code>candidates</code> list
	 * or does not exist, the default value will be returned
	 * @param name the attribute name
	 * @param candidates a list containing the possible return values
	 * @param defaultValue the default value
	 * @return the value
	 */
	public String getNamedAttribute(String name, String[] candidates, String defaultValue) {
		String value = getAttribute(name, defaultValue);

		for (int i=0;i<candidates.length;i++)
			if (value.equals(candidates[i]))
				return value;
		
		String list="";
		for (int i=0;i<candidates.length;i++)
			list+=candidates[i]+((i<candidates.length-1)?",":"");
		

		System.err.println("** Warning, Attribute value ('"+value+"') must be one of ["+list+"]:");
		System.err.println("   "+locateElement(name));

		return defaultValue;
	}

	/**
	 * Returns the value that must be either the string 'true' or 'false'.
	 * @param name the attribute name
	 * @return the value
	 * @throws XMLAttributeValidationException The attribute does not exist or is not a boolean value
	 */
	public boolean getBooleanAttribute(String name) throws XMLAttributeValidationException {
		String value = getNamedAttribute(name, new String[] {"true", "false"});
		return value.equals("true");
	}
	
	/**
	 * Returns the value that must be either the string 'true' or 'false'. If the attribute does not exist,
	 * or is not valid the default value will be returned
	 * @param name the attribute name
	 * @return the value
	 * @param defaultValue the default value
	 */
	public boolean getBooleanAttribute(String name, boolean defaultValue) {
		String value = getNamedAttribute(name, new String[] {"true", "false"}, Boolean.toString(defaultValue));
		return value.equals("true");
	}
}
