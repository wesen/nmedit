package nomad.gui.model.component.builtin;

import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nomad.gui.model.UIFactory;
import nomad.gui.model.component.AbstractControlPort;
import nomad.gui.model.component.AbstractUIControl;
import nomad.gui.model.component.builtin.implementation.ButtonGroup;
import nomad.gui.model.property.BoolProperty;
import nomad.gui.model.property.ImageTextProperty;
import nomad.gui.model.property.PointProperty;
import nomad.gui.model.property.Property;
import nomad.misc.ImageTracker;
import nomad.model.descriptive.DParameter;

public class ButtonGroupUI extends AbstractUIControl {
	
	private ButtonGroupPort bgp = null;

	public ButtonGroupUI(UIFactory factory) {
		super(factory);
		bgp = new ButtonGroupPort(factory.getImageTracker());
		setComponent(bgp.btngroup);
	}

	protected void installSizeProperty(boolean install) {	
		// don't want to install the default size property
		if (install) {
			registerProperty(bgp.btnsize);
		} else {
			unregisterProperty(bgp.btnsize);
		}
	}
	
	protected void registerPorts() {
		registerControlPort(bgp);
	}

	protected void registerPortProperties(AbstractControlPort port, boolean install) {
		if (install) {
			//registerProperty(bgp.btnsize);
			registerProperty(bgp.pOrientation);
		} else {
			//unregisterProperty(bgp.btnsize);
			unregisterProperty(bgp.pOrientation);
		}
		super.registerPortProperties(port, install);
	}

	public String getName() {
		return "ButtonGroup";
	}
	
	private class ButtonGroupPort extends AbstractControlPort {

		private DParameter param = null;
		private ButtonGroup btngroup = new ButtonGroup();
		private ButtonSizeProperty btnsize = new ButtonSizeProperty();
		private ArrayList imageTextProperties = new ArrayList();
		private BoolProperty pOrientation = new OrientationProperty();
		private ImageTracker imageTracker = null;

		public ButtonGroupPort(ImageTracker imageTracker) {
			super(ButtonGroupUI.this);
			this.imageTracker = imageTracker;
			pOrientation.setDefaultBooleanValue(true);
			btngroup.addChangeListener(
				new ChangeListener() {
					public void stateChanged(ChangeEvent arg0) {
						firePortValueUpdateEvent();
					}
				}
			);
		}

		public DParameter getParameterInfoAdapter() {
			return param;
		}

		public void setParameterInfoAdapter(DParameter parameterInfo) {
			if (param==parameterInfo)
				return ;
			btngroup.setParamInfo(parameterInfo);
			

			for (int i=0;i<imageTextProperties.size();i++) {
				unregisterProperty( (Property)imageTextProperties.get(i) );
			}
			imageTextProperties = new ArrayList();
			
			param = parameterInfo;
			/* update component */
			if (param!=null) {
				if (param.getNumStates()<=2) {
					btngroup.setButtonCount(1);
					btngroup.setButtonDisplay(0, param.getName());
				}
				else {
					btngroup.setButtonCount(param.getNumStates());
					for (int i=0;i<param.getNumStates();i++) {
						btngroup.setButtonDisplay(i, param.getFormattedValue(i));
					}
				}
				
				btngroup.setValue(param.getDefaultValue());
				
			} else {
				btngroup.setButtonCount(1);
			}

			for (int i=0;i<btngroup.getButtonCount();i++) {
				Property p = new MyImageTextProperty(i);
				imageTextProperties.add(p);
				registerProperty(p);
			}
			
			/* fire update event */
			paramPortChanged(); 
		}
		
		private class OrientationProperty extends BoolProperty {

			public OrientationProperty() {
				super("orient.hrz", ButtonGroupUI.this);
				setDefaultBooleanValue(true);
			}

			protected Object getInternalValue() {
				return new Boolean(btngroup.hasHorizontalOrientation());
			}

			protected void setInternalValue(Object value) {
				btngroup.setHorizontalOrientation(((Boolean)value).booleanValue());
			}

			public String getId() {
				return "bg.orientation";
			}
			
		}
		
		private class MyImageTextProperty extends ImageTextProperty {
			private int btnIndex = 0;
			private String text = "";
			private String defaultText = "";
			
			public MyImageTextProperty(int index) {
				super("text."+index, ButtonGroupUI.this, imageTracker);
				btnIndex = index;
				setInternalValue("");
				defaultText = text;
			}

			protected Object getInternalValue() {
				return text;
			}

			public String getId() {
				return "btn.text."+btnIndex;
			}

			protected void setInternalValue(Object value) {
				this.text = (String)value;
				
				if (text.length()==0) {
					if (param==null)
						this.text=""+btnIndex;
					else {
						if (param.getNumStates()<=2)
							this.text = param.getName();
						else
							this.text = param.getFormattedValue(btnIndex);
					}
					btngroup.setButtonDisplay(btnIndex, text);
				} else {
					
					Image image = getImage();

					if (image!=null)
						btngroup.setButtonDisplay(btnIndex, image);
					else
						btngroup.setButtonDisplay(btnIndex, text);
				}
			}
			
			public Object getDefaultValue() {
				return defaultText;
			}
			
		}
		
