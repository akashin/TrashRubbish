package com.mygdx.actor.action;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.util.Constants;

public class MovementAction extends TimedAction {
    static final float speed = Constants.CELL_SIZE * 3;

    private Actor actor;
    private float srcX, srcY;
    private float dstX, dstY;

    public MovementAction(float srcX, float srcY, float dstX, float dstY, Actor actor) {
        super((Math.abs(dstX - srcX) + Math.abs(dstY - srcY)) / speed);
        this.srcX = srcX;
        this.srcY = srcY;
        this.dstX = dstX;
        this.dstY = dstY;
        this.actor = actor;
        System.err.println(srcX + ":" + srcY + " " + dstX + ":" + dstY);
    }

    @Override
    public float act(float delta) {
        float change = super.act(delta);
        System.err.println("change" + change);
        float state = getState();
        actor.setPosition(
                srcX * (1 - state) + dstX * state,
                srcY * (1 - state) + dstY * state
        );
        return change;
    }
}
