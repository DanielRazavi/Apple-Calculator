package com.example.applecalculator;

import android.media.session.MediaSession;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.example.applecalculator.objcts.addition;
import com.example.applecalculator.objcts.division;
import com.example.applecalculator.objcts.multiplication;
import com.example.applecalculator.objcts.numbers;
import com.example.applecalculator.objcts.operations;
import com.example.applecalculator.objcts.subtraction;
import com.example.applecalculator.objcts.token;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Stack;
import java.util.Queue;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Views
    private ImageButton zero_button, one_button, two_button, three_button, four_button, five_button,
            six_button, seven_button, eight_button, nine_button;
    private ImageButton percent_button,period_button,posneg_button,process_button,clear_button,
            multi_button,division_button,add_button,subtract_button;
    TextView mathView;

    //Data Structures
    Stack<token> operations_stack;
    Queue<token> multi_usage_queue;

    public static String math_view = "0";

    operations new_opt = null;

    //flags
    Boolean reset_flag = true;
    Boolean used_already = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setup_views();
        setup_listeners();

        operations_stack = new Stack<>();
        multi_usage_queue = new LinkedList<>();

    }

    /**
     * Setting up the views in one convenient function. Mostly because it's more organised like
     * this.
     */
    private void setup_views(){

        //interface
        mathView = findViewById(R.id.mathview);
        mathView.setText(math_view);

        //number buttons
        zero_button = findViewById(R.id.zerobutton);
        one_button = findViewById(R.id.onebutton);
        two_button = findViewById(R.id.twobutton);
        three_button = findViewById(R.id.threebutton);
        four_button = findViewById(R.id.fourbutton);
        five_button = findViewById(R.id.fivebutton);
        six_button = findViewById(R.id.sixbutton);
        seven_button = findViewById(R.id.sevenbutton);
        eight_button = findViewById(R.id.eightbutton);
        nine_button = findViewById(R.id.ninebutton);

        //special buttons
        percent_button = findViewById(R.id.percentbutton);
        period_button = findViewById(R.id.periodbutton);
        posneg_button = findViewById(R.id.pnbutton);
        process_button = findViewById(R.id.processbutton);
        clear_button = findViewById(R.id.acbutton);

        //operation buttons
        multi_button = findViewById(R.id.multibutton);
        division_button = findViewById(R.id.dvsnbutton);
        add_button = findViewById(R.id.addbutton);
        subtract_button = findViewById(R.id.subtractbutton);

    }

    /**
     * Setting up the listeners for the views in one convenient function. Mostly beacause it's more
     * origanised like this.
     */
    private void setup_listeners() {

        //number button listeners
        zero_button.setOnClickListener(this);
        one_button.setOnClickListener(this);
        two_button.setOnClickListener(this);
        three_button.setOnClickListener(this);
        four_button.setOnClickListener(this);
        five_button.setOnClickListener(this);
        six_button.setOnClickListener(this);
        seven_button.setOnClickListener(this);
        eight_button.setOnClickListener(this);
        nine_button.setOnClickListener(this);

        //special button listeners
        percent_button.setOnClickListener(this);
        period_button.setOnClickListener(this);
        posneg_button.setOnClickListener(this);
        process_button.setOnClickListener(this);
        clear_button.setOnClickListener(this);

        //operation button listeners
        multi_button.setOnClickListener(this);
        division_button.setOnClickListener(this);
        add_button.setOnClickListener(this);
        subtract_button.setOnClickListener(this);
    }


    @Override
    public void onClick(View view){

        if (!number_clicked(view)){

            double translation = Double.parseDouble(math_view);

            //operations check
            if(view.getId() == R.id.multibutton)        { new_opt = new multiplication();}
            else if(view.getId() == R.id.dvsnbutton)    { new_opt = new division();}
            else if(view.getId() == R.id.addbutton)     { new_opt = new addition();}
            else if(view.getId() == R.id.subtractbutton){ new_opt = new subtraction();}

            else if(view.getId() == R.id.percentbutton){
                operations peek_opt = (operations) operations_stack.peek();

                if (translation == 0){
                    math_view = String.valueOf(0);
                }

                else if(peek_opt.get_priority() == 2){      //Multiplication and Division
                    translation = translation / 100;
                }

                else if(peek_opt.get_priority() == 1){      //Addition and Subtraction
                    translation = translation * 0.01
                            * calculate_queue(new LinkedList<>(multi_usage_queue));
                }

            }

            else if(view.getId() == R.id.processbutton){
                numbers current_number = new numbers(translation);
                multi_usage_queue.add(current_number);
                reset_flag = true;
                used_already = false;
                math_view = String.valueOf(calculate_queue(new LinkedList<token>(multi_usage_queue)));
                while (!multi_usage_queue.isEmpty()){
                    multi_usage_queue.remove();
                }
                //something is def not finished here! need to figure it out, don't have enough time
                //right now, gotta run out.

                //gotta give the solution, if there isn't a second operand, you repeat what ever
                //you have for the math_view.
            }

            if (new_opt != null){       //if it's an operation
                update_operations_stack(new_opt);
                operations_stack.push(new_opt);
            }

            numbers current_number = new numbers(translation);
            multi_usage_queue.add(current_number);
            reset_flag = true;
            used_already = false;
        }
    }

    /**
     * Handles numbered buttons' clicks and how numbers are put together behind the scenes.
     * @param view: the view that was clicked
     * @return Boolean representing whether if the clicked view was any of the numbered buttons.
     */
    @NonNull
    private Boolean number_clicked(View view){

        if(view.getId() == R.id.zerobutton){
            if (reset_flag){
                reset_flag = false;
                math_view = "0";
            }else if(!math_view.equals("0")){
                math_view = math_view + "0";
            }
            return true;
        }

        else if(view.getId() == R.id.onebutton){
            if (reset_flag){
                reset_flag = false;
                math_view = "1";
            }else{
                math_view = math_view + "1";
            }
            return true;
        }

        else if(view.getId() == R.id.twobutton){
            if (reset_flag){
                reset_flag = false;
                math_view = "2";
            }else{
                math_view = math_view + "2";
            }
            return true;
        }

        else if(view.getId() == R.id.threebutton){
            if (reset_flag){
                reset_flag = false;
                math_view = "3";
            }else{
                math_view = math_view + "3";
            }
            return true;
        }

        else if(view.getId() == R.id.fourbutton){
            if (reset_flag){
                reset_flag = false;
                math_view = "4";
            }else{
                math_view = math_view + "4";
            }
            return true;
        }

        else if(view.getId() == R.id.fivebutton){
            if (reset_flag){
                reset_flag = false;
                math_view = "5";
            }else{
                math_view = math_view + "5";
            }
            return true;
        }

        else if(view.getId() == R.id.sixbutton){
            if (reset_flag){
                reset_flag = false;
                math_view = "6";
            }else{
                math_view = math_view + "6";
            }
            return true;
        }

        else if(view.getId() == R.id.sevenbutton){
            if (reset_flag){
                reset_flag = false;
                math_view = "7";
            }else{
                math_view = math_view + "7";
            }
            return true;
        }

        else if(view.getId() == R.id.eightbutton){
            if (reset_flag){
                reset_flag = false;
                math_view = "8";
            }else{
                math_view = math_view + "8";
            }
            return true;
        }

        else if(view.getId() == R.id.ninebutton){
            if (reset_flag){
                reset_flag = false;
                math_view = "9";
            }else{
                math_view = math_view + "9";
            }
            return true;
        }
        else if(view.getId() == R.id.periodbutton){
            if (reset_flag){
                reset_flag = false;
                used_already = true;
                math_view = "0.";
            }else if (!used_already){
                used_already = true;
                math_view = math_view + ".";
            }
            return true;
        }else if(view.getId() == R.id.pnbutton){
            if (math_view.charAt(0) == '-'){
                math_view = math_view.substring(1);
            }else {
                math_view = '-' + math_view;

            }
            return true;
        }
        return false;
    }

    /**
     * Basically enforcing BEDMAS.
     * If the new operation has a higher/equal priority then the peak operation, the peak operation
     * needs to go to the Queue, so that it is not dealt with anymore. If the stack is empty, then
     * nothing needs to be done.
     * @param new_opt: the new operation that needs to enter the stack
     */
    private void update_operations_stack(operations new_opt){
        int kick = 0;
        if (operations_stack.isEmpty()){
            kick++;
        }
        while (kick == 0) {
            operations peek_opt = (operations) operations_stack.peek();
            if (peek_opt.get_priority() >= new_opt.get_priority()) {
                token switchie_token = operations_stack.pop();
                multi_usage_queue.add(switchie_token);
            }else{
                kick++;
            }
        }
    }

    /**
     * This function is designed to calculate the outcome of a given queue.
     * Implements the Postfix Calculator Algorithm.
     * @param some_queue: the multi_usage_queue in this scenario.
     * @return outcome: the out come of the given queue.
     */
    private double calculate_queue(Queue<token> some_queue){

        Stack<numbers> number_stack = new Stack<>();
        while (!some_queue.isEmpty()){

            token first_token = some_queue.remove();

            if (first_token.get_type().equals("number")){
                number_stack.push((numbers) first_token);
            }

            else{
                operations current_operation = (operations) first_token;
                double right = number_stack.pop().get_value();
                double left = number_stack.pop().get_value();

                double result = current_operation.operate(left, right);
                numbers new_num = new numbers(result);
                number_stack.push(new_num);
            }
        }

        return number_stack.pop().get_value();
    }


}
