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
 * Created on Feb 12, 2006
 */
package org.nomad.patch.format;

public class PatchConstructionException extends Exception {

	private boolean isFormatted = false;

	public PatchConstructionException() {
		super();
	}

	public PatchConstructionException(String message) {
		super(message);
	}

	public PatchConstructionException(String message, boolean isFormatted) {
		super(message);
		this.isFormatted = isFormatted;
	}

	public PatchConstructionException(String message, Throwable t) {
		super(message, t);
	}

	public PatchConstructionException(String message, Throwable t, boolean isFormatted) {
		super(message, t);
		this.isFormatted = isFormatted;
	}

	public PatchConstructionException(Throwable t) {
		super(t);
	}

	public PatchConstructionException(Throwable t, boolean isFormatted) {
		super(t);
		this.isFormatted = isFormatted;
	}

	public boolean isFormatted() {
		return isFormatted;
	}

}