		public int getParameterValue() {
			return btngroup.getValue();
		}

		public void setParameterValue(int value) {
			btngroup.setValue(value);
		}
		
		private class ButtonSizeProperty extends PointProperty {
			public ButtonSizeProperty() {
				super("size", ButtonGroupUI.this);
			}

			protected Object getInternalValue() {
				return getPointFromDimension(btngroup.getButtonSize());
			}

			protected void setInternalValue(Object value) {
				btngroup.setButtonSize(getDimensionFromPoint((Point)value));				
			}

			public String getId() {
				return "btn.size";
			}
		}
		
	}
	
/*
	
	private ButtonGroup btngroup = null;
	private PointProperty pbtnSize = null;
	private BoolProperty pbtnHorizontal = null;
	private BoolProperty pbtnInverse = null;
	public static ImageTracker theImageTracker = null;

	public ButtonGroupUI() {
		BtnSizeChangeListener cl = new BtnSizeChangeListener();
		
		btngroup = new ButtonGroup();
		btngroup.addComponentListener(cl);
		btngroup.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					firePortValueChangedEvent(new PortValueChangedEvent(ButtonGroupUI.this, 0));
				}
			}
		);
		
		// remove size property because the size depends only on btnSize
		this.installSizeProperty(false);
		// install button size property
		pbtnSize = new PointProperty("buttonsize");
		Dimension d = btngroup.getButtonSize();
		pbtnSize.setValue(d.width, d.height, null);
		pbtnSize.addChangeListener(cl);
		addProperty(pbtnSize);

		pbtnHorizontal = new BoolProperty("horizontal", btngroup.hasHorizontalOrientation());
		pbtnHorizontal.addChangeListener(new HrzChangeListener());
		pbtnInverse = new BoolProperty("inverse", btngroup.hasInverseOrder());
		pbtnInverse.addChangeListener(new InvChangeListener());
		
		addProperty(pbtnHorizontal);
		addProperty(pbtnInverse);
		
		getDefaultPort().addChangeListener(new ParamSourceChanged());
		setComponent(btngroup);
	}
	
	public void setImageTracker(ImageTracker imageTracker) {
		this.imageTracker = imageTracker;
	}

	public String getName() {
		return "knob";
	}

	private class HrzChangeListener implements ChangeListener {
		public void stateChanged(ChangeEvent event) {
			btngroup.setHorizontalOrientation(pbtnHorizontal.getBooleanValue());			
		}
	}

	private class InvChangeListener implements ChangeListener {
		public void stateChanged(ChangeEvent event) {
			btngroup.setInverseOrder(pbtnInverse.getBooleanValue());
		}
	}
	
	private class BtnSizeChangeListener implements ChangeListener, ComponentListener {
		
		// ChangeListener :
		
		public void stateChanged(ChangeEvent event) {
			btngroup.setButtonSize(pbtnSize.getPointAsDimension());
		}

		// ComponentListener :
		
		public void componentResized(ComponentEvent event) {
			Dimension d = btngroup.getButtonSize();
			pbtnSize.setValue(d.width, d.height, this);
		}

		public void componentMoved(ComponentEvent event) {
			//
		}

		public void componentShown(ComponentEvent event) {
			//
		}

		public void componentHidden(ComponentEvent event) {
			// 
		}
	}

	private class ParamSourceChanged implements ChangeListener {
		
		public IndexedITP[] properties = new IndexedITP[]{}; 
		
		public void stateChanged(ChangeEvent event) {
			DParameter param = getDefaultPort().getSelectedParameter();
			btngroup.setParamInfo(param);
			if (param!=null) {
				// update
				btngroup.setButtonCount(param.getNumStates()<=2?1:param.getNumStates());
				
				int count = btngroup.getButtonCount();
				int pcount = properties.length;
				while (pcount>count && pcount>=0) {
					ButtonGroupUI.this.removeProperty(properties[pcount-1]);
					pcount--;
				}
				if (pcount<0)
					pcount=0;
				IndexedITP[] tmp = new IndexedITP[count];
				for (int i=0;i<Math.min(properties.length, tmp.length);i++)
					tmp[i]=properties[i];
				properties = tmp;
				while (pcount<count && pcount>=0) {
					properties[pcount] = new IndexedITP("btn."+(pcount),imageTracker,pcount);
					properties[pcount].setValue(btngroup.getButtonText(pcount));
					ButtonGroupUI.this.addProperty(properties[pcount]);
					pcount++;
				}
			}
		}
	}
	
	public int getDefaultPortValue() {
		return btngroup.getValue();
	}
	
	private class IndexedITP extends ImageTextProperty {
		private int index = -1;
		public IndexedITP(String name, ImageTracker imageTracker, int index) {
			super(name, imageTracker);
			this.index = index;
			this.addChangeListener(new BtnChangeEvent());
		}
		
		class BtnChangeEvent implements ChangeListener {
			public void stateChanged(ChangeEvent event) {
				Image img = getImage();
				if (img!=null)
					btngroup.setButtonDisplay(index, img);
				else
					btngroup.setButtonDisplay(index, getValueString());
			}
		}
	}
	
*/
	
}
