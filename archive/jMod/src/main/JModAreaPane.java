package main;

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JDesktopPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JPopupMenu.Separator;

public class JModAreaPane extends JDesktopPane implements MouseListener, DropTargetListener
{
    private boolean poly = true;
    private PatchData patchData = null;
    private JModAreaPane _this = null;
    
    private int clickX = 0, clickY = 0;

    int oldCableDragX = 0;
    int oldCableDragY = 0;

    private DropTarget dropTarget = null;
    private int dropAction = DnDConstants.ACTION_COPY_OR_MOVE;

    JPopupMenu menu = new JPopupMenu();

    JModAreaPane(boolean newPoly, PatchData patchData) {
        super();

        _this = this;
        this.patchData = patchData;

        dropTarget = new DropTarget(this, dropAction, this, true);

        JMenu i_oMenu = new JMenu("Ins/Outs");
        JMenu oscMenu = new JMenu("Oscillators");
        JMenu lfoMenu = new JMenu("LFO's");
        JMenu envMenu = new JMenu("Envelopes");
        JMenu fltMenu = new JMenu("Filters");
        JMenu mixMenu = new JMenu("Mixers");
        JMenu audMenu = new JMenu("Audio");
        JMenu ctlMenu = new JMenu("Controls");
        JMenu logMenu = new JMenu("Logical");
        JMenu seqMenu = new JMenu("Sequencers");

        JMenuItem mod001 = new JMenuItem(ModuleData.getTypeNLC(1));
        JMenuItem mod002 = new JMenuItem(ModuleData.getTypeNLC(2));
        JMenuItem mod003 = new JMenuItem(ModuleData.getTypeNLC(3));
        JMenuItem mod004 = new JMenuItem(ModuleData.getTypeNLC(4));
        JMenuItem mod005 = new JMenuItem(ModuleData.getTypeNLC(5));
        JMenuItem mod007 = new JMenuItem(ModuleData.getTypeNLC(7));
        JMenuItem mod008 = new JMenuItem(ModuleData.getTypeNLC(8));
        JMenuItem mod009 = new JMenuItem(ModuleData.getTypeNLC(9));
        JMenuItem mod010 = new JMenuItem(ModuleData.getTypeNLC(10));
        JMenuItem mod011 = new JMenuItem(ModuleData.getTypeNLC(11));
        JMenuItem mod012 = new JMenuItem(ModuleData.getTypeNLC(12));
        JMenuItem mod013 = new JMenuItem(ModuleData.getTypeNLC(13));
        JMenuItem mod014 = new JMenuItem(ModuleData.getTypeNLC(14));
        JMenuItem mod015 = new JMenuItem(ModuleData.getTypeNLC(15));
        JMenuItem mod016 = new JMenuItem(ModuleData.getTypeNLC(16));
        JMenuItem mod017 = new JMenuItem(ModuleData.getTypeNLC(17));
        JMenuItem mod018 = new JMenuItem(ModuleData.getTypeNLC(18));
        JMenuItem mod019 = new JMenuItem(ModuleData.getTypeNLC(19));
        JMenuItem mod020 = new JMenuItem(ModuleData.getTypeNLC(20));
        JMenuItem mod021 = new JMenuItem(ModuleData.getTypeNLC(21));
        JMenuItem mod022 = new JMenuItem(ModuleData.getTypeNLC(22));
        JMenuItem mod023 = new JMenuItem(ModuleData.getTypeNLC(23));
        JMenuItem mod024 = new JMenuItem(ModuleData.getTypeNLC(24));
        JMenuItem mod025 = new JMenuItem(ModuleData.getTypeNLC(25));
        JMenuItem mod026 = new JMenuItem(ModuleData.getTypeNLC(26));
        JMenuItem mod027 = new JMenuItem(ModuleData.getTypeNLC(27));
        JMenuItem mod028 = new JMenuItem(ModuleData.getTypeNLC(28));
        JMenuItem mod029 = new JMenuItem(ModuleData.getTypeNLC(29));
        JMenuItem mod030 = new JMenuItem(ModuleData.getTypeNLC(30));
        JMenuItem mod031 = new JMenuItem(ModuleData.getTypeNLC(31));
        JMenuItem mod032 = new JMenuItem(ModuleData.getTypeNLC(32));
        JMenuItem mod033 = new JMenuItem(ModuleData.getTypeNLC(33));
        JMenuItem mod034 = new JMenuItem(ModuleData.getTypeNLC(34));
        JMenuItem mod035 = new JMenuItem(ModuleData.getTypeNLC(35));
        JMenuItem mod036 = new JMenuItem(ModuleData.getTypeNLC(36));
        JMenuItem mod037 = new JMenuItem(ModuleData.getTypeNLC(37));
        JMenuItem mod038 = new JMenuItem(ModuleData.getTypeNLC(38));
        JMenuItem mod039 = new JMenuItem(ModuleData.getTypeNLC(39));
        JMenuItem mod040 = new JMenuItem(ModuleData.getTypeNLC(40));
        JMenuItem mod043 = new JMenuItem(ModuleData.getTypeNLC(43));
        JMenuItem mod044 = new JMenuItem(ModuleData.getTypeNLC(44));
        JMenuItem mod045 = new JMenuItem(ModuleData.getTypeNLC(45));
        JMenuItem mod046 = new JMenuItem(ModuleData.getTypeNLC(46));
        JMenuItem mod047 = new JMenuItem(ModuleData.getTypeNLC(47));
        JMenuItem mod048 = new JMenuItem(ModuleData.getTypeNLC(48));
        JMenuItem mod049 = new JMenuItem(ModuleData.getTypeNLC(49));
        JMenuItem mod050 = new JMenuItem(ModuleData.getTypeNLC(50));
        JMenuItem mod051 = new JMenuItem(ModuleData.getTypeNLC(51));
        JMenuItem mod052 = new JMenuItem(ModuleData.getTypeNLC(52));
        JMenuItem mod053 = new JMenuItem(ModuleData.getTypeNLC(53));
        JMenuItem mod054 = new JMenuItem(ModuleData.getTypeNLC(54));
        JMenuItem mod057 = new JMenuItem(ModuleData.getTypeNLC(57));
        JMenuItem mod058 = new JMenuItem(ModuleData.getTypeNLC(58));
        JMenuItem mod059 = new JMenuItem(ModuleData.getTypeNLC(59));
        JMenuItem mod061 = new JMenuItem(ModuleData.getTypeNLC(61));
        JMenuItem mod062 = new JMenuItem(ModuleData.getTypeNLC(62));
        JMenuItem mod063 = new JMenuItem(ModuleData.getTypeNLC(63));
        JMenuItem mod064 = new JMenuItem(ModuleData.getTypeNLC(64));
        JMenuItem mod065 = new JMenuItem(ModuleData.getTypeNLC(65));
        JMenuItem mod066 = new JMenuItem(ModuleData.getTypeNLC(66));
        JMenuItem mod067 = new JMenuItem(ModuleData.getTypeNLC(67));
        JMenuItem mod068 = new JMenuItem(ModuleData.getTypeNLC(68));
        JMenuItem mod069 = new JMenuItem(ModuleData.getTypeNLC(69));
        JMenuItem mod070 = new JMenuItem(ModuleData.getTypeNLC(70));
        JMenuItem mod071 = new JMenuItem(ModuleData.getTypeNLC(71));
        JMenuItem mod072 = new JMenuItem(ModuleData.getTypeNLC(72));
        JMenuItem mod073 = new JMenuItem(ModuleData.getTypeNLC(73));
        JMenuItem mod074 = new JMenuItem(ModuleData.getTypeNLC(74));
        JMenuItem mod075 = new JMenuItem(ModuleData.getTypeNLC(75));
        JMenuItem mod076 = new JMenuItem(ModuleData.getTypeNLC(76));
        JMenuItem mod077 = new JMenuItem(ModuleData.getTypeNLC(77));
        JMenuItem mod078 = new JMenuItem(ModuleData.getTypeNLC(78));
        JMenuItem mod079 = new JMenuItem(ModuleData.getTypeNLC(79));
        JMenuItem mod080 = new JMenuItem(ModuleData.getTypeNLC(80));
        JMenuItem mod081 = new JMenuItem(ModuleData.getTypeNLC(81));
        JMenuItem mod082 = new JMenuItem(ModuleData.getTypeNLC(82));
        JMenuItem mod083 = new JMenuItem(ModuleData.getTypeNLC(83));
        JMenuItem mod084 = new JMenuItem(ModuleData.getTypeNLC(84));
        JMenuItem mod085 = new JMenuItem(ModuleData.getTypeNLC(85));
        JMenuItem mod086 = new JMenuItem(ModuleData.getTypeNLC(86));
        JMenuItem mod087 = new JMenuItem(ModuleData.getTypeNLC(87));
        JMenuItem mod088 = new JMenuItem(ModuleData.getTypeNLC(88));
        JMenuItem mod089 = new JMenuItem(ModuleData.getTypeNLC(89));
        JMenuItem mod090 = new JMenuItem(ModuleData.getTypeNLC(90));
        JMenuItem mod091 = new JMenuItem(ModuleData.getTypeNLC(91));
        JMenuItem mod092 = new JMenuItem(ModuleData.getTypeNLC(92));
        JMenuItem mod094 = new JMenuItem(ModuleData.getTypeNLC(94));
        JMenuItem mod095 = new JMenuItem(ModuleData.getTypeNLC(95));
        JMenuItem mod096 = new JMenuItem(ModuleData.getTypeNLC(96));
        JMenuItem mod097 = new JMenuItem(ModuleData.getTypeNLC(97));
        JMenuItem mod098 = new JMenuItem(ModuleData.getTypeNLC(98));
        JMenuItem mod099 = new JMenuItem(ModuleData.getTypeNLC(99));
        JMenuItem mod100 = new JMenuItem(ModuleData.getTypeNLC(100));
        JMenuItem mod102 = new JMenuItem(ModuleData.getTypeNLC(102));
        JMenuItem mod103 = new JMenuItem(ModuleData.getTypeNLC(103));
        JMenuItem mod104 = new JMenuItem(ModuleData.getTypeNLC(104));
        JMenuItem mod105 = new JMenuItem(ModuleData.getTypeNLC(105));
        JMenuItem mod106 = new JMenuItem(ModuleData.getTypeNLC(106));
        JMenuItem mod107 = new JMenuItem(ModuleData.getTypeNLC(107));
        JMenuItem mod108 = new JMenuItem(ModuleData.getTypeNLC(108));
        JMenuItem mod110 = new JMenuItem(ModuleData.getTypeNLC(110));
        JMenuItem mod111 = new JMenuItem(ModuleData.getTypeNLC(111));
        JMenuItem mod112 = new JMenuItem(ModuleData.getTypeNLC(112));
        JMenuItem mod113 = new JMenuItem(ModuleData.getTypeNLC(113));
        JMenuItem mod114 = new JMenuItem(ModuleData.getTypeNLC(114));
        JMenuItem mod115 = new JMenuItem(ModuleData.getTypeNLC(115));
        JMenuItem mod117 = new JMenuItem(ModuleData.getTypeNLC(117));
        JMenuItem mod118 = new JMenuItem(ModuleData.getTypeNLC(118));
        JMenuItem mod127 = new JMenuItem(ModuleData.getTypeNLC(127));
        
        poly = newPoly;
        
        addMouseListener(this);

        menu.add(i_oMenu);
        menu.add(oscMenu);
        menu.add(lfoMenu);
        menu.add(envMenu);
        menu.add(fltMenu);
        menu.add(mixMenu);
        menu.add(audMenu);
        menu.add(ctlMenu);
        menu.add(logMenu);
        menu.add(seqMenu);

        mod001.addActionListener(new AddModule(1));
        mod002.addActionListener(new AddModule(2));
        mod003.addActionListener(new AddModule(3));
        mod004.addActionListener(new AddModule(4));
        mod005.addActionListener(new AddModule(5));
        mod007.addActionListener(new AddModule(7));
        mod008.addActionListener(new AddModule(8));
        mod009.addActionListener(new AddModule(9));
        mod010.addActionListener(new AddModule(10));
        mod011.addActionListener(new AddModule(11));
        mod012.addActionListener(new AddModule(12));
        mod013.addActionListener(new AddModule(13));
        mod014.addActionListener(new AddModule(14));
        mod015.addActionListener(new AddModule(15));
        mod016.addActionListener(new AddModule(16));
        mod017.addActionListener(new AddModule(17));
        mod018.addActionListener(new AddModule(18));
        mod019.addActionListener(new AddModule(19));
        mod020.addActionListener(new AddModule(20));
        mod021.addActionListener(new AddModule(21));
        mod022.addActionListener(new AddModule(22));
        mod023.addActionListener(new AddModule(23));
        mod024.addActionListener(new AddModule(24));
        mod025.addActionListener(new AddModule(25));
        mod026.addActionListener(new AddModule(26));
        mod027.addActionListener(new AddModule(27));
        mod028.addActionListener(new AddModule(28));
        mod029.addActionListener(new AddModule(29));
        mod030.addActionListener(new AddModule(30));
        mod031.addActionListener(new AddModule(31));
        mod032.addActionListener(new AddModule(32));
        mod033.addActionListener(new AddModule(33));
        mod034.addActionListener(new AddModule(34));
        mod035.addActionListener(new AddModule(35));
        mod036.addActionListener(new AddModule(36));
        mod037.addActionListener(new AddModule(37));
        mod038.addActionListener(new AddModule(38));
        mod039.addActionListener(new AddModule(39));
        mod040.addActionListener(new AddModule(40));
        mod043.addActionListener(new AddModule(43));
        mod044.addActionListener(new AddModule(44));
        mod045.addActionListener(new AddModule(45));
        mod046.addActionListener(new AddModule(46));
        mod047.addActionListener(new AddModule(47));
        mod048.addActionListener(new AddModule(48));
        mod049.addActionListener(new AddModule(49));
        mod050.addActionListener(new AddModule(50));
        mod051.addActionListener(new AddModule(51));
        mod052.addActionListener(new AddModule(52));
        mod053.addActionListener(new AddModule(53));
        mod054.addActionListener(new AddModule(54));
        mod057.addActionListener(new AddModule(57));
        mod058.addActionListener(new AddModule(58));
        mod059.addActionListener(new AddModule(59));
        mod061.addActionListener(new AddModule(61));
        mod062.addActionListener(new AddModule(62));
        mod063.addActionListener(new AddModule(63));
        mod064.addActionListener(new AddModule(64));
        mod065.addActionListener(new AddModule(65));
        mod066.addActionListener(new AddModule(66));
        mod067.addActionListener(new AddModule(67));
        mod068.addActionListener(new AddModule(68));
        mod069.addActionListener(new AddModule(69));
        mod070.addActionListener(new AddModule(70));
        mod071.addActionListener(new AddModule(71));
        mod072.addActionListener(new AddModule(72));
        mod073.addActionListener(new AddModule(73));
        mod074.addActionListener(new AddModule(74));
        mod075.addActionListener(new AddModule(75));
        mod076.addActionListener(new AddModule(76));
        mod077.addActionListener(new AddModule(77));
        mod078.addActionListener(new AddModule(78));
        mod079.addActionListener(new AddModule(79));
        mod080.addActionListener(new AddModule(80));
        mod081.addActionListener(new AddModule(81));
        mod082.addActionListener(new AddModule(82));
        mod083.addActionListener(new AddModule(83));
        mod084.addActionListener(new AddModule(84));
        mod085.addActionListener(new AddModule(85));
        mod086.addActionListener(new AddModule(86));
        mod087.addActionListener(new AddModule(87));
        mod088.addActionListener(new AddModule(88));
        mod089.addActionListener(new AddModule(89));
        mod090.addActionListener(new AddModule(90));
        mod091.addActionListener(new AddModule(91));
        mod092.addActionListener(new AddModule(92));
        mod094.addActionListener(new AddModule(94));
        mod095.addActionListener(new AddModule(95));
        mod096.addActionListener(new AddModule(96));
        mod097.addActionListener(new AddModule(97));
        mod098.addActionListener(new AddModule(98));
        mod099.addActionListener(new AddModule(99));
        mod100.addActionListener(new AddModule(100));
        mod102.addActionListener(new AddModule(102));
        mod103.addActionListener(new AddModule(103));
        mod104.addActionListener(new AddModule(104));
        mod105.addActionListener(new AddModule(105));
        mod106.addActionListener(new AddModule(106));
        mod107.addActionListener(new AddModule(107));
        mod108.addActionListener(new AddModule(108));
        mod110.addActionListener(new AddModule(110));
        mod111.addActionListener(new AddModule(111));
        mod112.addActionListener(new AddModule(112));
        mod113.addActionListener(new AddModule(113));
        mod114.addActionListener(new AddModule(114));
        mod115.addActionListener(new AddModule(115));
        mod117.addActionListener(new AddModule(117));
        mod118.addActionListener(new AddModule(118));
        mod127.addActionListener(new AddModule(127));
        
        i_oMenu.add(mod001);
        i_oMenu.add(mod063);
        i_oMenu.add(mod065);
        i_oMenu.add(new Separator());
        i_oMenu.add(mod002);
        i_oMenu.add(mod127);
        i_oMenu.add(new Separator());
        i_oMenu.add(mod005);
        i_oMenu.add(mod004);
        i_oMenu.add(mod003);
        i_oMenu.add(new Separator());
        i_oMenu.add(mod067);
        i_oMenu.add(mod100);
        
        oscMenu.add(mod097);
        oscMenu.add(mod007);
        oscMenu.add(mod008);
        oscMenu.add(mod009);
        oscMenu.add(mod107);
        oscMenu.add(mod096);
        oscMenu.add(new Separator());
        oscMenu.add(mod014);
        oscMenu.add(mod010);
        oscMenu.add(mod011);
        oscMenu.add(mod012);
        oscMenu.add(mod013);
        oscMenu.add(mod106);
        oscMenu.add(mod085);
        oscMenu.add(new Separator());
        oscMenu.add(mod031);
        oscMenu.add(new Separator());
        oscMenu.add(mod095);
        oscMenu.add(mod058);
        
        lfoMenu.add(mod024);
        lfoMenu.add(mod025);
        lfoMenu.add(mod026);
        lfoMenu.add(new Separator());
        lfoMenu.add(mod080);
        lfoMenu.add(mod027);
        lfoMenu.add(mod028);
        lfoMenu.add(mod029);
        lfoMenu.add(mod030);
        lfoMenu.add(new Separator());
        lfoMenu.add(mod068);
        lfoMenu.add(new Separator());
        lfoMenu.add(mod033);
        lfoMenu.add(mod034);
        lfoMenu.add(mod110);
        lfoMenu.add(mod035);
        lfoMenu.add(new Separator());
        lfoMenu.add(mod099);
        
        envMenu.add(mod020);
        envMenu.add(mod084);
        envMenu.add(mod023);
        envMenu.add(mod046);
        envMenu.add(mod052);
        envMenu.add(new Separator());
        envMenu.add(mod071);

        fltMenu.add(mod086);
        fltMenu.add(mod087);
        fltMenu.add(mod050);
        fltMenu.add(new Separator());
        fltMenu.add(mod049);
        fltMenu.add(mod051);
        fltMenu.add(mod092);
        fltMenu.add(new Separator());
        fltMenu.add(mod045);
        fltMenu.add(mod108);
        fltMenu.add(mod032);
        fltMenu.add(new Separator());
        fltMenu.add(mod103);
        fltMenu.add(mod104);
        
        mixMenu.add(mod019);
        mixMenu.add(mod040);
        mixMenu.add(new Separator());
        mixMenu.add(mod044);
        mixMenu.add(new Separator());
        mixMenu.add(mod018);
        mixMenu.add(mod047);
        mixMenu.add(new Separator());
        mixMenu.add(mod113);
        mixMenu.add(mod114);
        mixMenu.add(mod111);
        mixMenu.add(mod112);
        mixMenu.add(new Separator());
        mixMenu.add(mod076);
        mixMenu.add(new Separator());
        mixMenu.add(mod079);
        mixMenu.add(mod088);
        mixMenu.add(new Separator());
        mixMenu.add(mod081);
        
        audMenu.add(mod061);
        audMenu.add(mod062);
        audMenu.add(mod074);
        audMenu.add(new Separator());
        audMenu.add(mod054);
        audMenu.add(mod078);
        audMenu.add(mod053);
        audMenu.add(mod082);
        audMenu.add(mod094);
        audMenu.add(mod102);
        audMenu.add(new Separator());
        audMenu.add(mod057);
        audMenu.add(new Separator());
        audMenu.add(mod083);
        audMenu.add(new Separator());
        audMenu.add(mod021);
        audMenu.add(mod105);
        audMenu.add(mod117);
        audMenu.add(mod118);
        
        ctlMenu.add(mod043);
        ctlMenu.add(new Separator());
        ctlMenu.add(mod039);
        ctlMenu.add(mod048);
        ctlMenu.add(mod016);
        ctlMenu.add(new Separator());
        ctlMenu.add(mod072);
        ctlMenu.add(mod075);
        ctlMenu.add(mod098);
        ctlMenu.add(mod022);
        ctlMenu.add(new Separator());
        ctlMenu.add(mod066);
        ctlMenu.add(mod115);
        
        logMenu.add(mod036);
        logMenu.add(mod064);
        logMenu.add(mod038);
        logMenu.add(mod037);
        logMenu.add(new Separator());
        logMenu.add(mod070);
        logMenu.add(new Separator());
        logMenu.add(mod073);
        logMenu.add(new Separator());
        logMenu.add(mod059);
        logMenu.add(mod089);
        logMenu.add(new Separator());
        logMenu.add(mod069);
        logMenu.add(mod077);
        
        seqMenu.add(mod017);
        seqMenu.add(new Separator());
        seqMenu.add(mod091);
        seqMenu.add(new Separator());
        seqMenu.add(mod015);
        seqMenu.add(mod090);
}

