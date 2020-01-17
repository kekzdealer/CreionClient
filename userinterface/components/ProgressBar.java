package components;

import graphics.ShapeFactory;
import graphics.TextureFactory;
import components.Component;
import ui.ComponentRenderData;

public class ProgressBar extends Component {
	
	private static final String TEXTURE_BACKGROUND = "progressbar_background";
	private static final String TEXTURE_FOREGROUND = "progressbar_foreground";
	
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
			
			final ShapeFactory sf = ShapeFactory.getInstance();
			final TextureFactory tf = TextureFactory.getIntance();
			
			final ComponentRenderData data = new ComponentRenderData(
					sf.createQuad(super.getWidth(), super.getHeight()),
					tf.createTexture(TEXTURE_BACKGROUND),
					super.getBorderWidth(),
					super.positionChildComponent(this));
			
			for(Component child : super.getChildren()) {
				data.addChildren(child.getComponentRenderData());
			}
			
			super.renderDataCache = data;
			return super.renderDataCache;
		}
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
