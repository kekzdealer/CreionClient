package input;

import java.util.HashSet;
import java.util.Set;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.lwjgl.glfw.GLFW;

import graphics.Display;

public class SourceMouse implements InputSourceI {
	
	private final Display display;

	private boolean doInteract = false;
	
	private final Vector2f mousePos = new Vector2f();
	
	public SourceMouse(Display display) {
		this.display = display;
		
		// Hide system cursor when inside the game window
		GLFW.glfwSetInputMode(display.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
		GLFW.glfwSetCursorPosCallback(display.window, (window, xpos, ypos) ->{
			mousePos.set((float) xpos, (float) ypos);
		});
	}
	
	@Override
	public float getCursorSensitivity() {
		return 0.04f;
	}

	@Override
	public Vector2fc pollCursorMoveDirection() {
		
		final Vector2fc mapped = new Vector2f(
				mousePos.x() / display.getWidth() * 2.0f - 1.0f,
				mousePos.y() / display.getHeight() * -2.0f + 1.0f // -2.0f to invert the y axis
				);
		
		return mapped;
	}

	@Override
	public boolean doInteract() {
		final int glfwResult = GLFW.glfwGetMouseButton(display.window, GLFW.GLFW_MOUSE_BUTTON_1);

		if (!doInteract && glfwResult >= 1) {
			// key was just pressed
			doInteract = true;
		} else if (doInteract && glfwResult <= 0) {
			// key was just released
			doInteract = false;
		}

		return doInteract;
	}

	@Override
	public boolean sourceConnectionClosed() {
		// TODO implement
		return false;
	}

	@Override
	public boolean closeGame() {
		// TODO implement
		return false;
	}

	@Override
	public Vector2ic pollMoveDirection() {
		// TODO implement
		return new Vector2i(0, 0);
	}

	@Override
	public boolean doAbility() {
		// TODO implement
		return false;
	}
	
	@Override
	public Set<Integer> toggleGUIWindow() {
		return new HashSet<Integer>();
	}
	
}
