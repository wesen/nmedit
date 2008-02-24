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
package net.sf.nmedit.nordmodular;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;


public class SynthSettingsFrm extends JPanel
{
   /**
     * 
     */
    private static final long serialVersionUID = -3312251676984503718L;
JPanel SynthSettingsFrm = new JPanel();
   JSpinner spChannelSlotA = new JSpinner();
   JSpinner spChannelSlotB = new JSpinner();
   JSpinner spChannelSlotC = new JSpinner();
   JSpinner spChannelSlotD = new JSpinner();
   JCheckBox spActiveSlotA = new JCheckBox();
   JCheckBox spActiveSlotB = new JCheckBox();
   JCheckBox spActiveSlotC = new JCheckBox();
   JCheckBox spActiveSlotD = new JCheckBox();
   JRadioButton rbMIDIClockExternal = new JRadioButton();
   ButtonGroup buttongroup1 = new ButtonGroup();
   JRadioButton rbMIDIClockInternal = new JRadioButton();
   JSpinner spMasterTune = new JSpinner();
   JSpinner spMIDIVelScaleMin = new JSpinner();
   JSpinner spMIDIVelScaleMax = new JSpinner();
   JCheckBox cbProgramChangeSend = new JCheckBox();
   JCheckBox cbProgramChangeReceive = new JCheckBox();
   JRadioButton rbKBModeActiveSlot = new JRadioButton();
   ButtonGroup buttongroup2 = new ButtonGroup();
   JRadioButton rbKBModeSelectedSlots = new JRadioButton();
   JCheckBox cbLocalOff = new JCheckBox();
   JCheckBox cbLEDsActive = new JCheckBox();
   JRadioButton rbKnobModeImmediate = new JRadioButton();
   ButtonGroup buttongroup3 = new ButtonGroup();
   JRadioButton rbKnobModeHook = new JRadioButton();
   JRadioButton rbPedalPolarityNormal = new JRadioButton();
   ButtonGroup buttongroup4 = new ButtonGroup();
   JRadioButton rbPedalPolarityInverted = new JRadioButton();
   JTextField jtSynthName = new JTextField();
   JSpinner spGlobalSyncBeats = new JSpinner();
   JSpinner spMIDIClockRate = new JSpinner();

   /**
    * Default constructor
    */
   public SynthSettingsFrm()
   {
      initializePanel();
   }

   /**
    * Adds fill components to empty cells in the first row and first column of the grid.
    * This ensures that the grid spacing will be the same as shown in the designer.
    * @param cols an array of column indices in the first row where fill components should be added.
    * @param rows an array of row indices in the first column where fill components should be added.
    */
   void addFillComponents( Container panel, int[] cols, int[] rows )
   {
      Dimension filler = new Dimension(10,10);

      boolean filled_cell_11 = false;
      CellConstraints cc = new CellConstraints();
      if ( cols.length > 0 && rows.length > 0 )
      {
         if ( cols[0] == 1 && rows[0] == 1 )
         {
            /** add a rigid area  */
            panel.add( Box.createRigidArea( filler ), cc.xy(1,1) );
            filled_cell_11 = true;
         }
      }

      for( int index = 0; index < cols.length; index++ )
      {
         if ( cols[index] == 1 && filled_cell_11 )
         {
            continue;
         }
         panel.add( Box.createRigidArea( filler ), cc.xy(cols[index],1) );
      }

      for( int index = 0; index < rows.length; index++ )
      {
         if ( rows[index] == 1 && filled_cell_11 )
         {
            continue;
         }
         panel.add( Box.createRigidArea( filler ), cc.xy(1,rows[index]) );
      }

   }

   /**
    * Helper method to load an image file from the CLASSPATH
    * @param imageName the package and name of the file to load relative to the CLASSPATH
    * @return an ImageIcon instance with the specified image file
    * @throws IllegalArgumentException if the image resource cannot be loaded.
    */
   public ImageIcon loadImage( String imageName )
   {
      try
      {
         ClassLoader classloader = getClass().getClassLoader();
         java.net.URL url = classloader.getResource( imageName );
         if ( url != null )
         {
            ImageIcon icon = new ImageIcon( url );
            return icon;
         }
      }
      catch( Exception e )
      {
         e.printStackTrace();
      }
      throw new IllegalArgumentException( "Unable to load image: " + imageName );
   }

