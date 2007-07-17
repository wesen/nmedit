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
import net.sf.nmedit.jtheme.JTPopupHandler;
import net.sf.nmedit.jtheme.clavia.nordmodular.plaf.JTBasicSliderScrollbarUI;
import net.sf.nmedit.jtheme.clavia.nordmodular.plaf.JTNM1KnobUI;
import net.sf.nmedit.jtheme.clavia.nordmodular.plaf.JTNM1ResetButtonUI;
import net.sf.nmedit.jtheme.clavia.nordmodular.plaf.JTNM1SliderUI;
import net.sf.nmedit.jtheme.clavia.nordmodular.plaf.NoteSeqEditorUI;
import net.sf.nmedit.jtheme.component.JTButtonControl;
import net.sf.nmedit.jtheme.component.JTComponent;
import net.sf.nmedit.jtheme.component.JTConnector;
import net.sf.nmedit.jtheme.component.JTDisplay;
import net.sf.nmedit.jtheme.component.JTImage;
import net.sf.nmedit.jtheme.component.JTKnob;
import net.sf.nmedit.jtheme.component.JTLabel;
import net.sf.nmedit.jtheme.component.JTLight;
import net.sf.nmedit.jtheme.component.JTModule;
import net.sf.nmedit.jtheme.component.JTModuleContainer;
import net.sf.nmedit.jtheme.component.JTSlider;
import net.sf.nmedit.jtheme.component.JTTextDisplay;
import net.sf.nmedit.jtheme.component.plaf.JTBasicButtonControlUI;
import net.sf.nmedit.jtheme.component.plaf.JTBasicConnectorUI;
import net.sf.nmedit.jtheme.component.plaf.JTBasicLightUI;
import net.sf.nmedit.jtheme.component.plaf.JTButtonControlUI;
import net.sf.nmedit.jtheme.component.plaf.JTDisplayUI;
import net.sf.nmedit.jtheme.component.plaf.JTImageUI;
import net.sf.nmedit.jtheme.component.plaf.JTLabelUI;
import net.sf.nmedit.jtheme.component.plaf.JTLightUI;
import net.sf.nmedit.jtheme.component.plaf.JTModuleContainerUI;
import net.sf.nmedit.jtheme.component.plaf.JTModuleUI;
import net.sf.nmedit.jtheme.component.plaf.JTTextDisplayUI;
import net.sf.nmedit.jtheme.store.StorageContext;

public class JTNM1Context extends JTCustomContext
{

    public static final String DISPLAY_SHAPE_FILL_COLOR_KEY = "display.shape.fill";
    public static final String DISPLAY_SHAPE_OUTLINE_COLOR_KEY = "display.shape.outline";
    public static final String DISPLAY_AXIS_COLOR_KEY = "display.axis";

    public static final Color GRAPH_DISPLAY_LINE = new Color(0xC0C0C0);
    public static final Color GRAPH_DISPLAY_FILL = new Color(0xC0D0C0);
    public static final Color GRAPH_DISPLAY_FILL_LINE = new Color(0x000000);

    private StorageContext stc;
    
    private JTPopupHandler popupHandler = new ControlPopupHandler();

    public JTNM1Context(StorageContext stc)
    {
        //super(true, true);
        super(false, true);
        this.stc = stc;
        
        
    }
    
    public JTPopupHandler getPopupHandler(JTComponent c)
    {
        return popupHandler;
    }
    
    public StorageContext getStorageContext()
    {
        return stc;
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
        installComponentType(TYPE_LIGHT, JTLight.class);
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
        installComponentClass(JTEnvelopeDisplay.class);
        installComponentClass(ClipDisp.class);
        installComponentClass(LFODisplay.class);
        installComponentClass(NoteVelScaleDisplay.class);

        installComponentClass(JTEqMidDisplay.class);
        installComponentClass(JTEqShelvingDisplay.class);
        installComponentClass(JTPhaserDisplay.class);
        installComponentClass(JTFilterEDisplay.class);
        installComponentClass(JTFilterFDisplay.class);
        installComponentClass(NMNoteSeqEditor.class);
        installComponentClass(VocoderDisplay.class);
        installComponentClass(JTLight.class);
        installComponentClass(NMScrollbar.class);
    }

