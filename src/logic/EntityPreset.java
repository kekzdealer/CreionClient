package logic;

public class EntityPreset {
	
	public String name;
	public String sprite;
	
	public EntityPreset(String name, String sprite) {
		this.name = name;
		this.sprite = sprite;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
