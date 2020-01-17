package graphics;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import utility.Logger;

@Deprecated
public class ShapeFactory {
	
	private static final ShapeFactory instance = new ShapeFactory();
	
	private ShapeFactory() {
		
	}
	
	public static ShapeFactory getInstance() {
		return instance;
	}
	
	private ArrayList<Integer> vaos = new ArrayList<>();
	private ArrayList<Integer> vbos = new ArrayList<>();
	
	/**
	 * Loads a Quad to memory and primes it with texture coordinates.
	 * Textures will be stretched to fill the quad.
	 * Width and height are taken as absolute values.
	 * @return
	 */
	public Shape createQuad(float width, float height) {
		width = Math.abs(width);// 1
		height = Math.abs(height);// 2
		// init data
		final float[] vertices = {
				// Left bottom triangle
				width, 0.0f, 0.0f,// r t
				0.0f, height, 0.0f, // l t
				0.0f, 0.0f, 0.0f,// l b
				// Right top triangle
				0.0f, height, 0.0f,// l t
				width, 0.0f, 0.0f,// r b
				width, height, 0.0f// r t
		};
		// Prime VAO with texture coordinates
		final float[] texCoords = {
				// Left bottom triangle
				1.0f, 1.0f,
				0.0f, 0.0f,
				0.0f, 1.0f,
				// Right top triangle
				0.0f, 0.0f,
				1.0f, 1.0f,
				1.0f, 0.0f
		};
		// Store in FloatBuffer
		final FloatBuffer vBuffer = BufferUtils.createFloatBuffer(vertices.length);
		vBuffer.put(vertices);
		vBuffer.flip();
		final FloatBuffer tBuffer = BufferUtils.createFloatBuffer(texCoords.length);
		tBuffer.put(texCoords);
		tBuffer.flip();
		
		
		final int vaoID = createVAO();
		storeDataInAttributeList(0, 3, vBuffer);
		storeDataInAttributeList(1, 2, tBuffer);
		GL30.glBindVertexArray(0);
				
		return new Shape(vaoID, vertices.length / 3);
	}
	
	public void destroyCreatedData() {
		for(int vao : vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		for(int vbo : vbos) {
			GL15.glDeleteBuffers(vbo);
		}
	}
	
	private int createVAO() {
		final int vaoID = GL30.glGenVertexArrays(); 
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	private void storeDataInAttributeList(int attributeID, int dimensions, FloatBuffer data) {
		final int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeID, dimensions, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
}
