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
 * Created on Nov 19, 2006
 */
package example.menulayout;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import net.sf.nmedit.nomad.core.i18n.LocaleConfiguration;
import net.sf.nmedit.nomad.core.menulayout.MLEntry;
import net.sf.nmedit.nomad.core.menulayout.MenuBuilder;
import net.sf.nmedit.nomad.core.menulayout.MenuLayout;

public class MenuExample
{

    private static LocaleConfiguration localeConf;

    final static String layoutSrc = "example/menulayout/MenuLayout.xml";
    final static String bundleSrc = "example/menulayout/MessageBundle";
    
    private static MenuBuilder theMenuBuilder;
    
    public static void main(String[] args) throws Exception
    {
        localeConf = LocaleConfiguration.getLocaleConfiguration();
        localeConf.setCurrentLocale(Locale.ENGLISH);
        localeConf.addLocaleChangeListener(new PropertyChangeListener(){
            public void propertyChange( PropertyChangeEvent evt )
            {
                if (evt.getPropertyName()==LocaleConfiguration.LOCALE_PROPERTY)
                {
                    System.out.println("Locale changed: "+evt.getOldValue()+" to "+evt.getNewValue());
                    ResourceBundle bundle = ResourceBundle.getBundle(bundleSrc, (Locale) evt.getNewValue());
                    theMenuBuilder.setResourceBundle(bundle);
                }
            }});
        
        MenuLayout ly = MenuLayout.getLayout(MenuExample.class.getResourceAsStream(layoutSrc));
        ResourceBundle rb = ResourceBundle.getBundle(bundleSrc);
        
        createApp(theMenuBuilder = new MenuBuilder(ly, rb));
    }

    private static void createApp( MenuBuilder menuFactory )
    {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(400, 300);
        f.setLocation(20, 20);
        f.setJMenuBar(menuFactory.createMenuBar("MainWindow.menu"));
        
        f.getContentPane().setLayout(new BorderLayout());
        JTextArea ta = new JTextArea("right-click to see 'MainWindow.menu.file.new' as popup");
        f.getContentPane().add(ta, BorderLayout.CENTER);
        ta.setComponentPopupMenu(menuFactory.createPopup("MainWindow.menu.file.new"));

        installActions(f, menuFactory.getLayout());
        
        menuFactory.addActionListener("MainWindow.menu.file.exit", new ActionListener(){
            public void actionPerformed( ActionEvent e )
            {
                System.exit(0);
            }});

        menuFactory.addActionListener("MainWindow.menu.locale.switch", new ActionListener(){
            public void actionPerformed( ActionEvent e )
            {
                System.out.println("Changing locale...");
                if (localeConf.getCurrentLocale()==Locale.ENGLISH)
                    localeConf.setCurrentLocale(Locale.GERMANY);
                else
                    localeConf.setCurrentLocale(Locale.ENGLISH);
            }});
        
        f.setVisible(true);
    }

    private static void installActions( Component parent, MenuLayout layout )
    {
        ActionListener l = new ActionNotifier(parent);
        Iterator<MLEntry> i = layout.getRoot().bfsIterator();
        while (i.hasNext())
        {
            MLEntry e = i.next();
            if (!e.getGlobalEntryPoint().equals("MainWindow.menu.locale.switch"))
                    e.addActionListener(l);
        }
    }
    
    private static class ActionNotifier implements ActionListener
    {

        private Component parent;

        public ActionNotifier( Component parent )
        {
            this.parent = parent;
        }

        public void actionPerformed( ActionEvent e )
        {
            if (e.getSource() instanceof JMenuItem)
            {
                MLEntry mlEntry = (MLEntry)((JMenuItem) e.getSource()).getAction();
                JOptionPane.showMessageDialog(parent, mlEntry.getGlobalEntryPoint());
            }
        }
        
    }
    
}
