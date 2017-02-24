import java.util.Scanner;

//TODO Handle exception where puzzle cannot be solved

public class TextSolver {
	//Create game board as a 2D array
	//Populate field with cells; make row, column, and zone values in cells correspond to row, column, and zone number
	//First dimension is row, second dimension is column
	private static Cell[][] field = {{new Cell(0,0,0), new Cell(0,1,0), new Cell(0,2,0), new Cell(0,3,1), new Cell(0,4,1), new Cell(0,5,1), new Cell(0,6,2), new Cell(0,7,2), new Cell(0,8,2)},
										{new Cell(1,0,0), new Cell(1,1,0), new Cell(1,2,0), new Cell(1,3,1), new Cell(1,4,1), new Cell(1,5,1), new Cell(1,6,2), new Cell(1,7,2), new Cell(1,8,2)},
										{new Cell(2,0,0), new Cell(2,1,0), new Cell(2,2,0), new Cell(2,3,1), new Cell(2,4,1), new Cell(2,5,1), new Cell(2,6,2), new Cell(2,7,2), new Cell(2,8,2)},
										{new Cell(3,0,3), new Cell(3,1,3), new Cell(3,2,3), new Cell(3,3,4), new Cell(3,4,4), new Cell(3,5,4), new Cell(3,6,5), new Cell(3,7,5), new Cell(3,8,5)},
										{new Cell(4,0,3), new Cell(4,1,3), new Cell(4,2,3), new Cell(4,3,4), new Cell(4,4,4), new Cell(4,5,4), new Cell(4,6,5), new Cell(4,7,5), new Cell(4,8,5)},
										{new Cell(5,0,3), new Cell(5,1,3), new Cell(5,2,3), new Cell(5,3,4), new Cell(5,4,4), new Cell(5,5,4), new Cell(5,6,5), new Cell(5,7,5), new Cell(5,8,5)},
										{new Cell(6,0,6), new Cell(6,1,6), new Cell(6,2,6), new Cell(6,3,7), new Cell(6,4,7), new Cell(6,5,7), new Cell(6,6,8), new Cell(6,7,8), new Cell(6,8,8)},
										{new Cell(7,0,6), new Cell(7,1,6), new Cell(7,2,6), new Cell(7,3,7), new Cell(7,4,7), new Cell(7,5,7), new Cell(7,6,8), new Cell(7,7,8), new Cell(7,8,8)},
										{new Cell(8,0,6), new Cell(8,1,6), new Cell(8,2,6), new Cell(8,3,7), new Cell(8,4,7), new Cell(8,5,7), new Cell(8,6,8), new Cell(8,7,8), new Cell(8,8,8)}};
	
	//Create a second 2D array to represent the zones of the Sudoku board
	//Populate zones by connecting each cell from field to its corresponding zone cell
	//Zone layout:
	//	0	1	2
	//	3	4	5
	//	6	7	8
	private static Cell[][] zone = {{field[0][0], field[0][1], field[0][2], field[1][0], field[1][1], field[1][2], field[2][0], field[2][1], field[2][2]},
									{field[0][3], field[0][4], field[0][5], field[1][3], field[1][4], field[1][5], field[2][3], field[2][4], field[2][5]},
									{field[0][6], field[0][7], field[0][8], field[1][6], field[1][7], field[1][8], field[2][6], field[2][7], field[2][8]},
									{field[3][0], field[3][1], field[3][2], field[4][0], field[4][1], field[4][2], field[5][0], field[5][1], field[5][2]},
									{field[3][3], field[3][4], field[3][5], field[4][3], field[4][4], field[4][5], field[5][3], field[5][4], field[5][5]},
									{field[3][6], field[3][7], field[3][8], field[4][6], field[4][7], field[4][8], field[5][6], field[5][7], field[5][8]},
									{field[6][0], field[6][1], field[6][2], field[7][0], field[7][1], field[7][2], field[8][0], field[8][1], field[8][2]},
									{field[6][3], field[6][4], field[6][5], field[7][3], field[7][4], field[7][5], field[8][3], field[8][4], field[8][5]},
									{field[6][6], field[6][7], field[6][8], field[7][6], field[7][7], field[7][8], field[8][6], field[8][7], field[8][8]}};
	
