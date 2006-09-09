package plugin.classictheme;

import java.awt.Image;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.VoiceArea;
import net.sf.nmedit.nomad.main.background.Background;
import net.sf.nmedit.nomad.main.background.BackgroundFactory;
import net.sf.nmedit.nomad.patch.ui.ModuleSectionUI;
import net.sf.nmedit.nomad.theme.NMTheme;
import net.sf.nmedit.nomad.theme.component.ADDisplay;
import net.sf.nmedit.nomad.theme.component.ADSRDisplay;
import net.sf.nmedit.nomad.theme.component.ADSRModDisplay;
import net.sf.nmedit.nomad.theme.component.AHDDisplay;
import net.sf.nmedit.nomad.theme.component.AudioLevelDisplay;
import net.sf.nmedit.nomad.theme.component.ClipDisp;
import net.sf.nmedit.nomad.theme.component.GroupDecoration;
import net.sf.nmedit.nomad.theme.component.LFODisplay;
import net.sf.nmedit.nomad.theme.component.NomadActiveLabel;
import net.sf.nmedit.nomad.theme.component.NomadButtonArray;
import net.sf.nmedit.nomad.theme.component.NomadClassicConnector;
import net.sf.nmedit.nomad.theme.component.NomadClassicKnob;
import net.sf.nmedit.nomad.theme.component.NomadLabel;
import net.sf.nmedit.nomad.theme.component.NomadResetButton;
import net.sf.nmedit.nomad.theme.component.NomadVocoderController;
import net.sf.nmedit.nomad.theme.component.NoteVelScaleDisplay;
import net.sf.nmedit.nomad.theme.component.Slider;
import net.sf.nmedit.nomad.theme.component.VocoderBandDisplay;
import net.sf.nmedit.nomad.theme.component.WaveWrapDisp;


public class ClassicThemeFactory extends NMTheme 
{
	public ClassicThemeFactory() 
    {
		putComponentClass(NomadActiveLabel.class,"display.text");
		putComponentClass(NomadButtonArray.class,"button");
		putComponentClass(NomadClassicConnector.class,"connector");
		putComponentClass(NomadClassicKnob.class,"knob");
		putComponentClass(NomadLabel.class,"label");
        putComponentClass(NomadResetButton.class,"knob.reset");
        putComponentClass(Slider.class,"slider");
		putComponentClass(VocoderBandDisplay.class,"display.vocoder");
		putComponentClass(NomadVocoderController.class,"display.vocoder.controller");
		putComponentClass(AudioLevelDisplay.class,"display.audiolevel");
        putComponentClass(GroupDecoration.class,"border.groupbox");
        putComponentClass(WaveWrapDisp.class,"WaveWrapDisplay");
        putComponentClass(ClipDisp.class,"ClipDisplay");

        putComponentClass(ADDisplay.class,"ADDisplay");
        putComponentClass(AHDDisplay.class,"AHDDisplay");
        putComponentClass(ADSRModDisplay.class,"ADSRModDisplay");
        putComponentClass(ADSRDisplay.class,"ADSRDisplay");
        putComponentClass(LFODisplay.class,"LFODisplay");
        
        putComponentClass(NoteVelScaleDisplay.class,"NoteVelScaleDisplay");
        
        init();
	}

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
    
    public void init()
    {
        init("/RESOURCE/xml/theme.xml");
    }
	
}
