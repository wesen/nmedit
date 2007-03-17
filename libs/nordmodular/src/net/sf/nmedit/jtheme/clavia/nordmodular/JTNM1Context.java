/* Copyright (C) 2006 Christian Schneider
 * 
 * This file is part of Nomad.
 * 
 * Nomad is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Nomad is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Nomad; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package net.sf.nmedit.jtheme.clavia.nordmodular;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.UIDefaults;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

import net.sf.nmedit.jtheme.JTCustomContext;
import net.sf.nmedit.jtheme.clavia.nordmodular.plaf.JTNM1ButtonControlUI;
import net.sf.nmedit.jtheme.clavia.nordmodular.plaf.JTNM1ConnectorUI;
import net.sf.nmedit.jtheme.clavia.nordmodular.plaf.JTNM1KnobUI;
import net.sf.nmedit.jtheme.clavia.nordmodular.plaf.JTNM1ResetButtonUI;
import net.sf.nmedit.jtheme.clavia.nordmodular.plaf.JTNM1SliderUI;
import net.sf.nmedit.jtheme.component.JTButtonControl;
import net.sf.nmedit.jtheme.component.JTConnector;
import net.sf.nmedit.jtheme.component.JTDisplay;
import net.sf.nmedit.jtheme.component.JTImage;
import net.sf.nmedit.jtheme.component.JTKnob;
import net.sf.nmedit.jtheme.component.JTLabel;
import net.sf.nmedit.jtheme.component.JTModule;
import net.sf.nmedit.jtheme.component.JTModuleContainer;
import net.sf.nmedit.jtheme.component.JTSlider;
import net.sf.nmedit.jtheme.component.JTTextDisplay;
import net.sf.nmedit.jtheme.component.plaf.JTDisplayUI;
import net.sf.nmedit.jtheme.component.plaf.JTImageUI;
import net.sf.nmedit.jtheme.component.plaf.JTLabelUI;
import net.sf.nmedit.jtheme.component.plaf.JTModuleContainerUI;
import net.sf.nmedit.jtheme.component.plaf.JTModuleUI;
import net.sf.nmedit.jtheme.component.plaf.JTTextDisplayUI;

public class JTNM1Context extends JTCustomContext
{

    public static final Color GRAPH_DISPLAY_LINE = new Color(0xC0C0C0);

    public JTNM1Context()
    {
        super(true, true);
    }

    public JTNM1Context(boolean hasModuleContainerOverlay, boolean dndAllowed)
    {
        super(hasModuleContainerOverlay, dndAllowed);
    }
    
    protected void installComponentClassMap()
    {
        installComponentType(TYPE_MODULE, JTModule.class);
        installComponentType(TYPE_KNOB, JTKnob.class);
        installComponentType(TYPE_SLIDER, JTSlider.class);
        installComponentType(TYPE_LABEL, JTLabel.class);
        installComponentType(TYPE_IMAGE, JTImage.class);
        installComponentType(TYPE_CONNECTOR, JTConnector.class);
        installComponentType(TYPE_BUTTONS, JTButtonControl.class);
        installComponentType(TYPE_RESET_BUTTON, JTNM1ResetButton.class);
        installComponentType(TYPE_TEXTDISPLAY, JTTextDisplay.class);
    }

    @Override
    protected void installComponentClasses()
    {
        installComponentClass(JTLabel.class);
        installComponentClass(JTImage.class);
        //installComponentClass(JTWaldorfKnob.class);

        installComponentClass(JTButtonControl.class);
        installComponentClass(JTNM1ResetButton.class);
        installComponentClass(JTKnob.class);
        installComponentClass(JTSlider.class);
        installComponentClass(WaveWrapDisp.class);
        installComponentClass(ADDisplay.class);
        installComponentClass(ADSRDisplay.class);
        installComponentClass(ADSRModDisplay.class);
        installComponentClass(AHDDisplay.class);
        installComponentClass(ClipDisp.class);
        installComponentClass(LFODisplay.class);
        installComponentClass(NoteVelScaleDisplay.class);

        installComponentClass(JTEqMidDisplay.class);
        installComponentClass(JTEqShelvingDisplay.class);
        installComponentClass(JTPhaserDisplay.class);
        installComponentClass(JTFilterEDisplay.class);
        installComponentClass(JTFilterFDisplay.class);
    }

    @Override
    protected void setDefaults(UIDefaults uidefaults)
    {
        FontUIResource baseFont = new FontUIResource("SansSerif", Font.PLAIN, 9);
        
        uidefaults.put(JTSlider.uiClassID, JTNM1SliderUI.class.getName());
        uidefaults.put(JTKnob.uiClassID, JTNM1KnobUI.class.getName());
        uidefaults.put(JTNM1ResetButton.uiClassID, JTNM1ResetButtonUI.class.getName());
        uidefaults.put(JTConnector.uiClassID, JTNM1ConnectorUI.class.getName());
        uidefaults.put(JTButtonControl.uiClassID, JTNM1ButtonControlUI.class.getName());
        uidefaults.put(JTNM1ButtonControlUI.borderUpKey, JTNM1BorderFactory.createNordEditor311RaisedButtonBorder());
        uidefaults.put(JTNM1ButtonControlUI.borderDownKey, JTNM1BorderFactory.createNordEditor311LoweredButtonBorder());

        uidefaults.put(JTImage.uiClassID, JTImageUI.class.getName());
        uidefaults.put(JTLabel.uiClassID, JTLabelUI.class.getName());
        uidefaults.put(JTLabelUI.fontKey, baseFont);
        uidefaults.put(JTTextDisplay.uiClassID, JTTextDisplayUI.class.getName());
        uidefaults.put(JTTextDisplayUI.borderKey, new BorderUIResource(JTNM1BorderFactory.createNordEditor311Border()));
        uidefaults.put(JTDisplay.uiClassID, JTDisplayUI.class.getName());
        uidefaults.put(JTDisplayUI.BACKGROUND_KEY, new ColorUIResource(0x008080));
        uidefaults.put(JTDisplayUI.FOREGROUND_KEY, new ColorUIResource(0x00ff00));
        uidefaults.put(JTModule.uiClassID, JTModuleUI.class.getName());
        uidefaults.put(JTEqMidDisplay.uiClassID, JTDisplayUI.class.getName());
        uidefaults.put(JTEqShelvingDisplay.uiClassID, JTDisplayUI.class.getName());
        uidefaults.put(JTPhaserDisplay.uiClassID, JTDisplayUI.class.getName());
        uidefaults.put(JTFilterEDisplay.uiClassID, JTDisplayUI.class.getName());
        uidefaults.put(JTFilterFDisplay.uiClassID, JTDisplayUI.class.getName());

        uidefaults.put(NoteVelScaleDisplay.uiClassID, JTDisplayUI.class.getName());
        uidefaults.put(ADDisplay.uiClassID, JTDisplayUI.class.getName());
        uidefaults.put(ADSRDisplay.uiClassID, JTDisplayUI.class.getName());
        uidefaults.put(AHDDisplay.uiClassID, JTDisplayUI.class.getName());
        uidefaults.put(ADSRModDisplay.uiClassID, JTDisplayUI.class.getName());
        uidefaults.put(WaveWrapDisp.uiClassID, JTDisplayUI.class.getName());
        uidefaults.put(ClipDisp.uiClassID, JTDisplayUI.class.getName());
        
        uidefaults.put(JTModuleUI.moduleBorder, new BorderUIResource(BorderFactory.createRaisedBevelBorder()));
        uidefaults.put(JTModuleContainer.uiClassId, JTModuleContainerUI.class.getName());
        uidefaults.put(JTModuleContainerUI.backgroundKey, new ColorUIResource(0x81818D));
        uidefaults.put(JTModuleContainerUI.DnDAllowedKey, Boolean.TRUE);
        uidefaults.put(JTModuleContainerUI.baseFontKey, baseFont);

        uidefaults.put(JTNM1SliderUI.sliderGripSizeKey, 3);
        uidefaults.put(JTNM1SliderUI.sliderBackgroundColorKey, new ColorUIResource(0xDCDCDC));
        uidefaults.put(JTNM1SliderUI.sliderGripColorKey, new ColorUIResource(0xC4C4C4));
        uidefaults.put(JTNM1SliderUI.sliderGripBorderLightColorKey, new ColorUIResource(0x909090));
        uidefaults.put(JTNM1SliderUI.sliderGripBorderDarkColorKey, new ColorUIResource(0x3D3D3D));
        uidefaults.put(JTNM1SliderUI.sliderGripColorKey, new ColorUIResource(0xC4C4C4));
        uidefaults.put(JTNM1SliderUI.sliderBackgroundColorKey, new ColorUIResource(0xDCDCDE));
        uidefaults.put(JTNM1SliderUI.borderKey, new BorderUIResource(JTNM1BorderFactory.createNordEditor311Border()));
    }

/*
 * 

    private void installComponents()
    {
        UIContext.put(ClassicConnectorUI.class, NConnector.uiClassID);
        
        
        putComponentClass(NomadActiveLabel.class,"display.text");
        putComponentClass(NomadButtonArray.class,"button");
        putComponentClass(NomadConnector.class,"connector");
        putComponentClass(Knob2.class,"knob");
        putComponentClass(NLabel.class,"label");
        
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
        putComponentClass(LightDisplay.class,"light");
        
        putComponentClass(NoteVelScaleDisplay.class,"NoteVelScaleDisplay");

    }

    public void init()
    {
        InputStream in =  getClass().getClassLoader().getResourceAsStream("theme.xml");
        if (in != null)
        {
            in = new BufferedInputStream(in);
            init(in);
            try
            {
            in.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    
 * */
}

