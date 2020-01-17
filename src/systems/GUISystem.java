package systems;

import java.util.HashMap;

import bus.Message;
import bus.MessageBus;
import bus.Recipients;
import components.Window;
import logic.EntityDatabase;
import logic.EntityDatabase.ComponentType;
import tags.Component;
import ui.UIManager;
import utility.Logger;

public class GUISystem extends AbstractSystem {
	
	private final UIManager uiManager;
	
	private final HashMap<Integer, Window> windows = new HashMap<>();
	
	public GUISystem(EntityDatabase entityDB, UIManager uiManager) {
		super(entityDB);
		
		this.uiManager = uiManager;
		
		// Initialize GUI Windows
		final Window characterInfo = new Window();
		characterInfo.setPosition(-0.7f, 0.7f);
		characterInfo.setSize(0.3f, 1.0f);
		characterInfo.setBorderWidth(0.03f);
		uiManager.addComponent(characterInfo);
		windows.put(TOGGLE_CHARACTER_INFO, characterInfo);
		
		final Window ch = new Window();
		ch.setPosition(0.1f, 0.6f);
		ch.setSize(0.1f, 0.2f);
		ch.setBorderWidth(0.01f);
		ch.setLayer((byte) -1);
		characterInfo.add(ch);
		
		final Window inventory = new Window();
		inventory.setPosition(0.5f, -0.5f);
		inventory.setSize(0.3f, 0.3f);
		inventory.setBorderWidth(0.03f);
		uiManager.addComponent(inventory);
		windows.put(TOGGLE_INVENTORY, inventory);
	}

	@Override
	protected void initializeComponent(ComponentType type, Object... args) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onComponentDelete(ComponentType type, Component component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		uiManager.render();
	}
	
	public static final int TOGGLE_CHARACTER_INFO = 0;
	public static final int TOGGLE_INVENTORY = 1;
	@Override
	public void processMessages() {
		Message message = null;
		while((message = MessageBus.getInstance().getNextMessage(Recipients.GUI_SYSTEM)) != null) {
			final Object[] args = message.getArgs();
			switch(message.getBehaviorID()) {
			case TOGGLE_CHARACTER_INFO: 
				if(windows.get(TOGGLE_CHARACTER_INFO).isVisible()) {
					windows.get(TOGGLE_CHARACTER_INFO).setVisible(false);
					Logger.INFO.log("Hide Character Info");
				} else {
					windows.get(TOGGLE_CHARACTER_INFO).setVisible(true);
					Logger.INFO.log("Show Character Info");
				}
				break;
			case TOGGLE_INVENTORY:
				if(windows.get(TOGGLE_INVENTORY).isVisible()) {
					windows.get(TOGGLE_INVENTORY).setVisible(false);
				} else {
					windows.get(TOGGLE_INVENTORY).setVisible(true);
				}
				break;
			default: Logger.ERROR.log("GUI System doesn't recognize this behavior ID: " + message.getBehaviorID()); 
			}
		}
	}
}