    class AddModule implements ActionListener {
        int modIndex = 0;
        
        AddModule(int newModIndex){
            modIndex = newModIndex;
        }
        
        public void actionPerformed(ActionEvent e) {
            Module mod = patchData.getModules().addModule(poly, "" + ((poly?patchData.getModules().getPolySize():patchData.getModules().getCommonSize()) + 1) + " " + modIndex + " 0 0", _this);
            mod.getModuleData().setModuleName(ModuleData.getTypeName(mod.getModuleData().getModType()));
            
            mod.setPixLocation(mod, ((clickX / ModuleData.pixWidth) * ModuleData.pixWidth), ((clickY / ModuleData.pixHeight) * ModuleData.pixHeight));
            mod.drawModule();
            patchData.getModules().rearangeModules(_this, mod, poly);
        }
    }
  
    public void mouseClicked(MouseEvent e)
    {
//        if (e.isPopupTrigger()) {
//            menu.show(e.getComponent(), e.getX(), e.getY());
//        }
    }

    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            clickX = e.getX();
            clickY = e.getY();
            menu.show(e.getComponent(), clickX, clickY);
        }
    }

    public void mousePressed(MouseEvent arg0) {}
    public void mouseEntered(MouseEvent arg0) {}
    public void mouseExited(MouseEvent arg0) {}
    
    public void dragOver(DropTargetDragEvent e)
    {
      int newDragX = e.getLocation().x - Cable.cableWindowOffset;
      int newDragY = e.getLocation().y - Cable.cableWindowOffset;

      if ((newDragX != oldCableDragX) || (newDragY != oldCableDragX)) {
        Cables.getDragCable().setNewDragWindowLayout((newDragX), (newDragY));
        Cables.getDragCable().repaint();
        oldCableDragX = newDragX;
        oldCableDragY = newDragY;
      }
      e.rejectDrag();
      
//      Debug.println("Module dragOver");
    }

    public void dragEnter(DropTargetDragEvent arg0) {}
    public void dropActionChanged(DropTargetDragEvent arg0) {}
    public void dragExit(DropTargetEvent arg0) {}
    public void drop(DropTargetDropEvent arg0) {}

}
