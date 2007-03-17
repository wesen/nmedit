package net.sf.nmedit.nordmodular.service;

import net.sf.nmedit.jtheme.clavia.nordmodular.JTNMPatch;
import net.sf.nmedit.nomad.core.service.exportService.ExportService;
import net.sf.nmedit.nomad.core.util.document.Document;
import net.sf.nmedit.nordmodular.PatchDocument;

public class ImageExportService implements ExportService
{

    public void export(Document doc)
    {
        if (!isExportSupported(doc))
            throw new UnsupportedOperationException(); // TODO other exception
        
        
        PatchDocument pdoc = (PatchDocument) doc;
        
        JTNMPatch patch = pdoc.getComponent();
    }

    public boolean isExportSupported(Document doc)
    {
        return doc instanceof PatchDocument;
    }

}
