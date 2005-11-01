package nomad.gui.model.component.builtin;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nomad.gui.model.UIFactory;
import nomad.gui.model.component.AbstractControlPort;
import nomad.gui.model.component.AbstractUIControl;
import nomad.gui.model.component.builtin.implementation.JModKnob;
import nomad.gui.model.property.BoolProperty;
import nomad.model.descriptive.DParameter;

public class DefaultControlUI extends AbstractUIControl {

	KnobControlPort thePort = null;
	
	public DefaultControlUI(UIFactory factory) {
		super(factory);
		thePort = new KnobControlPort();
		setComponent(thePort.knob);
	}
	
	protected void installSizeProperty(boolean install) {	
		// don't want to install the size property
	}
	
	protected void registerPorts() {
		registerControlPort(thePort);
	}

	public String getName() {
		return "Default Control";
	}

	protected void registerPortProperties(AbstractControlPort port, boolean install) {
		super.registerPortProperties(port, install);
		if (port instanceof KnobControlPort) // alwas true
			((KnobControlPort)port).registerAdditionalProperties(install);
	}
	
	private class KnobControlPort extends AbstractControlPort {

		private DParameter param = null;
		private JModKnob knob = new JModKnob(JModKnob.SMALL, true, true, 0, 0, 0, 127);
		private IndicatorProperty ip = new IndicatorProperty();
		private KnobSizeProperty ksp = new KnobSizeProperty();

		public KnobControlPort() {
			super(DefaultControlUI.this);
			knob.addChangeListener(
					new ChangeListener() {
						public void stateChanged(ChangeEvent arg0) {
							firePortValueUpdateEvent();

							if (param!=null)
								knob.setToolTipText(param.getName()+":"+getFormattedParameterValue());
						}
					}
				);
			ip.setDefaultBooleanValue(true);
			ksp.setDefaultBooleanValue(true);
			knob.setType(booleanToKnobType(ksp.getDefaultBooleanValue()));
		}
		
		private int booleanToKnobType (boolean small) {
			return small ? JModKnob.SMALL : JModKnob.LARGE;
		}
		
		private boolean knobTypeToBoolean(int type) {
			return type==JModKnob.SMALL;
		}
		
		public void registerAdditionalProperties(boolean install) {
			if (install) {
				registerProperty(ip);
				registerProperty(ksp);
			} else {
				unregisterProperty(ip);
				unregisterProperty(ksp);
			}
		}

		public DParameter getParameterInfoAdapter() {
			return param;
		}

		public void setParameterInfoAdapter(DParameter parameterInfo) {
			this.param = parameterInfo;
			/* update knob values */
			if (knob!=null) {
				knob.setMinValue(0);
				knob.setMaxValue(param.getNumStates()-1);
				knob.setValue(param.getDefaultValue());
				knob.info = param;
				
				if (param!=null)
					knob.setToolTipText(param.getName()+":"+getFormattedParameterValue());
			}
			
			/* fire update event */
			paramPortChanged();
		}

		public int getParameterValue() {
			return knob.getValue();
		}

		public void setParameterValue(int value) {
			knob.setValue(value);
		}
		
		public void updateKnobUI() {
			knob.validate();
			knob.repaint();
			knob.updateUI();
		}
		
		private class IndicatorProperty extends BoolProperty {

			public IndicatorProperty() {
				super("indicator", DefaultControlUI.this);
			}

			protected Object getInternalValue() {
				return new Boolean(knob.indicator);
			}

			protected void setInternalValue(Object value) {
				knob.indicator = ((Boolean)value).booleanValue();
				updateKnobUI();
			}

			public String getId() {
				return "knob.indicator";
			}
			
		}

		private class KnobSizeProperty extends BoolProperty {
			public KnobSizeProperty() {
				super("knob.small", DefaultControlUI.this);
			}

			protected Object getInternalValue() {
				return new Boolean(knobTypeToBoolean(knob.getType()));
			}

			protected void setInternalValue(Object value) {
				knob.setType(booleanToKnobType(((Boolean) value).booleanValue()));
				updateKnobUI();
			}

			public String getId() {
				return "knob.size";
			}
	
		}
		
	}
	
	
	
	
	/*
	private JModKnob knob = null;
	private BoolProperty psize = null;
	private BoolProperty pIndicatorButton = null;

	public DefaultControlUI() {
		knob = new JModKnob(JModKnob.SMALL, true, true, 0, 0, 0, 127);
		psize = new BoolProperty("knobsize");
		psize.addChangeListener(new KnobSizeChangeListener());
		addProperty(psize);

		pIndicatorButton = new BoolProperty("indicator");
		pIndicatorButton.addChangeListener(new IndicatorButtonChangeListener());
		addProperty(pIndicatorButton);
		
		knob.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					firePortValueChangedEvent(new PortValueChangedEvent(DefaultControlUI.this, 0));
				}
			}
		);
		
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
		public void stateChanged(ChangeEvent event) {
			knob.indicator = pIndicatorButton.getBooleanValue();
			knob.updateUI();
			knob.repaint();
		}
	}

	public int getDefaultPortValue() {
		return knob.getValue();
	}
	*/
}
