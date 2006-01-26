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
 * Created on Jan 25, 2006
 */
package org.nomad.util.misc;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class NomadUtilities {

	public static void setupAndShow(JFrame frame, double dw, double dh) {
	    Dimension screensz  = Toolkit.getDefaultToolkit().getScreenSize();
	    setupAndShow(frame, new Dimension((int)(screensz.width*dw), (int)(screensz.height*dh)));
	}

	public static void setupAndShow(JFrame frame, Dimension size) {
	    Dimension screensz  = Toolkit.getDefaultToolkit().getScreenSize();
	    size.height = Math.min(size.height, screensz.height);
	    size.width  = Math.min(size.width,  screensz.width);
	    
		frame.setPreferredSize(size);
		frame.setSize(size);
	
	    frame.setLocation(
	      (screensz.width- size.width)/2,
	      (screensz.height-size.height)/2
	    );
		frame.validate();

	    // set close operation, then show window
	    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    frame.setVisible(true);
	}
	
	public static void infoDialog(Component c, String message) {
		JOptionPane.showMessageDialog(c, message, "Information", JOptionPane.INFORMATION_MESSAGE); // info message
	}

	public static boolean isConfirmedByUser(Component c, String text) {
		return isConfirmedByUser(c, text, "confirm");
	}
	
	public static boolean isConfirmedByUser(Component c, String text, String label) {
		return JOptionPane.showConfirmDialog(c, text, label, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
	}
	
}
