
#include <stdio.h>
#include <unistd.h>

#include "nmprotocol/iammessage.h"
#include "nmprotocol/midiexception.h"

void printBitStream(BitStream& stream)
{
  while(stream.isAvailable(8)) {
    printf("%X ", stream.getInt(8));
  }
  printf("\n");
}

int main(int argc, char** argv)
{
  try {

    MidiMessage::BitStreamList bitStreamList;
    BitStream bitStream;
    MidiMessage::usePDLFile("../src/midi.pdl");

    printf("IAmMessage\n");
    IAmMessage iamMessage;
    iamMessage.setVersion(3,3);
    bitStream.setSize(0);
    iamMessage.getBitStream(&bitStreamList);
    bitStream = bitStreamList.front();
    printBitStream(bitStream);
    
  }
  catch (MidiException& exception) {
    printf("Exception: %s (errno: %d)\n",
	   exception.getMessage().c_str(),
	   exception.getError());
  }
}
