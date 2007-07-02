/* Copyright (C) 2006 Julien Pauty
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

/*
 * Created on Nov 27, 2006
 */
package net.sf.nmedit.jtheme.clavia.nordmodular.graphics.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.jtheme.clavia.nordmodular.graphics.Envelope;

public class AHDEnvelopeTest
{

    public static void main( String[] args )
    {
        JFrame f = new JFrame("Envelope test");
        f.setBounds(30,30,300,300);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().setLayout(new BorderLayout());
        f.getContentPane().add(createENV(),BorderLayout.CENTER);
        f.setVisible(true);
    }

    // the envelope
    static Envelope.AHDEnvelope sh = new Envelope.AHDEnvelope();
    static JComponent pp;
    static boolean doFill = false;
    static JComponent createENV()
    {
        
         pp = new EnvelopeDisplay();
        
        JComponent pan = new JPanel();
        pan.setLayout(new BorderLayout());
        pan.add(pp, BorderLayout.CENTER);
        JComponent panC = new JPanel();
        pan.add(new JScrollPane(panC), BorderLayout.SOUTH);

        panC.setLayout(new GridLayout(0, 1));
        panC.add(new JLabel("time:"));
        
        panC.add(lblPan(1,new AttackChanger()));
        panC.add(lblPan(1,new HoldChanger()));
        panC.add(lblPan(2,new DecayChanger()));

  

        JCheckBox cb = new JCheckBox("inverse");
        panC.add(cb);
        cb.addActionListener(new ActionListener(){

            public void actionPerformed( ActionEvent e )
            {
                sh.setIsInverse( ((JCheckBox)e.getSource()).isSelected());
                pp.repaint();
            }});
        JCheckBox cf = new JCheckBox("fill");
        panC.add(cf);
        cf.addActionListener(new ActionListener(){

            public void actionPerformed( ActionEvent e )
            {
                doFill = ((JCheckBox)e.getSource()).isSelected();
                pp.repaint();
            }});
        
        return pan;
    }
    
    static class EnvelopeDisplay extends JComponent
    {
        /**
         * 
         */
        private static final long serialVersionUID = 8616893092230992865L;
        final Color fillColor = Color.decode("#C0DCC0");
        
        {
            setOpaque(true);
            setBackground(Color.decode("#008080"));
            setForeground(Color.decode("#00FF00"));
        }

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            sh.setBounds(0, 0, getWidth(), getHeight());

            if (doFill)
            {
                sh.setFillEnabled(true);
                g2.setColor(fillColor);
                g2.fill(sh);
            }
            sh.setFillEnabled(false);
            g2.setColor(getForeground());
            g2.draw(sh);
        }
    }
    
    static JComponent lblPan(int index, JComponent c)
    {
        JPanel p = new JPanel(new BorderLayout());
        JLabel l = new JLabel(""+index);
        l.setLabelFor(c);
        p.add(l, BorderLayout.WEST);
        p.add(c, BorderLayout.CENTER);
        return p;
    }
    
    static class CurveTypeChange extends JComboBox implements ActionListener
    {
        /**
         * 
         */
        private static final long serialVersionUID = -4977436222099398597L;

        final static String[] curveTypes = new String[]{"Lin", "Exp", "Log"};
        
        private int index;

        public CurveTypeChange(int index)
        {
            super(curveTypes);
            this.index = index+1;
            setSelectedIndex(sh.getCurveType(index));
            addActionListener(this);
        }
        public void actionPerformed( ActionEvent e )
        {
            sh.setCurveType(index, getSelectedIndex());        
            pp.repaint();
        }
    }
    
    static class AttackChanger extends JSlider implements ChangeListener
    {
    
        /**
         * 
         */
        private static final long serialVersionUID = 6942079068080708058L;
        public AttackChanger()
        {
        	super(JSlider.HORIZONTAL, 0,Envelope.RANGE_MAX, 0);
            setValue((int)(sh.getAttackTime()));
            addChangeListener(this);
        }
        public void stateChanged( ChangeEvent e )
        {
            sh.setAttackTime(getValue());
            pp.repaint();
        }
    }
    
    static class HoldChanger extends JSlider implements ChangeListener
    {
        /**
         * 
         */
        private static final long serialVersionUID = 1328567361250410328L;
        public HoldChanger()
        {
        	super(JSlider.HORIZONTAL, 0,Envelope.RANGE_MAX, 0);
            setValue((int)(sh.getHoldTime()));
            addChangeListener(this);
        }
        public void stateChanged( ChangeEvent e )
        {
            sh.setHoldTime(getValue());
            pp.repaint();
        }
    }
    
    static class DecayChanger extends JSlider implements ChangeListener
    {
        /**
         * 
         */
        private static final long serialVersionUID = 758284796837855399L;
        public DecayChanger()
        {
        	super(JSlider.HORIZONTAL, 0,Envelope.RANGE_MAX, 0);
            setValue((int)(sh.getDecayTime()));
            addChangeListener(this);
        }
        public void stateChanged( ChangeEvent e )
        {
            sh.setDecayTime(getValue());
            pp.repaint();
        }
    }
    
}
