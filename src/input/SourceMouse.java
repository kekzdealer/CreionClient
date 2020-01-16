package input;

import java.nio.DoubleBuffer;
import java.util.HashSet;
import java.util.Set;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import graphics.Display;

public class SourceMouse implements InputSourceI {
	
	private boolean doInteract = false;
	private final Vector2f prevMousePos = new Vector2f();

	private final Display display;

	public SourceMouse(Display disp) {
		this.display = disp;
	}
	
	@Override
	public float getCursorSensitivity() {
		return 0.04f;
	}

	@Override
	public Vector2fc pollCursorMoveDirection() {
		final DoubleBuffer b1 = BufferUtils.createDoubleBuffer(1);
		final DoubleBuffer b2 = BufferUtils.createDoubleBuffer(1);
		GLFW.glfwGetCursorPos(display.window, b1, b2);
		
		// yaw -> x, pitch -> y, roll -> z
		final Vector2f ret = new Vector2f((float) b1.get(0), (float) b2.get(0));
		ret.sub(prevMousePos);
		ret.mul(new Vector2f(1.0f, -1.0f));

		prevMousePos.set((float) b1.get(0), (float) b2.get(0));

		return ret;
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
	public Set<String> toggleGUIWindow() {
		return new HashSet<String>();
	}
	
}