   public JPanel createSynthSettingsFrm()
   {
      SynthSettingsFrm.setName("SynthSettingsFrm");
      FormLayout formlayout1 = new FormLayout("FILL:DEFAULT:NONE,FILL:4DLU:NONE,FILL:DEFAULT:NONE,FILL:4DLU:NONE,FILL:DEFAULT:NONE,FILL:4DLU:NONE,FILL:DEFAULT:NONE,FILL:4DLU:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:NONE","CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
      CellConstraints cc = new CellConstraints();
      SynthSettingsFrm.setLayout(formlayout1);

      JLabel jlabel1 = new JLabel();
      jlabel1.setText("Slot A");
      SynthSettingsFrm.add(jlabel1,cc.xy(1,5));

      JLabel jlabel2 = new JLabel();
      jlabel2.setText("Slot B");
      SynthSettingsFrm.add(jlabel2,cc.xy(1,6));

      JLabel jlabel3 = new JLabel();
      jlabel3.setText("Slot C");
      SynthSettingsFrm.add(jlabel3,cc.xy(1,7));

      JLabel jlabel4 = new JLabel();
      jlabel4.setText("Slot D");
      SynthSettingsFrm.add(jlabel4,cc.xy(1,8));

      spChannelSlotA.setName("spChannelSlotA");
      SynthSettingsFrm.add(spChannelSlotA,cc.xy(3,5));

      spChannelSlotB.setName("spChannelSlotB");
      SynthSettingsFrm.add(spChannelSlotB,cc.xy(3,6));

      spChannelSlotC.setName("spChannelSlotC");
      SynthSettingsFrm.add(spChannelSlotC,cc.xy(3,7));

      spChannelSlotD.setName("spChannelSlotD");
      SynthSettingsFrm.add(spChannelSlotD,cc.xy(3,8));

      spActiveSlotA.setActionCommand("Active");
      spActiveSlotA.setName("spActiveSlotA");
      spActiveSlotA.setSelected(true);
      spActiveSlotA.setText("Active");
      SynthSettingsFrm.add(spActiveSlotA,cc.xy(5,5));

      spActiveSlotB.setActionCommand("Active");
      spActiveSlotB.setName("spActiveSlotB");
      spActiveSlotB.setSelected(true);
      spActiveSlotB.setText("Active");
      SynthSettingsFrm.add(spActiveSlotB,cc.xy(5,6));

      spActiveSlotC.setActionCommand("Active");
      spActiveSlotC.setName("spActiveSlotC");
      spActiveSlotC.setSelected(true);
      spActiveSlotC.setText("Active");
      SynthSettingsFrm.add(spActiveSlotC,cc.xy(5,7));

      spActiveSlotD.setActionCommand("Active");
      spActiveSlotD.setName("spActiveSlotD");
      spActiveSlotD.setSelected(true);
      spActiveSlotD.setText("Active");
      SynthSettingsFrm.add(spActiveSlotD,cc.xy(5,8));

      rbMIDIClockExternal.setActionCommand("External");
      rbMIDIClockExternal.setName("rbMIDIClockExternal");
      rbMIDIClockExternal.setSelected(true);
      rbMIDIClockExternal.setText("External");
      buttongroup1.add(rbMIDIClockExternal);
      SynthSettingsFrm.add(rbMIDIClockExternal,cc.xy(3,13));

      rbMIDIClockInternal.setActionCommand("Internal");
      rbMIDIClockInternal.setName("rbMIDIClockInternal");
      rbMIDIClockInternal.setText("Internal");
      buttongroup1.add(rbMIDIClockInternal);
      SynthSettingsFrm.add(rbMIDIClockInternal,cc.xy(3,12));

      spMasterTune.setName("spMasterTune");
      SynthSettingsFrm.add(spMasterTune,cc.xy(3,20));

      JLabel jlabel5 = new JLabel();
      jlabel5.setText("Cent");
      SynthSettingsFrm.add(jlabel5,cc.xy(5,20));

      JLabel jlabel6 = new JLabel();
      jlabel6.setText("Max");
      SynthSettingsFrm.add(jlabel6,cc.xy(5,23));

      JLabel jlabel7 = new JLabel();
      jlabel7.setText("Min");
      SynthSettingsFrm.add(jlabel7,cc.xy(5,24));

      spMIDIVelScaleMin.setName("spMIDIVelScaleMin");
      SynthSettingsFrm.add(spMIDIVelScaleMin,cc.xy(3,24));

      spMIDIVelScaleMax.setName("spMIDIVelScaleMax");
      SynthSettingsFrm.add(spMIDIVelScaleMax,cc.xy(3,23));

      cbProgramChangeSend.setActionCommand("Send");
      cbProgramChangeSend.setName("cbProgramChangeSend");
      cbProgramChangeSend.setSelected(true);
      cbProgramChangeSend.setText("Send");
      SynthSettingsFrm.add(cbProgramChangeSend,cc.xy(3,28));

      cbProgramChangeReceive.setActionCommand("Receive");
      cbProgramChangeReceive.setName("cbProgramChangeReceive");
      cbProgramChangeReceive.setSelected(true);
      cbProgramChangeReceive.setText("Receive");
      SynthSettingsFrm.add(cbProgramChangeReceive,cc.xy(3,27));

      rbKBModeActiveSlot.setActionCommand("Active slot");
      rbKBModeActiveSlot.setName("rbKBModeActiveSlot");
      rbKBModeActiveSlot.setSelected(true);
      rbKBModeActiveSlot.setText("Active slot");
      buttongroup2.add(rbKBModeActiveSlot);
      SynthSettingsFrm.add(rbKBModeActiveSlot,cc.xy(9,5));

      rbKBModeSelectedSlots.setActionCommand("Selected slots");
      rbKBModeSelectedSlots.setName("rbKBModeSelectedSlots");
      rbKBModeSelectedSlots.setText("Selected slots");
      buttongroup2.add(rbKBModeSelectedSlots);
      SynthSettingsFrm.add(rbKBModeSelectedSlots,cc.xy(9,6));

      cbLocalOff.setActionCommand("Local off");
      cbLocalOff.setName("cbLocalOff");
      cbLocalOff.setText("Local off");
      SynthSettingsFrm.add(cbLocalOff,cc.xy(9,8));

      cbLEDsActive.setActionCommand("LEDs active");
      cbLEDsActive.setName("cbLEDsActive");
      cbLEDsActive.setText("LEDs active");
      SynthSettingsFrm.add(cbLEDsActive,cc.xy(9,9));

      rbKnobModeImmediate.setActionCommand("Active slot");
      rbKnobModeImmediate.setName("rbKnobModeImmediate");
      rbKnobModeImmediate.setSelected(true);
      rbKnobModeImmediate.setText("Immediate");
      buttongroup3.add(rbKnobModeImmediate);
      SynthSettingsFrm.add(rbKnobModeImmediate,cc.xy(9,12));

      rbKnobModeHook.setActionCommand("Selected slots");
      rbKnobModeHook.setName("rbKnobModeHook");
      rbKnobModeHook.setText("Hook");
      buttongroup3.add(rbKnobModeHook);
      SynthSettingsFrm.add(rbKnobModeHook,cc.xy(9,13));

      rbPedalPolarityNormal.setActionCommand("Active slot");
      rbPedalPolarityNormal.setName("rbPedalPolarityNormal");
      rbPedalPolarityNormal.setSelected(true);
      rbPedalPolarityNormal.setText("Normal");
      buttongroup4.add(rbPedalPolarityNormal);
      SynthSettingsFrm.add(rbPedalPolarityNormal,cc.xy(9,16));

      rbPedalPolarityInverted.setActionCommand("Selected slots");
      rbPedalPolarityInverted.setName("rbPedalPolarityInverted");
      rbPedalPolarityInverted.setText("Inverted");
      buttongroup4.add(rbPedalPolarityInverted);
      SynthSettingsFrm.add(rbPedalPolarityInverted,cc.xy(9,17));

      jtSynthName.setName("jtSynthName");
      SynthSettingsFrm.add(jtSynthName,cc.xywh(3,2,3,1));

      spGlobalSyncBeats.setName("spGlobalSyncBeats");
      SynthSettingsFrm.add(spGlobalSyncBeats,cc.xy(3,17));

      JLabel jlabel8 = new JLabel();
      jlabel8.setText("Global sync (beats)");
      SynthSettingsFrm.add(jlabel8,cc.xy(5,17));

      JLabel jlabel9 = new JLabel();
      jlabel9.setText("Rate (BPM)");
      SynthSettingsFrm.add(jlabel9,cc.xy(5,16));

      spMIDIClockRate.setName("spMIDIClockRate");
      SynthSettingsFrm.add(spMIDIClockRate,cc.xy(3,16));

      JLabel jlabel10 = new JLabel();
      jlabel10.setText("Name");
      SynthSettingsFrm.add(jlabel10,cc.xywh(1,1,5,1));

      JLabel jlabel11 = new JLabel();
      jlabel11.setText("MIDI Channel");
      SynthSettingsFrm.add(jlabel11,cc.xy(1,4));

      JLabel jlabel12 = new JLabel();
      jlabel12.setText("MIDI Clock");
      SynthSettingsFrm.add(jlabel12,cc.xywh(1,11,5,1));

      JLabel jlabel13 = new JLabel();
      jlabel13.setText("Master Tune");
      SynthSettingsFrm.add(jlabel13,cc.xywh(1,19,5,1));

      JLabel jlabel14 = new JLabel();
      jlabel14.setText("MIDI velocity scale");
      SynthSettingsFrm.add(jlabel14,cc.xywh(1,22,5,1));

      JLabel jlabel15 = new JLabel();
      jlabel15.setText("Program change");
      SynthSettingsFrm.add(jlabel15,cc.xywh(1,26,5,1));

      JLabel jlabel16 = new JLabel();
      jlabel16.setText("Keyboard mode");
      SynthSettingsFrm.add(jlabel16,cc.xywh(7,4,5,1));

      JLabel jlabel17 = new JLabel();
      jlabel17.setText("Knob mode");
      SynthSettingsFrm.add(jlabel17,cc.xywh(7,11,5,1));

      JLabel jlabel18 = new JLabel();
      jlabel18.setText("Pedal polarity");
      SynthSettingsFrm.add(jlabel18,cc.xywh(7,15,5,1));

      addFillComponents(SynthSettingsFrm,new int[]{ 2,3,4,5,6,7,8,9,10,11 },new int[]{ 2,3,9,10,12,13,14,15,16,17,18,20,21,23,24,25,27,28 });
      return SynthSettingsFrm;
   }

   /**
    * Initializer
    */
   protected void initializePanel()
   {
      setLayout(new BorderLayout());
      add(createSynthSettingsFrm(), BorderLayout.CENTER);
   }


}
