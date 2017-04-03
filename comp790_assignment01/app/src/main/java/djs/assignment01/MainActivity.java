package djs.assignment01;

import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;


public class MainActivity extends AppCompatActivity
{
    // enums
    enum ESquareState { WHITE, BLACK};

    // variables
    private ESquareState[] m_state;
    private int m_move_count;
    private String m_sequence;
    ESquareState m_check_pattern_for_victory_state[] = new ESquareState[16];

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // restart to start
        this.restart();
    }

    private void restart()
    {
        // setup the state
        this.m_state = new ESquareState[16];

        // set state to all white
        for (int i = 0; i < 16; ++i)
        {
            this.m_state[i] = ESquareState.WHITE;
        }

        // in order to guarantee that we have a solvable puzzle (because what good is a puzzle
        // game that is impossible to solve), we just randomly "push" a bunch of the switches
        // to set the initial state of the board.  this guarantees there is a sequence of button
        // presses that WILL lead to a solution
        // pick random button presses to simulate
        Random r = new Random();
        for (int i = 0; i < 100; ++i)
        {
            switch (r.nextInt(10))
            {
                // press the button corresponding to the random [A->J]
                case 0: this.on_button_click_switch(findViewById(R.id.button_switch_a), true); break;
                case 1: this.on_button_click_switch(findViewById(R.id.button_switch_b), true); break;
                case 2: this.on_button_click_switch(findViewById(R.id.button_switch_c), true); break;
                case 3: this.on_button_click_switch(findViewById(R.id.button_switch_d), true); break;
                case 4: this.on_button_click_switch(findViewById(R.id.button_switch_e), true); break;
                case 5: this.on_button_click_switch(findViewById(R.id.button_switch_f), true); break;
                case 6: this.on_button_click_switch(findViewById(R.id.button_switch_g), true); break;
                case 7: this.on_button_click_switch(findViewById(R.id.button_switch_h), true); break;
                case 8: this.on_button_click_switch(findViewById(R.id.button_switch_i), true); break;
                case 9: this.on_button_click_switch(findViewById(R.id.button_switch_j), true); break;
            }
        }

        // set the visible squares accordingly
        this.set_square_color(R.id.board_square_0, 0, this.m_state[0]);
        this.set_square_color(R.id.board_square_1, 1, this.m_state[1]);
        this.set_square_color(R.id.board_square_2, 2, this.m_state[2]);
        this.set_square_color(R.id.board_square_3, 3, this.m_state[3]);
        this.set_square_color(R.id.board_square_4, 4, this.m_state[4]);
        this.set_square_color(R.id.board_square_5, 5, this.m_state[5]);
        this.set_square_color(R.id.board_square_6, 6, this.m_state[6]);
        this.set_square_color(R.id.board_square_7, 7, this.m_state[7]);
        this.set_square_color(R.id.board_square_8, 8, this.m_state[8]);
        this.set_square_color(R.id.board_square_9, 9, this.m_state[9]);
        this.set_square_color(R.id.board_square_10, 10, this.m_state[10]);
        this.set_square_color(R.id.board_square_11, 11, this.m_state[11]);
        this.set_square_color(R.id.board_square_12, 12, this.m_state[12]);
        this.set_square_color(R.id.board_square_13, 13, this.m_state[13]);
        this.set_square_color(R.id.board_square_14, 14, this.m_state[14]);
        this.set_square_color(R.id.board_square_15, 15, this.m_state[15]);

        // no moves yet
        this.m_move_count = 0;
        ((TextView)findViewById(R.id.move_count)).setText("Move Count: " + this.m_move_count);

        // no sequence yet
        this.m_sequence = "";
        ((TextView)findViewById(R.id.sequence)).setText("Sequence: " + this.m_sequence);

        // ensure all buttons enabled
        findViewById(R.id.button_switch_a).setEnabled(true);
        findViewById(R.id.button_switch_b).setEnabled(true);
        findViewById(R.id.button_switch_c).setEnabled(true);
        findViewById(R.id.button_switch_d).setEnabled(true);
        findViewById(R.id.button_switch_e).setEnabled(true);
        findViewById(R.id.button_switch_f).setEnabled(true);
        findViewById(R.id.button_switch_g).setEnabled(true);
        findViewById(R.id.button_switch_h).setEnabled(true);
        findViewById(R.id.button_switch_i).setEnabled(true);
        findViewById(R.id.button_switch_j).setEnabled(true);
        findViewById(R.id.button_auto).setEnabled(true);
        findViewById(R.id.button_restart).setEnabled(true);
    }

    private void set_square_color(int square_button_id, int square_index, ESquareState color)
    {
        this.m_state[square_index] = color;
        if (this.m_state[square_index] == ESquareState.BLACK)
        {
            ((Button) findViewById(square_button_id)).setBackgroundColor(Color.BLACK);
            ((Button) findViewById(square_button_id)).setTextColor(Color.WHITE);
        }
        else
        {
            ((Button) findViewById(square_button_id)).setBackgroundColor(Color.WHITE);
            ((Button) findViewById(square_button_id)).setTextColor(Color.BLACK);
        }
    }

    private void toggle_square_color(int square_button_id, int square_index, ESquareState[] state, boolean square_toggle)
    {
        if (state[square_index] == ESquareState.BLACK)
        {
            state[square_index] = ESquareState.WHITE;
            if (square_toggle == true)
            {
                ((Button) findViewById(square_button_id)).setBackgroundColor(Color.WHITE);
                ((Button) findViewById(square_button_id)).setTextColor(Color.BLACK);
            }
        }
        else
        {
            state[square_index] = ESquareState.BLACK;
            if (square_toggle == true)
            {
                ((Button) findViewById(square_button_id)).setBackgroundColor(Color.BLACK);
                ((Button) findViewById(square_button_id)).setTextColor(Color.WHITE);
            }
        }
    }

    public void on_button_click_restart(View view)
    {
        this.restart();
    }

    public void on_button_click_auto(View view)
    {
        // first thing to do is see if it is already solved....really
        if (this.check_pattern_for_victory("") == true)
        {
            // just show a toast that says already solved
            Toast.makeText(this.getBaseContext(), "Puzzle is already solved!!!", Toast.LENGTH_LONG).show();
            return;
        }

        // disable all buttons while running the auto-solver
        findViewById(R.id.button_switch_a).setEnabled(false);
        findViewById(R.id.button_switch_b).setEnabled(false);
        findViewById(R.id.button_switch_c).setEnabled(false);
        findViewById(R.id.button_switch_d).setEnabled(false);
        findViewById(R.id.button_switch_e).setEnabled(false);
        findViewById(R.id.button_switch_f).setEnabled(false);
        findViewById(R.id.button_switch_g).setEnabled(false);
        findViewById(R.id.button_switch_h).setEnabled(false);
        findViewById(R.id.button_switch_i).setEnabled(false);
        findViewById(R.id.button_switch_j).setEnabled(false);
        findViewById(R.id.button_auto).setEnabled(false);
        findViewById(R.id.button_restart).setEnabled(false);

        // run the solver on a thread
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                // user our solver to find a solution
                final String solution = MainActivity.this.auto_solve_v3();
                MainActivity.this.m_sequence = "";
                MainActivity.this.m_move_count = 0;

                // see if we got a solution
                if (solution != "")
                {
                    // we actually got a solution
                    // put the solution in the sequence text view
                    ((TextView)findViewById(R.id.sequence)).post(new Runnable() {
                        @Override
                        public void run() {
                            ((TextView)findViewById(R.id.sequence)).setText("Sequence: " + solution);
                        }
                    });
                    // put the move count for the solution in the text view
                    ((TextView)findViewById(R.id.move_count)).post(new Runnable() {
                        @Override
                        public void run() {
                            ((TextView)findViewById(R.id.move_count)).setText("Move Count: " + Integer.toString(solution.length()));
                        }
                    });
                }
                else
                {
                    // we did not get a solution...so say so in the text view
                    ((TextView)findViewById(R.id.sequence)).post(new Runnable() {
                        @Override
                        public void run() {
                            ((TextView)findViewById(R.id.sequence)).setText("Sequence: No Solution");
                        }
                    });
                    ((TextView)findViewById(R.id.move_count)).post(new Runnable() {
                        @Override
                        public void run() {
                            ((TextView)findViewById(R.id.move_count)).setText("Move Count: ");
                        }
                    });

                    // show a quick toast also that there is no solution
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this.getBaseContext(), "No Solution Exists", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                // now check if we have a solution
                if (solution != "")
                {
                    // we have a solution so lets animate the pressing of buttons
                    // puase before starting
                    SystemClock.sleep(1500);

                    // loop through all the solution switches
                    for (int i = 0; i < solution.length(); ++i)
                    {
                        // simulate the button press
                        switch (solution.charAt(i))
                        {
                            case 'A':
                            {
                                findViewById(R.id.button_switch_a).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.button_switch_a).setBackgroundColor(Color.RED);
                                        findViewById(R.id.button_switch_a).performClick();
                                    }
                                });
                                SystemClock.sleep(1500);
                                findViewById(R.id.button_switch_a).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.button_switch_a).setBackgroundResource(android.R.drawable.btn_default);
                                    }
                                });
                            } break;
                            case 'B':
                            {
                                findViewById(R.id.button_switch_b).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.button_switch_b).setBackgroundColor(Color.RED);
                                        findViewById(R.id.button_switch_b).performClick();
                                    }
                                });
                                SystemClock.sleep(1500);
                                findViewById(R.id.button_switch_b).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.button_switch_b).setBackgroundResource(android.R.drawable.btn_default);
                                    }
                                });
                            } break;
                            case 'C':
                            {
                                findViewById(R.id.button_switch_c).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.button_switch_c).setBackgroundColor(Color.RED);
                                        findViewById(R.id.button_switch_c).performClick();
                                    }
                                });
                                SystemClock.sleep(1500);
                                findViewById(R.id.button_switch_c).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.button_switch_c).setBackgroundResource(android.R.drawable.btn_default);
                                    }
                                });
                            } break;
                            case 'D':
                            {
                                findViewById(R.id.button_switch_d).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.button_switch_d).setBackgroundColor(Color.RED);
                                        findViewById(R.id.button_switch_d).performClick();
                                    }
                                });
                                SystemClock.sleep(1500);
                                findViewById(R.id.button_switch_d).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.button_switch_d).setBackgroundResource(android.R.drawable.btn_default);
                                    }
                                });
                            } break;
                            case 'E':
                            {
                                findViewById(R.id.button_switch_e).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.button_switch_e).setBackgroundColor(Color.RED);
                                        findViewById(R.id.button_switch_e).performClick();
                                    }
                                });
                                SystemClock.sleep(1500);
                                findViewById(R.id.button_switch_e).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.button_switch_e).setBackgroundResource(android.R.drawable.btn_default);
                                    }
                                });
                            } break;
                            case 'F':
                            {
                                findViewById(R.id.button_switch_f).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.button_switch_f).setBackgroundColor(Color.RED);
                                        findViewById(R.id.button_switch_f).performClick();
                                    }
                                });
                                SystemClock.sleep(1500);
                                findViewById(R.id.button_switch_f).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.button_switch_f).setBackgroundResource(android.R.drawable.btn_default);
                                    }
                                });
                            } break;
                            case 'G':
                            {
                                findViewById(R.id.button_switch_g).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.button_switch_g).setBackgroundColor(Color.RED);
                                        findViewById(R.id.button_switch_g).performClick();
                                    }
                                });
                                SystemClock.sleep(1500);
                                findViewById(R.id.button_switch_g).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.button_switch_g).setBackgroundResource(android.R.drawable.btn_default);
                                    }
                                });
                            } break;
                            case 'H':
                            {
                                findViewById(R.id.button_switch_h).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.button_switch_h).setBackgroundColor(Color.RED);
                                        findViewById(R.id.button_switch_h).performClick();
                                    }
                                });
                                SystemClock.sleep(1500);
                                findViewById(R.id.button_switch_h).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.button_switch_h).setBackgroundResource(android.R.drawable.btn_default);
                                    }
                                });
                            } break;
                            case 'I':
                            {
                                findViewById(R.id.button_switch_i).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.button_switch_i).setBackgroundColor(Color.RED);
                                        findViewById(R.id.button_switch_i).performClick();
                                    }
                                });
                                SystemClock.sleep(1500);
                                findViewById(R.id.button_switch_i).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.button_switch_i).setBackgroundResource(android.R.drawable.btn_default);
                                    }
                                });
                            } break;
                            case 'J':
                            {
                                findViewById(R.id.button_switch_j).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.button_switch_j).setBackgroundColor(Color.RED);
                                        findViewById(R.id.button_switch_j).performClick();
                                    }
                                });
                                SystemClock.sleep(1500);
                                findViewById(R.id.button_switch_j).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.button_switch_j).setBackgroundResource(android.R.drawable.btn_default);
                                    }
                                });
                            } break;
                        }
                    }
                }

                // one more pause
                SystemClock.sleep(1500);

                // re-enable all buttons
                // actually just the restart button should be enabled
                // once a puzzle is solved
                /*
                findViewById(R.id.button_switch_a).post(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.button_switch_a).setEnabled(true);
                    }
                });
                findViewById(R.id.button_switch_b).post(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.button_switch_b).setEnabled(true);
                    }
                });
                findViewById(R.id.button_switch_c).post(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.button_switch_c).setEnabled(true);
                    }
                });
                findViewById(R.id.button_switch_d).post(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.button_switch_d).setEnabled(true);
                    }
                });
                findViewById(R.id.button_switch_e).post(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.button_switch_e).setEnabled(true);
                    }
                });
                findViewById(R.id.button_switch_f).post(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.button_switch_f).setEnabled(true);
                    }
                });
                findViewById(R.id.button_switch_g).post(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.button_switch_g).setEnabled(true);
                    }
                });
                findViewById(R.id.button_switch_h).post(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.button_switch_h).setEnabled(true);
                    }
                });
                findViewById(R.id.button_switch_i).post(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.button_switch_i).setEnabled(true);
                    }
                });
                findViewById(R.id.button_switch_j).post(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.button_switch_j).setEnabled(true);
                    }
                });
                findViewById(R.id.button_auto).post(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.button_auto).setEnabled(true);
                    }
                });*/
                findViewById(R.id.button_restart).post(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.button_restart).setEnabled(true);
                    }
                });
            }
        });
        thread.start();
    }

    /*
    private String auto_solve_v1(String available_switches, ESquareState[] incoming_state)
    {
        if (available_switches.length() == 0)
        {
            // no more to check
            return "";
        }

        Log.v("AUTOSOLVE", "Checking: " + available_switches);

        // just something really long as a temp
        String best_solution = "";

        // loop through the avaialble switches one at a time and see if any of them solve
        for (int i = 0; i < available_switches.length(); ++i)
        {
            char c = available_switches.charAt(i);

            // apply the flip of the c switch to our state
            switch (c)
            {
                case 'A':
                {
                    // make a copy of the state
                    ESquareState[] state = new ESquareState[16];
                    for (int j = 0; j < 16; ++j)
                    {
                        state[j] = incoming_state[j];
                    }
                    // permute it based on A switch
                    this.toggle_square_color(0, 0, state, false);
                    this.toggle_square_color(0, 1, state, false);
                    this.toggle_square_color(0, 2, state, false);

                    // check for victory
                    if (this.check_for_victory(state) == true)
                    {
                        // found a solution
                        // this will always be the best (or tied for it but this one is first)
                        // as it is just one switch so a length of 1
                        // return just this letter as the solution
                        return "A";
                    }
                    // no victory yet so recurse this down
                    // use the string builder to remove the switch we just used from the available ones
                    StringBuilder sb = new StringBuilder(available_switches);
                    sb.deleteCharAt(i);
                    // call recurse
                    String recurse_solution = this.auto_solve(sb.toString(), state);
                    // see if we got a solution
                    if (recurse_solution != "")
                    {
                        // we did get a solution..is it the best?
                        if (((recurse_solution.length() + 1) < best_solution.length()) || (best_solution.length() == 0))
                        {
                            // it is the new best
                            // we store but dont return immediately because another route may be better
                            best_solution = "A" + recurse_solution;
                        }
                    }
                } break;
                case 'B':
                {
                    // make a copy of the state
                    ESquareState[] state = new ESquareState[16];
                    for (int j = 0; j < 16; ++j)
                    {
                        state[j] = incoming_state[j];
                    }
                    // permute it based on B switch
                    this.toggle_square_color(0, 3, state, false);
                    this.toggle_square_color(0, 7, state, false);
                    this.toggle_square_color(0, 9, state, false);
                    this.toggle_square_color(0, 11, state, false);

                    // check for victory
                    if (this.check_for_victory(state) == true)
                    {
                        // found a solution
                        // this will always be the best (or tied for it but this one is first)
                        // as it is just one switch so a length of 1
                        // return just this letter as the solution
                        return "B";
                    }
                    // no victory yet so recurse this down
                    // use the string builder to remove the switch we just used from the available ones
                    StringBuilder sb = new StringBuilder(available_switches);
                    sb.deleteCharAt(i);
                    // call recurse
                    String recurse_solution = this.auto_solve(sb.toString(), state);
                    // see if we got a solution
                    if (recurse_solution != "")
                    {
                        // we did get a solution..is it the best?
                        if (((recurse_solution.length() + 1) < best_solution.length()) || (best_solution.length() == 0))
                        {
                            // it is the new best
                            // we store but dont return immediately because another route may be better
                            best_solution = "B" + recurse_solution;
                        }
                    }
                } break;
                case 'C':
                {
                    // make a copy of the state
                    ESquareState[] state = new ESquareState[16];
                    for (int j = 0; j < 16; ++j)
                    {
                        state[j] = incoming_state[j];
                    }
                    // permute it based on B switch
                    this.toggle_square_color(0, 4, state, false);
                    this.toggle_square_color(0, 10, state, false);
                    this.toggle_square_color(0, 14, state, false);
                    this.toggle_square_color(0, 15, state, false);

                    // check for victory
                    if (this.check_for_victory(state) == true)
                    {
                        // found a solution
                        // this will always be the best (or tied for it but this one is first)
                        // as it is just one switch so a length of 1
                        // return just this letter as the solution
                        return "C";
                    }
                    // no victory yet so recurse this down
                    // use the string builder to remove the switch we just used from the available ones
                    StringBuilder sb = new StringBuilder(available_switches);
                    sb.deleteCharAt(i);
                    // call recurse
                    String recurse_solution = this.auto_solve(sb.toString(), state);
                    // see if we got a solution
                    if (recurse_solution != "")
                    {
                        // we did get a solution..is it the best?
                        if (((recurse_solution.length() + 1) < best_solution.length()) || (best_solution.length() == 0))
                        {
                            // it is the new best
                            // we store but dont return immediately because another route may be better
                            best_solution = "C" + recurse_solution;
                        }
                    }
                } break;
                case 'D':
                {
                    // make a copy of the state
                    ESquareState[] state = new ESquareState[16];
                    for (int j = 0; j < 16; ++j)
                    {
                        state[j] = incoming_state[j];
                    }
                    // permute it based on B switch
                    this.toggle_square_color(0, 0, state, false);
                    this.toggle_square_color(0, 4, state, false);
                    this.toggle_square_color(0, 5, state, false);
                    this.toggle_square_color(0, 6, state, false);
                    this.toggle_square_color(0, 7, state, false);

                    // check for victory
                    if (this.check_for_victory(state) == true)
                    {
                        // found a solution
                        // this will always be the best (or tied for it but this one is first)
                        // as it is just one switch so a length of 1
                        // return just this letter as the solution
                        return "D";
                    }
                    // no victory yet so recurse this down
                    // use the string builder to remove the switch we just used from the available ones
                    StringBuilder sb = new StringBuilder(available_switches);
                    sb.deleteCharAt(i);
                    // call recurse
                    String recurse_solution = this.auto_solve(sb.toString(), state);
                    // see if we got a solution
                    if (recurse_solution != "")
                    {
                        // we did get a solution..is it the best?
                        if (((recurse_solution.length() + 1) < best_solution.length()) || (best_solution.length() == 0))
                        {
                            // it is the new best
                            // we store but dont return immediately because another route may be better
                            best_solution = "D" + recurse_solution;
                        }
                    }
                } break;
                case 'E':
                {
                    // make a copy of the state
                    ESquareState[] state = new ESquareState[16];
                    for (int j = 0; j < 16; ++j)
                    {
                        state[j] = incoming_state[j];
                    }
                    // permute it based on B switch
                    this.toggle_square_color(0, 6, state, false);
                    this.toggle_square_color(0, 7, state, false);
                    this.toggle_square_color(0, 8, state, false);
                    this.toggle_square_color(0, 10, state, false);
                    this.toggle_square_color(0, 12, state, false);

                    // check for victory
                    if (this.check_for_victory(state) == true)
                    {
                        // found a solution
                        // this will always be the best (or tied for it but this one is first)
                        // as it is just one switch so a length of 1
                        // return just this letter as the solution
                        return "E";
                    }
                    // no victory yet so recurse this down
                    // use the string builder to remove the switch we just used from the available ones
                    StringBuilder sb = new StringBuilder(available_switches);
                    sb.deleteCharAt(i);
                    // call recurse
                    String recurse_solution = this.auto_solve(sb.toString(), state);
                    // see if we got a solution
                    if (recurse_solution != "")
                    {
                        // we did get a solution..is it the best?
                        if (((recurse_solution.length() + 1) < best_solution.length()) || (best_solution.length() == 0))
                        {
                            // it is the new best
                            // we store but dont return immediately because another route may be better
                            best_solution = "E" + recurse_solution;
                        }
                    }
                } break;
                case 'F':
                {
                    // make a copy of the state
                    ESquareState[] state = new ESquareState[16];
                    for (int j = 0; j < 16; ++j)
                    {
                        state[j] = incoming_state[j];
                    }
                    // permute it based on B switch
                    this.toggle_square_color(0, 0, state, false);
                    this.toggle_square_color(0, 2, state, false);
                    this.toggle_square_color(0, 14, state, false);
                    this.toggle_square_color(0, 15, state, false);

                    // check for victory
                    if (this.check_for_victory(state) == true)
                    {
                        // found a solution
                        // this will always be the best (or tied for it but this one is first)
                        // as it is just one switch so a length of 1
                        // return just this letter as the solution
                        return "F";
                    }
                    // no victory yet so recurse this down
                    // use the string builder to remove the switch we just used from the available ones
                    StringBuilder sb = new StringBuilder(available_switches);
                    sb.deleteCharAt(i);
                    // call recurse
                    String recurse_solution = this.auto_solve(sb.toString(), state);
                    // see if we got a solution
                    if (recurse_solution != "")
                    {
                        // we did get a solution..is it the best?
                        if (((recurse_solution.length() + 1) < best_solution.length()) || (best_solution.length() == 0))
                        {
                            // it is the new best
                            // we store but dont return immediately because another route may be better
                            best_solution = "F" + recurse_solution;
                        }
                    }
                } break;
                case 'G':
                {
                    // make a copy of the state
                    ESquareState[] state = new ESquareState[16];
                    for (int j = 0; j < 16; ++j)
                    {
                        state[j] = incoming_state[j];
                    }
                    // permute it based on B switch
                    this.toggle_square_color(0, 3, state, false);
                    this.toggle_square_color(0, 14, state, false);
                    this.toggle_square_color(0, 15, state, false);

                    // check for victory
                    if (this.check_for_victory(state) == true)
                    {
                        // found a solution
                        // this will always be the best (or tied for it but this one is first)
                        // as it is just one switch so a length of 1
                        // return just this letter as the solution
                        return "G";
                    }
                    // no victory yet so recurse this down
                    // use the string builder to remove the switch we just used from the available ones
                    StringBuilder sb = new StringBuilder(available_switches);
                    sb.deleteCharAt(i);
                    // call recurse
                    String recurse_solution = this.auto_solve(sb.toString(), state);
                    // see if we got a solution
                    if (recurse_solution != "")
                    {
                        // we did get a solution..is it the best?
                        if (((recurse_solution.length() + 1) < best_solution.length()) || (best_solution.length() == 0))
                        {
                            // it is the new best
                            // we store but dont return immediately because another route may be better
                            best_solution = "G" + recurse_solution;
                        }
                    }
                } break;
                case 'H':
                {
                    // make a copy of the state
                    ESquareState[] state = new ESquareState[16];
                    for (int j = 0; j < 16; ++j)
                    {
                        state[j] = incoming_state[j];
                    }
                    // permute it based on B switch
                    this.toggle_square_color(0, 4, state, false);
                    this.toggle_square_color(0, 5, state, false);
                    this.toggle_square_color(0, 7, state, false);
                    this.toggle_square_color(0, 14, state, false);
                    this.toggle_square_color(0, 15, state, false);

                    // check for victory
                    if (this.check_for_victory(state) == true)
                    {
                        // found a solution
                        // this will always be the best (or tied for it but this one is first)
                        // as it is just one switch so a length of 1
                        // return just this letter as the solution
                        return "H";
                    }
                    // no victory yet so recurse this down
                    // use the string builder to remove the switch we just used from the available ones
                    StringBuilder sb = new StringBuilder(available_switches);
                    sb.deleteCharAt(i);
                    // call recurse
                    String recurse_solution = this.auto_solve(sb.toString(), state);
                    // see if we got a solution
                    if (recurse_solution != "")
                    {
                        // we did get a solution..is it the best?
                        if (((recurse_solution.length() + 1) < best_solution.length()) || (best_solution.length() == 0))
                        {
                            // it is the new best
                            // we store but dont return immediately because another route may be better
                            best_solution = "H" + recurse_solution;
                        }
                    }
                } break;
                case 'I':
                {
                    // make a copy of the state
                    ESquareState[] state = new ESquareState[16];
                    for (int j = 0; j < 16; ++j)
                    {
                        state[j] = incoming_state[j];
                    }
                    // permute it based on B switch
                    this.toggle_square_color(0, 1, state, false);
                    this.toggle_square_color(0, 2, state, false);
                    this.toggle_square_color(0, 3, state, false);
                    this.toggle_square_color(0, 4, state, false);
                    this.toggle_square_color(0, 5, state, false);

                    // check for victory
                    if (this.check_for_victory(state) == true)
                    {
                        // found a solution
                        // this will always be the best (or tied for it but this one is first)
                        // as it is just one switch so a length of 1
                        // return just this letter as the solution
                        return "I";
                    }
                    // no victory yet so recurse this down
                    // use the string builder to remove the switch we just used from the available ones
                    StringBuilder sb = new StringBuilder(available_switches);
                    sb.deleteCharAt(i);
                    // call recurse
                    String recurse_solution = this.auto_solve(sb.toString(), state);
                    // see if we got a solution
                    if (recurse_solution != "")
                    {
                        // we did get a solution..is it the best?
                        if (((recurse_solution.length() + 1) < best_solution.length()) || (best_solution.length() == 0))
                        {
                            // it is the new best
                            // we store but dont return immediately because another route may be better
                            best_solution = "I" + recurse_solution;
                        }
                    }
                } break;
                case 'J':
                {
                    // make a copy of the state
                    ESquareState[] state = new ESquareState[16];
                    for (int j = 0; j < 16; ++j)
                    {
                        state[j] = incoming_state[j];
                    }
                    // permute it based on B switch
                    this.toggle_square_color(0, 3, state, false);
                    this.toggle_square_color(0, 4, state, false);
                    this.toggle_square_color(0, 5, state, false);
                    this.toggle_square_color(0, 9, state, false);
                    this.toggle_square_color(0, 13, state, false);

                    // check for victory
                    if (this.check_for_victory(state) == true)
                    {
                        // found a solution
                        // this will always be the best (or tied for it but this one is first)
                        // as it is just one switch so a length of 1
                        // return just this letter as the solution
                        return "J";
                    }
                    // no victory yet so recurse this down
                    // use the string builder to remove the switch we just used from the available ones
                    StringBuilder sb = new StringBuilder(available_switches);
                    sb.deleteCharAt(i);
                    // call recurse
                    String recurse_solution = this.auto_solve(sb.toString(), state);
                    // see if we got a solution
                    if (recurse_solution != "")
                    {
                        // we did get a solution..is it the best?
                        if (((recurse_solution.length() + 1) < best_solution.length()) || (best_solution.length() == 0))
                        {
                            // it is the new best
                            // we store but dont return immediately because another route may be better
                            best_solution = "J" + recurse_solution;
                        }
                    }
                } break;
            }
        }

        // now determine if we had a best solution and if so return it
        return best_solution;
    }
*/

    /*
    private String auto_solve_v2()
    {
        // the different switches
        String switches = "ABCDEFGHIJ";

        // queue to hold possible patterns to search
        Queue<String> queue = new LinkedList<String>();

        // add the root to the queue
        queue.add("");

        // keep going while the queue is not empty
        while (queue.isEmpty() == false)
        {
            // get the current pattern to evaluate
            String current = queue.poll();

            Log.v("AUTOSOLVE", "Checking: " + current);

            // see if applying the pattern to the state results in victory
            if (this.check_pattern_for_victory(current) == true)
            {
                // we have a solving pattern...since we are doing a breadth first search
                // it is guaranteed to be the best, or at least tied, so we go with it
                return current;
            }

            // this wasnt a win
            // push onto the queue the current + one more possible letter combinations
            // this means current + all possibilities for the next letter that have NOT been used yet in current
            for (int i = 0; i < switches.length(); ++i)
            {
                // determine if current already has switches.charAt(i) yet
                char c = switches.charAt(i);
                if (current.indexOf(c) == -1)
                {
                    // letter has not yet occured so we can use this one
                    queue.add(current + c);
                }
            }
        }

        // found no solution
        return "";
    }
*/

    private String auto_solve_v3()
    {
        // set the initial conditions for the resursive solver
        // try to use solutions of increasing number of switches
        // starting from 0 buttons up to all 10 switches
        for (int i = 0; i <= 10; ++i)
        {
            // get a solution for the number of switches
            String solution = auto_solve_v3_recurse("ABCDEFGHIJ", i, "", 0);

            // see if we got a solution
            if (solution != "")
            {
                // we do so return it
                return solution;
            }
        }

        // no solution :(
        return "";
    }

    private String auto_solve_v3_recurse(String all_switches, int num_switches, String current_switches, int index)
    {
        // go through the possible switches for this lvel
        for (int i = index; i < all_switches.length(); ++i)
        {
            // see if we have reched the number of switches for this check
            if(current_switches.length() + 1 == num_switches)
            {
                // appripriate number of switches collected
                // check this one for victory
                Log.v("AUTOSOLVE", "Checking: " + current_switches + all_switches.charAt(i));
                if (this.check_pattern_for_victory(current_switches + all_switches.charAt(i)) == true)
                {
                    // winner winner chicken dinner
                    return (current_switches + all_switches.charAt(i));
                }
            }
            else
            {
                // still not at the appropriate number of switches so add more recursively
                String solution = auto_solve_v3_recurse(all_switches, num_switches, current_switches + all_switches.charAt(i), i + 1);

                // see if it returned a solution
                if (solution != "")
                {
                    // we do have a solution so return it
                    return solution;
                }
            }
        }

        // no solution to be found down this path
        return "";
    }

    public void on_button_click_switch(View view)
    {
        this.on_button_click_switch(view, false);
    }

    public void on_button_click_switch(View view, boolean setup_only)
    {
        // determine which switch was clicked
        switch (view.getId())
        {
            case R.id.button_switch_a:
            {
                this.toggle_square_color(R.id.board_square_0, 0, this.m_state, true);
                this.toggle_square_color(R.id.board_square_1, 1, this.m_state, true);
                this.toggle_square_color(R.id.board_square_2, 2, this.m_state, true);
                this.m_sequence += "A";
            } break;
            case R.id.button_switch_b:
            {
                this.toggle_square_color(R.id.board_square_3, 3, this.m_state, true);
                this.toggle_square_color(R.id.board_square_7, 7, this.m_state, true);
                this.toggle_square_color(R.id.board_square_9, 9, this.m_state, true);
                this.toggle_square_color(R.id.board_square_11, 11, this.m_state, true);
                this.m_sequence += "B";
            } break;
            case R.id.button_switch_c:
            {
                this.toggle_square_color(R.id.board_square_4, 4, this.m_state, true);
                this.toggle_square_color(R.id.board_square_10, 10, this.m_state, true);
                this.toggle_square_color(R.id.board_square_14, 14, this.m_state, true);
                this.toggle_square_color(R.id.board_square_15, 15, this.m_state, true);
                this.m_sequence += "C";
            } break;
            case R.id.button_switch_d:
            {
                this.toggle_square_color(R.id.board_square_0, 0, this.m_state, true);
                this.toggle_square_color(R.id.board_square_4, 4, this.m_state, true);
                this.toggle_square_color(R.id.board_square_5, 5, this.m_state, true);
                this.toggle_square_color(R.id.board_square_6, 6, this.m_state, true);
                this.toggle_square_color(R.id.board_square_7, 7, this.m_state, true);
                this.m_sequence += "D";
            } break;
            case R.id.button_switch_e:
            {
                this.toggle_square_color(R.id.board_square_6, 6, this.m_state, true);
                this.toggle_square_color(R.id.board_square_7, 7, this.m_state, true);
                this.toggle_square_color(R.id.board_square_8, 8, this.m_state, true);
                this.toggle_square_color(R.id.board_square_10, 10, this.m_state, true);
                this.toggle_square_color(R.id.board_square_12, 12, this.m_state, true);
                this.m_sequence += "E";
            } break;
            case R.id.button_switch_f:
            {
                this.toggle_square_color(R.id.board_square_0, 0, this.m_state, true);
                this.toggle_square_color(R.id.board_square_2, 2, this.m_state, true);
                this.toggle_square_color(R.id.board_square_14, 14, this.m_state, true);
                this.toggle_square_color(R.id.board_square_15, 15, this.m_state, true);
                this.m_sequence += "F";
            } break;
            case R.id.button_switch_g:
            {
                this.toggle_square_color(R.id.board_square_3, 3, this.m_state, true);
                this.toggle_square_color(R.id.board_square_14, 14, this.m_state, true);
                this.toggle_square_color(R.id.board_square_15, 15, this.m_state, true);
                this.m_sequence += "G";
            } break;
            case R.id.button_switch_h:
            {
                this.toggle_square_color(R.id.board_square_4, 4, this.m_state, true);
                this.toggle_square_color(R.id.board_square_5, 5, this.m_state, true);
                this.toggle_square_color(R.id.board_square_7, 7, this.m_state, true);
                this.toggle_square_color(R.id.board_square_14, 14, this.m_state, true);
                this.toggle_square_color(R.id.board_square_15, 15, this.m_state, true);
                this.m_sequence += "H";
            } break;
            case R.id.button_switch_i:
            {
                this.toggle_square_color(R.id.board_square_1, 1, this.m_state, true);
                this.toggle_square_color(R.id.board_square_2, 2, this.m_state, true);
                this.toggle_square_color(R.id.board_square_3, 3, this.m_state, true);
                this.toggle_square_color(R.id.board_square_4, 4, this.m_state, true);
                this.toggle_square_color(R.id.board_square_5, 5, this.m_state, true);
                this.m_sequence += "I";
            } break;
            case R.id.button_switch_j:
            {
                this.toggle_square_color(R.id.board_square_3, 3, this.m_state, true);
                this.toggle_square_color(R.id.board_square_4, 4, this.m_state, true);
                this.toggle_square_color(R.id.board_square_5, 5, this.m_state, true);
                this.toggle_square_color(R.id.board_square_9, 9, this.m_state, true);
                this.toggle_square_color(R.id.board_square_13, 13, this.m_state, true);
                this.m_sequence += "J";
            } break;
        }

        // if only doing this as part of setup then dont check for victory
        if (setup_only == true)
        {
            return;
        }

        // not part of setup

        // increment move counter
        this.increment_move_count();

        // add to list of input moves
        ((TextView)findViewById(R.id.sequence)).setText("Sequence: " + this.m_sequence);

        // check for victory
        if (this.check_for_victory(this.m_state) == true)
        {
            String s = "You are a winner in " + Integer.toString(this.m_move_count) + " moves!!!";
            Toast.makeText(this.getBaseContext(), s, Toast.LENGTH_LONG).show();
        }
    }

    private void increment_move_count()
    {
        this.m_move_count += 1;
        ((TextView)findViewById(R.id.move_count)).setText("Move Count: " + this.m_move_count);
    }

    private boolean check_for_victory(ESquareState[] state)
    {
        // count the occourances of a black square
        int count = 0;
        for (int i = 0; i < 16; ++i)
        {
            if (state[i] == ESquareState.BLACK)
            {
                count += 1;
            }
        }

        if (count == 0)
        {
            // all white squares so win!!
            return true;
        }

        if (count == 16)
        {
            // all black squares so win!!!
            return true;
        }

        // no win :(
        return false;
    }

    private boolean check_pattern_for_victory(String pattern)
    {
        // construct a local state copy of the original
        for (int i = 0; i < 16; ++i)
        {
            this.m_check_pattern_for_victory_state[i] = this.m_state[i];
        }

        // apply all the lettered switches in the pattern
        for (int i = 0; i < pattern.length(); ++i)
        {
            switch (pattern.charAt(i))
            {
                case 'A':
                {
                    this.toggle_square_color(0, 0, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 1, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 2, this.m_check_pattern_for_victory_state, false);
                } break;
                case 'B':
                {
                    this.toggle_square_color(0, 3, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 7, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 9, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 11, this.m_check_pattern_for_victory_state, false);
                } break;
                case 'C':
                {
                    this.toggle_square_color(0, 4, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 10, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 14, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 15, this.m_check_pattern_for_victory_state, false);
                } break;
                case 'D':
                {
                    this.toggle_square_color(0, 0, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 4, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 5, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 6, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 7, this.m_check_pattern_for_victory_state, false);
                } break;
                case 'E':
                {
                    this.toggle_square_color(0, 6, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 7, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 8, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 10, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 12, this.m_check_pattern_for_victory_state, false);
                } break;
                case 'F':
                {
                    this.toggle_square_color(0, 0, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 2, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 14, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 15, this.m_check_pattern_for_victory_state, false);
                } break;
                case 'G':
                {
                    this.toggle_square_color(0, 3, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 14, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 15, this.m_check_pattern_for_victory_state, false);
                } break;
                case 'H':
                {
                    this.toggle_square_color(0, 4, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 5, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 7, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 14, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 15, this.m_check_pattern_for_victory_state, false);
                } break;
                case 'I':
                {
                    this.toggle_square_color(0, 1, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 2, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 3, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 4, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 5, this.m_check_pattern_for_victory_state, false);
                } break;
                case 'J':
                {
                    this.toggle_square_color(0, 3, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 4, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 5, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 9, this.m_check_pattern_for_victory_state, false);
                    this.toggle_square_color(0, 13, this.m_check_pattern_for_victory_state, false);
                } break;
            }
        }

        // pattern applied
        // check for victory!!!
        if (this.check_for_victory(this.m_check_pattern_for_victory_state) == true)
        {
            // found a winning pattern
            return true;
        }

        // nope nope
        return false;
    }
}