    @Override
    protected void setDefaults(UIDefaults uidefaults)
    {
        FontUIResource baseFont = new FontUIResource("SansSerif", Font.PLAIN, 9);

        uidefaults.put(DISPLAY_AXIS_COLOR_KEY, new ColorUIResource(0x000000));
        uidefaults.put(DISPLAY_SHAPE_FILL_COLOR_KEY, new ColorUIResource(0xC0D0C0));
        uidefaults.put(DISPLAY_SHAPE_OUTLINE_COLOR_KEY, new ColorUIResource(0xC0C0C0));

        uidefaults.put(JTSlider.uiClassID, JTNM1SliderUI.class.getName());
        uidefaults.put(JTKnob.uiClassID, JTNM1KnobUI.class.getName());
        uidefaults.put(JTNM1ResetButton.uiClassID, JTNM1ResetButtonUI.class.getName());
        uidefaults.put(JTConnector.uiClassID, JTBasicConnectorUI.class.getName());

        uidefaults.put(JTButtonControl.uiClassID, JTBasicButtonControlUI.class.getName());
        
        uidefaults.put(JTButtonControlUI.BORDER_KEY, JTNM1BorderFactory.createNordEditor311RaisedButtonBorder());
        uidefaults.put(JTButtonControlUI.SELECTED_BORDER_KEY, JTNM1BorderFactory.createNordEditor311LoweredButtonBorder());
        uidefaults.put(JTButtonControlUI.BACKGROUND_KEY, new ColorUIResource(0xB0B0B0));
        uidefaults.put(JTButtonControlUI.BACKGROUND_STATE_KEY, new ColorUIResource(0xC4F6ED));
        uidefaults.put(JTButtonControlUI.BACKGROUND_SELECTED_KEY, new ColorUIResource(0xC6CCDE));

        uidefaults.put(JTLight.uiClassId, JTBasicLightUI.class.getName());
        uidefaults.put(JTLightUI.BORDER_KEY,  new BorderUIResource(JTNM1BorderFactory.createNordEditor311Border()));
        uidefaults.put(JTImage.uiClassID, JTImageUI.class.getName());
        uidefaults.put(JTLabel.uiClassID, JTLabelUI.class.getName());
        uidefaults.put(JTLabelUI.fontKey, baseFont);
        uidefaults.put(JTTextDisplay.uiClassID, JTTextDisplayUI.class.getName());
        uidefaults.put(JTTextDisplayUI.borderKey, new BorderUIResource(JTNM1BorderFactory.createNordEditor311Border()));
        uidefaults.put(JTDisplay.uiClassID, JTDisplayUI.class.getName());
        uidefaults.put(JTDisplayUI.BACKGROUND_KEY, new ColorUIResource(0x008080));
        uidefaults.put(JTDisplayUI.FOREGROUND_KEY, new ColorUIResource(0x00ff00));
        uidefaults.put(JTDisplayUI.BORDER_KEY, new BorderUIResource(JTNM1BorderFactory.createNordEditor311Border()));
        uidefaults.put(JTModule.uiClassID, JTModuleUI.class.getName());
        uidefaults.put(JTEqMidDisplay.uiClassID, JTDisplayUI.class.getName());
        uidefaults.put(JTEqShelvingDisplay.uiClassID, JTDisplayUI.class.getName());
        uidefaults.put(JTPhaserDisplay.uiClassID, JTDisplayUI.class.getName());
        uidefaults.put(JTFilterEDisplay.uiClassID, JTDisplayUI.class.getName());
        uidefaults.put(VocoderDisplay.uiClassID, JTDisplayUI.class.getName());
        uidefaults.put(JTFilterFDisplay.uiClassID, JTDisplayUI.class.getName());
        uidefaults.put(LFODisplay.uiClassID, JTDisplayUI.class.getName());
        uidefaults.put(JTCompressorDisplay.uiClassID, JTDisplayUI.class.getName());

        uidefaults.put(NoteVelScaleDisplay.uiClassID, JTDisplayUI.class.getName());
        uidefaults.put(JTEnvelopeDisplay.uiClassID, JTDisplayUI.class.getName());
        uidefaults.put(WaveWrapDisp.uiClassID, JTDisplayUI.class.getName());
        uidefaults.put(ClipDisp.uiClassID, JTDisplayUI.class.getName());
        uidefaults.put(NMScrollbar.ScrollBarUIClassID, JTBasicSliderScrollbarUI.class.getName());
        
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
        
        uidefaults.put(NoteSeqEditorUI.borderKey, new BorderUIResource(JTNM1BorderFactory.createNordEditor311Border()));
        
        uidefaults.put(NMNoteSeqEditor.uiClassID, NoteSeqEditorUI.class.getName());
        

        uidefaults.put("module.background$default", new ColorUIResource(0xC0C0C0));
        uidefaults.put("module.background$1", new ColorUIResource(0xE5777A));
        uidefaults.put("module.background$2", new ColorUIResource(0xE7D14B));
        uidefaults.put("module.background$3", new ColorUIResource(0x93D162));
        uidefaults.put("module.background$4", new ColorUIResource(0x69D6C7));
        uidefaults.put("module.background$5", new ColorUIResource(0x74A0D4));
        uidefaults.put("module.background$6", new ColorUIResource(0xD673C7));
    }

}

