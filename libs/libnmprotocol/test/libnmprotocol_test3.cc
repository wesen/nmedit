
#include <stdio.h>
#include <unistd.h>

#include "nmprotocol/mididriver.h"
#include "nmprotocol/midiexception.h"
#include "nmprotocol/iammessage.h"
#include "pdl/protocol.h"
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
};

int main(int argc, char** argv)
{
  try {
    
    MidiMessage::usePDLFile("../src/midi.pdl", 0);

    MidiDriver* driver =
      MidiDriver::createDriver(*MidiDriver::getDrivers().begin());
    driver->connect(*driver->getMidiInputPorts().begin(),
		    *driver->getMidiOutputPorts().begin());

    NMProtocol nmProtocol(driver);
    nmProtocol.addListener(new Listener());

    printf("Send IAmMessage\n");
    BitStream bitStream;
    IAmMessage iAmMessage;
    iAmMessage.setVersion(3,3);

    nmProtocol.send(&iAmMessage);

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
