package components;

import graphics.Texture;
import ui.ComponentRenderData;

public class Icon extends Component {
	
	private Texture icon;
	
	private String iconName;
	
	public Icon(String iconName) {
		super();
		this.iconName = iconName;
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
					rm.loadCachedTexture(iconName),
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

	public Icon setIcon(Texture icon) {
		this.icon = icon;
		return this;
	}
	
	public Texture getIcon() {
		return icon;
	}
}
