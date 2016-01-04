package com.mygdx.actor.action;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MovementAction implements BasicAction {
    Actor actor;
    float duration;
    float elapsed;
    float srcX, srcY;
    float dstX, dstY;

    static final float speed = 64 * 2;

    public MovementAction(float srcX, float srcY, float dstX, float dstY, Actor actor) {
        this.srcX = srcX;
        this.srcY = srcY;
        this.dstX = dstX;
        this.dstY = dstY;
        this.actor = actor;

        float delta = Math.abs(dstX - srcX) + Math.abs(dstY - srcY);
        duration = delta / speed;
        elapsed = 0;
    }

    @Override
    public float act(float delta) {
        if (elapsed == duration) {
            return 0;
        }
        float change = Math.min(delta, duration - elapsed);
        elapsed += change;
        float alpha = elapsed / duration;
        actor.setPosition(srcX * (1 - alpha) + dstX * alpha, srcY * (1 - alpha) + dstY * alpha);
        delta -= change;
        return change;
    }

}
