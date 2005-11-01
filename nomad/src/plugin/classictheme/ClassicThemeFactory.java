package plugin.classictheme;

import nomad.gui.model.UIFactory;
import nomad.gui.model.component.builtin.ButtonGroupUI;
import nomad.gui.model.component.builtin.DefaultConnectorUI;
import nomad.gui.model.component.builtin.DefaultControlUI;
import nomad.gui.model.component.builtin.DefaultLabelUI;
import nomad.gui.model.component.builtin.DefaultTextDisplay;
import nomad.gui.model.component.builtin.SliderUI;
import nomad.gui.model.component.builtin.VocoderUI;

public class ClassicThemeFactory extends UIFactory {
	public ClassicThemeFactory() {
		installUIClass(DefaultLabelUI.class);
		installUIClass(DefaultTextDisplay.class);
		installUIClass(DefaultControlUI.class);
		installUIClass(DefaultConnectorUI.class);
		installUIClass(ButtonGroupUI.class);
		installUIClass(SliderUI.class);
		installUIClass(VocoderUI.class);
		
		installDefaultControl(DefaultControlUI.class);
		installDefaultOptionControl(ButtonGroupUI.class);
		installDefaultLabel(DefaultLabelUI.class);
		installDefaultConnector(DefaultConnectorUI.class);
	}

	public String getUIDescriptionFileName() {
		return "src/plugin/classictheme/ui.xml";
	}
}
