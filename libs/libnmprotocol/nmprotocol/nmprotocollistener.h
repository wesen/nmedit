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

#ifndef NMPROTOCOLLISTENER_H
#define NMPROTOCOLLISTENER_H

#include "nmprotocol/iammessage.h"
#include "nmprotocol/lightmessage.h"
#include "nmprotocol/patchmessage.h"
#include "nmprotocol/ackmessage.h"
#include "nmprotocol/patchlistmessage.h"
#include "nmprotocol/newpatchinslotmessage.h"
#include "nmprotocol/voicecountmessage.h"
#include "nmprotocol/getpatchmessage.h"
#include "nmprotocol/slotsselectedmessage.h"
#include "nmprotocol/slotactivatedmessage.h"

class NMProtocolListener
{
 public:

  NMProtocolListener() {}
  virtual ~NMProtocolListener() {}

  virtual void messageReceived(IAmMessage message) {}
  virtual void messageReceived(LightMessage message) {}
  virtual void messageReceived(PatchMessage message) {}
  virtual void messageReceived(AckMessage message) {}
  virtual void messageReceived(PatchListMessage message) {}
  virtual void messageReceived(NewPatchInSlotMessage message) {}
  virtual void messageReceived(VoiceCountMessage message) {}
  virtual void messageReceived(GetPatchMessage message) {}
  virtual void messageReceived(SlotsSelectedMessage message) {}
  virtual void messageReceived(SlotActivatedMessage message) {}
  
};

#endif
