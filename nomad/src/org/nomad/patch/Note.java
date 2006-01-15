package org.nomad.patch;

class Note {

	private int note, attack, release;

	Note (int newNote, int newAttack, int newRelease) {
		note = newNote;
		attack = newAttack;
		release = newRelease;
	}	

	public int getNote() {
		return note;
	}

	public int getAttack() {
		return attack;
	}

	public int getRelease() {
		return release;
	}
}	
