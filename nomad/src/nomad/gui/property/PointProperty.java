package nomad.gui.property;

import java.awt.Point;


public class PointProperty extends Property {

	private final static Point defaultValue = new Point(0,0);

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
			return value;

		throw new InvalidValueException("Unrecognized value '"+value+"'.");
	}

	public Object getDefaultValue() {
		return defaultValue;
	}

	public Point getPoint() {
		return (Point) getValue();
	}
	
	public void setValue(Point p, Object sender) {
		try {
			super.setValue(p, sender);
		} catch (InvalidValueException e) {
			e.printStackTrace(); // should never occure
		}
	}
	
	public void setValue(int x, int y, Object sender) {
		setValue(new Point(x, y), sender);
	}

	public String getStringRepresentation() {
		Point p = getPoint();
		return p.x+","+p.y;
	}
}
