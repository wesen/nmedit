package plugin.deprecatedclassictheme;

import java.awt.Graphics;

import nomad.graphics.BackgroundPainter;
import nomad.gui.ModuleSectionGUI;
import nomad.gui.model.UIFactory;
import nomad.gui.model.component.builtin.ButtonGroupUI;
import nomad.gui.model.component.builtin.DefaultConnectorUI;
import nomad.gui.model.component.builtin.DefaultControlUI;
import nomad.gui.model.component.builtin.DefaultLabelUI;
import nomad.gui.model.component.builtin.DefaultTextDisplay;
import nomad.gui.model.component.builtin.SliderArrayUI;
import nomad.gui.model.component.builtin.SliderArrayUI14;
import nomad.gui.model.component.builtin.SliderUI;
import nomad.gui.model.component.builtin.VocoderUI;
import nomad.misc.ImageTracker;
import nomad.patch.ModuleSection;

public class ClassicThemeFactory extends UIFactory {
	public ClassicThemeFactory() {
		installUIClass(DefaultLabelUI.class);
		installUIClass(DefaultTextDisplay.class);
		installUIClass(DefaultControlUI.class);
		installUIClass(DefaultConnectorUI.class);
		installUIClass(ButtonGroupUI.class);
		installUIClass(SliderUI.class);
		installUIClass(VocoderUI.class);
		installUIClass(SliderArrayUI.class);
		installUIClass(SliderArrayUI14.class);
		
		DefaultControlUI.resetKnobLook();
		
		installDefaultControl(DefaultControlUI.class);
		installDefaultOptionControl(ButtonGroupUI.class);
		installDefaultLabel(DefaultLabelUI.class);
		installDefaultConnector(DefaultConnectorUI.class);
	}

	public String getUIDescriptionFileName() {
		return "src/plugin/classictheme/ui.xml";
	}

	public ModuleSectionGUI getModuleSectionGUI(ModuleSection moduleSection) {
		return new ClassicModuleSectionGUI(moduleSection, getImageTracker());
	}
	
}

class ClassicModuleSectionGUI extends ModuleSectionGUI {
	private static BackgroundPainter bgpainter = null;
	
	public ClassicModuleSectionGUI(ModuleSection moduleSection, ImageTracker itracker) {
		super(moduleSection) ;
		
		if (bgpainter==null) {
			bgpainter = new BackgroundPainter(
					this, itracker.getImage("classic-patch-background")
				);
		}
	}

	public void paintComponent(Graphics g) {
		bgpainter.paintBackground(g);
	}
	
}