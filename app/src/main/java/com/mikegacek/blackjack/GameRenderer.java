package com.mikegacek.blackjack;

import com.mikegacek.blackjack.framework.gl.Camera2D;
import com.mikegacek.blackjack.framework.gl.ParticleGroup;
import com.mikegacek.blackjack.framework.gl.SpriteBatcher;
import com.mikegacek.blackjack.framework.gl.Texture;
import com.mikegacek.blackjack.framework.gl.TextureRegion;
import com.mikegacek.blackjack.framework.impl.GLGraphics;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Michael on 11/30/2016.
 */
public class GameRenderer implements Serializable{

    private static final long serialVersionUID = 14L;

    private transient SpriteBatcher batcher;
    private transient GLGraphics glGraphics;
    private transient GameManager gameManager;
    private transient ChipManager chipManager;
    private transient SlotMachine slotMachine;
    private transient Settings settingsManager;
    private transient Camera2D cam;
    private float bettingAlphas,insuranceAlphas,messageAlphas;
    private BetAndCount hand1,hand2,hand3,hand4,dealerHand;
    private ParticleGroup chipParticles;

    public GameRenderer(GLGraphics glGraphics, SpriteBatcher batcher, GameManager gameManager, ChipManager chipManager, SlotMachine slotMachine, Settings settings) {
        this.batcher=batcher;
        this.glGraphics=glGraphics;
        this.gameManager=gameManager;
        this.chipManager=chipManager;
        this.slotMachine=slotMachine;
        this.settingsManager=settings;
        hand1= new BetAndCount(this.glGraphics,this.batcher);
        hand2= new BetAndCount(this.glGraphics,this.batcher);
        hand3= new BetAndCount(this.glGraphics,this.batcher);
        hand4= new BetAndCount(this.glGraphics,this.batcher);
        dealerHand = new BetAndCount(this.glGraphics,this.batcher);
        this.cam = new Camera2D(glGraphics, 540, 960);
        bettingAlphas=1;
        insuranceAlphas=0;
        messageAlphas=0;
        chipParticles= new ParticleGroup(Assets.particle,Assets.p,.8f,0,.8f,0,1);
    }

    public void render(int state) {

        if(state<5) {
            renderBackground(state);
            renderDoubleButton();
            renderSplitButton();
            renderSurrenderButton();
            renderEdge();
            renderMoney(state);
            renderHitButton();
            renderStandButton();
            if(chipManager.getMoney()==0 &&chipManager.getMainBet().isEmpty() && chipManager.getSideBetLeft().isEmpty() && chipManager.getSideBetRight().isEmpty() && chipManager.getChips().isEmpty())
                renderFreeChips(state);
            else
                renderBetButtons(state);
            renderChips();
            renderCards();
            renderBetsAndCounts();
            if (state == 0 || state == 3)
                renderMenuButton();
            if (gameManager.getInsurance() && gameManager.getState() == 0)
                renderInsurance();
            else if (insuranceAlphas != 0) {
                insuranceAlphas -= .1f;
                if (insuranceAlphas < 0)
                    insuranceAlphas = 0;
            }
            renderHintButton();
            renderPlayButton();

            if (state == 4)
                renderSlotMachine();
        }
        else if(state==5)
            renderSettings();

    }


