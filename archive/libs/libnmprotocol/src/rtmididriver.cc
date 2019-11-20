/*
    Nord Modular Midi Protocol 3.03 Library
    Copyright (C) 2003 Marcus Andersson

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

#ifdef RTMIDI

#include "nmprotocol/midiexception.h"
#include "nmprotocol/rtmididriver.h"

RtMidiDriver::RtMidiDriver()
{
	init();
}

void RtMidiDriver::init()
{
	try
	{
		midi_in = new RtMidiIn();
	}
	catch (RtError &error)
	{
		// rethrow error
		throw MidiException("Failed to open midi input port: " +
				error.getMessage(), 0);
	}

	try
	{
		midi_out = new RtMidiOut();
	}
	catch (RtError &error)
	{
		// rethrow error
		throw MidiException("Failed to open midi output port: " +
				error.getMessage(), 0);
	}
}

RtMidiDriver::~RtMidiDriver()
{
	delete midi_out;
	delete midi_in;
}


RtMidiDriver::StringList RtMidiDriver::getMidiPortNames(RtMidi *port)
{
	StringList ports;
	
	for(int i = 0; i < port->getPortCount(); i++)
		try
		{
			ports.push_back(port->getPortName(i));
		}
		catch (RtError &error)
		{
			throw MidiException("Failed to get midi port name: " +
					error.getMessage(), 0);
		}

	return ports;
}

RtMidiDriver::StringList RtMidiDriver::getMidiInputPorts()
{
	return getMidiPortNames(midi_in);
}

RtMidiDriver::StringList RtMidiDriver::getMidiOutputPorts()
{
	return getMidiPortNames(midi_out);
}

void RtMidiDriver::openPortByName(RtMidi *port, string name)
{
	// find and open midi port
	for(int i = 0; i < port->getPortCount(); i++)
		try
		{
			if (port->getPortName(i) != name)
				continue;
			
			port->openPort(i);
			break;
		}
		catch (RtError &error)
		{
			throw MidiException("Failed to open midi port: " +
					error.getMessage(), 0);
		}
}

void RtMidiDriver::connect(string midiInputPort, string midiOutputPort)
{
	openPortByName(midi_in, midiInputPort);
	openPortByName(midi_out, midiOutputPort);
}

void RtMidiDriver::disconnect()
{
	// RtMidi cannot disconnect, so instead deinit entire system
	delete midi_in;
	delete midi_out;

	// reinit after "disconnect"
	init();
}

void RtMidiDriver::send(Bytes bytes)
{
	midi_out->sendMessage(&bytes);
}

void RtMidiDriver::receive(Bytes& bytes)
{
	midi_in->getMessage(&bytes);
}

#endif // RTMIDI
