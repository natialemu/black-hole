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

        BlackHoleBoard currentBoard = new BlackHoleBoard();
        assertEquals(currentBoard.getScore(),0);
        for(int i = 1; i< 6; i++) {
            for (int j = 0; j < i + 1; j++) {
                currentBoard.setValue(currentBoard.coordsToIndex(j, i));
            }
        }

        assertEquals(currentBoard.getScore(),0);

        BlackHoleBoard newBoard = new BlackHoleBoard();

        for(int i = 4; i< 6; i++) {
            for (int j = 0; j < i + 1; j++) {
                newBoard.setValue(newBoard.coordsToIndex(j, i));
            }
        }

        for(int i = 0; i< 3; i++) {
            for (int j = 0; j < i + 1; j++) {
                newBoard.setValue(newBoard.coordsToIndex(j, i));
            }
        }

        newBoard.setValue(newBoard.coordsToIndex(0, 3));
        newBoard.setValue(newBoard.coordsToIndex(1, 3));
        newBoard.setValue(newBoard.coordsToIndex(3, 3));
        assert(newBoard.gameOver());
        int nullIndex = newBoard.gameIsOver();
        assertEquals(newBoard.coordsToIndex(2,3),nullIndex);


        assertEquals(newBoard.getScore(),-41);
        //hole is at 3,3






        // TODO: Implement this test to verify that your getScore method is working.

    }



    @Test
    public void testGameIsOver() {


        for(int i = 1; i< 6; i++) {
            for (int j = 0; j < i + 1; j++) {
                b.setValue(b.coordsToIndex(j, i));
            }
        }

        assertEquals (b.gameIsOver(),0);
        BlackHoleBoard currentBoard = new BlackHoleBoard();
        for(int i = 3; i< 6; i++) {
            for (int j = 0; j < i + 1; j++) {
                currentBoard.setValue(currentBoard.coordsToIndex(j, i));
            }
        }
        assertEquals(currentBoard.gameIsOver(),-1);
        for(int i = 0; i< 3; i++) {
            for (int j = 0; j < i + 1; j++) {
                currentBoard.setValue(currentBoard.coordsToIndex(j, i));
            }
        }

        assertEquals(currentBoard.gameIsOver(),5);
        //TODO

    }

    @Test
    public void testGameOver(){
        //TODO
        for(int i = 1; i< 6; i++) {
            for (int j = 0; j < i + 1; j++) {
                b.setValue(b.coordsToIndex(i, j));
            }
        }

        assert (b.gameOver());
        BlackHoleBoard currentBoard = new BlackHoleBoard();
        for(int i = 3; i< 6; i++) {
            for (int j = 0; j < i + 1; j++) {
                currentBoard.setValue(currentBoard.coordsToIndex(i, j));
            }
        }
        assertFalse(currentBoard.gameOver());
        for(int i = 0; i< 3; i++) {
            for (int j = 0; j < i + 1; j++) {
                currentBoard.setValue(currentBoard.coordsToIndex(i, j));
            }
        }

        assertFalse(currentBoard.gameOver());

    }

    @Test
    public void testPickMove() {
        //TODO

    }

    @Test
    public void testSetValue(){
        //start off with empty board
        //
        assertEquals(b.getCurrentPlayer(),0);

        b.setValue(0);
        assertEquals(b.getCurrentPlayer(),1);
        BlackHoleTile[] tiles = b.getTiles();
        assertEquals(tiles[0].player,0);
        assertEquals(tiles[0].value,1);
        //make sure tile at 0 has the right value
        //make sure tile 0 belogs to the right player

        b.setValue(5);
        assertEquals(b.getTiles()[5].player,1);
        assertEquals(b.getTiles()[5].value,1);
        assertEquals(b.getCurrentPlayer(),0);


        //TODO
    }



    @Test
    public void testGetMovesFromMinMax() {


    }

    @Test
    public void testGetFilledIndex() {

        BlackHoleBoard currentBoard = new BlackHoleBoard();

        BlackHoleBoard nextStateBoard = new BlackHoleBoard();

        assertEquals(currentBoard.getFilledIndex(nextStateBoard),-1);

        nextStateBoard.setValue(4);
        assertEquals(currentBoard.getFilledIndex(nextStateBoard),4);

        nextStateBoard.setValue(6);
        assertEquals(currentBoard.getFilledIndex(nextStateBoard),-1);

        currentBoard.setValue(4);
        assertEquals(currentBoard.getFilledIndex(nextStateBoard),6);

        currentBoard.setValue(6);
        currentBoard.setValue(5);
        assertEquals(currentBoard.getFilledIndex(nextStateBoard),-1);

        currentBoard.setValue(8);
        assertEquals(currentBoard.getFilledIndex(nextStateBoard),-1);

        nextStateBoard.setValue(5);
        nextStateBoard.setValue(8);
        nextStateBoard.setValue(10);
        assertEquals(currentBoard.getFilledIndex(nextStateBoard),10);


    }

}