    private void renderSettings() {
        String netIncome;

        switch(settingsManager.getPage()) {
            case 1:
                batcher.beginBatch(Assets.menu);
                batcher.drawSprite(270,480,540,960,Assets.deckMenu);
                batcher.endBatch();

                renderPressedButton(Assets.buttons,settingsManager.getDeckLeft());
                renderPressedButton(Assets.buttons,settingsManager.getDeckRight());
                //381 865
                renderSettingsNumber(381,866,settingsManager.getDecks()+"",.75f);


                renderPressedButton(Assets.buttons,settingsManager.getPenLeft());
                renderPressedButton(Assets.buttons,settingsManager.getPenRight());

                String pen= settingsManager.getPenetration()+"%";
                float offset=21*.75f+(pen.length()-2)*14*.75f;

                renderSettingsNumber(381-offset,776,pen,.75f);

                renderRadioButton(382,820,Assets.radioButtonOn,Assets.buttons,settingsManager.getCSM());
                renderRadioButton(215,600,Assets.radioButtonOn,Assets.buttons,settingsManager.getBackground()==1);
                renderRadioButton(325,600,Assets.radioButtonOn,Assets.buttons,settingsManager.getBackground()==2);
                renderRadioButton(215,464,Assets.radioButtonOn,Assets.buttons,settingsManager.getBackground()==3);
                renderRadioButton(325,464,Assets.radioButtonOn,Assets.buttons,settingsManager.getBackground()==4);

                renderSettingsNumber(240,293,""+settingsManager.getPlayerBlackjacks(),.5f);
                renderSettingsNumber(245,260,""+settingsManager.getDealerBlackjacks(),.5f);
                renderSettingsNumber(204,228,""+settingsManager.getHandsPlayed(),.5f);
                renderSettingsNumber(179,195,""+settingsManager.getHandsWon(),.5f);
                renderSettingsNumber(172,163,""+settingsManager.getHandsLost(),.5f);
                renderSettingsNumber(177,130,"$"+settingsManager.getMoneyBet(),.5f);
                renderSettingsNumber(187,98,"$"+settingsManager.getMoneyWon(),.5f);
                renderSettingsNumber(180,65,"$"+settingsManager.getMoneyLost(),.5f);

                int netTotal=(settingsManager.getMoneyWon()-settingsManager.getMoneyLost());
                if(netTotal>0) {
                    glGraphics.getGl().glColor4f(0, 1, 0, 1);
                    netIncome="+$"+netTotal;
                }
                else if(netTotal<0) {
                    glGraphics.getGl().glColor4f(1, 0, 0, 1);
                    netIncome="-$"+Math.abs(netTotal);
                }
                else {
                    glGraphics.getGl().glColor4f(1, 1, 1, 1);
                    netIncome="$"+netTotal;
                }

                renderSettingsNumber(184,33,netIncome,.5f);

                glGraphics.getGl().glColor4f(1,1,1,1);
                break;
            case 2:
                batcher.beginBatch(Assets.menu);
                batcher.drawSprite(270,480,540,960,Assets.gameplayMenu);
                batcher.endBatch();

                renderPressedButton(Assets.buttons,settingsManager.getBlackjackLeft());
                renderPressedButton(Assets.buttons,settingsManager.getBlackjackRight());

                renderSettingsNumber(360,866,settingsManager.getBlackjackPaysString(),.75f);

                renderPressedButton(Assets.buttons,settingsManager.getSplitLeft());
                renderPressedButton(Assets.buttons,settingsManager.getSplitRight());

                renderSettingsNumber(381,650,settingsManager.getSplitHands()+"",.75f);

                renderPressedButton(Assets.buttons,settingsManager.getDoubleLeft());
                renderPressedButton(Assets.buttons,settingsManager.getDoubleRight());

                batcher.beginBatch(Assets.settingsFont);
                batcher.drawSprite(381,709,settingsManager.getDealerAction().width*.65f,settingsManager.getDealerAction().height*.65f,settingsManager.getDealerAction());
                batcher.drawSprite(381,425,settingsManager.getDoubleDownFont().width*.5f,settingsManager.getDoubleDownFont().height*.5f,settingsManager.getDoubleDownFont());
                batcher.endBatch();

                renderRadioButton(382,821,Assets.radioButtonOn,Assets.buttons,settingsManager.getInsurance());
                renderRadioButton(382,776,Assets.radioButtonOn,Assets.buttons,settingsManager.getSurrender());
                renderRadioButton(382,605,Assets.radioButtonOn,Assets.buttons,settingsManager.getResplit());
                renderRadioButton(382,560,Assets.radioButtonOn,Assets.buttons,settingsManager.getHitAces());
                renderRadioButton(382,515,Assets.radioButtonOn,Assets.buttons,settingsManager.getDoubleAces());
                renderRadioButton(382,470,Assets.radioButtonOn,Assets.buttons,settingsManager.getDas());

                renderSettingsNumber(214,293,""+settingsManager.getDoubleDownTotal(),.5f);
                renderSettingsNumber(267,260,""+settingsManager.getDoubleDownWon(),.5f);
                renderSettingsNumber(259,228,""+settingsManager.getDoubleDownLost(),.5f);
                renderSettingsNumber(116,195,""+settingsManager.getSplitsTotal(),.5f);
                renderSettingsNumber(216,163,""+settingsManager.getInsuranceTotal(),.5f);
                renderSettingsNumber(269,130,""+settingsManager.getInsuranceWon(),.5f);
                renderSettingsNumber(167,98,""+settingsManager.getSurrenderTotal(),.5f);

                break;
            case 3:
                batcher.beginBatch(Assets.menu);
                batcher.drawSprite(270,480,540,960,Assets.sidebetMenu);
                batcher.endBatch();

                renderRadioButton(382,821,Assets.radioButtonOn,Assets.buttons,settingsManager.getPerfectPairs());
                renderRadioButton(382,648,Assets.radioButtonOn,Assets.buttons,settingsManager.getTwentyOneV1());
                renderRadioButton(382,468,Assets.radioButtonOn,Assets.buttons,settingsManager.getTwentyOneV2());

                renderSettingsNumber(281,293,""+settingsManager.getPerfectPairsTotal(),.5f);
                renderSettingsNumber(246,260,""+settingsManager.getPerfectPairsWon(),.5f);
                renderSettingsNumber(190,195,""+settingsManager.getTwentyOneTotal(),.5f);
                renderSettingsNumber(165,163,""+settingsManager.getTwentyOneWon(),.5f);

                if(settingsManager.getPerfectPairsIncome()>0) {
                    glGraphics.getGl().glColor4f(0, 1, 0, 1);
                    netIncome="+$"+settingsManager.getPerfectPairsIncome();
                }
                else if(settingsManager.getPerfectPairsIncome()<0) {
                    glGraphics.getGl().glColor4f(1, 0, 0, 1);
                    netIncome="-$"+Math.abs(settingsManager.getPerfectPairsIncome());
                }
                else {
                    glGraphics.getGl().glColor4f(1, 1, 1, 1);
                    netIncome="$"+settingsManager.getPerfectPairsIncome();
                }
                renderSettingsNumber(321,228,netIncome,.5f);

                if(settingsManager.getTwentyOneV1Income()>0) {
                    glGraphics.getGl().glColor4f(0, 1, 0, 1);
                    netIncome="+$"+settingsManager.getTwentyOneV1Income();
                }
                else if(settingsManager.getTwentyOneV1Income()<0) {
                    glGraphics.getGl().glColor4f(1, 0, 0, 1);
                    netIncome="-$"+Math.abs(settingsManager.getTwentyOneV1Income());
                }
                else {
                    glGraphics.getGl().glColor4f(1, 1, 1, 1);
                    netIncome="$"+settingsManager.getTwentyOneV1Income();
                }
                renderSettingsNumber(365,130,netIncome,.5f);

                if(settingsManager.getTwentyOneV2Income()>0) {
                    glGraphics.getGl().glColor4f(0, 1, 0, 1);
                    netIncome="+$"+settingsManager.getTwentyOneV2Income();
                }
                else if(settingsManager.getTwentyOneV2Income()<0) {
                    glGraphics.getGl().glColor4f(1, 0, 0, 1);
                    netIncome="-$"+Math.abs(settingsManager.getTwentyOneV2Income());
                }
                else {
                    glGraphics.getGl().glColor4f(1, 1, 1, 1);
                    netIncome="$"+settingsManager.getTwentyOneV2Income();
                }
                renderSettingsNumber(365,98,netIncome,.5f);

                glGraphics.getGl().glColor4f(1,1,1,1);

                break;
        }
    }

    private void renderSettingsNumber(float x, float y, String string,float scale) {



        batcher.beginBatch(Assets.settingsFont);
        for (int i = 0; i < string.length(); i++) {
            if(string.charAt(i)=='%')
                x+=14*scale;
            TextureRegion temp= findSettingsNumber2(string.charAt(i));
            batcher.drawSprite(x, y, temp.width *scale, temp.height *scale, temp);
            x+=28*scale;
        }
        batcher.endBatch();



    }

    private void renderRadioButton(float x, float y, TextureRegion t, Texture texture, boolean flag) {
        float alpha;
        if(flag==true)
            alpha=1;
        else
            alpha=0;
        glGraphics.getGl().glColor4f(1,1,1,alpha);

        batcher.beginBatch(texture);
        batcher.drawSprite(x,y,t.width,t.height,t);
        batcher.endBatch();

        glGraphics.getGl().glColor4f(1,1,1,1);
    }

    private void renderPressedButton(Texture t, Button b) {
        if(b.getPressed()==true) {
            glGraphics.getGl().glColor4f(.5f,.5f,.5f,1);
        }
        batcher.beginBatch(t);
        batcher.drawSprite(b,b.getTextureRegion());
        batcher.endBatch();
        glGraphics.getGl().glColor4f(1,1,1,1);
    }

    private void renderSlotMachine() {
        slotMachine.drawSlotMachine();
    }

