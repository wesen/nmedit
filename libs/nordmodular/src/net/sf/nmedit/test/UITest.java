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
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileFilter;

import org.xml.sax.InputSource;

import net.sf.nmedit.jpatch.clavia.nordmodular.NM1ModuleDescriptions;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
import net.sf.nmedit.jpatch.history.History;
import net.sf.nmedit.jpatch.transformation.Transformations;
import net.sf.nmedit.jpatch.transformation.impl.TransformationsBuilder;
import net.sf.nmedit.jsynth.clavia.nordmodular.utils.NmUtils;
import net.sf.nmedit.jtheme.clavia.nordmodular.JTNM1Context;
import net.sf.nmedit.jtheme.clavia.nordmodular.JTNMPatch;
import net.sf.nmedit.jtheme.clavia.nordmodular.NMStorageContext;
import net.sf.nmedit.jtheme.store.DefaultStorageContext;
import net.sf.nmedit.jtheme.util.RelativeClassLoader;

/**
 * Note: add the nordmodular/data/ folder to the classpath.
 */
public class UITest
{
    
    static JFrame frame;
    static JTabbedPane tabbedPane;
    static List<File> files = new ArrayList<File>();
    static NM1ModuleDescriptions modules;
    static JTNM1Context uicontext;

    public static void main(String[] args)
    {   
        updateUI();
    }
    
    private static class UITestAction extends AbstractAction
    {
        public static final String FileOpen = "Open";
        public static final String FileClose= "Close";
        public static final String UIUpdate = "Update UI";
        public static final String Exit = "Exit";
        public static final String FileUndo = "Undo";
        public static final String FileRedo = "Redo";
        
        public UITestAction(String command)
        {
            putValue(ACTION_COMMAND_KEY, command);
            putValue(NAME, command);
        }
        
        public void actionPerformed(ActionEvent e)
        {
            if (e.getActionCommand() == FileOpen)
                UITest.fileOpen();
            else if (e.getActionCommand() == FileClose)
                UITest.fileClose();
            else if (e.getActionCommand() == UIUpdate)
                UITest.updateUI();
            else if (e.getActionCommand() == FileUndo)
                UITest.fileUndo(true);
            else if (e.getActionCommand() == FileRedo)
                UITest.fileUndo(false);
            else if (e.getActionCommand() == Exit)
            {
                if (JOptionPane.showConfirmDialog(frame, "Do you want to exit ?", "Exit?", 
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION)
                {
                    System.exit(0);
                }
            }
        }
    }
    
