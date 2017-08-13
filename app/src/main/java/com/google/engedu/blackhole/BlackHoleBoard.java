/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.blackhole;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

/* Class that represent the state of the game.
 * Note that the buttons on screen are not updated by this class.
 */
public class BlackHoleBoard {

    private final int gameDepth = BOARD_SIZE -1;

    public int getBoardDepth() {
        return boardDepth;
    }

    public void setBoardDepth(int boardDepth) {
        this.boardDepth = boardDepth;
    }

    private int boardDepth;

    public static int THRESHOLD = 4;

    public int getBoardScore() {
        return boardScore;
    }

    public void setBoardScore(int boardScore) {
        this.boardScore = boardScore;
    }

    private int boardScore;
    // The number of turns each player will take.
    public final static int NUM_TURNS = 10;
    // Size of the game board. Each player needs to take 10 turns and leave one empty tile.
    public final static int BOARD_SIZE = NUM_TURNS * 2 + 1;
    // Relative position of the neighbors of each tile. This is a little tricky because of the
    // triangular shape of the board.
    public final static int[][] NEIGHBORS = {{-1, -1}, {0, -1}, {-1, 0}, {1, 0}, {0, 1}, {1, 1}};
    // When we get to the Monte Carlo method, this will be the number of games to simulate.
    private static final int NUM_GAMES_TO_SIMULATE = 2000;
    // The tiles for this board.
    private BlackHoleTile[] tiles;

    public BlackHoleTile[] getTiles() {
        return tiles;
    }

    // The number of the current player. 0 for user, 1 for computer.
    private int currentPlayer;
    // The value to assign to the next move of each player.
    private int[] nextMove = {1, 1};
    // A single random object that we'll reuse for all our random number needs.
    private static final Random random = new Random();

    public ArrayList<Integer> getMovesToMake() {
        return movesToMake;
    }

    //indices of moves that the computer should make
    private ArrayList<Integer> movesToMake = new ArrayList<>();

    // Constructor. Nothing to see here.
    public BlackHoleBoard() {
        tiles = new BlackHoleTile[BOARD_SIZE];
        boardScore = 0;
        reset();
    }

    // Copy board state from another board. Usually you would use a copy constructor instead but
    // object allocation is expensive on Android so we'll reuse a board instead.
    public void copyBoardState(BlackHoleBoard other) {
        this.tiles = other.tiles.clone();
        this.currentPlayer = other.currentPlayer;
        this.nextMove = other.nextMove.clone();
        boardScore = other.getBoardScore();
        boardDepth = other.getBoardDepth();
    }

    // Reset this board to its default state.
    public void reset() {
        currentPlayer = 0;
        boardDepth = 0;
        nextMove[0] = 1;
        nextMove[1] = 1;
        for (int i = 0; i < BOARD_SIZE; i++) {
            tiles[i] = null;
        }
    }

    // Translates column and row coordinates to a location in the array that we use to store the
    // board.
    protected int coordsToIndex(int col, int row) {
        return col + row * (row + 1) / 2;
    }

    // This is the inverse of the method above.
    protected Coordinates indexToCoords(int i) {

        int row = (int)((Math.sqrt(8*i + 1) - 1)/2);
        int col;
        if(row == 0){
            col = 0;
        }else{
            col = i - ((row)*(row+1)/2);//i& the sum of all the row sizes so far
        }
        Coordinates result = new Coordinates(col,row);

        return result;
    }

    // Getter for the number of the player's next move.
    public int getCurrentPlayerValue() {
        return nextMove[currentPlayer];
    }

