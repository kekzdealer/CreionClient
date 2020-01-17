package ui;

import java.util.HashSet;

import components.ProgressBar;
import rendering.UIRenderer;
import components.Component;

public class UIManager {
	
	private final HashSet<Component> components = new HashSet<>();
	private final UIRenderer renderer;
		
	public UIManager(int width, int height) {
		renderer = new UIRenderer(width, height);
	}
	
	public void cleanUp() {
		// TODO implement UIManager.cleanUp()
	}
	
	/**
	 * Register an UI Component with the UI Manager.
	 */
	public void addComponent(Component component) {
		components.add(component);
	}
	
	/**
	 * Call to update the UI texture for the next frame
	 */
	public void render() {
		//renderer.startRendering();
		for(Component component : components) {
			// "Clean" components are not updated.
			if(component.isDirty()) {
				// Newly hidden components are erased
				if(!component.isVisible()) {
					renderer.eraseArea(null); // TODO
					continue;
				} else {
					// ProgressBars requires a special shader
					if(component instanceof ProgressBar) {
						renderer.renderProgressBar((ProgressBar) component);
					} else {
						// Gathers all elements and child component's elements
						final HashSet<ComponentElement> elements = 
								component.getComponentRenderData().getElements();
						for(ComponentElement element : elements) {
							renderer.renderElement(element);
						}
					}
				}
			}
		}
		//renderer.stopRendering();
	}
	
	/**
	 * Call to retrieve the UI texture id
	 */
	public int getUITexture() {
		return renderer.getUITexture();
	}
}
