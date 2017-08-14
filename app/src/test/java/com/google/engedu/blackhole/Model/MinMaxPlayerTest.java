package com.google.engedu.blackhole.Model;

import com.google.engedu.blackhole.Model.BlackHoleBoard;
import com.google.engedu.blackhole.Model.MinMaxPlayer;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Nathnael on 8/13/2017.
 */
public class MinMaxPlayerTest {

    @Test
    public void minMax() throws Exception {
        BlackHoleBoard newBoard = new BlackHoleBoard();

        for(int i = 3; i< 6; i++) {
            for (int j = 0; j < i + 1; j++) {
                newBoard.setValue(newBoard.coordsToIndex(j, i));
            }
        }
        newBoard.setValue(newBoard.coordsToIndex(1,2));
        newBoard.setValue(newBoard.coordsToIndex(2,2));

        MinMaxPlayer minMaxPlayer = new MinMaxPlayer(newBoard);

        //int nextMove = BlackHoleBoard.movesToMake.get(b);

        assertEquals(minMaxPlayer.getMove(newBoard),1);
        /*
        the order of the moves matters bc it affects the values that get destroyed later
         */
//        assertEquals(remainingMoves.size(),1);
//
//
//        assert(remainingMoves.contains(1));
//        assertFalse (remainingMoves.contains(2));
    }

    @Test
    public void getAdjacentStates() throws Exception {

        BlackHoleBoard b = new BlackHoleBoard();
        //------------------------------------------------------------------------------------------
        //                   When first three rows are not filled
        //------------------------------------------------------------------------------------------

        for(int i = 3; i< 6; i++) {
            for (int j = 0; j < i + 1; j++) {
                b.setValue(b.coordsToIndex(j, i));
            }
        }





        assertEquals(b.getEmptySpaces(),6);
        MinMaxPlayer minMaxPlayer = new MinMaxPlayer(b);

        List<BlackHoleBoard> actualAdjacentStates = minMaxPlayer.getAdjacentStates(b);

        assertEquals(actualAdjacentStates.size(),6);

        List<BlackHoleBoard> expectedAdjacentStates = new ArrayList<>();

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < i+1;j++){
                BlackHoleBoard expectedAdjacentState = new BlackHoleBoard();
                expectedAdjacentState.copyBoardState(b);
                expectedAdjacentState.setValue(expectedAdjacentState.coordsToIndex(i,j));
                expectedAdjacentStates.add(expectedAdjacentState);
            }
        }

        for(BlackHoleBoard expectedAdjacentState : expectedAdjacentStates){
            assert(actualAdjacentStates.contains(expectedAdjacentState));
        }


        //----------------------------------------------------------------------------------------
        //                          When first two rows are not filled
        //----------------------------------------------------------------------------------------

        BlackHoleBoard currentBoard = new BlackHoleBoard();
        for(int i = 2; i< 6; i++) {
            for (int j = 0; j < i + 1; j++) {
                currentBoard.setValue(currentBoard.coordsToIndex(j, i));
            }
        }

        minMaxPlayer = new MinMaxPlayer(currentBoard);

        List<BlackHoleBoard> actualAdjacentStates1 = minMaxPlayer.getAdjacentStates(currentBoard);

        assertEquals(actualAdjacentStates1.size(),3);

        List<BlackHoleBoard> expectedAdjacentStates1 = new ArrayList<>();

        for(int i = 0; i < 2; i++){
            for(int j = 0; j < i+1;j++){
                BlackHoleBoard expectedAdjacentState = new BlackHoleBoard();
                expectedAdjacentState.copyBoardState(currentBoard);
                expectedAdjacentState.setValue(expectedAdjacentState.coordsToIndex(j,i));
                expectedAdjacentStates1.add(expectedAdjacentState);
            }
        }

        for(BlackHoleBoard expectedAdjacentState : expectedAdjacentStates1){
            assert(actualAdjacentStates1.contains(expectedAdjacentState));
        }


        // ---------------------------------------

        // ------------------------------------

        BlackHoleBoard newBoard = new BlackHoleBoard();

        for(int i = 3; i< 6; i++) {
            for (int j = 0; j < i + 1; j++) {
                newBoard.setValue(newBoard.coordsToIndex(j, i));
            }
        }
        newBoard.setValue(newBoard.coordsToIndex(2,1));
        newBoard.setValue(newBoard.coordsToIndex(2,2));
        minMaxPlayer = new MinMaxPlayer(newBoard);

        List<BlackHoleBoard> newAdjacentBoards = minMaxPlayer.getAdjacentStates(newBoard);
        List<Integer> unfilledSpaces = new ArrayList<>();

        for(BlackHoleBoard blackHoleBoard: newAdjacentBoards){
            unfilledSpaces.add(newBoard.getFilledIndex(blackHoleBoard));
        }

        assertEquals(unfilledSpaces.size(),4);
        assert (unfilledSpaces.contains(0));
        assert (unfilledSpaces.contains(1));
        assert (unfilledSpaces.contains(2));
        assert (unfilledSpaces.contains(3));
        assert (!unfilledSpaces.contains(4));



    }

}