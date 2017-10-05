package com.mikegacek.blackjack;

import com.mikegacek.blackjack.framework.gl.SpriteBatcher;
import com.mikegacek.blackjack.framework.impl.GLGraphics;

import java.util.ArrayList;

/**
 * Created by Michael on 2/1/2017.
 */

public interface SideBets{

    public int payout(ArrayList<Card> playerCards, ArrayList<Card> dealerCards, int bet, StatisticsManager statisticsManager);

    public void createPayoutTextures(int v);

    public void resetPayout();

    public void drawPayouts(SpriteBatcher batcher, GLGraphics glGraphics, int pay, int version,int p);

    public int getPayoutAmount();

    public int getPayout();

    public int getPosition();

    public int getVersion();

    public int getBet();

    public void setVersion(int v);

    public void setPayout(int payout);

    public void setPosition(int position);

    public void setBet(int bet);

    public String getName();

    public void updateName();

    public int getId();

    public void load();
}


