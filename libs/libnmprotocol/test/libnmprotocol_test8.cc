
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
  
  Listener() {}
  virtual ~Listener() {}

  void messageReceived(IAmMessage message) {
    printf("IAmMessage: ");
    printf("Sender: %d ", message.getSender());
    printf("VersionHigh: %d ", message.getVersionHigh());
    printf("VersionLow: %d \n", message.getVersionLow());
  }

  void messageReceived(LightMessage message) {
    printf("LightMessage: ");
    printf("pid: %d ", message.getPid());
    printf("StartIndex: %d ", message.getStartIndex());
    for (int i = 0; i < 20; i++) {
      printf("%d ", message.getLightStatus(i));
    }
    printf("\n");
  }

  void messageReceived(PatchListMessage message) {
    printf("PatchListMessage: ");
    printf("Section: %d ", message.getSection());
    printf("Position: %d\n", message.getPosition());

    PatchListMessage::StringList names = message.getNames();
    for (PatchListMessage::StringList::iterator i = names.begin();
	 i != names.end(); i++) {
      printf("   %s\n", (*i).c_str());
    }
  }

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
    nmProtocol.addListener(new Listener());

    printf("Send GetPatchListMessage\n");
    BitStream bitStream;
    GetPatchListMessage getPatchListMessage(0, 0);

    nmProtocol.send(&getPatchListMessage);

    while(1) {
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