    private void renderInsurance() {
        if(insuranceAlphas<1)
            insuranceAlphas+=.1f;
        else if(insuranceAlphas>1)
            insuranceAlphas=1;
        glGraphics.getGl().glColor4f(1,1,1,insuranceAlphas);
        batcher.beginBatch(Assets.ins);
        batcher.drawSprite(270,480,540,960,Assets.insurance);
        batcher.endBatch();

        String money="$";
        int amount=chipManager.getMainBetMoney()/2;
        money=money+amount;
        TextureRegion tempChip;
        float x=270-13*(money.length()-1);
        float y=580;

        if(amount<5)
            tempChip=new TextureRegion(Assets.chips,0,67,67,67);
        else if(amount<25)
            tempChip=new TextureRegion(Assets.chips,67,67,67,67);
        else if(amount<100)
            tempChip=new TextureRegion(Assets.chips,134,67,67,67);
        else if(amount<500)
            tempChip=new TextureRegion(Assets.chips,201,67,67,67);
        else if(amount<1000)
            tempChip=new TextureRegion(Assets.chips,268,67,67,67);
        else if(amount<10000)
            tempChip=new TextureRegion(Assets.chips,335,67,67,67);
        else
            tempChip=new TextureRegion(Assets.chips,402,67,67,67);

        batcher.beginBatch(Assets.chips);
        batcher.drawSprite(x-70,y,67,67,tempChip);
        batcher.endBatch();

        batcher.beginBatch(Assets.numbers);
        for (int i = 0; i < money.length(); i++) {
            TextureRegion temp= findNumber(money.charAt(i));
            batcher.drawSprite(x, y, temp.width * .75f, temp.height * .75f, temp);
            x+=26;
        }
        batcher.endBatch();

        batcher.beginBatch(Assets.chips);
        batcher.drawSprite(x+44,y,67,67,tempChip);
        batcher.endBatch();

        if(gameManager.check.getPressed())
            glGraphics.getGl().glColor4f(.5f,.5f,.5f,insuranceAlphas);

        batcher.beginBatch(Assets.buttons);
        batcher.drawSprite(85,40,75,75,Assets.check);
        batcher.endBatch();

        glGraphics.getGl().glColor4f(1,1,1,insuranceAlphas);

        if(gameManager.uncheck.getPressed())
            glGraphics.getGl().glColor4f(.5f,.5f,.5f,insuranceAlphas);

        batcher.beginBatch(Assets.buttons);
        batcher.drawSprite(455,40,75,75,Assets.uncheck);
        batcher.endBatch();

        glGraphics.getGl().glColor4f(1,1,1,1);
    }


    private void renderCards() {
        if(gameManager.playerCards.size()+gameManager.playerSplitOne.size()+gameManager.playerSplitTwo.size()+gameManager.playerSplitThree.size()+gameManager.dealerCards.size()>0) {
            batcher.beginBatch(Assets.cards);

            for (Card card : gameManager.playerCards) {
                batcher.drawSprite(card.getCurrentX() + (((int) card.getRotation() / 90) * 20 * card.getScaleX()), card.getCurrentY() + (((int) card.getRotation() / 90) * 20 * card.getScaleX()), 100 * card.getScaleX(), 140 * card.getScaleY(), card.getRotation(), card.getTexture());
            }
            for (Card card : gameManager.playerSplitOne) {
                batcher.drawSprite(card.getCurrentX() + (((int) card.getRotation() / 90) * 20 * card.getScaleX()), card.getCurrentY() + (((int) card.getRotation() / 90) * 20 * card.getScaleX()), 100 * card.getScaleX(), 140 * card.getScaleY(), card.getRotation(), card.getTexture());
            }
            for (Card card : gameManager.playerSplitTwo) {
                batcher.drawSprite(card.getCurrentX() + (((int) card.getRotation() / 90) * 20 * card.getScaleX()), card.getCurrentY() + (((int) card.getRotation() / 90) * 20 * card.getScaleX()), 100 * card.getScaleX(), 140 * card.getScaleY(), card.getRotation(), card.getTexture());
            }
            for (Card card : gameManager.playerSplitThree) {
                batcher.drawSprite(card.getCurrentX() + (((int) card.getRotation() / 90) * 20), card.getCurrentY() + (((int) card.getRotation() / 90) * 20), 100 * card.getScaleX(), 140 * card.getScaleY(), card.getRotation(), card.getTexture());
            }
            //If dealer card is visible draw from 0->end else draw from 1->0
            if(gameManager.dealerCards.get(1).getVisable()) {
                for (Card card : gameManager.dealerCards) {
                    batcher.drawSprite(card.getCurrentX(), card.getCurrentY(), 100 * card.getScaleX(), 140 * card.getScaleY(), card.getTexture());
                }
            }
            else {
                batcher.drawSprite(gameManager.dealerCards.get(1).getCurrentX(), gameManager.dealerCards.get(1).getCurrentY(), 100 * gameManager.dealerCards.get(1).getScaleX(), 140 * gameManager.dealerCards.get(1).getScaleY(), gameManager.dealerCards.get(1).getTexture());
                batcher.drawSprite(gameManager.dealerCards.get(0).getCurrentX(), gameManager.dealerCards.get(0).getCurrentY(), 100 * gameManager.dealerCards.get(0).getScaleX(), 140 * gameManager.dealerCards.get(0).getScaleY(), gameManager.dealerCards.get(0).getTexture());
            }
            if(gameManager.getShuffleInProgress()) {
                for(Card card:gameManager.shuffling) {
                    batcher.drawSprite(card.getCurrentX(),card.getCurrentY(),100,140,card.getTexture());
                }
            }
            batcher.endBatch();
        }

    }

    private void renderMoney(int state) {
        String money="$"+chipManager.getShownMoney();
        float x=270-9*(money.length()-1);
        float y=40;
        int payout=-1;
        float r=1,g=1,b=1;
        if(chipManager.getShownMoney()!=chipManager.getMoney()) {
            if(chipManager.getShownMoney()>chipManager.getMoney()) {
                chipManager.setShownMoney(chipManager.getShownMoney()-((chipManager.getShownMoney()-chipManager.getMoney())/15+1));
            }
            else {
                chipManager.setShownMoney(chipManager.getShownMoney()+((chipManager.getMoney()-chipManager.getShownMoney())/15+1));
            }
        }

        //Money left to play
        glGraphics.getGl().glColor4f(0,1,0,1);
        batcher.beginBatch(Assets.numbers);
        for (int i = 0; i < money.length(); i++) {
            TextureRegion temp= findNumber(money.charAt(i));
            batcher.drawSprite(x, y, temp.width * .5f, temp.height * .5f, temp);
            x+=18;
        }
        batcher.endBatch();
        glGraphics.getGl().glColor4f(1,1,1,1);

        if(state==0) {
            drawBetsWonLoss(270,140,chipManager.getMainBetMoney(),false);
            if(gameManager.getSideBetLeft().getVersion()>0)
                drawBetsWonLoss(160,140,chipManager.getSideBetLeftMoney(),false);
            if(gameManager.getSideBetRight().getVersion()>0)
                drawBetsWonLoss(380,140,chipManager.getSideBetRightMoney(),false);
        }
        else if(state==1 || state==2) {
            if(gameManager.getState()<6 && state==1)
                drawBetsWonLoss(270,140,chipManager.getMainBetMoney(),false);
            else
                drawBetsWonLoss(270,140,gameManager.checkPayout(),true);

            if(state==1) {
                if(gameManager.getSideBetLeft().getVersion()>0)
                    drawBetsWonLoss(160,140,gameManager.getSideBetLeft().getPayoutAmount(),true);
                if(gameManager.getSideBetRight().getVersion()>0)
                    drawBetsWonLoss(380,140,gameManager.getSideBetRight().getPayoutAmount(),true);
            }
            else {
                if(gameManager.getSideBetLeft().getOldVersion()>0)
                    drawBetsWonLoss(160,140,gameManager.getSideBetLeft().getPayoutAmount(),true);
                if(gameManager.getSideBetRight().getOldVersion()>0)
                    drawBetsWonLoss(380,140,gameManager.getSideBetRight().getPayoutAmount(),true);
            }

        }

    }

