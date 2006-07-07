package plugin.g2theme;

import java.io.FileNotFoundException;

import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.VoiceArea;
import net.sf.nmedit.jpatch.clavia.nordmodular.v3_03.spec.DModule;
import net.sf.nmedit.nomad.main.background.Background;
import net.sf.nmedit.nomad.main.background.BackgroundFactory;
import net.sf.nmedit.nomad.patch.ui.ModuleSectionUI;
import net.sf.nmedit.nomad.patch.ui.ModuleUI;
import net.sf.nmedit.nomad.theme.UIFactory;
import net.sf.nmedit.nomad.theme.component.AudioLevelDisplay;
import net.sf.nmedit.nomad.theme.component.GroupDecoration;
import net.sf.nmedit.nomad.theme.component.NomadLabel;
import net.sf.nmedit.nomad.theme.component.NomadVocoderController;
import net.sf.nmedit.nomad.theme.component.VocoderBandDisplay;
import net.sf.nmedit.nomad.theme.xml.dom.ThemeNode;
import plugin.g2theme.custom.G2ActiveLabel;
import plugin.g2theme.custom.G2BorderFactory;
import plugin.g2theme.custom.G2ButtonArray;
import plugin.g2theme.custom.G2ColorConstants;
import plugin.g2theme.custom.G2Connector;
import plugin.g2theme.custom.G2Knob;
import plugin.g2theme.custom.G2ResetButton;

public class G2ThemeFactory extends UIFactory {
	public G2ThemeFactory() {
		installClass(G2ActiveLabel.class,"display.text");
		installClass(G2ButtonArray.class,"button");
		installClass(G2Connector.class,"connector");
		installClass(G2Knob.class,"knob");
		installClass(NomadLabel.class,"label");
		installClass(G2ResetButton.class,"knob.reset");
		installClass(VocoderBandDisplay.class,"display.vocoder");
		installClass(NomadVocoderController.class,"display.vocoder.controller");
		installClass(AudioLevelDisplay.class,"display.audiolevel");
		installClass(GroupDecoration.class,"border.groupbox");
		
	
		try {
			getImageTracker().loadFromDirectory("plugin/g2theme/images");
		} catch (FileNotFoundException e) {
			System.out.println("Plugin images not found");
		}
	}

	public ModuleUI getModuleGUI(DModule info) {
		ModuleUI gui = super.getModuleGUI(info);
		gui.setBackground(G2ColorConstants.MODULE_BACKGROUND);
		gui.setBorder(G2BorderFactory.createG2ModulePaneBorder());
		return gui;
	}

	public ModuleSectionUI getModuleSectionUI(VoiceArea moduleSection) {
        
        Background bg = BackgroundFactory.createTiledBackground(
                (moduleSection!=null && moduleSection.isPolyVoiceArea()) ?
                        getImageTracker().getImage("msection-va"):
                            getImageTracker().getImage("msection-fx")
        );
        
        ModuleSectionUI m = super.getModuleSectionUI(moduleSection);
        m.setBackgroundB(bg);
        return m;
	}

    @Override
    public ThemeNode getThemeSetup()
    {
        return loadThemeSetup("/RESOURCE/xml/theme.xml");
    }
	
}
