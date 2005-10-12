package nomad.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XMLReader {
	
	public static Document readDocument(String file) {
		return readDocument(new File(file), null);
	}
	
	public static Document readDocument(String file, boolean validating) {
		return readDocument(new File(file), validating);
	}

	public static Document readDocument(String file, XMLErrorHandler handler) {
		return readDocument(new File(file), handler);
	}
	
	public static Document readDocument(File file) {
		return readDocument(file, null);
	}
	
	public static Document readDocument(File file, boolean validating) {
		return readDocument(file, (validating?new DefaultXMLErrorHandler():null));
	}

	public static Document readDocument(File file, XMLErrorHandler handler) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(handler!=null);
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			if (handler!=null)
				builder.setErrorHandler(handler);
			
			return builder.parse(file);
		}
		catch (ParserConfigurationException pce) {
			if (handler!=null)
				handler.fatalError(pce);
			else
				pce.printStackTrace();
		}
		catch (IOException ioe) {
			if (handler!=null)
				handler.fatalError(ioe);
			else
				ioe.printStackTrace();
		} catch (SAXException sxe) {

			Exception  ex = sxe;
		    if (sxe.getException() != null)
		    	ex = sxe.getException();
		    ex.printStackTrace();
		}
		return null;
		
		
	}
}
