package net.sf.nmedit.jpatch.randomizer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.TransferHandler;

public class Variation extends JComponent { 
	/**
	 * 
	 */
	private static final long serialVersionUID = -3543257101198489340L;
	int values[] = null;
	
	public Variation(){
		construct();
	}
	
	public Variation(int size) {
		
		if (size > 0) {
			values = new int[size];
			for (int i = 0; i < size; i ++)
			{
				values[i]=(int)(128*Math.random() );
				//System.out.println(values[i]);
			}
		}
		construct();
	}
	
	public Variation(Variation v,double range,double probability) {
		values = new int[v.getNbValues()];
		
		mutate(v,range,probability);		
		construct();
	}
	
	private void construct(){
		setTransferHandler(new VariationTransferHandler());
		
		VariationListener listener = new VariationListener();
		addMouseListener(listener);
		addMouseMotionListener(listener);
		
		setPreferredSize(new Dimension(50,50));
		setMaximumSize(new Dimension(50,50));
		setMinimumSize(new Dimension(50,50));
		
	}
	
	public void mutate(Variation refVar,double range,double probability )
	{
		if (values == null)
		{
			values = new int[refVar.getNbValues()];
		}
		
		
		for (int i = 0 ; i  < values.length ; i++)
		{
			
			if (Math.random() < probability)
			{
				double amplitude = 127*2*range;
				int offset = (int)(Math.random()* amplitude
							-amplitude/2);
				int val = refVar.getValues()[i] + offset;
				values[i] = val < 0 ? 0: val > 128 ? 127:val;
			} else {
				values[i] = refVar.getValues()[i];
			}
		}	
	}
	
	public void randomize(int size)
	{
		if (values == null)
		{
			values = new int[size];
		}
		
		for (int i = 0 ; i  < values.length ; i++)
		{
				values[i] = (int)(Math.random()*127);
		}
	}
	
	public int getNbValues(){
		return values.length;
	}
	
	public int[] getValues() {
		return values;
	}
	
	public Color colorValue(int hue){
		
		//return Color.getHSBColor(hue/128f, hue/128f, hue/128f);
		//return Color.getHSBColor(hue/128f, hue/128f,.8f);
		//return Color.getHSBColor(hue/128f, 0f, hue/128f);
		return Color.getHSBColor(.2f*hue/128f,1f-hue/128f,.8f); // ok variation rouge
		
		//return Color.getHSBColor(.3f*hue/128f,.5f+.5f*hue/128f,.8f);
		
	}

	private Polygon poly = null;
	
	private Polygon getPolygon()
	{
		//if (poly==null){ 
			if (values == null) return null;
			
			poly = new Polygon();
			
			int w = getWidth()/8;
			int h = getHeight()/8;
			
			poly.addPoint(w/2,h/2);
			
			int prevX = w/2;
			int prevY = h/2;
			double prevAngle = 0;
			for (int i = 0; i < values.length ; i += 2)
			{
				double angle = 2*Math.PI* values[i]/512f+prevAngle;
				double amplitude = values[i+1]/2;
				int x = (int)(prevX+amplitude*Math.cos(angle));
				int y = (int)(prevY+amplitude*Math.sin(angle));
				poly.addPoint(x,y);
				prevX = x;
				prevY = y;
				prevAngle = angle;
			}
			
			Rectangle bound = poly.getBounds();
			
			// translate the poly to 0,0
			if (bound.x < 0) poly.translate(-bound.x, 0);
			if (bound.y < 0) poly.translate(0,-bound.y);
			
			// scale it
			double scaleX = bound.width/((double)getWidth()-10d);
			double scaleY = bound.height/((double)getHeight()-10d);
			
			//System.out.println(scaleX+" "+scaleY);
			for (int i=0; i < poly.xpoints.length;  i++)
			{
				poly.xpoints[i] /= scaleX;
				poly.xpoints[i] += 5;
			}
			for (int i=0; i < poly.ypoints.length;  i++)
			{
				poly.ypoints[i] /= scaleY;
				poly.ypoints[i] += 5;
			}
			//System.out.println(p.getBounds());
		//}
		return poly;
		
	}

