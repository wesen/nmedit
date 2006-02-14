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
 * Created on Jan 19, 2006
 */
package org.nomad.theme.component;

import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTextField;

import org.nomad.patch.Module;
import org.nomad.patch.ui.ModuleUI;
import org.nomad.theme.NomadClassicColors;
import org.nomad.xml.dom.module.DModule;

public class ModuleGuiTitleLabel extends NomadLabel {

	private Module module = null;
	
	public ModuleGuiTitleLabel(DModule info) {
    	setDynamicOverlay(true);
        setText( info.getName() );
        
	    addMouseListener(new MouseAdapter() {
	       	public void mouseClicked(MouseEvent event) {
	       		if (event.getClickCount()==2)
	       			editModuleTitle();
	       	}
	    });
	}

	public void setModule(Module module) {
		this.module = module;
		if (module!=null)
			setText( module.getName() );
	}
	
	private void editModuleTitle() {
		ModuleTitleEditor mte = new ModuleTitleEditor();
		Dimension size = mte.getPreferredSize();
		mte.setSize(Math.max(size.width, ModuleUI.Metrics.WIDTH_DIV_2), size.height);
		mte.setLocation(getLocation());
		getParent().add(mte);
		mte.requestFocus();
	}
   	
	private class ModuleTitleEditor extends JTextField implements KeyListener, FocusListener {
		public ModuleTitleEditor() {
			setBackground(NomadClassicColors.MODULE_BACKGROUND);
			setText(label().getText());
			addKeyListener(this);
			addFocusListener(this);
		}
		
		public ModuleGuiTitleLabel label() {
			return ModuleGuiTitleLabel.this;
		}

		public void keyPressed(KeyEvent event) {
			switch (event.getKeyCode()) {
				case KeyEvent.VK_ESCAPE:
					done(false); break;
				case KeyEvent.VK_ENTER:
					done(true); break;
			}
		}

		public void focusLost(FocusEvent event) {
			done(false);
		}
		
		public void done(boolean writeBack) {
			setVisible(false);
			getParent().remove(this);
			removeKeyListener(this);
			removeFocusListener(this);
			if (writeBack) {
				module.setName(getText());
				label().setText(getText());
			}
		}

		public void keyTyped(KeyEvent event) {
			// TODO Auto-generated method stub
			
		}

		public void keyReleased(KeyEvent event) {
			// TODO Auto-generated method stub
			
		}

		public void focusGained(FocusEvent event) {
			// TODO Auto-generated method stub
			
		}
	}
	
}
