package com.mikegacek.blackjack.framework;

/**
 * Created by laptop on 2/12/2015.
 */
public abstract class Screen {
    protected final Game game;

    public Screen(Game game) {
        this.game = game;
    }

    public abstract void update(float deltaTime);

    public abstract void present(float deltaTime);

    public abstract void resume();

    public abstract void pause();

    public abstract void dispose();
}
