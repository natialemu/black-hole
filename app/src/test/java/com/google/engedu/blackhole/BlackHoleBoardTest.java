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

import android.util.Pair;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.junit.Assert.*;

public class BlackHoleBoardTest {

    private BlackHoleBoard b;

    @Before
    public void setUp(){

        b = new BlackHoleBoard();

    }
    @Test
    public void testCoordsToIndex() {

        assertEquals(0, b.coordsToIndex(0, 0));
        assertEquals(1, b.coordsToIndex(0, 1));
        assertEquals(2, b.coordsToIndex(1, 1));
        assertEquals(3, b.coordsToIndex(0, 2));
        assertEquals(4, b.coordsToIndex(1, 2));
        assertEquals(5, b.coordsToIndex(2, 2));
    }

    @Test
    public void testIndexToCoords() {

        Coordinates coords = b.indexToCoords(0);
        assertEquals(0, coords.x);
        assertEquals(0, coords.y);
        coords = b.indexToCoords(1);
        assertEquals(0, coords.x);
        assertEquals(1, coords.y);
        for (int i = 0; i < b.BOARD_SIZE; i++) {
            coords = b.indexToCoords(i);
            assertEquals(i, b.coordsToIndex(coords.x, coords.y));
        }
    }

    @Test
    public void testGetScore() {

        // TODO: Implement this test to verify that your getScore method is working.

    }
    @Test
    public void testGetAdjacentStates() {

        //------------------------------------------------------------------------------------------
        //                   When first three rows are not filled
        //------------------------------------------------------------------------------------------

        for(int i = 3; i< 6; i++) {
            for (int j = 0; j < i + 1; j++) {
                b.setValue(b.coordsToIndex(i, j));
            }
        }


        List<BlackHoleBoard> actualAdjacentStates = b.getAdjacentStates(b);

        assertEquals(actualAdjacentStates.size(),6);

        List<BlackHoleBoard> expectedAdjacentStates = new ArrayList<>();

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < i+1;j++){
                BlackHoleBoard expectedAdjacentState = new BlackHoleBoard();
                b.copyBoardState(expectedAdjacentState);
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
                currentBoard.setValue(currentBoard.coordsToIndex(i, j));
            }
        }


        List<BlackHoleBoard> actualAdjacentStates1 = currentBoard.getAdjacentStates(currentBoard);

        assertEquals(actualAdjacentStates1.size(),3);

        List<BlackHoleBoard> expectedAdjacentStates1 = new ArrayList<>();

        for(int i = 0; i < 2; i++){
            for(int j = 0; j < i+1;j++){
                BlackHoleBoard expectedAdjacentState = new BlackHoleBoard();
                currentBoard.copyBoardState(expectedAdjacentState);
                expectedAdjacentState.setValue(expectedAdjacentState.coordsToIndex(i,j));
                expectedAdjacentStates1.add(expectedAdjacentState);
            }
        }

        for(BlackHoleBoard expectedAdjacentState : expectedAdjacentStates1){
            assert(actualAdjacentStates1.contains(expectedAdjacentState));
        }


    }

    @Test
    public void testMinMax(){

        //only four moves are left
        for(int i = 2; i< 6; i++) {
            for (int j = 0; j < i + 1; j++) {
                b.setValue(b.coordsToIndex(i, j));
            }
        }
        b.setValue(b.coordsToIndex(2,1));
        b.setValue(b.coordsToIndex(2,2));

        Stack<Integer> remainingMoves = new Stack<>();

        b.minMax(b,remainingMoves);

        assertEquals(remainingMoves.size(),1);


        assert(remainingMoves.contains(1));
        //assert (remainingMoves.contains(2));

        /*
        also the remaining number of moves should only be for the computer
         */
        //TODO:

    }

    @Test
    public void testGetNeighbors(){
        //TODO

    }

    @Test
    public void testGameIsOver() {
        //TODO

    }

    @Test
    public void testGameOver(){
        //TODO

    }

    @Test
    public void testPickMove() {
        //TODO

    }

    @Test
    public void testSetValue(){
        //TODO
    }
}
