package plugin.classictheme;

import nomad.gui.ConnectorUI;
import nomad.gui.LabelUI;
import nomad.gui.UIFactory;
import nomad.gui.knob.KnobUI;

public class ClassicThemeFactory extends UIFactory {
	public ClassicThemeFactory() {
		installUIClass(LabelUI.class);
		installUIClass(KnobUI.class);
		installUIClass(ConnectorUI.class);
		installDefaultControl(KnobUI.class);
		installDefaultLabel(LabelUI.class);
		installDefaultConnector(ConnectorUI.class);
	}
}
