package ui;

import java.util.HashSet;
import java.util.Set;

import org.joml.Matrix4fc;

import graphics.Shape;
import graphics.Texture;

public class ComponentRenderData {
	
	private final HashSet<ComponentRenderData> children = new HashSet<>();
	
	private final Shape shape;
	private final Texture texture;
	private final float borderWidth;
	private final Matrix4fc parentOffset;
	
	public ComponentRenderData(Shape shape, Texture texture, float borderWidth, Matrix4fc parentOffset) {
		this.shape = shape;
		this.texture = texture;
		this.borderWidth = borderWidth;
		this.parentOffset = parentOffset;
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
	
	public Matrix4fc getParentOffset() {
		return parentOffset;
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
		if(child.hasChildren()) {
			for(ComponentRenderData c : child.getChildren()) {
				c.accumulate(acc, c);
			}
		}
	}
	
	public Set<ComponentRenderData> withChildDataAsSet(){
		final HashSet<ComponentRenderData> acc = new HashSet<>();
		acc.add(this);
		accumulate(acc, this);
		return acc;
	}
}
