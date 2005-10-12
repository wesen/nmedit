package nomad.misc;

import java.util.Properties;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.lang.SecurityException;

public class PropertyFactory
{
  public static Properties
    CreateProperties(String fileName)
    throws FileNotFoundException,
           SecurityException,
           IOException
  {
    InputStream in = new FileInputStream(fileName);
    Properties properties = new Properties();
    properties.load(in);
    in.close();
    return properties;
  }
}
