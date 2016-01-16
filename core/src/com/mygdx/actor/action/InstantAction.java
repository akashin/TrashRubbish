package com.mygdx.actor.action;

public abstract class InstantAction implements BasicAction {
    public abstract void act();

    @Override
    public float act(float delta) {
        act();
        return 0;
    }

    @Override
    public boolean finished() {
        return true;
    }
}
