package nomad.gui.model.component.builtin;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nomad.gui.model.UIFactory;
import nomad.gui.model.component.AbstractControlPort;
import nomad.gui.model.component.AbstractUIControl;
import nomad.gui.model.component.builtin.implementation.SimpleSlider;
import nomad.gui.model.component.builtin.implementation.SliderArray;
import nomad.model.descriptive.DParameter;

/**
 * A user interface containing 16 ports using the {@link nomad.gui.model.component.builtin.implementation.SimpleSlider}
 * 
 * @author Christian Schneider
 */
public class SliderArrayUI extends AbstractUIControl {
	
	private SliderArray sarray = new SliderArray();

	public SliderArrayUI(UIFactory factory) {
		super(factory);
		setComponent(sarray);
	}
	
	protected void registerPorts() {
		SimpleSlider[] sliders = sarray.getSliders();
		
		for (int i=0;i<sliders.length;i++) {
			SliderControlPort port = new SliderControlPort(sliders[i]);
			registerControlPort(port);
		}
	}

	public String getName() {
		return "SliderArray";
	}
	
	public SliderArray getSliderArray() {
		return sarray;
	}

	private class SliderControlPort extends AbstractControlPort {

		private DParameter param = null;
		private SimpleSlider slider = null;

		public SliderControlPort(SimpleSlider slider) {
			super(SliderArrayUI.this);
			this.slider = slider;
			slider.setSize(16, 100);
			slider.setLocation(0,0);
			
			slider.addChangeListener(
					new ChangeListener() {
						public void stateChanged(ChangeEvent arg0) {
							firePortValueUpdateEvent();
							if (param!=null)
								SliderControlPort.this.slider.setToolTipText(param.getName()+":"+getFormattedParameterValue());
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
