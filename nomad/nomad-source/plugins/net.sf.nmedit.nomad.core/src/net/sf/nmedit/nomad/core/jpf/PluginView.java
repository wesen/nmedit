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

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.misc.FocusStroke;
import net.sf.nmedit.nomad.core.swing.LinkLabel;
import net.sf.nmedit.nomad.core.swing.document.Document;
import net.sf.nmedit.nomad.core.swing.document.DocumentManager;

import org.java.plugin.PluginManager;
import org.java.plugin.registry.Library;
import org.java.plugin.registry.PluginAttribute;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginRegistry;
import org.java.plugin.registry.Version;

public class PluginView implements Document
{
    
    private PluginManager pluginManager;
    private JComponent rootContainer;
    private JPanel view;

    public PluginView()
    {
        pluginManager = PluginManager.lookup(this);
        view = new JPanel();
        view.setBackground(Color.WHITE);
        view.setForeground(Color.BLACK);
        JScrollPane scroller = new JScrollPane(view);
        rootContainer = scroller;
        scroller.getVerticalScrollBar().setUnitIncrement(20);
        scroller.getHorizontalScrollBar().setUnitIncrement(10);
        
        initView();
    }

    private void initView()
    {
        PluginRegistry registry = pluginManager.getRegistry();
        List<PluginDescriptor> descriptorList = new ArrayList<PluginDescriptor>(registry.getPluginDescriptors());
        
        Collections.sort(descriptorList, new PluginOrder());
        
        LabelLinkHandler linkHandler = new LabelLinkHandler(view);
        view.setLayout(new BoxLayout(view, BoxLayout.Y_AXIS));
        
        PluginPaneEventHandler ppeh = new PluginPaneEventHandler();
        
        for (PluginDescriptor descriptor: descriptorList)
        {
            JComponent c = createPluginPane(descriptor, linkHandler);
            
            Border panBorder = BorderFactory.createCompoundBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createRaisedBevelBorder(),
                    BorderFactory.createEmptyBorder(1,1,1,1)), 
                    FocusStroke.getFocusStrokeBorder(c, Color.BLACK)
             );
            c.setBorder(panBorder);
            c.setFocusable(true);
            ppeh.install(c);
            view.add(c);
        }
    }
    
    private JComponent createPluginPane(PluginDescriptor descriptor, LabelLinkHandler linkHandler)
    {
        JPanel p = new JPanel();

        PropertyPaneBuilder titlePane = new PropertyPaneBuilder(LinkLabel.styleLabel(true, false, false, descriptor.getId()));

        String description = getAttributeValue(descriptor, "description");
        if (description != null )
        {
            titlePane.add(description);
        }
        else
            titlePane.addBox();

        Version version = descriptor.getVersion();
        if (version != null)
            titlePane.add("version", version.toString());
        
        String licenseLoc = getAttributeValue(descriptor, "license-location");
        String license = getAttributeValue(descriptor, "license");
        
        if (licenseLoc == null)
        {
            if (license != null)
                titlePane.add("license", license);
        }
        else
        {
            URL licenseURL = getResource(descriptor, licenseLoc);
            if (licenseURL == null)
                titlePane.add("license", license);
            else
            {
                String title = license != null ? license : "view";
                LinkLabel link = new LinkLabel(title);
                titlePane.add("license", link);
    
                makeLicenseLink(descriptor.getId(), link, licenseURL);
            }
        }

        String vendor = getAttributeValue(descriptor, "vendor");
        if (vendor != null )
        {
            titlePane.add("vendor", vendor);
        }

        String pluginVendor = getAttributeValue(descriptor, "plugin-vendor");
        if (pluginVendor != null )
        {
            titlePane.add("plugin-vendor", pluginVendor);
        }
        
        p.setBackground(Color.WHITE);
        p.putClientProperty(PluginDescriptor.class, descriptor);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        p.add(titlePane.getContainer());
        
        ImageIcon icon = getIconFor(descriptor);
        
        if (icon != null)
        {
            JLabel iconLabel = new JLabel(icon);
            iconLabel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
            p.add(iconLabel);
        }
        
        PropertyPaneBuilder depending = new PropertyPaneBuilder("used by:");
        
        Collection<PluginDescriptor> list = descriptor.getRegistry().getDependingPlugins(descriptor);
        
        if (list.isEmpty())
        {
            depending.add("-");
        }
        else
        {
            for (PluginDescriptor user: list)
            {
                LinkLabel link = depending.addLink(user.getId());
                link.putClientProperty(PluginDescriptor.class, user);
                linkHandler.install(link);
            }
        }
        
        p.add(depending.getContainer());
        
        PropertyPaneBuilder libraries = new PropertyPaneBuilder("libraries:");
        
        Collection<Library> libs = descriptor.getLibraries();
        
        if (libs.isEmpty())
        {
            libraries.add("-");
        }
        else
        {
            for (Library l: libs)
            {
                String path = l.getPath();
                if (path.toLowerCase().endsWith(".jar"))
                {
                    int sep = path.lastIndexOf(File.separatorChar);
                    if (sep>=0)
                        path = path.substring(sep+1);
                        
                    libraries.add(path);
                }
            }
        }

        p.add(libraries.getContainer());
        return p;
    }
    
    private void makeLicenseLink(String pluginTitle, LinkLabel link, URL licenseURL)
    {
        link.addActionListener(new LicenseDocument(pluginTitle, licenseURL));
    }
    
    private static class LicenseDocument implements Document, ActionListener
    {

        private URL licenseURL;
        private String pluginTitle;
        private JComponent lazyComponent = null;

        public LicenseDocument(String pluginTitle, URL licenseURL)
        {
            this.pluginTitle = pluginTitle;
            this.licenseURL = licenseURL;
        }

        public JComponent getComponent()
        {
            if (lazyComponent == null)
                lazyComponent = createComponent();
            return lazyComponent;
        }

        private JComponent createComponent()
        {
            JEditorPane editor;
            try
            {
                editor = new JEditorPane(licenseURL);
            }
            catch (IOException e)
            {
                editor = new JEditorPane();
                editor.setText("Resource not found: "+licenseURL);
            }
            editor.setEditable(false);
            return new JScrollPane(editor);
        }

        public Icon getIcon()
        {
            return null;
        }

        public String getTitle()
        {
            return "License "+pluginTitle;
        }

        public String getTitleExtended()
        {
        	return getTitle();
        }

        public void actionPerformed(ActionEvent e)
        {
            DocumentManager docs = Nomad.sharedInstance().getDocumentManager();
            if (docs.contains(this))
            {
                docs.setSelection(this);
            }
            else if (docs.add(this))
            {
                docs.setSelection(this);
            }
        }

        public void dispose()
        {
            lazyComponent = null;
        }

        public <T> T getFeature(Class<T> featureClass)
        {
            // TODO Auto-generated method stub
            return null;
        }
        
        public File getFile()
        {
            return null;
        }

        public Object getProperty(String name)
        {
            // TODO Auto-generated method stub
            return null;
        }

		public boolean isModified() {
			// TODO Auto-generated method stub
			return false;
		}
        
    }

    private String getAttributeValue(PluginDescriptor d, String attribute)
    {
        PluginAttribute att = d.getAttribute(attribute);
        if (att == null) return null;
        String val = att.getValue().trim();
        if (val.length()==0) return null;
        return val;
    }
    
    private URL getResource(PluginDescriptor d, String resource)
    {
        if (resource == null)
            return null;
        
        File path = (new File(d.getLocation().getPath())).getParentFile();
        try
        {
            return (new File(path, resource)).toURI().toURL();
        }
        catch (MalformedURLException e)
        {
        }
        return null;
    }
    
    private ImageIcon getIconFor(PluginDescriptor d)
    {
        URL url = getResource(d, getAttributeValue(d, "icon"));
        return (url == null) ? null : new ImageIcon(url);
    }

    private static class PluginPaneEventHandler extends MouseAdapter
        implements MouseListener, FocusListener
    {

        public void install(JComponent c)
        {
            c.addMouseListener(this);
            c.addFocusListener(this);
        }

        public void mousePressed(MouseEvent e)
        {
            e.getComponent().requestFocus();
        }

        public void focusGained(FocusEvent e)
        {
            Component c = e.getComponent();
            if (c instanceof JComponent)
                ((JComponent) c).scrollRectToVisible(new Rectangle(0,0,c.getWidth(), c.getHeight()));
            c.setBackground(Color.LIGHT_GRAY);
        }

        public void focusLost(FocusEvent e)
        {
            e.getComponent().setBackground(Color.WHITE);
        }
        
    }
    
    private static class PluginOrder implements Comparator<PluginDescriptor>
    {

        public int compare(PluginDescriptor o1, PluginDescriptor o2)
        {
            String id1 = o1.getId();
            String id2 = o2.getId();
            return id1.compareTo(id2);
        }
        
    }
    
    private static class PropertyPaneBuilder
    {
        private JPanel pane;
        private JLabel titleLabel;
        private Box lastBox = null;
        
        public PropertyPaneBuilder(String title)
        {
            pane = new JPanel();
            pane.setOpaque(false);
            pane.setBorder(null);
            pane.setAlignmentX(JComponent.LEFT_ALIGNMENT);
            pane.setLayout(new GridLayout(0,2));

            pane.add(titleLabel = new JLabel(title));
        }
        
        public void addBox()
        {
            pane.add(Box.createHorizontalBox());
        }

        public JLabel getTitleLabel()
        {
            return titleLabel;
        }
        
        public JComponent getContainer()
        {
            return pane;
        }
        
        public <T extends JComponent> T add(T c)
        {
            pane.add(c);
            pane.add(lastBox = Box.createHorizontalBox());
            return c;
        }

        public JLabel add(String label)
        {
            return add(new JLabel(label));
        }

        public void add(String title, String label)
        {
            add(title, new JLabel(label));
        }

        public <T extends Component> T add(String title, T c)
        {
            if (lastBox != null)
            {
                pane.remove(lastBox);
                lastBox = null;
            }

            pane.add(new JLabel(title));
            pane.add(c);
            return c;
        }

        public LinkLabel addLink(String label)
        {
            return add(new LinkLabel(label));
        }
        
    }
    
    private static class LabelLinkHandler implements ActionListener
    {

        private JComponent root;

        public LabelLinkHandler(JComponent root)
        {
            this.root = root;
        }

        public void install(LinkLabel link)
        {
            link.addActionListener(this);
        }

        public void actionPerformed(ActionEvent e)
        {
            Component c = (Component) e.getSource();
            if (c == null || !(c instanceof JComponent)) return;
            Object descriptor = ((JComponent) c).getClientProperty(PluginDescriptor.class);
            if (descriptor == null || !(descriptor instanceof PluginDescriptor)) return;
            
            for (Component c2: root.getComponents())
            {
                if ((c2 instanceof JComponent) &&
                 ((JComponent)c2).getClientProperty(PluginDescriptor.class) == descriptor)
                {
                    c2.requestFocus();
                    break;
                }
            }
        }
        
    }

    public JComponent getComponent()
    {
        return rootContainer;
    }

    public Icon getIcon()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getTitle()
    {
        return "Plugins";
    }

    public String getTitleExtended()
    {
        return getTitle();
    }

    public void dispose()
    {
        // no op
    }

    public <T> T getFeature(Class<T> featureClass)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public File getFile()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getProperty(String name)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    public boolean isModified() {
    	return false;
    }
    
}
