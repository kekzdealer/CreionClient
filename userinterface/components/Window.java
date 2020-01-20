
package components;

import components.Component;
import ui.ComponentRenderData;

public class Window extends Component {
	
	private static final String TEXTURE_BODY = "window_background";
	
	public Window() {
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
					rm.loadCachedTexture(TEXTURE_BODY),
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
	
}
