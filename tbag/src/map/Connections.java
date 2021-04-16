package map;

import java.util.HashMap;

public class Connections {
	private HashMap<String, Room> connectionsMap;
	private int connectionID;
	
	public Connections(int roomID) {
		this.connectionsMap = new HashMap<String, Room>();
		this.connectionID = roomID;
	}
	
	public void addConnection(String direction, Room destination) {
		connectionsMap.put(direction, destination);
	}
	
	public boolean hasConnection(String direction) {
		return connectionsMap.containsKey(direction);
	}
	
	public Room getConnection(String direction) {
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

	public HashMap<String, Room> getConnectionsMap() {
		return connectionsMap;
	}
}
