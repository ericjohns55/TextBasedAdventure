package cs320.windows98.tbag.connection;

import java.util.HashMap;
import cs320.windows98.tbag.room.Room;


// connections: HashMap<String, Room>
// getConnections(): HashMap<String, Room>

public class Connection{
	
	
	HashMap<String, Room> ConnectionMap = new HashMap<String, Room>();
	String string;
	Room room;
	
	
	
	public Connection(String string, Room room){
		
		this.room = room;
		this.string = string;
		
		
		ConnectionMap.put(this.string, this.room);
		
	}
	
	
	public HashMap<String, Room> getConnections() {
		
		return ConnectionMap;
		
	}
	
	
}
