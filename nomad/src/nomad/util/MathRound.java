package nomad.util;

public final class MathRound {

	/**
	 * rounds d until digit '-to'. For example d=123, to=2 returns 120.
	 * If d=123.456 and to=-2 then 123.45 is returned.
	 * 
	 * @param d Value to round
	 * @param to decimal position
	 * @return rounded value of d
	 */
	public static double round(double d, int to) {
		if (to == 0)
			return Math.round(d);

		double norm = Math.pow(10.0, -to);
		return Math.round(d*norm) / norm;
	}
	
	/**
	 * Returns the value of d as a String. If d is a natural number,
	 * it is returned without decimal point.
	 * @param d value that should be converted to a string representation
	 * @return d as string
	 */
	public static String doubleToStr(double d) {
		double truncated = (int) d;
		if (d==truncated)
			return Integer.toString((int)d);
		else
			return Double.toString(d);
	}

}
