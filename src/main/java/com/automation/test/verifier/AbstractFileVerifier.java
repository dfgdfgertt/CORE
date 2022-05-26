package com.automation.test.verifier;

public abstract class AbstractFileVerifier<ACT, EXP> extends AbstractVerifier<ACT, EXP> {

    protected final int SLEEP_PERIOD = 500;
    
    protected int timeout;

    public AbstractFileVerifier(int timeout) {
        super();
        this.timeout = timeout;
    }
    
    protected int getNSleep() {
        return timeout / SLEEP_PERIOD;
    }
}
