# black-hole

## Overview

This is an android two-player strategy game which uses a computer player implemented for a game known as [blackhole](https://www.youtube.com/watch?v=zMLE7a3faI4). This was the workshop of Google's Applied CS with android.

## Data Structures and Algorithms used

A combination of the [min-max](http://www.geeksforgeeks.org/minimax-algorithm-in-game-theory-set-1-introduction/) algorithm and [monte-carlo](https://en.wikipedia.org/wiki/Monte_Carlo_method) were used to implement the computer player. Although the min-max algorithm would yeild the most optimal solution, in favor of time efficiency, monte-carlo was used until only 4 moves are left in the game. After that, the computer's next move is determined by the min-max algorithm.

## Tests

Comprehensive unit tests were written throughout the developement of the app.

![Alt text](/resources/blackhole_tests.png?raw=true "Tests for Blackhole")


