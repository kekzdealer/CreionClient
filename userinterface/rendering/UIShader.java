package rendering;

import org.joml.Matrix4fc;

import graphics.ShaderProgram;

public class UIShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "userinterface.txt";
	private static final String FRAGMENT_FILE = "userinterface.txt";
	
	private int location_diffuseMap;
	private int location_transformation;
	private int location_borderWidth;
	
	public UIShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
		getAllUniformLocations();
	}
	
	@Override
	public void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "texCoord");
	}

	@Override
	protected void getAllUniformLocations() {
		location_diffuseMap = super.getUniformLocation("diffuse");
		location_transformation = super.getUniformLocation("transformation");
		location_borderWidth = super.getUniformLocation("borderWidth");
	}
	
	
	public void uploadTexture(int textureBank) {
		super.loadInt(location_diffuseMap, textureBank);
	}

	public void uploadTransformation(Matrix4fc transformation) {
		super.loadMatrix4f(location_transformation, transformation);
	}
	
	public void uploadBorderWidth(float borderWidth) {
		super.loadFloat(location_borderWidth, borderWidth);
	}
}
