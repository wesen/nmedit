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

#ifndef SYNTH_H
#define SYNTH_H

#include "nmprotocol/nmprotocollistener.h"

#include <map>
#include <list>

class SynthListener;
class NMProtocol;
class Patch;

class Synth : public NMProtocolListener
{
 public:
  
  SynthListener(NMProtocol*);
  
  virtual ~SynthListener();

  Patch *getPatch(int);
  void setPatch(int, Patch*);

  void load(int, int);
  void store(int, int);

  boolean isSlotActive(int);
  void setSlotActive(int, boolean);

  boolean isSlotSelected(int);
  void setSlotSelected(int, boolean);

  int getSlotVoices(int);

  void addListener(SynthListener*);
  void removeListener(SynthListener*);

  void notifyListeners(int slot, Patch* patch);
  void notifyListeners();
  void notifyListeners(int slot):

  virtual void messageReceived(IAmMessage message);
  virtual void messageReceived(LightMessage message);
  virtual void messageReceived(PatchMessage message);
  virtual void messageReceived(AckMessage message);
  virtual void messageReceived(PatchListMessage message);
  virtual void messageReceived(NewPatchInSlotMessage message);
  virtual void messageReceived(VoiceCountMessage message);
  virtual void messageReceived(GetPatchMessage message);
  virtual void messageReceived(SlotsSelectedMessage message);
  virtual void messageReceived(SlotActivatedMessage message);

 private:
  
  typedef list<SynthListener*> SynthListenerList;
  typedef map<int, Patch*> PatchMap;

  NMProtocol* protocol;
  SynthListenerList listeners;
  PatchMap patches;
  boolean slotActive[4];
  boolean slotSelected[4];
  int slotVoices[4];
};

#endif
