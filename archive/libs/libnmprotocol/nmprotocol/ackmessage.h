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

#ifndef ACKMESSAGE_H
#define ACKMESSAGE_H

#include "nmprotocol/midimessage.h"

class Packet;

class AckMessage : public virtual MidiMessage
{
 public:

  AckMessage();
  AckMessage(Packet* packet);
  virtual ~AckMessage();

  void setPid1(int pid);
  void setPid2(int pid);

  virtual void getBitStream(BitStreamList* bitStreamList);

  virtual void notifyListener(NMProtocolListener* listener);
    
  int getPid1();
  int getPid2();

 private:
  
  int pid1;
  int pid2;
  int type;
};

#endif
