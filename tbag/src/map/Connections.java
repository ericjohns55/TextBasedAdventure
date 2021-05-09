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
	
	// add connection to map
	public void addConnection(String direction, int destination) {
		connectionsMap.put(direction, destination);
		
		this.destinationID = destination;
		this.direction = direction;
	}
	
	public boolean hasConnection(String direction) {
		return connectionsMap.containsKey(direction);	// check if it exists
	}
	
	public int getConnection(String direction) {
		if (hasConnection(direction)) {
			return connectionsMap.get(direction);	// return the connection if it exists, -1 if not
		} else {
			return -1;
		}
	}
	
	public boolean removeConnection(String direction) {
		if (hasConnection(direction)) {
			connectionsMap.remove(direction);	// take connection out of the map
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
