package cs320.windows98.tbag.connection;

import java.util.HashMap;
import cs320.windows98.tbag.room.Room;


// connections: HashMap<String, Room>
// getConnections(): HashMap<String, Room>

public class Connection{
	
	
	HashMap<String, Room> ConnectionMap = new HashMap<String, Room>();
	String string;
	Room room;
	
	
//	connection constructor = Connection(String direction, Room connectingRoom)
//			3) room has addConnection(Connection connection) method
//			when instantiating the room we manually program in the connections
	
	
	public Connection(String string, Room room){
		
		this.room = room;
		this.string = string;
		
		
		
		
	}
	
	public void setConnection(String direction, Room connectingRoom)
	{
		ConnectionMap.put(direction, connectingRoom);
	}
	
	
	public String getDirection()
	{
		return string;
	}
	
	public HashMap<String, Room> getConnections() {
		
		return ConnectionMap;
		
	}
	
	
}
