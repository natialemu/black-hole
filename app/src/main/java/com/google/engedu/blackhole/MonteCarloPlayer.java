package com.google.engedu.blackhole;

import java.util.HashMap;

import static com.google.engedu.blackhole.BlackHoleBoard.THRESHOLD;

/**
 * Created by Nathnael on 8/13/2017.
 */

public class MonteCarloPlayer {

    int nextMove;

    public MonteCarloPlayer(BlackHoleBoard currentBoard){
        monteCarlo(currentBoard);

    }

    public void monteCarlo(BlackHoleBoard currentBoard){
        //TODO: implement monte carlo for height above 4
        if(currentBoard.getBoardDepth() < (currentBoard.getGameDepth() - THRESHOLD)){
            HashMap<Integer,Integer> mapNextMovetoScore = new HashMap<>();//map first move to final outcome
            for(int i = 0; i < BlackHoleBoard.NUM_GAMES_TO_SIMULATE; i++){
                //ArrayList<Integer> currentIndices = new ArrayList<>();
                int finalScore = 0;
                BlackHoleBoard currentWorkingBoard = new BlackHoleBoard();
                currentWorkingBoard.copyBoardState(currentBoard);
                int score = 0;
                int index = currentWorkingBoard.pickRandomMove();
                while(!currentWorkingBoard.gameOver()){
                    //int index = currentWorkingBoard.pickRandomMove();

                    currentWorkingBoard.setValue(index);
                    index = currentWorkingBoard.pickRandomMove();


                }
                if(currentWorkingBoard.gameOver()){
                    score = currentWorkingBoard.getScore();
                }
                mapNextMovetoScore.put(index,score);
            }
            int minScore = 0;
            int indexWithMinScore = 0;
            for(int key: mapNextMovetoScore.keySet()){
                if(mapNextMovetoScore.get(key) < minScore){
                    minScore = mapNextMovetoScore.get(key);
                    indexWithMinScore = key;
                }
            }
            //ArrayList<Integer> reccomendedMoves = mapNextMovetoScore.get(minScore);
            nextMove =  indexWithMinScore;
        }else {
            nextMove = -1;
        }
    }

    public int getNextMove(){
        return nextMove;
    }
}
