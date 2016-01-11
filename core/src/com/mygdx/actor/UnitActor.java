package com.mygdx.actor;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class UnitActor<T> extends Actor {
    protected final T unit;
    protected final AssetManager assetManager;

    public UnitActor(T unit, AssetManager assetManager) {
        this.unit = unit;
        this.assetManager = assetManager;
    }

    public T getUnit() {
        return unit;
    }
}
