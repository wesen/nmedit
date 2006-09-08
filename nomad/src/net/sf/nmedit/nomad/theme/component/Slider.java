package net.sf.nmedit.nomad.theme.component;
import javax.swing.JComponent;

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
 * Created on Aug 26, 2006
 */

public class Slider extends NomadControl
{

    // TODO implement key event handling / implement morph value
    public Slider()
    {
        setUI(createUI());
    }

    protected SliderUI createUI()
    {
        return (SliderUI) SliderUI.createUI(this);
    }

}
