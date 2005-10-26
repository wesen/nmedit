package plugin.classictheme;

import nomad.gui.model.UIFactory;
import nomad.gui.model.component.builtin.ButtonGroupUI;
import nomad.gui.model.component.builtin.DefaultConnectorUI;
import nomad.gui.model.component.builtin.DefaultControlUI;
import nomad.gui.model.component.builtin.DefaultLabelUI;

public class ClassicThemeFactory extends UIFactory {
	public ClassicThemeFactory() {
		DefaultLabelUI.theImageTracker = this.getImageTracker();
		ButtonGroupUI.theImageTracker = this.getImageTracker();
		
		installUIClass(DefaultLabelUI.class);
		installUIClass(DefaultControlUI.class);
		installUIClass(DefaultConnectorUI.class);
		installUIClass(ButtonGroupUI.class);
		installDefaultControl(DefaultControlUI.class);
		installDefaultOptionControl(ButtonGroupUI.class);
		installDefaultLabel(DefaultLabelUI.class);
		installDefaultConnector(DefaultConnectorUI.class);
	}

	public String getUIDescriptionFileName() {
		return "src/plugin/classictheme/ui.xml";
	}
}
