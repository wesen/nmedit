#!/bin/sh

while true; do

read module name id
echo "module.$module.name=$name"

read remarks remark
only1=`echo $remark | grep "only 1"`
if [ -n "$only1" ]; then
    echo "module.$module.cardinality=1"
else
    echo "module.$module.cardinality=*"
fi;
cvaonly=`echo $remark | grep "cva only"`
if [ -n "$cvaonly" ]; then
    echo "module.$module.modulesection=0"
else
    echo "module.$module.modulesection=*"
fi;

read cycles cyclesval
echo "module.$module.cycles=$cyclesval"

read progmem progmemval
echo "module.$module.progmem=$progmemval"

read xmem xmemval
echo "module.$module.xmem=$xmemval"

read ymem ymemval
echo "module.$module.ymem=$ymemval"

read zeropage zeropageval
echo "module.$module.zeropage=$zeropageval"

read dynmem dynmemval
echo "module.$module.dynmem=$dynmemval"

read height heightval
echo "module.$module.height=$heightval"

read parameters
read line
while [ "$line" != "inputs" ]; do 
    parameter=`echo $line | cut -d' ' -f1`
    name=`echo $line | cut -d'"' -f2`
    min=`echo $line | cut -d'"' -f3 | cut -d' ' -f2 | cut -d'.' -f1`
    max=`echo $line | cut -d'"' -f3 | cut -d' ' -f2 | cut -d'.' -f3`
    map=`echo $line | cut -d'"' -f4`
    echo "module.$module.parameter.$parameter.name=\"$name\""
    echo "module.$module.parameter.$parameter.min=$min"
    echo "module.$module.parameter.$parameter.max=$max"
    echo "module.$module.parameter.$parameter.map=$map"
    read line
done;

read line
while [ "$line" != "outputs" ]; do 
    input=`echo $line | cut -d' ' -f1`
    name=`echo $line | cut -d'"' -f2`
    type=`echo $line | cut -d'"' -f3 | cut -d' ' -f2`
    echo "module.$module.input.$input.name=\"$name\""
    echo "module.$module.input.$input.type=$type"
    read line
done;

read line
while [ "$line" != "custom" ]; do 
    output=`echo $line | cut -d' ' -f1`
    name=`echo $line | cut -d'"' -f2`
    type=`echo $line | cut -d'"' -f3 | cut -d' ' -f2`
    echo "module.$module.output.$output.name=\"$name\""
    echo "module.$module.output.$output.type=$type"
    read line
done;

read line
while [ "$line" != "----------" ]; do 
    custom=`echo $line | cut -d' ' -f1`
    name=`echo $line | cut -d'"' -f2`
    min=`echo $line | cut -d'"' -f3 | cut -d' ' -f2 | cut -d'.' -f1`
    max=`echo $line | cut -d'"' -f3 | cut -d' ' -f2 | cut -d'.' -f3`
    map=`echo $line | cut -d'"' -f4`
    echo "module.$module.custom.$custom.name=\"$name\""
    echo "module.$module.custom.$custom.min=$min"
    echo "module.$module.custom.$custom.max=$max"
    echo "module.$module.custom.$custom.map=$map"
    read line
done;

echo ""

done
