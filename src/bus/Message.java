package bus;

public class Message {
	
	private final Recipients[] recipients;
	private final int behaviorID;
	private final Object[] args;

	private boolean complete = false;
	
	public Message(Recipients recipient, int behaviorID, Object... args) {
		this.recipients = new Recipients[1];
		recipients[0] = recipient;
		this.behaviorID = behaviorID;
		this.args = args;
	}
	
	public Message(Recipients[] recipients, int behaviorID, Object... args) {
		this.recipients = recipients;
		this.behaviorID = behaviorID;
		this.args = args;
	}
	
	public Recipients getRecipient() {
		return recipients[0];
	}
	
	public Recipients[] getRecipients() {
		return recipients;
	}
	
	public int getBehaviorID() {
		return behaviorID;
	}
	
	public Object[] getArgs() {
		return args;
	}
	
	public void setComplete() {
		complete = true;
	}
	public boolean isComplete() {
		return complete;
	}
}
