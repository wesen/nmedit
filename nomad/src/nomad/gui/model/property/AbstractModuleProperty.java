package nomad.gui.model.property;

import nomad.gui.model.component.AbstractUIComponent;
import nomad.model.descriptive.DModule;

/**
 * @author Christian Schneider
 */
public abstract class AbstractModuleProperty extends Property {

	public AbstractModuleProperty(String displayName, AbstractUIComponent uicomponent) {
		super(displayName, uicomponent);
	}

	public abstract DModule getModule();

}
