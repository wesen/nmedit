package nomad.application.ui;

import javax.swing.JButton;
import javax.swing.ImageIcon;

import nomad.model.descriptive.DModule;
import nomad.util.MathRound;

/**
 * @author Christian Schneider
 */
class ModuleToolbarButton extends JButton
{
  private DModule module;

  public ModuleToolbarButton(DModule module)
  {
    super(new ImageIcon(module.getIcon()));
    this.module = module;
    this.setToolTipText(module.getName()+" ("+MathRound.round(module.getCycles(),-2)+"%)");
  }

  public DModule getModuleDescription()
  { return module; }

}
