package net.sf.nmedit.nomad.xml.dom.substitution;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Set;

import net.sf.nmedit.nomad.xml.pull.SubstitutionParser;

import org.xmlpull.v1.XmlPullParserException;

public class Substitutions 
{
	private HashMap<String, Substitution> subs = new HashMap<String, Substitution>();
	
	public Substitutions() {
		super();
	}
	
	public Substitutions(String fileName) {
		this.load(fileName);
	}

	public void putSubstitution(String key, Substitution u) {
		subs.put(key, u);
	}

	public Substitution getSubstitution(String key) {
		return subs.get(key);
	}

	public boolean contains(String key) {
		return subs.containsKey(key);
	}

	public Set getKeys() {
		return subs.keySet();
	}

	public boolean load(String fileName) {
		return load(new File(fileName));
	}
	
	public boolean load(File file) {
		try {
			return load(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean load(InputStream in) {
		return load(new InputStreamReader(in));
	}
	
	public boolean load(Reader reader) {
		SubstitutionParser parser = new SubstitutionParser(this);
		try {
			parser.parse(reader);
			return true;
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