    //Colored shows a green/red win loss amount, uncolored is total number of chips in play
    private void drawBetsWonLoss(float x, float y, int payout, boolean colored) {
        float r=.7f,g=.7f,b=.7f;
        String money;
        if(payout<0) {
            if(colored) {
                money = "-$" + (payout * -1);
                r = 1;
                g = 0;
                b = 0;
            }
            else
                money = "$" + (payout*-1);
        }
        else if(payout>0){
            if(colored) {
                money = "+$" + payout;
                r = 0;
                g = 1;
                b = 0;
            }
            else
                money = "$" + payout;
        } //Print PUSH
        else {
            //PRINT PUSH
            money="$0";
            r=.7f;g=.7f;b=.7f;
        }

        x=x-18*.35f*(money.length()-1);
        y=y;
        glGraphics.getGl().glColor4f(r,g,b,1);
        batcher.beginBatch(Assets.numbers);
        for (int i = 0; i < money.length(); i++) {
            TextureRegion temp= findNumber(money.charAt(i));
            batcher.drawSprite(x, y, temp.width * .35f, temp.height * .35f, temp);
            x+=36*.35f;
        }
        batcher.endBatch();
        glGraphics.getGl().glColor4f(1,1,1,1);
    }

    private void renderBetsAndCounts() {
        if(bettingAlphas>0) {
            hand1.resetAlpha();
            hand2.resetAlpha();
            hand3.resetAlpha();
            hand4.resetAlpha();
            dealerHand.resetAlpha();
        }

        //Hand Bets
        if(gameManager.playerCards.size()!=0)
            betAndCountCheck(gameManager.playerCards,chipManager.getPlayerHandMoney(),hand1);
        if(gameManager.playerSplitOne.size()!=0)
            betAndCountCheck(gameManager.playerSplitOne,chipManager.getPlayerSplitOneMoney(),hand2);
        if(gameManager.playerSplitTwo.size()!=0)
            betAndCountCheck(gameManager.playerSplitTwo,chipManager.getPlayerSplitTwoMoney(),hand3);
        if(gameManager.playerSplitThree.size()!=0)
            betAndCountCheck(gameManager.playerSplitThree,chipManager.getPlayerSplitThreeMoney(),hand4);
        if(gameManager.dealerCards.size()!=0)
            dealerCountCheck(gameManager.dealerCards,dealerHand);

    }

    private void betAndCountCheck(ArrayList<Card> cards, int money, BetAndCount bet) {
        Card temp = cards.get(cards.size()-1);
        float xPos,yPos;
        float r=1,g=1,b=1;
        //Get Offsets for position
        xPos=cards.get((cards.size()-1)/2).getCurrentX();
        if(cards.size()%2==0)
            xPos+=16*cards.get(0).getScaleX();
        yPos=cards.get(0).getCurrentY()+93*cards.get(0).getScaleY();
        if(cards.get(0).getCurrentY()==400)
            xPos=270;

        int handTotal=Math.abs(gameManager.calcHand(cards));
        int dealerTotal=Math.abs(gameManager.calcHand(gameManager.dealerCards));

        //Get the Color for number: Red for bust, gray for push, green for win, white for hand in progress
        if(!gameManager.dealerCards.get(0).getVisable() || gameManager.getState()<6) {
            r=1;
            g=1;
            b=1;
        } //player wins
        else if(((dealerTotal>21 && handTotal<=21) || (handTotal<=21 && handTotal>dealerTotal)) && gameManager.getSurrender()==false) {
            r=0;
            g=1;
            b=0;
        } //player loses
        else if(((dealerTotal<=21 && dealerTotal>handTotal) || (handTotal>21)) || gameManager.getSurrender()==true) {
            r=1;
            g=0;
            b=0;
        } //push
        else if(dealerTotal==handTotal) {
            r=.7f;
            g=.7f;
            b=.7f;
        }

        if(temp.getCurrentX()==temp.getNewX() && temp.getNewX()>0 && temp.getNewX()<560)
            bet.setIncreaseAlpha(true);
        if(temp.getRotation()==0)
            bet.drawBetAndCount("$"+money,""+gameManager.calcHand(cards),xPos,yPos,.5f*cards.get(0).getScaleX(),.5f*cards.get(0).getScaleY(),r,g,b,false);
        else
            bet.drawBetAndCount("$"+money,""+gameManager.calcHand(cards),xPos,yPos,.5f*cards.get(0).getScaleX(),.5f*cards.get(0).getScaleY(),r,g,b,true);
    }

    private void dealerCountCheck(ArrayList<Card> cards, BetAndCount b) {
        if(cards.get(0).getCurrentY()==780 && cards.get(1).getVisable())
            b.setIncreaseAlpha(true);
        b.drawCount(""+gameManager.calcHand(cards),270,687,.5f,.5f,90);
    }


    private void renderHitButton() {
        if(gameManager.hitButton.getPressed())
            glGraphics.getGl().glColor4f(.5f,.5f,.5f,1);
        batcher.beginBatch(Assets.hit);
        batcher.drawSprite(gameManager.hitButton.getXPos(), gameManager.hitButton.getYPos(), gameManager.hitButton.getWidth(), gameManager.hitButton.getHeight(), Assets.hitButton);
        batcher.endBatch();
        glGraphics.getGl().glColor4f(1,1,1,1);
    }

    private void renderStandButton() {
        if(gameManager.standButton.getPressed())
            glGraphics.getGl().glColor4f(.5f,.5f,.5f,1);
        batcher.beginBatch(Assets.stand);
        batcher.drawSprite(gameManager.standButton.getXPos(), gameManager.standButton.getYPos(), gameManager.standButton.getWidth(), gameManager.standButton.getHeight(), Assets.standButton);
        batcher.endBatch();
        glGraphics.getGl().glColor4f(1,1,1,1);
    }

