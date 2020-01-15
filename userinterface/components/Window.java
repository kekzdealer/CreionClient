package components;

import graphics.ShapeFactory;
import graphics.TextureFactory;
import components.Component;
import ui.ComponentRenderData;
import ui.ComponentElement;

public class Window extends Component {
	
	private static final String TEXTURE_BODY = "window_backdrop";
	private static final String TEXTURE_CORNER = "window_border_corner";
	private static final String TEXTURE_HORIZONTAL_EDGE = "window_horizontal_border_edge";
	private static final String TEXTURE_VERTICAL_EDGE = "window_vertical_border_edge";
	
	private float borderWidth = 0.1f;
	
	public Window() {
		super();
	}
	
	@Override
	public ComponentRenderData getComponentRenderData() {
		if(super.renderDataCache != null) {
			return super.renderDataCache;
		} else {
			// Dimensions
			final ComponentElement body = new ComponentElement(
					super.getWidth() - borderWidth * 2.0f,
					super.getHeight() - borderWidth * 2.0f);
			final ComponentElement corners = new ComponentElement(borderWidth, borderWidth);
			final ComponentElement horizontalEdges = new ComponentElement(
					super.getWidth() - borderWidth * 2.0f,
					borderWidth);
			final ComponentElement verticalEdges = new ComponentElement(
					borderWidth,
					super.getHeight() - borderWidth * 2.0f);
			
			final ShapeFactory sf = ShapeFactory.getInstance();
			final TextureFactory tf = TextureFactory.getIntance();
			
			// Shapes
			body.setShape(sf.createQuad(body.getWidth(), body.getHeight()));
			corners.setShape(sf.createQuad(corners.getWidth(), corners.getHeight()));
			horizontalEdges.setShape(sf.createQuad(horizontalEdges.getWidth(), horizontalEdges.getHeight()));
			verticalEdges.setShape(sf.createQuad(verticalEdges.getWidth(), verticalEdges.getHeight()));
			
			// Parent-relative transforms
			super.positionElement(body, borderWidth, borderWidth, 0.0f);
			// LT, LB, RB, RT
			super.positionElement(corners, 0.0f, borderWidth, 0.0f);
			super.positionElement(corners, 0.0f, corners.getHeight() - borderWidth, 0.0f);
			super.positionElement(corners, super.getWidth() - borderWidth, super.getHeight() - borderWidth, 0.0f);
			super.positionElement(corners, super.getWidth() - borderWidth, borderWidth, 0.0f);
			super.positionElement(horizontalEdges, 0.0f, borderWidth, 0.0f);
			super.positionElement(horizontalEdges, super.getWidth() - borderWidth, borderWidth, 0.0f);
			super.positionElement(verticalEdges, borderWidth, 0.0f, 0.0f);
			super.positionElement(verticalEdges, borderWidth, super.getHeight() - borderWidth, 0.0f);
			
			// Textures
			body.setTexture(tf.createTexture(TEXTURE_BODY));
			corners.setTexture(tf.createTexture(TEXTURE_CORNER));
			horizontalEdges.setTexture(tf.createTexture(TEXTURE_HORIZONTAL_EDGE));
			verticalEdges.setTexture(tf.createTexture(TEXTURE_VERTICAL_EDGE));
			
			// Finalize
			final ComponentRenderData data = new ComponentRenderData(
					body, corners, horizontalEdges, verticalEdges);
			for(Component child : super.getChildren()) {
				data.addChildren(child.getComponentRenderData());
			}
			super.renderDataCache = data;
			return super.renderDataCache;
		}
	}
	
	public Window setBorderWidth(float borderWidth) {
		this.borderWidth = borderWidth;
		return this;
	}
}
