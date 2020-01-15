package ui;

import java.util.HashSet;
import java.util.Iterator;

public class ComponentRenderData {
	
	private final HashSet<ComponentElement> elements = new HashSet<>();
	private final HashSet<ComponentRenderData> children = new HashSet<>();
	
	public ComponentRenderData(ComponentElement... element) {
		for(ComponentElement e : element) {
			elements.add(e);
		}
	}
	
	public void addChildren(ComponentRenderData child) {
		children.add(child);
	}
	
	public HashSet<ComponentRenderData> getChildren(){
		return children;
	}
	
	public boolean hasChildren() {
		return (children.size() == 0) ? false : true;
	}
	
	/**
	 * Recursively accumulate all sub-components render elements.
	 */
	private void elementAccumulator(HashSet<ComponentElement> acc, ComponentRenderData child) {
		if(child.hasChildren()) {
			acc.addAll(elements);
			for(ComponentRenderData c : child.getChildren()) {
				elementAccumulator(acc, c);
			}
		}
	}
	
	/**
	 * Returns this components render elements, as well as all it's children's
	 * render elements.
	 */
	public HashSet<ComponentElement> getElements() {
		final HashSet<ComponentElement> result = new HashSet<>();
		for(ComponentRenderData child : children) {
			elementAccumulator(result, child);
		}
		return elements;
	}
	
	/**
	 * Returns only the first render element and ignores potential children's data. </br>
	 * Used for progress bars.
	 */
	public  ComponentElement getElement() {
		final Iterator<ComponentElement> i = elements.iterator();
		return i.next();
	}
}
