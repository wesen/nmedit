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

#ifndef LIGHTMESSAGE_H
#define LIGHTMESSAGE_H

#include "nmprotocol/midimessage.h"

class Packet;

class LightMessage : public virtual MidiMessage
{
 public:

  LightMessage();
  LightMessage(Packet* packet);
  virtual ~LightMessage();

  virtual void getBitStream(BitStreamList* bitStreamList);

  virtual void notifyListener(NMProtocolListener* listener);
    
  int getStartIndex();
  int getLightStatus(int lightNo);
  int getPid();

 private:
  
  int pid;
  int sc;
  int startIndex;
  int lights[20];
  int checksum;
};

#endif
