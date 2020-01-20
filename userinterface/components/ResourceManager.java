package components;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.opengl.GL12C;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;

import graphics.Shape;
import graphics.Texture;
import logic.EntityPreset;
import ui.ComponentRenderData;
import utility.Logger;

public class ResourceManager implements AutoCloseable {
	
	private static final String RESOURCE_PATH = "userinterface/textures/";
	private static ResourceManager instance;
	
	private final HashSet<EntityPreset> presets = new HashSet<>();
	
	private final HashMap<String, Integer> texturesCache = new HashMap<>();
	private final HashMap<String, Texture> textures = new HashMap<>();
	
	private final HashSet<Shape> carriers = new HashSet<>();
	
	private final Texture defaultTexture;
	private final Texture eraserTexture;
	
	private Shape guiCarrier;
	
	private ResourceManager() {
		presets.add(new EntityPreset("window_background", "window_background"));
		presets.add(new EntityPreset("progressbar_red", "progressbar_background_red"));
		presets.add(new EntityPreset("progressbar_green", "progressbar_foreground_green"));
		
		defaultTexture = createDefaultTextures(true);
		eraserTexture = createDefaultTextures(false);
		
		guiCarrier = createCarrier(2.0f, 2.0f);
	}
	
	protected static ResourceManager getInstance() {
		if(instance == null) {
			instance = new ResourceManager();
		}
		return instance;
	}
	
	private Texture loadTextureFromDisk(String fileName) {		
		final IntBuffer width = BufferUtils.createIntBuffer(1);
		final IntBuffer height = BufferUtils.createIntBuffer(1);
		final IntBuffer comp = BufferUtils.createIntBuffer(1);

		final ByteBuffer data = STBImage.stbi_load(RESOURCE_PATH + fileName + ".png", width, height, comp, 4);
		if(data == null){
			System.err.println(STBImage.stbi_failure_reason() + " -> " + fileName);
		}
		
		final int textureID = GL11C.glGenTextures();
		GL11C.glBindTexture(GL11C.GL_TEXTURE_2D, textureID);
		// Set texture wrapping
		GL11C.glTexParameterf(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_WRAP_S, GL12C.GL_CLAMP_TO_EDGE);
		GL11C.glTexParameterf(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_WRAP_T, GL12C.GL_CLAMP_TO_EDGE);
		// Set texture filtering
		GL11C.glTexParameterf(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_MIN_FILTER, GL11C.GL_NEAREST);
		GL11C.glTexParameterf(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_MAG_FILTER, GL11C.GL_NEAREST);
		
		// Write buffer to buffer bound to GL_TEXTURE_2D
		GL11C.glTexImage2D(GL11C.GL_TEXTURE_2D, 0, GL11C.GL_RGBA, width.get(), height.get(), 0, GL11C.GL_RGBA, GL11C.GL_UNSIGNED_BYTE, data);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		texturesCache.put(fileName, 1);
		final Texture texture = new Texture(textureID);
		textures.put(fileName, texture);
		return texture;
	}
	
	/**
	 * Pass 0 to create a default texture, or pass 1 to create an eraser texture
	 */
	private Texture createDefaultTextures(boolean defaultOrEraser) {
		
		final byte[] bytesDefault = {
			0, 0, 0, Byte.MAX_VALUE, 0, Byte.MAX_VALUE,
			Byte.MAX_VALUE, 0, Byte.MAX_VALUE, 0, 0, 0
		};
		final byte[] bytesEraser = {
				0, 0, 0, 0, 
				0, 0, 0, 0,
				0, 0, 0, 0,
				0, 0, 0, 0
		};
		
		// width = 2, height = 2, channels = 3 or 4
		final int channels = defaultOrEraser == true ? 3 : 4;
		final ByteBuffer data = BufferUtils.createByteBuffer(2 * 2 * channels);
		data.put(defaultOrEraser == true ? bytesDefault : bytesEraser);
		data.flip();
		
		final int textureID = GL11C.glGenTextures();
		GL11C.glBindTexture(GL11C.GL_TEXTURE_2D, textureID);
		// Set texture wrapping
		GL11C.glTexParameterf(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_WRAP_S, GL12C.GL_CLAMP_TO_EDGE);
		GL11C.glTexParameterf(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_WRAP_T, GL12C.GL_CLAMP_TO_EDGE);
		// Set texture filtering
		GL11C.glTexParameterf(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_MIN_FILTER, GL11C.GL_NEAREST);
		GL11C.glTexParameterf(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_MAG_FILTER, GL11C.GL_NEAREST);
		
		// Write buffer to buffer bound to GL_TEXTURE_2D
		final int dataFormat = defaultOrEraser == true ? GL11C.GL_RGB : GL11C.GL_RGBA;
		GL11C.glTexImage2D(GL11C.GL_TEXTURE_2D, 0, dataFormat, 2, 2, 0, dataFormat, GL11C.GL_UNSIGNED_BYTE, data);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		return new Texture(textureID);
	}
	
