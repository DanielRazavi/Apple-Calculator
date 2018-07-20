package com.example.applecalculator.objcts;

public class division extends token {

    @Override
    public String get_type() {
        return "operation";
    }

    public double operate(int num1, int num2){
        // How should I handle num2 equalling 0?
        return num1 / num2;
    }

    /*
    Brackets        4
    Exponents       3
    Division        2
    Multiplication  2
    Addition        1
    Subtraction     1
     */
    public int get_priority(){
        return 2;
    }
}
