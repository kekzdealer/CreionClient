package components;

import graphics.ShapeFactory;
import graphics.TextureFactory;
import components.Component;
import ui.ComponentRenderData;

public class Button extends Component {
	
	private static final String TEXTURE_BODY = "button";
	
	@Override
	public ComponentRenderData getComponentRenderData() {
		if(super.renderDataCache != null) {
			return super.renderDataCache;
		} else {
			
			final ShapeFactory sf = ShapeFactory.getInstance();
			final TextureFactory tf = TextureFactory.getIntance();
			
			final ComponentRenderData data = new ComponentRenderData(
					sf.createQuad(super.getWidth(), super.getHeight()),
					tf.createTexture(TEXTURE_BODY),
					super.getBorderWidth());
			
			for(Component child : super.getChildren()) {
				data.addChildren(child.getComponentRenderData());
			}
			
			super.renderDataCache = data;
			return super.renderDataCache;
		}
	}
	
}
