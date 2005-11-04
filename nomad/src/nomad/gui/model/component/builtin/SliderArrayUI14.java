package nomad.gui.model.component.builtin;

import java.awt.Component;

import nomad.gui.model.UIFactory;
import nomad.gui.model.component.builtin.implementation.SliderArray;

public class SliderArrayUI14 extends SliderArrayUI {

	public SliderArrayUI14(UIFactory factory) {
		super(factory);
	}

	protected void setComponent(Component component) {
		((SliderArray) component).setSliderCount(14);
		super.setComponent(component);
	}
	
}
