package graphics;

import java.io.BufferedReader;

import java.io.IOException;

import org.joml.Matrix4fc;
import org.joml.Vector2fc;
import org.joml.Vector3fc;
import org.joml.Vector4fc;
import org.lwjgl.opengl.GL20C;

import utility.File;

public  abstract class ShaderProgram{
	
	private static final String SHADER_LOC = "/shaders/";
	
	private int programID;
	
	public ShaderProgram(String vertexFile, String fragmentFile){
		programID = buildShaderProgram("v_" + vertexFile, "f_" + fragmentFile);
	}
	
	private int buildShaderProgram(String vShader, String fShader){
		int vShaderID;
		int fShaderID;
		int shaderProgramID;
		// CREATE VERTEX SHADER
		vShaderID = GL20C.glCreateShader(GL20C.GL_VERTEX_SHADER);
		GL20C.glShaderSource(vShaderID, readShaderFile(vShader));
		GL20C.glCompileShader(vShaderID);
		if(GL20C.glGetShaderi(vShaderID, GL20C.GL_COMPILE_STATUS) != 1){
			System.err.println(GL20C.glGetShaderInfoLog(vShaderID));
			System.exit(1);
		}
		
		// CREATE FRAGMENT SHADER
		fShaderID = GL20C.glCreateShader(GL20C.GL_FRAGMENT_SHADER);
		GL20C.glShaderSource(fShaderID, readShaderFile(fShader));
		GL20C.glCompileShader(fShaderID);
		if(GL20C.glGetShaderi(fShaderID, GL20C.GL_COMPILE_STATUS) != 1){
			System.err.println(GL20C.glGetShaderInfoLog(fShaderID));
			System.exit(1);
		}
		
		// LINK SHADER PROGRAM
		shaderProgramID = GL20C.glCreateProgram();
		GL20C.glAttachShader(shaderProgramID, vShaderID);
		GL20C.glAttachShader(shaderProgramID, fShaderID);
		
		bindAttributes();
		
		GL20C.glLinkProgram(shaderProgramID);
		if(GL20C.glGetProgrami(shaderProgramID, GL20C.GL_LINK_STATUS) != 1){
			System.err.println(GL20C.glGetProgramInfoLog(shaderProgramID));
			System.exit(1);
		}
		GL20C.glDeleteShader(vShaderID);
		GL20C.glDeleteShader(fShaderID);
		return shaderProgramID;
	}
	
	private String readShaderFile(String fileName){
		StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new File(SHADER_LOC +fileName).getReader())){
			String line;
			while((line = br.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
		}
		catch(IOException e){
			System.err.println("Error while reading shader file: " +fileName);
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	
	protected abstract void bindAttributes();
	protected abstract void getAllUniformLocations();
	
	protected int getUniformLocation(String uniformName){
		return GL20C.glGetUniformLocation(programID, uniformName);
	}
	
	protected void bindAttribute(int attribute, String variableName){
		GL20C.glBindAttribLocation(programID, attribute, variableName);
	}
	
	public void start(){
		GL20C.glUseProgram(programID);
	}
	
	public void stop(){
		GL20C.glUseProgram(0);
	}
	
	protected void loadInt(int location, int value){
		GL20C.glUniform1i(location, value);
	}
	
	protected void loadFloat(int location, float value){
		GL20C.glUniform1f(location, value);
	}
	
	protected void loadVector2f(int location, Vector2fc vector){
		GL20C.glUniform2f(location, vector.x(), vector.y());
	}
	
	protected void loadVector3f(int location, Vector3fc vector){
		GL20C.glUniform3f(location, vector.x(), vector.y(), vector.z());
	}
	
	protected void loadVector4f(int location, Vector4fc vector){
		GL20C.glUniform4f(location, vector.x(), vector.y(), vector.z(), vector.w());
	}
	
	protected void loadMatrix4f(int location, Matrix4fc matrix){
		final float[] matrixBuffer4f = new float[16];
		matrix.get(matrixBuffer4f);
		GL20C.glUniformMatrix4fv(location, false, matrixBuffer4f);
	}
	
}
