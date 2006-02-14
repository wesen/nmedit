package plugin.classictheme;

import java.awt.Graphics;

import org.nomad.patch.ModuleSection;
import org.nomad.patch.ui.ModuleSectionUI;
import org.nomad.patch.ui.ModuleUI;
import org.nomad.theme.NomadClassicColors;
import org.nomad.theme.UIFactory;
import org.nomad.theme.component.AudioLevelDisplay;
import org.nomad.theme.component.GroupDecoration;
import org.nomad.theme.component.NomadActiveLabel;
import org.nomad.theme.component.NomadButtonArray;
import org.nomad.theme.component.NomadClassicConnector;
import org.nomad.theme.component.NomadClassicKnob;
import org.nomad.theme.component.NomadImageView;
import org.nomad.theme.component.NomadLabel;
import org.nomad.theme.component.NomadResetButton;
import org.nomad.theme.component.NomadVocoderController;
import org.nomad.theme.component.VocoderBandDisplay;
import org.nomad.util.graphics.BackgroundPainter;
import org.nomad.util.graphics.ImageTracker;
import org.nomad.xml.dom.module.DModule;

public class ClassicThemeFactory extends UIFactory {
	public ClassicThemeFactory() {
		installClass(NomadActiveLabel.class,"display.text");
		installClass(NomadButtonArray.class,"button");
		installClass(NomadClassicConnector.class,"connector");
		installClass(NomadClassicKnob.class,"knob");
		installClass(NomadImageView.class,"image");
		installClass(NomadLabel.class,"label");
		installClass(NomadResetButton.class,"knob.reset");
		installClass(VocoderBandDisplay.class,"display.vocoder");
		installClass(NomadVocoderController.class,"display.vocoder.controller");
		installClass(AudioLevelDisplay.class,"display.audiolevel");
		installClass(GroupDecoration.class,"border.groupbox");
	}

	public String getUIDescriptionFileName() {
		return "plugin/classictheme/ui.xml";
	}

	public ModuleUI getModuleGUI(DModule info) {
		ModuleUI gui = super.getModuleGUI(info);
		gui.setBackground(NomadClassicColors.MODULE_BACKGROUND);
		return gui;
	}

	public ModuleSectionUI getModuleSectionUI(ModuleSection moduleSection) {
		return new ClassicModuleSectionGUI(moduleSection, getImageTracker());
	}
	
}

class ClassicModuleSectionGUI extends ModuleSectionUI {
	private static BackgroundPainter bgpainter = null;
	
	public ClassicModuleSectionGUI(ModuleSection moduleSection, ImageTracker itracker) {
		super(moduleSection) ;
        setDoubleBuffered(false);
		if (bgpainter==null) {
			bgpainter = new BackgroundPainter(itracker.getImage("classic-patch-background"));
		}
	}

	public void paintComponent(Graphics g) {
		bgpainter.paintBackground(g, this);
	}
	
}