package input;

import java.util.Set;

import org.joml.Vector2fc;
import org.joml.Vector2ic;

/**
 * Implement this when adding a new input system to the engine
 * 
 * @author kekzd
 *
 */
public interface InputSourceI {
	
	/**
	 * Polls if the input source does still exist and is active.
	 * 
	 * @return Input source status
	 */
	public boolean sourceConnectionClosed();

	// System actions
	public boolean closeGame();
	
	// Movement
	public Vector2ic pollMoveDirection();
	
	/**
	 * Queries cursor sensitivity.
	 * 
	 * @return cursor sensitivity from 0.1f to 1.0f
	 */
	public float getCursorSensitivity();
	public Vector2fc pollCursorMoveDirection();
	
	// Generic action triggered
	public boolean doInteract();
	public boolean doAbility();
	
	// GUI window toggles
	public Set<Integer> toggleGUIWindow();
	
}
