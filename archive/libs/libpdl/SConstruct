
src = 'pdlparser.yy pdllex.ll pdllexer.cc bitstream.cc condition.cc constantmatcher.cc intstream.cc matcher.cc packet.cc packetmatcher.cc packetparser.cc protocol.cc tracer.cc variablematcher.cc'

inc = 'bitstream.h condition.h constantmatcher.h intstream.h matcher.h packet.h packetmatcher.h packetparser.h parser.h protocol.h tracer.h variablematcher.h'

import os
withJava = int(ARGUMENTS.get('JAVA', 0)) == 1;
java_home = os.environ.get('JAVA_HOME','')

# check is JAVA compilation is requested and JAVA_HOME is set
if not os.environ.has_key('JAVA_HOME') and (withJava == 1):
	print "compilation with JAVA requested, but JAVA_HOME not set, aborting!"
	Exit(1)

if withJava:
  print 'Compiling with java support (JAVA_HOME = "%s")' % java_home
  src = 'pdl.i %s' % src

srcfiles = [('src/' + x) for x in Split(src)]
incfiles = [('pdl/' + x) for x in Split(inc)]

pdl = Environment(SWIGFLAGS = '-c++ -java -package nmcom.swig.pdl -outdir ../../java-nmcom/src/nmcom/swig/pdl',
                  YACCFLAGS = '-d -ppdl',
                  LEXFLAGS = '-+ -olex.yy.c -Ppdl',
                  CPPPATH = ['.',java_home+'/include/'])

opts = Options('pdl.conf')
opts.Add(PathOption('PREFIX',
                    'Install dir (Defult: /usr/local)',
                    '/usr/local'))
opts.Add(BoolOption('JAVA', 'Set to 1 to compile with java support', 0))
opts.Update(pdl)
opts.Save('pdl.conf', pdl)

conf = Configure(pdl)


if withJava:
  # java include path
  java_include=java_home+'/include'
  pdl.Append(CPPPATH=[java_include])
  
  # check for 'jni_md.h'
  for subdirInclude in os.listdir(java_include):
    mdfile=subdirInclude+'/jni_md.h'
    if os.path.isfile(java_include+'/'+mdfile):
      if conf.CheckCHeader(mdfile):
        mdpath=java_include+'/'+subdirInclude
        pdl.Append(CPPPATH=[mdpath])
        break

  # if 'jni_md.h' is not on the path, this will fail
  if not conf.CheckCHeader('jni.h'):
    print 'jni.h not found!'
    Exit(1)


pdl = conf.Finish()

idir_prefix = '$PREFIX'
idir_lib    = '$PREFIX/lib'
idir_inc    = '$PREFIX/include/pdl'

lib = pdl.SharedLibrary('pdl', srcfiles)

pdl.Install(idir_lib, lib)
pdl.Install(idir_inc, incfiles)
pdl.Alias('install', idir_prefix)
