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
package net.sf.nmedit.nomad.core.jpf;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import org.java.plugin.PluginManager;
import org.java.plugin.registry.PluginAttribute;
import org.java.plugin.registry.PluginDescriptor;

public class JPFPluginDialog extends JDialog
{

    private PluginManager manager;
    
    private PluginEntry selectedEntry;

    public JPFPluginDialog(JFrame owner,PluginManager manager)
    {
        super(owner);
        this.manager = manager;
    }
    
    public JPFPluginDialog(JFrame owner)
    {
        this(owner, PluginManager.lookup(JPFPluginDialog.class));
        
        JPanel cont = new JPanel();
        cont.setLayout(new GridLayout(0, 1));
        addEntries(cont);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new JScrollPane(cont), BorderLayout.CENTER);
        
        JPanel options = new JPanel();
        
        options.setLayout(new BoxLayout(options, BoxLayout.X_AXIS));
        options.add(Box.createHorizontalGlue());
        options.add(new JButton("Update"));
        options.add(new JButton("Install plugins..."));
        
        getContentPane().add(options, BorderLayout.SOUTH);
    }
    
    private void addEntries(JPanel cont)
    {
        for (PluginDescriptor pd :
        manager.getRegistry().getPluginDescriptors())
        {
            PluginEntry e = new PluginEntry(this, pd);
            cont.add(e);
        }
    }

    public boolean isSelected(PluginEntry e)
    {
        return selectedEntry == e;
    }
    
    public PluginEntry getSelectedEntry()
    {
        return selectedEntry;
    }
    
    public void setSelectedEntry(PluginEntry e)
    {
        if (this.selectedEntry != e)
        {
            if (this.selectedEntry != null)
                this.selectedEntry.setSelected(false);
            this.selectedEntry = e;
            if (e!=null)
                e.setSelected(true);
        }
    }
    
    public PluginManager getPluginManager()
    {
        return manager;
    }
    
    private static class PluginEntry extends JPanel
    {
        
        static Border DefaultBorder = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
        
        private JPFPluginDialog ownerDialog;
        private JPanel paneInfo;
        private JPanel paneActions;
        
        private JButton btnDetails;
        private JButton btnActivation;
        private JButton btnInstallation;
        private PluginDescriptor descriptor;
        
        public PluginEntry(JPFPluginDialog owner, PluginDescriptor pd)
        {
            setBorder(DefaultBorder);
            setAlignmentX(0);
            setOpaque(true);
            setBackground(Color.WHITE);
            this.descriptor = pd;
            this.ownerDialog = owner;

            paneInfo = new JPanel();
            paneInfo.setLayout(new BorderLayout());
            paneInfo.setOpaque(false);
            paneInfo.setAlignmentX(0);
            
            Image icon = iconFor(descriptor);
            if (icon != null)
                paneInfo.add(new JLabel(new ImageIcon(icon)), BorderLayout.WEST);
            
            paneInfo.add(labelFor(descriptor), BorderLayout.CENTER);
            paneActions = new JPanel();
            paneActions.setOpaque(false);
            paneActions.setAlignmentX(0);

            btnDetails = new JButton("Details");
            btnActivation = new JButton("Deaktivate");
            btnInstallation = new JButton("Uninstall");
            
            paneActions.add(btnDetails);
            paneActions.add(btnActivation);
            paneActions.add(btnInstallation);
            
            setLayout(new BorderLayout());
            add(paneInfo, BorderLayout.CENTER);
            
            PluginEntryListener.install(this);
        }
        
        private Image iconFor(PluginDescriptor d)
        {
            PluginAttribute att = d.getAttribute("icon");
            if (att == null) return null;
            String attIcon = att.getValue();
            
            ClassLoader loader =
            getOwner()
            .getPluginManager()
            .getPluginClassLoader(d);
            
            URL url = loader.getResource(attIcon);
            if (url == null)
                return null;
            
            try
            {
                return ImageIO.read(url);
            }
            catch (IOException e)
            {
                return null;
            }
        }
        
        private JLabel labelFor(PluginDescriptor d)
        {
            String text = "<html><body><b>"+d.getId()+"</b>"
            +" "+d.getVersion()
            + "</body></html>";
            return new JLabel(text);
        }

        public JPFPluginDialog getOwner()
        {
            return ownerDialog;
        }

        public void selectEntry()
        {
            getOwner().setSelectedEntry(this);
        }
        
        public boolean isSelected()
        {
            return getOwner().isSelected(this);
        }
        
        public void setSelected(boolean selected)
        {
            if (selected)
                configureSelected();
            else
                configureDeselected();
        }

        private void configureDeselected()
        {
            setBackground(Color.WHITE);
            remove(paneActions);
            invalidate();
            revalidate();
            repaint();
        }

        private void configureSelected()
        {
            if (isAncestorOf(paneActions))
                return;
            
            setBackground(Color.LIGHT_GRAY);
            
            add(paneActions, BorderLayout.SOUTH);
            invalidate();
            revalidate();
            repaint();
            scrollRectToVisible(getBounds());
        }
        
    }
    
    private static class PluginEntryListener
      extends MouseAdapter implements MouseListener, ActionListener
   {
        
        private static PluginEntryListener instance
            = new PluginEntryListener();

        public static PluginEntryListener getInstance()
        {
            return instance;
        }
        
        public static void install(PluginEntry e)
        {
            e.addMouseListener(getInstance());
        }
        
        public void mouseClicked(MouseEvent e)
        {
            Component c = e.getComponent();
            
            if (c instanceof PluginEntry)
            {
                ((PluginEntry) c).selectEntry();
            }
            
        }

        public void actionPerformed(ActionEvent e)
        {
            // TODO Auto-generated method stub
            
        }
        
   }
    
}
