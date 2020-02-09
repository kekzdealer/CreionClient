package graphics;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import bus.MessageBus;
import bus.Recipients;
import systems.RenderSystem;

public class Display {
	
	public long window = 0;

	private int width;
	private int height;

	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}

	private boolean isInitialised = false;

	public boolean isInitialised() {
		return isInitialised;
	}

	public Display(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void create() {
		// Set error output
		GLFWErrorCallback.createPrint(System.err).set();
		// Initialize GLFW
		if (glfwInit() != true) {
			throw new RuntimeException("GLFW initialization failed!");
		}
		// Set window properties
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

		// Create window
		window = glfwCreateWindow(width, height, "Creion", NULL, NULL);
		if (window == NULL) {
			throw new RuntimeException("Could not create Window");
		}
		
		// ESC to close
		GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) ->{
            if(key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS)
                GLFW.glfwSetWindowShouldClose(window, true);
        });
		
		// Window resizing
		GLFW.glfwSetWindowSizeCallback(window, (window, width, height) -> {
			this.width = width;
			this.height = height;
			// Tell the render system to update the GLViewPort
			MessageBus.getInstance().messageSystem(Recipients.RENDER_SYSTEM, RenderSystem.WINDOW_RESIZE);
		});
		
		glfwSetWindowPos(window, 1, 20);
		
		GLFW.glfwMakeContextCurrent(window);
		GL.createCapabilities();
		
		glfwShowWindow(window);
		
		
		IntBuffer w = BufferUtils.createIntBuffer(1);
		IntBuffer h = BufferUtils.createIntBuffer(1);
		glfwGetWindowSize(window, w, h);
		width = w.get(0);
		height = h.get(0);
		
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

		isInitialised = true;
	}
	
	public void setFullscreen(boolean fullscreen) {
		
	}

	public void submitFrame() {
		if (!isInitialised()) {
			throw new IllegalStateException("Display not initialised!");
		}

		glfwSwapBuffers(window);
	}

	public void destroy() {
		if (!isInitialised()) {
			throw new IllegalStateException("Display not initialised!");
		}

		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

}
