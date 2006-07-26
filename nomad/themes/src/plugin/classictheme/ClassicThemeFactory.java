package plugin.classictheme;

import java.awt.Image;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.VoiceArea;
import net.sf.nmedit.nomad.main.background.Background;
import net.sf.nmedit.nomad.main.background.BackgroundFactory;
import net.sf.nmedit.nomad.patch.ui.ModuleSectionUI;
import net.sf.nmedit.nomad.theme.UIFactory;
import net.sf.nmedit.nomad.theme.component.ADDisplay;
import net.sf.nmedit.nomad.theme.component.ADSRDisplay;
import net.sf.nmedit.nomad.theme.component.ADSRModDisplay;
import net.sf.nmedit.nomad.theme.component.AHDDisplay;
import net.sf.nmedit.nomad.theme.component.AudioLevelDisplay;
import net.sf.nmedit.nomad.theme.component.ClipDisp;
import net.sf.nmedit.nomad.theme.component.GroupDecoration;
import net.sf.nmedit.nomad.theme.component.NomadActiveLabel;
import net.sf.nmedit.nomad.theme.component.NomadButtonArray;
import net.sf.nmedit.nomad.theme.component.NomadClassicConnector;
import net.sf.nmedit.nomad.theme.component.NomadClassicKnob;
import net.sf.nmedit.nomad.theme.component.NomadLabel;
import net.sf.nmedit.nomad.theme.component.NomadResetButton;
import net.sf.nmedit.nomad.theme.component.NomadVocoderController;
import net.sf.nmedit.nomad.theme.component.VocoderBandDisplay;
import net.sf.nmedit.nomad.theme.component.WaveWrapDisp;
import net.sf.nmedit.nomad.theme.xml.dom.ThemeNode;


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
        installClass(WaveWrapDisp.class,"WaveWrapDisplay");
        installClass(ClipDisp.class,"ClipDisplay");

        installClass(ADDisplay.class,"ADDisplay");
        installClass(AHDDisplay.class,"AHDDisplay");
        installClass(ADSRModDisplay.class,"ADSRModDisplay");
        installClass(ADSRDisplay.class,"ADSRDisplay");
	}
    
    /*
	public ModuleUI getModuleGUI(DModule info) {
		ModuleUI gui = super.getModuleGUI(info);
		return gui;
	}*/

    private Background classicBackground = null;
    
	public ModuleSectionUI getModuleSectionUI(VoiceArea moduleSection) 
    {    
        if (classicBackground==null)
        {
            Image image = getImageTracker().getImage("classic-patch-background");
            classicBackground =
                BackgroundFactory.createTiledBackground(image);
        }
        ModuleSectionUI msui = super.getModuleSectionUI(moduleSection);
        msui.setBackgroundB(classicBackground);
        msui.setOpaque(true);
        return msui;
	}

    @Override
    public ThemeNode getThemeSetup()
    {
        return loadThemeSetup("/RESOURCE/xml/theme.xml");
    }
	
}
