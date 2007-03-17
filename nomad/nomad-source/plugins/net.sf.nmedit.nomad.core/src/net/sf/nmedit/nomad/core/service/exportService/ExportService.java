package net.sf.nmedit.nomad.core.service.exportService;

import net.sf.nmedit.nomad.core.util.document.Document;

public interface ExportService
{

    boolean isExportSupported(Document doc);
    
    void export(Document doc);
    
}
