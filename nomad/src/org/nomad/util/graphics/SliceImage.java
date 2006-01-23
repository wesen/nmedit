package org.nomad.util.graphics;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.ImageIcon;

import org.nomad.util.misc.MalformedPropertyfileException;
import org.nomad.util.misc.PropertyFactory;

/**
 * Slices an image into smaller parts.
 * Slices can be associated with keys.
 *
 * SliceImage can be created by a property file.
 * The Property-File must contain the attributes:
 * <pre>slice.hrz &lt;value&gt; - <i>number of images in horizontal direction</i>
 *slice.vrt &lt;value&gt; - <i>number of images in vertical direction</i></pre>
 * Keys are associated with coordinates by
 * <pre>&lt;key&gt; &lt;x&gt; &lt;y&gt;</pre>.
 * 
 * @author Christian Schneider
 */

public class SliceImage
{
  /**
   * Hashmap for key to slice-coordinate association.
   */
  private HashMap<String, Point> directors;

  /**
   * Caches slices.
   */
  private Image[][] slices = null;

  /**
   * Number of sclices in horizontal direction.
   */
  private int sliceCountHrz;

  /**
   * Number of sclices in vertical direction.
   */
  private int sliceCountVrt;

  /**
   * Creates a SliceImage object.
   * @param imageFile File object pointing to source image.
   * @param grid Dimension of grid which slices the source image.
   * Note this is not size of a slice in pixels.
   * @throws IllegalArgumentException If grid has at least
   * one dimension less or equal zero.
   */
  public SliceImage(File imageFile, Dimension grid)
    throws IllegalArgumentException
  {
    this(imageFile.getName(), grid);
  }

  /**
   * Creates a SliceImage object.
   * @param imageFile Filename of source image.
   * @param grid Dimension of grid which slices the source image.
   * Note this is not size of a slice in pixels.
   * @throws IllegalArgumentException If grid has at least
   * one dimension less or equal zero.
   */
  public SliceImage(String imageFile, Dimension grid)
    throws IllegalArgumentException
  {
    this(Toolkit.getDefaultToolkit().getImage(imageFile), grid);
  }

  /**
   * Creates a SliceImage object.
   * @param image Image which will be sliced.
   * @param grid Dimension of grid which slices the source image.
   * Note this is not size of a slice in pixels.
   * @throws IllegalArgumentException If grid has at least
   * one dimension less or equal zero.
   */
  public SliceImage(Image image, Dimension grid)
    throws IllegalArgumentException
  {	  
    // check grid dimensions
    if (grid.getWidth()<=0 || grid.getHeight()<=0)
      throw new IllegalArgumentException
      ( "Grid has at least one dimension less or equal zero." );

    // Trick to make sure that required data is available.
    image = (new ImageIcon(image)).getImage();

    // store grid dimensions
    this.sliceCountHrz = (int) grid.getWidth();
    this.sliceCountVrt = (int) grid.getHeight();

    // Create slices ...

    // source image dimensions
    int imgW  = image.getWidth(null);
    int imgH  = image.getHeight(null);

    // slice dimensions in Pixel
    int pxsliceW = (int) (imgW / sliceCountHrz);
    int pxsliceH = (int) (imgH / sliceCountVrt);

    // create slices cache
    slices = new Image[sliceCountHrz][sliceCountVrt];

    // loop through all slices
    for (int i=0; i<sliceCountHrz; i++)
      for (int j=0; j<sliceCountVrt; j++)
      { // sclice offset relative to (0,0)
        int xoffset = i*pxsliceW;
        int yoffset = j*pxsliceH;

        // "copy slice"-image producer
        ImageProducer iproducer = new FilteredImageSource (
          image.getSource(),
          new CropImageFilter(xoffset, yoffset, pxsliceW, pxsliceH)
        );

        // create slice
        slices[i][j] = Toolkit.getDefaultToolkit().createImage(iproducer);
      }

    // max(number of keys) = width*height
    directors = new HashMap<String, Point>();
  }

  // getters and setters

  /**
   * Returns the number of slices in horizontal direction.
   * @return the number of slices in horizontal direction.
   */
  public int getSliceCountHrz()
  { return sliceCountHrz; }

  /**
   * Returns the number of slices in vertical direction.
   * @return the number of slices in vertical direction.
   */
  public int getSliceCountVrt()
  { return sliceCountVrt; }

  /**
   * Returns true if a slice at given coordinates exists.
   * @param x x position
   * @param y y position
   * @return true if slice at x,y exists 
   */
  private boolean checkCoordinates(int x, int y)
  { return (0<=x && x<sliceCountHrz && 0<=y && y<=sliceCountVrt); }

  /**
   * Returns the slice at the given position.
   * @param x Horizontal offset. Must be greater or
   *          equal zero and less than getSliceCountVrt.
   * @param y Vertical offset. Must be greater or
   *          equal zero and less than getSliceCountVrt.
   * @return the slice at the given position.
   * @throws IndexOutOfBoundsException
   *          If conditions for x and y are not fulfilled.
   */
  public Image getSlice(int x, int y)
    throws IndexOutOfBoundsException
  {
    if (!checkCoordinates(x, y))
      throw new IndexOutOfBoundsException
      ( "Slice at given coordinates does not exists (x="+x+",y="+y+")." );

    return slices[x][y];
  }

