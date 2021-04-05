package map;

public class PlayableObject extends RoomObject {
	private String[] requiredNotes;
	private String playedNotes;
	
	public PlayableObject(String name, String description, String direction, String[] requiredNotes) {
		super(name, description, direction, false, false, false);
		this.setInteractable(true);
		this.requiredNotes = requiredNotes;
		this.playedNotes = "";
	}

	public String[] getRequiredNotes() {
		return requiredNotes;
	}

	public void setRequiredNotes(String[] requiredNotes) {
		this.requiredNotes = requiredNotes;
	}

	public String getPlayedNotes() {
		return playedNotes;
	}

	public void setPlayedNotes(String playedNotes) {
		this.playedNotes = playedNotes;
	}
	
	public boolean playedPassage() {
		boolean playedPassage = true;
		
		for (String note : requiredNotes) {
			if (!playedNotes.toUpperCase().contains(note)) {
				return false;
			}
		}
		
		return playedPassage;
	}
	
	public boolean playNote(String entry) {
		char[] split = entry.toUpperCase().toCharArray();
		
		for (char character : split) {
			if (character > 71 || character < 65) {
				return false;
			}
		}
		
		playedNotes += entry;
		
		return true;
	}
}
