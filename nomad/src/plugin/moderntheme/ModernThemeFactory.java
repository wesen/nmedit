package plugin.moderntheme;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.BorderFactory;

import nomad.graphics.BackgroundPainter;
import nomad.graphics.HGradientBackgroundRenderer;
import nomad.graphics.LCDBackgroundRenderer;
import nomad.gui.ModuleGUI;
import nomad.gui.ModuleSectionGUI;
import nomad.gui.model.UIFactory;
import nomad.gui.model.component.AbstractUIComponent;
import nomad.gui.model.component.builtin.ButtonGroupUI;
import nomad.gui.model.component.builtin.DefaultConnectorUI;
import nomad.gui.model.component.builtin.DefaultControlUI;
import nomad.gui.model.component.builtin.DefaultLabelUI;
import nomad.gui.model.component.builtin.DefaultTextDisplay;
import nomad.gui.model.component.builtin.SliderArrayUI;
import nomad.gui.model.component.builtin.SliderArrayUI14;
import nomad.gui.model.component.builtin.SliderUI;
import nomad.gui.model.component.builtin.VocoderUI;
import nomad.gui.model.component.builtin.implementation.ButtonGroup;
import nomad.gui.model.component.builtin.implementation.NomadKnobLook;
import nomad.gui.model.component.builtin.implementation.VocoderBandDisplay;
import nomad.misc.ImageTracker;
import nomad.patch.Module;
import nomad.patch.ModuleSection;

public class ModernThemeFactory extends UIFactory {

	public final Color dispBackgroundColor = Color.decode("#2721CE");
	public final Color dispLightColor = Color.decode("#367AF9");
	public final Color dispForegroundColor = Color.decode("#0BE9F9");
	
	public ModernThemeFactory() {
		installUIClass(CustomizedLabelUI.class);
		//installUIClass(DefaultTextDisplay.class);
		installUIClass(DefaultControlUI.class);
		installUIClass(DefaultConnectorUI.class);
		installUIClass(ButtonGroupUI.class);
		installUIClass(SliderUI.class);
		installUIClass(VocoderUI.class);
		installUIClass(SliderArrayUI.class);
		installUIClass(SliderArrayUI14.class);
		
		installDefaultControl(DefaultControlUI.class);
		installDefaultOptionControl(ButtonGroupUI.class);
		installDefaultLabel(CustomizedLabelUI.class);
		installDefaultConnector(DefaultConnectorUI.class);
		

		setupNomadKnobLook(DefaultControlUI.getKnobSmallLook());
		setupNomadKnobLook(DefaultControlUI.getKnobLargeLook());
		
		//renderImages();
		
		Image img = Toolkit.getDefaultToolkit().getImage(
				"src/plugin/moderntheme/patch-background.jpg");
		if (img!=null)
			getImageTracker().putImage( "patch-background", img );
	}
	
	private void setupNomadKnobLook(NomadKnobLook look) {
		look.setTopColor(Color.decode("#9D9D9D")); //#C0C0C0
		look.setMiddleColor(Color.decode("#345DFF"));
		look.setOutsideNeedle(Color.decode("#8E99AC"));
		look.setOutsideCircle(Color.decode("#0022FF"));
	}
	
	public class CustomizedLabelUI extends DefaultLabelUI {

		public CustomizedLabelUI(UIFactory factory) {
			super(factory);
		}
		
		public void setComponent(Component c) {
			if (c!=null)
				c.setForeground(Color.decode("#3A405A"/*"#325EFF"/*"#0043FD"*/));
			super.setComponent(c);
		}
	}
	
	public class CustomizedTextDisplay extends DefaultTextDisplay {
		public CustomizedTextDisplay(UIFactory factory) {
			super(factory);/*
			final Color txtLight = dispLightColor;
			final Color txtForeG = dispForegroundColor;
			final Color txtBackG = dipsBackgroundColor;*/

			final Color txtLight = Color.decode("#FFFFFF"/*"#7C8486"*/);
			//final Color txtForeG = Color.decode("#BCCBCE");
			final Color txtBackG = Color.decode("#BDD4FF"/*"#E9EFF1"*/);
			
			getLabel().setForeground(Color.decode("#2A2A2B")/*txtForeG*/);
			getComponent().setBackground(txtBackG);
			getComponent().setForeground(txtLight);
			LCDBackgroundRenderer dispRender = new LCDBackgroundRenderer();
			dispRender.setDisplayColor(txtBackG);
			dispRender.setLightColor(txtLight);
			dispRender.setDistance(4.0f);
			dispRender.randomizeBehaviour(new Dimension(50, 18));
			getContainer().setBorder(
				BorderFactory.createLineBorder(Color.GRAY)	
			);

			setBackgroundRenderer(dispRender);
		}
	}

