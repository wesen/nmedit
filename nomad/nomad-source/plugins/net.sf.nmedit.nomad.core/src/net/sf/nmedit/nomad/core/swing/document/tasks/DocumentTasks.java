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
 * Created on Jun 1, 2006
 */
package net.sf.nmedit.nomad.core.swing.document.tasks;

import java.io.File;

import net.sf.nmedit.nomad.core.swing.document.Document;

public interface DocumentTasks
{

    /**
     * Returns the file, associated with the specified document.
     * The return value is <code>null</code> when the 
     * document has not been saved before.
     * 
     * @param d the document
     * @return the file, associated with the specified document
     */
    File getAssociatedFile(Document d);

    /**
     * Chooses a adequate file that can be associated with
     * the specified document.
     * 
     * The file has not to be created. This operation could
     * for example show a file save dialog to let the user
     * choose a file name. 
     * 
     * A return value of <code>null</code> means that
     * any operation (closing) that will result in a loss
     * of the documents information should be aborted.  
     * 
     * @param d
     * @return the newly associated file
     */
    File getAssociateWithFile(Document d);
    
    /**
     * Returns true when the specified document is modified
     * or newly create and thus has to be saved.
     * 
     * @param d
     * @return true when the specified document is modified
     */
    boolean isModified(Document d);

    /**
     * Savely closes the specified documents.
     * Savely means that the operation will not result in a loss 
     * of information. The operation uses {@link #isModified(Document)}
     * to decide whether a document is modified or not.
     * If it is not modified, it is closed immediatelly.
     * If it is modified, the document will be saved in it's
     * associated file. If and only if the document was saved
     * it will be closed immediately.
     * 
     * @param documents
     * @return number of documents that where savely closed
     */
    int closeSavely(Document ... documents);
    
    /**
     * Closes the specified document immediatelly without
     * checking if it was modified. Unsafed modifications
     * are lost.
     * 
     * @param d 
     */
    void closeImmediatelly(Document d);

    /**
     * 
     * TODO throw exception when operation fails
     * 
     * @param d
     * @param file
     */
    void saveAs( Document d, File file );
}
