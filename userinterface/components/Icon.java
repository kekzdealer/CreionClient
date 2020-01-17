package components;

import graphics.ShapeFactory;
import graphics.Texture;
import graphics.TextureFactory;
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
			return super.renderDataCache;
		} else {
			
			final ShapeFactory sf = ShapeFactory.getInstance();
			final TextureFactory tf = TextureFactory.getIntance();
			
			final ComponentRenderData data = new ComponentRenderData(
					sf.createQuad(super.getWidth(), super.getHeight()),
					tf.createTexture(iconName),
					super.getBorderWidth(),
					super.positionComponent(this));
			
			for(Component child : super.getChildren()) {
				data.addChildren(child.getComponentRenderData());
			}
			
			super.renderDataCache = data;
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
