package logic;

import java.io.IOException;
import java.util.HashMap;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;

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
	
	private static final int TARGET_FPS = 1;

	private static boolean running = false;
	
	private final String address;
	private final int port;
	
	// TIME
	public static float FRAME_TIME = 0;
	private float frameBegin = 0;
	
	public static void main(String[] args) {
		// Map runtime arguments
		final HashMap<String, String> argMap = new HashMap<>();
		for(String arg : args) {
			String[] kv = arg.split("=");
			if(kv.length > 1) {
				argMap.put(kv[0], kv[1]);
			} else {
				argMap.put(kv[0], "");
			}
		}
		
		final String address = argMap.containsKey("address") ? argMap.get("address") : "";
		final int port = (argMap.containsKey("port") && !argMap.get("port").equals("")) 
				? Integer.parseInt(argMap.get("port")) : -1;
		final Main main = new Main(address, port);
		
		final Thread gameloop = new Thread(main, "game loop");
		running = true;
		gameloop.start();
	}
	
	public Main(String address, int port) {
		this.address = address;
		this.port = port;
	}
	
	@Override
	public void run() {
		
		// Window creation 
		Display display = null;
		display = new Display(1280, 720);
		display.create();
		
		final MessageBus messageBus = MessageBus.getInstance();
				
		// Only make a NetworkConnector if an address and port was passed as arguments on startup
		NetworkConnector networkConnector = null;
		if(!address.equals("") && port >= 0) {
			Logger.INFO.log("Acceptedd parameters: " + address + ":" + port);
			networkConnector = new NetworkConnector(address, port);
		}
		
		final InputMapper inputMapper = new InputMapper(display, messageBus);
		final UIManager uiManager = new UIManager(display.getWidth(), display.getHeight());
		
		final RenderComponentResourceManager renderComponentResourceManager = new RenderComponentResourceManager();
		
		final EntitySystem entitySystem = new EntitySystem();
		final RenderSystem renderSystem = new RenderSystem(entitySystem.getEntityDB(), display, 
				renderComponentResourceManager, uiManager);
		final TransformationSystem transformationSystem = new TransformationSystem(entitySystem.getEntityDB());
		final GUISystem guiSystem = new GUISystem(entitySystem.getEntityDB(), uiManager);
		entitySystem.setupSystems(renderSystem, transformationSystem, guiSystem);
		
		// spawn player. has id = 0.
		messageBus.messageSystem(Recipients.ENTITY_SYSTEM, EntitySystem.SPAWN, "mage", new Matrix4f());
		
		while(running) {
			frameBegin = (float) GLFW.glfwGetTime();
			// Input
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
