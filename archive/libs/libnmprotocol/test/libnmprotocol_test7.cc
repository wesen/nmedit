
#include <stdio.h>
#include <unistd.h>

#include "nmprotocol/mididriver.h"
#include "nmprotocol/midiexception.h"
#include "nmprotocol/iammessage.h"
#include "nmprotocol/ackmessage.h"
#include "nmprotocol/requestpatchmessage.h"
#include "nmprotocol/patchmessage.h"
#include "pdl/pdlexception.h"
#include "nmprotocol/nmprotocollistener.h"
#include "nmprotocol/nmprotocol.h"
#include "nmpatch/patch.h"
#include "nmpatch/modulesection.h"

class Listener : public virtual NMProtocolListener
{
public:
  
  NMProtocol* p;
  Patch* patch;

  Listener(NMProtocol* p) { this->p = p;}
  virtual ~Listener() {}

  void messageReceived(AckMessage message) {
    printf("AckMessage: ");
    printf("pid1: %d ", message.getPid1());
    printf("pid2: %d \n", message.getPid2());

    printf("Send GetPatchMessage.\n");
    patch = new Patch();
    GetPatchMessage getPatchMessage(message.getSlot(), message.getPid1());
    p->send(&getPatchMessage);
  }

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

  void messageReceived(PatchMessage message) {
    message.getPatch(patch);
    printf("Name: %s\n", patch->getName().c_str());
    printf("%s", patch->write().c_str());    
  }
};

int main(int argc, char** argv)
{
  try {
    
    MidiMessage::usePDLFile("../src/midi.pdl", 0);
    PatchMessage::usePDLFile("../src/patch.pdl", 0);
    ModuleSection::usePPFFile("../../libnmpatch/src/module.ppf");

    printf("\nRequest patch\n\n");
    MidiDriver* driver =
      MidiDriver::createDriver(*MidiDriver::getDrivers().begin());
    driver->connect("/dev/snd/midiC1D0", "/dev/snd/midiC1D0");

    NMProtocol nmProtocol(driver);
    nmProtocol.addListener(new Listener(&nmProtocol));

    RequestPatchMessage requestPatchMessage;
    nmProtocol.send(&requestPatchMessage);

    while(!nmProtocol.sendQueueIsEmpty()) {
      nmProtocol.heartbeat();
    }

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
