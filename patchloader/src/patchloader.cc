/*
    Nord Modular patch loader
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

#include <stdio.h>
#include <unistd.h>

#include "nmprotocol/mididriver.h"
#include "nmprotocol/midiexception.h"
#include "nmprotocol/patchmessage.h"
#include "nmprotocol/nmprotocol.h"

#include "pdl/pdlexception.h"

#include "nmpatch/patch.h"
#include "nmpatch/patchexception.h"

void load(string, string, int, string, string, string);

extern char *optarg;
extern int optind;

int main(int argc, char** argv)
{
  printf("Patchloader version %s, Copyright (C) 2003 Marcus Andersson\n"
	 "Patchloader comes with ABSOLUTELY NO WARRANTY. This is free\n"
	 "software, and you are welcome to redistribute it under certain\n"
	 "conditions.\n", VERSION);

  try {
    MidiMessage::usePDLFile("/usr/local/lib/nmprotocol/midi.pdl", 0);
    PatchMessage::usePDLFile("/usr/local/lib/nmprotocol/patch.pdl", 0);

    string patchname, drivername, input, output;
    bool error = false;
    int slot = 0;

    int option;
    while ((option = getopt(argc, argv, "n:s:d:i:o:")) != -1) {

      switch(option) {

      case 'n':
	patchname = string(optarg);
	break;
	
      case 'd':
	drivername = string(optarg);
	break;

      case 'i':
	input = string(optarg);
	break;
	
      case 'o':
	output = string(optarg);
	break;
	
      case 's':
	sscanf(optarg, "%d", &slot);
	if (slot < 0 || slot > 3) {
	  error = true;
	}
	break;

      case '?':
	error = true;

      default:
	break;
      }
    }
    
    if (error || optind >= argc) {
      printf("usage: %s -n patchname -s {0,1,2,3} -d mididriver\n"
	     "                   -i midiinput -o midioutput patchfile\n\n",
	     argv[0]);

      printf("Available midi drivers:\n");
      MidiDriver::StringList drivers = MidiDriver::getDrivers();
      for (MidiDriver::StringList::iterator i = drivers.begin();
	   i != drivers.end(); i++) {
	printf(" %s\n", (*i).c_str());
	MidiDriver* driver = MidiDriver::createDriver(*i);
	printf("  inputs:");
	MidiDriver::StringList inputs = driver->getMidiInputPorts();
	for (MidiDriver::StringList::iterator j = inputs.begin();
	     j != inputs.end(); j++) {
	  printf(" %s", (*j).c_str());
	}
	printf("\n  outputs:");
	MidiDriver::StringList outputs = driver->getMidiOutputPorts();
	for (MidiDriver::StringList::iterator j = outputs.begin();
	     j != outputs.end(); j++) {
	  printf(" %s", (*j).c_str());
	}
	printf("\n\n");
      }
      exit(1);
    }

    printf("Loading patch...\n");
    load(argv[optind], patchname, slot, drivername, input, output);
    printf("Done.\n");

  }
  catch (MidiException& exception) {
    printf("MidiException: %s (%d)\n",
	   exception.getMessage().c_str(),
	   exception.getError());
  }
  catch (PDLException& exception) {
    printf("PDLException: %s (%d)\n",
	   exception.getMessage().c_str(),
	   exception.getError());
  }
  catch (PatchException& exception) {
    printf("PatchException: %s (%d)\n",
	   exception.getMessage().c_str(),
	   exception.getError());
  }
}

void load(string filename, string patchname, int slot,
	  string drivername, string input, string output)
{
  Patch* patch = new Patch(filename);
  patch->setName(patchname);
  PatchMessage patchMessage(patch);
  patchMessage.setSlot(slot);
  
  MidiDriver* driver = MidiDriver::createDriver(drivername);
  driver->connect(input, output);
  
  NMProtocol nmProtocol;
  nmProtocol.useMidiDriver(driver);
  
  nmProtocol.send(&patchMessage);
  
  while(!nmProtocol.sendQueueIsEmpty()) {
    nmProtocol.heartbeat();
    usleep(10000);
  }
  
  nmProtocol.useMidiDriver(0);
  driver->disconnect();
}
