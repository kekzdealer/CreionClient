package components;

import java.util.HashSet;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector2f;
import org.joml.Vector2fc;

import components.Component;
import ui.ComponentRenderData;

/**
 * Parent class of all UI Components. 
 * Children have to call markDirty() in all of their specific 
 * setters for changes to be correctly displayed.
 * 
 * @author kekzdealer
 *
 */
public abstract class Component {
	
	// Position relative to parent
	private float xPos = 0.0f;
	private float yPos = 0.0f;
	// Absolute UI Layer (-127 to 127)
	private byte layer = 0;
	// Absolute dimensions
	private float width = 1.0f;
	private float height = 1.0f;
	private float borderWidth = 0.0f;
	// Component render flags
	private boolean visible = false;
	private boolean dirty = true;
	// Child components
	private final HashSet<Component> children = new HashSet<>();
	// Render data cache
	protected ComponentRenderData renderDataCache = null;
	
	public Component() {
		
	}
	
	public void add(Component component) {
		children.add(component);
		markDirty();
	}
	
	public void remove(Component component) {
		children.remove(component);
		markDirty();
	}
	
	public HashSet<Component> getChildren() {
		return children;
	}
	
	/**
	 * Accumulate all the required render data in this method.
	 * Marks the component as clean so it will not be re-rendered
	 * unless marked dirty.
	 * @return Render Data
	 */
	public abstract ComponentRenderData getComponentRenderData();
	
	/**
	 * Helper method to make positioning of child elements more intuitive. </br>
	 * Unlike what is usual for UI-frameworks, OpenGL puts the origin of a planar shape
	 * at the bottom left, instead of the top left. This method does that conversion 
	 * automatically. </br>
	 * The parent component transformation is also automatically added.
	 * @param component
	 * 			Child to position
	 */
	protected Matrix4fc positionComponent(Component component) {
		final Matrix4f transform = new Matrix4f();
		// Make relative to parent
		transform.translate(xPos, yPos, layer);
		if(!component.equals(this)) {
			// Encode offset to this component
			transform.translate(
					component.getPosition().x(), 
					component.getPosition().y() + component.getHeight(), 
					component.getLayer() - this.getLayer());			
		}
		return transform;
	}
	
	public Component setPosition(float x, float y) {
		xPos = x;
		yPos = y;
		markDirty();
		return this;
	}
	
	public Vector2fc getPosition() {
		return new Vector2f(xPos, yPos);
	}
	
	public Component setLayer(byte layer) {
		this.layer = layer;
		markDirty();
		return this;
	}
	
	public byte getLayer() {
		return layer;
	}
	
	public Component setSize(float width, float height) {
		this.width = width;
		this.height = height;
		markDirty();
		return this;
	}
	
	public Vector2fc getSize() {
		return new Vector2f(width, height);
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public Component setBorderWidth(float borderWidth) {
		this.borderWidth = borderWidth;
		return this;
	}
	
	public float getBorderWidth() {
		return borderWidth;
	}
	
	public Component setVisible(boolean visible) {
		this.visible = visible;
		for(Component child : children) {
			child.setVisible(visible);
		}
		markDirty();
		return this;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public Component markClean() {
		this.dirty = false;
		return this;
	}
	
	public Component markDirty() {
		this.dirty = true;
		return this;
	}
	
	public boolean isDirty() {
		return dirty;
	}
	
}