	protected Shape createCarrier(float width, float height) {
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
		
		final int vaoID = GL30.glGenVertexArrays();
		final int vbo0 = 0;
		final int vbo1 = 1;
		GL30.glBindVertexArray(vaoID);
		storeDataInAttributeList(0, 3, vBuffer);
		storeDataInAttributeList(1, 2, tBuffer);
		GL30.glBindVertexArray(0);
		
		return new Shape(vaoID, vertices.length / 3, vbo0, vbo1);
	}
	
	private void storeDataInAttributeList(int attributeID, int dimensions, FloatBuffer data) {
		final int vboID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeID, dimensions, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	/**
	 * Returns a 2 by 2 pixels texture that is completely transparent.
	 * Used to erase GUI elements from the FBO texture.
	 */
	public Texture getEraserTexture() {
		return eraserTexture;
	}
	
	/**
	 * Returns a screen filling rectangle that can be used to render the GUI FBO texture onto.
	 */
	protected Shape getGUICarrier() {
		return guiCarrier;
	}
	
	@Override
	public void close() {
		// Delete the guiCarrier mesh
		GL30.glDeleteVertexArrays(guiCarrier.getVaoID());
		for(int vbo : guiCarrier.getVboIDs()) {
			GL15.glDeleteBuffers(vbo);
		}
		// Delete carrier meshes
		for(Shape shape : carriers) {
			GL30.glDeleteVertexArrays(shape.getVaoID());
			for(int vbo : shape.getVboIDs()) {
				GL15.glDeleteBuffers(vbo);
			}
		}
		
		// Delete default/eraser textures
		GL11.glDeleteTextures(defaultTexture.getTextureID());
		GL11.glDeleteTextures(eraserTexture.getTextureID());
		// Delete all other remaining textures
		for(Texture t : textures.values()) {
			GL11.glDeleteTextures(t.getTextureID());
		}
	}
	
	/**
	 * Request a texture from a known set of textures.
	 * The texture will either be loaded from disk,
	 * or returned from cache.
	 */
	public Texture loadCachedTexture(String presetName) {
		// Search preset object
		EntityPreset preset = null;
		for(EntityPreset i : presets) {
			if(i.toString().equals(presetName)) {
				preset = i;
				break;
			}
		}
		if(preset == null) {
			Logger.ERROR.log("Unknown preset: " + presetName);
			return defaultTexture;
		}
		
		Texture texture = null;
		// Either: Grab cached texture and increment reference counter
		// Or: Load texture fresh from disk. Use default texture as fallback.
		if(texturesCache.containsKey(preset.sprite)) {
			texture = textures.get(preset.sprite);
			texturesCache.put(preset.sprite, texturesCache.get(preset.sprite) + 1);
		} else {
			texture = loadTextureFromDisk(preset.sprite);
			if(texture == null) {
				texture = defaultTexture;
			}
		}
		
		return texture;
	}
	
	/**
	 * Call to signal that data from a GUI component is no longer used 
	 * and it's referenced resources may be released if they are not used
	 * by any other render component.
	 */
	public void unload(ComponentRenderData component) {
		// Reverse lookup texture name from texture id
		String textureName = null;
		for(Entry<String, Texture> e : textures.entrySet()) {
			if(e.getValue().equals(component.getTexture())) {
				textureName = e.getKey();
				break;
			}
		}
		// Decrement reference counter
		texturesCache.put(textureName, texturesCache.get(textureName) - 1);
		// Optional delete texture from video memory
		if(texturesCache.get(textureName) < 1) {
			GL11.glDeleteTextures(textures.get(textureName).getTextureID());
		}
	}
		
}
