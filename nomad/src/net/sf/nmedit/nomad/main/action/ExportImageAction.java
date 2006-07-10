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
 * Created on Jul 8, 2006
 */
package net.sf.nmedit.nomad.main.action;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import net.sf.nmedit.nomad.main.FileFilterBuilder;
import net.sf.nmedit.nomad.main.Nomad;
import net.sf.nmedit.nomad.main.resources.AppIcons;
import net.sf.nmedit.nomad.patch.ui.PatchDocument;
import net.sf.nmedit.nomad.patch.ui.PatchImageExport;
import net.sf.nmedit.nomad.patch.ui.PatchUI;
import net.sf.nmedit.nomad.util.NomadUtilities;

public class ExportImageAction extends NomadAction
{

    public ExportImageAction( Nomad nomad )
    {
        super( nomad );
        setEnabled(false);
        putValue(NAME, "Export image...");
        putValue(SHORT_DESCRIPTION, "Export current patch as PNG image");
        putValue(SMALL_ICON, AppIcons.getImageIcon("insert_image2"));
    }

    public void actionPerformed( ActionEvent e )
    {
        exportImageAsTry(getNomad());
    }
    
    public static void exportImageAsTry(Nomad nomad)
    {
        final PatchDocument d = nomad.getActivePatch();
        if (d!=null) 
        {
            final File target = getTarget(nomad);
            
            if (target!=null)
            {
                if (target.exists())
                {
                    if (!NomadUtilities.isConfirmedByUser(nomad, "File already exists. Overwrite it?"))
                        return;
                    
                }
                
                (new Thread(){public void run(){
                    synchronized(d.getPatchUI().getTreeLock())
                    {
                exportImageAs(d.getPatchUI(), target);
                    }
                }}).start();
            }
        }
    }
    
    private static File getTarget(Nomad nomad)
    {
        JFileChooser fChooser = new JFileChooser();
        fChooser.addChoosableFileFilter( FileFilterBuilder.createFileFilter("png", "Portable Network Graphics") );
        fChooser.setMultiSelectionEnabled(false);

        if (fChooser.showSaveDialog( nomad ) == JFileChooser.APPROVE_OPTION)
        {
            return NomadUtilities.assureFileExtensionExists(fChooser.getSelectedFile(), "png"); 
        }
        else
        {
            return null;
        }
    }

    public static void exportImageAs(PatchUI ui, File file)
    {
        PatchImageExport export = new PatchImageExport(ui);
        try
        {
            //export.setBackground(Color.WHITE);
            export.saveExportImagePNG(file);
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
    }
    
}
