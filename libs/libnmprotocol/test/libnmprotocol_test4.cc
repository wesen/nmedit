
#include <stdio.h>
#include <unistd.h>

#include "nmprotocol/mididriver.h"
#include "nmprotocol/midiexception.h"
#include "nmprotocol/iammessage.h"
#include "pdl/protocol.h"
#include "nmprotocol/nmprotocollistener.h"
#include "nmprotocol/nmprotocol.h"
#include "nmpatch/patch.h"

class Listener : public virtual NMProtocolListener
{
public:
  
  Listener() {}
  virtual ~Listener() {}

  void messageReceived(PatchMessage message) {
    Patch* patch = message.getPatch();
    printf("%s", patch->write().c_str());    
  }
};

int main(int argc, char** argv)
{
  try {
    
    MidiMessage::usePDLFile("../src/midi.pdl");
    PatchMessage::usePDLFile("../src/patch.pdl");

    printf("\nLoad patch\n\n");
    Patch* patch = new Patch(argv[1]);
    PatchMessage patchMessage(patch);
    BitStream bitStream;
    patchMessage.getBitStream(&bitStream);

    printf("\nParse patch\n\n");
    MidiMessage* midiMessage = MidiMessage::create(&bitStream);
    if (midiMessage != 0) {
      midiMessage->notifyListener(new Listener());
      delete midiMessage;
    }
  }
  catch (MidiException& exception) {
    printf("Exception: %s %d\n",
	   exception.getMessage().c_str(),
	   exception.getError());
  }
}
