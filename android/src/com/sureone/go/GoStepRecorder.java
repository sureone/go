package com.sureone.go;

import java.util.Stack;

public class GoStepRecorder {
    Stack<GoStep> mSteps=null;
    public GoStepRecorder() {
        mSteps = new Stack<GoStep>();
    }

    public void push(GoStep step) {
        mSteps.push(step);

    }

    public GoStep pop() {
        if(mSteps.size()==0) return null;
        return mSteps.pop();
    }

    public void clear() {
        mSteps.clear();
    }

    public int size() {
        return mSteps.size();
    }
}
