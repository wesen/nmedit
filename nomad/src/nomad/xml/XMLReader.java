package nomad.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * A helper class that reads a xml document from a file and returns the document node
 * to the xml file.
 * 
 * @author Christian Schneider
 */
public class XMLReader {
	
	/**
	 * Reads document using the default error handler.
	 * @param file xml file name
	 * @return the document node
	 */
	public static Document readDocument(String file) {
		return readDocument(new File(file), null);
	}

	/**
	 * Reads document using the default error handler.
	 * @param file xml file name
	 * @return the document node
	 */
	public static Document readDocument(File file) {
		return readDocument(file, null);
	}
	
	/**
	 * Reads document and validates it.
	 * @param file xml file name
	 * @param validating true if the xml file should be validated
	 * @return the document node
	 */
	public static Document readDocument(String file, boolean validating) {
		return readDocument(new File(file), validating);
	}
	
	/**
	 * Reads document and validates it.
	 * @param file xml file name
	 * @param validating true if the xml file should be validated
	 * @return the document node
	 */
	public static Document readDocument(File file, boolean validating) {
		return readDocument(file, (validating?new DefaultXMLErrorHandler():null));
	}

	/**
	 * Reads a document using a custom error handler
	 * @param file  xml file name
	 * @param handler the error handler
	 * @return the document node
	 */
	public static Document readDocument(String file, XMLErrorHandler handler) {
		return readDocument(new File(file), handler);
	}

	/**
	 * Reads a document using a custom error handler
	 * @param file  xml file name
	 * @param handler the error handler
	 * @return the document node
	 */
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