	public static void main(String[] args) {
		
		//Input Sudoku grid and fill the cells with their respective values
		System.out.println("Enter your Sudoku grid. Use the number 0 to denote an empty cell.");
		System.out.println("Separate lines with Enter and numbers with Space.");
		Scanner scan = new Scanner(System.in);
		for (int i=0; i<9; i++) {
			for (Cell cell : field[i]) {
				int temp = scan.nextInt();
				if (temp!=0) {
					cell.setValue(temp);
					cell.setSolved(true);
				}
			}
		}
		scan.close();
		
		//Create a variable to check if the puzzle is still in progress
		boolean puzzleSolved = false;
		
		//While the puzzle is still unsolved and no duplicates are produced, solve puzzle using markups and candidate checks
		String before = "before", after = "after";
		while (!puzzleSolved && !before.equals(after)) {
			
			//Compute all possibilities for each cell
			markup();
			
			//Create a string representation of the puzzle before candidate check
			before = puzzleToString();
			
			//Check for cells that only have one possibility and mark them solved
			for (int i=0; i<9; i++) {
				for (Cell cell : field[i]) {
					cell.candidateCheck();
				}
			}
			
			//Create a string representation of the puzzle after candidate check
			//Reset possibilities for all cells
			for (int i=0; i<9; i++) {
				for (Cell cell : field[i])
					cell.resetPossible();
			}
			//Recompute all possibilities for each cell taking into account the newly solved cells (if any)
			markup();
			//Create a string representation of the puzzle
			after = puzzleToString();

			//Check to see if the entire puzzle is solved
			//If yes, break while loop; if no, reset possibilities for all cells
			if (checkPuzzle()) {
				puzzleSolved = true;
			} else {
				for (int i=0; i<9; i++) {
					for (Cell cell : field[i])
						cell.resetPossible();
				}
			}
			
		}

		//Print the solved puzzle or a message
		if (puzzleSolved)
			print();
		else
			System.out.println("The puzzle cannot be solved using only candidate checks.");
	}
	
	//Helper method to print the puzzle
	public static void print() {
		for (int i=0; i<9; i++) {
			for (Cell cell : field[i])
				System.out.print(cell.getValue() + " ");
			System.out.println("");
		}
	}
	
	//Helper method to print the puzzle after each iteration of the solution algorithm
	//For testing purposes only
	public static void printPossible() {
		for (int i=0; i<9; i++) {
			for (Cell cell : field[i]) {
				if (cell.isSolved())
					System.out.print(cell.getValue() + "\t");
				else {
					if (cell.isMaybe1())
						System.out.print("1");
					if (cell.isMaybe2())
						System.out.print("2");
					if (cell.isMaybe3())
						System.out.print("3");
					if (cell.isMaybe4())
						System.out.print("4");
					if (cell.isMaybe5())
						System.out.print("5");
					if (cell.isMaybe6())
						System.out.print("6");
					if (cell.isMaybe7())
						System.out.print("7");
					if (cell.isMaybe8())
						System.out.print("8");
					if (cell.isMaybe9())
						System.out.print("9");
					System.out.print("\t");
				}
			}
			System.out.println("");
		}
		System.out.println("");
	}
	
