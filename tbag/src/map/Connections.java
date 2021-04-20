package map;

import java.util.HashMap;

public class Connections {
	private HashMap<String, Integer> connectionsMap;
	private int connectionID;
	private int destinationID;
	private String direction;
	
	public Connections(int roomID) {
		this.connectionsMap = new HashMap<String, Integer>();
		this.connectionID = roomID;
	}
	
	public void addConnection(String direction, int destination) {
		connectionsMap.put(direction, destination);
		
		this.destinationID = destination;
		this.direction = direction;
	}
	
	public boolean hasConnection(String direction) {
		return connectionsMap.containsKey(direction);
	}
	
	public int getConnection(String direction) {
		return connectionsMap.get(direction);
	}
	
	public boolean removeConnection(String direction) {
		if (hasConnection(direction)) {
			connectionsMap.remove(direction);
			return true;
		}
		
		return false;
	}

	public int getConnectionID() {
		return connectionID;
	}

	public void setConnectionID(int connectionID) {
		this.connectionID = connectionID;
	}

	public HashMap<String, Integer> getConnectionsMap() {
		return connectionsMap;
	}

	public int getDestinationID() {
		return destinationID;
	}

	public void setDestinationID(int destinationID) {
		this.destinationID = destinationID;
	}

	public String getDirection() {
		return direction != null ? direction : "";
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}
}
