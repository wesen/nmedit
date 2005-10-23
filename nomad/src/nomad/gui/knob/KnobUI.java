package nomad.gui.knob;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nomad.gui.ControlUI;
import nomad.gui.property.BoolProperty;
import nomad.model.descriptive.DParameter;

public class KnobUI extends ControlUI {

	private JModKnob knob = null;
	private BoolProperty psize = null;
	private BoolProperty pIndicatorButton = null;

	public KnobUI() {
		knob = new JModKnob(JModKnob.SMALL, true, true, 0, 0, 0, 127);
		psize = new BoolProperty();
		psize.addChangeListener(new KnobSizeChangeListener());
		getProperties().putProperty("size:large", psize);
		
		pIndicatorButton = new BoolProperty();
		pIndicatorButton.addChangeListener(new IndicatorButtonChangeListener());
		getProperties().putProperty("indicator", pIndicatorButton);
		
		getDefaultPort().addChangeListener(new ParamSourceChanged());
		setComponent(knob);
	}

	public String getName() {
		return "knob";
	}

	private class KnobSizeChangeListener implements ChangeListener {
		public void stateChanged(ChangeEvent event) {
			if (psize.getBooleanValue())
				knob.setType(JModKnob.LARGE);
			else
				knob.setType(JModKnob.SMALL);
		}
	}
	
	private class ParamSourceChanged implements ChangeListener {
		public void stateChanged(ChangeEvent event) {
			DParameter param = getDefaultPort().getSelectedParameter();
			if (param!=null) {
				knob.setMinValue(0);
				knob.setMaxValue(param.getNumStates()-1);
				knob.setValue(param.getDefaultValue());
				knob.info = param;
			}
		}
	}
	
	private class IndicatorButtonChangeListener implements ChangeListener {
		public void stateChanged(ChangeEvent arg0) {
			knob.indicator = pIndicatorButton.getBooleanValue();
			knob.updateUI();
			knob.repaint();
		}
	}
	
}
