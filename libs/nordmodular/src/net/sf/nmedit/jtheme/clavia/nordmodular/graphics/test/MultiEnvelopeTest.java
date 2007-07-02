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
import net.sf.nmedit.jtheme.clavia.nordmodular.graphics.MultiEnvelope;


public class MultiEnvelopeTest
{

    public static void main( String[] args )
    {
        JFrame f = new JFrame("Envelope test");        
        f.setBounds(30,30,600,600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().setLayout(new BorderLayout());
        f.getContentPane().add(createENV(),BorderLayout.CENTER);
        f.setVisible(true);
       
       
    }

    // the envelope
    static MultiEnvelope sh = new MultiEnvelope();
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
        
        panC.add(lblPan(1,new LevelChanger(1)));
        panC.add(lblPan(1,new TimeChanger(1)));
        panC.add(lblPan(2,new LevelChanger(2)));
        panC.add(lblPan(2,new TimeChanger(2)));
        panC.add(lblPan(3,new LevelChanger(3)));
        panC.add(lblPan(3,new TimeChanger(3)));
        panC.add(lblPan(4,new LevelChanger(4)));
        panC.add(lblPan(4,new TimeChanger(4)));
        //panC.add(lblPan(5,new LevelChanger(5)));
        panC.add(lblPan(5,new TimeChanger(5)));
        
        panC.add(lblPan(6,new SustainChanger()));
        
        panC.add(new JLabel("curve:"));
        for (int i=0;i<sh.getSegmentCount()-1;i++)
                panC.add(lblPan(i,new CurveTypeChange(i)));

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
        private static final long serialVersionUID = -7144533902038873158L;
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
        private static final long serialVersionUID = -3349234351342699398L;

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
    
    static class LevelChanger extends JSlider implements ChangeListener
    {
    	/**
         * 
         */
        private static final long serialVersionUID = 5178095337548830904L;
        private int segment;
    	
        public LevelChanger(int segment)
        {           	
        	super(JSlider.HORIZONTAL, 0,Envelope.RANGE_MAX, 0);
        	
        	this.segment = segment;
            setValue((int)(sh.getLevel(segment)));
            sh.setLevel(segment,getValue());
            addChangeListener(this);
        }
        public void stateChanged( ChangeEvent e )
        {
            sh.setLevel(segment,getValue());
            pp.repaint();
        }
    }
    
    static class TimeChanger extends JSlider implements ChangeListener
    {
    	/**
         * 
         */
        private static final long serialVersionUID = -4277462784798419347L;
        private int segment;
    	
        public TimeChanger(int segment)
        {   
        	super(JSlider.HORIZONTAL, 0,Envelope.RANGE_MAX, 0);
        	this.segment = segment;
        	
            setValue((int)(sh.getTime(segment)));             
            //sh.setTime(segment,getValue()/100f);            
            addChangeListener(this);
            pp.repaint();
        }
        public void stateChanged( ChangeEvent e )
        {
            sh.setTime(segment,getValue());
//            System.out.println("val" + segment + "="+getValue()+ " val=" + sh.getTime(segment) );
//            System.out.println("val2="+sh.getTime(2));
            pp.repaint();
        }
    }
    
    static class SustainChanger extends JSlider implements ChangeListener
    {
    
        /**
         * 
         */
        private static final long serialVersionUID = 3486762989560437414L;
        public SustainChanger()
        {   
        	super(JSlider.HORIZONTAL, 0,4, 0);        	
        	
            setValue((sh.getSustainSeg()));             
            //sh.setTime(segment,getValue()/100f);            
            addChangeListener(this);
            pp.repaint();
        }
        public void stateChanged( ChangeEvent e )
        {
            sh.setSustainSeg(getValue());
            
//            System.out.println("val" + segment + "="+getValue()+ " val=" + sh.getTime(segment) );
//            System.out.println("val2="+sh.getTime(2));
            pp.repaint();
        }
    }
}
