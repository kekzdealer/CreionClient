package systems;

import java.util.HashMap;

import bus.Message;
import bus.MessageBus;
import bus.Recipients;
import components.Cursor;
import components.UIManager;
import components.Window;
import logic.EntityDatabase;
import logic.EntityDatabase.ComponentType;
import tags.Component;
import utility.Logger;

public class GUISystem extends AbstractSystem {
	
	private final UIManager uiManager;
	
	private final HashMap<Integer, components.Component> windows = new HashMap<>();
	
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
		
		final Cursor cursor = new Cursor();
		cursor.setPosition(0.0f, 0.0f);
		cursor.setSize(0.1f, 0.1f);
		cursor.setVisible(true);
		uiManager.addComponent(cursor);
		windows.put(UPDATE_CURSOR_POSITION, cursor);
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
	
	public static final int UPDATE_CURSOR_POSITION = 0;
	public static final int TOGGLE_CHARACTER_INFO = 1;
	public static final int TOGGLE_INVENTORY = 2;
	@Override
	public void processMessages() {
		Message message = null;
		while((message = MessageBus.getInstance().getNextMessage(Recipients.GUI_SYSTEM)) != null) {
			final Object[] args = message.getArgs();
			switch(message.getBehaviorID()) {
			case UPDATE_CURSOR_POSITION:
				windows.get(UPDATE_CURSOR_POSITION).setPosition(
						(float) args[0], 
						(float) args[1]);
				break;
			case TOGGLE_CHARACTER_INFO: 
				if(windows.get(TOGGLE_CHARACTER_INFO).isVisible()) {
					windows.get(TOGGLE_CHARACTER_INFO).setVisible(false);
				} else {
					windows.get(TOGGLE_CHARACTER_INFO).setVisible(true);
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
