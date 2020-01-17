package components;

import graphics.ShapeFactory;
import graphics.TextureFactory;

import components.Component;
import ui.ComponentRenderData;

public class Window extends Component {
	
	private static final String TEXTURE_BODY = "window_backdrop";
	
	public Window() {
		super();
	}
	
	@Override
	public ComponentRenderData getComponentRenderData() {
		if(super.renderDataCache != null) {
			super.markClean();
			return super.renderDataCache;
		} else {
			
			final ShapeFactory sf = ShapeFactory.getInstance();
			final TextureFactory tf = TextureFactory.getIntance();
			
			final ComponentRenderData data = new ComponentRenderData(
					sf.createQuad(super.getWidth(), super.getHeight()),
					tf.createTexture(TEXTURE_BODY),
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
