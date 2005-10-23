package nomad.gui.property;

import nomad.model.descriptive.DModule;
import nomad.model.descriptive.DParameter;

public class ParamProperty extends Property {

	private DModule info = null;
	private ParamOption[] options = new ParamOption[] {new ParamOption(null)};
	
	public void setModule(DModule info) {
		this.info = info;
		updateOptions();
	}
	
	private void updateOptions() {
		options = new ParamOption[info.getParameterCount()+1];
		for (int i=0;i<info.getParameterCount();i++) 
			options[i] = new ParamOption(info.getParameter(i));
		options[options.length-1] = new ParamOption(null);
	}
	
	public Object[] getOptions() {
		return options;
	}
	
	public DParameter getSelectedParameter() {
		return ((ParamOption) getValue()).param;
	}

	protected Object checkAndNormalizeValue(Object value) throws InvalidValueException {
		if (value instanceof ParamOption) {
			for (int i=0;i<options.length;i++) {
				if (value==options[i]) {
					return value;
				}
			}
		}
		else if (value instanceof DParameter) { 
			for (int i=0;i<options.length;i++) {
				if (value==options[i].param) {
					return options[i];
				}
			}
		}

		throw new InvalidValueException("Unreconized type '"+value+"'.");
	}
	
	public void setParameter(DParameter param) {
		try {
			setValue(param);
		} catch (InvalidValueException e) {
			// should never occure
			e.printStackTrace();
		}
	}


	private class ParamOption {
		DParameter param = null;
		
		public ParamOption(DParameter param) {
			this.param = param;
		}
		
		public String toString() {
			if (param==null)
				return "null";
			else
				return param.getId()+"#"+param.getName();
		}
	}
	
}
