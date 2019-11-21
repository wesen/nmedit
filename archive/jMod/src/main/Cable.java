package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Cable extends JPanel implements Cloneable{
	/**
	 * 0 6 0 0 4 0 1 = kleur, module, 'gat', in (0), module, 'gat', in (0) of uit (1).
	 * 'strikt' is dat het eerste gat een 'in' is en het tweede gat een 'uit' is...
	 */
	
    // .pch info
	private int color, originalColor;
	private int beginArray[], endArray[];
    // .pch info

    private int xBegin, yBegin, xEnd, yEnd;
	private int cxBegin, cyBegin, cxEnd, cyEnd;

	private int initXBegin, initYBegin; // de initiele waarde vanhet beginpunt (voor dragCable)
	private int initXEnd, initYEnd; // de initiele waarde vanhet eindpunt (voor dragCable)
	
	private boolean curved;

    public static int AUDIO = 0;
    public static int CONTROL = 1;
    public static int LOGIC = 2;
    public static int SLAVE = 3;
    public static int USER1 = 4;
    public static int USER2 = 5;
    public static int LOOSE = 6;
    public static int SELECTCOLOR1 = 7;
    public static int SELECTCOLOR2 = 8;
  
    private static Color DARKRED = Color.RED.darker().darker();
    private static Color DARKYELLOW = Color.YELLOW.darker().darker();
    private static Color DARKGRAY = Color.GRAY.darker().darker();
    private static Color DARKGREEN = Color.GREEN.darker();
    private static Color DARKMAGENTA = Color.MAGENTA.darker().darker();
    private static Color DARKWHITE = Color.WHITE.darker().darker();
    private static Color DARKCYAN = Color.CYAN.darker();
    private static Color LIGHTGRAY = Color.GRAY.brighter();
    private static Color LIGHTORANGE = Color.ORANGE.brighter();
  
	private CubicCurve2D.Double cubic = new CubicCurve2D.Double();
	private Point2D.Double start = new Point2D.Double(0,0);
	private Point2D.Double one = new Point2D.Double(0,0);
	private Point2D.Double two = new Point2D.Double(0,0);
	private Point2D.Double end = new Point2D.Double(0,0); 
	
	private final static BasicStroke stroke1 = new BasicStroke(1.0f);
	private final static BasicStroke stroke2 = new BasicStroke(2.0f);
	
	public final static int cableWindowOffset = 7;	// Hoeveel groter moet het windowtje zijn van de cable?

	private final static RenderingHints AALIAS = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    private int waveOffset = 0;
    private int waveOffset2 = 0;
    private int waveOffset3 = 0;
    private double d2, d3 = 0;
	
//	private Cables cables = null; 
	
	private Dimension dimension = new Dimension(0,0);
	
	Cable(String params) {
		super();
//		[0] = module index, [1] = connector index, [2] = input or output

		beginArray = new int[3];
		endArray = new int[3];

		String[] paramArray = new String[7];
		paramArray = params.split(" ");
		
		color = Integer.parseInt(paramArray[0]);
        originalColor = color;

// De 4de parameter is 'altijd' 0, de 7de bepaald in(0) of uit(1) voor het laatste 'paar'
// 'Out' kan ook een 'In' zijn... het doorlussen van een Out -> In -> In is mogelijk!!!
    	beginArray[0] = Integer.parseInt(paramArray[1]);
    	beginArray[1] = Integer.parseInt(paramArray[2]);
    	beginArray[2] = Integer.parseInt(paramArray[3]);
        if (beginArray[2] != 0) System.out.println("IN CONNECTOR EXPECTED!!! PATCH NON 3.0 complient!");

		endArray[0] = Integer.parseInt(paramArray[4]);
		endArray[1] = Integer.parseInt(paramArray[5]);
		endArray[2] = Integer.parseInt(paramArray[6]); // In of Uit
		init();
	}

	Cable(int newBeginMod, int newBeginConnector, int newBeginConnectorType, int newEndMod, int newEndConnector, int newEndConnectorType) {
		beginArray = new int[3];
		endArray = new int[3];

        setCableData(newBeginMod, newBeginConnector, newBeginConnectorType, newEndMod, newEndConnector, newEndConnectorType, Cable.LOOSE);

		init();
	}
	
	Cable(int newX, int newY, int newType) {
        beginArray = new int[3];
        endArray = new int[3];
    
        color = newType;
        originalColor = color;

		xBegin = newX;
		yBegin = newY;
		initXBegin = xBegin;
		initYBegin = yBegin;

		xEnd = newX;
		yEnd = newY;
        initXEnd = xEnd;
        initYEnd = yEnd;
		setCableWindowLayout(xBegin, yBegin, xEnd, yEnd, true);
		init();
	}

	public void setCableData(int newInMod, int newInConnector, int newInInput, int newOutMod, int newOutConnector, int newOutInput, int newType) {
    
        if (newInMod > -1) { // bij -1 dan moeten we de oude waarden behouden
      		beginArray[0] = newInMod;
      		beginArray[1] = newInConnector;
      		beginArray[2] = newInInput;
        }
        if (newOutMod > -1) { // bij -1 dan moeten we de oude waarden behouden
      		endArray[0] = newOutMod;
      		endArray[1] = newOutConnector;
      		endArray[2] = newOutInput; // In of Uit
        }
    
        if (newInInput == 1) {// als er in de begin array een out zit, dan moeten we gaan omdraaien.
            int newTempMod, newTempConnector, newTempInput = -1;
                
            newTempMod = beginArray[0];
            newTempConnector = beginArray[1];
            newTempInput = beginArray[2];
              
            beginArray[0] = endArray[0];
            beginArray[1] = endArray[1];
            beginArray[2] = endArray[2];
            
            endArray[0] = newTempMod;
            endArray[1] = newTempConnector;
            endArray[2] = newTempInput;
        }
   
		color = newType;
        originalColor = color;
	}
	
// Sertters
	
	public void init() {
		this.setBorder(BorderFactory.createEmptyBorder());
		this.setOpaque(false);

        double r = Math.random(); 
        if (r < 1)
            d2 = 1.2;
        if (r < 0.8)
            d2 = 1.4;
        if (r < 0.6)
            d2 =  1.6;
        if (r < 0.4)
            d2 = 2;
        if (r < 0.2)
            d2 = 3;

        r = Math.random(); 
        if (r < 1)
            d3 = 1.2;
        if (r < 0.8)
            d3 = 1.4;
        if (r < 0.6)
            d3 = 1.6;
        if (r < 0.4)
            d3 = 2;
        if (r < 0.2)
            d3 = 3;
  }

  public void setColor(int newColor) {
    color = newColor;
    originalColor = color;
    this.repaint();
  }

  public void setSelectColor1() {
      if (color != Cable.SELECTCOLOR1 && color != Cable.SELECTCOLOR2) {
          originalColor = color;
      }
      color = Cable.SELECTCOLOR1;
      this.repaint();
    }

  public void setSelectColor2() {
      if (color != Cable.SELECTCOLOR2 && color != Cable.SELECTCOLOR1) {
          originalColor = color;
      }
      color = Cable.SELECTCOLOR2;
      this.repaint();
    }

//  public int getTempColor() {
//      return tempColor;
//  }
  
  public void restoreColor() {
    color = originalColor;
    this.repaint();
  }

  public void setNewDragWindowLayout(int newX, int newY) {
//    Debug.println(Cables.getDragBegin()?"Begin":"End");
//    Debug.println("new : " + (newX - (2 * cableOffset)) + ", " + (newY - (2 * cableOffset)));
//    Debug.println("x,y : " + xBegin + "," + yBegin + " " + xEnd + "," + yEnd);

    if (Cables.getDragBeginWindow()) setCableWindowBegins(newX, newY); else setCableWindowEnds(newX, newY);
  }
  
	private void setCableWindowEnds(int newXEnd, int newYEnd) {
    // Bij het verslepen van de output
	  setCableWindowLayout(initXBegin, initYBegin, newXEnd, newYEnd, false);
	}

  private void setCableWindowBegins(int newXBegin, int newYBegin) {
    // Bij het verslepen van de input
    setCableWindowLayout(newXBegin, newYBegin, initXEnd, initYEnd, false);
  }
  
	public void setCableWindowLayout(int newXBegin, int newYBegin, int newXEnd, int newYEnd, boolean newCurved) {
//	    Debug.println("x, y  : " + newXin + "," + newYin + " " + newXout + "," + newYout);
//
//	    int cxMin, cyMin;
		int dummy;
		
		this.curved = newCurved;
		
		if (newXBegin > newXEnd) {
			dummy = newXBegin;
			newXBegin = newXEnd;
			newXEnd = dummy;					 
		
			dummy = newYBegin;
			newYBegin = newYEnd;
			newYEnd = dummy;					 
		}

		xBegin = newXBegin - (2 * cableWindowOffset);
		yBegin = newYBegin - (2 * cableWindowOffset);
		xEnd = newXEnd - (2 * cableWindowOffset);
		yEnd = newYEnd - (2 * cableWindowOffset);

        cxBegin = newXBegin - (Math.min(newXBegin, newXEnd)) + (2 * cableWindowOffset) + Connection.imageWidth;
        cyBegin = newYBegin - (Math.min(newYBegin, newYEnd)) + (2 * cableWindowOffset) + Connection.imageWidth;
        cxEnd = newXEnd - (Math.min(newXBegin, newXEnd)) + (2 * cableWindowOffset) + Connection.imageWidth;
        cyEnd = newYEnd - (Math.min(newYBegin, newYEnd)) + (2 * cableWindowOffset) + Connection.imageWidth;

		dimension.setSize(Math.abs(xBegin - xEnd) + (4*cableWindowOffset) + (2*Connection.imageWidth), Math.abs(yBegin - yEnd) + (4*cableWindowOffset) + (2 * Connection.imageWidth));
        
		this.setLocation(Math.min(xBegin, xEnd), Math.min(yBegin, yEnd));
		this.setSize(dimension);

        waveOffset = (int)Math.sqrt(Math.abs(cxBegin - cxEnd) * Math.abs(cxBegin - cxEnd) + (Math.abs(cyBegin - cyEnd) * Math.abs(cyBegin - cyEnd)));
        
        if (waveOffset < 50) { waveOffset = 5; }
        if ((waveOffset >= 50) && (waveOffset < 100)) { waveOffset = 20; }
        if ((waveOffset >= 100) && (waveOffset < 200)) { waveOffset = 30; }
        if (waveOffset >= 200) { waveOffset = 40; }

        waveOffset2 = (int)(waveOffset / d2);
        waveOffset3 = (int)(waveOffset / d3);

		start.setLocation(cxBegin, cyBegin);

		if (this.curved) {
			if (cyBegin > cyEnd) {
				one.setLocation(cxBegin + waveOffset2, cyBegin - waveOffset);
				two.setLocation(cxEnd - waveOffset3, cyEnd + waveOffset);
			}
			else {
				one.setLocation(cxBegin + waveOffset3, cyBegin + waveOffset);
				two.setLocation(cxEnd - waveOffset2, cyEnd - waveOffset);
			}
		}
		else {
			one.setLocation(cxBegin, cyBegin);
			two.setLocation(cxEnd, cyEnd);
		}
		
		end.setLocation(cxEnd, cyEnd);
		
		cubic.setCurve(start, one, two, end);				

//		Debug.println("Nieuwe waarden : " + newXBegin + "," + newYBegin + " " + newXEnd + "," + newYEnd);
//		Debug.println("Panel op splitpane : " + xBegin + "," + yBegin + " " + xEnd + "," + yEnd);
//		Debug.println("Kabel op panel     : " + cxBegin + "," + cyBegin + " " + cxEnd + "," + cyEnd);
		
//		System.out.println(Math.abs(Math.abs(cxIn - cxOut) - Math.abs(cyIn - cyOut)));
	}

  public void reInitCables() {
    initXBegin = xBegin + (2 * cableWindowOffset);
    initYBegin = yBegin + (2 * cableWindowOffset);
    initXEnd = xEnd + (2 * cableWindowOffset);
    initYEnd = yEnd + (2 * cableWindowOffset);
  }
  
// Getters

    public void setBeginModule(int i) {
        beginArray[0] = i;
    }
    
    public int getBeginModule() {
        return beginArray[0];
    }

    public void setBeginConnector(int i) {
        beginArray[1] = i;
    }
    
    public int getBeginConnector() {
        return beginArray[1];
    }

    public void setBeginConnectorType(int i) {
        beginArray[2] = i;
    }
    
    public int getBeginConnectorType() {
        return beginArray[2];
    }

	public int getEndModule() {
		return endArray[0];
	}

	public int getEndConnector() {
		return endArray[1];
	}

	public int getEndConnectorType() {
		return endArray[2];
	}

    public boolean determBeginOrEndWindowDrag(int newX, int newY) {
        if ((newX - (2 * cableWindowOffset) == xBegin) && (newY - (2 * cableWindowOffset) == yBegin))
            return true;
        return false;
    }

    public boolean determBeginOrEndCableDrag(int newMod, int newIndex, int newInput) {
        if (newMod == beginArray[0] && newIndex == beginArray[1] && newInput == 0)
            return true;
        return false;
    }
  
	public int getColor() {
		return color;
	}

	public String getName() {
		String returnName;
		switch (color) {
			case 0: returnName = "Audio"; break;		// 24bit, min = -64, max = +64 - 96kHz.
			case 1: returnName = "Control"; break;		// 24bit, min = -64, max = +64 - 24kHz.
			case 2: returnName = "Logic"; break;		//  1bit, low =  0, high = +64.
			case 3: returnName = "Slave"; break;		// frequentie referentie tussen master en slave modulen
			case 4: returnName = "User1"; break;
			case 5: returnName = "User2"; break;
			case 6: returnName = "Loose"; break;
			default: returnName = "Wrong type..."; break;
		}
		return returnName;
	}
	
	public void paintComponent(Graphics gg) {
		super.paintComponent(gg);

//		this.setLocation(Math.min(xIn, xOut), Math.min(yIn, yOut));
//		this.setSize(dimension);

		Graphics2D g = (Graphics2D) gg;

		g.addRenderingHints(AALIAS);	

		switch (color) {
			case 0: g.setColor(Cable.DARKRED); break;
			case 1: g.setColor(Color.BLACK); break;
			case 2: g.setColor(Cable.DARKYELLOW); break;
			case 3: g.setColor(Cable.DARKGRAY); break;
			case 4: g.setColor(Cable.DARKGREEN); break;
			case 5: g.setColor(Cable.DARKMAGENTA); break;
            case 6: g.setColor(Cable.DARKWHITE); break;
            case 7: g.setColor(Cable.DARKCYAN); break;
            case 8: g.setColor(Color.ORANGE); break;
		}

		g.setStroke(stroke2);
		g.draw(cubic);

		switch (color) {
			case 0: g.setColor(Color.RED); break;
			case 1: g.setColor(Color.BLUE); break;
			case 2: g.setColor(Color.YELLOW); break;
			case 3: g.setColor(Cable.LIGHTGRAY); break;
			case 4: g.setColor(Color.GREEN); break;
			case 5: g.setColor(Color.MAGENTA); break;
            case 6: g.setColor(Color.WHITE); break;
            case 7: g.setColor(Color.CYAN); break;
            case 8: g.setColor(Cable.LIGHTORANGE); break;
		}

		g.setStroke(stroke1);
		g.draw(cubic);
        
//        g.drawOval((int)cubic.ctrlx1, (int)cubic.ctrly1, 2, 2);
//        g.drawOval((int)cubic.ctrlx2, (int)cubic.ctrly2, 2, 2);

//		g.setColor(Color.DARK_GRAY);
//		g.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
	}
    
//    public Object clone() {
//        try {
//            Cable obj = (Cable)super.clone();
//            obj.map = (HashMap)map.clone();
//            return obj;
//        }
//        catch (CloneNotSupportedException e) {
//            throw new InternalError(e.toString());
//        }
//    }
    
    public String toString() {
        return "[" + beginArray[0] + ", " + beginArray[1] + ", " + beginArray[2] + "]-[" + endArray[0] + ", " + endArray[1] + ", " + endArray[2] + "]";
    }
    
}
