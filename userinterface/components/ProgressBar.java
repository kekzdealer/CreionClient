package components;

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
			super.markClean();
			return super.renderDataCache;
		} else {
			
			final ResourceManager rm = ResourceManager.getInstance();
			final ComponentRenderData data = new ComponentRenderData(
					rm.createCarrier(super.getWidth(), super.getHeight()),
					rm.loadCachedTexture(TEXTURE_BACKGROUND),
					super.getBorderWidth(),
					super.positionComponent(this));
			
			for(Component child : super.getChildren()) {
				data.addChildren(child.getComponentRenderData());
			}
			
			super.renderDataCache = data;
			super.markClean();
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