    // Getter for the number of the current player.
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    // Check whether the current game is over (only one blank tile).
    public boolean gameOver() {
        int empty = -1;
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (tiles[i] == null) {
                if (empty == -1) {
                    empty = i;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    // Pick a random valid move on the board. Returns the array index of the position to play.
    public int pickRandomMove() {
        ArrayList<Integer> possibleMoves = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (tiles[i] == null) {

                possibleMoves.add(i);

            }
        }
        return possibleMoves.get(random.nextInt(possibleMoves.size()));
    }

    // Pick a good move for the computer to make. Returns the array index of the position to play.
    public int pickMove() {
        //based on the depth of the board, call Monte carlo and make a move until the threshold based
        //on monte carlo
        //if the board is at the threshold depth, implement monte carlo first
        //call m
        // TODO: Implement this method have the computer make a move.
        // At first, we'll just invoke pickRandomMove (above) but later, you'll need to replace
        // it with an algorithm that uses the Monte Carlo method to pick a good move.
        monteCarlo();
        getMovesFromMinMax();
        return movesToMake.remove(0);
    }

    // Makes the next move on the board at position i. Automatically updates the current player.
    public void setValue(int i) {
        if(tiles[i] == null && !gameOver()) {
            tiles[i] = new BlackHoleTile(currentPlayer, nextMove[currentPlayer]);
            nextMove[currentPlayer]++;
            boardDepth++;
            currentPlayer++;
            currentPlayer %= 2;
        }
    }

    /* If the game is over, computes the score for the current board by adding up the values of
     * all the tiles that surround the empty tile.
     * Otherwise, returns 0.
     */
    public int getScore() {
        int score = 0;
        int emptyIndex = gameIsOver();
        if(emptyIndex != -1){

            ArrayList<BlackHoleTile> tiles = getNeighbors(indexToCoords(emptyIndex));
            for(BlackHoleTile tile: tiles){
                //if the tile is the computer's, subtract score
                //if the tile is the player's, add to the score
                //goal is to optimize for the computer
                if(tile.player == 1){//computer
                    score -= tile.value;
                }
                else{
                    score += tile.value;
                }

            }
        }
        return score;
    }

    public int gameIsOver() {
        boolean isOver = false;
        int nullIndex = -1;

        for(int i = 0; i < tiles.length; i++){
            if(isOver && tiles[i] == null){
                nullIndex = -1;
            }

            if(!isOver && tiles[i] == null){
                isOver = true;
                nullIndex = i;
            }

        }
        return nullIndex;

    }

    // Helper for getScore that finds all the tiles around the given coordinates.
    private ArrayList<BlackHoleTile> getNeighbors(Coordinates coords) {
        ArrayList<BlackHoleTile> result = new ArrayList<>();
        for(int[] pair : NEIGHBORS) {
            BlackHoleTile n = safeGetTile(coords.x + pair[0], coords.y + pair[1]);
            if (n != null) {
                result.add(n);
            }
        }
        return result;
    }

    // Helper for getNeighbors that gets a tile at the given column and row but protects against
    // array over/underflow.
    private BlackHoleTile safeGetTile(int col, int row) {
        if (row < 0 || col < 0 || col > row) {
            return null;
        }
        int index = coordsToIndex(col, row);
        if (index >= BOARD_SIZE) {
            return null;
        }
        return tiles[index];
    }

    //TODO create a second argument which is a stack to hold the indices
    public void minMax(BlackHoleBoard startingBoard, Stack<Integer> remainingMoves){
        //TODO: implement the min-max algorithm for height less than 4

        if(startingBoard.getBoardDepth() >= (gameDepth - THRESHOLD)){
            int gameScore = startingBoard.getScore();
            if(gameScore != 0){
                startingBoard.setBoardScore(gameScore);
                return;
            } else{

                if(startingBoard.getCurrentPlayer() == 1){
                    //TODO: for each adjacent board, call minmax recursively and assign the returned score then get the maximum of the returned score

                    ArrayList<BlackHoleBoard> adjacentBoards = getAdjacentStates(startingBoard);
                    int adjacentSize = adjacentBoards.size();
                    for(int i= 0; i < adjacentSize; i++){
                        minMax(adjacentBoards.get(i),remainingMoves);
                    }
                    startingBoard.setBoardScore(getMin(adjacentBoards));
                    BlackHoleBoard minNextMove = adjacentBoards.get(0);
                    for(BlackHoleBoard blackHoleBoard: adjacentBoards){
                        if(blackHoleBoard.getBoardScore() < minNextMove.boardScore){
                            minNextMove = blackHoleBoard;
                        }
                    }
                    int index = startingBoard.getFilledIndex(minNextMove);
                    remainingMoves.push(index);
                    //if i am the computer here, i wanna make the move with the board that has
                    //the smallest score of the adjacent states
                }else{
                    //TODO: for each adjacent board, call minmax recursively and assign the returned score then get minimum of those scores
                    //startingBoard.setBoardScore(getMin(getAdjacentStates(startingBoard)));
                    ArrayList<BlackHoleBoard> adjacentBoards = getAdjacentStates(startingBoard);
                    int adjacentSize = adjacentBoards.size();

                    for(int i= 0; i < adjacentSize; i++){
                        minMax(adjacentBoards.get(i),remainingMoves);
                    }
                    startingBoard.setBoardScore(getMax(adjacentBoards));
                }
                //store moves the computer should make

            }

        }

        //base case: if the game is over:
        //      then set the boardScore to the returned value of the gameOVer() method
        //      return the score
        // get the current board.
        //if the current board is the computer:(getCurrentPlayer is 1)
        //     set the boardScore of the current board to be the minimum of the score of all of the neighboring states
        //if the currentBoard is the player: (getCurrentPlayer is 0)
        //    set the boardScore to be the maximum of the score of the neighboring states

    }

    private int getMin(ArrayList<BlackHoleBoard> adjacentStates) {
        int minScore = adjacentStates.get(0).getBoardScore();
        for(int i = 1; i < adjacentStates.size(); i++){
            if(adjacentStates.get(i).getBoardScore() < minScore){
                minScore = adjacentStates.get(0).getBoardScore();
            }
        }

        return minScore;

    }

    private int getMax(ArrayList<BlackHoleBoard> adjacentStates) {
        int maxScore = adjacentStates.get(0).getBoardScore();
        for(int i = 1; i < adjacentStates.size(); i++){
            if(adjacentStates.get(i).getBoardScore() > maxScore){
                maxScore = adjacentStates.get(0).getBoardScore();
            }
        }

        return maxScore;


    }

    //retrurns indecies of the paths the computer should take to reach threshold
    public void monteCarlo(){
        //TODO: implement monte carlo for height above 4
        if(boardDepth < (gameDepth - THRESHOLD)){
            HashMap<Integer,ArrayList<Integer>> mapAvgScoreToIndices = new HashMap<>();
            for(int i = 0; i < NUM_GAMES_TO_SIMULATE; i++){
                ArrayList<Integer> currentIndices = new ArrayList<>();
                BlackHoleBoard currentWorkingBoard = new BlackHoleBoard();
                currentWorkingBoard.copyBoardState(this);
                int score = 0;
                while(!currentWorkingBoard.gameOver()){
                    int index = currentWorkingBoard.pickRandomMove();
                    if(currentWorkingBoard.getCurrentPlayer() == 1) {//only consider moves the computer is making
                        currentIndices.add(index);
                        score = currentWorkingBoard.getScore();
                    }
                    currentWorkingBoard.setValue(index);


                }
                mapAvgScoreToIndices.put(score,currentIndices);
            }
            int maxScore = Collections.max(mapAvgScoreToIndices.keySet());
            ArrayList<Integer> reccomendedMoves = mapAvgScoreToIndices.get(maxScore);
            for(int i = 0; i < THRESHOLD/2; i++){
                reccomendedMoves.remove(reccomendedMoves.size()-1);
            }
            movesToMake.addAll(reccomendedMoves);

        }
    }
    //returns teh indicies of the paths from Min-max the computer should take after threshold depth
    private void getMovesFromMinMax(){
        if(getBoardDepth() > (gameDepth - THRESHOLD)){

            //now each state has an associated score
            //starting from the current board
            //get the board with minimum score
            //getFilledIndex(this board, the minScore neighboring board)
            //add that index to the list moves to make
            Stack<Integer> remainingMoves = new Stack<>();
            BlackHoleBoard currentBoard = this;
            minMax(currentBoard,remainingMoves);
            final int stackStize = remainingMoves.size();
            for(int i = 0; i < stackStize; i++) {
                movesToMake.add(remainingMoves.pop());
            }

        }

    }

    public int getFilledIndex(BlackHoleBoard nextStateBoard)
    {
        boolean isNextState = false;
        int index = -1;
        if(getEmptySpaces() - nextStateBoard.getEmptySpaces() != 1){
            return index;
        }
            for (int i = 0; i < BOARD_SIZE; i++) {
                if(isNextState && !nullSafeGetTile(i).equals(nextStateBoard.nullSafeGetTile(i))){
                    index = -1;
                }

                if (!isNextState && !nullSafeGetTile(i).equals(nextStateBoard.nullSafeGetTile(i))) {
                    isNextState = true;
                    index = i;

                }
            }

        return index;
    }

    public BlackHoleTile nullSafeGetTile(int i ){
        if(getTiles()[i] == null){
            return new BlackHoleTile(1,-1);
        }
        return getTiles()[i];
    }

    public ArrayList<BlackHoleBoard> getAdjacentStates(BlackHoleBoard board){
        ArrayList<BlackHoleBoard> boards = new ArrayList<>();

        BlackHoleBoard workingCopyBoard = new BlackHoleBoard();

        workingCopyBoard.copyBoardState(board);//copy board into workingCopyBoard


        //copy the current board to the workingCopyBoard one
        //pick a random move from the workingCopyBoard
        //but make a move on the copyBlackHOleBoard ont the randomly selected board inside the for loop
        //add the board currently being iterated to the arraylist

        //threshold is the number of empty spaces that exist
        int numEmptySpaces = workingCopyBoard.getEmptySpaces();
        for(int i = 0;i < numEmptySpaces;i++){
            BlackHoleBoard copyBlackHoleBoard = new BlackHoleBoard();
            board.copyBoardState(copyBlackHoleBoard);
            if(workingCopyBoard.gameOver()){
                break;
            }
            int moveIndex = workingCopyBoard.pickRandomMove();
            copyBlackHoleBoard.setValue(moveIndex);
            boards.add(copyBlackHoleBoard);

        }

        return boards;
    }

    public int getEmptySpaces() {
        int count = 0;
        for(BlackHoleTile blackHoleTile: getTiles()){
            if(blackHoleTile == null){
                count++;
            }
        }
        return count;
    }
}