    private void renderDoubleButton() {
        if(gameManager.doubleButton.getPressed())
            glGraphics.getGl().glColor4f(.5f,.5f,.5f,1);
        batcher.beginBatch(Assets.doub);
        batcher.drawSprite(gameManager.doubleButton.getXPos(), gameManager.doubleButton.getYPos(), gameManager.doubleButton.getWidth(), gameManager.doubleButton.getHeight(), Assets.doubleButton);
        batcher.endBatch();
        glGraphics.getGl().glColor4f(1,1,1,1);
    }

    private void renderSplitButton() {
        if(gameManager.splitButton.getPressed())
            glGraphics.getGl().glColor4f(.5f,.5f,.5f,1);
        batcher.beginBatch(Assets.split);
        batcher.drawSprite(gameManager.splitButton.getXPos(), gameManager.splitButton.getYPos(), gameManager.splitButton.getWidth(), gameManager.splitButton.getHeight(), Assets.splitButton);
        batcher.endBatch();
        glGraphics.getGl().glColor4f(1,1,1,1);
    }

    private void renderSurrenderButton() {
        if(gameManager.surrenderButton.getPressed())
            glGraphics.getGl().glColor4f(.5f,.5f,.5f,1);
        batcher.beginBatch(Assets.surr);
        batcher.drawSprite(gameManager.surrenderButton.getXPos(),gameManager.surrenderButton.getYPos(),gameManager.surrenderButton.getWidth(),gameManager.surrenderButton.getHeight(),Assets.surrenderButton);
        batcher.endBatch();
        glGraphics.getGl().glColor4f(1,1,1,1);
    }

    private void renderHintButton() {
        glGraphics.getGl().glColor4f(1,1,1,gameManager.hintButton.getAlpha());
        if(gameManager.hintButton.getPressed())
            glGraphics.getGl().glColor4f(.5f,.5f,.5f,gameManager.hintButton.getAlpha());
        if(gameManager.getState()>=0 && gameManager.getState()<5) {
            if(gameManager.hintButton.getAlpha()<1)
                gameManager.hintButton.setAlpha(gameManager.hintButton.getAlpha()+.1f);
            else
                gameManager.hintButton.setAlpha(1);
            batcher.beginBatch(Assets.buttons);
            batcher.drawSprite(gameManager.hintButton.getXPos(), gameManager.hintButton.getYPos(), gameManager.hintButton.getWidth(), gameManager.hintButton.getHeight(), Assets.hint);
            batcher.endBatch();
        }
        else {
            if(gameManager.hintButton.getAlpha()>0)
                gameManager.hintButton.setAlpha(gameManager.hintButton.getAlpha()-.1f);
            else
                gameManager.hintButton.setAlpha(0);
        }

        glGraphics.getGl().glColor4f(1,1,1,1);
    }

    private void renderPlayButton() {
        glGraphics.getGl().glColor4f(1,1,1,gameManager.playButton.getAlpha());
        if(gameManager.playButton.getPressed())
            glGraphics.getGl().glColor4f(.5f,.5f,.5f,gameManager.playButton.getAlpha());
        if(gameManager.getState()>=0 && gameManager.getState()<5) {
            if(gameManager.playButton.getAlpha()<1)
                gameManager.playButton.setAlpha(gameManager.playButton.getAlpha()+.1f);
            else
                gameManager.playButton.setAlpha(1);
            batcher.beginBatch(Assets.buttons);
            batcher.drawSprite(gameManager.playButton.getXPos(), gameManager.playButton.getYPos(), gameManager.playButton.getWidth(), gameManager.playButton.getHeight(), Assets.play);
            batcher.endBatch();
        }
        else {
            if(gameManager.playButton.getAlpha()>0)
                gameManager.playButton.setAlpha(gameManager.playButton.getAlpha()-.1f);
            else
                gameManager.playButton.setAlpha(0);
        }

        glGraphics.getGl().glColor4f(1,1,1,1);
    }

    private void renderMenuButton() {
        batcher.beginBatch(Assets.buttons);
        batcher.drawSprite(gameManager.menu.getXPos(),gameManager.menu.getYPos(),gameManager.menu.getWidth(),gameManager.menu.getHeight(),Assets.menuButton);
        batcher.drawSprite(gameManager.settings.getXPos(),gameManager.settings.getYPos(),gameManager.settings.getWidth(),gameManager.settings.getHeight(),gameManager.settings.getTextureRegion());
        batcher.drawSprite(gameManager.rules.getXPos(),gameManager.rules.getYPos(),gameManager.rules.getWidth(),gameManager.rules.getHeight(),gameManager.rules.getTextureRegion());
        batcher.drawSprite(gameManager.freeChips.getXPos(),gameManager.freeChips.getYPos(),gameManager.freeChips.getWidth(),gameManager.freeChips.getHeight(),gameManager.freeChips.getTextureRegion());
        batcher.drawSprite(gameManager.sound.getXPos(),gameManager.sound.getYPos(),gameManager.sound.getWidth(),gameManager.sound.getHeight(),gameManager.sound.getTextureRegion());
        batcher.endBatch();
    }

    private void renderFreeChips(int state) {
        if(state!=0 && bettingAlphas>0) {
            bettingAlphas-=.1f;
            if(bettingAlphas<0)
                bettingAlphas=0;
        }
        else if(state==0 && bettingAlphas<1) {
            bettingAlphas+=.2f;
            if(bettingAlphas>1)
                bettingAlphas=1;
        }

        if(chipManager.getFreeChips().getPressed()) {
            glGraphics.getGl().glColor4f(.5f, .5f, .5f, bettingAlphas);
        }
        else {
            glGraphics.getGl().glColor4f(1, 1, 1, bettingAlphas);
        }

        batcher.beginBatch(Assets.buttons);
        batcher.drawSprite(chipManager.getFreeChips().getXPos(), chipManager.getFreeChips().getYPos(), chipManager.getFreeChips().getWidth(), chipManager.getFreeChips().getHeight(), chipManager.getFreeChips().getTextureRegion());
        batcher.endBatch();

        if (gameManager.playerCards.size() != 0 && state != 1 && state!=3) {
            glGraphics.getGl().glColor4f(.5f,1,1,.5f);
            if(state==2 && chipManager.getPreviousArrow().getRotation()!=-180) {
                chipManager.getPreviousArrow().setRotation(chipManager.getPreviousArrow().getRotation()-10);
                if(chipManager.getPreviousArrow().getRotation()==-360)
                    chipManager.getPreviousArrow().setRotation(0);
            }
            else if(chipManager.getPreviousArrow().getRotation()!=0 && state!=2) {
                chipManager.getPreviousArrow().setRotation(chipManager.getPreviousArrow().getRotation()-10);
                if(chipManager.getPreviousArrow().getRotation()==-360)
                    chipManager.getPreviousArrow().setRotation(0);
            }

            batcher.beginBatch(Assets.buttons);
            batcher.drawSprite(chipManager.getPreviousArrow().getXPos(), chipManager.getPreviousArrow().getYPos(), chipManager.getPreviousArrow().getWidth()*.5f, chipManager.getPreviousArrow().getHeight()*.5f, chipManager.getPreviousArrow().getRotation(), chipManager.getPreviousArrow().getTextureRegion());
            batcher.endBatch();
            glGraphics.getGl().glColor4f(1,1,1,bettingAlphas);
        }

        glGraphics.getGl().glColor4f(1,1,1,1);
    }

