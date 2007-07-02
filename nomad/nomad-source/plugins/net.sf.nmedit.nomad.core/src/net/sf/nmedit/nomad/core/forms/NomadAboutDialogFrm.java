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
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;


public class NomadAboutDialogFrm extends JPanel
{
   /**
     * 
     */
    private static final long serialVersionUID = 8136042905180754205L;
JPanel m_NomadAboutDialogFrm = new JPanel();
   JTabbedPane m_jtabbedpane1 = new JTabbedPane();
   JEditorPane m_epAbout = new JEditorPane();
   JTextArea m_epLicense = new JTextArea();

   /**
    * Default constructor
    */
   public NomadAboutDialogFrm()
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

   public JPanel createNomadAboutDialogFrm()
   {
      m_NomadAboutDialogFrm.setName("NomadAboutDialogFrm");
      FormLayout formlayout1 = new FormLayout("FILL:370PX:GROW(1.0)","FILL:DEFAULT:GROW(1.0)");
      CellConstraints cc = new CellConstraints();
      m_NomadAboutDialogFrm.setLayout(formlayout1);

      m_jtabbedpane1.addTab("About",null,createPanel());
      m_jtabbedpane1.addTab("License",null,createPanel1());
      m_NomadAboutDialogFrm.add(m_jtabbedpane1,cc.xy(1,1));

      addFillComponents(m_NomadAboutDialogFrm,new int[0],new int[0]);
      return m_NomadAboutDialogFrm;
   }

   public JPanel createPanel()
   {
      JPanel jpanel1 = new JPanel();
      FormLayout formlayout1 = new FormLayout("FILL:DEFAULT:GROW(1.0)","FILL:DEFAULT:GROW(1.0)");
      CellConstraints cc = new CellConstraints();
      jpanel1.setLayout(formlayout1);

      m_epAbout.setEditable(false);
      m_epAbout.setName("epAbout");
      JScrollPane jscrollpane1 = new JScrollPane();
      jscrollpane1.setViewportView(m_epAbout);
      jscrollpane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      jscrollpane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      jpanel1.add(jscrollpane1,cc.xy(1,1));

      addFillComponents(jpanel1,new int[0],new int[0]);
      return jpanel1;
   }

   public JPanel createPanel1()
   {
      JPanel jpanel1 = new JPanel();
      FormLayout formlayout1 = new FormLayout("FILL:DEFAULT:GROW(1.0)","FILL:DEFAULT:GROW(1.0)");
      CellConstraints cc = new CellConstraints();
      jpanel1.setLayout(formlayout1);

      m_epLicense.setEditable(false);
      m_epLicense.setName("epLicense");
      JScrollPane jscrollpane1 = new JScrollPane();
      jscrollpane1.setViewportView(m_epLicense);
      jscrollpane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      jscrollpane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      jpanel1.add(jscrollpane1,cc.xy(1,1));

      addFillComponents(jpanel1,new int[0],new int[0]);
      return jpanel1;
   }

   /**
    * Initializer
    */
   protected void initializePanel()
   {
      setLayout(new BorderLayout());
      add(createNomadAboutDialogFrm(), BorderLayout.CENTER);
   }


}
