package plugin.classictheme;

import net.sf.nmedit.nomad.main.background.Background;
import net.sf.nmedit.nomad.main.background.BackgroundFactory;
import net.sf.nmedit.nomad.patch.ui.ModuleSectionUI;
import net.sf.nmedit.nomad.patch.ui.ModuleUI;
import net.sf.nmedit.nomad.patch.virtual.VoiceArea;
import net.sf.nmedit.nomad.theme.NomadClassicColors;
import net.sf.nmedit.nomad.theme.UIFactory;
import net.sf.nmedit.nomad.theme.component.AudioLevelDisplay;
import net.sf.nmedit.nomad.theme.component.GroupDecoration;
import net.sf.nmedit.nomad.theme.component.NomadActiveLabel;
import net.sf.nmedit.nomad.theme.component.NomadButtonArray;
import net.sf.nmedit.nomad.theme.component.NomadClassicConnector;
import net.sf.nmedit.nomad.theme.component.NomadClassicKnob;
import net.sf.nmedit.nomad.theme.component.NomadLabel;
import net.sf.nmedit.nomad.theme.component.NomadResetButton;
import net.sf.nmedit.nomad.theme.component.NomadVocoderController;
import net.sf.nmedit.nomad.theme.component.VocoderBandDisplay;
import net.sf.nmedit.nomad.xml.dom.module.DModule;


public class ClassicThemeFactory extends UIFactory {
	public ClassicThemeFactory() {
		installClass(NomadActiveLabel.class,"display.text");
		installClass(NomadButtonArray.class,"button");
		installClass(NomadClassicConnector.class,"connector");
		installClass(NomadClassicKnob.class,"knob");
		installClass(NomadLabel.class,"label");
		installClass(NomadResetButton.class,"knob.reset");
		installClass(VocoderBandDisplay.class,"display.vocoder");
		installClass(NomadVocoderController.class,"display.vocoder.controller");
		installClass(AudioLevelDisplay.class,"display.audiolevel");
		installClass(GroupDecoration.class,"border.groupbox");
	}

	public String getUIDescriptionFileName() {
		return "plugin/classictheme/theme.xml";
	}

	public ModuleUI getModuleGUI(DModule info) {
		ModuleUI gui = super.getModuleGUI(info);
		gui.setBackground(NomadClassicColors.MODULE_BACKGROUND);
		return gui;
	}

    private Background classicBackground = null;
    
	public ModuleSectionUI getModuleSectionUI(VoiceArea moduleSection) {
        
        if (classicBackground==null)
        {
            classicBackground =
                BackgroundFactory.createTiledBackground(getImageTracker().getImage("classic-patch-background"));
        }
        ModuleSectionUI msui = super.getModuleSectionUI(moduleSection);
        msui.setBackgroundB(classicBackground);
        return msui;
	}
	
}
