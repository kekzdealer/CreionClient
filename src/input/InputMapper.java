package input;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector2fc;
import org.joml.Vector2ic;
import org.lwjgl.glfw.GLFW;

import bus.MessageBus;
import bus.Recipients;
import graphics.Display;
import systems.EntitySystem;
import systems.GUISystem;
import systems.TransformationSystem;
import utility.Logger;



public class InputMapper {
	
	private MessageBus messageBus;

	private List<InputSourceI> inputSources = new ArrayList<>();

	
	public InputMapper(Display display, MessageBus bus) {
		if (!display.isInitialised()) {
			throw new IllegalStateException("Display not initialised!");
		}

		this.messageBus = bus;

		inputSources.add(new SourceKeyboard(display));
		inputSources.add(new SourceMouse(display));
	}

	public void updateInput() {
		GLFW.glfwPollEvents();

		// Remove source if connection closed
		final Iterator<InputSourceI> iterator = inputSources.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().sourceConnectionClosed()) {
				iterator.remove();
			}
		}
		
		// Process input events
		for (InputSourceI is : inputSources) {
			processInputSource(is);
		}
	}

	private void processInputSource(InputSourceI input) {
		if (input.sourceConnectionClosed()) { 
			// Ignore inactive sources
			return;
		}
		
		// Move direction
		final Vector2ic moveDir = input.pollMoveDirection();
		final long unitsPerFrame = 10;
		messageBus.messageSystem(Recipients.TRANSFORMATION_SYSTEM, TransformationSystem.MOVE, 0, moveDir.x() * unitsPerFrame, moveDir.y() * unitsPerFrame);
		
		// Ability use
		if(input.doAbility()) {
			messageBus.messageSystem(Recipients.ENTITY_SYSTEM, EntitySystem.SPAWN, "red_magic_circle", new Matrix4f().translate(0.0f, 0.0f, -1.0f).scale(3.0f));
		}
		
		// GUI window toggle
		for(int guiWindow : input.toggleGUIWindow()) {
			messageBus.messageSystem(Recipients.GUI_SYSTEM, guiWindow);
		}
		
		// Cursor pos
		final Vector2fc cursorPos = input.pollCursorMoveDirection();
		final long x = Math.round(cursorPos.x());
		final long y = Math.round(cursorPos.y());
		messageBus.messageSystem(Recipients.GUI_SYSTEM, GUISystem.UPDATE_CURSOR_POSITION, cursorPos.x(), cursorPos.y());
		
	}
}
