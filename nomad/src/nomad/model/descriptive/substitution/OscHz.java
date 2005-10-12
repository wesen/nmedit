package nomad.model.descriptive.substitution;

import nomad.util.MathRound;

public class OscHz extends Substitution {

	public OscHz() {
		super();
	}

	public String valueToString(int value) {
		double aFloat = 440.0d * Math.pow(2.0d, (value-69)/12);

		if (aFloat<10)
			return ""+MathRound.round(aFloat, -2)+" Hz";
		
		else if (aFloat<100)
			return ""+MathRound.round(aFloat, -1)+" Hz";
				
		else if (aFloat<1000)
			return ""+MathRound.round(aFloat,  0)+" Hz";
					
		else if (aFloat<1000)
			return ""+MathRound.round(aFloat/1000.0, -2)+" kHz";
		
		else
			return ""+MathRound.round(aFloat/1000.0, -2)+" kHz";
	}

}
