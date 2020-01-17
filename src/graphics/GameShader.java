package graphics;

import org.joml.Matrix4fc;

/**
 * Shader for background elements.
 * <ul>
 * 	<li>No lighting</li>
 * 	<li>No animations</li>
 * </ul>
 * @author kekzdealer
 *
 */
public class GameShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "background.txt";
	private static final String FRAGMENT_FILE = "background.txt";
	
	// Texture
	private int location_diffuseMap;
	private int location_transformation;
	private int location_projection;
	
	public GameShader() {
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
		location_projection = super.getUniformLocation("projection");
	}
	
	
	public void uploadTexture(int textureBank) {
		super.loadInt(location_diffuseMap, textureBank);
	}

	public void uploadTransformation(Matrix4fc transformation) {
		super.loadMatrix4f(location_transformation, transformation);
	}
	
	public void uploadProjection(Matrix4fc projection) {
		super.loadMatrix4f(location_projection, projection);
	}
}
