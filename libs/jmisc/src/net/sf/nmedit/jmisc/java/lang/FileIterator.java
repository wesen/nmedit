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
 * Created on Sep 5, 2006
 */
package net.sf.nmedit.jmisc.java.lang;

import java.io.File;
import java.io.FileFilter;
import java.util.Queue;

/**
 * A file iterator capable of iterating directories recursive.
 * @author Christian Schneider
 */
public final class FileIterator extends BFSIterator<File>
{
    
    // the file filter or null when no filter is used
    private final FileFilter fileFilter;

    /**
     * Creates an recursive file iterator with no {@link FileFilter}.
     * @see #FileIterator(Queue, File, FileFilter)
     * @see BFSIterator#BFSIterator(Queue, E)
     */
    public FileIterator( File start )
    {
        this( start, null );
    }

    /**
     * Creates an recursive file iterator with no {@link FileFilter}.
     * @see #FileIterator(Queue, File, FileFilter)
     */
    public FileIterator( Queue<File> queue, File start )
    {
        this( queue, start, null );
    }

    /**
     * Creates an recursive file iterator.
     * @see #FileIterator(Queue, File, FileFilter)
     * @see BFSIterator#BFSIterator(Queue, E)
     */
    public FileIterator( File start, FileFilter fileFilter )
    {
        super( start );
        this.fileFilter = fileFilter;
    }

    /**
     * Creates an recursive file iterator.
     * If start is null the iteration is empty.
     * If start is a file the iteration contains only this file.
     * If start is a directory the iteration starts with the given directory and
     * iterates over all containing files and subdirectories that are accpeted by the {@link FileFilter}.  
     * Note, the specified start of the iteration is always added to the iteration if accpected by the FileFilter or not.
     * 
     * @param queue queue used for buffering
     * @param start start of the iteration
     * @param fileFilter the file filter or <code>null</code> when the files are not filtered
     */
    public FileIterator( Queue<File> queue, File start, FileFilter fileFilter )
    {
        super( queue, start );
        this.fileFilter = fileFilter;
    }

    /**
     * Only if parent is a directory, it's files and sub-directories (first level) are enqueued.
     * If the {@link FileFilter} is available, only files and directories which were accepted are
     * added to the queue.
     * @param queue the queue
     * @param parent the parent file
     */
    @Override
    protected void enqueueChildren( Queue<File> queue, File parent )
    {
        if (parent.isDirectory())
        {
            // note: fileFilter can be null but we add the additional check
            // for optimization (listFiles() does not have to check whether fileFilter is null or not)
            File[] files = fileFilter == null ? parent.listFiles() : parent.listFiles(fileFilter);
            if (files.length>0)
            {
                // to reduce memory usage we sort the children so that files are before directories
                // they will be removed from the buffer before new files in directories are appended
                fdSort(files);
                // append files to the queue
                for (int i=0;i<files.length;i++)
                    queue.offer(files[i]);
            }
        }
    }

    /**
     * The file iterator does not allow removing elements.
     */
    public final void remove()
    {
        throw new UnsupportedOperationException();
    }
    
    /**
     * It is not necessary to check for modifications.
     */
    protected final void checkMod()
    {
        // not checked
    }
    
    /**
     * Sorts an array of files so that normal files come first and directories are at the end of the array.
     * Thus it assures the sequence of files is (file1, file2, ..., dir1, dir2, ...)
     * 
     * The array is sorted in O(n).
     * @param files array of files
     */
    public static void fdSort(File[] files)
    {
        final int len = files.length;
        if (len<=0) return;
        
        // index of the first directory in the array or -1 if there is none
        // di<i
        int di=-1;
        for (int i=0;i<len;i++)
        {
            File f = files[i];
            
            if (f.isFile())
            {
                // check whether there are directories before this file in the array
                if (di>=0)
                {
                    // f not a directory but a normal file
                    // because di<i and isfirstdir(@di) and isfile(@i) we have to swap both
                    // to create the order (file1,file2...,dir1,dir2,...)
                    File dir = files[di]; // remember dir
                    files[di] = f; // store file at position of the first directory in the array
                    files[i] = dir; // put the directory to the current location
                    di++; // files[di] is now file so we have to increment di so that it points to the next directory
                }
            }
            else if (di<0)
            {
                // f is the first directory in the array, we have to remember its position
                di = i;
            }
        }
    }

}
