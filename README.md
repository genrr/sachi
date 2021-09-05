# sachi
Sachi board game

A two-person abstract strategy board game played on 11x11 board. Features 8 different pieces, with different movesets and ranges. The aim is to surround the other player with area marks.(![mark_area](https://user-images.githubusercontent.com/35907453/114156422-3bbfc880-992b-11eb-8520-7ca0d44b2d07.png))
Area marks are placed on squares and are used to generate income needed for placing more marks, attacking and moving. Players can move pieces, attack pieces and place area marks during one turn. 

![sachiboard3](https://user-images.githubusercontent.com/35907453/114158069-fa301d00-992c-11eb-9939-6372bed1875a.png)



Building areas

Any rectangle formed with own area marks(4 corners) counts as an area. Marks must be placed according to pieces' ranges, meaning there must be a piece that can reach the square you are attempting to place the mark. When there is no opponent piece outside any of your areas, you win the game. Piece being on the border (ie. on the same x/y coordinate as the marks) still counts as being inside the area. Placing an area mark costs the same amount of counters as you have marks on the board currently. Area marks give each one counter income per turn. Area marks can be attacked, and opponent doing so profits one counter and mark disappears reducing other players income.

Piece actions

Pieces can attack from range, meaning the piece doesn't move to target square when attacking. Pieces other than General can't attack through pieces or marks. If player has enough counters for given attack, piece or mark at the target square is removed from the board and attacking player receives removed pieces Value in counters. Pieces can not move to squares occupied with marks, but all pieces can move through any other piece/mark, own or opponent. Players can attack their own marks, for counter gain of 1 minus the attackers attack cost to make space on the board.

Game objective

Game ends when one player has all the other players pieces inside his areas. Game can also end when one player has captured all the other players pieces. Draw can happen, when neither player have placed a mark or captured a piece during the last 30 moves or when both players have only one piece remaining. Players can also propose a draw or resign voluntarily.



# In game

press 'SHIFT' for attack mode

press 'ALT' for mark placement mode



# Running
at command line: java --module-path "PATH"/lib --add-modules=javafx.controls -jar sachi.jar

where "PATH" is a path to JavaFX library.



# Dependencies

Java 14

JavaFX 11.0.6
