package rendering;

import org.joml.Matrix4fc;

import graphics.ShaderProgram;

public class ProgressBarShader extends ShaderProgram {

	private static final String VERTEX_FILE = "progressbar.txt";
	private static final String FRAGMENT_FILE = "progressbar.txt";
	
	private int location_diffuseMapB;
	private int location_diffuseMapF;
	private int location_transformation;
	private int location_progress;
	private int location_borderWidth;
	
	public ProgressBarShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "texCoord");
	}

	@Override
	protected void getAllUniformLocations() {
		location_diffuseMapB = super.getUniformLocation("diffuse_b");
		location_diffuseMapF = super.getUniformLocation("diffuse_f");
		location_transformation = super.getUniformLocation("transformation");
		location_progress = super.getUniformLocation("progress");
		location_borderWidth = super.getUniformLocation("borderWidth");
	}
	
	public void uploadTextures(int background, int foreground) {
		super.loadInt(location_diffuseMapB, background);
		super.loadInt(location_diffuseMapF, foreground);
	}
	
	public void uploadTransformation(Matrix4fc transformation) {
		super.loadMatrix4f(location_transformation, transformation);
	}
	
	public void uploadProgress(float progress) {
		super.loadFloat(location_progress, progress);
	}
	
	public void uploadBorderWidth(float borderWidth) {
		super.loadFloat(location_borderWidth, borderWidth);
	}

}