    private void renderBetButtons(int state) {
        if(state!=0 && bettingAlphas>0) {
            bettingAlphas-=.1f;
            if(bettingAlphas<0)
                bettingAlphas=0;
        }
        else if(state==0 && bettingAlphas<1) {
            bettingAlphas+=.2f;
            if(bettingAlphas>1)
                bettingAlphas=1;
        }

        messageAlphas+=.01f;
        if(messageAlphas>=1)
            messageAlphas=-1;
        if(Math.abs(messageAlphas)>bettingAlphas)
            messageAlphas=bettingAlphas;

        if(chipManager.getDirection()==1) {
            glGraphics.getGl().glColor4f(0, 1, 0, Math.abs(messageAlphas));
            writeText("TAP TO ADD CHIPS", 187, 108, .35f);
        }
        else {
            glGraphics.getGl().glColor4f(1,0,0,Math.abs(messageAlphas));
            writeText("TAP TO REMOVE CHIPS",169,108,.35f);
        }

        glGraphics.getGl().glColor4f(1,1,1,bettingAlphas);

        batcher.beginBatch(Assets.buttons);
        batcher.drawSprite(chipManager.getBetChips().getXPos(), chipManager.getBetChips().getYPos(), Math.abs(chipManager.getBetChips().getWidth() * chipManager.getBetChips().getScaleX()), Math.abs(chipManager.getBetChips().getHeight() * chipManager.getBetChips().getScaleY()), chipManager.getBetChips().getTextureRegion());
        batcher.endBatch();
        if(chipManager.getRepeat().getPressed()) {
            glGraphics.getGl().glColor4f(.5f,.5f,.5f,bettingAlphas);
        }

        batcher.beginBatch(Assets.buttons);
        batcher.drawSprite(chipManager.getRepeat().getXPos(),chipManager.getRepeat().getYPos(),chipManager.getRepeat().getWidth(),chipManager.getRepeat().getHeight(),chipManager.getRepeat().getTextureRegion());
        batcher.endBatch();

        glGraphics.getGl().glColor4f(1,1,1,bettingAlphas);

        if(chipManager.getDealButton().getPressed()) {
            if(chipManager.getDealButton().getAlpha()<bettingAlphas)
                glGraphics.getGl().glColor4f(.5f, .5f, .5f, chipManager.getDealButton().getAlpha());
            else
                glGraphics.getGl().glColor4f(.5f, .5f, .5f, bettingAlphas);
        }
        else {
            if(chipManager.getDealButton().getAlpha()<bettingAlphas)
                glGraphics.getGl().glColor4f(1, 1, 1, chipManager.getDealButton().getAlpha());
            else
                glGraphics.getGl().glColor4f(1, 1, 1, bettingAlphas);
        }

        batcher.beginBatch(Assets.buttons);
        batcher.drawSprite(chipManager.getDealButton().getXPos(), chipManager.getDealButton().getYPos(), chipManager.getDealButton().getWidth(), chipManager.getDealButton().getHeight(), chipManager.getDealButton().getTextureRegion());
        batcher.endBatch();
        //draw the previous cards arrow
        if (gameManager.playerCards.size() != 0 && state != 1 && state!=3) {
            glGraphics.getGl().glColor4f(.5f,1,1,.5f);
            if(state==2 && chipManager.getPreviousArrow().getRotation()!=-180) {
                chipManager.getPreviousArrow().setRotation(chipManager.getPreviousArrow().getRotation()-10);
                if(chipManager.getPreviousArrow().getRotation()==-360)
                    chipManager.getPreviousArrow().setRotation(0);
            }
            else if(chipManager.getPreviousArrow().getRotation()!=0 && state!=2) {
                chipManager.getPreviousArrow().setRotation(chipManager.getPreviousArrow().getRotation()-10);
                if(chipManager.getPreviousArrow().getRotation()==-360)
                    chipManager.getPreviousArrow().setRotation(0);
            }

            batcher.beginBatch(Assets.buttons);
            batcher.drawSprite(chipManager.getPreviousArrow().getXPos(), chipManager.getPreviousArrow().getYPos(), chipManager.getPreviousArrow().getWidth()*.5f, chipManager.getPreviousArrow().getHeight()*.5f, chipManager.getPreviousArrow().getRotation(), chipManager.getPreviousArrow().getTextureRegion());
            batcher.endBatch();
            glGraphics.getGl().glColor4f(1,1,1,bettingAlphas);
        }

        drawChips(chipManager.getChip1(), 0);
        drawChips(chipManager.getChip5(),1);
        drawChips(chipManager.getChip25(),2);
        drawChips(chipManager.getChip100(),3);
        drawChips(chipManager.getChip500(),4);
        drawChips(chipManager.getChip1000(),5);
        drawChips(chipManager.getChip10000(),6);

        glGraphics.getGl().glColor4f(1,1,1,1);

        if(bettingAlphas==1)
            chipParticles.drawParticles(glGraphics,batcher);
    }
    //chip 0-6
    private void drawChips(Button b, int chip) {
        if(chip==chipManager.getChipLocation())
            glGraphics.getGl().glColor4f(1,1,1,bettingAlphas);
        else
            glGraphics.getGl().glColor4f(.5f,.5f,.5f,bettingAlphas);

        batcher.beginBatch(Assets.chips);
        batcher.drawSprite(b.getXPos(), b.getYPos(), b.getWidth(), b.getHeight(), b.getTextureRegion());
        batcher.endBatch();

        glGraphics.getGl().glColor4f(1,1,1,1);
    }

