
src = 'patchparser.yy patchlexer.ll nmlexer.cc cable.cc ctrlmap.cc knobmap.cc module.cc modulesection.cc morph.cc morphmap.cc note.cc patch.cc moduletype.cc'

inc = 'cable.h ctrlmap.h knobmap.h module.h modulesection.h morph.h morphmap.h note.h patch.h moduletype.h modulelistener.h modulesectionlistener.h'

import os

withJava = int(ARGUMENTS.get('JAVA', 0)) == 1;
java_home = os.environ.get('JAVA_HOME','')

# check is JAVA compilation is requested and JAVA_HOME is set
if not os.environ.has_key('JAVA_HOME') and (withJava == 1):
	print "compilation with JAVA requested, but JAVA_HOME not set, aborting!"
	Exit(1)

if withJava:
  print 'Compiling with java support (JAVA_HOME="%s")' % java_home
  src = 'nmpatch.i %s' % src

pkg = 'module.ppf patch.pdl'

srcfiles = [('src/' + x)     for x in Split(src)]
incfiles = [('nmpatch/' + x) for x in Split(inc)]
pkgfiles = [('src/' + x)     for x in Split(pkg)]

nmpatch = Environment(SWIGFLAGS = '-c++ -java -package nmcom.swig.nmpatch -outdir ../../java-nmcom/src/nmcom/swig/nmpatch',
                      YACCFLAGS = '-d -pnm',
                      LEXFLAGS = '-+ -olex.yy.c -Pnm',
                     LIBPATH = ['../libppf', '../libpdl','/lib', '/usr/lib', '/usr/local/lib'],
                      CPPPATH = ['.', '../libpdl', '../libppf',java_home+'/include/'])

opts = Options('nmpatch.conf')
opts.Add(PathOption('PREFIX',
                    'Install dir (Defult: /usr/local)',
                    '/usr/local'))
opts.Add(BoolOption('JAVA', 'Set to 1 to compile with java support', 0))
opts.Update(nmpatch)
opts.Save('nmpatch.conf', nmpatch)

idir_prefix = '$PREFIX'
idir_lib    = '$PREFIX/lib'
idir_pkg    = '$PREFIX/lib/nmpatch'
idir_inc    = '$PREFIX/include/nmpatch'

conf = Configure(nmpatch)

conf.env.Append(CPPFLAGS='-DLIBPATH=\'"' + idir_pkg + '"\'')

if withJava:
  # java include path
  java_include=java_home+'/include'
  nmpatch.Append(CPPPATH=[java_include])
  
  # check for 'jni_md.h'
  for subdirInclude in os.listdir(java_include):
    mdfile=subdirInclude+'/jni_md.h'
    if os.path.isfile(java_include+'/'+mdfile):
      if conf.CheckCHeader(mdfile):
        mdpath=java_include+'/'+subdirInclude
        nmpatch.Append(CPPPATH=[mdpath])
        break

  # if 'jni_md.h' is not on the path, this will fail
  if not conf.CheckCHeader('jni.h'):
    print 'jni.h not found!'
    Exit(1)

if not conf.CheckLib('ppf'):
   print "libppf missing!"
   Exit(1)

if not conf.CheckLib('pdl'):
   print "libpdl missing!"
   Exit(1)

nmpatch = conf.Finish()

lib = nmpatch.SharedLibrary('nmpatch', srcfiles)

nmpatch.Install(idir_lib, lib)
nmpatch.Install(idir_inc, incfiles)
nmpatch.Install(idir_pkg, pkgfiles)
nmpatch.Alias('install', idir_prefix)

