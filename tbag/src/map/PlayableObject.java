package map;

public class PlayableObject extends RoomObject {
	private char[] requiredNotes;
	private String playedNotes;
	private boolean isInstrument;
	
	public PlayableObject(String name, String description, String direction, String requiredNotes, boolean isInstrument, int roomID) {
		super(name, description, direction, false, false, false, roomID);
		this.setInteractable(true);
		this.playedNotes = "";
		this.isInstrument = isInstrument;
		this.requiredNotes = requiredNotes.toCharArray();
	}

	public char[] getRequiredNotes() {
		return requiredNotes;
	}

	public void setRequiredNotes(String requiredNotes) {
		this.requiredNotes = requiredNotes.toCharArray();
	}

	public String getPlayedNotes() {
		return playedNotes;
	}

	public void setPlayedNotes(String playedNotes) {
		this.playedNotes = playedNotes;
	}
	
	public boolean isInstrument() {
		return isInstrument;
	}
	
	public void setInstrument(boolean isInstrument) {
		this.isInstrument = isInstrument;
	}
	
	public boolean playedPassage() {
		boolean playedPassage = true;
		
		for (int i = 0; i < requiredNotes.length; i++) {
			if (!playedNotes.toUpperCase().contains(Character.toString(requiredNotes[i]))) {
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
