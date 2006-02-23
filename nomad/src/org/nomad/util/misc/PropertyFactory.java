package org.nomad.util.misc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
