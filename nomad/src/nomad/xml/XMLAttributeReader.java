package nomad.xml;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class XMLAttributeReader {
	
	private Node node;
	private NamedNodeMap attributes;

	public XMLAttributeReader(Node node) {
		this.node = node;
		this.attributes = node.getAttributes();
	}

	private String locateElement() {
		Node pos = node;		
		String element = pos.getNodeName();
		
		// element id if exists
		Node idNode = pos.getAttributes().getNamedItem("id");
		if (idNode!=null && idNode.getNodeValue()!=null && idNode.getNodeValue().length()>0)
			element+="[id:"+idNode.getNodeValue()+"]";

		String location = element;
		
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
	
	public String locateElement(String attributeName) {
		return locateElement()+":@"+attributeName;
	}

	public String getAttribute(String name) throws XMLAttributeValidationException {
		String value = getAttribute(name, null);
		if (value==null)
			throw new XMLAttributeValidationException(
			  "Missing attribute: "+locateElement(name)
			);
		return value;
	}

	public String getAttribute(String name, String defaultValue) {
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

	public boolean getBooleanAttribute(String name) throws XMLAttributeValidationException {
		String value = getNamedAttribute(name, new String[] {"true", "false"});
		return value.equals("true");
	}

	public boolean getBooleanAttribute(String name, boolean defaultValue) {
		String value = getNamedAttribute(name, new String[] {"true", "false"}, Boolean.toString(defaultValue));
		return value.equals("true");
	}
}
