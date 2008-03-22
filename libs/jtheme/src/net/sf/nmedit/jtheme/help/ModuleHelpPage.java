/* Copyright (C) 2008 Christian Schneider
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
package net.sf.nmedit.jtheme.help;

import java.awt.BorderLayout;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.StyleSheet;
import javax.swing.text.html.parser.ParserDelegator;

import net.sf.nmedit.jpatch.PConnectorDescriptor;
import net.sf.nmedit.jpatch.PDescriptor;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PParameterDescriptor;
import net.sf.nmedit.jpatch.PRoles;
import net.sf.nmedit.jtheme.JTContext;

public class ModuleHelpPage extends JPanel
{
    
    private PModuleDescriptor dmodule;
    private JTContext context;
    private String title;
    private JEditorPane textArea;
    private HTMLDocument doc;
    private URL moduleImageURL;
    
    public ModuleHelpPage(JTContext context)
    {
        this(context, null, null);
    }
    
    public ModuleHelpPage(JTContext context, PModuleDescriptor dmodule, URL moduleImageURL)
    {
        createComponents();
        this.context = context;
        this.moduleImageURL = moduleImageURL;
        setModule(dmodule);
    }

    private void createComponents()
    {
        setLayout(new BorderLayout());
        textArea = new JEditorPane();
        textArea.setEditable(false);
        textArea.setContentType("text/html"); 
        add(new JScrollPane(textArea), BorderLayout.CENTER);
        
        StyleSheet ss = new StyleSheet();
        ss.addRule("h1 { font-size:24pt; font-weight: bold; margin-top:6pt; margin-bottom:4pt; }" +
        		   "h2 { font-size:20pt; font-weight: bold; margin-top:6pt; margin-bottom:4pt; }" +
                   "h3 { font-size:16pt; font-weight: bold; color:blue; }" +
                   "strong {font-weight: bold; }" +
                   ".indent { margin-left:12pt; }" +
                   "ul {margin-left: 16pt; }" +
                   ".module_image { margin-top:12pt;margin-bottom:12pt; }"+
                   "body{padding:12pt;}");
        
        doc = new HTMLDocument(ss);
        
        ParserDelegator pd = new ParserDelegator();
        
        doc.setParser(pd);
        
        
        textArea.setDocument(doc);
    }

    private void setModule(PModuleDescriptor dmodule)
    {
        if (this.dmodule != dmodule)
        {
            this.dmodule = dmodule;
            if (dmodule == null)
                clearHelp();
            else
                generateHelp();
        }
    }

    protected void clearHelp()
    {
        try
        {
            doc.setInnerHTML(doc.getDefaultRootElement(), "");
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected String roles(PRoles roles)
    {
        if (roles == null || roles.size()==0) return "";

        String html="<div><strong>roles:</strong>";
        for (String s: roles)
        {
            html+=" "+s;
        }
        html+="</div>";
        return html;
    }
    
    protected String attributes(PDescriptor d)
    {
        String html= "";
        Iterator<String> iter = d.attributeKeys();
        if (iter.hasNext())
        {
            List<String> list = new ArrayList<String>();
            while(iter.hasNext())
                list.add(iter.next());
            Collections.sort(list);

            html+="<h4>Attributes</h4>";
            html+="<ul>";
            for (String key: list)
            {
                Object value = d.getAttribute(key);
                
                String stringValue;
                if (value != null && value instanceof CharSequence)
                    stringValue = "\""+value+"\"";
                else
                    stringValue = String.valueOf(value);
                html+="<li><strong>"+key+":</strong> "+stringValue+"</li>";
            }
            html+="</ul>";
        }
        return html;
    }
    
    protected void generateHelp()
    {
        title = dmodule.getStringAttribute("fullname");
        if (title == null) title = dmodule.getName();

        String html = "";
        html+="<body>";
        html+="<div>";
        
        URL url = dmodule.getModules().getImageURL(dmodule.get32x32IconSource());
        if (url != null)
            html+="<div><img src="+url+" /></div>";

        String htmlDoc = dmodule.getModules().getDocumentationFor(dmodule);

        if (moduleImageURL != null)
        {
            html+="<div class=\"module_image\"><img src=\""+moduleImageURL+"\" /></div>";
        }
        
        html+=htmlDoc;
        
        html+="<h1>Generated Documentation</h1>";

        html+="<h2>Module</h2>";
        html+=roles(dmodule.getRoles());
        html+=attributes(dmodule);
        if (dmodule.getConnectorDescriptorCount()>0)
        {
            html+="<h2>Connectors</h2>";
            for (int i=0;i<dmodule.getConnectorDescriptorCount();i++)
            {
                PConnectorDescriptor con = dmodule.getConnectorDescriptor(i);
                html+="<h3>"+con.getName()+"</h3>";
                html+=roles(con.getRoles());
                html+=attributes(con);
            }
        }
        
        

        if (dmodule.getParameterDescriptorCount()>0)
        {
            Set<PParameterDescriptor> extended = new HashSet<PParameterDescriptor>();
            html+="<h2>Parameters</h2>";
            for (int i=0;i<dmodule.getParameterDescriptorCount();i++)
            {
                PParameterDescriptor par = dmodule.getParameterDescriptor(i);
                if (par.getExtensionDescriptor()!=null)
                    extended.add(par.getExtensionDescriptor());
            }

            for (int i=0;i<dmodule.getParameterDescriptorCount();i++)
            {
                PParameterDescriptor par = dmodule.getParameterDescriptor(i);
                if (extended.contains(par))
                    continue;
                PParameterDescriptor ext = par.getExtensionDescriptor();
                
                html+="<h3>"+par.getName()+"</h3>";
                html+="<div class=\"indent\">";
                

                html+="<div><strong>range:</strong> "+par.getDisplayValue(par.getMinValue())+" <strong>to</strong> "+par.getDisplayValue(par.getMaxValue())+"</div>";
                html+="<div><strong>default:</strong> "+par.getDisplayValue(par.getDefaultValue())+"</div>";
                html+=roles(par.getRoles());
                html+=attributes(par);
                
                if (ext != null)
                    html+="<div><strong>ext:</strong> "+ext.getName()+"</div>";
                
                html+="</div>";
            }
        }
        

        html+="</div>";
        html+="</body>";
        
        try
        {
            
            doc.setInnerHTML(doc.getDefaultRootElement(), html);
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Icon getIcon()
    {
        if  (dmodule != null)
        {
            Image img = dmodule.getModules().getImage(dmodule.get16x16IconSource());
            if (img != null)
                return new ImageIcon(img);
        } 
        return null;
    }
    
    public String getTitle()
    {
        return title;
    }
    
}
