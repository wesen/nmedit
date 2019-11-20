/*
    Nord Modular patch loader
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

#include <stdio.h>
#include <unistd.h>

#include "nmprotocol/mididriver.h"
#include "nmprotocol/midiexception.h"
#include "nmprotocol/patchmessage.h"
#include "nmprotocol/nmprotocol.h"

#include "pdl/pdlexception.h"

#include "ppf/ppfexception.h"

#include "nmpatch/patch.h"
#include "nmpatch/modulesection.h"
#include "nmpatch/patchexception.h"

#include "nmprotocol/synth.h"
#include "nmprotocol/synthlistener.h"
#include "mainwindow.h"

#include <FL/Fl.H>

extern char *optarg;
extern int optind;

class DebugSynthListener : public SynthListener
{
 public:
  
  DebugSynthListener()
  {
    window = new MainWindow();
  }
  
  virtual ~DebugSynthListener() {}

  virtual void newPatchInSlot(int slot, Patch* patch)
  {
    window->newPatch(patch);
  }

  virtual void patchListChanged()
  {
  }

  virtual void slotStateChanged(int slot, bool active,
				bool selected, int voices)
  {
    printf("Slot %d: %d %d %d\n", slot, active, selected, voices);
  }
  
 private:
  
  MainWindow* window; 

};

int main(int argc, char** argv)
{
  MidiDriver* driver = 0;

  printf("nmEdit version 1, Copyright (C) 2004 Marcus Andersson\n"
	 "nmEdit comes with ABSOLUTELY NO WARRANTY. This is free "
	 "software,\nand you are welcome to redistribute it under certain "
	 "conditions.\n");

  try {
    MidiMessage::usePDLFile("/usr/local/lib/nmprotocol/midi.pdl", 0);
    Patch::usePDLFile("/usr/local/lib/nmpatch/patch.pdl", 0);
    ModuleSection::usePPFFile("/usr/local/lib/nmpatch/module.ppf");

    string drivername, input, output;
    bool error = false;

    int option;
    while ((option = getopt(argc, argv, "d:i:o:")) != -1) {

      switch(option) {

      case 'd':
	drivername = string(optarg);
	break;

      case 'i':
	input = string(optarg);
	break;
	
      case 'o':
	output = string(optarg);
	break;
	
      case '?':
	error = true;

      default:
	break;
      }
    }
    
    if (error || optind > argc) {
      printf("usage: %s \\\n"
	     "        -d mididriver \\\n"
	     "        -i midiinput -o midioutput\n\n",
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

    driver = MidiDriver::createDriver(drivername);
    driver->connect(input, output);
    NMProtocol nmProtocol(driver);
    Synth synth(&nmProtocol);
    DebugSynthListener dsl;
    synth.addListener(&dsl);

    while(1) {
      Fl::check();
      nmProtocol.heartbeat();
      usleep(10000);
    }

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
  catch (ppf::PPFException& exception) {
    printf("PPFException: %s (%d)\n",
	   exception.getMessage().c_str(),
	   exception.getError());
  }

  if (driver) {
    driver->disconnect();
    delete(driver);
  }
}
