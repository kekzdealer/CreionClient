package systems;

import logic.EntityDatabase;
import logic.EntityDatabase.ComponentType;
import tags.Component;

public abstract class AbstractSystem {

	protected final EntityDatabase entityDB;
	
	public AbstractSystem(EntityDatabase entityDB) {
		this.entityDB = entityDB;
	}
	
	protected abstract void initializeComponent(ComponentType type, Object...args);
	protected abstract void onComponentDelete(ComponentType type, Component component);
	
	public abstract void update();
	public abstract void processMessages();
}