  /**
   * Associates a key with coordinates of a slice.
   * @param x Horizontal offset. Must be greater or
   *          equal zero and less than getSliceCountVrt.
   * @param y Vertical offset. Must be greater or
   *          equal zero and less than getSliceCountVrt.
   * @param key The key which will be associated with the
   *            given coordinates.
   * @throws IndexOutOfBoundsException
   *          If conditions for x and y are not fulfilled.
   */
  public void setKey(int x, int y, String key)
    throws IndexOutOfBoundsException
  {
    if (!checkCoordinates(x, y))
      throw new IndexOutOfBoundsException
      ( "Slice at given coordinates does not exists (x="+x+",y="+y+")." );

    Point sliceSelector = new Point(x, y);
    directors.put(key, sliceSelector);
  }

  /**
   * Returns the slice at the position associated with the key.
   * @param key Placeholder for slice-coordinate.
   * @return the slice at the position associated with the key.
   * @throws NullPointerException If the key has no association
   *                              with any coordinate.
   */
  public Image getSlice(Object key)
    throws NullPointerException
  {
    Point sliceSelector = directors.get(key);
    if (sliceSelector==null)
      throw new NullPointerException("Key '"
        +key+"' is not associated with a coordinate.");

    return (Image) slices[(int) sliceSelector.getX()]
                         [(int) sliceSelector.getY()];
  }
  
  /**
   * Returns an iterator that iterates over String objects containing
   * all keys.
   * @return key iterator
   */
  public Iterator<String> getKeys() {
	  return directors.keySet().iterator();
  }
  
  /**
   * Adds all images that are associated with a key to the image tracker.
   * @param itracker the image tracker
   */
  public void feedImageTracker(ImageTracker itracker) {
	  Iterator keyIterator = getKeys();
	  while (keyIterator.hasNext()) {
		  String key = (String) keyIterator.next();
		  itracker.putImage(key, getSlice(key)); 
	  }
  }

  // -----------------------------------------------------
  
  public static SliceImage createSliceImage(String slice) {
	  String name = slice;
	  for (int i=name.length()-1;i>=0;i--)
		  if (name.charAt(i)=='.')
		  {
			  name = name.substring(0, i)+".slice";
			  break;
		  }

	  try {
		return SliceImage.createFromPropertyFile(name, slice);
	  } catch (FileNotFoundException e) {
		  e.printStackTrace();
	  } catch (SecurityException e) {
		  e.printStackTrace();
	  } catch (IOException e) {
		  e.printStackTrace();
	  } catch (MalformedPropertyfileException e) {
		  e.printStackTrace();
	  }
	  return null;
  }

  // -----------------------------------------------------

  public static SliceImage createFromPropertyFile
    (File propertyFile, File imageFile)
    throws FileNotFoundException,
           SecurityException,
           IOException,
           MalformedPropertyfileException
  {
    return SliceImage.createFromPropertyFile
      (propertyFile.getName(), imageFile.getName());
  }

  public static SliceImage createFromPropertyFile
    (String propertyFile, String imageFile)
    throws FileNotFoundException,
           SecurityException,
           IOException,
           MalformedPropertyfileException
  {
    return SliceImage.createFromPropertyFile
      (PropertyFactory.CreateProperties(propertyFile), imageFile);
  }

  public static SliceImage createFromPropertyFile
    (Properties properties, String imageFile)
    throws MalformedPropertyfileException
  {
    String shproperty = properties.getProperty("slice.hrz");
    String svproperty = properties.getProperty("slice.vrt");

    if (shproperty==null || svproperty==null)
      throw new MalformedPropertyfileException(
        "Missing 'slice'-attribute(s) in property file.");

    int dimx, dimy;

    try
    {
      dimx = Integer.parseInt(shproperty);
      dimy = Integer.parseInt(svproperty);
    }
    catch(NumberFormatException cause)
    {
      throw new MalformedPropertyfileException(
        "Syntax error in 'slice'-attibute(s).", cause);
    }

    SliceImage si = new SliceImage(imageFile, new Dimension(dimx, dimy));

    for (Enumeration e=properties.propertyNames();e.hasMoreElements();)
    {
      String key   = (String) e.nextElement();
      String value = (String) properties.getProperty(key);

      if (!key.equals("slice.hrz")
        &&!key.equals("slice.vrt"))
      {
        String[] splitted = value.split("(\\s)+", 2);

        if (splitted.length!=2)
          throw new MalformedPropertyfileException("Semantic error in property file.");

        int x, y;

        try
        {
          x = Integer.parseInt(splitted[0].trim());
          y = Integer.parseInt(splitted[1].trim());
        }
        catch(NumberFormatException cause)
        {
          throw new MalformedPropertyfileException(
            "Syntax error in key-coordinate attibute(s).", cause);
        }

        si.setKey(x, y, key.trim());
      }
    }

    return si;
  }


} // SliceImage
