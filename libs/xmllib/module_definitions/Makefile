all: moduledef

SOURCES = src/patch303.txt src/s303.txt

moduledef: $(SOURCES)
	../scripts/xmlize_module_table.pl $(SOURCES) ../module_definitions.xml
	cp module_definitions.dtd ../

clean:
	rm -f ../module_definitions.xml
	rm -f ../module_definitions.dtd
