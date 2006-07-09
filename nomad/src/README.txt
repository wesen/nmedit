Nomad - Nord Modular Editor
---------------------------

Starting Nomad
--------------

Nomad can by started by the following command:

	java -jar nomad.jar

Reporting Problems
------------------

Problems related to Nomad can be reported on the nmedit-devel mailing list:
http://sourceforge.net/mailarchive/forum.php?forum_id=18303

Please attach a copy of the log file (nomad.log) to your posting if appropriate.

Nomad - Version 0.2.1 (Current)
-------------------------------

This is a preview release of Nomad. It is not meant for production use.

Please note that using some patches might flood the console with exceptions.
We are aware of that problem and working on it. When this happens it is
not necessary to restart Nomad, instead download an empty patch to the
slot with the problematic patch.

-	full support for reading and writing patch files (Version 3.03)
	Problems:
	-	in same cases user cable colors are lost when importing a file
	-	patch files with wrong line breaks are not supported (no empty lines, no random linebreaks where numbers are expected on a specific line)
	-	occurrences of the string '[/' in the note section are replaced with '\[/' when saving a file
		occurrences of the string '\[/' in the note section are replaced with '\[/' when loading a file
		
		This avoids the problem that following lines are removed both in Nomad and in Clavia's Editor.
		In Clavias Editor the escape character is not removed.

-	(almost) full support for up- and downloading a patch from/to the synthesizer
	Problems:
	-	The played notes are not sent to the synthesizer

-	synchronisation:
	following events are synchronized with the synthesizer:
	-	new module
	-	delete module
	-	move module
	-	new cable
	-	delete cable
	-	parameter changes
	
	other changes made to a patch have to be synchronized manually by sending it to the synthesizer

-	image export of a patch (translucent PNG image)
	Problems:
	-	cables outside of the boundary are truncated

-	modules have no graph displays
-	text displays have no alternating views
-	some button groups are in wrong order
-	moving multiple modules might result in overlapped modules
-	deleting multiple modules freezes the application for a moment until the operation is done

A License
---------

Nomad - Nord Modular Editor
Copyright (C) 2006 Marcus Andersson, Christian Schneider, Ian Hoogeboom

Nomad is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

Nomad is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

B Resources
----------

gpl.txt:	The GPL license
