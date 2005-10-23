package nomad.gui.property;

import nomad.model.descriptive.DModule;
import nomad.model.descriptive.DParameter;

public class ParamProperty extends Property {

	private DModule info = null;
	private DParameter[] options = new DParameter[] {};
	private String[] names = new String[] {"null"};
	
	public void setModule(DModule info) {
		this.info = info;
		updateOptions();
	}
	
	private void updateOptions() {
		options = new DParameter[info.getParameterCount()];
		names = new String[info.getParameterCount()+1];
		for (int i=0;i<info.getParameterCount();i++) {
			DParameter param = info.getParameter(i);
			options[i] = param;
			names[i] = paramToString(param);
		}
		names[names.length-1] = "null";
	}
	
	public Object[] getOptions() {
		return names;
	}
	
	public DParameter getSelectedParameter() {
		String value = (String) this.getValue();
		if (value!=null && !value.equals("null")) {
			for (int i=0;i<names.length;i++)
				if (names[i].equals(value))
					return options[i];
		}
		return null;
	}
	
	private String paramToString(DParameter param) {
		return param.getId()+"#"+param.getName();
	}
	
	public void setParameter(DParameter param) {
		for (int i=0;i<options.length;i++)
			if (options[i]==param) {
				try {
					setValue(paramToString(param));
				} catch (InvalidValueException e) {
					// should never occure
					e.printStackTrace();
				}
				break;
			}
	}
	
}
