package systems;

import bus.Message;
import bus.MessageBus;
import bus.Recipients;
import logic.EntityDatabase;
import logic.EntityDatabase.ComponentType;
import tags.Component;
import ui.UIManager;
import utility.Logger;

public class GUISystem extends AbstractSystem {
	
	private final UIManager uiManager;
	
	public GUISystem(EntityDatabase entityDB, UIManager uiManager) {
		super(entityDB);
		
		this.uiManager = uiManager;
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
		// TODO Auto-generated method stub
		
	}
	
	public static final int TOGGLE_CHARACTER_INFO = 0;
	public static final int TOGGLE_INVENTORY = 1;
	@Override
	public void processMessages() {
		Message message = null;
		while((message = MessageBus.getInstance().getNextMessage(Recipients.GUI_SYSTEM)) != null) {
			final Object[] args = message.getArgs();
			switch(message.getBehaviorID()) {
			
			default: Logger.ERROR.log("GUI System doesn't recognize this behavior ID: " + message.getBehaviorID()); 
			}
		}
	}
}
