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
 * Created on Jan 11, 2006
 */
package net.sf.nmedit.jtheme.clavia.nordmodular;

import java.awt.Color;

public class NomadClassicColors {

	public final static Color MORPH_BLUE = Color.decode("#5A5FB3");
	public final static Color MORPH_RED = Color.decode("#CB4F4F" /*"#CE9287"*/);
	public final static Color MORPH_YELLOW = Color.decode("#E5DE45");
	public final static Color MORPH_GREEN = Color.decode("#9AC899");
	public final static Color MORPH_GRAY = Color.decode("#A8A8A8");

	public final static Color BUTTON_BACKGROUND = Color.decode("#A5A5A5");
	public final static Color BUTTON_FOCUSED_BACKGROUND = Color.decode("#C4F6ED");
	public final static Color BUTTON_SELECTED_BACKGROUND = Color.decode("#C6CCDE");
	
	public static final Color BUTTON_FOREGROUND = Color.decode("#EBEAEF");
	public final static Color MODULE_BACKGROUND = Color.decode("#BFBFBF");
	public final static Color TEXT_DISPLAY_BACKGROUND = Color.decode("#372C7B");

	public final static Color AUDIO_LEVEL_DISPLAY_LOW = Color.decode("#087309");
	public final static Color AUDIO_LEVEL_DISPLAY_HIGH = Color.decode("#767518");
	public final static Color AUDIO_LEVEL_DISPLAY_LIGHT = Color.decode("#00CC00");
	public static final Color GROUPBOX_BORDER = Color.decode("#777777");

    /*
	public static Color getConnectorColor(DConnector info) {
		return info==null ? Color.WHITE : getConnectorColor(info.getSignal());
	}
	public static Color getConnectorColor(int signal) {
		switch (signal) {
			case DConnector.SIGNAL_AUDIO: return NomadClassicColors.MORPH_RED;
			case DConnector.SIGNAL_CONTROL: return NomadClassicColors.MORPH_BLUE;
			case DConnector.SIGNAL_LOGIC: return  NomadClassicColors.MORPH_YELLOW;
			case DConnector.SIGNAL_SLAVE: return  NomadClassicColors.MORPH_GRAY; 
		}
		return Color.WHITE;
	}*/
	
}
