package rendering;

import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import ui.ComponentRenderData;
import utility.Logger;

public class UIRenderer {
	
	private final UIShader uiShader = new UIShader();
	private final ProgressBarShader progressBarShader = new ProgressBarShader();
	
	private final UIFrameBuffer fbo;
	
	public UIRenderer(int width, int height) {
		fbo = new UIFrameBuffer(width, height);
	}
	
	public void destroy() {
		fbo.destroy();
	}
	
	/**
	 * Prepares the renderer by binding the UI Texture Frame Buffer Object.
	 */
	public void startRendering() {
		fbo.bindFrameBuffer();
	}
	
	/**
	 * Finalizes UI rendering by releasing the UI Texture Frame Buffer Object.
	 */
	public void stopRendering() {
		fbo.unbindFrameBuffer();
	}
	
	/**
	 * Renders a single progress bar.
	 */
	public void renderProgressBar(ComponentRenderData bar, float progress, float maxProgress) {
		progressBarShader.start();
		progressBarShader.uploadBorderWidth(bar.getBorderWidth());
		progressBarShader.uploadProgress(progress / maxProgress);
		GL30.glBindVertexArray(bar.getShape().getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		// TODO only uses one texture right now
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, bar.getTexture().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, bar.getTexture().getTextureID());
		progressBarShader.uploadTextures(0, 1);
		progressBarShader.uploadTransformation(bar.getTranslation());
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, bar.getShape().getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
		progressBarShader.stop();
	}
	
	/**
	 * Renders a single UI element.
	 */
	public void renderComponent(ComponentRenderData element) {
		uiShader.start();
		uiShader.uploadBorderWidth(element.getBorderWidth());
		GL30.glBindVertexArray(element.getShape().getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, element.getTexture().getTextureID());
		uiShader.uploadTexture(0);
		final Matrix4fc conv = element.getTranslation();
		Logger.INFO.log(conv.getTranslation(new Vector3f()).toString());
		uiShader.uploadTransformation(conv);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, element.getShape().getVertexCount());
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
		uiShader.stop();
	}
	
	/**
	 * Erases the specified area by overwriting it with a fully transparent texture.
	 */
	public void eraseComponent(ComponentRenderData element) {
		uiShader.start();
		// TODO overwrite with generic transparent texture from resource manager
		uiShader.stop();
	}
	
	public int getUITexture() {
		return fbo.getTextureID();
	}
	
}
