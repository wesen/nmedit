/*
    nmEdit
    Copyright (C) 2004 Marcus Andersson

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

#include "synth.h"
#include "nmlistener.h"
#include "nmprotocol/nmprotocol.h"
#include "nmpatch/patch.h"

Synth::Synth(NMProtocol* protocol)
{
  this->protocol = protocol;
  protocol->addListener(this);

  IAmMessage iAmMessage;
  iAmMessage.setVersion(3,3);
  protocol->send(&iAmMessage);
}

virtual Synth::~Synth()
{
  protocol->removeListener(this);
}

Patch* Synth::getPatch(int slot)
{
  return patches[slot];
}

void Synth::setPatch(int slot, Patch* patch)
{
  patches[slot] = patch;
  PatchMessage patchMessage(patch);
  patchMessage.setSlot(slot);
  protocol->send(&patchMessage);
  notifyListeners(slot, patch);
}

void Synth::load(int slot, int mempos)
{
}

void Synth::store(int slot, int mempos)
{
}

boolean Synth::isSlotActive(int slot)
{
  return slotActive[slot];
}

void Synth::setSlotActive(int slot, boolean active)
{
  slotActive[slot] = active;
  notifyListeners(slot);
}

boolean Synth::isSlotSelected(int slot)
{
  return slotSelected[slot];
}

void Synth::setSlotSelected(int slot, boolean active)
{
  slotSelected[slot] = active;
  notifyListeners(slot);
}

int Synth::getSlotVoices(int slot)
{
  return slotVoices[slot];
}

void Synth::addListener(SynthListener* listener)
{
  listeners.push_back(listener);
}

void Synth::removeListener(SynthListener* listener)
{
  listeners.remove(listener);
}

void Synth::notifyListeners(int slot, Patch* patch)
{
  for (ListenerList::iterator i = listeners.begin();
       i != listeners.end(); i++) {
    (*i)->newPatchInSlot(slot, patch);
  }
}

void Synth::notifyListeners()
{
  for (ListenerList::iterator i = listeners.begin();
       i != listeners.end(); i++) {
    (*i)->patchListChanged();
  }
}

void Synth::notifyListeners(int slot, boolean active,
			    boolean selected, int voices)
{
  for (ListenerList::iterator i = listeners.begin();
       i != listeners.end(); i++) {
    (*i)->slotStateChanged(slot, slotActive[slot],
			   slotSelected[slot], slotVoices[slot]);
  }
}

void Synth::messageReceived(IAmMessage message)
{
  RequestPatchMessage requestPatchMessage;
  for (int i = 0; i < 4; i++) {
    requestPatchMessage.setSlot(i);
    protocol->send(&requestPatchMessage);
  }
}

void Synth::messageReceived(LightMessage message)
{
}

void Synth::messageReceived(PatchMessage message)
{
  message.getPatch(patches[message.getSlot()]);
  notifyListeners(message.getSlot(), patches[message.getSlot()]);
}

void Synth::messageReceived(AckMessage message)
{
}

void Synth::messageReceived(PatchListMessage message)
{
}

void Synth::messageReceived(NewPatchInSlotMessage message)
{
  RequestPatchMessage requestPatchMessage;
  requestPatchMessage.setSlot(message.getSlot());
  nmProtocol.send(&requestPatchMessage);
}

void Synth::messageReceived(VoiceCountMessage message)
{
  for (int i = 0; i < 4; i++) {
    if (voiceCount[i] != message.getVoiceCount(i)) {
      voiceCount[i] = message.getVoiceCount(i);
      notifyListeners(i);
    }
  }
}

void Synth::messageReceived(GetPatchMessage message)
{
}

void Synth::messageReceived(SlotsSelectedMessage message)
{
}

void Synth::messageReceived(SlotActivatedMessage message)
{
}
