package logic;

import java.io.IOException;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;

import bus.Message;
import bus.MessageBus;
import bus.Recipients;
import components.UIManager;
import graphics.Display;
import input.InputMapper;
import networking.NetworkConnector;
import systems.EntitySystem;
import systems.GUISystem;
import systems.RenderSystem;
import systems.TransformationSystem;
import utility.Logger;

public class Main implements Runnable {
	
	public static final int RESIZE_WINDOW = 0;
	
	private static final int TARGET_FPS = 1;

	private static boolean running = false;
	
	// Engine parts
	private final Display display;
	private final MessageBus messageBus;
	private NetworkConnector networkConnector; // TODO allow for this to be final and always have an instance
	private final InputMapper inputMapper;
	private final UIManager uiManager;
	private final RenderComponentResourceManager renderComponentResourceManager;
	
	private final EntitySystem entitySystem;
	private final RenderSystem renderSystem;
	private final TransformationSystem transformationSystem;
	private final GUISystem guiSystem;
	
	// Time
	private static float FRAME_TIME = 0;
	private float frameBegin = 0;
	
	public static void main(String[] args) {
				
		Main main;
		if(args.length >= 2) {
			main = new Main(args[0], Integer.parseInt(args[1]));		
		} else {
			main = new Main("", -1);
		}
		
		final Thread gameloop = new Thread(main, "game loop");
		running = true;
		gameloop.start();
	}
	
	public Main(String address, int port) {
		
		// Window creation 
		display = new Display(1280, 720);
		display.create();
		
		messageBus = MessageBus.getInstance();
		
		// TODO change this - see above todo
		// Only make a NetworkConnector if an address and port was passed as arguments on startup
		if(!address.equals("") && port >= 0) {
			Logger.INFO.log("Acceptedd parameters: " + address + ":" + port);
			networkConnector = new NetworkConnector(address, port);
		}
		
		inputMapper = new InputMapper(display, messageBus);
		uiManager = new UIManager(display.getWidth(), display.getHeight());
		renderComponentResourceManager = new RenderComponentResourceManager();
		
		entitySystem = new EntitySystem();
		renderSystem = new RenderSystem(entitySystem.getEntityDB(), display, 
				renderComponentResourceManager, uiManager);
		transformationSystem = new TransformationSystem(entitySystem.getEntityDB());
		guiSystem = new GUISystem(entitySystem.getEntityDB(), uiManager);
		
		entitySystem.setupSystems(renderSystem, transformationSystem, guiSystem);
		
	}
	
	private void processMessages() {
		Message message = null;
		while((message = MessageBus.getInstance().getNextMessage(Recipients.ENTITY_SYSTEM)) != null) {
			final Object[] args = message.getArgs();
			switch(message.getBehaviorID()) {
			
			default: Logger.ERROR.log("Main doesn't recognize this behavior ID: " + message.getBehaviorID());
			}
		}
	}
	
	@Override
	public void run() {
		
		// spawn player. has id = 0.
		messageBus.messageSystem(Recipients.ENTITY_SYSTEM, EntitySystem.SPAWN, "mage", new Matrix4f());
		
		while(running) {
			frameBegin = (float) GLFW.glfwGetTime();
			// Input
			processMessages();
			inputMapper.updateInput();
			
			// Logic
			entitySystem.processMessages();
			
			transformationSystem.processMessages();
			transformationSystem.update();
			
			// Render
			guiSystem.processMessages();
			guiSystem.update();
			renderSystem.processMessages();
			renderSystem.update();
			// Control
			if(GLFW.glfwWindowShouldClose(display.window)){
				running = false;
			}
			
			FRAME_TIME = (float) GLFW.glfwGetTime() - frameBegin;
			try {
				final long sleepyTime = (long) Math.max(0, (1000 / TARGET_FPS) - FRAME_TIME);
				Thread.sleep(sleepyTime);
			} catch(InterruptedException e) {
				Logger.ERROR.log("Main thread was interrupted while sleeping");
				e.printStackTrace(Logger.ERROR.getPrintStream());
			}
		}
		display.destroy();
		if(networkConnector != null && networkConnector.isConnected()) {
			try {
				networkConnector.close();
			} catch (IOException e) {
				Logger.ERROR.log("Error while closing Network Connector");
			}
		}
	}
	
}
