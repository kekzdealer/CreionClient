package graphics;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.opengl.GL12C;
import org.lwjgl.stb.STBImage;

@Deprecated
public class TextureFactory {
	
	private static final TextureFactory instance = new TextureFactory();
	
	private TextureFactory( ) {
		
	}
	
	public static TextureFactory getIntance() {
		return instance;
	}
	
	private ArrayList<Integer> textureIDs = new ArrayList<>();
	
	public Texture createTexture(String filename) {
		
		final IntBuffer width = BufferUtils.createIntBuffer(1);
		final IntBuffer height = BufferUtils.createIntBuffer(1);
		final IntBuffer comp = BufferUtils.createIntBuffer(1);
		int widthInt;
		int heightInt;
		int textureID;
		
		ByteBuffer data = STBImage.stbi_load("src/graphics/" + filename + ".png", width, height, comp, 4);
		if(data == null){
			System.err.println(STBImage.stbi_failure_reason() + " -> " + filename);
		}
		
		widthInt = width.get();
		heightInt = height.get();
		
		textureID = GL11C.glGenTextures();
		textureIDs.add(textureID);
		GL11C.glBindTexture(GL11C.GL_TEXTURE_2D, textureID);
		// Set texture wrapping
		GL11C.glTexParameterf(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_WRAP_S, GL12C.GL_CLAMP_TO_EDGE);
		GL11C.glTexParameterf(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_WRAP_T, GL12C.GL_CLAMP_TO_EDGE);
		// Set texture filtering
		GL11C.glTexParameterf(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_MIN_FILTER, GL11C.GL_NEAREST);
		GL11C.glTexParameterf(GL11C.GL_TEXTURE_2D, GL11C.GL_TEXTURE_MAG_FILTER, GL11C.GL_NEAREST);
		
		// Write buffer to buffer bound to GL_TEXTURE_2D
		GL11C.glTexImage2D(GL11C.GL_TEXTURE_2D, 0, GL11C.GL_RGBA, widthInt, heightInt, 0, GL11C.GL_RGBA, GL11C.GL_UNSIGNED_BYTE, data);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		return new Texture(textureID);
	}
	
	public void destroyCreatedData() {
		for(int textureID : textureIDs) {
			GL11.glDeleteTextures(textureID);
		}
	}
}