	public AbstractUIComponent newUIInstance(String uiClassName) {
		if (uiClassName.endsWith("DefaultLabelUI"))
			return newDefaultLabelInstance();
		else if (uiClassName.endsWith("DefaultTextDisplay"))
			return new CustomizedTextDisplay(this);
		else {
			AbstractUIComponent uic = super.newUIInstance(uiClassName);
			if (uic instanceof ButtonGroupUI) {
				((ButtonGroup)uic.getComponent())
					.setButtonColors(
							Color.decode("#242930"),
							Color.decode("#CECDD3")
					);
			} else if (uic instanceof VocoderUI) {
				VocoderUI vocui = (VocoderUI) uic;
				VocoderBandDisplay disp = 
					vocui.getVocoderControl().getVocoderBandDisplay();

				disp.setBackground(dispBackgroundColor);
				disp.setForeground(dispForegroundColor);
				
				LCDBackgroundRenderer dispRender = new LCDBackgroundRenderer();
				dispRender.setDisplayColor(dispBackgroundColor);
				dispRender.setLightColor(dispLightColor);
				dispRender.randomizeBehaviour(new Dimension(150, 100));
				disp.setBackgroundRenderer(dispRender);
			} else if (uic instanceof SliderUI) {
				SliderUI sui = (SliderUI) uic;
				sui.getSlider().setBackground(dispBackgroundColor);
				sui.getSlider().useModernStyle(dispForegroundColor);
				sui.getSlider().setOpaque(false);
			} else if (uic instanceof SliderArrayUI || uic instanceof SliderArrayUI14) {
				SliderArrayUI saui = (SliderArrayUI) uic;

				LCDBackgroundRenderer dispRender = new LCDBackgroundRenderer();
				dispRender.setDisplayColor(dispBackgroundColor);
				dispRender.setLightColor(dispLightColor);
				dispRender.randomizeBehaviour(new Dimension(150, 100));
				saui.getSliderArray().setBackground(dispBackgroundColor);
				saui.getSliderArray().setForeground(dispForegroundColor);
				saui.getSliderArray().setBackgroundRenderer(dispRender);
				saui.getSliderArray().setBorder(BorderFactory.createLoweredBevelBorder());
				saui.getSliderArray().setModern(dispForegroundColor);
			}
			return uic;
		}
	}
	
	public AbstractUIComponent newDefaultLabelInstance() {
		return new CustomizedLabelUI(this);
	}
	
	public String getUIDescriptionFileName() {
		return "src/plugin/classictheme/ui.xml";
	}

	public ModuleGUI getModuleGUI(Module module, ModuleSectionGUI moduleSectionGUI) {
		return new ModernModuleGUI(this, module, moduleSectionGUI);
	}

	public ModuleSectionGUI getModuleSectionGUI(ModuleSection moduleSection) {
		return new ModernModuleSectionGUI(moduleSection, getImageTracker());
	}
}


class ModernModuleSectionGUI extends ModuleSectionGUI {
	private static BackgroundPainter bgpainter = null;
	public ModernModuleSectionGUI(ModuleSection moduleSection, ImageTracker itracker) {
		super(moduleSection);
		if (bgpainter==null) {
			bgpainter = new BackgroundPainter(
				this, itracker.getImage("patch-background")
			);
		}
	}

	public void paintComponent(Graphics g) {
		bgpainter.paintBackground(g);
	}
}

class ModernModuleGUI extends ModuleGUI {

	private static HGradientBackgroundRenderer renderer = null;
	
	public ModernModuleGUI(UIFactory factory, Module module, ModuleSectionGUI moduleSectionGUI) {
		super(factory, module, moduleSectionGUI);

		setBackground(Color.decode("#CECECE"));
		setForeground(Color.decode("#F1F1F1"));

		if (renderer==null) {
			renderer = new HGradientBackgroundRenderer();
			renderer.setBackgroundColor(getBackground());
			renderer.setLightColor(getForeground());
			renderer.setLight(0.5); // center light
			renderer.setRadius(0.75);
		}
		
		setBackgroundRenderer(renderer);
	}

}
