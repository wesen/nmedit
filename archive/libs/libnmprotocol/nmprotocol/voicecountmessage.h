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

#ifndef VOICECOUNTMESSAGE_H
#define VOICECOUNTMESSAGE_H

#include "nmprotocol/midimessage.h"

class Packet;

class VoiceCountMessage : public virtual MidiMessage
{
 public:

  enum Sender {
    PC = 0,
    MODULAR
  };

  VoiceCountMessage();
  VoiceCountMessage(Packet* packet);
  virtual ~VoiceCountMessage();

  virtual void getBitStream(BitStreamList* bitStreamList);

  virtual void notifyListener(NMProtocolListener* listener);
    
  int getVoiceCount(int slot);

 private:
  
  int voices[4];

};

#endif
