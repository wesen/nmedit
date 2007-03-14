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
 * Created on Sep 12, 2006
 */
package net.sf.nmedit.nmutils;

import java.io.File;

public class FileSort
{

    /**
     * Sorts an array of files so that normal files come first and directories are at the end of the array.
     * Thus it assures the sequence of files is (file1, file2, ..., dir1, dir2, ...)
     * 
     * The array is sorted in O(n).
     * @param files array of files
     */
    public static void sortFilesDirectories(File[] files)
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
    /**
     * Sorts an array of files so that directories come first and normal files are at the end of the array.
     * Thus it assures the sequence of files is (dir1, dir2, ..., file1, file2, ...)
     * 
     * The array is sorted in O(n).
     * @param files array of files
     */
    public static void sortDirectoriesFiles(File[] files)
    {
        final int len = files.length;
        if (len<=0) return;
        
        // index of the first directory in the array or -1 if there is none
        // di<i
        int fi=-1;
        for (int i=0;i<len;i++)
        {
            File f = files[i];
            
            if (f.isDirectory())
            {
                // check whether there are files before this directories in the array
                if (fi>=0)
                {
                    // f not a normal file but  a directory
                    // because fi<i and isfirstfile(@fi) and isdir(@i) we have to swap both
                    // to create the order (file1,file2...,dir1,dir2,...)
                    File file = files[fi]; // remember dir
                    files[fi] = f; // store file at position of the first directory in the array
                    files[i] = file; // put the file to the current location
                    fi++; // files[di] is now dir so we have to increment fi so that it points to the next directory
                }
            }
            else if (fi<0)
            {
                // f is the first file in the array, we have to remember its position
                fi = i;
            }
        }
    }

}
