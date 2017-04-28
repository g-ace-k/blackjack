package com.mikegacek.blackjack;

import com.mikegacek.blackjack.framework.gl.SpriteBatcher;
import com.mikegacek.blackjack.framework.gl.TextureRegion;
import com.mikegacek.blackjack.framework.impl.GLGraphics;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Michael on 2/1/2017.
 */

public class PerfectPairs implements SideBets,Serializable {

    private int version,oldVersion;
    private int payout,oldPayout,bet;
    private transient ArrayList<TextureRegion> payouts;

    public PerfectPairs(int version) {
        this.version=version;
        payout=-1;
        oldPayout=-1;
        bet=0;
        payouts = new ArrayList<TextureRegion>();
        createPayoutTextures(version);
    }

    public void createPayoutTextures(int v) {
        payouts.clear();
        switch(v) {
            case 1:
                payouts.add(Assets.sideBetCircle);
                payouts.add(new TextureRegion(Assets.payouts,0,0,376,54));
                payouts.add(new TextureRegion(Assets.payouts, 0, 64, 376, 35));
                payouts.add(new TextureRegion(Assets.payouts, 0, 99, 376, 35));
                payouts.add(new TextureRegion(Assets.payouts, 0, 135, 376, 35));
                break;
        }
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getPayout() {
        return payout;
    }

    public void setPayout(int payout) {
        this.payout = payout;
    }

    public int getOldPayout() {
        return oldPayout;
    }

    public void setOldPayout(int oldPayout) {
        this.oldPayout = oldPayout;
    }

    public int getOldVersion() { return oldVersion;}

    public void setOldVersion(int v) { oldVersion=v;}
    @Override
    public void resetPayout() {
        oldPayout=payout;
        payout=-1;
    }


    //version 1 payout, perfect pair 25:1, colored pair 15:1, mixed pair 5:1
    //Value goes from 1 Ace to 13 King
    //Suit goes 0 club, 1 diamond, 2 heart, 3 spades
    @Override
    public int payout(ArrayList<Card> playerCards, ArrayList<Card> dealerCards,int bet,Settings settingsManager) {
        Card card1=playerCards.get(0);
        Card card2=playerCards.get(1);
        this.bet=bet;
        //If same value
        switch(version) {
            case 1:
                if (card1.getValue() == card2.getValue()) {
                    payout = 5;
                    //If same Color
                    if (((card1.getSuit() == 0 || card1.getSuit() == 3) && (card2.getSuit() == 0 || card2.getSuit() == 3)) || ((card1.getSuit() == 1 || card1.getSuit() == 2) && (card2.getSuit() == 1 || card2.getSuit() == 2))) {
                        payout = 15;
                        //If same Suit
                        if (card1.getSuit() == card2.getSuit()) {
                            payout = 25;
                        }
                    }
                }
                break;
        }

        if(bet>0) {
            settingsManager.setPerfectPairsTotal(settingsManager.getPerfectPairsTotal()+1);
            settingsManager.setPerfectPairsIncome(settingsManager.getPerfectPairsIncome()+bet*payout);
            if(payout>0)
                settingsManager.setPerfectPairsWon(settingsManager.getPerfectPairsWon()+1);
        }
        if(payout>0)
            Assets.winningBet.play((float)MainScreen.sound*.2f);
        else if(payout<0)
            Assets.multiChips.play((float)MainScreen.sound*.2f);

        oldPayout=payout;
        return payout;
    }

    @Override
    public void drawPayouts(SpriteBatcher batcher, GLGraphics glGraphics,int pay,int v) {
        //changing text to green to show winner
        int change=-1;

        createPayoutTextures(v);
        //Draw 3 rows
        //Row 1 PerfectPair
        //Row 2 Colored Pair
        //Row 3 Mixed Pair
        glGraphics.getGl().glColor4f(1,1,1,1);
        if(payouts.size()>=2) {
            batcher.beginBatch(Assets.sideBet);
            batcher.drawSprite(304, 674, 146, 146, payouts.get(0));
            batcher.endBatch();
            batcher.beginBatch(Assets.payouts);
            batcher.drawSprite(188,517,376,54,payouts.get(1));
            batcher.endBatch();
        }

        if(pay==25)
            change=2;
        else if(pay==15)
            change=3;
        else if(pay==5)
            change=4;
        for(int i=2;i<payouts.size();i++) {
            if(i==change) {
                glGraphics.getGl().glColor4f(0,1,0,1);
            }
            batcher.beginBatch(Assets.payouts);
            batcher.drawSprite(188, 464-(i-2)*35, 376, 35, payouts.get(i));
            batcher.endBatch();
            glGraphics.getGl().glColor4f(1, 1, 1, 1);
        }

    }

    @Override
    public int getPayoutAmount() {
        return oldPayout*bet;
    }

    public void load() {
        payouts = new ArrayList<TextureRegion>();
        createPayoutTextures(version);
    }
}
