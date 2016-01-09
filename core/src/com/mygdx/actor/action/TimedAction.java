package com.mygdx.actor.action;

public abstract class TimedAction implements BasicAction {
    protected final float duration;
    protected float elapsed;

    public TimedAction(float duration) {
        if (duration <= 0) {
            throw new IllegalStateException("Duration should be more than zero");
        }
        this.duration = duration;
        elapsed = 0;
    }

    @Override
    public float act(float delta) {
        float change = Math.min(delta, duration - elapsed);
        elapsed += change;
        return change;
    }

    @Override
    public boolean finished() {
        return elapsed == duration;
    }

    public float getState() {
        return elapsed / duration;
    }
}
