package com.mikegacek.blackjack;

import com.mikegacek.blackjack.framework.gl.SpriteBatcher;
import com.mikegacek.blackjack.framework.impl.GLGraphics;

import java.util.ArrayList;

/**
 * Created by Michael on 2/1/2017.
 */

public interface SideBets{

    public int payout(ArrayList<Card> playerCards, ArrayList<Card> dealerCards, int bet, Settings settingsManager);

    public void createPayoutTextures(int v);

    public void resetPayout();

    public void drawPayouts(SpriteBatcher batcher, GLGraphics glGraphics, int pay, int version);

    public int getPayoutAmount();

    public int getPayout();

    public int getOldPayout();

    public int getOldVersion();

    public int getVersion();

    public void setVersion(int v);

    public void setOldVersion(int v);

    public void setPayout(int payout);

    public void setOldPayout(int payout);

    public void load();
}


