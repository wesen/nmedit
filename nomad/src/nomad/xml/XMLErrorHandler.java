package nomad.xml;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.ErrorHandler;

public interface XMLErrorHandler extends ErrorHandler {
	public void fatalError(ParserConfigurationException pce) ;
	public void fatalError(IOException ioe);
}
