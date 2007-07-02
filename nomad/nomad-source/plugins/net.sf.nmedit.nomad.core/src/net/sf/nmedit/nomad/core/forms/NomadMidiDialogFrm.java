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
package net.sf.nmedit.nomad.core.forms;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;


public class NomadMidiDialogFrm extends JPanel
{
   /**
     * 
     */
    private static final long serialVersionUID = -8229919657319024068L;
JComboBox m_cbOutDevices = new JComboBox();
   JLabel m_lblInVendor = new JLabel();
   JLabel m_lblInVersion = new JLabel();
   JLabel m_lblOutVendor = new JLabel();
   JComboBox m_cbInDevices = new JComboBox();
   JLabel m_lblOutDescription = new JLabel();
   JLabel m_lblInDescription = new JLabel();
   JLabel m_lblOutVersion = new JLabel();
   JButton m_btnRefresh = new JButton();
   JSeparator m_jseparator1 = new JSeparator();
   JSeparator m_jseparator2 = new JSeparator();

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
      FormLayout formlayout1 = new FormLayout("FILL:DEFAULT:NONE,FILL:DEFAULT:NONE,FILL:DEFAULT:GROW(1.0)","CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE");
      CellConstraints cc = new CellConstraints();
      jpanel1.setLayout(formlayout1);

      m_cbOutDevices.setName("cbOutDevices");
      jpanel1.add(m_cbOutDevices,cc.xy(3,9));

      JLabel jlabel1 = new JLabel();
      jlabel1.setText("Vendor");
      jpanel1.add(jlabel1,cc.xy(1,5));

      JLabel jlabel2 = new JLabel();
      jlabel2.setText("Version");
      jpanel1.add(jlabel2,cc.xy(1,6));

      JLabel jlabel3 = new JLabel();
      jlabel3.setText("Vendor");
      jpanel1.add(jlabel3,cc.xy(1,12));

      JLabel jlabel4 = new JLabel();
      jlabel4.setText("Version");
      jpanel1.add(jlabel4,cc.xy(1,13));

      m_lblInVendor.setName("lblInVendor");
      m_lblInVendor.setText("lblInVendor");
      jpanel1.add(m_lblInVendor,cc.xy(3,5));

      m_lblInVersion.setName("lblInVersion");
      m_lblInVersion.setText("lblInVersion");
      jpanel1.add(m_lblInVersion,cc.xy(3,6));

      m_lblOutVendor.setName("lblOutVendor");
      m_lblOutVendor.setText("lblOutVendor");
      jpanel1.add(m_lblOutVendor,cc.xy(3,12));

      JLabel jlabel5 = new JLabel();
      jlabel5.setText("Device");
      jpanel1.add(jlabel5,cc.xy(1,2));

      m_cbInDevices.setName("cbInDevices");
      jpanel1.add(m_cbInDevices,cc.xy(3,2));

      m_lblOutDescription.setName("lblOutDescription");
      m_lblOutDescription.setText("lblOutDescription");
      jpanel1.add(m_lblOutDescription,cc.xy(3,11));

      JLabel jlabel6 = new JLabel();
      jlabel6.setText("Description");
      jpanel1.add(jlabel6,cc.xy(1,11));

      JLabel jlabel7 = new JLabel();
      jlabel7.setText("Description");
      jpanel1.add(jlabel7,cc.xy(1,4));

      m_lblInDescription.setName("lblInDescription");
      m_lblInDescription.setText("lblInDescription");
      jpanel1.add(m_lblInDescription,cc.xy(3,4));

      JLabel jlabel8 = new JLabel();
      jlabel8.setText("Device");
      jpanel1.add(jlabel8,cc.xy(1,9));

      m_lblOutVersion.setName("lblOutVersion");
      m_lblOutVersion.setText("lblOutVersion");
      jpanel1.add(m_lblOutVersion,cc.xy(3,13));

      JLabel jlabel9 = new JLabel();
      jlabel9.setText("Output");
      jpanel1.add(jlabel9,cc.xy(1,8));

      JLabel jlabel10 = new JLabel();
      jlabel10.setText("Input");
      jpanel1.add(jlabel10,cc.xy(1,1));

      m_btnRefresh.setActionCommand("Refresh");
      m_btnRefresh.setName("btnRefresh");
      m_btnRefresh.setText("Refresh");
      jpanel1.add(m_btnRefresh,new CellConstraints(3,15,1,1,CellConstraints.RIGHT,CellConstraints.DEFAULT));

      jpanel1.add(m_jseparator1,cc.xy(3,1));

      jpanel1.add(m_jseparator2,cc.xy(3,8));

      addFillComponents(jpanel1,new int[]{ 2 },new int[]{ 3,7,10,14,15,16 });
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
