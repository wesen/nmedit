package nomad.application;

// @see http://www.randelshofer.ch/oop/javasplash/javasplash.html

import java.awt.Color;
import java.awt.Font;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Window;
import java.awt.Image;
import java.awt.Frame;
import java.lang.InterruptedException;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.lang.Runtime;
import java.net.URL;
import java.lang.InternalError;

/**
 * @author Christian Schneider
 */
public class SplashWindow extends Window
{
  /**
   * Current instance of the splash window.
   */
  private static SplashWindow instance;
  
  /**
   * Current status message
   */
  private String statusMessage = null;
  private Point statusPosition = null;

  /**
   * The splash image.
   */
  private Image image;    
  // Buffer
  private Image buffer;
  private Graphics bufferGraphics;
  int bufwidth;
  int bufheight;

  /**
   * Indicates that the window has been drawn at least once.
   */
  private boolean paintCalled = false;

  private SplashWindow(Frame parent, Image image, Point statusPosition)
  {
    super(parent);
    this.image = image;
    this.statusPosition = statusPosition;

    // load image
    MediaTracker mt = new MediaTracker(this);
    mt.addImage(image, 0);
    try
    {
      mt.waitForID(0);
    }
    catch (InterruptedException ie)
    { }

    int wndw = image.getWidth(this);
    int wndh = image.getHeight(this);
    setSize(wndw, wndh); // set window size
    Dimension screensz = Toolkit.getDefaultToolkit().getScreenSize();
    // center window
    setLocation((screensz.width-wndw)/2, (screensz.height-wndh)/2);

    // Users shall be able to close the splash window by
    // clicking on its display area. This mouse listener
    // listens for mouse clicks and disposes the splash window.
    MouseAdapter disposeOnClick = new MouseAdapter()
      {
        public void mouseClicked(MouseEvent evt)
        {
          // Note: To avoid that method splash hangs, we
          // must set paintCalled to true and call notifyAll.
          // This is necessary because the mouse click may
          // occur before the contents of the window
          // has been painted.
          synchronized(SplashWindow.this)
          {
            SplashWindow.this.paintCalled = true;
            SplashWindow.this.notifyAll();
          }
          SplashWindow.instance = null;
          dispose();
        }
      };
    addMouseListener(disposeOnClick);
  }
  
  private void resetBuffer(){
      // always keep track of the image size
	  bufwidth=getSize().width;
      bufheight=getSize().height;

      //    clean up the previous image
      if(bufferGraphics!=null){
          bufferGraphics.dispose();
          bufferGraphics=null;
      }
      if(buffer!=null){
          buffer.flush();
          buffer=null;
      }
      System.gc();

      //    create the new image with the size of the panel
      buffer=createImage(bufwidth,bufheight);
      bufferGraphics=buffer.getGraphics();
  }
  
  /**
   * Sets the statusMessage property of the splash screen instance.
   * If the splash screen instance is null the message is written
   * to System.out
   * @param message the status message
   */
  public static void statusMessage(String message) {
	  if (instance == null)
		  System.out.println(message);
	  else {
		  instance.setStatusMessage(message);
	  }
  }
  
  private void setStatusMessage(String message) {
	this.statusMessage = message;
	updateStatus();
  }

/**
   * Updates the display area of the window.
   */
  public void update(Graphics g)
  {
    // Note: Since the paint method is going to draw an
    // image that covers the complete area of the component we
    // do not fill the component with its background color
    // here. This avoids flickering.
    paint(g);
  }

  /**
   * Paints the image on the window.
   */
  public void paint(Graphics g)
  {
      //    checks the buffersize with the current panelsize
      //    or initialises the image with the first paint
      if(bufwidth!=getSize().width || 
         bufheight!=getSize().height || 
         buffer==null || bufferGraphics==null)
         resetBuffer();

      if(bufferGraphics!=null){
          //this clears the offscreen image, not the onscreen one
          bufferGraphics.clearRect(0,0,bufwidth,bufheight);

          //calls the paintbuffer method with 
          //the offscreen graphics as a param
          paintBuffer(bufferGraphics);

          //we finaly paint the offscreen image onto the onscreen image
          g.drawImage(buffer, 0, 0, this);
      }
    
    // Notify method splash that the window
    // has been painted.
    // Note: To improve performance we do not enter
    // the synchronized block unless we have to.
    if (!paintCalled)
    {
      paintCalled = true;
      synchronized (this) { notifyAll(); }
    }
  }
  
  protected void paintBuffer(Graphics g) {
      g.drawImage(image,0,0,this);
      this.drawStatus(g);
  }
  
  private void updateStatus() {
	  this.update(this.getGraphics());
		//this.update(this.getGraphics());
  }
  
  private void drawStatus(Graphics g) {
	  if (statusPosition!=null && statusMessage!=null) {
		  g.setFont(new Font("Tahoma", Font.PLAIN , 10));
		  g.setColor(Color.BLACK);
		  g.drawString(statusMessage, statusPosition.x, statusPosition.y);
	  }
  }

  /**
   * Open's a splash window using the specified image.
   * @param image The splash image.
   * @param statusPosition the position of the status message
   */
  public static void splash(Image image, Point statusPosition)
  {
    if (instance == null && image != null)
    {
      Frame f = new Frame();
      // Create the splash image
      instance = new SplashWindow(f, image, statusPosition);
      // Show the window.
      instance.show();
      // Note: To make sure the user gets a chance to see the
      // splash window we wait until its paint method has been
      // called at least once by the AWT event dispatcher thread.
      // If more than one processor is available, we don't wait,
      // and maximize CPU throughput instead.
      if ( !EventQueue.isDispatchThread()
          &&Runtime.getRuntime().availableProcessors()==1)
      {
        synchronized (instance)
        {
          while (!instance.paintCalled)
          {
            try { instance.wait(); } catch (InterruptedException e) {}
          }
        }
      }
    }
  }

  /**
   * Open's a splash window using the specified image.
   * @param imageURL The url of the splash image.
   * @param statusPosition 
   */
  public static void splash(URL imageURL, Point statusPosition)
  {
    if (imageURL != null)
      splash(Toolkit.getDefaultToolkit().createImage(imageURL), statusPosition);
  }

  /**
   * Closes the splash window.
   */
  public static void disposeSplash()
  {
    if (instance != null)
    {
      instance.getOwner().dispose();
      instance = null;
    }
  }

  /**
   * Invokes the main method of the provided class name.
   * @param className The class name 
   * @param args the command line arguments
   */
  public static void invokeMain(String className, String[] args)
  {
    try
    {
      Class.forName(className)
        .getMethod("main", new Class[] {String[].class})
        .invoke(null, new Object[] {args});
    }
    catch (Exception e)
    {
      InternalError error =
        new InternalError("Failed to invoke main method");
      error.initCause(e);
      throw error;
    }
  }
}
