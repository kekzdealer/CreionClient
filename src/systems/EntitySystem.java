package systems;

import java.util.HashSet;

import org.joml.Matrix4f;

import bus.Message;
import bus.MessageBus;
import bus.Recipients;
import logic.EntityDatabase;
import logic.EntityDatabase.ComponentType;
import utility.Logger;

public class EntitySystem {

	private final EntityDatabase entityDB = new EntityDatabase();

	private RenderSystem renderSystem;
	private TransformationSystem transformationSystem;
	private GUISystem guiSystem;
	
	public EntityDatabase getEntityDB() {
		return entityDB;
	}
	
	public void setupSystems(RenderSystem renderSystem, TransformationSystem transformationSystem,
			GUISystem guiSystem) {
		this.renderSystem = renderSystem;
		this.transformationSystem = transformationSystem;
		this.guiSystem = guiSystem;
	}
	
	public static final int SPAWN = 0;
	public static final int REMOVE = 1;
	public void processMessages() {
		Message message = null;
		while((message = MessageBus.getInstance().getNextMessage(Recipients.ENTITY_SYSTEM)) != null) {
			final Object[] args = message.getArgs();
			switch(message.getBehaviorID()) {
			case SPAWN: spawnEntity((String) args[0], (Matrix4f) args[1]); break;
			case REMOVE: removeEntity((int) args[0]); break;
			default: Logger.ERROR.log("Entity System doesn't recognize this behavior ID: " + message.getBehaviorID());
			}
		}
	}

	public int spawnEntity(String textureResourceName, Matrix4f initialLocation) {
		final HashSet<ComponentType> tags = new HashSet<>();
		tags.add(ComponentType.RENDER);
		tags.add(ComponentType.TRANSFORMATION);
		final int eID = entityDB.generateEntity(tags);
		
		renderSystem.initializeComponent(ComponentType.RENDER, eID, textureResourceName);
		transformationSystem.initializeComponent(ComponentType.TRANSFORMATION, eID, initialLocation);
		
		return eID;
	}
	
	public void removeEntity(int eID) {
		renderSystem.onComponentDelete(ComponentType.RENDER, entityDB.getRenderComponent(eID));
		transformationSystem.onComponentDelete(ComponentType.TRANSFORMATION, entityDB.getTransformationComponent(eID));
		
		entityDB.deleteEntity(eID);
	}
}
