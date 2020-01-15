package logic;

import java.util.HashMap;
import java.util.HashSet;

import tags.CDurability;
import tags.CRender;
import tags.CTransformation;
import tags.Component;

public class EntityDatabase {
	
	public enum ComponentType {
		RENDER,
		TRANSFORMATION,
		DURABILITY
	}
	
	private final HashMap<Integer, HashSet<ComponentType>> entities = new HashMap<>();
	
	private final HashMap<Integer, CRender> renderComponents = new HashMap<>();
	private final HashMap<Integer, CTransformation> transformationComponents = new HashMap<>();
	private final HashMap<Integer, CDurability> durabilityComponents = new HashMap<>();
	
	public int generateEntity(HashSet<ComponentType> tags) {
		int i = 0;
		while(entities.containsKey(i)){
			i++;
		}
		entities.put(i, tags);
		return i;
	}
	
	public void deleteEntity(int eID) {
		renderComponents.remove(eID);
		transformationComponents.remove(eID);
		durabilityComponents.remove(eID);
		
		entities.remove(eID);
	}
	
	public int getEntityCount() {
		return entities.size();
	}
	
	public void addComponent(int eID, ComponentType type, Component component) {
		switch(type) {
		case RENDER: renderComponents.put(eID, (CRender) component); break;
		case TRANSFORMATION: transformationComponents.put(eID, (CTransformation) component); break;
		case DURABILITY: durabilityComponents.put(eID, (CDurability) component);
		};
	}
	
	public CRender getRenderComponent(int eID) {
		return renderComponents.get(eID);
	}
	
	public CTransformation getTransformationComponent(int eID) {
		return transformationComponents.get(eID);
	}
	
	public CDurability getDurabilityComponent(int eID) {
		return durabilityComponents.get(eID);
	}
	
}
