package org.nomad.port;

import org.nomad.plugin.NomadFactory;

public abstract class ComPortFactory extends NomadFactory {

	// public abstract String[] getLibraries();
	public abstract ComPort getInstance();
	
}
