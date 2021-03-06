package com.mikegacek.blackjack;

import com.mikegacek.blackjack.framework.gl.SpriteBatcher;
import com.mikegacek.blackjack.framework.gl.TextureRegion;
import com.mikegacek.blackjack.framework.impl.GLGraphics;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Michael on 2/1/2017.
 */

public class TwentyOnePlusThree implements SideBets,Serializable{

    private int version;
    private final int id=2;
    private int payout,bet,position;
    private String name;
    private transient ArrayList<TextureRegion> payouts;


    //Two version: version 1 is with 2 or less decks, version 2 is 3 or more decks
    public TwentyOnePlusThree(int version,int position) {
        this.version=version;
        payout=-1;
        bet=0;
        updateName();
        this.position=position;
        payouts = new ArrayList<TextureRegion>();
        createPayoutTextures(version);
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
        updateName();
    }

    public int getPayout() {
        return payout;
    }

    public void setPayout(int payout) {
        this.payout = payout;
    }

    public int getBet() { return bet;}

    public void setBet(int b) { bet=b;}

    public int getPosition() { return position;}

    public void setPosition(int position) { this.position=position;}

    public String getName() {
        return name;
    }

    public void updateName() {
        if(version==0) {
            name="None";
        }
        else if(version==1) {
            name="21+3(1-8 decks)";
        }
        else if(version==2) {
            name="21+3(3-8 decks)";
        }
    }

    public int getId() { return id;}

    @Override
    public void resetPayout() {
        payout=-1;
    }

    @Override
    public int payout(ArrayList<Card> playerCards, ArrayList<Card> dealerCards,int bet,StatisticsManager statisticsManager) {
        boolean flush=false;
        boolean straight=false;
        boolean trips=false;
        this.bet=bet;
        Card card1= playerCards.get(0);
        Card card2= playerCards.get(1);
        Card dealerCard= dealerCards.get(0);

        //Check for flush
        if(card1.getSuit()==card2.getSuit() && card2.getSuit()==dealerCard.getSuit())
            flush=true;

        //Check for Straight if theres a queen and king (12 and 13) change the 1 into a 14
        int[] list = sortForStraight(card1.getValue(),card2.getValue(),dealerCard.getValue());
        if(list[2]==(list[1]+1) && list[1]==(list[0]+1))
            straight=true;
        //Check for trips
        if(list[0]==list[1] && list[1]==list[2])
            trips=true;

        //Case 1: straight flush 30:1 trips 20:1 straight 10:1 flush 5:1
        //Case 2: flush trips 100:1 straight flush 35:1 trips 33:1 straight 10:1 flush 5:1
        switch(version) {
            case 1:
                if(trips)
                    payout=20;
                else if(flush) {
                    payout=5;
                    if(straight)
                        payout=30;
                }
                else if(straight) {
                    payout=10;
                }
                break;
            case 2:
                if(trips) {
                    payout=33;
                    if(flush)
                        payout=100;
                }
                else if(flush) {
                    payout=5;
                    if(straight)
                        payout=35;
                }
                else if(straight)
                    payout=10;
                break;
        }

        if(bet>0) {
            statisticsManager.setTwentyOneTotal(statisticsManager.getTwentyOneTotal()+1);
            if(version==1)
                statisticsManager.setTwentyOneV1Income(statisticsManager.getTwentyOneV1Income()+bet*payout);
            else if(version==2)
                statisticsManager.setTwentyOneV2Income(statisticsManager.getTwentyOneV2Income()+bet*payout);
            if(payout>0)
                statisticsManager.setTwentyOneWon(statisticsManager.getTwentyOneWon()+1);
        }

        if(payout>0)
            Assets.winningBet.play((float)MainScreen.sound*.2f);
        else if(payout<0)
            Assets.multiChips.play((float)MainScreen.sound*.2f);
        return payout;
    }

