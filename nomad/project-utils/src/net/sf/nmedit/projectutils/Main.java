/* Copyright (C) 2007 Christian Schneider
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

package net.sf.nmedit.projectutils;

public class Main
{

    public static void main(String[] args) throws Exception
    {
        if (args.length<1)
        {
            System.err.println("missing arguments");
            System.exit(1);
        }
        
        // copy filed 1,2,... (if existing)
        String[] args2 = new String[args.length-1];
        System.arraycopy(args, 1, args2, 0, args.length - 1);
        
        if ("-validate-class-version".equals(args[0]))
            ClassFileVersionValidator.main(args2);
        else if ("-license".equals(args[0]))
            LicenseValidator.main(args2);
        else
        {
            System.err.println("unknown argument "+args[0]);
            System.exit(1);
        }
    }
    
}
