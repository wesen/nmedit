package plugin.g2theme;

import java.awt.Graphics;
import java.io.FileNotFoundException;

import org.nomad.patch.Module;
import org.nomad.patch.ModuleSection;
import org.nomad.theme.ModuleGUI;
import org.nomad.theme.ModuleSectionGUI;
import org.nomad.theme.UIFactory;
import org.nomad.theme.component.AudioLevelDisplay;
import org.nomad.theme.component.GroupDecoration;
import org.nomad.theme.component.NomadImageView;
import org.nomad.theme.component.NomadLabel;
import org.nomad.theme.component.NomadVocoderController;
import org.nomad.theme.component.VocoderBandDisplay;
import org.nomad.util.graphics.BackgroundPainter;
import org.nomad.util.graphics.ImageTracker;
import org.nomad.xml.dom.module.DModule;

import plugin.g2theme.custom.G2ActiveLabel;
import plugin.g2theme.custom.G2BorderFactory;
import plugin.g2theme.custom.G2ButtonArray;
import plugin.g2theme.custom.G2ColorConstants;
import plugin.g2theme.custom.G2Connector;
import plugin.g2theme.custom.G2Knob;
import plugin.g2theme.custom.G2ResetButton;

public class G2ThemeFactory extends UIFactory {
	private static BackgroundPainter bgpainterVA = null;
	private static BackgroundPainter bgpainterFX = null;
	
	public G2ThemeFactory() {
		installClass(G2ActiveLabel.class,"display.text");
		installClass(G2ButtonArray.class,"button");
		installClass(G2Connector.class,"connector");
		installClass(G2Knob.class,"knob");
		installClass(NomadImageView.class,"image");
		installClass(NomadLabel.class,"label");
		installClass(G2ResetButton.class,"knob.reset");
		installClass(VocoderBandDisplay.class,"display.vocoder");
		installClass(NomadVocoderController.class,"display.vocoder.controller");
		installClass(AudioLevelDisplay.class,"display.audiolevel");
		installClass(GroupDecoration.class,"border.groupbox");
		
	
		try {
			getImageTracker().loadFromDirectory("plugin/g2theme/images");
			bgpainterVA = new BackgroundPainter(getImageTracker().getImage("msection-va"));
			bgpainterFX = new BackgroundPainter(getImageTracker().getImage("msection-fx"));
		} catch (FileNotFoundException e) {
			System.out.println("Plugin images not found");
		}
	}

	public String getUIDescriptionFileName() {
		// use the classic theme ui file
		return "plugin/classictheme/ui.xml";
	}

	public ModuleGUI getModuleGUI(DModule info, Module module, ModuleSectionGUI moduleSectionGUI) {
		ModuleGUI gui = super.getModuleGUI(info, module, moduleSectionGUI);
		gui.setBackground(G2ColorConstants.MODULE_BACKGROUND);
		gui.setBorder(G2BorderFactory.createG2ModulePaneBorder());
		return gui;
	}

	public ModuleSectionGUI getModuleSectionGUI(ModuleSection moduleSection) {
		BackgroundPainter painter = bgpainterVA;			
		if (moduleSection!=null 
			&& moduleSection.getModulesSectionType()==ModuleSection.ModulesSectionType.POLY) {
			painter = bgpainterFX;
		}
		return new G2ModuleSectionGUI(moduleSection, getImageTracker(), painter);
	}
	
}

class G2ModuleSectionGUI extends ModuleSectionGUI {
	
	private BackgroundPainter background;
	
	public G2ModuleSectionGUI(ModuleSection moduleSection, ImageTracker itracker,
			BackgroundPainter background
	) {
		super(moduleSection) ;
        setDoubleBuffered(false);
        this.background = background;
	}

	public void paintComponent(Graphics g) {
		background.paintBackground(g, this);
	}
	
}