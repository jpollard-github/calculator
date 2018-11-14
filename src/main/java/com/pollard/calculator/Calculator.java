package com.pollard.calculator;

import org.springframework.stereotype.Service;

/**
 * Primary calculation engine class
 */
@Service
public class Calculator {
    int sum(int a, int b) {
        return a + b;
    }
}