	//Helper method to compute all possibilities for each cell
	public static void markup() {
		//For each zone
		for (int i=0; i<9; i++) {
			//For each cell in zone[i]
			for (Cell cell : zone[i]) {
				//If the cell is not solved
				if (!cell.isSolved()) {
					//Test the cell for each possible value 1-9
					for (int k=1; k<=9; k++) {
						//As long as the value is not already in a solved cell in the zone
						if (((zone[i][0].isSolved() && zone[i][0].getValue()!=k) || !(zone[i][0].isSolved())) &&
								((zone[i][1].isSolved() && zone[i][1].getValue()!=k) || !(zone[i][1].isSolved())) &&
								((zone[i][2].isSolved() && zone[i][2].getValue()!=k) || !(zone[i][2].isSolved())) &&
								((zone[i][3].isSolved() && zone[i][3].getValue()!=k) || !(zone[i][3].isSolved())) &&
								((zone[i][4].isSolved() && zone[i][4].getValue()!=k) || !(zone[i][4].isSolved())) &&
								((zone[i][5].isSolved() && zone[i][5].getValue()!=k) || !(zone[i][5].isSolved())) &&
								((zone[i][6].isSolved() && zone[i][6].getValue()!=k) || !(zone[i][6].isSolved())) &&
								((zone[i][7].isSolved() && zone[i][7].getValue()!=k) || !(zone[i][7].isSolved())) &&
								((zone[i][8].isSolved() && zone[i][8].getValue()!=k) || !(zone[i][8].isSolved()))) {
							cell.setValue(k);
							if (checkCell(cell)) {
								cell.addPossible();
							}
						}
					}
				}
			}
		}
	}
	
	//Helper method to find instances where a number can only go in one cell in a zone
	public static void zonePlaceFind() {
		
	}
	
	//Helper method to find instances where a number can only go in one cell in a row
	public static void rowPlaceFind() {
		
	}
	
	//Helper method to find instances where a number can only go in one cell in a column
	public static void columnPlaceFind() {
		
	}
	
	//Helper method to check to see if the value of a cell can work for that cell
	public static boolean checkCell(Cell cell) {
		//Create variables to represent the row and column the cell will be checked against
		int row = cell.getRow();
		int column = cell.getColumn();
		
		//Check cell against values of the other row members
		for (int i=0; i<9; i++) {
			if (field[row][i].isSolved() && field[row][i].getValue()==cell.getValue())
				return false;
		}
		
		//Check cell against values of the other column members
		for (int i=0; i<9; i++) {
			if (field[i][column].isSolved() && field[i][column].getValue()==cell.getValue())
				return false;
		}
		
		//If the cell's value is not equal to any of the values of the solved cells in the same row and column, return true
		return true;
	}
	
	//Helper method to check if the entire puzzle has been solved
	//If any cell is unsolved, return false; otherwise return true
	public static boolean checkPuzzle() {
		for (int i=0; i<9; i++) {
			for (Cell cell : field[i]) {
				if (!cell.isSolved())
					return false;
			}
		}
		return true;
	}
	
	//Helper method to return a String representation of the puzzle
	//Used for duplicate checks
		public static String puzzleToString() {
			String puzzle = "";
			for (int i=0; i<9; i++) {
				for (int j=0; j<9; j++) {
					if (field[i][j].isSolved())
						puzzle = puzzle.concat(field[i][j].getValue() + " ");
					else {
						if (field[i][j].isMaybe1())
							puzzle = puzzle.concat("1");
						if (field[i][j].isMaybe2())
							puzzle = puzzle.concat("2");
						if (field[i][j].isMaybe3())
							puzzle = puzzle.concat("3");
						if (field[i][j].isMaybe4())
							puzzle = puzzle.concat("4");
						if (field[i][j].isMaybe5())
							puzzle = puzzle.concat("5");
						if (field[i][j].isMaybe6())
							puzzle = puzzle.concat("6");
						if (field[i][j].isMaybe7())
							puzzle = puzzle.concat("7");
						if (field[i][j].isMaybe8())
							puzzle = puzzle.concat("8");
						if (field[i][j].isMaybe9())
							puzzle = puzzle.concat("9");
						puzzle = puzzle.concat(" ");
					}
				}
			}
			return puzzle;
		}
	
}

//Random test puzzle
// 0 0 0 0 9 0 0 5 0 7 0 0 0 4 5 0 2 3 0 0 3 0 8 0 0 6 7 0 0 4 0 2 9 0 0 8 2 3 9 0 0 0 7 1 6 1 0 0 7 3 0 2 0 0 9 1 0 0 6 0 3 0 0 5 2 0 4 7 0 0 0 9 0 4 0 0 1 0 0 0 0
