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

package com.google.engedu.blackhole.View;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.engedu.blackhole.Model.BlackHoleBoard;
import com.google.engedu.blackhole.R;

public class MainActivity extends AppCompatActivity {

    // Colors used to differentiate human player from computer player.
    private static final int[] COLORS = {Color.rgb(255, 128, 128), Color.rgb(128, 128, 255)};

    // The main board instance.
    private BlackHoleBoard board;

    // Initialize the board on launch.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String fabId = "fab";

        for(int i = 0; i < 21; i++){
            String currentfabId = fabId + Integer.toString(i);
            int id = getResources().getIdentifier(currentfabId, "id", getPackageName());

            FloatingActionButton floatingActionButton = (FloatingActionButton)findViewById(id);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickHandler(view);
                }
            });
        }

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.button);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onReset(view);
            }
        });

        board = new BlackHoleBoard();
        onReset(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.game_help){
            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /* Shared handler for all the game buttons. When the user takes a turn we mark the button as
     * having been clicked and let the computer take a turn.
     */
    public void onClickHandler(View view) {
        FloatingActionButton clicked = (FloatingActionButton) view;
        if (clicked.isEnabled()) {
            markButtonAsClicked(clicked);
            computerTurn();
        }
    }

    // Change the button that was clicked and update the board accordingly.
    private void markButtonAsClicked(FloatingActionButton clicked) {
        clicked.setEnabled(false);
        //get the id of the clicked fab.
        //extract the last number and append it to button
        //set the text of that textView
        String fabId = getResources().getResourceEntryName(clicked.getId());

        String commonId = fabId.substring(3);

        String textViewID = "button" + commonId;
        int tvid = getResources().getIdentifier(textViewID, "id", getPackageName());

        TextView textView = (TextView) findViewById(tvid);

        textView.setText("" + board.getCurrentPlayerValue());
        clicked.getBackground().setColorFilter(
                COLORS[board.getCurrentPlayer()], PorterDuff.Mode.MULTIPLY);
        String buttonLabel = getResources().getResourceEntryName(textView.getId());
        board.setValue(Integer.parseInt(buttonLabel.substring(6)));
        if (board.gameOver()) {
            handleEndOfGame();
        }
    }

    // When the game is over, declare a winner.
    private void handleEndOfGame() {
        disableAllButtons();
        int score = board.getScore();
        String message = null;
        if (score > 0) {
            message = "You win by " + score;
        }
        else if (score < 0) {
            message = "You lose by " + -score;
        }
        if (message != null) {
            Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
            toast.show();
            Log.i("BlackHole", message);
        }
    }

    // When the game is over disable all buttons (really just the one button that is left).
    private void disableAllButtons() {
        for (int i = 0; i < BlackHoleBoard.BOARD_SIZE; i++) {
            int id = getResources().getIdentifier("fab" + i, "id", getPackageName());
            FloatingActionButton b = (FloatingActionButton) findViewById(id);
            b.setEnabled(false);
        }
    }

    // Let the computer take a turn.
    private void computerTurn() {
        int position = board.pickMove();
        int id = getResources().getIdentifier("fab" + position, "id", getPackageName());
        //Toast.makeText(this," trying to find button"+position,Toast.LENGTH_SHORT).show();
        FloatingActionButton b = (FloatingActionButton) findViewById(id);
        if (b == null) {
            Log.i("Blackhole", "Couldn't find button " + position + " with id " + id);
        }else{
            markButtonAsClicked(b);
        }

    }

    // Handler for the reset button. Resets both the board and the game buttons.
    public void onReset(View view) {
        board.reset();
        for (int i = 0; i < BlackHoleBoard.BOARD_SIZE; i++) {
            int id = getResources().getIdentifier("fab" + i, "id", getPackageName());
            FloatingActionButton b = (FloatingActionButton) findViewById(id);
            b.setEnabled(true);

            String textViewID = "button" + i;
            int tvid = getResources().getIdentifier(textViewID, "id", getPackageName());

            TextView textView = (TextView) findViewById(tvid);

            textView.setText("?");

            b.getBackground().setColorFilter(null);
        }
    }
}
