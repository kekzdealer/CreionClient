package utility;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;

public class CMath {
	
	/**
	 * Converts integer distance units used by the game logic
	 * to OpenGL compatible vertex locations
	 */
	public static float distanceUnitsToFloat(long untis) {
		return untis / 1000.0f;
	}
	
	/**
	 * Converts an OpenGL compatible vertex location to an integer number
	 * that can be used inside the game logic 
	 */
	public static long floatToDistanceUnits(float ogl) {
		return Math.round(ogl * 1000.0f);
	}
	
	/**
	 * Creates an orthogonal projection matrix.
	 * @param left
	 * 			Distance to left screen border
	 * @param right
	 * 			Distance to right screen border
	 * @param top
	 * 			Distance to upper screen border
	 * @param bottom
	 * 			Distance to lower screen border
	 * @param near
	 * 			Minimum render distance
	 * @param far
	 * 			Maximum render distance 
	 * @return
	 * 			Orthogonal Projection Matrix
	 */
	public static Matrix4fc projectOrthogonal(
			float left, float right,
			float top, float bottom,
			float near, float far) {
		
		final Matrix4f p = new Matrix4f();
		
		final float ff = 2.0f / (right - left);
		final float ss = 2.0f / (top - bottom);
		final float tt = -2.0f / (far - near);
		
		final float f4 = -(right + left) / (right - left);
		final float s4 = -(top + bottom) / (top - bottom);
		final float t4 = -(far + near) / (far - near);
		
		return p.set(
				ff, 0.0f, 0.0f, f4,
				0.0f, ss, 0.0f, s4,
				0.0f, 0.0f, tt, t4,
				0.0f, 0.0f, 0.0f, 1.0f);
	}
}
