package commands;

import actor.NPC;
import actor.Player;
import game.Game;
import items.Inventory;
import map.Room;
import puzzle.Puzzle;

public abstract class UserCommand {
	private Game game;

	private String verb;
	private String noun;
	private String location;

	private Player player;
	private Room room;
	private Inventory inventory;
	private Puzzle puzzle;
	private NPC npc;

	public void loadInputandGame(Game game, String verb, String noun, String location) {

		this.game = game;

		this.verb = verb;
		this.noun = noun;
		this.location = location;

		this.player = game.getPlayer();
		this.room = player.getRoom();
		this.inventory = player.getInventory();
		this.puzzle = room.getPuzzle();
		this.npc = room.getNpc();
	}

	public abstract void execute();

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public String getVerb() {
		return verb;
	}

	public void setVerb(String verb) {
		this.verb = verb;
	}

	public String getNoun() {
		return noun;
	}

	public void setNoun(String noun) {
		this.noun = noun;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public Puzzle getPuzzle() {
		return puzzle;
	}

	public void setPuzzle(Puzzle puzzle) {
		this.puzzle = puzzle;
	}

	public NPC getNpc() {
		return npc;
	}

	public void setNpc(NPC npc) {
		this.npc = npc;
	}
}