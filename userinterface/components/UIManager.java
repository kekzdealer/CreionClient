package components;

import java.util.HashSet;

import graphics.Shape;
import rendering.UIRenderer;
import ui.ComponentRenderData;
import utility.Logger;

public class UIManager {
	
	private final HashSet<Component> components = new HashSet<>();
	private final ResourceManager resourceManager = ResourceManager.getInstance();
	private final UIRenderer renderer;
		
	public UIManager(int width, int height) {
		renderer = new UIRenderer(resourceManager, width, height);
	}
	
	public void cleanUp() {
		// TODO implement UIManager.cleanUp()
		renderer.destroy();
		resourceManager.close();
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
		renderer.startRendering();
		for(Component component : components) {
			// "Clean" components are not updated.
			if(component.isDirty()) {
				// Newly hidden components are erased
				if(!component.isVisible()) {
					for(ComponentRenderData element : component.getComponentRenderData().withChildDataAsSet()) {
						Logger.INFO.log("Erasing a component");
						renderer.eraseComponent(element);
						component.markClean();
					}
					continue;
				} else {
					// ProgressBars requires a special shader
					if(component instanceof ProgressBar) {
						final ProgressBar p = (ProgressBar) component;
						renderer.renderProgressBar(component.getComponentRenderData(), 
								p.getProgress(), p.getProgressMax());
					} else {
						for(ComponentRenderData element : component.getComponentRenderData().withChildDataAsSet()) {
							Logger.INFO.log("Rendering a component");
							renderer.renderComponent(element);
						}
					}
				}
			}
		}
		renderer.stopRendering();
	}
	
	/**
	 * Call to retrieve the UI texture id
	 */
	public int getUITexture() {
		return renderer.getUITexture();
	}
	
	/**
	 * Call to retrieve the UI carrier shape object
	 */
	public Shape getUICarrier() {
		return resourceManager.getGUICarrier();
	}
}
