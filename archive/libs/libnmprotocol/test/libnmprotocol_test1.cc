
#include <stdio.h>
#include <unistd.h>

#include "nmprotocol/mididriver.h"
#include "nmprotocol/midiexception.h"

int main(int argc, char** argv)
{
  try {
    
    MidiDriver* driver =
      MidiDriver::createDriver(*MidiDriver::getDrivers().begin());
    driver->connect(*driver->getMidiInputPorts().begin(),
		    *driver->getMidiOutputPorts().begin());

    while(1) {
      MidiDriver::Bytes bytes;
      driver->receive(bytes);
      for (MidiDriver::Bytes::iterator i = bytes.begin();
	   i != bytes.end(); i++) {
	printf("%X ", *i);
      }
      if (bytes.size() > 0) {
	printf("\n\n");
      }
    }

    driver->disconnect();
  }
  catch (MidiException& exception) {
    printf("Exception: %s %d\n",
	   exception.getMessage().c_str(),
	   exception.getError());
  }
}
