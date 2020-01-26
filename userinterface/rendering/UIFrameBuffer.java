package rendering;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import utility.Logger;

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
		Logger.INFO.log("FBO textureID: " + textureID);
		final int status = GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER);
		if(status != GL30.GL_FRAMEBUFFER_COMPLETE) {
			Logger.ERROR.log("Frame Buffer assembly failed:");
			switch(status) {
			case GL30.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
				Logger.ERROR.log("Incomplete Attachment");
				break;
			case GL30.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER:
				Logger.ERROR.log("Incomplete Draw Buffer");
				break;
			case GL30.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
				Logger.ERROR.log("Incomplete Missing Attachment");
				break;
			case GL30.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER:
				Logger.ERROR.log("Incomplete Read Buffer");
			case GL30.GL_FRAMEBUFFER_UNSUPPORTED:
				Logger.ERROR.log("Frame Buffer Assembly Unsupported");
				break;
			case GL30.GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE:
				Logger.ERROR.log("Incomplete Multi Sample");
				break;
			case 0:
				Logger.ERROR.log("An Error occured lmao");
			}
		} else {
			Logger.INFO.log("Successfully assembled GUI Frame Buffer");
		}
		
		GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		unbindFrameBuffer();
	}
	
	public void destroy() {
		GL30.glDeleteFramebuffers(id);
		GL11.glDeleteTextures(textureID);
		GL30.glDeleteRenderbuffers(depthBufferID);
	}
	
	public void bindFrameBuffer() {
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
		// Create texture
		final int textureID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)null);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		// Attach to frame buffer
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, textureID, 0);
		return textureID;
	}
	
	private int createDepthBufferAttachment() {
        // Create render buffer for depth buffer
		final int depthBufferID = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBufferID);
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width,
                height);
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);
        // Attach to frame buffer
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT,
                GL30.GL_RENDERBUFFER, depthBufferID);
        return depthBufferID;
    }
	
}
