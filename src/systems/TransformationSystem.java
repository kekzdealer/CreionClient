package systems;

import org.joml.Matrix4fc;

import bus.Message;
import bus.MessageBus;
import bus.Recipients;
import logic.EntityDatabase;
import logic.EntityDatabase.ComponentType;
import tags.CTransformation;
import tags.Component;
import utility.CMath;
import utility.Logger;

public class TransformationSystem extends AbstractSystem {

	public TransformationSystem(EntityDatabase entityDB) {
		super(entityDB);
	}
	
	/**
	 * Argument Sequence:</br>
	 * - int: entity ID</br>
	 * - Matrix4fc: transformation
	 */
	@Override
	protected void initializeComponent(ComponentType type, Object... args) {
		switch(type) {
		case TRANSFORMATION:
			final CTransformation c = new CTransformation();
			c.get().mul((Matrix4fc) args[1]);
			entityDB.addComponent((int) args[0], type, c);
			break;
		default: Logger.ERROR.log("Transformation System cannot initialize a foreign component");
		}
	}
	
	@Override
	protected void onComponentDelete(ComponentType type, Component component) {
		// Nothing to do here 
	}

	@Override
	public void update() {
		for(int eID = 0; eID < super.entityDB.getEntityCount(); eID++) {
			
		}
	}

	public static final int TRANSLATE = 0; // entity_id, float x, float y, float z
	public static final int MOVE = 1; // entity_id, long x_units, long y_units
	@Override
	public void processMessages() {
		Message message = null;
		while((message = MessageBus.getInstance().getNextMessage(Recipients.TRANSFORMATION_SYSTEM)) != null) {
			final Object[] args = message.getArgs();
			switch(message.getBehaviorID()) {
			case TRANSLATE: 
				super.entityDB.getTransformationComponent((int) args[0]).get().translate(
						(float) args[1], 
						(float) args[2], 
						(float) args[3]);
				break;
			case MOVE:
				super.entityDB.getTransformationComponent((int) args[0]).get().translate(
						CMath.distanceUnitsToFloat((long) args[1]), 
						CMath.distanceUnitsToFloat((long) args[2]), 
						0.0f);
				break;
			default: Logger.ERROR.log("Transformation System doesn't recognize this behavior ID: " + message.getBehaviorID());
			}
		}
	}

}
