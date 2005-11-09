package nomad.gui.model;

import java.io.FileNotFoundException;
import java.util.HashMap;

import nomad.application.Run;
import nomad.gui.AbstractModuleGUI;
import nomad.gui.ModuleGUI;
import nomad.gui.ModuleSectionGUI;
import nomad.gui.model.component.AbstractConnectorUI;
import nomad.gui.model.component.AbstractUIComponent;
import nomad.gui.model.component.AbstractUIControl;
import nomad.gui.model.property.ConnectorProperty;
import nomad.gui.model.property.ParamPortProperty;
import nomad.gui.model.property.Property;
import nomad.model.descriptive.DConnector;
import nomad.model.descriptive.DModule;
import nomad.model.descriptive.DParameter;
import nomad.model.descriptive.ModuleDescriptions;
import nomad.patch.Connector;
import nomad.patch.Module;
import nomad.plugin.cache.ModulePropertyCallback;
import nomad.plugin.cache.UICache;
import nomad.plugin.cache.UICacheException;
import nomad.xml.XMLAttributeReader;
import nomad.xml.XMLAttributeValidationException;
import nomad.xml.XMLReader;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Ian Hoogeboom
 *
 * To create a module's GUI.
 * This will read the build-up xml of modules and returns a modlePanel with it's 'knobs and sliders'.
 *  
 * This is to seperate the creation of the GUI from it's 'data' (Module)
 * It returns an NomadModule with a different look for each module type.
 * 
 */
public class ModuleGUIBuilder {
	
	public static ModuleGUIBuilder instance = null;
	
	public static void createGUIBuilder(UIFactory factory) {
		instance = new ModuleGUIBuilder(factory, factory.getUIDescriptionFileName());
	}
	
	public static ModuleGUI createGUI(Module module, ModuleSectionGUI moduleSectionGUI) {
		return (instance.cache!=null)
			?	instance.buildModuleWithCache(module, moduleSectionGUI, module.getDModule())
			:	instance.buildModule(module, moduleSectionGUI, module.getDModule());
	}

	public static void createGUIComponents(AbstractModuleGUI target, Module module, DModule info) {
		if (instance.cache!=null) { 			
			if (instance.cache.loadModule(info.getModuleID(), instance.getBuilder(module, target, info))) ;
		} else {
		
			Node moduleNode = (Node) instance.xmlModuleNodes.get(new Integer(info.getModuleID()));
			if (moduleNode!=null) 
				instance.buildModulePanel(target, module, info, moduleNode);
			else
				instance.buildModulePanelAndGuessLook(target, module, info);
		}
	}
	
	public ModuleBuilder getBuilder(Module module, AbstractModuleGUI target, DModule info) {
		return new ModuleBuilder(module, target, info);
	}
	
	private void buildModulePanelAndGuessLook(AbstractModuleGUI target, Module module, DModule moduleInfo) {

		int pad=5;
		int line=0;
		int lineHeight=12;
				
		for (int i=0;i<moduleInfo.getParameterCount();i++) {
			DParameter info = moduleInfo.getParameter(i);
			//new ParamView(modulePane,info,pad,pad+line*lineHeight);
			
			int offset= 20*(i%4);

			AbstractUIControl control = info.getNumStates()<6 // option
				? factory.newDefaultOptionControlInstance()
			    : factory.newDefaultControlInstance();
			
			control.getControlPort(0).setParameterInfoAdapter(info);
			control.getComponent().setLocation(pad+offset,pad+line*lineHeight);
			
			//slider.setSize(100, 16);
			target.add(control);
			
			for (int j=0;j<control.getControlPortCount();j++)
				control.getControlPort(j).setParameterInfoAdapter(info);
			
			if (i%4==3)
				line++;
		}
	//	if ((moduleInfo.getParameterCount()-1)%4!=3)
	//		line++;*
		
		if (moduleInfo.getConnectorCount()>0) {
			for (int i=0;i<moduleInfo.getConnectorCount();i++) {
				AbstractConnectorUI cui = factory.newDefaultConnectorInstance();
				cui.setConnectorInfoAdapter(moduleInfo.getConnector(i));
				cui.getComponent().setLocation(pad+i*18,pad+line*lineHeight);
				target.add(cui);
				
				Connector c = module.findConnector(cui.getConnectorInfoAdapter());
				if (c!=null)
					c.setUI(cui);
			}			
			
		}
		
		target.validate();
		target.updateUI();
	}

