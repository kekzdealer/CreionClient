package utility;

import org.joml.Matrix4f;

public class Projector {
	
	private static final Matrix4f p = new Matrix4f();
	
	public static Matrix4f getProjectionMatrix(
			float left, float right,
			float top, float bottom,
			float near, float far) {
		
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
