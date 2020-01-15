package tags;

import org.joml.Matrix4f;

import tags.Component;

public class CTransformation extends Component {
	
	private final Matrix4f transformation = new Matrix4f();
	
	public Matrix4f get() {
		return transformation;
	}
}
