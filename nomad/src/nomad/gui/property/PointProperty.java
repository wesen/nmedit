package nomad.gui.property;

import java.awt.Point;


public class PointProperty extends Property {

	public PointProperty() {
		super();
	}
	
	/**
	 * Allows either instances of class Point or a String of the form  x, y where
	 * x and y are numbers.  
	 */
	protected Object checkAndNormalizeValue(Object value) throws InvalidValueException {
		if (value==null)
			throw new InvalidValueException("Value must not be 'null'.");

		if (value instanceof String) {
			String[] pieces = ((String)value).split(",");
			if (pieces.length!=2)
				throw new InvalidValueException("Unrecognized string '"+value+"'.");
			
			try {
				int x = Integer.parseInt(pieces[0].trim());
				int y = Integer.parseInt(pieces[1].trim());
				return new Point(x, y);
			} catch (NumberFormatException cause) {
				throw new InvalidValueException("Unrecognized string '"+value+"'.", cause);
			}
		}
		else if (value instanceof Point)
			return new PointWrap((Point)value);

		throw new InvalidValueException("Unrecognized value '"+value+"'.");
	}

	public Object getDefaultValue() {
		return new PointWrap(0,0);
	}

	public Point getPoint() {
		return (Point) getValue();
	}

	public void setValue(Point p, Object sender) {
		try {
			super.setValue(new PointWrap(p), sender);
		} catch (InvalidValueException e) {
			e.printStackTrace(); // should never occure
		}
	}
	
	public void setValue(int x, int y, Object sender) {
		setValue(new PointWrap(x, y), sender);
	}

	final class PointWrap extends Point {
		public PointWrap(Point point) {
			super(point);
		}
		public PointWrap(int x, int y) {
			super(x, y);
		}
		public String toString() {
			return x+","+y;
		}
	}
}