    private int[] sortForStraight(int value1, int value2, int value3) {
        int low,mid,high;
        //Check for Straight if theres a queen and king (12 and 13) change the 1 into a 14
        if((value1==12 || value2==12 || value3==12) && (value1==13 || value2==13 || value3==13)) {
            if(value1==1)
                value1=14;
            else if(value2==1)
                value2=14;
            else if(value3==1)
                value3=14;
        }
        int[] list = {value1,value2,value3};

        boolean unsorted=true;
        int temp;
        //Should put an actual sorting algorithm here but a short one checking for 3 values will do
        while(unsorted) {
            if(list[0]>list[1]) {
                temp=list[1];
                list[1]=list[0];
                list[0]=temp;
            }
            if(list[1]>list[2]) {
                temp=list[2];
                list[2]=list[1];
                list[1]=temp;
            }
            if(list[2]>=list[1]) {
                if(list[1]>=list[0]) {
                    unsorted=false;
                }
            }
        }

        return list;
    }

    @Override
    public void createPayoutTextures(int v) {
        payouts.clear();

        switch(v) {
            case 1:
                payouts.add(Assets.sideBetCircle);
                payouts.add(new TextureRegion(Assets.payouts,0,172,376,54));
                payouts.add(new TextureRegion(Assets.payouts,0,236,376,35));
                payouts.add(new TextureRegion(Assets.payouts,0,272,376,35));
                payouts.add(new TextureRegion(Assets.payouts,0,307,376,35));
                payouts.add(new TextureRegion(Assets.payouts,0,342,376,35));
                break;
            case 2:
                payouts.add(Assets.sideBetCircle);
                payouts.add(new TextureRegion(Assets.payouts,0,398,376,54));
                payouts.add(new TextureRegion(Assets.payouts,0,462,376,35));
                payouts.add(new TextureRegion(Assets.payouts,0,497,376,35));
                payouts.add(new TextureRegion(Assets.payouts,0,533,376,35));
                payouts.add(new TextureRegion(Assets.payouts,0,568,376,35));
                payouts.add(new TextureRegion(Assets.payouts,0,603,376,35));
        }
    }

    @Override
    public void drawPayouts(SpriteBatcher batcher, GLGraphics glGraphics, int pay,int v, int p) {
        int change=-1;
        int textOffset=0;
        int circleOffset=0;

        if(p==0) {
            textOffset=188;
            circleOffset=304;
        }
        else {
            textOffset=892;
            circleOffset=776;
        }

        createPayoutTextures(v);

        glGraphics.getGl().glColor4f(1,1,1,1);
        if(payouts.size()>=2) {
            batcher.beginBatch(Assets.sideBet);
            batcher.drawSprite(circleOffset, 674, 146, 146, payouts.get(0));
            batcher.endBatch();
            batcher.beginBatch(Assets.payouts);
            batcher.drawSprite(textOffset,517,376,54,payouts.get(1));
            batcher.endBatch();
        }
        //Payouts 30,20,10,5
        //Payouts 100,35,33,10,5
        switch(v) {
            case 1:
                if(pay==30)
                    change=2;
                else if(pay==20)
                    change=3;
                else if(pay==10)
                    change=4;
                else if(pay==5)
                    change=5;

                for(int i=2;i<payouts.size();i++) {
                    if(i==change)
                        glGraphics.getGl().glColor4f(0,1,0,1);
                    batcher.beginBatch(Assets.payouts);
                    batcher.drawSprite(textOffset,464-(i-2)*35,376,35,payouts.get(i));
                    batcher.endBatch();
                    glGraphics.getGl().glColor4f(1,1,1,1);
                }
                break;
            case 2:
                if(pay==100)
                    change=2;
                else if(pay==35)
                    change=3;
                else if(pay==33)
                    change=4;
                else if(pay==10)
                    change=5;
                else if(pay==5)
                    change=6;
                for(int i=2;i<payouts.size();i++) {
                    if(i==change)
                        glGraphics.getGl().glColor4f(0,1,0,1);
                    batcher.beginBatch(Assets.payouts);
                    batcher.drawSprite(textOffset,464-(i-2)*35,376,35,payouts.get(i));
                    batcher.endBatch();
                    glGraphics.getGl().glColor4f(1,1,1,1);
                }
                break;

        }
    }

    @Override
    public int getPayoutAmount() {
        return payout*bet;
    }

    public void load() {
        payouts = new ArrayList<TextureRegion>();
        createPayoutTextures(version);
    }
}
