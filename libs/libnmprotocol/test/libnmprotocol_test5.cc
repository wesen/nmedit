
#include <stdio.h>
#include <unistd.h>

#include "nmprotocol/mididriver.h"
#include "nmprotocol/midiexception.h"
#include "nmprotocol/iammessage.h"
#include "pdl/pdlexception.h"
#include "nmprotocol/nmprotocollistener.h"
#include "nmprotocol/nmprotocol.h"
#include "nmpatch/patch.h"

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
};

int main(int argc, char** argv)
{
  try {
    
    MidiMessage::usePDLFile("../src/midi.pdl", 0);
    PatchMessage::usePDLFile("../src/patch.pdl", 0);

    printf("\nLoad patch\n\n");
    Patch* patch = new Patch(argv[1]);
    patch->setName("Big_Phaser_Pad");
    PatchMessage patchMessage(patch);
    patchMessage.setPid(0x42);
    MidiMessage::BitStreamList bitStreamList;
    BitStream bitStream;
    patchMessage.getBitStream(&bitStreamList);

    for (MidiMessage::BitStreamList::iterator i = bitStreamList.begin();
	 i != bitStreamList.end(); i++) {
      bitStream = (*i);
      while (bitStream.isAvailable(8)) {
	printf("%X ", bitStream.getInt(8));
      }
      printf("\n");
    }

    MidiDriver* driver =
      MidiDriver::createDriver(*MidiDriver::getDrivers().begin());
    driver->connect(*driver->getMidiInputPorts().begin(),
		    *driver->getMidiOutputPorts().begin());

    NMProtocol nmProtocol;
    nmProtocol.addListener(new Listener());
    nmProtocol.useMidiDriver(driver);

    nmProtocol.send(&patchMessage);

    while(1) {
      nmProtocol.heartbeat();
      sleep(1);
    }

    nmProtocol.useMidiDriver(0);
    driver->disconnect();
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