	double angles[] = new double[64];
	private Polygon getPolygon2()
	{
		//if (poly==null){ 
		if (values == null) return null;
		
		poly = new Polygon();
		
		int w = getWidth()/8;
		int h = getHeight()/8;
		
		poly.addPoint(w/2,h/2);
		
		int prevX = w/2;
		int prevY = h/2;
		double prevAngle = 0;
		for (int i = 0; i < values.length ; i += 2)
		{ 
			double angle = 2*Math.PI* values[i]/512f+prevAngle;
			angles[i] = 2*Math.PI* values[i]/512f;  
			double amplitude = values[i+1]/2;
			int x = (int)(prevX+amplitude*Math.cos(angle));
			int y = (int)(prevY+amplitude*Math.sin(angle));
			poly.addPoint(x,y);
			prevX = x;
			prevY = y;
			prevAngle = angle;
		}
		
		Rectangle bound = poly.getBounds();
		
		// translate the poly to 0,0
		if (bound.x < 0) poly.translate(-bound.x, 0);
		if (bound.y < 0) poly.translate(0,-bound.y);
		
		// scale it
		double scaleX = bound.width/((double)getWidth()-10d);
		double scaleY = bound.height/((double)getHeight()-10d);
		
		//System.out.println(scaleX+" "+scaleY);
		for (int i=0; i < poly.xpoints.length;  i++)
		{
			poly.xpoints[i] /= scaleX;
			poly.xpoints[i] += 5;
		}
		for (int i=0; i < poly.ypoints.length;  i++)
		{
			poly.ypoints[i] /= scaleY;
			poly.ypoints[i] += 5;
		}
		//System.out.println(p.getBounds());
		//}
		return poly;
		
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
				
		Graphics2D g2 = (Graphics2D) g;
        //g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
//        if (values != null){
//        	for (int x = 0 ; x < 8 ; x++)
//	        {
//				for (int y = 0 ; y < 8 ; y++)
//	        	{
//					int w = getWidth()/8;
//					int h = getHeight()/8;
//	        		//Color c =  mutator.getPalette()[mutator.getColors()[series][x+y*8]];
//	        		Color c = colorValue(getValues()[x+y*8]);
//	        		g2.setColor(c);
//	            	g2.fillRect(x*w,y*h,w,h);
//	         
//	        		//g.fillRect(0,0,getWidth(),getHeight());
//	        	} 	            	
//	        }
//        }
		g2.setColor(Color.black);
		
		if (values != null){
			Polygon p = getPolygon2();
			g2.setColor(Color.black);
			g2.fillRect(0,0,getWidth()-1,getHeight()-1);
			for (int i = 0 ; i < p.xpoints.length-1 ; i ++){
				if (i%2 == 0){
					g2.setColor(colorValue((int )(angles[i]*256/Math.PI)));
				
					//g2.setStroke(new StrokeWidthManager());
					//System.out.println(angles[i]*256/Math.PI + " a ");
				}
				g2.drawLine(p.xpoints[i],p.ypoints[i],p.xpoints[i+1],p.ypoints[i+1]);
				
			}
		}
		
		g2.setColor(Color.DARK_GRAY);
		g2.drawRect(0,0,getWidth()-1,getHeight()-1);
    }

	public void setValues(int[] values) {
		this.values = values;
	}
	
	public class VariationListener implements MouseMotionListener, MouseListener{

		private MouseEvent firstMouseEvent = null;
		
		public void mouseDragged(MouseEvent e) {
			if (firstMouseEvent != null){				
	//			If they are holding down the control key, COPY rather than MOVE
	            int ctrlMask = InputEvent.CTRL_DOWN_MASK;
	            int action = ((e.getModifiersEx() & ctrlMask) == ctrlMask) ?
	                  TransferHandler.COPY : TransferHandler.MOVE;
	
	            int dx = Math.abs(e.getX() - firstMouseEvent.getX());
	            int dy = Math.abs(e.getY() - firstMouseEvent.getY());
	            //Arbitrarily define a 5-pixel shift as the
	            //official beginning of a drag.
	            if (dx > 2 || dy > 2) {
	            	//System.out.println("drag");
	                //This is a drag, not a click.
	                JComponent c = (JComponent)e.getSource();
	                TransferHandler handler = c.getTransferHandler();
	                //Tell the transfer handler to initiate the drag.
	                handler.exportAsDrag(c, firstMouseEvent, action);
	                firstMouseEvent = null;
	            }
			}
		}

		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		public void mousePressed(MouseEvent e) {
			System.out.println("ee");
			firstMouseEvent = e;
	        e.consume();
		}

		public void mouseReleased(MouseEvent arg0) {
			firstMouseEvent = null;
			
		}
		
	}
} 
