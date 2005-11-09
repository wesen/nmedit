package nomad.gui.model.property;

import java.awt.Dimension;
import java.awt.Point;

import nomad.gui.model.component.AbstractUIComponent;

/**
 * The abstract internal operations must use java.awt.Point as transfer type for value.
 * The property allows objects of type string that have the form <code>&lt;int&gt;, &lt;int&gt; where
 * the first integer is the x value and the second one the y value
 * 
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

	/**
	 * Returns the point
	 * @return the point
	 */
	public Point getValueAsPoint() {
		return (Point) getValue();
	}
	
	/**
	 * Returns the value as dimension, with <code>point.x=dimension.width</code> and
	 * <code>point.y=dimension.height</code>.
	 * @return the value as dimension, with x value as width and y value as height
	 */
	public Dimension getValueAsDimension() {
		Point p = getValueAsPoint();
		return new Dimension(p.x, p.y);
	}

	/**
	 * Returns a point from given dimension with
	 * <code>point.x=dimension.width</code> and
	 * <code>point.y=dimension.height</code>.
	 * @param d the dimension
	 * @return the point
	 */
	public static Point getPointFromDimension(Dimension d) {
		return new Point(d.width, d.height);
	}

	/**
	 * Returns a dimension from given point with
	 * <code>point.x=dimension.width</code> and
	 * <code>point.y=dimension.height</code>.
	 * @param p the point
	 * @return the dimension
	 */
	public static Dimension getDimensionFromPoint(Point p) {
		return new Dimension(p.x, p.y);
	}

	/**
	 * Returns an object (java.awt.Point) from the string representation
	 * or null if either the representation is null or the representation
	 * has not the form <code>&lt;int&gt;, &lt;int&gt;
	 */
	public Object parseString(String representation) {
		if (representation==null)
			return null;
		
		if (!representation.matches("[\\+,-]?\\d+,[\\+,-]?\\d+"))
			return null;
		
		String[] splitted = representation.split(",");
		return new Point(Integer.parseInt(splitted[0]),Integer.parseInt(splitted[1]));
	} 

}
