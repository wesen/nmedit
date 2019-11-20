src = 'parser.cc bundle.cc boundbundle.cc'
inc = 'boundbundle.h bundle.h parser.h ppfexception.h'

import os
withJava = int(ARGUMENTS.get('JAVA', 0)) == 1;
java_home = os.environ.get('JAVA_HOME','')

# check is JAVA compilation is requested and JAVA_HOME is set
if not os.environ.has_key('JAVA_HOME') and (withJava == 1):
	print "compilation with JAVA requested, but JAVA_HOME not set, aborting!"
	Exit(1)

if withJava:
  print 'Compiling with java support (JAVA_HOME = "%s")' % java_home
  src = 'ppf.i ' + src

srcfiles = [('src/' + x) for x in Split(src)]
incfiles = [('ppf/' + x) for x in Split(inc)]

ppf = Environment(SWIGFLAGS = '-c++ -java -package nmcom.swig.ppf -outdir ../../java-nmcom/src/nmcom/swig/ppf',
                  CPPPATH = ['.'])

opts = Options('ppf.conf')
opts.Add(PathOption('PREFIX',
                    'Install dir (Defult: /usr/local)',
                    '/usr/local'))
opts.Add(BoolOption('JAVA', 'Set to 1 to compile with java support', 0))
opts.Update(ppf)
opts.Save('ppf.conf', ppf)

conf = Configure(ppf)

if withJava:
  # java include path
  java_include=java_home+'/include'
  ppf.Append(CPPPATH=[java_include])
  
  # check for 'jni_md.h'
  for subdirInclude in os.listdir(java_include):
    mdfile=subdirInclude+'/jni_md.h'
    if os.path.isfile(java_include+'/'+mdfile):
      if conf.CheckCHeader(mdfile):
        mdpath=java_include+'/'+subdirInclude
        ppf.Append(CPPPATH=[mdpath])
        break

  # if 'jni_md.h' is not on the path, this will fail
  if not conf.CheckCHeader('jni.h'):
    print 'jni.h not found!'
    Exit(1)

tcllist = [(x + 'tcl.h') for x in ['', 'tcl8.5/', 'tcl8.4/', 'tcl8.3/', 'tcl8.2/', 'tcl8.1/', 'tcl8.0/']]

tclfound = False
for tcl in tcllist:
   if conf.CheckCHeader(tcl):
      print 'Using ' + tcl
      tclfound = True
      conf.env.Append(CPPFLAGS=('-DTCL_H=\'<' + tcl + '>\''))
      break

if not tclfound:
   print 'tcl.h not found!'
   Exit(1)

tcllibs = Split('tcl tcl8.5 tcl8.4 tcl8.3 tcl8.2 tcl8.1 tcl8.0')

tclfound = False
for tcllib in tcllibs:
   if conf.CheckLib(tcllib):
      conf.env.Append(LIBS=[tcllib])
      tclfound = True
      break
if not tclfound:
   print 'libtcl missing!'
   Exit(1)

ppf = conf.Finish()

idir_prefix = '$PREFIX'
idir_lib    = '$PREFIX/lib'
idir_inc    = '$PREFIX/include/ppf'

lib = ppf.SharedLibrary('ppf', srcfiles)

ppf.Install(idir_lib, lib)
ppf.Install(idir_inc, incfiles)
ppf.Alias('install', idir_prefix)
