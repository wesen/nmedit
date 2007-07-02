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

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.text.html.HTMLEditorKit;

public class NomadAboutDialogFrmHandler extends NomadAboutDialogFrm
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -6904912098189841299L;
    private NomadAboutDialogFrm form;

    public NomadAboutDialogFrmHandler()
    {
        this.form = this;
    }
    
    public void setLicenseVisible()
    {
        selectTab(1);
    }
    
    public void setAboutTextVisible()
    {
        selectTab(0);
    }
    
    private void selectTab(int index)
    {
        JTabbedPane tp = form.m_jtabbedpane1;
        
        if (index>= 0 && index<tp.getTabCount())
            tp.setSelectedIndex(index);
    }

    public JEditorPane getAboutEditorPane()
    {
        return form.m_epAbout;
    }

    public JTextArea getLicenseTextArea()
    {
        return form.m_epLicense;
    }
    
    public static void main(String[] args) throws Exception
    {
        NomadAboutDialogFrmHandler form = new NomadAboutDialogFrmHandler();

        form.getLicenseTextArea().setText("This is the license text area.\n"+form);
        form.getAboutEditorPane().setEditorKit(new HTMLEditorKit());
        form.getAboutEditorPane().setText("<html><head><style type=\"text/css\">"
            //    +"/*<![CDATA[*/"
                +"body{font-family:monospaced;font-size:10px;color:#AAA;}"
                +"h1{font-family:monospaced;font-size:12px;color:#AAA;}"
              //  +"/*]]>*/" 
                +"</style></head><body><h1>Heading</h1>This is the <b>about</b> <u>html</u> text.<br/>"+form+"</body></html>");
        
        JFrame f = new JFrame("About");
        f.setBounds(20, 20, 400, 300);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().setLayout(new BorderLayout());
        f.getContentPane().add(form, BorderLayout.CENTER);
        f.setVisible(true);
    }
    
}
