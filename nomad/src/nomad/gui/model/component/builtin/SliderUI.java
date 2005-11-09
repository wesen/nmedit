package nomad.gui.model.component.builtin;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nomad.gui.model.UIFactory;
import nomad.gui.model.component.AbstractControlPort;
import nomad.gui.model.component.AbstractUIControl;
import nomad.gui.model.component.builtin.implementation.SimpleSlider;
import nomad.model.descriptive.DParameter;

/**
 * A simple slider user interface that can be used to control one parameter.
 * @author Christian Schneider
 */
public class SliderUI extends AbstractUIControl {

	SliderControlPort thePort = null;
	
	public SliderUI(UIFactory factory) {
		super(factory);
		thePort = new SliderControlPort();
		setComponent(thePort.slider);
	}
	
	protected void registerPorts() {
		registerControlPort(thePort);
	}

	public String getName() {
		return "Slider";
	}
	
	public SimpleSlider getSlider() {
		return thePort.slider;
	}

	/* no additional properties
	protected void registerPortProperties(AbstractControlPort port, boolean install) {
		super.registerPortProperties(port, install);
	}*/
	
	private class SliderControlPort extends AbstractControlPort {

		private DParameter param = null;
		private SimpleSlider slider = new SimpleSlider();

		public SliderControlPort() {
			super(SliderUI.this);
			slider.setSize(16, 100);
			slider.setLocation(0,0);
			
			slider.addChangeListener(
					new ChangeListener() {
						public void stateChanged(ChangeEvent arg0) {
							firePortValueUpdateEvent();
							if (param!=null)
								slider.setToolTipText(param.getName()+":"+getFormattedParameterValue());
						}
					}
				);
		}

		public DParameter getParameterInfoAdapter() {
			return param;
		}

		public void setParameterInfoAdapter(DParameter parameterInfo) {
			this.param = parameterInfo;
			/* update knob values */
			if (slider!=null) {
				slider.setMinValue(0);
				slider.setMaxValue(param.getNumStates()-1);
				slider.setValue(param.getDefaultValue());
				
				if (param!=null)
					slider.setToolTipText(param.getName()+":"+getFormattedParameterValue());
			}
			
			/* fire update event */
			paramPortChanged();
		}

		public int getParameterValue() {
			return slider.getValue();
		}

		public void setParameterValue(int value) {
			slider.setValue(value);
		}
		
		public void updateKnobUI() {
			slider.validate();
			slider.repaint();
			slider.updateUI();
		}
		
	}
}
