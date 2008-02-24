package net.sf.nmedit.jsynth.nomad.forms;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class SaveInSynthFrm extends JPanel
{
   /**
     * 
     */
    private static final long serialVersionUID = -6750958734378239943L;
JComboBox cbSynth = new JComboBox();
   JComboBox cbSlot = new JComboBox();
   JComboBox cbBank = new JComboBox();
   JCheckBox cbSaveInSlot = new JCheckBox();
   JCheckBox cbSaveInBank = new JCheckBox();
   JButton btnCancel = new JButton();
   JButton btnSave = new JButton();
   JCheckBox cbOpenPatch = new JCheckBox();
   JLabel lblIcon = new JLabel();

   /**
    * Default constructor
    */
   public SaveInSynthFrm()
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
      FormLayout formlayout1 = new FormLayout("FILL:DEFAULT:NONE,FILL:8DLU:NONE,FILL:DEFAULT:NONE,FILL:4DLU:NONE,FILL:DEFAULT:GROW(1.0)","CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:NONE,CENTER:DEFAULT:NONE,CENTER:DEFAULT:NONE,CENTER:2DLU:GROW(1.0),CENTER:DEFAULT:NONE");
      CellConstraints cc = new CellConstraints();
      jpanel1.setLayout(formlayout1);

      JLabel jlabel1 = new JLabel();
      jlabel1.setText("Synthesizer");
      jpanel1.add(jlabel1,cc.xy(3,1));

      cbSynth.setName("cbSynth");
      jpanel1.add(cbSynth,cc.xy(5,1));

      cbSlot.setName("cbSlot");
      jpanel1.add(cbSlot,cc.xy(5,3));

      cbBank.setName("cbBank");
      jpanel1.add(cbBank,cc.xy(5,5));

      cbSaveInSlot.setActionCommand("Save In Slot");
      cbSaveInSlot.setName("cbSaveInSlot");
      cbSaveInSlot.setText("Save In Slot");
      jpanel1.add(cbSaveInSlot,cc.xy(3,3));

      cbSaveInBank.setActionCommand("Save In Bank");
      cbSaveInBank.setName("cbSaveInBank");
      cbSaveInBank.setText("Save In Bank");
      jpanel1.add(cbSaveInBank,cc.xy(3,5));

      jpanel1.add(createPanel1(),new CellConstraints(5,8,1,1,CellConstraints.RIGHT,CellConstraints.DEFAULT));
      cbOpenPatch.setActionCommand("Open Patch");
      cbOpenPatch.setName("cbOpenPatch");
      cbOpenPatch.setText("Open Patch");
      jpanel1.add(cbOpenPatch,cc.xy(3,6));

      lblIcon.setName("lblIcon");
      lblIcon.setText("JLabel");
      jpanel1.add(lblIcon,new CellConstraints(1,1,1,8,CellConstraints.DEFAULT,CellConstraints.TOP));

      addFillComponents(jpanel1,new int[]{ 2,4 },new int[]{ 2,3,4,5,6,7,8 });
      return jpanel1;
   }

   public JPanel createPanel1()
   {
      JPanel jpanel1 = new JPanel();
      FormLayout formlayout1 = new FormLayout("FILL:DEFAULT:NONE,FILL:4DLU:NONE,FILL:DEFAULT:NONE","CENTER:DEFAULT:NONE");
      CellConstraints cc = new CellConstraints();
      jpanel1.setLayout(formlayout1);

      btnCancel.setActionCommand("JButton");
      btnCancel.setName("btnCancel");
      btnCancel.setText("Cancel");
      jpanel1.add(btnCancel,cc.xy(3,1));

      btnSave.setActionCommand("JButton");
      btnSave.setName("btnSave");
      btnSave.setText("Save");
      jpanel1.add(btnSave,cc.xy(1,1));

      addFillComponents(jpanel1,new int[]{ 2 },new int[0]);
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
