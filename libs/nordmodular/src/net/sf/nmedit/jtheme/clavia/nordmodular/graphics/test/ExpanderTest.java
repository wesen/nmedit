/* Copyright (C) 2007 Julien Pauty
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

package net.sf.nmedit.jtheme.clavia.nordmodular.graphics.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.sf.nmedit.jtheme.clavia.nordmodular.graphics.Expander;




public class ExpanderTest
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

    // the eq
    static Expander sh = new Expander();
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
        
        panC.add(lblPan(1,new ThresholdChanger()));
        panC.add(lblPan(2,new RatioChanger()));
        panC.add(lblPan(3,new GateChanger()));
       
//        panC.add(lblPan(4,new ReleaseChanger()));

        
//        panC.add(new JLabel("curve:"));
//        for (int i=0;i<sh.getSegmentCount()-1;i++)
//                panC.add(lblPan(i,new CurveTypeChange(i)));
//
//        JCheckBox cb = new JCheckBox("inverse");
//        panC.add(cb);
//        cb.addActionListener(new ActionListener(){
//
//            public void actionPerformed( ActionEvent e )
//            {
//                sh.setIsInverse( ((JCheckBox)e.getSource()).isSelected());
//                pp.repaint();
//            }});
//        JCheckBox cf = new JCheckBox("fill");
//        panC.add(cf);
//        cf.addActionListener(new ActionListener(){
//
//            public void actionPerformed( ActionEvent e )
//            {
//                doFill = ((JCheckBox)e.getSource()).isSelected();
//                pp.repaint();
//            }});
        
        return pan;
    }
    
    static class EnvelopeDisplay extends JComponent
    {
        /**
         * 
         */
        private static final long serialVersionUID = -6283820502645930485L;
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

            /*if (doFill)
            {
                //sh.setFillEnabled(true);
                g2.setColor(fillColor);      
            	g2.fill(sh);
            }*/
//            sh.setFillEnabled(false);
            g2.setColor(Color.decode("#B0B0B0"));
        //   g2.drawLine(0, getHeight(), getWidth(), 0);            g2.drawLine(-1, (int)((1-sh.getRefLevel())*getHeight()), getWidth()+1, (int)((1-sh.getRefLevel())*getHeight()));
        //   g2.drawLine((int)((sh.getRefLevel())*getWidth()), -1, (int)((sh.getRefLevel())*getWidth()), getHeight()+1);
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
    
      
    
    static class ThresholdChanger extends JSlider implements ChangeListener
    {
        /**
         * 
         */
        private static final long serialVersionUID = -3740630414671160171L;
        public ThresholdChanger()
        {
        	super(JSlider.HORIZONTAL, 0,200, 0);
            setValue((int)(sh.getThreshold()*200f));
            addChangeListener(this);
        }
        public void stateChanged( ChangeEvent e )
        {
            sh.setThreshold(getValue()/200f);
            pp.repaint();
        }
    }
//    
    static class RatioChanger extends JSlider implements ChangeListener
    {
        /**
         * 
         */
        private static final long serialVersionUID = 8118802460111531023L;
        public RatioChanger()
        {
        	super(JSlider.HORIZONTAL,0,66,0);
            setValue((int)(sh.getRatio()));
            addChangeListener(this);
        }
        public void stateChanged( ChangeEvent e )
        {
            sh.setRatio(getValue());
            pp.repaint();
        }
    }
    static class GateChanger extends JSlider implements ChangeListener
    {
        /**
         * 
         */
        private static final long serialVersionUID = 8118802460111531023L;
        public GateChanger()
        {
        	super(JSlider.HORIZONTAL,0,200,0);
            setValue((int)(sh.getGate()*200f));
            addChangeListener(this);
        }
        public void stateChanged( ChangeEvent e )
        {
            sh.setGate(getValue()/200f);
            pp.repaint();
        }
    }
//    
    
}




