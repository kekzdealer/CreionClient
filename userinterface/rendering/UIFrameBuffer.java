package rendering;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

public class UIFrameBuffer {
	
	private final int width;
	private final int height;
	
	private final int id;
	private final int textureID;
	private final int depthBufferID;
	
	public UIFrameBuffer(int width, int height) {
		this.width = width;
		this.height = height;
		
		id = createFrameBuffer();
		textureID = createTextureAttachment();
		depthBufferID = createDepthBufferAttachment();
		unbindFrameBuffer();
	}
	
	public void destroy() {
		GL30.glDeleteFramebuffers(id);
		GL11.glDeleteTextures(textureID);
		GL30.glDeleteRenderbuffers(depthBufferID);
	}
	
	public void bindFrameBuffer() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);//To make sure the texture isn't bound
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, id);
    }
	
	public void unbindFrameBuffer() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	}
	
	public int getTextureID() {
		return textureID;
	}
	
	private int createFrameBuffer() {
		final int id = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, id);
		// Make color attachment 0 the draw buffer
		GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
		return id;
	}
	
	private int createTextureAttachment() {
		final int textureID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)null);
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, textureID, 0);
		return textureID;
	}
	
	private int createDepthBufferAttachment() {
        final int depthBufferID = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBufferID);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width,
                height);
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT,
                GL30.GL_RENDERBUFFER, depthBufferID);
        return depthBufferID;
    }
	
}
