package tags;

import tags.Component;
import graphics.Shape;
import graphics.Texture;

public class CRender extends Component {
	
	private final Shape shape;
	private final Texture texture;
	
	public CRender(Shape shape, Texture texture) {
		this.shape = shape;
		this.texture = texture;
	}

	public Shape getShape() {
		return shape;
	}

	public Texture getTexture() {
		return texture;
	}
	
}
