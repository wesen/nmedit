package org.nomad.xml.dom.substitution;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;


import org.nomad.xml.XMLAttributeReader;
import org.nomad.xml.XMLAttributeValidationException;
import org.nomad.xml.XMLReader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLSubstitutionReader 
{
	private HashMap subs = new HashMap();
	
	public XMLSubstitutionReader() {
		super();
	}
	
	public XMLSubstitutionReader(String filename) {
		this.loadFromXML(filename);
	}

	public void putSubstitution(String key, Substitution u) {
		subs.put(key, u);
	}

	public Substitution getSubstitution(String key) {
		return (Substitution) subs.get(key);
	}

	public boolean contains(String key) {
		return subs.containsKey(key);
	}

	public Set getKeys() {
		return subs.keySet();
	}

	public void loadFromXML(String filename) {
		Document document = XMLReader.readDocument(filename, true);
		if (document!=null)
			loadFromXML(document);
	}
	
	private void loadFromXML(Document doc) {
		NodeList list = doc.getElementsByTagName("list");
		for (int i=0;i<list.getLength();i++) {			
			loadListSubstitution(list.item(i));
		}
		list = doc.getElementsByTagName("transform");
		for (int i=0;i<list.getLength();i++) {
			loadTransformation(list.item(i));
		}
		list = doc.getElementsByTagName("use-implementation");
		for (int i=0;i<list.getLength();i++) {			
			loadImplementation(list.item(i));
		}
	}

	private void loadListSubstitution(Node node) {
		XMLAttributeReader att = new XMLAttributeReader(node);
		String key;
		try {
			key = att.getAttribute("id");
		} catch (XMLAttributeValidationException e) {
			e.printStackTrace();
			return;
		}

		NodeList items=node.getChildNodes();
		
		LinkedList list = new LinkedList();
		for (int i=0;i<items.getLength();i++) {
			Node item = items.item(i);
			if (item.getNodeName().equals("item"))
				list.add(getNodeText(item));
		}
		String[] values = new String[list.size()];
		for (int i=0;i<list.size();i++)
			values[i]=(String)list.get(i);
		putSubstitution(key, new ListSubstitution(values));
	}

	private String getNodeText(Node node) {
		node = node.getFirstChild();
		String text="";
		while (node!=null) {
			if (node.getNodeType()==Node.TEXT_NODE && node.getNodeValue()!=null)
				text+=node.getNodeValue();
			node=node.getNextSibling();
		}
		return text;
	}
	
	private void loadTransformation(Node node) {
		XMLAttributeReader att = new XMLAttributeReader(node);
		
		String key;
		
		try {
			key=att.getAttribute("id");
		} catch (XMLAttributeValidationException e) {
			e.printStackTrace();
			return ;
		}

		int offset=att.getIntegerAttribute("offset", 0);
		double factor=att.getDoubleAttribute("factor", 1);
		

		TransformationSubstitution t = new TransformationSubstitution(offset, factor);
		t.setPraefix(att.getAttribute("praefix", null));
		t.setSuffix(att.getAttribute("suffix", null));

		// if-node
		node = node.getFirstChild();
		while (node!=null && node.getNodeName()!="if")
			node=node.getNextSibling();

		if (node!=null) {
			att = new XMLAttributeReader(node);
			try {
				t.setReplacement(
						att.getIntegerAttribute("value-is"),
						att.getAttribute("replace-with")
				);
			} catch (XMLAttributeValidationException e) {
				e.printStackTrace();
			}
			
		}

		putSubstitution(key, t);
	}
	
	private void loadImplementation(Node node) {
		XMLAttributeReader att = new XMLAttributeReader(node);
		
		String key;
		String className;
		try {
			key = att.getAttribute("id");
			className = att.getAttribute("class");
		} catch (XMLAttributeValidationException e) {
			e.printStackTrace();
			return ;
		}

		Class theClass;
		try {
			theClass = ClassLoader.getSystemClassLoader().loadClass(className);
			try {
				Substitution subs;
				subs = (Substitution) theClass.newInstance();
				putSubstitution(key, subs);
			} catch (ClassCastException e) {
				System.err.println("** Warning: Class '"+className+"' is not a descendant of '"+Substitution.class.getName()+"'");
				System.err.println("   "+att.locateElement("class"));
				e.printStackTrace();
			} catch (InstantiationException e) {
				System.err.println("** Warning: Class '"+className+"' ('"+Substitution.class.getName()+"') can not be instantiated.");
				System.err.println("   "+att.locateElement("class"));
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				System.err.println("** Warning: Class '"+className+"' ('"+Substitution.class.getName()+"') can not be created.");
				System.err.println("   "+att.locateElement("class"));
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			System.err.println("** Warning: Class '"+className+"' ('"+Substitution.class.getName()+"') not found.");
			System.err.println("   "+att.locateElement("class"));
			e.printStackTrace();
		} 
	}

}
