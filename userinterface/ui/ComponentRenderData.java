package ui;

import java.util.HashSet;
import java.util.Set;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import graphics.Shape;
import graphics.Texture;

public class ComponentRenderData {
	
	private final HashSet<ComponentRenderData> children = new HashSet<>();
	
	private final Shape shape;
	private final Texture texture;
	private final float borderWidth;
	
	private Matrix4f translation;
	private boolean isTranslationCorrected = false;
	
	public ComponentRenderData(Shape shape, Texture texture, float borderWidth, Matrix4f parentOffset) {
		this.shape = shape;
		this.texture = texture;
		this.borderWidth = borderWidth;
		this.translation = parentOffset;
	}
	
	public Shape getShape() {
		return shape;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public float getBorderWidth() {
		return borderWidth;
	}
	
	public Matrix4f getTranslation() {
		return translation;
	}
	
	public void addParentOffset(Matrix4fc offset) {
		translation.mul(offset);
	}
	
	public void addChildren(ComponentRenderData child) {
		children.add(child);
	}
	
	private Set<ComponentRenderData> getChildren(){
		return children;
	}
	
	public boolean hasChildren() {
		return (children.size() == 0) ? false : true;
	}
	
	private void accumulate(Set<ComponentRenderData> acc, ComponentRenderData child) {
		// Calculate child's translation relative to it's parent
		if(!child.isTranslationCorrected) {
			child.addParentOffset(translation);
			child.isTranslationCorrected = true;
		}
		
		acc.add(child);
		
		if(child.hasChildren()) {
			for(ComponentRenderData c : child.getChildren()) {				
				c.accumulate(acc, c);
			}
		}
	}
	
	public Set<ComponentRenderData> withChildDataAsSet(){
		final HashSet<ComponentRenderData> acc = new HashSet<>();
		acc.add(this);
		for(ComponentRenderData child : children) {
			accumulate(acc, child);			
		}
		return acc;
	}
}
