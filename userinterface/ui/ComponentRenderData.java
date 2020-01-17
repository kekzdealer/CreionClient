package ui;

import java.util.HashSet;
import java.util.Set;

import graphics.Shape;
import graphics.Texture;

public class ComponentRenderData {
	
	private final HashSet<ComponentRenderData> children = new HashSet<>();
	
	private final Shape shape;
	private final Texture texture;
	private final float borderWidth;
	
	public ComponentRenderData(Shape shape, Texture texture, float borderWidth) {
		this.shape = shape;
		this.texture = texture;
		this.borderWidth = borderWidth;
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
	
	public Set<ComponentRenderData> getAllData(){
		final HashSet<ComponentRenderData> acc = new HashSet<>();
		accumulate(acc, this);
		return acc;
	}
}
