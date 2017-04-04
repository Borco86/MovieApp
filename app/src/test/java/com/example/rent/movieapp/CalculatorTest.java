package com.example.rent.movieapp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by RENT on 2017-04-04.
 */

public class CalculatorTest {
    Calculator calculator;

    @Before
    public void setup() {
        calculator = new Calculator();
    }

    @Test
    public void shouldAddTwoNumbers() {
        //given
        int i = 1;
        int j = 2;

        //when
        int result = calculator.sum(i, j);

        //then
        Assert.assertTrue(result == 3);
    }

    @Test
    public void shouldSortListOfNumbers() {
        //given
        List<Integer> unsortedList = Arrays.asList(3, 5, 1, 2, 4);
        //when
        List<Integer> result = calculator.sort(unsortedList);
        //then
        Assert.assertEquals(result, Arrays.asList(1, 2, 3, 4, 5));
    }
}