	protected ModuleGUI createModulePaneGUI(Module module, ModuleSectionGUI moduleSectionGUI) {
		ModuleGUI modulePanel = null;
		modulePanel = factory.getModuleGUI(module, moduleSectionGUI);//new ModuleGUI(module, moduleSectionGUI);
    	modulePanel.setLocation(module.getPixLocationX(), module.getPixLocationY());
    	modulePanel.setSize(module.getPixWidth(), module.getPixHeight());
    	modulePanel.setNameLabel(module.getModuleTitle(), module.getPixWidth());
    	modulePanel.setVisible(true);
		return modulePanel;
	}
	
	/**
	 * Hashmap consits of pairs (String:module-id, org.w3c.dom.Node:ModuleTag-Node)
	 */
	private HashMap xmlModuleNodes = new HashMap();
	private UIFactory factory = null;
	private UICache cache = null;
	
	public void setUIFactory(UIFactory factory) {
		this.factory = factory;
	}
	
	private void initCache(String xmlFile) {
		Run.statusMessage("Caching...");
		try {
			cache = new UICache(xmlFile.replaceAll("xml","cache"));
		} catch (FileNotFoundException e) {
			Run.statusMessage("Caching...failed");
			cache = null;
			e.printStackTrace();
			return ;
		}
		Run.statusMessage("Caching...done");
	}
	
	public ModuleGUIBuilder(UIFactory factory, String xmlFile) {
		this.factory = factory;
		initCache(xmlFile);
		
		if (cache!=null) {
			return ; // nothing more to do
		}
		
		Document doc = XMLReader.readDocument(xmlFile, false /*validating*/);
		
		if (doc!=null){
			// read <module id="..." tags
			NodeList moduleNodeList = doc.getElementsByTagName("module");
			for (int i=0;i<moduleNodeList.getLength();i++) {
				Node moduleNode = moduleNodeList.item(i);
				if (moduleNode.getNodeType()==Node.ELEMENT_NODE) {
					int moduleId = 0;
					try {
						moduleId = (new XMLAttributeReader(moduleNode)).getIntegerAttribute("id");
					} catch (XMLAttributeValidationException e) {
						xmlModuleNodes = null;
						e.printStackTrace();
						break;
					}
					
					xmlModuleNodes.put(new Integer(moduleId), moduleNode);
				}
			}
		}
	}

	public ModuleGUI buildModule(Module module, ModuleSectionGUI moduleSectionGUI, int moduleID) {
		return buildModule(module, moduleSectionGUI, ModuleDescriptions.model.getModuleById(moduleID));
	}
	
	public ModuleGUI buildModule(Module module, ModuleSectionGUI moduleSectionGUI, DModule moduleInfo) {
		Node moduleNode = (Node) xmlModuleNodes.get(new Integer(moduleInfo.getModuleID()));
		if (moduleNode==null)
			return null;
		else
			return buildModule(module, moduleSectionGUI, moduleInfo, moduleNode);
	}
	
	protected ModuleGUI buildModule(Module module, ModuleSectionGUI moduleSectionGUI,DModule info, Node moduleNode) {
		return (ModuleGUI) buildModulePanel(createModulePaneGUI(module, moduleSectionGUI),
				module, info, moduleNode);
	}
	
	private ModuleGUI buildModuleWithCache(Module module, ModuleSectionGUI moduleSectionGUI, DModule moduleInfo) {
		ModuleGUI modulegui = createModulePaneGUI(module, moduleSectionGUI);
		
		ModuleBuilder builder = new ModuleBuilder(module, modulegui, module.getDModule());
		if (cache.loadModule(moduleInfo.getModuleID(), builder)) {
			return modulegui;
		} else
			return null;
	}
	
	private class ModuleBuilder implements ModulePropertyCallback {

		private AbstractModuleGUI modulegui = null;
		private AbstractUIComponent component = null;
		private Module module = null;
		private DModule info = null;
		
		public ModuleBuilder(Module module, AbstractModuleGUI modulegui, DModule info) {
			this.modulegui = modulegui;
			this.info = info;
			this.module = module;
		}
		
		
		public void readComponent(String className) throws UICacheException {
			// create the component
			component = instance.factory.newUIInstance(className);
			if (component==null)
				throw new UICacheException("Class not found '"+className+"'.");
			modulegui.add(component);
		}
		
