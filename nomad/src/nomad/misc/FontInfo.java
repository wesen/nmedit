package nomad.misc;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;

public class FontInfo {

	public static Dimension getTextRect(String text, Font font, Component c) {
		if (text==null)
			text="null";
		else if (font==null || c==null)
			return new Dimension(0,0);
		
        FontMetrics fontMetrics= c.getFontMetrics(font);
        Rectangle2D r = fontMetrics.getStringBounds(text, c.getGraphics());
        
        return new Dimension ((int)r.getWidth(), (int)r.getHeight());
	}
	
}
