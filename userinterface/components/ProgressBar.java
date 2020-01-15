package components;

import graphics.ShapeFactory;
import graphics.TextureFactory;
import components.Component;
import ui.ComponentElement;
import ui.ComponentRenderData;

public class ProgressBar extends Component {
	
	private static final String TEXTURE_BACKGROUND = "progressbar_background";
	private static final String TEXTURE_FOREGROUND = "progressbar_foreground";
	
	private float borderWidth = 0.1f;
	private float progress = 0.0f;
	private float progressMax = 100.0f;
	
	public ProgressBar() {
		super();
	}
	
	@Override
	public ComponentRenderData getComponentRenderData() {
		if(super.renderDataCache != null) {
			return super.renderDataCache;
		} else {
			final ComponentElement body = new ComponentElement(
					super.getWidth(), super.getHeight());
			
			final ShapeFactory sf = ShapeFactory.getInstance();
			final TextureFactory tf = TextureFactory.getIntance();
			
			body.setShape(sf.createQuad(body.getWidth(), body.getHeight()));
			super.positionElement(body, 0.0f, 0.0f, 0.0f);
			body.setTexture(tf.createTexture(TEXTURE_BACKGROUND));
			body.setTexture(tf.createTexture(TEXTURE_FOREGROUND));
			
			final ComponentRenderData data = new ComponentRenderData(body);
			for(Component child : super.getChildren()) {
				data.addChildren(child.getComponentRenderData());
			}
			
			super.renderDataCache = data;
			return super.renderDataCache;
		}
	}
	
	public ProgressBar setBorderWidth(float borderWidth) {
		this.borderWidth = borderWidth;
		super.markDirty();
		return this;
	}
	
	public float getBorderWidth() {
		return borderWidth;
	}
	
	public ProgressBar setProgress(float progress, float progressMax) {
		this.progress = progress;
		this.progressMax = progressMax;
		super.markDirty();
		return this;
	}
	
	public float getProgress() {
		return progress;
	}
	
	public float getProgressMax() {
		return progressMax;
	}
	
}