		public void readComponentProperty(String propertyId, String value) throws UICacheException {
			if (component==null)
				throw new UICacheException("No component");
			
			Property property = component.getPropertyById(propertyId);
			if (property==null) {
				// TODO throw exception 
				System.err.println("** Ignored: Component '"+component+"' has no such property:'"+propertyId+"'.");
			} else if (property instanceof ParamPortProperty) {
				try {
					String paramId = value.split("\\.")[1];
					
					((ParamPortProperty)property).setParameter(
						info.getParameterById(Integer.parseInt(paramId))
					);
				} catch (Exception e) {
					System.err.println("Current property:"+propertyId+","+value);
					e.printStackTrace();
				}
			} else if (property instanceof ConnectorProperty) {
				try {
					String[] splitted = value.split("\\.");
					String connectorId = splitted[1];
					boolean isInput = /*splitted.length<=2 || */splitted[2].equals("input");
					
					DConnector cinfo = info.getConnectorById(Integer.parseInt(connectorId),isInput);
					
					((ConnectorProperty)property).setConnector(cinfo);
					
					if (component instanceof AbstractConnectorUI) {
						Connector c = module.findConnector(cinfo);
						if (c!=null)
							c.setUI((AbstractConnectorUI)component);
					}
				} catch (Exception e) {
					System.err.println("Current property:"+propertyId+","+value);
					e.printStackTrace();
				}
			} else {
				property.setValue(value);
			}
			
		}
		
	}
	
	protected AbstractModuleGUI buildModulePanel(AbstractModuleGUI modulegui, Module module, DModule info, Node moduleNode) {
		// load controls
		NodeList componentNodeList = moduleNode.getChildNodes();
		for (int i=0;i<componentNodeList.getLength();i++) {
			Node componentNode = componentNodeList.item(i);
			if (componentNode.getNodeType()==Node.ELEMENT_NODE) {
				String componentClassName = null;
				try {
					componentClassName = (new XMLAttributeReader(componentNode)).getAttribute("class");
				} catch (XMLAttributeValidationException e) {
					e.printStackTrace();
					return null;
				}
				// create the component
				AbstractUIComponent component = factory.newUIInstance(componentClassName);
				modulegui.add(component);
				
				// load properties
				NodeList propertyNodeList = componentNode.getChildNodes();
				for (int j=0;j<propertyNodeList.getLength();j++) {
					Node propertyNode = propertyNodeList.item(j);
					if (propertyNode.getNodeType()==Node.ELEMENT_NODE) {
						String propertyID = null;
						String propertyValue = null;
						try {
							XMLAttributeReader ar = new XMLAttributeReader(propertyNode);
							propertyID = ar.getAttribute("id");
							propertyValue = ar.getAttribute("value");
						} catch (XMLAttributeValidationException e) {
							e.printStackTrace();
							return null;
						}
						Property property = component.getPropertyById(propertyID);
						if (property==null) {
							// TODO throw exception 
							System.err.println("Could not find property '"+propertyID+"' ("+component+")");
							return null;
						} else if (property instanceof ParamPortProperty) {
							try {
								propertyValue = propertyValue.split("\\.")[1];
								
								((ParamPortProperty)property).setParameter(
									info.getParameterById(Integer.parseInt(propertyValue))
								);
							} catch (Exception e) {
								System.err.println("Current property:"+propertyID+","+propertyValue);
								e.printStackTrace();
							}
						} else if (property instanceof ConnectorProperty) {
							try {
								String[] splitted = propertyValue.split("\\.");
								propertyValue = splitted[1];
								boolean isInput = /*splitted.length<=2 || */splitted[2].equals("input");
								
								DConnector cinfo = info.getConnectorById(Integer.parseInt(propertyValue),isInput);
								((ConnectorProperty)property).setConnector(cinfo);
								
								if (component instanceof AbstractConnectorUI) {
									Connector c = module.findConnector(cinfo);
									if (c!=null)
										c.setUI((AbstractConnectorUI)component);
								}
								
							} catch (Exception e) {
								System.err.println("Current property:"+propertyID+","+propertyValue);
								e.printStackTrace();
							}
						} else {
							property.setValue(propertyValue);
						}
					}
				}
			}
		}
		//factory

		return modulegui;
	}
	
}
