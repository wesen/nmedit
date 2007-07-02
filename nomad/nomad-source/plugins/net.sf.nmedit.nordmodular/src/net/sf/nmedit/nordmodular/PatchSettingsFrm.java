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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;


public class PatchSettingsFrm extends JPanel
{
   /**
     * 
     */
    private static final long serialVersionUID = -2445985798566960520L;
JSpinner spRequestedVoices = new JSpinner();
   JButton btnGetCurrentNotes = new JButton();
   JSpinner spVelRangeMax = new JSpinner();
   JSpinner spVelRangeMin = new JSpinner();
   JSpinner spKbRangeMin = new JSpinner();
   JSpinner spKbRangeMax = new JSpinner();
   JRadioButton rbPedalModeSustain = new JRadioButton();
   ButtonGroup buttongroup1 = new ButtonGroup();
   JRadioButton rbPedalModeOnOff = new JRadioButton();
   JSpinner spBendRange = new JSpinner();
   JRadioButton rbPortaNormal = new JRadioButton();
   ButtonGroup buttongroup2 = new ButtonGroup();
   JRadioButton rbPortaAuto = new JRadioButton();
   JSpinner spPortaTime = new JSpinner();
   JComboBox cbOctaveShift = new JComboBox();
   JCheckBox cbVoiceRePoly = new JCheckBox();
   JCheckBox cbVoiceReCommon = new JCheckBox();
   JLabel lblCycles = new JLabel();
   JLabel lblProgMem = new JLabel();
   JLabel lblXmem = new JLabel();
   JLabel lblYmem = new JLabel();
   JLabel lblZeroPage = new JLabel();
   JLabel lblDynMem = new JLabel();

   /**
    * Default constructor
    */
   public PatchSettingsFrm()
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

