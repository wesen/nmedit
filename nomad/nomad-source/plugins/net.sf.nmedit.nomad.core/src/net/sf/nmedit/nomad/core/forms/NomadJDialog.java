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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public class NomadJDialog extends JDialog
{

    /**
     * 
     */
    private static final long serialVersionUID = -4445330763131454925L;

    public NomadJDialog()
    {
        super();
    }

    public NomadJDialog(Frame owner)
    {
        super(owner);
    }

    public NomadJDialog(Dialog owner)
    {
        super(owner);
    }

    public NomadJDialog(Frame owner, boolean modal)
    {
        super(owner, modal);
    }

    public NomadJDialog(Frame owner, String title)
    {
        super(owner, title);
    }

    public NomadJDialog(Dialog owner, boolean modal)
    {
        super(owner, modal);
    }

    public NomadJDialog(Dialog owner, String title)
    {
        super(owner, title);
    }

    public NomadJDialog(Frame owner, String title, boolean modal)
    {
        super(owner, title, modal);
    }

    public NomadJDialog(Dialog owner, String title, boolean modal)
    {
        super(owner, title, modal);
    }

    public NomadJDialog(Frame owner, String title, boolean modal,
            GraphicsConfiguration gc)
    {
        super(owner, title, modal, gc);
    }

    public NomadJDialog(Dialog owner, String title, boolean modal,
            GraphicsConfiguration gc)
    {
        super(owner, title, modal, gc);
    }

    public TitlePane createTitlePane(String title, Icon icon)
    {
        TitlePane tp = new TitlePane(title, icon);
        getContentPane().add(tp, BorderLayout.NORTH);
        return tp;
    }
    
    public static class TitlePane extends JPanel
    {
        /**
         * 
         */
        private static final long serialVersionUID = -9180986567660904009L;
        private JLabel lblTitle;
        private JLabel lblIcon;
        private JSeparator separator;

        public TitlePane(String title, Icon icon)
        {
            lblTitle = new JLabel(title);
            lblIcon = new JLabel();
            if (icon != null) lblIcon.setIcon(icon);

            lblTitle.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
            lblIcon.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
            
            setBackground(Color.WHITE);
            separator = new JSeparator(JSeparator.HORIZONTAL);
            
            setLayout(new BorderLayout());
            add(separator, BorderLayout.SOUTH);
            add(lblTitle, BorderLayout.WEST);
            add(lblIcon, BorderLayout.EAST);
        }
        
        public JSeparator getSeparator()
        {
            return separator;
        }
        
        public JLabel getTitleLabel()
        {
            return lblTitle;
        }
        
        public JLabel getIconLabel()
        {
            return lblIcon;
        }
        
    }
    
    /*
    // for testing
    public static void main(String[] args)
    {
        BufferedImage bi = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bi.createGraphics();
        try
        {
            g2.setColor(Color.BLUE);
            g2.drawRect(0, 0, 31, 31);
            g2.drawLine(0, 0, 31, 31);
            g2.drawLine(31, 0, 0, 31);
        }
        finally
        {
            g2.dispose();
        }
        
        TitlePane bar = new TitlePane("Title", new ImageIcon(bi));
        
        JFrame f = new JFrame("TitleBar Test");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setBounds(20,20, 400, 300);
        f.getContentPane().setLayout(new BorderLayout());
        f.getContentPane().add(new JPanel(), BorderLayout.CENTER);
        f.getContentPane().add(bar, BorderLayout.NORTH);
        f.setVisible(true);
    }
    */
    
}
