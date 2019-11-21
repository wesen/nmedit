
#include <stdio.h>
#include <unistd.h>

#include "nmprotocol/mididriver.h"
#include "nmprotocol/midiexception.h"
#include "nmprotocol/getpatchlistmessage.h"
#include "pdl/protocol.h"
#include "pdl/tracer.h"
#include "nmprotocol/nmprotocollistener.h"
#include "nmprotocol/nmprotocol.h"

class Listener : public virtual NMProtocolListener
{
public:
  
  Listener(NMProtocol* nmProtocol)
  {
    this->nmProtocol = nmProtocol;
  }

  virtual ~Listener() {}

  void messageReceived(PatchListMessage message) {

    printf("PatchListMessage:\n");

    if (message.endOfList()) {
      printf("   END\n");
    }
    else {
      int section = message.getSection();
      int position = message.getPosition();
      
      PatchListMessage::StringList names = message.getNames();
      for (PatchListMessage::StringList::iterator i = names.begin();
	   i != names.end(); i++, position++) {
	if (message.endOfSection(*i)) {
	  section++;
	  position = -1;
	}
	else if (message.emptyPosition(*i)) {
	  printf("   %d %d *EMPTY*\n", section, position);
	}
	else {
	  printf("   %d%02d %d %s\n", section+1, position+1, (*i)[0], (*i).c_str());
	}
      }
      
      section += position / 99;
      position = position % 99;
      if (section < 9) {
	GetPatchListMessage getPatchListMessage(section, position);
	nmProtocol->send(&getPatchListMessage);      
      }
    }
  }

private:

  NMProtocol* nmProtocol;

};

class TestTracer : public virtual Tracer
{
public:
  void trace(string message)
  {
    printf("TRACE: %s\n", message.c_str());
  }
};

int main(int argc, char** argv)
{
  try {
    
    MidiMessage::usePDLFile("../src/midi.pdl", 0);

    MidiDriver* driver =
      MidiDriver::createDriver(*MidiDriver::getDrivers().begin());
    driver->connect("/dev/snd/midiC1D0", "/dev/snd/midiC1D0");

    NMProtocol nmProtocol(driver);
    nmProtocol.addListener(new Listener(&nmProtocol));

    printf("Send GetPatchListMessage\n");
    BitStream bitStream;
    GetPatchListMessage getPatchListMessage(0, 0);

    nmProtocol.send(&getPatchListMessage);

    while(!nmProtocol.sendQueueIsEmpty()) {
      nmProtocol.heartbeat();
    }

    driver->disconnect();
  }
  catch (MidiException& exception) {
    printf("Exception: %s %d\n",
	   exception.getMessage().c_str(),
	   exception.getError());
  }
}
