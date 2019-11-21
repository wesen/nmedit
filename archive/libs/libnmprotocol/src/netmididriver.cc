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

#ifdef NETMIDI

#include <unistd.h>
#include <glob.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/ioctl.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <errno.h>
#include <netdb.h>

#include "nmprotocol/midiexception.h"
#include "nmprotocol/netmididriver.h"

NetMidiDriver::NetMidiDriver()
{
}

NetMidiDriver::~NetMidiDriver()
{
  disconnect();
}

NetMidiDriver::StringList NetMidiDriver::getMidiInputPorts()
{
  StringList ports;
  ports.push_back("<hostname>:<portnumber>");
  return ports;
}

NetMidiDriver::StringList NetMidiDriver::getMidiOutputPorts()
{
  StringList ports;
  return ports;
}

void NetMidiDriver::connect(string midiInputPort, string midiOutputPort)
{
  struct sockaddr_in dest_addr;
  struct hostent *host;
  int colonpos = midiInputPort.find(":");
  string hostname = midiInputPort.substr(0, colonpos);;
  string port = midiInputPort.substr(colonpos + 1);

  if((sockfd = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
    throw MidiException("Failed to create socket.", errno);
  }

  if((host = gethostbyname(hostname.c_str())) == 0) {
    throw MidiException("Failed to get host ip address.", h_errno);
  }

  dest_addr.sin_family = AF_INET;
  dest_addr.sin_port = htons(atoi(port.c_str()));
  dest_addr.sin_addr.s_addr =
    inet_addr(inet_ntoa(*(struct in_addr *)(host->h_addr_list[0])));
  memset(&(dest_addr.sin_zero), '\0', 8);

  if(::connect(sockfd,
               (struct sockaddr *)&dest_addr, sizeof(struct sockaddr))) {
    throw MidiException("Failed to connect to socket.", errno);
  }

  int flag = 1;
  if(ioctl(sockfd, FIONBIO, &flag) < 0) {
     throw MidiException("Failed to set non-blocking socket.", errno);
  }
}

void NetMidiDriver::disconnect()
{
  close(sockfd);
}

void NetMidiDriver::send(Bytes bytes)
{
  unsigned char buffer[bytes.size()];
  int n;
  Bytes::iterator i;

  for(n = 0, i = bytes.begin(); i != bytes.end(); i++, n++) {
    buffer[n] = (*i);
  }

  if (::send(sockfd, buffer, bytes.size(), 0) < 0) {
    throw MidiException("Failed to write to midi output port.", errno);
  }
}

void NetMidiDriver::receive(Bytes& bytes)
{
  unsigned char byte;
  int n;

  bytes.clear();

  while ((n = recv(sockfd, &byte, 1, 0)) == 1) {
    inputBuffer.push_back(byte);
    if (byte == SYSEX_END) {
      bytes = inputBuffer;
      inputBuffer.clear();
      return;
    }
  }
  
  if (n < 0 && errno != EWOULDBLOCK) {
    throw MidiException("Failed to read from midi input port.", errno);
  }
}

#endif // NETMIDI