    private void renderChips() {
        if(chipManager.getChips().size()+chipManager.getSideBetLeft().size()+chipManager.getSideBetRight().size()+chipManager.getMainBet().size()!=0) {
            int j=0;
            batcher.beginBatch(Assets.chips);
            for(int i=0;i<chipManager.getSideBetLeft().size() && j<1800;i++,j++) {
                Chip chip=chipManager.getSideBetLeft().get(i);
                batcher.drawSprite(chip.getCurrentX(), chip.getCurrentY(), 67 * chip.getScaleX(), 67 * chip.getScaleY(), chip.getTexture());
            }
            for(int i=0;i<chipManager.getSideBetRight().size() && j<1800;i++,j++) {
                Chip chip=chipManager.getSideBetRight().get(i);
                batcher.drawSprite(chip.getCurrentX(), chip.getCurrentY(), 67 * chip.getScaleX(), 67 * chip.getScaleY(), chip.getTexture());
            }
            for(int i=0;i<chipManager.getMainBet().size() && j<1800;i++,j++) {
                Chip chip=chipManager.getMainBet().get(i);
                batcher.drawSprite(chip.getCurrentX(), chip.getCurrentY(), 67 * chip.getScaleX(), 67 * chip.getScaleY(), chip.getTexture());
            }
            for(int i=0;i<chipManager.getChips().size() && j<1800;i++,j++) {
                Chip chip=chipManager.getChips().get(i);
                batcher.drawSprite(chip.getCurrentX(), chip.getCurrentY(), 67 * chip.getScaleX(), 67 * chip.getScaleY(), chip.getTexture());
            }
            batcher.endBatch();
        }
    }

    private void renderBackground(int state) {


        if(state==4) {
            float[] ambient = {.2f, .2f, .2f, 1};
            float[] position = {0, 0, 1, 0};

            glGraphics.getGl().glLightfv(glGraphics.getGl().GL_LIGHT0, glGraphics.getGl().GL_DIFFUSE, ambient, 0);
            glGraphics.getGl().glLightfv(glGraphics.getGl().GL_LIGHT0, glGraphics.getGl().GL_POSITION, position, 0);

            glGraphics.getGl().glEnable(glGraphics.getGl().GL_LIGHTING);
            glGraphics.getGl().glEnable(glGraphics.getGl().GL_LIGHT0);
        }

        glGraphics.getGl().glColor4f(1,1,1,1);

        batcher.beginBatch(Assets.background);
        batcher.drawSprite(270, 480, 540, 960, settingsManager.getBackgroundTexture());
        batcher.endBatch();

        batcher.beginBatch(Assets.bet);
        batcher.drawSprite(270, 230, 151,151,Assets.mainBet);
        batcher.endBatch();


        if(state==2) {
            gameManager.getSideBetLeft().drawPayouts(batcher, glGraphics, gameManager.getSideBetLeft().getOldPayout(),gameManager.getSideBetLeft().getOldVersion());
        }
        else
            gameManager.getSideBetLeft().drawPayouts(batcher,glGraphics,gameManager.getSideBetLeft().getPayout(),gameManager.getSideBetLeft().getVersion());


        if(state==2) {
            gameManager.getSideBetRight().drawPayouts(batcher, glGraphics, gameManager.getSideBetRight().getOldPayout(),gameManager.getSideBetRight().getOldVersion());
        }
        else
            gameManager.getSideBetRight().drawPayouts(batcher,glGraphics,gameManager.getSideBetRight().getPayout(),gameManager.getSideBetRight().getVersion());


        glGraphics.getGl().glColor4f(.5f,.5f,.5f,.8f);


        writeText("DEALER "+settingsManager.getDealerString()+" ON SOFT 17",15,950,.35f);
        writeText("BLACKJACK PAYS: "+settingsManager.getBlackjackPaysString(),15,928,.35f);
        writeText("INSURANCE PAYS: 2/1",13,906,.35f);
        if(settingsManager.getCSM()==true) {
            writeText("CONTINUOUS SHUFFLING",15,884,.35f);
        }
        else if(!gameManager.getShuffleInProgress() && ((int)(gameManager.getDecks()*52*(settingsManager.getPenetration()/100f))-gameManager.getPointer())>0)
            //writeText("CARDS SEEN:" + ((int)(gameManager.getDecks()*52*(settingsManager.getPenetration()/100f))-gameManager.getPointer()),15,884,.35f);
            writeText("CARDS SEEN: " + gameManager.getPointer() + " OF " + gameManager.getDecks()*52,15,884,.35f);
        else
            writeText("SHUFFLING",15,884,.35f);
        writeText("DECKS: " + gameManager.getDecks(),15,862,.35f);

        glGraphics.getGl().glColor4f(1,1,1,1);
        glGraphics.getGl().glDisable(glGraphics.getGl().GL_LIGHTING);
    }

    private void renderEdge() {
        batcher.beginBatch(Assets.edgeBackground);
        batcher.drawSprite(270,480,540,960,Assets.edge);
        batcher.endBatch();
    }

    public ParticleGroup getChipParticles() {
        return chipParticles;
    }

    private void writeText(String text, float x, float y,float size) {
        batcher.beginBatch(Assets.numbers);
        TextureRegion temp = findNumber(text.charAt(0));
        for (int i = 0; i < text.length(); i++) {
            batcher.drawSprite(x, y, temp.width * size, temp.height * size, temp);
            if (i < text.length() - 1) {
                x+=(temp.width/2+2)*size;
                temp = findNumber(text.charAt(i+1));
                x+=(temp.width/2+2)*size;
            }
        }
        batcher.endBatch();
    }

    private TextureRegion findSettingsNumber(char c) {
        TextureRegion r;
        switch(c) {
            case '1':
                r=new TextureRegion(Assets.settingsFont,0,0,12,28);
                break;
            case '2':
                r=new TextureRegion(Assets.settingsFont,15,0,13,28);
                break;
            case '3':
                r=new TextureRegion(Assets.settingsFont,31,0,13,28);
                break;
            case '4':
                r=new TextureRegion(Assets.settingsFont,46,0,15,28);
                break;
            case '5':
                r=new TextureRegion(Assets.settingsFont,63,0,14,28);
                break;
            case '6':
                r=new TextureRegion(Assets.settingsFont,79,0,14,28);
                break;
            case '7':
                r=new TextureRegion(Assets.settingsFont,95,0,14,28);
                break;
            case '8':
                r=new TextureRegion(Assets.settingsFont,111,0,15,28);
                break;
            case '9':
                r=new TextureRegion(Assets.settingsFont,128,0,14,28);
                break;
            case '0':
                r=new TextureRegion(Assets.settingsFont,144,0,14,28);
                break;
            case '$':
                r=new TextureRegion(Assets.settingsFont,159,0,15,28);
                break;
            case '+':
                r=new TextureRegion(Assets.settingsFont,176,0,15,28);
                break;
            case '-':
                r=new TextureRegion(Assets.settingsFont,192,0,15,28);
                break;
            case '%':
                r=new TextureRegion(Assets.settingsFont,208,0,21,28);
                break;
            case ':':
                r=new TextureRegion(Assets.settingsFont,232,0,5,28);
                break;
            default:
                r=null;
        }

        return r;
    }

