
#include <stdio.h>
#include <unistd.h>

#include "nmprotocol/mididriver.h"
#include "nmprotocol/midiexception.h"
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

  void messageReceived(NewPatchInSlotMessage message) {
    printf("New patch in slot %d, pid: %d\n",
	   message.getSlot(), message.getPid());
  }

  void messageReceived(VoiceCountMessage message) {
    printf("VoiceCountMessage: %d %d %d %d\n",
	   message.getVoiceCount(0),
	   message.getVoiceCount(1),
	   message.getVoiceCount(2),
	   message.getVoiceCount(3));
  }

  void messageReceived(SlotsSelectedMessage message) {
    printf("SlotsSelectedMessage: %d %d %d %d\n",
	   message.isSelected(0),
	   message.isSelected(1),
	   message.isSelected(2),
	   message.isSelected(3));
  }

  void messageReceived(SlotActivatedMessage message) {
    printf("SlotActivatedMessage: %d\n",
	   message.getActiveSlot());
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

    printf("Send SlotsSelectedMessage\n");
    SlotsSelectedMessage slotsSelectedMessage;
    slotsSelectedMessage.setSelected(0, false);
    slotsSelectedMessage.setSelected(0, true);
    slotsSelectedMessage.setSelected(0, false);
    slotsSelectedMessage.setSelected(0, true);
    nmProtocol.send(&slotsSelectedMessage);

    printf("Send SlotActivatedMessage\n");
    SlotActivatedMessage slotActivatedMessage;
    slotActivatedMessage.setActiveSlot(2);
    nmProtocol.send(&slotActivatedMessage);

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
