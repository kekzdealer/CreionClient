package systems;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import bus.Message;
import bus.MessageBus;
import bus.Recipients;
import graphics.BackgroundShader;
import graphics.Display;
import logic.EntityDatabase;
import logic.EntityDatabase.ComponentType;
import tags.CRender;
import tags.CTransformation;
import tags.Component;
import utility.Logger;
import utility.Projector;
import logic.RenderComponentResourceManager;

public class RenderSystem extends AbstractSystem {
	
	private final Display display;
	private final RenderComponentResourceManager renderComponentResourceManager;
	
	private final BackgroundShader backgroundShader = new BackgroundShader();
	
	private final Matrix4fc projectionMatrix; 
	
	public RenderSystem(EntityDatabase entityDB, Display display, RenderComponentResourceManager renderComponentResourceManager) {
		super(entityDB);
		this.display = display;
		this.renderComponentResourceManager = renderComponentResourceManager;
		
		if (!display.isInitialised()) {
			throw new IllegalStateException("Display not initialised!");
		}
		
		GL11C.glViewport(0, 0, display.getWidth(), display.getHeight());
		//projectionMatrix = new Matrix4f();
		projectionMatrix = Projector.getProjectionMatrix(-1.6f, 1.6f, 0.9f, -0.9f, -128.0f, 128.0f);
		
		// Back-face culling
		GL11C.glEnable(GL11C.GL_CULL_FACE);
		GL11C.glCullFace(GL11C.GL_BACK);
		// Depth-Testing
		GL11C.glEnable(GL11C.GL_DEPTH_TEST);
		GL11C.glDepthMask(true);
	}
	
	/**
	 * Argument sequence:</br>
	 * - int: entity ID</br>
	 * - String: texture resource name
	 */
	@Override
	protected void initializeComponent(ComponentType type, Object...args) {
		switch(type) {
		case RENDER:
			final CRender c = renderComponentResourceManager.loadFromPreset((String) args[1]);
			entityDB.addComponent((int) args[0], type, c);
			break;
		default: Logger.ERROR.log("Render System cannot initialize a foreign component");
		}
	}
	
	@Override
	protected void onComponentDelete(ComponentType type, Component component) {
		switch(type) {
		case RENDER:
			renderComponentResourceManager.unload((CRender) component); 
			break;
		default: Logger.ERROR.log("Render System cannot delete a foreign component");
		}
	}
	
	@Override
	public void update() {
		// Prepare
		GL11.glClearColor(0.2f, 0.6f, 0.1f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		// Render entities
		for(int eID = 0; eID < entityDB.getEntityCount(); eID++) {
			final CRender cr = entityDB.getRenderComponent(eID);
			final CTransformation ct = entityDB.getTransformationComponent(eID);
			
			backgroundShader.start();
			backgroundShader.uploadProjection(projectionMatrix);
			GL30.glBindVertexArray(cr.getShape().getVaoID());
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, cr.getTexture().getTextureID());
			backgroundShader.uploadTexture(0); // Activated texture bank 0 two lines ago, so pass 0 here.
			backgroundShader.uploadTransformation(ct.get());
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cr.getShape().getVertexCount());
			backgroundShader.stop();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
			GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(1);
			GL30.glBindVertexArray(0);
		}
		
		// Render UI frame buffer texture from UIManager here later
		
		// Finalize
		
	}
	
	@Override
	public void processMessages() {
		Message message = null;
		while((message = MessageBus.getInstance().getNextMessage(Recipients.RENDER_SYSTEM)) != null) {
			switch(message.getBehaviorID()) {
			
			default: Logger.ERROR.log("Render System doesn't recognize this behavior ID: " + message.getBehaviorID());
			}
		}
	}
	
}
