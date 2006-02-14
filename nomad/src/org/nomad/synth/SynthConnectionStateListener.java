package org.nomad.synth;

public interface SynthConnectionStateListener {
	
	/**
	 * Called if the state of the current connection to the Nord Modular changes
     * because either a new connection is opened or the current connection is closed.
     * 
	 * @param connection Synth object that had a changed connection state.
	 */
	public void synthConnectionStateChanged(SynthConnection connection) ;

}
