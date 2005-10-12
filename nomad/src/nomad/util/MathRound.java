package nomad.util;

public final class MathRound {

	/**
	 * rounds until digit '-to'
	 */
	public static double round(double d, int to) {
		if (to == 0)
			return Math.round(d);

		double norm = Math.pow(10.0, -to);
		return Math.round(d*norm) / norm;
	}
	
	public static String doubleToStr(double d) {
		double truncated = (int) d;
		if (d==truncated)
			return Integer.toString((int)d);
		else
			return Double.toString(d);
	}

}
