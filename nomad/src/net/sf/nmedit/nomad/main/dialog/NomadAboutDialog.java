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
 * Created on Feb 14, 2006
 */
package net.sf.nmedit.nomad.main.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.sf.nmedit.nomad.core.application.Application;
import net.sf.nmedit.nomad.main.resources.AppIcons;
import net.sf.nmedit.nomad.theme.plugin.ThemePluginManager;
import net.sf.nmedit.nomad.theme.plugin.ThemePluginProvider;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;

public class NomadAboutDialog extends NomadDialog implements MouseListener {

    private JTabbedPane tabbedPane;
    
	public NomadAboutDialog() 
    {
		setTitle("About");
        setImage(AppIcons.IC_NORDMODULAR);
        setPackingEnabled(false);
        setPreferredSize(new Dimension(540,300));
		setScrollbarEnabled(false);
        
        tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(null);
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, tabbedPane);
        tabbedPane.addTab("About", buildAboutComponent());
        tabbedPane.addTab("License", buildLicenseComponent());
	}

    private JComponent buildAboutComponent()
    {
        JPanel panel = new JPanel();

        FormLayout layout = new FormLayout("pref, 20px, pref:grow", "");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout, panel);
        builder.setDefaultDialogBorder();
        
        builder.appendSeparator("Nomad");
        addTextProperty(builder, "Version", Application.getVersion());
        builder.append("Homepage", createCopyTextLabel("http://nmedit.sourceforge.net"));

        builder.appendSeparator("Programming");
        addTextProperty(builder, "Marcus Andersson");
        addTextProperty(builder, "Christian Schneider");
        addTextProperty(builder, "Ian Hoogeboom");
        
        builder.appendSeparator("Reverse Engineering");
        addTextProperty(builder, "Marcus Andersson");
        addTextProperty(builder, "Jan Punter");
        
        builder.appendSeparator("Artwork");
        builder.append("Icons", createCopyTextLabel("http://jimmac.musichall.cz/"));
        addTextProperty(builder, "Icons", "Christian Schneider");
        addTextProperty(builder, "Splash Screen", "Tobias Weinald");
        
        for (int i=0;i<ThemePluginManager.getPluginCount();i++)
        {
            ThemePluginProvider plugin = ThemePluginManager.getPlugin(i);
            builder.appendSeparator(plugin.getName());

            addTextProperty(builder, "Description", plugin.getDescription());
            addTextProperty(builder, "Version", plugin.getVersion());
            builder.append("Homepage", createCopyTextLabel(plugin.getHomepage()));
            for (String author : plugin.getAuthors())
            {
                addTextProperty(builder, author);
            }
        }
        
        return new JScrollPane(panel);
    }
    
    private JComponent buildLicenseComponent()
    {
        InputStream is = getClass().getResourceAsStream("/gpl.txt");
        StringBuffer license = new StringBuffer();
        if (is!=null)
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            try
            {
                while ((line=br.readLine())!=null)
                {
                    license.append(line);
                    license.append("\n");
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
        JTextArea area = new JTextArea(license.toString());
        area.setFont(new Font("monospaced", Font.PLAIN, getFont().getSize()));
        area.setEditable(false);
        return new JScrollPane(area);
    }
    
    private void addTextProperty(DefaultFormBuilder builder, String title, String text)
    {
        builder.append(title, new JLabel(text));
    }
    
    private void addTextProperty(DefaultFormBuilder builder,String text)
    {
        addTextProperty(builder, "", text);
    }
	
    private JComponent createCopyTextLabel(String text)
    {
        JTextField tf = new JTextField(text);
        tf.setEditable(false);
        tf.setOpaque(false);
        tf.setBorder(null);
        tf.addMouseListener(this);
        return tf;
    }
    
    public void invokeAboutDialog() {
        super.invoke(new String[]{":Close"});
    }

    public void invokeLicenseDialog() {
        tabbedPane.setSelectedIndex(1);
        super.invoke(new String[]{":Close"});
    }

    private static class CopyTextPopupBuilder implements ActionListener 
    {
        private final JTextField field;

        public CopyTextPopupBuilder( JTextField field )
        {
            this.field = field;
        }

        public static void buildAndShowPopup( JTextField field, MouseEvent e )
        {
            JPopupMenu popup = new JPopupMenu();
            CopyTextPopupBuilder handler = new CopyTextPopupBuilder(field);
            popup.add("Copy").addActionListener(handler);
            popup.show(field, e.getX(), e.getY());
        }

        public void actionPerformed( ActionEvent e )
        {
            field.setSelectionStart(0);
            field.setSelectionEnd(field.getText().length());   
            field.copy();
        }
    }

    public void mousePressed( MouseEvent e )
    { 
        if (e.isPopupTrigger() && (e.getComponent() instanceof JTextField))
        {
            CopyTextPopupBuilder.buildAndShowPopup((JTextField) e.getComponent(), e);
        }
    }

    public void mouseClicked( MouseEvent e )
    { }

    public void mouseReleased( MouseEvent e )
    { }

    public void mouseEntered( MouseEvent e )
    { }

    public void mouseExited( MouseEvent e )
    { } 
}
