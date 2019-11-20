
#include <stdio.h>

#include "pdl/protocol.h"
#include "pdl/bitstream.h"
#include "pdl/intstream.h"
#include "pdl/packet.h"
#include "pdl/packetparser.h"
#include "pdl/tracer.h"

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
  Protocol* p = new Protocol("test1.pdl");

  TestTracer tracer;
  p->useTracer(&tracer);

  BitStream stream;
  Packet packet;

  stream.append(0xf0, 8);
  stream.append(0x33, 8);
  stream.append(   0, 1);
  stream.append(0x16, 5);
  stream.append(   0, 2);
  stream.append(0x06, 8);
  stream.append(   0, 1);
  stream.append(  15, 7);
  stream.append(   0, 1);
  stream.append(0x7f, 7);
  stream.append(   0, 1);
  stream.append(  16, 7);
  stream.append(   0, 1);  
  stream.append( 127, 7);
  stream.append(0xf7, 8);

  printf("!\n");
  p->getPacketParser("Sysex")->parse(&stream, &packet);
  printf("!\n");
  
  printf("%d \n", packet.getVariable("cc"));
  printf("%d \n", packet.getVariable("slot"));
  printf("%s \n", packet.getPacket("data")->getName().c_str());
  printf("%d \n", packet.getPacket("data")->getVariable("pid1"));
  printf("%d \n", packet.getPacket("data")->getVariable("pid2"));
  printf("%d \n", packet.getPacket("data")->getVariable("checksum"));

  
  IntStream data;
  data.append(22);
  data.append(0);
  data.append(16);
  data.append(17);
  data.append(127);
  stream.setSize(0);

  printf("!\n");
  p->getPacketParser("Sysex")->generate(&data, &stream);
  printf("!\n");

  while(stream.isAvailable(8)) {
    printf("%X\n", stream.getInt(8));
  }

  p->useTracer(0);
}