    private TextureRegion findSettingsNumber2(char c) {
        TextureRegion r;
        switch(c) {
            case '1':
                r=new TextureRegion(Assets.settingsFont,0,0,24,56);
                break;
            case '2':
                r=new TextureRegion(Assets.settingsFont,31,0,26,56);
                break;
            case '3':
                r=new TextureRegion(Assets.settingsFont,63,0,26,56);
                break;
            case '4':
                r=new TextureRegion(Assets.settingsFont,93,0,30,56);
                break;
            case '5':
                r=new TextureRegion(Assets.settingsFont,127,0,27,56);
                break;
            case '6':
                r=new TextureRegion(Assets.settingsFont,160,0,27,56);
                break;
            case '7':
                r=new TextureRegion(Assets.settingsFont,192,0,27,56);
                break;
            case '8':
                r=new TextureRegion(Assets.settingsFont,224,0,28,56);
                break;
            case '9':
                r=new TextureRegion(Assets.settingsFont,257,0,27,56);
                break;
            case '0':
                r=new TextureRegion(Assets.settingsFont,289,0,28,56);
                break;
            case '$':
                r=new TextureRegion(Assets.settingsFont,322,0,27,56);
                break;
            case '+':
                r=new TextureRegion(Assets.settingsFont,354,0,28,56);
                break;
            case '-':
                r=new TextureRegion(Assets.settingsFont,384,0,28,56);
                break;
            case ':':
                r=new TextureRegion(Assets.settingsFont,415,0,7,56);
                break;
            case '%':
                r=new TextureRegion(Assets.settingsFont,430,0,42,56);
                break;
            default:
                r=null;
        }

        return r;
    }

    private TextureRegion findNumber(char c) {
        TextureRegion r;

        switch(c) {
            case '1':
                r=new TextureRegion(Assets.numbers,0,0,22,60);
                break;
            case '2':
                r=new TextureRegion(Assets.numbers,26,0,29,60);
                break;
            case '3':
                r=new TextureRegion(Assets.numbers,58,0,30,60);
                break;
            case '4':
                r=new TextureRegion(Assets.numbers,90,0,32,60);
                break;
            case '5':
                r=new TextureRegion(Assets.numbers,124,0,31,60);
                break;
            case '6':
                r=new TextureRegion(Assets.numbers,159,0,31,60);
                break;
            case '7':
                r=new TextureRegion(Assets.numbers,191,0,24,60);
                break;
            case '8':
                r=new TextureRegion(Assets.numbers,217,0,31,60);
                break;
            case '9':
                r=new TextureRegion(Assets.numbers,252,0,31,60);
                break;
            case '0':
                r=new TextureRegion(Assets.numbers,286,0,31,60);
                break;
            case '$':
                r=new TextureRegion(Assets.numbers,320,0,32,60);
                break;
            case '/':
                r=new TextureRegion(Assets.numbers,354,0,25,60);
                break;
            case '+':
                r=new TextureRegion(Assets.numbers,382,0,30,60);
                break;
            case '-':
                r=new TextureRegion(Assets.numbers,416,0,30,60);
                break;
            case ':':
                r=new TextureRegion(Assets.numbers,451,0,11,60);
                break;
            case 'A':
                r= new TextureRegion(Assets.numbers,0,66,34,60);
                break;
            case 'B':
                r= new TextureRegion(Assets.numbers,36,66,32,60);
                break;
            case 'C':
                r= new TextureRegion(Assets.numbers,72,66,33,60);
                break;
            case 'D':
                r= new TextureRegion(Assets.numbers,109,66,33,60);
                break;
            case 'E':
                r= new TextureRegion(Assets.numbers,146,66,24,60);
                break;
            case 'F':
                r= new TextureRegion(Assets.numbers,174,66,24,60);
                break;
            case 'G':
                r= new TextureRegion(Assets.numbers,200,66,33,60);
                break;
            case 'H':
                r= new TextureRegion(Assets.numbers,237,66,33,60);
                break;
            case 'I':
                r= new TextureRegion(Assets.numbers,274,66,15,60);
                break;
            case 'J':
                r= new TextureRegion(Assets.numbers,292,66,20,60);
                break;
            case 'K':
                r= new TextureRegion(Assets.numbers,316,66,34,60);
                break;
            case 'L':
                r= new TextureRegion(Assets.numbers,352,66,23,60);
                break;
            case 'M':
                r= new TextureRegion(Assets.numbers,377,66,43,60);
                break;
            case 'N':
                r= new TextureRegion(Assets.numbers,425,66,31,60);
                break;
            case 'O':
                r= new TextureRegion(Assets.numbers,0,130,32,60);
                break;
            case 'P':
                r= new TextureRegion(Assets.numbers,36,130,31,60);
                break;
            case 'Q':
                r= new TextureRegion(Assets.numbers,70,130,32,61);
                break;
            case 'R':
                r= new TextureRegion(Assets.numbers,106,130,32,60);
                break;
            case 'S':
                r= new TextureRegion(Assets.numbers,141,130,32,60);
                break;
            case 'T':
                r= new TextureRegion(Assets.numbers,174,130,31,60);
                break;
            case 'U':
                r= new TextureRegion(Assets.numbers,207,130,32,60);
                break;
            case 'V':
                r= new TextureRegion(Assets.numbers,241,130,35,60);
                break;
            case 'W':
                r= new TextureRegion(Assets.numbers,280,130,53,60);
                break;
            case 'X':
                r= new TextureRegion(Assets.numbers,338,130,32,60);
                break;
            case 'Y':
                r= new TextureRegion(Assets.numbers,372,130,32,60);
                break;
            case 'Z':
                r= new TextureRegion(Assets.numbers,408,130,26,60);
                break;
            case ' ':
                r= new TextureRegion(Assets.numbers,436,130,16,60);
                break;
            default:
                r= null;
        }

        return r;
    }


    public void loadData(SpriteBatcher batcher, GLGraphics glGraphics, GameManager gameManager, ChipManager chipManager, SlotMachine slotMachine, Settings settingsManager, Camera2D cam) {
        chipParticles.loadTextures(Assets.particle,Assets.p);
        hand1.load(glGraphics,batcher);
        hand2.load(glGraphics,batcher);
        hand3.load(glGraphics,batcher);
        hand4.load(glGraphics,batcher);
        dealerHand.load(glGraphics,batcher);

        this.batcher=batcher;
        this.glGraphics=glGraphics;
        this.gameManager=gameManager;
        this.chipManager=chipManager;
        this.slotMachine=slotMachine;
        this.settingsManager=settingsManager;
        this.cam=cam;
    }

    public void loadParticles() {
        chipParticles.loadTextures(Assets.particle,Assets.p);
    }

}

