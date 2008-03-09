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
package net.sf.nmedit.nomad.core.service.initService;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.swing.tree.TreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.nmedit.nomad.core.Nomad;
import net.sf.nmedit.nomad.core.jpf.TempDir;
import net.sf.nmedit.nomad.core.service.Service;
import net.sf.nmedit.nomad.core.swing.ExtensionFilter;
import net.sf.nmedit.nomad.core.swing.explorer.ExplorerTree;
import net.sf.nmedit.nomad.core.swing.explorer.FileContext;

public class ExplorerLocationMemory implements InitService
{
    
    private static final String EXPLORER_LOCATIONS = "explorer-location.properties";

    private File getTempFile()
    {
        return TempDir.forObject(this).getTempFile(EXPLORER_LOCATIONS);
    }
    
    public void init()
    {
        File file = getTempFile();
        if (file.exists() && file.isFile())
        {
            Properties p = new Properties();
            InputStream in = null;
            try
            {
                in = new BufferedInputStream(new FileInputStream(file));
                p.load(in);
            }
            catch (IOException e)
            {
                Log log = LogFactory.getLog(getClass());
                if (log.isWarnEnabled())
                {
                    log.warn("could not read property file", e);
                }
                return;
            }
            finally
            {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                    // no op
                }
            }
            createLocations(p);
        }
    }

    public void shutdown()
    {
        Enumeration<TreeNode> nodes = Nomad.sharedInstance().getExplorer().getRoot().children();
        
        List<FileContext> locations = new ArrayList<FileContext>();
    	TempDir tempDir = TempDir.generalTempDir();
    	File userPatches = tempDir.getTempFile("patches");
		String canonicalPatches = null;
        try {
        	canonicalPatches = userPatches.getCanonicalPath();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        while (nodes.hasMoreElements())
        {
            TreeNode node = nodes.nextElement();
            if (node instanceof FileContext)
            {
                FileContext fc = (FileContext) node;
                try {
					if (!fc.getFile().getCanonicalPath().equals(canonicalPatches))
						locations.add(fc);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
        
        Properties p = new Properties();
        writeProperties(p, locations);

        File file = getTempFile();
        
        BufferedOutputStream out = null;
        try
        {
            out = new BufferedOutputStream(new FileOutputStream(file));
            p.store(out, "this file is generated, do not edit it");
        }
        catch (IOException e)
        {
            // ignore
        }
        finally
        {
            try
            {
                out.flush();
                out.close();
            }
            catch (IOException e)
            {
                // ignore
            }
        }
    }

    private void writeProperties(Properties p, List<FileContext> locations)
    {
        p.put("location.count", Integer.toString(locations.size()));
        for (int i=locations.size()-1;i>=0;i--)
        {
            FileContext fc = locations.get(i);
            p.put("location."+i+".path", fc.getFile().getPath());
            
            FileFilter ff = fc.getFileFilter();
            if (ff != null && ff instanceof ExtensionFilter)
            {
                ExtensionFilter ef = (ExtensionFilter) ff;
                p.put("location."+i+".filter", ef.getExtension());
            }
        }
    }

    private void createLocations(Properties p)
    {
        ExplorerTree tree = Nomad.sharedInstance().getExplorer();
        
        String o = p.getProperty("location.count");
        if (o == null) return;
        int size;
        try
        {
            size = Integer.parseInt(o);
        }
        catch (NumberFormatException e)
        {
            return;
        }
        
        for (int i=0;i<size;i++)
        {
            String path = p.getProperty("location."+i+".path");
            
            if (path != null)
            {
                FileContext fc = new FileContext(tree, new File(path));
                

                String filter = p.getProperty("location."+i+".filter");
                if (filter != null)
                    fc.setFileFilter(new ExtensionFilter("?", filter, true));
                
                tree.addRootNode(fc);
            }
        }
        
        tree.fireRootChanged();
    }

    public Class<? extends Service> getServiceClass()
    {
        return InitService.class;
    }

}
