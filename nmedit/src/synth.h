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
  
  Synth(NMProtocol*);
  
  virtual ~Synth();

  Patch *getPatch(int);
  void setPatch(int, Patch*);

  void load(int, int);
  void store(int, int);

  int getActiveSlot();
  void setActiveSlot(int);

  bool isSlotSelected(int);
  void setSlotSelected(int, bool);

  int getSlotVoices(int);

  void addListener(SynthListener*);
  void removeListener(SynthListener*);

  void notifyListeners(int slot, Patch* patch);
  void notifyListeners();
  void notifyListeners(int slot);

  virtual void messageReceived(IAmMessage message);
  virtual void messageReceived(LightMessage message);
  virtual void messageReceived(PatchMessage message);
  virtual void messageReceived(AckMessage message);
  virtual void messageReceived(PatchListMessage message);
  virtual void messageReceived(NewPatchInSlotMessage message);
  virtual void messageReceived(VoiceCountMessage message);
  virtual void messageReceived(SlotsSelectedMessage message);
  virtual void messageReceived(SlotActivatedMessage message);
  virtual void messageReceived(ParameterMessage message);

 private:
  
  typedef list<SynthListener*> SynthListenerList;
  typedef map<int, Patch*> PatchMap;
  typedef map<int, bool> SelectedMap;
  typedef map<int, int> VoiceMap;
  typedef map<int, int> PidMap;

  NMProtocol* protocol;
  SynthListenerList listeners;
  PatchMap patches;
  PidMap pids;
  int activeSlot;
  SelectedMap slotSelected;
  VoiceMap slotVoices;
};

#endif
