/*
    libnmProtocol
    Copyright (C) 2004-2005 Marcus Andersson

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

#include "nmprotocol/synth.h"
#include "nmprotocol/synthlistener.h"
#include "nmprotocol/nmprotocol.h"
#include "nmpatch/patch.h"
#include "nmpatch/modulelistener.h"
#include "nmpatch/modulesectionlistener.h"
#include "pdl/tracer.h"
#include "pdl/protocol.h"

#include <stdio.h>

class SynthModuleListener : public ModuleListener
{
  NMProtocol* protocol;
  ParameterMessage parameterMessage;

public:

  SynthModuleListener(NMProtocol* protocol,
		      int pid, int slot, int section, int module)
  {
    this->protocol = protocol;
    parameterMessage = ParameterMessage(pid, section, module, 0, 0);
    parameterMessage.setSlot(slot);
  }

  void parameterChanged(ModuleType::Parameter param, int value)
  {
    parameterMessage.setParameter(param);
    parameterMessage.setValue(value);
    protocol->send(&parameterMessage);
  }
};

class SynthSectionListener : public ModuleSectionListener
{
  NMProtocol* protocol;
  int pid;
  int slot;
  int section;

public:

  SynthSectionListener(NMProtocol* protocol, int pid, int slot, int section)
  {
    this->protocol = protocol;
    this->pid = pid;
    this->slot = slot;
    this->section = section;
  }

  void newModule(Module* module, int index)
  {
    module->
      addListener(new SynthModuleListener(protocol, pid, slot,
					  section, index));
  }
};

Synth::Synth(NMProtocol* protocol)
{
  this->protocol = protocol;
  protocol->addListener(this);

  activeSlot = 0;
  for (int slot = 0; slot < 4; slot++) {
    pids[slot] = -1;
  }

  IAmMessage iAmMessage;
  iAmMessage.setVersion(3,3);
  protocol->send(&iAmMessage);
}

Synth::~Synth()
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

int Synth::getActiveSlot()
{
  return activeSlot;
}

void Synth::setActiveSlot(int slot)
{
  int oldActiveSlot = activeSlot;
  activeSlot = slot;

  SlotActivatedMessage message;
  message.setActiveSlot(slot);
  protocol->send(&message);

  notifyListeners(oldActiveSlot);
  notifyListeners(slot);
}

bool Synth::isSlotSelected(int slot)
{
  return slotSelected[slot];
}

void Synth::setSlotSelected(int slot, bool active)
{
  slotSelected[slot] = active;
  notifyListeners(slot);

  SlotsSelectedMessage message;
  for (int slot = 0; slot < 4; slot++) {
    message.setSelected(slot, slotSelected[slot]);
  }
  protocol->send(&message);
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
  for (SynthListenerList::iterator i = listeners.begin();
       i != listeners.end(); i++) {
    (*i)->newPatchInSlot(slot, patch);
  }
}

void Synth::notifyListeners()
{
  for (SynthListenerList::iterator i = listeners.begin();
       i != listeners.end(); i++) {
    (*i)->patchListChanged();
  }
}

void Synth::notifyListeners(int slot)
{
  for (SynthListenerList::iterator i = listeners.begin();
       i != listeners.end(); i++) {
    (*i)->slotStateChanged(slot, activeSlot == slot,
			   slotSelected[slot], slotVoices[slot]);
  }
}

void Synth::messageReceived(IAmMessage message)
{
  printf("IAM\n");
  RequestPatchMessage requestPatchMessage;
  for (int slot = 0; slot < 4; slot++) {
    requestPatchMessage.setSlot(slot);
    protocol->send(&requestPatchMessage);
  }
}

void Synth::messageReceived(LightMessage message)
{
}

void Synth::messageReceived(PatchMessage message)
{
  int slot = message.getSlot();
  message.getPatch(patches[slot]);
}

void Synth::messageReceived(AckMessage message)
{
  printf("ACK\n");
  int slot = message.getSlot();
  int pid = message.getPid1();
  if (pid != pids[slot]) {
    pids[slot] = pid;
    patches[slot] = new Patch();
    GetPatchMessage getPatchMessage(slot, pids[slot]);
    protocol->send(&getPatchMessage);
    notifyListeners(slot, patches[slot]);
    patches[slot]->getModuleSection(ModuleSection::POLY)->
      addListener(new SynthSectionListener(protocol, pid, slot,
					   ModuleSection::POLY));
  }
}

void Synth::messageReceived(PatchListMessage message)
{
}

void Synth::messageReceived(NewPatchInSlotMessage message)
{
  int slot = message.getSlot();
  printf("New patch %d\n", slot);
  pids[slot] = message.getPid();
  patches[slot] = new Patch();
  GetPatchMessage getPatchMessage(slot, pids[slot]);
  protocol->send(&getPatchMessage);
  notifyListeners(slot, patches[slot]);
}

void Synth::messageReceived(VoiceCountMessage message)
{
  printf("Voice\n");
  for (int slot = 0; slot < 4; slot++) {
    if (slotVoices[slot] != message.getVoiceCount(slot)) {
      slotVoices[slot] = message.getVoiceCount(slot);
      notifyListeners(slot);
    }
  }
}

void Synth::messageReceived(SlotsSelectedMessage message)
{
  printf("Select\n");
  for (int slot = 0; slot < 4; slot++) {
    if (slotSelected[slot] != message.isSelected(slot)) {
      slotSelected[slot] = message.isSelected(slot);
      notifyListeners(slot);
    }
  }
}

void Synth::messageReceived(SlotActivatedMessage message)
{
  printf("Active\n");
  int oldActiveSlot = activeSlot;
  activeSlot = message.getActiveSlot();

  notifyListeners(oldActiveSlot);
  notifyListeners(activeSlot);
}

void Synth::messageReceived(ParameterMessage message)
{
  patches[message.getSlot()]->
    getModuleSection(message.getSection())->
    getModule(message.getModule())->
    setParameter(message.getParameter(), message.getValue());
}

