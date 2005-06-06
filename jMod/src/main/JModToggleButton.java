package main;

import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class JModToggleButton extends JModParameterObject {

    private JToggleButton b = null;
    
    public final static int LARGE = 1;
    public final static int SMALL = 2;
    
//    public JModSlider() {
//        super();
//    }
    
    /* TODO +Implement listener.
     * min/max values can depent on the state of the toggle button.
     */
    
    public JModToggleButton(int x, int y, int w, int h, Object obj, Parameter newPar) {
        super(0, 1, newPar);

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

        b.setSelected(getValue()==1);
        
        b.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent arg0) {
                setValue(b.isSelected()?1:0);
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
        if (b!=null) b.setSelected(getValue()==1);
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
