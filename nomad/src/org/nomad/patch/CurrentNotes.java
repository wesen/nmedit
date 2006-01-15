package org.nomad.patch;

import java.io.*;
import java.util.*;

class CurrentNotes {
	private Vector currentNotes = null;

	CurrentNotes (){
		Note note;
		currentNotes = new Vector();

		note = new Note(64, 0, 0);
		currentNotes.add(note);
		note = new Note(64, 0, 0);
		currentNotes.add(note);
	}

// Setters

	public void addNotes(String params) {
		Note note;
		String[] paramArray = new String[4];
		currentNotes.clear();
		int newNote, newAttack, newRelease;
		do {
			paramArray = params.split(" ", 4);
			newNote = Integer.parseInt(paramArray[0]);
			newAttack = Integer.parseInt(paramArray[1]);
			newRelease = Integer.parseInt(paramArray[2]);
			params = paramArray[3];
			note = new Note(newNote, newAttack, newRelease);
			currentNotes.add(note);
		} while (params.trim().length() > 0); // trim, omdat en nog een ' ' achter komt.
// Bij het compleet inlezen halen we de laatste weer weg. Deze wordt extra opgeslagen door de Clavia editor
		currentNotes.removeElement(currentNotes.lastElement());
	}

// Getters

	public int getCurrentNoteSize() {
		return currentNotes.size();
	}

	public Note getNote(int index) {
//		Note note;
		return (Note) currentNotes.get(index);
	}

// Inlezen patch gegevens.

	public void readCurrentNoteDump(BufferedReader pchFile) {
//		String dummy;
		try {
			addNotes(pchFile.readLine());
		}
		catch(Exception e) {
			System.out.println(e + " in readCurrentNoteDump");
		}
	}

// Creeren patch gegevens.

	public StringBuffer createCurrentNoteDump(StringBuffer result) {
		Note note = null;
//		if (getCurrentNoteSize() > 0) {
			result.append("[CurrentNoteDump]\r\n");
			for (int i=0; i < getCurrentNoteSize(); i++) {
				note = getNote(i);
				result.append("" + note.getNote() + ' ' + note.getAttack() + ' ' + note.getRelease() + ' ');
			}
// We herhalen de eerste... of moeten we de laatste herhalen om consistent te zijn met de CurrentNoteDump bug?
			note = getNote(0);
			result.append("" + note.getNote() + ' ' + note.getAttack() + ' ' + note.getRelease() + ' ');
			result.append("\r\n[/CurrentNoteDump]\r\n\r\n");
//		}
		return result;
	}
}
