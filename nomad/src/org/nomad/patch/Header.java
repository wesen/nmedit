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
 * Created on Feb 13, 2006
 */
package org.nomad.patch;


public class Header {

	// defaults ???
	
	private int keyboard_range_min = 0;
	private int keyboard_range_max = 127;

	private int velocity_range_min = 0;
	private int velocity_range_max = 127;

	private int portamento_time = 0;
	private boolean portamento_auto = false;

	private boolean voice_retrigger_poly_active = false;
	private boolean voice_retrigger_common_active = false;

	private int[] unknown = new int[]{0,0,0,0};
	
	private boolean[] cable_visibility = new boolean[7];
	
	private int bend_range = 0;
	private int requested_voices = 0;
	private int separator_position = 0;
	private int octave_shift = 0;

	public boolean isCableVisible(CableColor cable) {
		return cable_visibility[cable.ColorID];
	}
	
	public int getUnknown(int index) {
		return unknown[index];
	}
	
	public void setUnknown(int index, int value) {
		unknown[index]=value;
	}
	
	public int getUnknown1() { return getUnknown(0); }
	public int getUnknown2() { return getUnknown(1); }
	public int getUnknown3() { return getUnknown(2); }
	public int getUnknown4() { return getUnknown(3); }

	public void setUnknown1(int value) { setUnknown(0, value); }
	public void setUnknown2(int value) { setUnknown(1, value); }
	public void setUnknown3(int value) { setUnknown(2, value); }
	public void setUnknown4(int value) { setUnknown(3, value); }
	
	public void setCableVisible(CableColor color, boolean visible) {
		cable_visibility[color.ColorID] = visible;
	}

	public int getBendRange() {
		return bend_range;
	}

	public void setBendRange(int bend_range) {
		this.bend_range = bend_range;
	}

	public int getKeyboardRangeMax() {
		return keyboard_range_max;
	}

	public void setKeyboardRangeMax(int keyboard_range_max) {
		this.keyboard_range_max = keyboard_range_max;
	}

	public void setKeyboardRange(int min, int max) {
		setKeyboardRangeMin(min);
		setKeyboardRangeMax(max);
	}

	public int getKeyboardRangeMin() {
		return keyboard_range_min;
	}

	public void setKeyboardRangeMin(int keyboard_range_min) {
		this.keyboard_range_min = keyboard_range_min;
	}

	public int getOctaveShift() {
		return octave_shift;
	}

	public void setOctaveShift(int octave_shift) {
		this.octave_shift = octave_shift;
	}

	public boolean isPortamentoAutoEnabled() {
		return portamento_auto;
	}

	public void setPortamentoAutoEnabled(boolean enable) {
		this.portamento_auto = enable;
	}

	public void setPortamento(int portamento_time, boolean autoEnabled) {
		setPortamentoTime(portamento_time);
		setPortamentoAutoEnabled(autoEnabled);
	}

	public int getPortamentoTime() {
		return portamento_time;
	}

	public void setPortamentoTime(int portamento_time) {
		this.portamento_time = portamento_time;
	}

	public int getRequestedVoices() {
		return requested_voices;
	}

	public void setRequestedVoices(int requested_voices) {
		this.requested_voices = requested_voices;
	}

	public int getSeparatorPosition() {
		return separator_position;
	}

	public void setSeparatorPosition(int separator_position) {
		this.separator_position = separator_position;
	}

	public int getVelocityRangeMax() {
		return velocity_range_max;
	}

	public void setVelocityRangeMax(int velocity_range_max) {
		this.velocity_range_max = velocity_range_max;
	}

	public int getVelocityRangeMin() {
		return velocity_range_min;
	}

	public void setVelocityRangeMin(int velocity_range_min) {
		this.velocity_range_min = velocity_range_min;
	}

	public void setVelocityRange(int min, int max) {
		setVelocityRangeMin(min);
		setVelocityRangeMax(max);
	}

	public boolean isVoiceRetriggerCommonActive() {
		return voice_retrigger_common_active;
	}

	public void setVoiceRetriggerActive(boolean poly_active, boolean common_active) {
		setVoiceRetriggerCommonActive(common_active);
		setVoiceRetriggerPolyActive(poly_active);
	}

	public void setVoiceRetriggerCommonActive(boolean active) {
		this.voice_retrigger_common_active = active;
	}

	public boolean isVoiceRetriggerPolyActive() {
		return voice_retrigger_poly_active;
	}

	public void setVoiceRetriggerPolyActive(boolean active) {
		this.voice_retrigger_poly_active = active;
	}

	
}
