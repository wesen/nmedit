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

#include <unistd.h>
#include <glob.h>
#include <alsa/asoundlib.h>

#include "nmprotocol/midiexception.h"
#include "nmprotocol/alsadriver.h"

ALSADriver::ALSADriver()
{
}

ALSADriver::~ALSADriver()
{
  disconnect();
}

ALSADriver::StringList ALSADriver::getMidiInputPorts()
{
  return getMidiPorts(O_RDONLY | O_NONBLOCK);
}

ALSADriver::StringList ALSADriver::getMidiOutputPorts()
{
  return getMidiPorts(O_WRONLY | O_NONBLOCK);
}

ALSADriver::StringList ALSADriver::getMidiPorts(int flags)
{
  StringList ports;
  glob_t globdata;
  int fd;

  glob("/dev/snd/midi*", 0, 0, &globdata);
  if (globdata.gl_pathc > 0) {
    int n = 0;
    char* path;
    while ((path = globdata.gl_pathv[n]) != 0) {
      fd = open(path, flags);
      if (fd >= 0) {
	ports.push_back(path);
	close(fd);
      }
      n++;
    }
  }
  globfree(&globdata);

  return ports;
}

void ALSADriver::connect(string midiInputPort, string midiOutputPort)
{
  fd_in = open(midiInputPort.c_str(), O_RDONLY | O_NONBLOCK);
  if (fd_in < 0) {
    throw MidiException("Failed to open midi input port.", errno);
  }

  fd_out = open(midiOutputPort.c_str(), O_WRONLY | O_NONBLOCK);
  if (fd_out < 0) {
    close(fd_in);
    throw MidiException("Failed to open midi output port.", errno);
  }
}

void ALSADriver::disconnect()
{
  close(fd_in);
  close(fd_out);
}

void ALSADriver::send(Bytes bytes)
{
  unsigned char buffer[bytes.size()];
  int n;
  Bytes::iterator i;

  for(n = 0, i = bytes.begin(); i != bytes.end(); i++, n++) {
    buffer[n] = (*i);
  }

  if (write(fd_out, buffer, bytes.size()) < 0) {
    throw MidiException("Failed to write to midi output port.", errno);
  }
}

void ALSADriver::receive(Bytes& bytes)
{
  unsigned char byte;
  int n;

  bytes.clear();

  while ((n = read(fd_in, &byte, 1)) == 1) {
    inputBuffer.push_back(byte);
    if (byte == SYSEX_END) {
      bytes = inputBuffer;
      inputBuffer.clear();
      return;
    }
  }
  
  if (n < 0 && errno != EAGAIN) {
    throw MidiException("Failed to read from midi input port.", errno);
  }
}
