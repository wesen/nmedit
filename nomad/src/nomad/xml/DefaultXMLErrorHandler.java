package nomad.xml;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class DefaultXMLErrorHandler implements XMLErrorHandler {

	public void fatalError(ParserConfigurationException pce) {
		System.err.println("** Fatal:");
		pce.printStackTrace();
	}

	public void fatalError(IOException ioe) {
		System.err.println("** Fatal:");
		ioe.printStackTrace();
	}

	public void error(SAXParseException sxe) throws SAXException {
		// thread validation errors as fatal
		printSXEHint(sxe, "Validation Error");
		throw sxe;
	}

	public void fatalError(SAXParseException sxe) throws SAXException {
		printSXE(sxe, "Fatal");
	}

	public void warning(SAXParseException sxe) throws SAXException {
		printSXE(sxe, "Warning");
	}
	
	protected void printSXE(SAXParseException sxe, String praefix) {
		printSXEHint(sxe, praefix);
		System.err.println("   "+sxe.getMessage());
	}
	
	protected void printSXEHint(SAXParseException sxe, String praefix) {
		System.err.println("** "+praefix+", line "
				+sxe.getLineNumber()
				+", col "+sxe.getColumnNumber()
				+", uri "+sxe.getSystemId());
	}

}
