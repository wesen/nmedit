import nmcom.swig.nmpatch.*;
import nmcom.swig.nmprotocol.*;
import nmcom.swig.pdl.*;
import nmcom.swig.ppf.*;

public class Jnmedit implements ShutDownEventListener
{
  // state for endless loop; Setting state_run will stop the loop
  boolean state_run=true;

  // MidiDriver
  private MidiDriver mdriver=null;

  // sleep
  public static void sleep(int ms)
  {
    long start=System.currentTimeMillis();
    while (System.currentTimeMillis()-start<ms) ;
  }

  /* loads library 'libname' and catches 'UnsatisfiedLinkError'
   * Returns true if library was successfully loaded. Otherwise
   * it returns false.
   */
  public static boolean loadLib(String libname)
  {
    try
    {
      System.loadLibrary(libname);
    }
    catch(UnsatisfiedLinkError e)
    {
      System.out.println("'"+libname+"' [failed]");
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public static void main(String [] args)
  {
    System.out.println("*** nmEdit java example ***");

    // handle arguments
    if (args.length!=3)
    {
      System.out.println("Wrong number of arguments.");
      System.out.println();
      System.out.println("Three parameters are required.");
      System.out.println("1. Midi Driver        (i.e. ALSA or NETMIDI)");
      System.out.println("2. Midi Input Device  (i.e. /dev/snd/midiC1D0)");
      System.out.println("3. Midi Output Device (i.e. /dev/snd/midiC1D0)");
      System.out.println();
      System.out.println("example call:");
      System.out.println("java Jnmedit ALSA /dev/snd/midiC1D0 /dev/snd/midiC1D0");
      return;
    }

    // Load the libraries
    System.out.print("-loading libraries...");
    if (!loadLib("nmpatch")) System.exit(1);
    if (!loadLib("pdl")) System.exit(1);
    if (!loadLib("ppf")) System.exit(1);
    if (!loadLib("nmprotocol")) System.exit(1);
    System.out.println("[done]"); // loading libraries

    // Create application and run
    Jnmedit app = new Jnmedit();
    app.run(args[0], args[1], args[2]);
  }

  protected void run(String driverName, String midiIn, String midiOut)
  {
    System.out.println("-using driver: '"+driverName+"'");
    System.out.println("-using input : '"+midiIn+"'");
    System.out.println("-using output: '"+midiOut+"'");
    System.out.println();

    try // initializing
    {
      MidiMessage.usePDLFile("/usr/local/lib/nmprotocol/midi.pdl", null);
      PatchMessage.usePDLFile("/usr/local/lib/nmprotocol/patch.pdl", null);
      ModuleSection.usePPFFile("/usr/local/lib/nmpatch/module.ppf");
    } catch (PDLException e)
    {
      e.printStackTrace();
      return;
    }

    try // create mididriver and connect to midi ports
    {
      mdriver = MidiDriver.createDriver(driverName);
      mdriver.connect(midiIn, midiOut);
    }
    catch(MidiException e)
    {
      System.out.println(e+"("+e.getError()+")");
      return;
    }
    catch(Exception e)
    {
      e.printStackTrace();
      return;
    }

    // Add shutdown hook to catch STRG+C keystroke
    Runtime.getRuntime()
      .addShutdownHook(new ShutDownThread(this));

    // create nmProtocol class
    NMProtocol nmProtocol=null;
    try
    {
      nmProtocol = new NMProtocol(mdriver);
    }
    catch(MidiException e)
    {
      System.out.println(e);
      return;
    }

    // create synth class and add listener
    Synth synth = new Synth(nmProtocol);
    synth.addListener(new DebugSynthListener());

    // forever loop
    while(state_run)
    {
      try // send heartbeat() message
      {
        nmProtocol.heartbeat();
      }
      catch(MidiException e)
      {
        System.out.println(e+"("+e.getError()+")");
      }
      catch(PDLException e)
      {
        System.out.println(e);
      }
      catch(Exception e)
      { // Unknown exception, better
        System.out.println(e);
        break; // exit loop
      }
      Jnmedit.sleep(600); // wait
    }
    stop();
  }

  public void stop()
  {
    state_run=false;
    if (mdriver!=null) // disconnect midiport
      mdriver.disconnect();
    System.out.println("exit.");
  }

  public void eventOnShutDown()
  {
    System.out.print("STRG+C received, shutting down...");
    stop();
  }
}

// Interface for receiving events from class ShutDownThread
interface ShutDownEventListener
{
  public void eventOnShutDown();
}

// Thread for addShutDownHook
class ShutDownThread extends Thread
{
  ShutDownEventListener listener;

  public ShutDownThread(ShutDownEventListener listener)
  { this.listener=listener; }

  public void run()
  { listener.eventOnShutDown(); }
}

// Custom SynthListener
class DebugSynthListener extends SynthListener
{
  public void newPatchInSlot(int slot, Patch patch)
  {
    System.out.println("newPatchInSlot()::slot,name="+slot+",'"+patch.getName()+"'");
    if (slot==0) // only listen to slot 0
      new MyModuleSectionListener(patch.getModuleSection(ModuleSection.Type.POLY));
  }
  public void slotStateChanged(int slot, boolean active, boolean selected,int voices)
  { System.out.println("Slot "+slot+": "+active+" "+selected+" "+voices); }

  public void patchListChanged()
  { ; }
}

// Custom ModuleSectionListener
class MyModuleSectionListener extends ModuleSectionListener
{
  ModuleSection section=null;

  public MyModuleSectionListener(ModuleSection section)
  {
    this.section=section;
    this.section.addListener(this);
  }

  public void newModule(Module module, int index)
  {
      module.addListener(new MyModuleListener(module));
      ModuleType type = module.getType();
      System.out.println(
        "Module '"+type.getName()+"':: "
       +"x,y="+module.getXPosition()+","+module.getYPosition()+";");
      for (int j=0;j<type.numberOfParameters();j++)
      {
        System.out.println(" - Parameter '"+type.getParameterName(j)+"':: "
          +"min,max="+type.getParameterMin(j)+","+type.getParameterMax(j)+";"
          +"value="+module.getParameter(j)+";");
      }
  }
}

class MyModuleListener extends ModuleListener
{
  Module module;

  public MyModuleListener(Module module)
  {
    this.module=module;
  }

  public void parameterChanged(int param, int value)
  {
    ModuleType type = module.getType();
    System.out.println("parameterChanged() in '"+type.getName()
     +"' parameter '"+type.getParameterName(param)+"' value="+value);
  }

  public void positionChanged(int arg0, int arg1)
  { ; }

}

