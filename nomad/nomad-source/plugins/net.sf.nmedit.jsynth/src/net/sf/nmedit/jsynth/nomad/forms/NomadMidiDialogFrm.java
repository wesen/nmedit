/* Copyright (C) 2007 Christian Schneider
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
package net.sf.nmedit.jsynth.nomad.forms;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class NomadMidiDialogFrm extends JPanel
{
   JLabel lblInVendor = new JLabel();
   JLabel lblInVersion = new JLabel();
   JLabel lblOutVendor = new JLabel();
   JLabel lblOutDescription = new JLabel();
   JLabel lblInDescription = new JLabel();
   JLabel lblOutVersion = new JLabel();
   JComboBox cbOutDevices = new JComboBox();
   JComboBox cbInDevices = new JComboBox();

   /**
    * Default constructor
    */
   public NomadMidiDialogFrm()
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
      FormLayout formlayout1 = new FormLayout("FILL:DEFAULT:NONE,FILL:8DLU:NONE,FILL:DEFAULT:GROW(1.0)","CENTER:DEFAULT:NONE,CENTER:4DLU:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:4DLU:NONE,CENTER:DEFAULT:NONE,CENTER:4DLU:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
      CellConstraints cc = new CellConstraints();
      jpanel1.setLayout(formlayout1);

      JLabel jlabel1 = new JLabel();
      jlabel1.setText("Vendor");
      jpanel1.add(jlabel1,cc.xy(1,4));

      JLabel jlabel2 = new JLabel();
      jlabel2.setText("Version");
      jpanel1.add(jlabel2,cc.xy(1,5));

      JLabel jlabel3 = new JLabel();
      jlabel3.setText("Vendor");
      jpanel1.add(jlabel3,cc.xy(1,10));

      JLabel jlabel4 = new JLabel();
      jlabel4.setText("Version");
      jpanel1.add(jlabel4,cc.xy(1,11));

      lblInVendor.setName("lblInVendor");
      lblInVendor.setText("lblInVendor");
      jpanel1.add(lblInVendor,cc.xy(3,4));

      lblInVersion.setName("lblInVersion");
      lblInVersion.setText("lblInVersion");
      jpanel1.add(lblInVersion,cc.xy(3,5));

      lblOutVendor.setName("lblOutVendor");
      lblOutVendor.setText("lblOutVendor");
      jpanel1.add(lblOutVendor,cc.xy(3,10));

      lblOutDescription.setName("lblOutDescription");
      lblOutDescription.setText("lblOutDescription");
      jpanel1.add(lblOutDescription,cc.xy(3,9));

      JLabel jlabel5 = new JLabel();
      jlabel5.setText("Description");
      jpanel1.add(jlabel5,cc.xy(1,9));

      JLabel jlabel6 = new JLabel();
      jlabel6.setText("Description");
      jpanel1.add(jlabel6,cc.xy(1,3));

      lblInDescription.setName("lblInDescription");
      lblInDescription.setText("lblInDescription");
      jpanel1.add(lblInDescription,cc.xy(3,3));

      lblOutVersion.setName("lblOutVersion");
      lblOutVersion.setText("lblOutVersion");
      jpanel1.add(lblOutVersion,cc.xy(3,11));

      JLabel jlabel7 = new JLabel();
      jlabel7.setText("Output");
      jpanel1.add(jlabel7,cc.xy(1,7));

      JLabel jlabel8 = new JLabel();
      jlabel8.setText("Input");
      jpanel1.add(jlabel8,cc.xy(1,1));

      cbOutDevices.setName("cbOutDevices");
      jpanel1.add(cbOutDevices,cc.xy(3,7));

      cbInDevices.setName("cbInDevices");
      jpanel1.add(cbInDevices,cc.xy(3,1));

      addFillComponents(jpanel1,new int[]{ 2 },new int[]{ 2,6,8 });
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
