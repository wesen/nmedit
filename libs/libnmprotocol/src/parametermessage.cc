/*
    Nord Modular Midi Protocol 3.03 Library
    Copyright (C) 2005 Marcus Andersson

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

#include "nmprotocol/parametermessage.h"
#include "nmprotocol/nmprotocollistener.h"
#include "nmprotocol/midiexception.h"
#include "pdl/packet.h"

ParameterMessage::ParameterMessage()
{
  cc = 0x13;
  sc = 0x40;
  slot = 0;
  pid = 0;
  section = 0;
  module = 0;
  parameter = 0;
  value = 0;

  expectsreply = true;
}

ParameterMessage::ParameterMessage(Packet* packet)
{
  ParameterMessage();

  slot = packet->getVariable("slot");
  pid = packet->getVariable("data:pid");
  section = packet->getVariable("data:data:section");
  module = packet->getVariable("data:data:module");
  parameter = packet->getVariable("data:data:parameter");
  value = packet->getVariable("data:data:value");
}

ParameterMessage::ParameterMessage(int pid, int section, int module,
				   int parameter, int value)
{
  ParameterMessage();

  this->pid = pid;
  this->section = section;
  this->module = module;
  this->parameter = parameter;
  this->value = value;
}

ParameterMessage::~ParameterMessage()
{
}

void ParameterMessage::getBitStream(BitStreamList* bitStreamList)
{
  IntStream intStream;
  intStream.append(cc);
  intStream.append(slot);
  intStream.append(pid);
  intStream.append(sc);
  intStream.append(section);
  intStream.append(module);
  intStream.append(parameter);
  intStream.append(value);
  appendChecksum(&intStream);
  
  BitStream bitStream;
  MidiMessage::getBitStream(intStream, &bitStream);
  bitStreamList->push_back(bitStream);
}

void ParameterMessage::notifyListener(NMProtocolListener* listener)
{
  listener->messageReceived(*this);
}

ModuleSection::Type ParameterMessage::getSection()
{
  return (ModuleSection::Type)section;
}

int ParameterMessage::getModule()
{
  return module;
}

ModuleType::Parameter ParameterMessage::getParameter()
{
  return (ModuleType::Parameter)parameter;
}

int ParameterMessage::getValue()
{
  return value;
}

int ParameterMessage::getPid()
{
  return pid;
}
