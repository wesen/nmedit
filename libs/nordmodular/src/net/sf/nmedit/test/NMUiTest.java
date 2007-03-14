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
package net.sf.nmedit.test;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.filechooser.FileFilter;

import net.sf.nmedit.jpatch.Module;
import net.sf.nmedit.jpatch.ModuleDescriptor;
import net.sf.nmedit.jpatch.clavia.nordmodular.NM1ModuleDescriptions;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMModule;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jpatch.spec.DefaultModuleDescriptor;
import net.sf.nmedit.jsynth.clavia.nordmodular.utils.NmUtils;
import net.sf.nmedit.jtheme.JTException;
import net.sf.nmedit.jtheme.clavia.nordmodular.JTNM1Context;
import net.sf.nmedit.jtheme.clavia.nordmodular.JTNMPatch;
import net.sf.nmedit.jtheme.component.JTModule;
import net.sf.nmedit.jtheme.component.JTModuleContainer;
import net.sf.nmedit.jtheme.store.DefaultStorageContext;
import net.sf.nmedit.jtheme.store.ModuleStore;
import net.sf.nmedit.jtheme.util.RelativeClassLoader;

import org.xml.sax.InputSource;

public class NMUiTest implements ActionListener
{

    public static void main(String[] args) throws Exception
    {
        (new NMUiTest()).test();
    }
    
    private NM1ModuleDescriptions descriptions;
    DefaultStorageContext context;
    
    private void test() throws Exception
    {
        loadData();
        buildFrame();

        System.out.println(new URI("nomad://synth/clavia/nm1/MyNordA/slot/0"));
        System.out.println(new URI("nomad://synth/clavia/micro1/MyMicroA/slot/0"));
    }
    
    JComboBox combo;
    JFrame fr;
  JTNM1Context uicontext = new JTNM1Context();
  
  JTModuleContainer mcontainer;
    
    private void buildFrame() throws JTException
    {
        fr = new JFrame("nm1 MODULES");
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fr.getContentPane().setLayout(new BorderLayout());
        fr.getContentPane().add(combo=createComboBox(), BorderLayout.NORTH);
        
        fr.setBounds(10, 10, 400, 400);
        
        fr.setJMenuBar(createMenuBar());

        mcontainer = uicontext.createModuleContainer();
        mcontainer.getCableManager().setVisibleRegion(0, 0, 1000,1000);
        fr.getContentPane().add(mcontainer, BorderLayout.CENTER);
        
        combo.addActionListener(this);

        fr.setVisible(true);
    }

    private JMenuBar createMenuBar()
    {
        JMenuBar bar = new JMenuBar();
        bar
        .add(new JMenu("File"))
        .add("Open")
        .addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){
            openPatch();
        }});
        return bar;
    }
    
    void openPatch()
    {
        JFileChooser chooser = new JFileChooser(new File("/home/christian/Programme/nomad/data/patch"));
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileFilter(new FileFilter(){

            @Override
            public boolean accept(File f)
            {
                return f.isDirectory() || f.getName().endsWith("pch");
            }

            @Override
            public String getDescription()
            {
                return "Nord Modular patch 3.0";
            }
            
        });
        
        chooser.showOpenDialog(fr);
        
        File file = chooser.getSelectedFile();
        if (file != null)
            open(file);
    }

    private void open(File file)
    {
        try
        {
        tryopen(file);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    private void tryopen(File file) throws Exception
    {
        InputStream in = new BufferedInputStream(new FileInputStream(file));
        NMPatch patch = NmUtils.parsePatch(descriptions, in);
        
        JTNMPatch jtpatch = new JTNMPatch(context, uicontext, (NMPatch) patch);
        
        JFrame frpatch = new JFrame(file.getName());
        frpatch.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frpatch.setBounds(20, 20, 700, 600);
        frpatch.getContentPane().setLayout(new BorderLayout());
        frpatch.getContentPane().add(jtpatch, BorderLayout.CENTER);
        frpatch.setVisible(true);
    }

    private JComboBox createComboBox()
    {
        List<ModuleDescriptor> moduleList = new ArrayList<ModuleDescriptor>(110);
        
        for (ModuleDescriptor md : descriptions)
        {
            moduleList.add(md);
        }
        
        JComboBox cb = new JComboBox(moduleList.toArray(new ModuleDescriptor[moduleList.size()]));
        
        cb.setRenderer(new DefaultListCellRenderer(){

            public Component getListCellRendererComponent(
                JList list,
            Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus)
            {
                String moduleName = ((ModuleDescriptor)value).getComponentName(); 
                
                return
                super.getListCellRendererComponent(list, moduleName, index, isSelected, cellHasFocus);
            }
            
        });
        
        return cb;
    }
    
    private String uri2base(URI uri)
    {
        String base = uri.getPath();
        return base.substring(0, base.lastIndexOf('/'));
    }
    
    private String base(URL url)
    {
        try
        {
            return uri2base(url.toURI());
        }
        catch (URISyntaxException e)
        {
            return null;
        }
    }
    
    private void loadData() throws Exception
    {
        String themePath = "/classic-theme/";
        String themeFile = "classic-theme.xml";
        
        URL themeURL = getClass().getResource(themePath+themeFile);
        
        String base = base(themeURL);

        ClassLoader classLoader = new RelativeClassLoader(base, getClass().getClassLoader());
        InputStream is = classLoader.getResourceAsStream(themeFile);
        
        if (is == null)
            throw new NullPointerException("stream is null");
        

        String descriptionsFile = "/module-descriptions/modules.xml"; 
        URL descURL = getClass().getResource(descriptionsFile);
        
        
        InputStream in = new BufferedInputStream(getClass().getResourceAsStream(descriptionsFile));
        try
        {
        descriptions = NM1ModuleDescriptions.parse(in);
        }
        finally
        {
            in.close();
        }
        

        context = new DefaultStorageContext(classLoader);
        
        InputSource source = new InputSource(is);
        
        context.parseStore(source);
        
    }

    public void actionPerformed(ActionEvent e)
    {
        selectModule((ModuleDescriptor)combo.getSelectedItem());
    }

    private ModuleDescriptor current;
    private JTModule currentComp;
    
    private void selectModule(ModuleDescriptor descriptor)
    {
        if (current == descriptor) return;
        
        if (currentComp != null)
            mcontainer.remove(currentComp);
        currentComp = null;

        ModuleStore ms = context.getModuleStoreById(""+descriptor.getIndex());
        
        Module module = new NMModule((DefaultModuleDescriptor) descriptor);
        
        
        
        try
        {
            currentComp = ms.createModule(uicontext, module);
        }
        catch (JTException e)
        {
            e.printStackTrace();
            return;
        }

        mcontainer.add(currentComp);
        
        mcontainer.getCableManager().clear();
        mcontainer.repaint();
        fr.repaint();
    }
    
}

