package graphics;

public class Shape {
	
	private final int vaoID;
	private final int vertexCount;
	private final int[] vertexBufferObjectIDs;
	
	public Shape(int vaoID, int vertexCount, int... vertxBufferObjectID) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
		this.vertexBufferObjectIDs = vertxBufferObjectID;
	}
	
	public int getVaoID() {
		return vaoID;
	}
	
	public int getVertexCount() {
		return vertexCount;
	}
	
	public int[] getVboIDs() {
		return vertexBufferObjectIDs;
	}
	
}
