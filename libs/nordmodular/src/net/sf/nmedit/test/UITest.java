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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import javax.swing.undo.UndoManager;

import org.xml.sax.InputSource;

import net.sf.nmedit.jpatch.PConnectorDescriptor;
import net.sf.nmedit.jpatch.PDescriptor;
import net.sf.nmedit.jpatch.PLightDescriptor;
import net.sf.nmedit.jpatch.PModuleDescriptor;
import net.sf.nmedit.jpatch.PParameterDescriptor;
import net.sf.nmedit.jpatch.clavia.nordmodular.NM1ModuleDescriptions;
import net.sf.nmedit.jpatch.clavia.nordmodular.NMPatch;
//import net.sf.nmedit.jpatch.history.History;
import net.sf.nmedit.jpatch.transform.PTTransformationsBuillder;
import net.sf.nmedit.jpatch.transform.PTTransformations;
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
    	files.add(new File("/Users/distrinet/Documents/patch/test.pch"));
        updateUI();
    }
    
    private static class UITestAction extends AbstractAction
    {
        /**
         * 
         */
        private static final long serialVersionUID = 5875924171704911680L;
        public static final String FileOpen = "Open";
        public static final String FileClose= "Close";
        public static final String UIUpdate = "Update UI";
        public static final String Exit = "Exit";
        public static final String FileUndo = "Undo";
        public static final String FileRedo = "Redo";
        
        public static final String MD_SERIALIZATION = "MD Serialization";
        
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
            
            else if (e.getActionCommand() == MD_SERIALIZATION)
            {
                UITest.MD_SerializationTest();
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
        JMenu test = new JMenu("Test");
        test.add(new UITestAction(UITestAction.MD_SERIALIZATION));
        mnbar.add(test);
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
    
    public static void MD_SerializationTest()
    {
        System.out.println("Serialization Test...");
        try
        {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();   
            ObjectOutputStream out = new ObjectOutputStream(bout);

            System.out.println("writing...");
            modules.writeCache(out);
            out.flush();
            out.close();
            System.out.println("ok");
            
            byte[] data = bout.toByteArray();

            System.out.println("reading");
            NM1ModuleDescriptions copy = new NM1ModuleDescriptions(modules.getModuleDescriptionsClassLoader());
            copy.readCache(new ObjectInputStream(new ByteArrayInputStream(data)));
            System.out.println("ok");
            
            compare(modules, copy);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private static void compare(NM1ModuleDescriptions m1, NM1ModuleDescriptions m2)
    {
        check_true(m1.getDefinedSignals()!= null && m2.getDefinedSignals()!=null, "defined signals", m1, m2);
        check_true(m1.getModuleCount() == m2.getModuleCount(), "module count", m1, m2);
        
        for (PModuleDescriptor ma: m1)
        {
            PModuleDescriptor mb = m2.getModuleById(ma.getComponentId());
            check_true(mb != null, "module[component-id="+ma.getComponentId()+"]", ma, mb);
            compare(ma, mb);
        }   
    }

    private static void compare(PModuleDescriptor ma, PModuleDescriptor mb)
    {
        compareDescriptor(ma, mb);

        check_true(ma.getParameterDescriptorCount()==mb.getParameterDescriptorCount(), "parameter count", ma, mb);
        check_true(ma.getConnectorDescriptorCount()==mb.getConnectorDescriptorCount(), "connector count", ma, mb);
        check_true(ma.getLightDescriptorCount()==mb.getLightDescriptorCount(), "light count", ma, mb);

        for (int i=ma.getParameterDescriptorCount()-1;i>=0;i--)
        {
            PParameterDescriptor pa = ma.getParameterDescriptor(i);
            PParameterDescriptor pb = mb.getParameterDescriptor(i);
            compare(pa, pb);
        }
        for (int i=ma.getConnectorDescriptorCount()-1;i>=0;i--)
        {
            PConnectorDescriptor pa = ma.getConnectorDescriptor(i);
            PConnectorDescriptor pb = mb.getConnectorDescriptor(i);
            compare(pa, pb);
        }
        for (int i=ma.getLightDescriptorCount()-1;i>=0;i--)
        {
            PLightDescriptor pa = ma.getLightDescriptor(i);
            PLightDescriptor pb = mb.getLightDescriptor(i);
            compare(pa, pb);
        }
    }

    private static void compare(PLightDescriptor pa, PLightDescriptor pb)
    {
        compareDescriptor(pa, pb);
    }

    private static void compare(PConnectorDescriptor pa, PConnectorDescriptor pb)
    {
        compareDescriptor(pa, pb);
    }

    private static void compare(PParameterDescriptor pa, PParameterDescriptor pb)
    {
        compareDescriptor(pa, pb);
    }

    private static void compareDescriptor(PDescriptor pa, PDescriptor pb)
    {
        check_true(pa.getComponentId().equals(pb.getComponentId()), "component-id", pa, pb);
        check_true(pa.getDescriptorIndex()==pb.getDescriptorIndex(), "descriptor index", pa, pb);
        
        if (!(pa instanceof PModuleDescriptor))
        {
            check_true(pa.getParentDescriptor()!=null, "no parent", pa);
            check_true(pb.getParentDescriptor()!=null, "no parent", pb);
        }
        
    }

    private static void check_true(boolean t, String msg, Object ... src)
    {
        if (!t) 
        {
            
            StringBuilder sb = new StringBuilder();
            sb.append(msg);
            sb.append("; source: ");
            int cnt = 0;
            for (Object o: src)
            {
                sb.append(o);
                if (++cnt<src.length)
                    sb.append(", ");
            }
            throw new RuntimeException(sb.toString());
        }
    }

    static boolean fileOpenNoList(File file)
    {
        InputStream in = null;
        NMPatch patch = null;
        
        try
        {
            in = new BufferedInputStream(new FileInputStream(file));
            patch = NmUtils.parsePatch(modules, in);
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
            patchui = new JTNMPatch(uicontext, patch);
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
        UndoManager history = patch.getUndoManager();
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
        
        PTTransformations t = PTTransformationsBuillder.build(is, modules);
       
        modules.setTransformations(t);
        
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
            modules = NM1ModuleDescriptions.parse(RelativeClassLoader.fromPath(UITest.class.getClassLoader(), moduleURL), in);
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
    }

    static void createUIContext() 
    {
        String classicThemeXml = "/classic-theme/classic-theme.xml";
        URL classicThemeURL = UITest.class.getResource(classicThemeXml);
        ClassLoader relLoader;
        
        try
        {
        relLoader = RelativeClassLoader.fromPath(UITest.class.getClassLoader(), classicThemeURL);
        }
        catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }

        uicontext = new JTNM1Context(null);
        DefaultStorageContext dsc = new NMStorageContext(uicontext, relLoader);
        uicontext.setStorageContext(dsc);
        
        InputStream in = null;
        try
        {
            in = new BufferedInputStream(classicThemeURL.openStream());
            dsc.parseStore(new InputSource(in), relLoader);
            
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
