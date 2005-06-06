package main;

import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class JModRadioButton extends JModParameterObject {
    // The 'groupng' of the radio button is depending on the newPar given!

    private JToggleButton b = null;
    
    public final static int LARGE = 1;
    public final static int SMALL = 2;
    
    boolean start = true;
    boolean startval = false;
    
    /* TODO +Implement listener.
     * min/max values display of other objects can depent on the state of the radio button.
     */
    
    public JModRadioButton(int x, int y, int w, int h, Object obj, int min_val, int max_val, Parameter newPar) {
        super(min_val, max_val, newPar);

        b = new JToggleButton();
        
        if (obj instanceof String) {
            b.setText((String)obj);
            b.setFont(new Font("Dialog", Font.PLAIN, 10));
        }
        
//        ImageIcon icon = new ImageIcon("./grafix/_sync.gif");
//        b = new JToggleButton(new ImageIcon("./grafix/_sync.gif"));
        
        b.setSize(w, h);
        b.setMargin(new Insets(0, 0, 0, 0));
        b.setFocusPainted(false);
        add(b);

        setLocation(x, y);
        setSize(b.getWidth(), b.getHeight());

        b.setSelected(par.getValue()==getMaxValue());
        
        b.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent arg0) {
                if (b.isSelected())
                    setValue(getMaxValue());
                else
                    b.setSelected(true);
            }});
        
        par.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
               int val = (int)((Parameter) e.getSource()).getValue();
               setValueWithoutFireStarter(val);
           }
       });
    }

    public void updateKnob() {
        // Als de waarde in de parameter wordt veranderd, wat moet er dan gebeuren...
        // Bij het zetten van de default waarde, bestaat b nog niet, dus kunnen we hem niet grafisch updaten
        if (b!=null) {
            if (par.getValue()==getMaxValue())
                if (!b.isSelected())
                    b.setSelected(true);
                
            if (par.getValue()!=getMaxValue())
                if (b.isSelected())
                    b.setSelected(false);
        }
    }
}

//package bingo.player;
//import java.awt.*;
//import javax.swing.*;
//import javax.swing.event.*;
//
//class NumberButton extends JToggleButton {
//    static protected Font font;
//    static protected ImageIcon selectedIcon, invisibleIcon;
//    
//    NumberButton(String label) {
//        super(label);
//        setHorizontalTextPosition(AbstractButton.CENTER);
//        setFocusPainted(false);
//        setBorderPainted(false);
//        if (font == null) {
//            font = new Font("serif", Font.BOLD, 24);
//        } setFont(font);
//        if (selectedIcon == null) {
//            selectedIcon = new ImageIcon("chit.gif");
//        }
//        setSelectedIcon(selectedIcon); /* * No selected/pressed/rollover icons get shown unless * the toggle button's default icon is non-null. The * workaround is to create a transparent, full-sized icon * for the default icon. */
//        if (invisibleIcon == null) {
//            invisibleIcon = new ImageIcon("invisible.gif");
//        }
//        setIcon(invisibleIcon);
//    }
//} 
