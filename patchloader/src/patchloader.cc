
#include <stdio.h>
#include <unistd.h>

#include "nmprotocol/mididriver.h"
#include "nmprotocol/midiexception.h"
#include "nmprotocol/patchmessage.h"
#include "nmprotocol/nmprotocol.h"

#include "pdl/pdlexception.h"

#include "nmpatch/patch.h"

void load(string, string, string, string, string);

int main(int argc, char** argv)
{
  try {
    
    MidiMessage::usePDLFile("/usr/local/lib/nmprotocol/midi.pdl", 0);
    PatchMessage::usePDLFile("/usr/local/lib/nmprotocol/patch.pdl", 0);

    printf("Loading patch...\n");
    load(argv[1], argv[2], "ALSA", "/dev/snd/midiC1D0", "/dev/snd/midiC1D0");
    printf("Done.\n");

  }
  catch (MidiException& exception) {
    printf("MidiException: %s %d\n",
	   exception.getMessage().c_str(),
	   exception.getError());
  }
  catch (PDLException& exception) {
    printf("PDLException: %s %d\n",
	   exception.getMessage().c_str(),
	   exception.getError());
  }
}

void load(string filename, string patchname,
	  string driverName, string input, string output)
{
  Patch* patch = new Patch(filename);
  patch->setName(patchname);
  PatchMessage patchMessage(patch);
  
  MidiDriver* driver = MidiDriver::createDriver(driverName);
  driver->connect(input, output);
  
  NMProtocol nmProtocol;
  nmProtocol.useMidiDriver(driver);
  
  nmProtocol.send(&patchMessage);
  
  while(!nmProtocol.sendQueueIsEmpty()) {
    nmProtocol.heartbeat();
  }
  
  nmProtocol.useMidiDriver(0);
  driver->disconnect();
}