    static void createFrame()
    {
        Rectangle frameBounds;
        if (frame != null)
        {
            frameBounds = frame.getBounds();
            frame.dispose();
            frame = null;
        }
        else
        {
            Dimension frameSize = new Dimension(700, 600);
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension wind = frameSize;

            frameBounds = new Rectangle(new Point((screen.width-wind.width)/2, (screen.height-wind.height)/2), 
                    frameSize);
        }
        
        // frame
        frame = new JFrame(UITest.class.getName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(frameBounds);

        // menu
        JMenuBar mnbar = new JMenuBar();
        JMenu mnfile = new JMenu("File");
        mnfile.add(new UITestAction(UITestAction.FileOpen));
        mnfile.add(new UITestAction(UITestAction.FileClose));
        mnfile.addSeparator();
        mnfile.add(new UITestAction(UITestAction.UIUpdate));
        mnfile.addSeparator();
        mnfile.add(new UITestAction(UITestAction.Exit));
        mnbar.add(mnfile);
        JMenu mnpatch = new JMenu("Patch");
        mnpatch.add(new UITestAction(UITestAction.FileUndo));
        mnpatch.add(new UITestAction(UITestAction.FileRedo));
        mnbar.add(mnpatch);
        frame.setJMenuBar(mnbar);
        
        // content
        tabbedPane = new JTabbedPane();
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }
    
    static JFileChooser chooser;
    static void fileOpen()
    {
        if (chooser == null)
        {
            chooser = new JFileChooser();
            chooser.setFileFilter(new FileFilter()
            {
                @Override
                public boolean accept(File f) { return f.isDirectory() || (f.isFile() && f.getPath().toLowerCase().endsWith(".pch")); } 
                @Override
                public String getDescription() { return "NM1 Patch File (*.pch)"; }
            });
            
            chooser.setMultiSelectionEnabled(true);
        }
        
        if (chooser.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION)
            return;
        
        for (File file: chooser.getSelectedFiles())
        {
            fileOpen(file);
        }
    }
    
    static void fileOpen(File file)
    {
        if (files.contains(file))
            return;
        if (fileOpenNoList(file))
            files.add(file);
    }
    
    static boolean fileOpenNoList(File file)
    {
        InputStream in = null;
        NMPatch patch = null;
        
        try
        {
            in = new BufferedInputStream(new FileInputStream(file));
            patch = NmUtils.parsePatch(modules, in);
            patch.getHistory().setEnabled(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            close(in);
        }
        
        patch.setName(NmUtils.getPatchNameFromfileName(file));
        
        JTNMPatch patchui;
        try
        {
            patchui = new JTNMPatch((DefaultStorageContext)uicontext.getStorageContext(),
                    uicontext, patch);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        
        tabbedPane.addTab(patch.getName(), patchui);
        
        return true;
    }

    private static void syncOpenFiles()
    {
        for (File file: files)
            fileOpenNoList(file);
    }
    
    static void fileClose()
    {
        int index = tabbedPane.getSelectedIndex();
        if (index<0) return;
        files.remove(index);
        tabbedPane.removeTabAt(index);
    }

    public static void fileUndo(boolean undo)
    {
        Component c = tabbedPane.getSelectedComponent();
        if (c==null) return;

        JTNMPatch jtpc = (JTNMPatch) c;
        
        NMPatch patch = jtpc.getPatch();
        History history = patch.getHistory();
        if (undo) history.undo();
        else history.redo();
    }

    static void updateUI()
    {
        createFrame(); // create a new frame
        createModuleDescriptions();
        createTransformations();
        createUIContext(); // create ui context
        syncOpenFiles(); // reopen files 

        // show frame
        frame.setVisible(true);
    }

    static void createTransformations()
    {
        try
        {
            createTransformationsE();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    static void createTransformationsE() throws Exception
    {
        
        InputSource is = new InputSource(new FileInputStream("./nordmodular/data/module-descriptions/transformations.xml"));
        
        Transformations t = TransformationsBuilder.build(is, modules);
       
        modules.setTransformations(t);
        
        System.out.println("transformations: "+t);
        
    }

    private static void createModuleDescriptions()
    {
        if (modules != null) return; // create them only once

        String modulesXml = "/module-descriptions/modules.xml";
        URL moduleURL = UITest.class.getResource(modulesXml);
        if (moduleURL == null)
        {
            System.err.println("Resource not found: "+modulesXml);
            System.exit(1);
        }
        
        InputStream in = null;
        try
        {
            in = new BufferedInputStream( new FileInputStream(new File(moduleURL.toURI())) );
            modules = NM1ModuleDescriptions.parse(in);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        finally
        {
            close(in);
        }

        ClassLoader loader = UITest.class.getClassLoader();
        File f;
        try
        {
            f = (new File(moduleURL.toURI())).getParentFile();
            loader = new RelativeClassLoader(f, loader);
        }
        catch (URISyntaxException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        modules.setModuleDescriptionsClassLoader(loader);
        
    }

    static void createUIContext()
    {
        String classicThemeXml = "/classic-theme/classic-theme.xml";
        URL classicThemeURL = UITest.class.getResource(classicThemeXml);
        File classicThemeFile = null;
        try
        {
            classicThemeFile = new File(classicThemeURL.toURI());
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        RelativeClassLoader relLoader = new RelativeClassLoader(classicThemeFile.getParent());
        
        DefaultStorageContext dsc = new NMStorageContext(relLoader);
        
        InputStream in = null;
        try
        {
            in = new BufferedInputStream(new FileInputStream(classicThemeFile));
            dsc.parseStore(new InputSource(in));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        finally
        {
            close(in);
        }
        uicontext = new JTNM1Context(dsc);
    }

    // helper
    static void close(InputStream in)
    {
        if (in == null)
            return;
        
        try
        {
            in.close();
        }
        catch (IOException e)
        {
            // ignore
        }
    }

}
