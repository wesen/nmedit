package org.nomad.port;

/**
 * Specialisation of AbstractSynthListener that may be notified
 * if the state of the current connection to the Nord Modular changes
 * because either a new connection is opened or the current connection
 * is closed.
 * 
 * @author Christian Schneideer
 */
public interface SynthConnectionStateListener extends AbstractSynthListener {
	
	/**
	 * Called if the state of the current connection to the Nord Modular changes
     * because either a new connection is opened or the current connection is closed.
     * 
	 * @param synth Synth object that had a changed connection state.
	 */
	public void synthConnectionStateChanged(Synth synth) ;

}
