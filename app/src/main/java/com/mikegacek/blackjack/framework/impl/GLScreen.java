package com.mikegacek.blackjack.framework.impl;

import com.mikegacek.blackjack.framework.Game;
import com.mikegacek.blackjack.framework.Screen;

/**
 * Created by Michael on 11/12/2015.
 */
public abstract class GLScreen extends Screen{

    protected final GLGraphics glGraphics;
    protected final GLGame glGame;

    public GLScreen(Game game) {
        super(game);
        glGame = (GLGame)game;
        glGraphics=glGame.getGlGraphics();
    }
}
