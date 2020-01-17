package input;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.lwjgl.glfw.GLFW;

import graphics.Display;
import systems.GUISystem;

public class SourceKeyboard implements InputSourceI {
	
	private enum Actions {
		FORWARD,
		BACKWARD,
		LEFT,
		RIGHT,
		
		INTERACT,
		ABILITY,
		
		CURSOR_UP,
		CURSOR_DOWN,
		CURSOR_LEFT,
		CURSOR_RIGHT,
		
		SHUTDOWN,
		
		TOGGLE_CHARACTER_INFO,
		TOGGLE_INVENTORY
	}
	private final HashMap<Actions, Integer> keyMappings;	
	
	private final Display display;

	// flags
	private boolean doInteract = false;
	private boolean doAbility = false;

	public SourceKeyboard(Display disp) {
		this.display = disp;
		// Configure key bindings
		keyMappings = new HashMap<>();
		keyMappings.put(Actions.FORWARD, GLFW.GLFW_KEY_W);
		keyMappings.put(Actions.BACKWARD, GLFW.GLFW_KEY_S);
		keyMappings.put(Actions.LEFT, GLFW.GLFW_KEY_A);
		keyMappings.put(Actions.RIGHT, GLFW.GLFW_KEY_D);
		
		keyMappings.put(Actions.INTERACT, GLFW.GLFW_KEY_E);
		keyMappings.put(Actions.ABILITY, GLFW.GLFW_KEY_Q);
		
		keyMappings.put(Actions.CURSOR_UP, GLFW.GLFW_KEY_UP);
		keyMappings.put(Actions.CURSOR_DOWN, GLFW.GLFW_KEY_DOWN);
		keyMappings.put(Actions.CURSOR_LEFT, GLFW.GLFW_KEY_LEFT);
		keyMappings.put(Actions.CURSOR_RIGHT, GLFW.GLFW_KEY_RIGHT);
		
		keyMappings.put(Actions.SHUTDOWN, GLFW.GLFW_KEY_ESCAPE);
		
		keyMappings.put(Actions.TOGGLE_CHARACTER_INFO, GLFW.GLFW_KEY_C);
		keyMappings.put(Actions.TOGGLE_INVENTORY, GLFW.GLFW_KEY_B);
	}

	@Override
	public float getCursorSensitivity() {
		return 0.5f;
	}
	
	@Override
	public Vector2ic pollMoveDirection() {
		final Vector2i ret = new Vector2i();
		
		if (GLFW.glfwGetKey(display.window, keyMappings.get(Actions.LEFT)) == GLFW.GLFW_PRESS) {
			ret.x = -1;
		}
		if (GLFW.glfwGetKey(display.window, keyMappings.get(Actions.FORWARD)) == GLFW.GLFW_PRESS) {
			ret.y = 1;
		}
		if (GLFW.glfwGetKey(display.window, keyMappings.get(Actions.BACKWARD)) == GLFW.GLFW_PRESS) {
			ret.y = -1;
		}
		if (GLFW.glfwGetKey(display.window, keyMappings.get(Actions.RIGHT)) == GLFW.GLFW_PRESS) {
			ret.x = 1;
		}
		
		return ret;
	}

	@Override
	public Vector2fc pollCursorMoveDirection() {
		final Vector2f ret = new Vector2f();
		
		if (GLFW.glfwGetKey(display.window, keyMappings.get(Actions.CURSOR_LEFT)) == GLFW.GLFW_PRESS) {
			ret.x = -1.0f;
		}
		if (GLFW.glfwGetKey(display.window, keyMappings.get(Actions.CURSOR_UP)) == GLFW.GLFW_PRESS) {
			ret.y = 1.0f;
		}
		if (GLFW.glfwGetKey(display.window, keyMappings.get(Actions.CURSOR_DOWN)) == GLFW.GLFW_PRESS) {
			ret.y = -1.0f;
		}
		if (GLFW.glfwGetKey(display.window, keyMappings.get(Actions.CURSOR_RIGHT)) == GLFW.GLFW_PRESS) {
			ret.x = 1.0f;
		}
		
		return ret;
	}
	

	@Override
	public boolean doInteract() {
		final int glfwResult = GLFW.glfwGetKey(display.window, keyMappings.get(Actions.INTERACT));
		if(!doInteract && glfwResult == GLFW.GLFW_PRESS) {
			doInteract = true;
		} else if (doInteract && glfwResult == GLFW.GLFW_RELEASE) {
			doInteract = false;
		}

		return doInteract;
	}

	@Override
	public boolean doAbility() {
		final int glfwResult = GLFW.glfwGetKey(display.window, keyMappings.get(Actions.ABILITY));
		if(!doAbility && glfwResult == GLFW.GLFW_PRESS) {
			doAbility = true;
		} else if(doAbility && glfwResult == GLFW.GLFW_RELEASE) {
			doAbility = false;
		}
		
		return doAbility;
	}

	@Override
	public boolean closeGame() {
		return GLFW.glfwGetKey(display.window, keyMappings.get(Actions.SHUTDOWN)) == GLFW.GLFW_PRESS;
	}
	
	@Override
	public Set<Integer> toggleGUIWindow(){
		final HashSet<Integer> e = new HashSet<>();
		if(GLFW.glfwGetKey(display.window, keyMappings.get(Actions.TOGGLE_CHARACTER_INFO)) == GLFW.GLFW_PRESS) {
			e.add(GUISystem.TOGGLE_CHARACTER_INFO);
		}
		if(GLFW.glfwGetKey(display.window, keyMappings.get(Actions.TOGGLE_INVENTORY)) == GLFW.GLFW_PRESS) {
			e.add(GUISystem.TOGGLE_INVENTORY);
		}
		return e;
	}

	@Override
	public boolean sourceConnectionClosed() {
		// TODO implement
		return false;
	}

}
