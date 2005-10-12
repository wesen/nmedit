package nomad.gui;

import javax.swing.JDesktopPane;

public class NomadModuleSection extends JDesktopPane
{
    public final static class ModulesSectionType {
        public final static int COMMON = 0;
        public final static int POLY = 1;
        public final static int MORPH = 2;
    }
    private int moduleSection = ModulesSectionType.POLY;
    
    public NomadModuleSection(int moduleSection) {
        super();
        this.moduleSection = moduleSection;
    }
}
