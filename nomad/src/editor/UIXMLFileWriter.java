package editor;

import java.io.FileNotFoundException;

import nomad.gui.model.component.AbstractUIComponent;
import nomad.model.descriptive.DModule;
import nomad.xml.XMLFileWriter;

public class UIXMLFileWriter extends XMLFileWriter {

	public UIXMLFileWriter(String file) throws FileNotFoundException {
		super(file,
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
			"<!DOCTYPE ui-description SYSTEM \"ui-description.dtd\">"	
		);
	}
	
	protected void prolog(String xmlProlog, String docType) {
		super.prolog(xmlProlog, docType);
		beginTag("ui-description", true);
	}

	protected void epilog() {
		endTag();
		super.epilog();
	}
	
	public void beginModuleElement(DModule module) {
		beginTagStart("module");
		addAttribute("id", Integer.toString(module.getModuleID()));
		beginTagFinish(true);
	}
	
	public void writeComponent(AbstractUIComponent component) {
		// begin control-tag section
		beginTagStart("control");
		addAttribute("class", component.getClass().getName());
		beginTagFinish(true);
		
		for (int i=0;i<component.getPropertyCount();i++)
			component.getProperty(i).writeXMLEntry(this);
		
		// end control-tag section
		endTag();
	}
	
}
