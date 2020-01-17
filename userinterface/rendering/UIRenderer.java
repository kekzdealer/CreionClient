package rendering;

import java.util.Iterator;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import components.ProgressBar;
import graphics.Shape;
import graphics.Texture;
import ui.ComponentElement;
import ui.ComponentRenderData;

public class UIRenderer {
	
	private final UIShader uiShader = new UIShader();
	private final ProgressBarShader progressBarShader = new ProgressBarShader();
	
	private final UIFrameBuffer fbo = null;
	
	public UIRenderer(int width, int height) {
		//fbo = new UIFrameBuffer(width, height);
	}
	
	/**
	 * Converts from a coordinate system with an origin in the top left
	 * to one with an origin in the middle of the screen.
	 * </br>
	 * </br>
	 * 2D Graphics usually have their origin in the top left corner.
	 * The y-axis would also be facing downwards instead of upwards.
	 * Invert the y-value and offset by x = -1.0f, and y = 1.0f.
	 * @return Converted transformation matrix
	 */
	private Matrix4fc convertToOGL(Matrix4fc transform) {
		final Vector3f correctedY = transform.getTranslation(new Vector3f());
		correctedY.set(correctedY.x(), -correctedY.y(), correctedY.z());
		return new Matrix4f(transform)
				.setTranslation(correctedY)
				.translate(-1.0f, 1.0f, 0.0f);
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
	public void renderProgressBar(ProgressBar bar) {
		progressBarShader.start();
		progressBarShader.uploadBorderWidth(bar.getBorderWidth());
		progressBarShader.uploadProgress(bar.getProgress() / bar.getProgressMax());
		final ComponentElement element = bar.getComponentRenderData().getElement();
		final Iterator<Texture> tex = element.getTextures().iterator();
		
		GL30.glBindVertexArray(element.getShape().getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.next().getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.next().getTextureID());
		progressBarShader.uploadTextures(0, 1);
		for(Matrix4fc transform : element.getTransforms()) {
			progressBarShader.uploadTransformation(convertToOGL(transform));
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, element.getShape().getVertexCount());
		}
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
		progressBarShader.stop();
	}
	
	/**
	 * Renders a single UI element.
	 */
	public void renderElement(ComponentRenderData element) {
		uiShader.start();
		GL30.glBindVertexArray(element.getShape().getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, element.getTexture().getTextureID());
		uiShader.uploadTexture(0);
		for(Matrix4fc transform : element.getTransforms()) {
			uiShader.uploadTransformation(convertToOGL(transform));
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, element.getShape().getVertexCount());
		}
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
		uiShader.stop();
	}
	
	/**
	 * Erases the specified area by overwriting it with a fully transparent texture.
	 */
	public void eraseArea(Shape area) {
		uiShader.start();
		
		uiShader.stop();
	}
	
	public int getUITexture() {
		return fbo.getTextureID();
	}
	
}
