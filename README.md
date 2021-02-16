# Text Based Adventure Game

Team Project Proposal
CS320-103: Software Engineering, Spring Semester 2021
Due Date: Friday, 2-12-2021, by 9:00am

Text Based Adventure

Team Members:
Eric Johns
Josh Kopelcheck
Michael Feldman

Summary:
We will be creating an escape room text-adventure game, where the player has a set time limit to escape a set number of areas and solve challenging puzzles. The project will consist of several stages, each with a handful of rooms. Every room will have sufficient clues for the user to solve problems and move on to the next stage and hopefully escape. There will be a set order in which to solve the puzzles in each room, and you have to solve every puzzle in the area before you move on to the next. 
Every area will have multiple puzzles that have to be solved before moving onto the next area. The user's job is to look for clues around each room to solve each puzzle. They may consist of finding keys around the area to open locks, or moving around furniture to find the way to the next room, or simple brain teasers. The user must solve the puzzle in each room to move onto the next. The user has a time limit, and like most escape rooms, must be out before the time runs out or they do not win the game. 

Features: 
Major Features:
Player:
    The user plays the game in first person through a player. The player can traverse through rooms and interact with items. The player’s goal is to solve all the puzzles in each major area, so they can move onto the next stage. Once they finish the last room, they escape and get a score based on how fast they solved puzzles, whether they used hints, and how many attempts they used on each puzzle.
Stages:
The game consists of three major stages, and 8-10 rooms per stage. Each stage is a section of rooms that has a common theme and represents a major milestone in getting through the game. When the player completes a stage, they will get a small time boost to help them escape in time.
Rooms:
    Rooms are a subsection of stages and consist of one puzzle each. The user must solve the puzzle to move onto the next room, and each room moves sequentially into the next. Rooms are full of interactable items that can be used to solve the puzzles. A room has connections, and leads into one consecutive room.  A room has a description that explains how the room looks and what is inside it.
Items:
    Items are in every room and the player may be able to interact with them or pick them up. Some items may not be able to be moved. Items may be critical to challenges. Items will also have weight. They can be carried across rooms and stages and can be stored in the player’s inventory. 
Inventory:
    An inventory will keep track of the player’s items. The inventory can only hold a specific amount of weight, and will not allow new items to be added to it if that weight limit is exceeded. The player will be able to run a command to see what is in their inventory.
Commands:
    Commands are text based and tell the game what the player wants to do. These commands will consist of a noun and a verb. When running  a command, the object in question must be in the room or player’s inventory. There are certain keywords that must be used to specify certain actions.
User interface: 
In the top of the screen, there will be a moves counter to monitor how many moves the player makes. There will also be a score displayed in the UI to show the user how well they are doing.

Score:
Points are awarded to the score for solving problems and moving through rooms quickly. Having a lower moves count at the end of the game will reward the player with some extra points for doing well on each puzzle.

Stretch Goals:
Time:
    The game must be completed in a set time or the player loses. It counts down at a constant rate and shows the user how much time they have left before game over. The time left will also be displayed on the top of the screen so the user is aware of how much time they have left.
Hints:
Hints may be requested on any puzzle, but the player must incur a penalty doing so. This penalty may consist of losing time to escape, or they take a hit to their score. They can only ask for a certain amount of hints in each stage, and each puzzle will only have one hint to give.
Scoreboard:
At the end of the game, there is a scoreboard where they can compare how they did to other players who went through the game. If they complete the game, they can choose to submit their score along with 3 initials they can be identified by.
Progress Bar:
Players can see their progress in the game via a progress bar on the top left of the screen. This will fill in as they solve puzzles and let them know how close they are to escaping.
Synonyms:
    The game will recognize multiple words that have similar meanings so the user is not stumbling over a specific term they have to figure out to accomplish what they want. This prevents frustration and confusion from them not knowing what they are supposed to type.
