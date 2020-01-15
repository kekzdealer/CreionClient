package tags;

import tags.Component;

public class CDurability extends Component {
	
	private int durability;
	private int maxDurability;
	
	public int getDurability() {
		return durability;
	}

	public void setDurability(int durability) {
		this.durability = durability;
	}

	public int getMaxDurability() {
		return maxDurability;
	}

	public void setMaxDurability(int maxDurability) {
		this.maxDurability = maxDurability;
	}
	
	public boolean isAlive() {
		return (durability > 0) ? true : false;
	}
}
