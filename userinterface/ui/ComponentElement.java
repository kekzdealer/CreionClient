package ui;

import java.util.HashSet;
import java.util.Iterator;

import org.joml.Matrix4fc;

import graphics.Shape;
import graphics.Texture;

public class ComponentElement {
	
	private Shape shape;
	private final HashSet<Texture> textures = new HashSet<>();
	private final HashSet<Matrix4fc> transforms = new HashSet<>();
	private final float width;
	private final float height;
	
	public ComponentElement(float width, float height) {
		this.width = width;
		this.height = height;
	}

	public void setShape(Shape shape) {
		this.shape = shape;
	}
	
	public void setTexture(Texture texture) {
		textures.clear();
		textures.add(texture);
	}
	
	public void addTexture(Texture texture) {
		textures.add(texture);
	}
	
	public void setTransforms(Matrix4fc... transform) {
		for(Matrix4fc m : transform) {
			transforms.add(m);
		}
	}
	
	public void addTransform(Matrix4fc transform) {
		transforms.add(transform);
	}
	
	public Shape getShape() {
		return shape;
	}

	public HashSet<Matrix4fc> getTransforms() {
		return transforms;
	}

	public Texture getTexture() {
		final Iterator<Texture> i = textures.iterator();
		return i.next();
	}
	
	public HashSet<Texture> getTextures(){
		return textures;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
}