   public JPanel createPanel()
   {
      JPanel jpanel1 = new JPanel();
      FormLayout formlayout1 = new FormLayout("FILL:DEFAULT:NONE,FILL:4DLU:NONE,FILL:DEFAULT:NONE,FILL:4DLU:NONE,FILL:DEFAULT:NONE,FILL:4DLU:NONE,FILL:DEFAULT:NONE,FILL:4DLU:NONE,FILL:DEFAULT:NONE,FILL:4DLU:NONE,FILL:DEFAULT:NONE","CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
      CellConstraints cc = new CellConstraints();
      jpanel1.setLayout(formlayout1);

      JLabel jlabel1 = new JLabel();
      jlabel1.setText("Voices");
      jpanel1.add(jlabel1,cc.xywh(1,1,5,1));

      JLabel jlabel2 = new JLabel();
      jlabel2.setText("Requested");
      jpanel1.add(jlabel2,cc.xy(1,2));

      spRequestedVoices.setName("spRequestedVoices");
      jpanel1.add(spRequestedVoices,cc.xy(3,2));

      JLabel jlabel3 = new JLabel();
      jlabel3.setText("Notes");
      jpanel1.add(jlabel3,cc.xywh(1,4,5,1));

      btnGetCurrentNotes.setActionCommand("Get current notes");
      btnGetCurrentNotes.setName("btnGetCurrentNotes");
      btnGetCurrentNotes.setText("Get current notes");
      jpanel1.add(btnGetCurrentNotes,cc.xy(3,5));

      JLabel jlabel4 = new JLabel();
      jlabel4.setText("Velocity range");
      jpanel1.add(jlabel4,cc.xywh(1,8,5,1));

      JLabel jlabel5 = new JLabel();
      jlabel5.setText("Max");
      jpanel1.add(jlabel5,cc.xy(5,9));

      JLabel jlabel6 = new JLabel();
      jlabel6.setText("Min");
      jpanel1.add(jlabel6,cc.xy(5,10));

      spVelRangeMax.setName("spVelRangeMax");
      jpanel1.add(spVelRangeMax,cc.xy(3,9));

      spVelRangeMin.setName("spVelRangeMin");
      jpanel1.add(spVelRangeMin,cc.xy(3,10));

      JLabel jlabel7 = new JLabel();
      jlabel7.setText("Keyboard range");
      jpanel1.add(jlabel7,cc.xywh(1,12,5,1));

      spKbRangeMin.setName("spKbRangeMin");
      jpanel1.add(spKbRangeMin,cc.xy(3,14));

      spKbRangeMax.setName("spKbRangeMax");
      jpanel1.add(spKbRangeMax,cc.xy(3,13));

      JLabel jlabel8 = new JLabel();
      jlabel8.setText("Min");
      jpanel1.add(jlabel8,cc.xy(5,14));

      JLabel jlabel9 = new JLabel();
      jlabel9.setText("Max");
      jpanel1.add(jlabel9,cc.xy(5,13));

      JLabel jlabel10 = new JLabel();
      jlabel10.setText("Pedal mode");
      jpanel1.add(jlabel10,cc.xywh(1,16,5,1));

      rbPedalModeSustain.setActionCommand("Sustain");
      rbPedalModeSustain.setName("rbPedalModeSustain");
      rbPedalModeSustain.setText("Sustain");
      buttongroup1.add(rbPedalModeSustain);
      jpanel1.add(rbPedalModeSustain,cc.xy(3,17));

      rbPedalModeOnOff.setActionCommand("On/Off");
      rbPedalModeOnOff.setName("rbPedalModeOnOff");
      rbPedalModeOnOff.setText("On/Off");
      buttongroup1.add(rbPedalModeOnOff);
      jpanel1.add(rbPedalModeOnOff,cc.xy(3,18));

      JLabel jlabel11 = new JLabel();
      jlabel11.setText("Bend Range");
      jpanel1.add(jlabel11,cc.xywh(7,1,5,1));

      spBendRange.setName("spBendRange");
      jpanel1.add(spBendRange,cc.xy(9,2));

      JLabel jlabel12 = new JLabel();
      jlabel12.setText("Semitones");
      jpanel1.add(jlabel12,cc.xy(11,2));

      JLabel jlabel13 = new JLabel();
      jlabel13.setText("Portamento");
      jpanel1.add(jlabel13,cc.xywh(7,4,5,1));

      rbPortaNormal.setActionCommand("Normal");
      rbPortaNormal.setName("rbPortaNormal");
      rbPortaNormal.setText("Normal");
      buttongroup2.add(rbPortaNormal);
      jpanel1.add(rbPortaNormal,cc.xy(9,5));

      rbPortaAuto.setActionCommand("Auto");
      rbPortaAuto.setName("rbPortaAuto");
      rbPortaAuto.setText("Auto");
      buttongroup2.add(rbPortaAuto);
      jpanel1.add(rbPortaAuto,cc.xy(9,6));

      spPortaTime.setName("spPortaTime");
      jpanel1.add(spPortaTime,cc.xy(9,9));

      JLabel jlabel14 = new JLabel();
      jlabel14.setText("Time");
      jpanel1.add(jlabel14,cc.xy(11,9));

      JLabel jlabel15 = new JLabel();
      jlabel15.setText("Octave shift");
      jpanel1.add(jlabel15,cc.xywh(7,12,5,1));

      cbOctaveShift.setName("cbOctaveShift");
      cbOctaveShift.addItem("+2");
      cbOctaveShift.addItem("+1");
      cbOctaveShift.addItem("0");
      cbOctaveShift.addItem("-1");
      cbOctaveShift.addItem("-2");
      jpanel1.add(cbOctaveShift,cc.xy(9,13));

      JLabel jlabel16 = new JLabel();
      jlabel16.setText("Voice retrigger");
      jpanel1.add(jlabel16,cc.xywh(7,16,5,1));

      cbVoiceRePoly.setActionCommand("Poly");
      cbVoiceRePoly.setName("cbVoiceRePoly");
      cbVoiceRePoly.setText("Poly");
      jpanel1.add(cbVoiceRePoly,cc.xy(9,17));

      cbVoiceReCommon.setActionCommand("Common");
      cbVoiceReCommon.setName("cbVoiceReCommon");
      cbVoiceReCommon.setText("Common");
      jpanel1.add(cbVoiceReCommon,cc.xy(9,18));

      JLabel jlabel17 = new JLabel();
      jlabel17.setText("Resources used (PVA / CVA in %)");
      jpanel1.add(jlabel17,cc.xywh(1,20,8,1));

      JLabel jlabel18 = new JLabel();
      jlabel18.setText("Cycles:");
      jpanel1.add(jlabel18,cc.xy(1,22));

      JLabel jlabel19 = new JLabel();
      jlabel19.setText("Prog. mem:");
      jpanel1.add(jlabel19,cc.xy(1,23));

      JLabel jlabel20 = new JLabel();
      jlabel20.setText("X mem:");
      jpanel1.add(jlabel20,cc.xy(1,24));

      JLabel jlabel21 = new JLabel();
      jlabel21.setText("Y mem:");
      jpanel1.add(jlabel21,cc.xy(1,25));

      lblCycles.setName("lblCycles");
      lblCycles.setText("JLabel");
      jpanel1.add(lblCycles,cc.xy(3,22));

      lblProgMem.setName("lblProgMem");
      lblProgMem.setText("JLabel");
      jpanel1.add(lblProgMem,cc.xy(3,23));

      lblXmem.setName("lblXmem");
      lblXmem.setText("JLabel");
      jpanel1.add(lblXmem,cc.xy(3,24));

      lblYmem.setName("lblYmem");
      lblYmem.setText("JLabel");
      jpanel1.add(lblYmem,cc.xy(3,25));

      JLabel jlabel22 = new JLabel();
      jlabel22.setText("Zero page:");
      jpanel1.add(jlabel22,cc.xy(7,22));

      JLabel jlabel23 = new JLabel();
      jlabel23.setText("Dyn mem:");
      jpanel1.add(jlabel23,cc.xy(7,23));

      lblZeroPage.setName("lblZeroPage");
      lblZeroPage.setText("JLabel");
      jpanel1.add(lblZeroPage,cc.xy(9,22));

      lblDynMem.setName("lblDynMem");
      lblDynMem.setText("JLabel");
      jpanel1.add(lblDynMem,cc.xy(9,23));

      addFillComponents(jpanel1,new int[]{ 2,3,4,5,6,8,9,10,11 },new int[]{ 3,5,6,7,9,10,11,13,14,15,17,18,19,21 });
      return jpanel1;
   }

   /**
    * Initializer
    */
   protected void initializePanel()
   {
      setLayout(new BorderLayout());
      add(createPanel(), BorderLayout.CENTER);
   }


}
