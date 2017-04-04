package com.example.rent.movieapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by RENT on 2017-04-04.
 */
public class Calculator {
    public int sum(int i, int j) {
        return i + j;
    }

    public List<Integer> sort(List<Integer> unsortedList) {
        List<Integer> copy = new ArrayList<>(unsortedList);
        Collections.sort(copy);
        return copy;
    }
}
