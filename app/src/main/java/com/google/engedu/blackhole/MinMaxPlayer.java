package com.google.engedu.blackhole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nathnael on 8/13/2017.
 */

public class MinMaxPlayer {
    private Map<BlackHoleBoard,Integer> movesToMake = new HashMap<>();

    //private BlackHoleBoard startingBoard;

    public MinMaxPlayer(BlackHoleBoard blackHoleBoard){
        //startingBoard = blackHoleBoard;
        minMax(blackHoleBoard);
    }

    public void minMax(BlackHoleBoard startingBoard){
        if(startingBoard.getBoardDepth() >= ( startingBoard.getGameDepth()- BlackHoleBoard.THRESHOLD)){

            if(startingBoard.gameOver()){
                int gameScore = startingBoard.getScore();
                startingBoard.setBoardScore(gameScore);
            } else{

                if(startingBoard.getCurrentPlayer() == 1){
                    //TODO: for each adjacent board, call minmax recursively and assign the returned score then get the maximum of the returned score

                    ArrayList<BlackHoleBoard> adjacentBoards = getAdjacentStates(startingBoard);
                    int adjacentSize = adjacentBoards.size();
                    for(int i= 0; i < adjacentSize; i++){
                        minMax(adjacentBoards.get(i));
                    }
                    startingBoard.setBoardScore(getMin(adjacentBoards));
                    BlackHoleBoard minNextMove = adjacentBoards.get(0);
                    for(BlackHoleBoard blackHoleBoard: adjacentBoards){
                        if(blackHoleBoard.getBoardScore() < minNextMove.getBoardScore()){
                            minNextMove = blackHoleBoard;
                        }
                    }
                    int index = startingBoard.getFilledIndex(minNextMove);
                    if(!movesToMake.containsKey(startingBoard)){
                        movesToMake.put(startingBoard,index);
                    }

                    //remainingMoves.push(index);
                    //if i am the computer here, i wanna make the move with the board that has
                    //the smallest score of the adjacent states
                }else{
                    //TODO: for each adjacent board, call minmax recursively and assign the returned score then get minimum of those scores
                    //startingBoard.setBoardScore(getMin(getAdjacentStates(startingBoard)));
                    ArrayList<BlackHoleBoard> adjacentBoards = getAdjacentStates(startingBoard);
                    int adjacentSize = adjacentBoards.size();

                    for(int i= 0; i < adjacentSize; i++){
                        minMax(adjacentBoards.get(i));
                    }
                    startingBoard.setBoardScore(getMax(adjacentBoards));
                }
                //store moves the computer should make

            }

        }

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
        int unFilledIndex = -1;
        for(int i = 0;i < numEmptySpaces;i++){
            //this board has the same number of spaces as original board
            BlackHoleBoard copyBlackHoleBoard = new BlackHoleBoard();
            copyBlackHoleBoard.copyBoardState(board);

            if(workingCopyBoard.gameOver()){
                unFilledIndex = workingCopyBoard.gameIsOver();
                break;
            }
            int moveIndex = workingCopyBoard.pickRandomMove();
            copyBlackHoleBoard.setValue(moveIndex);
            workingCopyBoard.setValue(moveIndex);
            boards.add(copyBlackHoleBoard);

        }
        BlackHoleBoard copyBlackHoleBoard = new BlackHoleBoard();
        copyBlackHoleBoard.copyBoardState(board);

        copyBlackHoleBoard.setValue(unFilledIndex);
        boards.add(copyBlackHoleBoard);

        return boards;
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

    public int getMove(BlackHoleBoard board){
        if(!movesToMake.containsKey(board)){
            return -1;
        }
        return movesToMake.get(board);
    }
}
