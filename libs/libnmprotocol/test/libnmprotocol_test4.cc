
#include <stdio.h>
#include <unistd.h>

#include "nmprotocol/mididriver.h"
#include "nmprotocol/midiexception.h"
#include "nmprotocol/iammessage.h"
#include "pdl/pdlexception.h"
#include "pdl/tracer.h"
#include "nmprotocol/nmprotocollistener.h"
#include "nmprotocol/nmprotocol.h"
#include "nmpatch/patch.h"

class TestTracer : public virtual Tracer
{
public:
  void trace(string message)
  {
    printf("TRACE: %s\n", message.c_str());
  }
};

class Listener : public virtual NMProtocolListener
{
public:
  
  Listener() {}
  virtual ~Listener() {}

  void messageReceived(PatchMessage message) {
    Patch patch;
    message.getPatch(&patch);
    printf("Name: %s\n", patch.getName().c_str());
    printf("%s", patch.write().c_str());    
  }
};

int main(int argc, char** argv)
{
  try {
    
    MidiMessage::usePDLFile("../src/midi.pdl", new TestTracer());
    PatchMessage::usePDLFile("../src/patch.pdl", new TestTracer());

    printf("\nLoad patch\n\n");
    Patch* patch = new Patch(argv[1]);
    patch->setName("Big_Phaser_Pad");
    PatchMessage patchMessage(patch);
    MidiMessage::BitStreamList bitStreamList;
    patchMessage.getBitStream(&bitStreamList);

    printf("\nParse patch\n\n");
    for (MidiMessage::BitStreamList::iterator i = bitStreamList.begin();
	 i != bitStreamList.end(); i++) {
      BitStream bitStream = (*i);
      while (bitStream.isAvailable(8)) {
	printf("%X ", bitStream.getInt(8));
      }
      printf("\n");

      bitStream.setPosition(0);
      MidiMessage* midiMessage = MidiMessage::create(&bitStream);
      if (midiMessage != 0) {
	midiMessage->notifyListener(new Listener());
	delete midiMessage;
      }
    }
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
