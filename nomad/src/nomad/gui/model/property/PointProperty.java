package nomad.gui.model.property;

import java.awt.Dimension;
import java.awt.Point;

import nomad.gui.model.component.AbstractUIComponent;

/**
 * The abstract internal operations must use java.awt.Point as transfer type for value
 * @author Christian Schneider
 */
public abstract class PointProperty extends Property {

	public PointProperty( String displayName, AbstractUIComponent uicomponent) {
		super(displayName, uicomponent);
	}

	public void setValue(Object value) {
		if (value instanceof Dimension)
			setInternalValue(getPointFromDimension((Dimension)value));
		else
			super.setValue(value);
	}
	
	public Object getValue() {
		return new Point((Point) super.getValue()) {
			public String toString() {
				return x+","+y;
			}
		};
	}

	public Point getValueAsPoint() {
		return (Point) getValue();
	}
	
	public Dimension getValueAsDimension() {
		Point p = getValueAsPoint();
		return new Dimension(p.x, p.y);
	}
	
	public static Point getPointFromDimension(Dimension d) {
		return new Point(d.width, d.height);
	}
	
	public static Dimension getDimensionFromPoint(Point p) {
		return new Dimension(p.x, p.y);
	}

	public Object parseString(String representation) {
		if (representation==null)
			return null;
		
		if (!representation.matches("[\\+,-]?\\d+,[\\+,-]?\\d+"))
			return null;
		
		String[] splitted = representation.split(",");
		return new Point(Integer.parseInt(splitted[0]),Integer.parseInt(splitted[1]));
	} 

}
