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

/*
 * Created on Apr 28, 2006
 */
package net.sf.nmedit.nomad.main.ui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class HeaderSection extends JComponent
{
/*
    final static Color gradT1 = Color.decode("#7B8296");
    final static Color gradT2 = Color.decode("#A5B0C9");
*/
    final static Color gradT1 = Color.decode("#CFB651");
    final static Color gradT2 = Color.decode("#FFD763");

    final static Color gradB1 = Color.decode("#CED4E2");
    final static Color gradB2 = Color.decode("#C2C9D9");

    private JComponent header;

    private JPanel 
    contentPane;

    private HeaderSection( JComponent header )
    {
        this.header = header;
        setOpaque(false);
        setLayout(new BorderLayout());

        contentPane = new JPanel();
        contentPane.setOpaque(false);
        contentPane.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
                
        /*
        HeaderContainer h = new HeaderContainer();
        h.add(header);*/
        
        add(header, BorderLayout.NORTH);
        add(contentPane, BorderLayout.CENTER);   
    }
    
    public JComponent getHeader()
    {
        return header;
    }
    
    public HeaderSection( String title )
    {  
        this(new HeaderLabel(title));
        JLabel lblTitle = (JLabel) header;
        lblTitle.setBorder(BorderFactory.createEmptyBorder(2,4,2,2));
        lblTitle.setOpaque(false);
    }

    public JComponent getContentPane()
    {
        return contentPane;
    }
/*
    private static class HeaderContainer extends JComponent
    {
        public HeaderContainer()
        {
            setLayout(new GridLayout(1,1));
            setOpaque(true);
        }

        protected void paintComponent(Graphics g)
        {
            Graphics2D g2 = (Graphics2D) g;
            GradientPaint gp;
            Paint p = g2.getPaint();

            gp = new GradientPaint(0, 0, gradT1, 0, 6, gradT2, false);
            g2.setPaint(gp);
            g.fillRect(0, 0, getWidth(), getHeight());
            
            /*
            //gp = new GradientPaint(0, y, gradB1, 0, getHeight(), gradB2);
            //g2.setPaint(gp);
            //g.fillRect(0, y, getWidth(), getHeight());
            
            
            g2.setPaint(p);
        }
        
   }*/
    
}
