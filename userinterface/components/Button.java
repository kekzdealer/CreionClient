package components;

import graphics.ShapeFactory;
import graphics.TextureFactory;
import components.Component;
import ui.ComponentElement;
import ui.ComponentRenderData;

public class Button extends Component {
	
	private static final String TEXTURE_BODY = "button";
	
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
			body.setTexture(tf.createTexture(TEXTURE_BODY));
			
			final ComponentRenderData data = new ComponentRenderData(body);
			for(Component child : super.getChildren()) {
				data.addChildren(child.getComponentRenderData());
			}
			
			super.renderDataCache = data;
			return super.renderDataCache;
		}
	}
	
}
