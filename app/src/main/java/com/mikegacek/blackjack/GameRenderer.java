package com.mikegacek.blackjack;

import android.util.Log;

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
    private transient SettingsManager settingsManager;
    private transient Camera2D cam;
    private float bettingAlphas,insuranceAlphas;
    private float hand1Alpha,hand2Alpha,hand3Alpha,hand4Alpha,dealerHandAlpha;
    private float light;
    private Texture backgroundTexture;
    private TextureRegion backgroundTextureRegion;

    public GameRenderer(GLGraphics glGraphics, SpriteBatcher batcher, GameManager gameManager, ChipManager chipManager, SlotMachine slotMachine, SettingsManager settings) {
        this.batcher=batcher;
        this.glGraphics=glGraphics;
        this.gameManager=gameManager;
        this.chipManager=chipManager;
        this.slotMachine=slotMachine;
        this.settingsManager=settings;
        this.cam = new Camera2D(glGraphics, 540, 960);
        bettingAlphas=1;
        insuranceAlphas=0;
        light=1;
        backgroundTexture=Assets.greenBackground;
        backgroundTextureRegion=Assets.green;
    }

    public void render(int state) {

        if(state<5) {
            renderBackground(state);
            renderShoeMeter(state);
            renderPressedButton(Assets.buttons,gameManager.doubleButton);
            renderPressedButton(Assets.buttons,gameManager.splitButton);
            renderPressedButton(Assets.buttons,gameManager.surrenderButton);
            renderEdge();
            renderMoney(state);
            renderPressedButton(Assets.buttons,gameManager.hitButton);
            renderPressedButton(Assets.buttons,gameManager.standButton);
            if(chipManager.getMoney()==0 &&chipManager.getMainBet().isEmpty() && chipManager.getSideBetLeft().isEmpty() && chipManager.getSideBetRight().isEmpty() && chipManager.getChips().isEmpty())
                renderFreeChips(state);
            else
                renderBetButtons(state);
            renderCards();
            renderBetsAndCounts();
            renderChips();
            if (state == 0 || state == 3)
                renderMenuButton(state);
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
        //Background
        batcher.beginBatch(Assets.settingsBackground);
        batcher.drawSprite(540,960,1080,1920,Assets.settingsBack);
        batcher.endBatch();

        //Toggles with Color
        renderToggleWithColor(Assets.buttons,settingsManager.getCSMToggle());
        renderToggleWithColor(Assets.buttons,settingsManager.getInsuranceToggle());
        renderToggleWithColor(Assets.buttons,settingsManager.getSurrenderToggle());
        renderToggleWithColor(Assets.buttons,settingsManager.getResplitAcesToggle());
        renderToggleWithColor(Assets.buttons,settingsManager.getHitSplitAcesToggle());
        renderToggleWithColor(Assets.buttons,settingsManager.getDoubleSplitAcesToggle());
        renderToggleWithColor(Assets.buttons,settingsManager.getDoubleAfterSplitToggle());

        //Slider

        renderSlider(Assets.buttons,settingsManager.getDeckPenetration());
        CalibriFont.drawNumbersBackwardsAndEqualDistance(settingsManager.getPenetration()+"",908,1581,1,1,1,1,.55f,.55f,batcher,glGraphics);

        //Buttons with Color
        renderButtonWithColor(Assets.buttons,settingsManager.getDeck1());
        renderButtonWithColor(Assets.buttons,settingsManager.getDeck2());
        renderButtonWithColor(Assets.buttons,settingsManager.getDeck3());
        renderButtonWithColor(Assets.buttons,settingsManager.getDeck4());
        renderButtonWithColor(Assets.buttons,settingsManager.getDeck5());
        renderButtonWithColor(Assets.buttons,settingsManager.getDeck6());
        renderButtonWithColor(Assets.buttons,settingsManager.getDeck7());
        renderButtonWithColor(Assets.buttons,settingsManager.getDeck8());

        renderButtonWithColor(Assets.buttons,settingsManager.getBlackjackPays32());
        renderButtonWithColor(Assets.buttons,settingsManager.getBlackjackPays75());
        renderButtonWithColor(Assets.buttons,settingsManager.getBlackjackPays65());
        renderButtonWithColor(Assets.buttons,settingsManager.getBlackjackPays11());

        renderButtonWithColor(Assets.buttons,settingsManager.getDealerStand());
        renderButtonWithColor(Assets.buttons,settingsManager.getDealerHit());

        renderButtonWithColor(Assets.buttons,settingsManager.getSplit2());
        renderButtonWithColor(Assets.buttons,settingsManager.getSplit3());
        renderButtonWithColor(Assets.buttons,settingsManager.getSplit4());

        renderButtonWithColor(Assets.buttons,settingsManager.getDoubleAny2());
        renderButtonWithColor(Assets.buttons,settingsManager.getDouble911());
        renderButtonWithColor(Assets.buttons,settingsManager.getDouble1011());
        //Exit Button

        renderPressedButton(Assets.buttons,settingsManager.getExitSettings());
        //Foregound
        batcher.beginBatch(Assets.settingsForeground);
        batcher.drawSprite(540,960,1080,1920,Assets.settingsFore);
        batcher.endBatch();
    }

    private void renderToggleWithColor(Texture t,Toggle toggle) {
        glGraphics.getGl().glColor4f(toggle.getRedf(),toggle.getGreenf(),toggle.getBluef(),toggle.getAlpha());

        batcher.beginBatch(t);
        batcher.drawSprite(toggle,Assets.settingsToggle);
        batcher.endBatch();

        glGraphics.getGl().glColor4f(1,1,1,1);
        batcher.beginBatch(t);
        batcher.drawSprite(toggle.getCircleXPos(),toggle.getyPos(),63,63,Assets.settingsToggleCircle);
        batcher.endBatch();
    }

    private void renderButtonWithColor(Texture t, Button button) {
        glGraphics.getGl().glColor4f(button.getRedf(),button.getGreenf(),button.getBluef(),button.getAlpha());

        batcher.beginBatch(t);
        batcher.drawSprite(button,button.getTextureRegion());
        batcher.endBatch();

        glGraphics.getGl().glColor4f(1,1,1,1);
    }

    private void renderSlider(Texture t,Slider slider) {
        // draw at 280,get Y, 560 long , 9 wide
        batcher.beginBatch(t);
        batcher.drawSprite((slider.getxPos()+slider.getBarXStart())/2,slider.getyPos(),(slider.getxPos()-slider.getBarXStart()),9,Assets.settingsSliderOrangeBar);
        batcher.drawSprite(((slider.getBarXEnd()+slider.getxPos())/2),slider.getyPos(),slider.getBarXEnd()-slider.getxPos(),9,Assets.settingsSliderGrayBar);
        batcher.endBatch();

        batcher.beginBatch(t);
        batcher.drawSprite(slider.getxPos(),slider.getyPos(),69,69,Assets.settingsSliderCircle);
        batcher.endBatch();
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
        batcher.drawSprite(541,1224,986,383,Assets.insurance);
        batcher.endBatch();

        String money="$";
        int amount=chipManager.getMainBetMoney()/2;
        money=money+amount;
        TextureRegion tempChip;
        float x=40*(money.length()-1)+128;
        float y=1260;

        if(amount<5)
            tempChip=new TextureRegion(Assets.chips,0,128,128,128);
        else if(amount<25)
            tempChip=new TextureRegion(Assets.chips,128,128,128,128);
        else if(amount<100)
            tempChip=new TextureRegion(Assets.chips,256,128,128,128);
        else if(amount<500)
            tempChip=new TextureRegion(Assets.chips,384,128,128,128);
        else if(amount<1000)
            tempChip=new TextureRegion(Assets.chips,512,128,128,128);
        else if(amount<10000)
            tempChip=new TextureRegion(Assets.chips,640,128,128,128);
        else
            tempChip=new TextureRegion(Assets.chips,768,128,128,128);

        batcher.beginBatch(Assets.chips);
        batcher.drawSprite(540-x,y,128,128,tempChip);
        batcher.endBatch();

        //temp 1.2 scale
        CalibriFont.drawNumbersCentered(money,540,y,1,1,1,1,1.2f,1.2f,batcher,glGraphics);

        batcher.beginBatch(Assets.chips);
        batcher.drawSprite(540+x,y,128,128,tempChip);
        batcher.endBatch();

        if(gameManager.yes.getPressed())
            glGraphics.getGl().glColor4f(.5f,.5f,.5f,insuranceAlphas);

        batcher.beginBatch(Assets.ins);
        batcher.drawSprite(gameManager.yes,Assets.yes);
        batcher.endBatch();

        glGraphics.getGl().glColor4f(1,1,1,insuranceAlphas);

        if(gameManager.no.getPressed())
            glGraphics.getGl().glColor4f(.5f,.5f,.5f,insuranceAlphas);

        batcher.beginBatch(Assets.ins);
        batcher.drawSprite(gameManager.no,Assets.no);
        batcher.endBatch();

        glGraphics.getGl().glColor4f(1,1,1,1);
    }


    private void renderCards() {
        if(gameManager.playerCards.size()+gameManager.playerSplitOne.size()+gameManager.playerSplitTwo.size()+gameManager.playerSplitThree.size()+gameManager.dealerCards.size()>0) {
            batcher.beginBatch(Assets.cards);

            for (Card card : gameManager.playerCards) {
                batcher.drawSprite(card.getCurrentX() + (((int) card.getRotation() / 90) * 30 * card.getScaleX()), card.getCurrentY() + (((int) card.getRotation() / 90) * 30 * card.getScaleX()), 150 * card.getScaleX(), 210 * card.getScaleY(), card.getRotation(), card.getTexture());
            }
            for (Card card : gameManager.playerSplitOne) {
                batcher.drawSprite(card.getCurrentX() + (((int) card.getRotation() / 90) * 30 * card.getScaleX()), card.getCurrentY() + (((int) card.getRotation() / 90) * 30 * card.getScaleX()), 150 * card.getScaleX(), 210 * card.getScaleY(), card.getRotation(), card.getTexture());
            }
            for (Card card : gameManager.playerSplitTwo) {
                batcher.drawSprite(card.getCurrentX() + (((int) card.getRotation() / 90) * 30 * card.getScaleX()), card.getCurrentY() + (((int) card.getRotation() / 90) * 30 * card.getScaleX()), 150 * card.getScaleX(), 210 * card.getScaleY(), card.getRotation(), card.getTexture());
            }
            for (Card card : gameManager.playerSplitThree) {
                batcher.drawSprite(card.getCurrentX() + (((int) card.getRotation() / 90) * 30* card.getScaleX()), card.getCurrentY() + (((int) card.getRotation() / 90) * 30* card.getScaleX()), 150 * card.getScaleX(), 210 * card.getScaleY(), card.getRotation(), card.getTexture());
            }
            //If dealer card is visible draw from 0->end else draw from 1->0
            if(gameManager.dealerCards.get(1).getVisable()) {
                for (Card card : gameManager.dealerCards) {
                    batcher.drawSprite(card.getCurrentX(), card.getCurrentY(), 150 * card.getScaleX(), 210 * card.getScaleY(), card.getTexture());
                }
            }
            else {
                batcher.drawSprite(gameManager.dealerCards.get(1).getCurrentX(), gameManager.dealerCards.get(1).getCurrentY(), 150 * gameManager.dealerCards.get(1).getScaleX(), 210 * gameManager.dealerCards.get(1).getScaleY(), gameManager.dealerCards.get(1).getTexture());
                batcher.drawSprite(gameManager.dealerCards.get(0).getCurrentX(), gameManager.dealerCards.get(0).getCurrentY(), 150 * gameManager.dealerCards.get(0).getScaleX(), 210 * gameManager.dealerCards.get(0).getScaleY(), gameManager.dealerCards.get(0).getTexture());
            }
            if(gameManager.getShuffleInProgress()) {
                for(Card card:gameManager.shuffling) {
                    batcher.drawSprite(card.getCurrentX(),card.getCurrentY(),150*card.getScaleX(),210*card.getScaleY(),card.getTexture());
                }
            }
            batcher.endBatch();
        }

    }

    private void renderMoney(int state) {
        String money="$"+chipManager.getShownMoney();
        float x=540-17.5f*(money.length()-1);

        float y=76;
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
        CalibriFont.drawNumberEqualDistance(money,x,y,0,1,0,1,.75f,.75f,batcher,glGraphics);
        glGraphics.getGl().glColor4f(1,1,1,1);

        if(state==0) {
            drawBetsWonLoss(540,320,chipManager.getMainBetMoney(),false);
            if(gameManager.getSideBetLeft().getVersion()>0)
                drawBetsWonLoss(304,575,chipManager.getSideBetLeftMoney(),false);
            if(gameManager.getSideBetRight().getVersion()>0)
                drawBetsWonLoss(776,575,chipManager.getSideBetRightMoney(),false);
        }
        else if(state==1 || state==2) {
            if(gameManager.getState()<6 && state==1)
                drawBetsWonLoss(540,320,chipManager.getMainBetMoney(),false);
            else
                drawBetsWonLoss(540,320,gameManager.checkPayout(),true);

            if(state==1) {
                if(gameManager.getSideBetLeft().getVersion()>0)
                    drawBetsWonLoss(304,575,gameManager.getSideBetLeft().getPayoutAmount(),true);
                if(gameManager.getSideBetRight().getVersion()>0)
                    drawBetsWonLoss(776,575,gameManager.getSideBetRight().getPayoutAmount(),true);
            }
            else {
                if(gameManager.getOldSideBetLeft().getVersion()>0)
                    drawBetsWonLoss(304,575,gameManager.getOldSideBetLeft().getPayoutAmount(),true);
                if(gameManager.getOldSideBetRight().getVersion()>0)
                    drawBetsWonLoss(776,575,gameManager.getOldSideBetRight().getPayoutAmount(),true);
            }

        }

    }

    private String addCommas(String string) {
        int index=string.length()%3;
        while(index<string.length()&& index!=0) {
            string = string.substring(0, index) + "," + string.substring(index);
            index+=4;
        }
        return string;
    }

    private int numberOfCommas(String string) {
        int total=0;
        for(int i=0;i<string.length();i++) {
            if(string.charAt(i)==',')
                total++;
        }
        return total;
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
        CalibriFont.drawNumbersCentered(money,x,y,r,g,b,1,.6f,.6f,batcher,glGraphics);
    }

    private void renderBetsAndCounts() {
        if(bettingAlphas>0) {
            hand1Alpha=0;
            hand2Alpha=0;
            hand3Alpha=0;
            hand4Alpha=0;
            dealerHandAlpha=0;
        }

        //Hand Bets
        if(gameManager.playerCards.size()!=0)
            hand1Alpha=betAndCountCheck(gameManager.playerCards,chipManager.getPlayerHandMoney(),hand1Alpha);
        if(gameManager.playerSplitOne.size()!=0)
            hand2Alpha=betAndCountCheck(gameManager.playerSplitOne,chipManager.getPlayerSplitOneMoney(),hand2Alpha);
        if(gameManager.playerSplitTwo.size()!=0)
            hand3Alpha=betAndCountCheck(gameManager.playerSplitTwo,chipManager.getPlayerSplitTwoMoney(),hand3Alpha);
        if(gameManager.playerSplitThree.size()!=0)
            hand4Alpha=betAndCountCheck(gameManager.playerSplitThree,chipManager.getPlayerSplitThreeMoney(),hand4Alpha);
        if(gameManager.dealerCards.size()!=0)
            dealerCountCheck(gameManager.dealerCards);

    }

    private float betAndCountCheck(ArrayList<Card> cards, int money, float alpha) {
        Card temp = cards.get(0);
        String bet="$"+money;
        float xPos,yPos;
        float r=1,g=1,b=1;
        //Get Offsets for position
        xPos=cards.get((cards.size()-1)/2).getCurrentX();
        if(cards.size()%2==0)
            xPos+=31*cards.get(0).getScaleX()*.75f;
        yPos=cards.get(0).getCurrentY()+135*cards.get(0).getScaleY();
        if(cards.get(0).getCurrentY()==800)
            xPos=540;

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

        if(temp.getCurrentX()==temp.getNewX() && temp.getNewX()>0 && temp.getNewX()<1120) {
            alpha+=.1f;
            if(alpha>1)
                alpha=1;
        }
        else if(temp.getNewX()<0) {
            alpha-=.1f;
            if(alpha<0)
                alpha=0;
        }
        else if(cards.size()==1 && alpha<1) //only to stop it from fainting showing after splitting
            alpha=0;

        //draw highlight
        float size=((32+(bet.length()+1)*31+64+127)*temp.getScaleX()*.75f);
        if(cards.get(cards.size()-1).getRotation()==90 ) {
            String originalBet="$"+Integer.parseInt(bet.substring(1))/2;
            if(originalBet.length()!=bet.length())
                size -= 31 * temp.getScaleX()*.75f;
        }

        glGraphics.getGl().glColor4f(1,1,1,alpha);

        batcher.beginBatch(Assets.highlight);
        batcher.drawSprite(xPos - size / 2 - 4 * temp.getScaleX()*.75f, yPos, 8 * temp.getScaleX()*.75f, 70 * temp.getScaleY()*.75f, Assets.highlightLeft);
        batcher.drawSprite(xPos, yPos, size, 70 * temp.getScaleY()*.75f, Assets.highlightMiddle);
        batcher.drawSprite(xPos + size / 2 + 4 * temp.getScaleX()*.75f, yPos, 8 * temp.getScaleX()*.75f, 70 * temp.getScaleY()*.75f, Assets.highlightRight);
        batcher.endBatch();

        glGraphics.getGl().glColor4f(1,1,1,1);


        CalibriFont.drawNumbers(bet, xPos - size / 2 + 24 * temp.getScaleX()*.5625f, yPos, 1, 1, 0,alpha, temp.getScaleX()*.5625f, temp.getScaleY()*.5625f,batcher,glGraphics);
        CalibriFont.drawNumbersBackwards(""+gameManager.calcHand(cards), xPos + size / 2 - 24 * temp.getScaleX()*.5625f, yPos, r, g, b,alpha, temp.getScaleX()*.5625f, temp.getScaleY()*.5625f,batcher,glGraphics);

        return alpha;
    }

    private void dealerCountCheck(ArrayList<Card> cards) {
        if(cards.get(0).getCurrentY()==1560 && cards.get(1).getVisable()) {
            if(dealerHandAlpha<1)
                dealerHandAlpha+=.1f;
            else if(dealerHandAlpha>1)
                dealerHandAlpha=1;
        }
        else {
            if(dealerHandAlpha>0)
                dealerHandAlpha-=.1f;
            else if(dealerHandAlpha<0)
                dealerHandAlpha=0;
        }

        glGraphics.getGl().glColor4f(1,1,1,dealerHandAlpha);

        //508 to 476

        batcher.beginBatch(Assets.highlight);
        batcher.drawSprite(540 - 180 / 2 - 3, 1374, 12, 70, Assets.highlightLeft);
        batcher.drawSprite(540, 1374, 174, 70, Assets.highlightMiddle);
        batcher.drawSprite(540 + 180 / 2 + 3, 1374, 12, 70, Assets.highlightRight);
        batcher.endBatch();

        glGraphics.getGl().glColor4f(1,1,1,1);

        CalibriFont.drawNumbersCentered(""+gameManager.calcHand(gameManager.dealerCards),540,1374,1,1,1,dealerHandAlpha,.75f,.75f,batcher,glGraphics);
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

    private void renderMenuButton(int state) {
        //state 0=bet 3=menu
        if(state==3 && gameManager.menu.getRotation()!=180) {
            gameManager.menu.setRotation(gameManager.menu.getRotation()+18);
            gameManager.menu.setScaleX(gameManager.menu.getScaleX()-.1f);
            gameManager.menu.setScaleY(gameManager.menu.getScaleY()-.1f);
        }
        else if(state==0 && gameManager.menu.getRotation()!=0) {
            gameManager.menu.setRotation(gameManager.menu.getRotation()-18);
            gameManager.menu.setScaleX(gameManager.menu.getScaleX()+.1f);
            gameManager.menu.setScaleY(gameManager.menu.getScaleY()+.1f);
        }

        batcher.beginBatch(Assets.buttons);
        batcher.drawSprite(gameManager.menu.getXPos(),gameManager.menu.getYPos(),gameManager.menu.getWidth()*gameManager.menu.getScaleX(),gameManager.menu.getHeight()*gameManager.menu.getScaleY(),gameManager.menu.getTextureRegion());
        batcher.endBatch();

        batcher.beginBatch(Assets.menuBack);
        batcher.drawSprite(gameManager.menuBackground,gameManager.menuBackground.getTextureRegion());
        batcher.endBatch();

        batcher.beginBatch(Assets.menuButtons);
        batcher.drawSprite(gameManager.tableGreen,gameManager.tableGreen.getTextureRegion());
        batcher.drawSprite(gameManager.tableBlue,gameManager.tableBlue.getTextureRegion());
        batcher.drawSprite(gameManager.tableRed,gameManager.tableRed.getTextureRegion());
        batcher.drawSprite(gameManager.tablePurple,gameManager.tablePurple.getTextureRegion());
        batcher.endBatch();

        renderPressedButton(Assets.menuButtons,gameManager.menuBackArrow);

        renderPressedButton(Assets.menuButtons,gameManager.settings);
        renderPressedButton(Assets.menuButtons,gameManager.statistics);
        renderPressedButton(Assets.menuButtons,gameManager.freeChips);
        renderPressedButton(Assets.menuButtons,gameManager.earnChips);
        renderPressedButton(Assets.menuButtons,gameManager.rules);
        renderPressedButton(Assets.menuButtons,gameManager.sound);
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
        if(chipManager.getBetChips().getPressed())
            glGraphics.getGl().glColor4f(.5f,.5f,.5f,Math.min(bettingAlphas,chipManager.getBetChips().getAlpha()));
        else
            glGraphics.getGl().glColor4f(1,1,1,Math.min(bettingAlphas,chipManager.getBetChips().getAlpha()));

        batcher.beginBatch(Assets.buttons);
        batcher.drawSprite(chipManager.getBetChips(),Assets.addChips);
        batcher.endBatch();

        if(chipManager.getRemoveChips().getPressed())
            glGraphics.getGl().glColor4f(.5f,.5f,.5f,Math.min(bettingAlphas,chipManager.getRemoveChips().getAlpha()));
        else
            glGraphics.getGl().glColor4f(1,1,1,Math.min(bettingAlphas,chipManager.getRemoveChips().getAlpha()));

        batcher.beginBatch(Assets.buttons);
        batcher.drawSprite(chipManager.getRemoveChips(),Assets.removeChips);
        batcher.endBatch();
        glGraphics.getGl().glColor4f(1,1,1,bettingAlphas);

        if(chipManager.getRepeat().getPressed()) {
            glGraphics.getGl().glColor4f(.5f,.5f,.5f,bettingAlphas);
        }

        batcher.beginBatch(Assets.buttons);
        batcher.drawSprite(chipManager.getRepeat(),chipManager.getRepeat().getTextureRegion());
        batcher.endBatch();

        glGraphics.getGl().glColor4f(1,1,1,bettingAlphas);

        if(chipManager.getDoubleBetButton().getPressed()) {
            glGraphics.getGl().glColor4f(.5f,.5f,.5f,bettingAlphas);
        }

        batcher.beginBatch(Assets.buttons);
        batcher.drawSprite(chipManager.getDoubleBetButton(),chipManager.getDoubleBetButton().getTextureRegion());
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
        batcher.drawSprite(chipManager.getDealButton(),chipManager.getDealButton().getTextureRegion());
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
            batcher.drawSprite(chipManager.getPreviousArrow(), chipManager.getPreviousArrow().getRotation(), chipManager.getPreviousArrow().getTextureRegion());
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

    }
    //chip 0-6
    private void drawChips(Button b, int chip) {
        if(chip==chipManager.getChipLocation()) {
            glGraphics.getGl().glColor4f(1, 1, 1, bettingAlphas);
            batcher.beginBatch(Assets.chips);
            if(chipManager.getDirection()==1)
                batcher.drawSprite(b.getXPos(),b.getYPos(),146,146,Assets.highlightGreen);
            else
                batcher.drawSprite(b.getXPos(),b.getYPos(),146,146,Assets.highlightRed);
            batcher.endBatch();
        }
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
                batcher.drawSprite(chip.getCurrentX(), chip.getCurrentY(), 128 * chip.getScaleX(), 128 * chip.getScaleY(), chip.getTexture());
            }
            for(int i=0;i<chipManager.getSideBetRight().size() && j<1800;i++,j++) {
                Chip chip=chipManager.getSideBetRight().get(i);
                batcher.drawSprite(chip.getCurrentX(), chip.getCurrentY(), 128 * chip.getScaleX(), 128 * chip.getScaleY(), chip.getTexture());
            }
            for(int i=0;i<chipManager.getMainBet().size() && j<1800;i++,j++) {
                Chip chip=chipManager.getMainBet().get(i);
                batcher.drawSprite(chip.getCurrentX(), chip.getCurrentY(), 128 * chip.getScaleX(), 128 * chip.getScaleY(), chip.getTexture());
            }
            for(int i=0;i<chipManager.getChips().size() && j<1800;i++,j++) {
                Chip chip=chipManager.getChips().get(i);
                batcher.drawSprite(chip.getCurrentX(), chip.getCurrentY(), 128 * chip.getScaleX(), 128 * chip.getScaleY(), chip.getTexture());
            }
            batcher.endBatch();
        }
    }

    private void renderShoeMeter(int state) {


        batcher.beginBatch(Assets.shoeMeter);
        batcher.drawSprite(882,1828,314,42,Assets.meterBackground);
        batcher.endBatch();

        //optimize this to just use one stretched out sprite
        //batcher.beginBatch(Assets.shoeMeter);
        //for(int i=(726+(int)(gameManager.getPointer()*(6f/gameManager.getDecks())));i<=1038;i++) {
        //    batcher.drawSprite(i,1828,1,42,Assets.meterFilled);
        //}
        //batcher.endBatch();

        //location of where the meter will start
        int loc=(int)(gameManager.getPointer()*(6f/gameManager.getDecks()));


        batcher.beginBatch(Assets.shoeMeter);
        if(settingsManager.getCSM()==false)
            batcher.drawSprite(1039-(312-loc)/2,1828,312-loc,42,Assets.meterFilled);
        else {
            if(state!=0)
                batcher.drawSprite(1039-(312-loc)/2,1828,312-loc,42,Assets.meterFilled);
            else
                batcher.drawSprite(1039-(312)/2,1828,312,42,Assets.meterFilled);
        }
        batcher.endBatch();

        //(int)(gameManager.getDecks()*52*(settingsManager.getPenetration()/100f))-gameManager.getPointer())<=0)  meter is 312 in length
        if(settingsManager.getCSM()==false) {
            batcher.beginBatch(Assets.shoeMeter);
            batcher.drawSprite(726 + ((312 * settingsManager.getPenetration()) / 100), 1828, 3, 42, Assets.meterStop);
            batcher.endBatch();
        }

        //Makes sure the deck meter is up to date, needed because assets load after the mainscreen initialization, can probably fix later with time for optimization
        Assets.meterDecks.changeValues((gameManager.getDecks()-1)*29,41,29,40);

        batcher.beginBatch(Assets.shoeMeter);
        batcher.drawSprite(902,1882,300,40,Assets.meterText);
        batcher.drawSprite(726,1882,29,40,Assets.meterDecks);
        batcher.drawSprite(882,1828,324,54,Assets.meter);
        batcher.endBatch();
    }

    private void renderBackground(int state) {


        if(state==4 || (gameManager.getInsurance() && gameManager.getState() == 0)) {
            if(light>.2f)
                light-=.05f;
            else
                light=.2f;
        }
        else {
            if(light<1)
                light+=.05f;
            else
                light=1;
        }

        glGraphics.getGl().glColor4f(light,light,light,1);

        batcher.beginBatch(backgroundTexture);
        //batcher.drawSprite(540, 960, 1080, 1920, settingsManager.getBackgroundTexture());
        batcher.drawSprite(540, 960, 1080, 1920,backgroundTextureRegion);
        batcher.endBatch();

        batcher.beginBatch(Assets.tableSettings);
        batcher.drawSprite(539, 1279, 817, 54, Assets.blackjackPayout);
        batcher.drawSprite(540,1156,1080,174,Assets.dealerSetting);
        batcher.endBatch();

        glGraphics.getGl().glColor4f(1,1,1,1);

        batcher.beginBatch(Assets.bet);
        batcher.drawSprite(540, 487, 281,281,Assets.mainBet);
        batcher.endBatch();


        if(state==2) {
            gameManager.getOldSideBetLeft().drawPayouts(batcher, glGraphics, gameManager.getOldSideBetLeft().getPayout(),gameManager.getOldSideBetLeft().getVersion(),gameManager.getOldSideBetLeft().getPosition());
        }
        else
            gameManager.getSideBetLeft().drawPayouts(batcher,glGraphics,gameManager.getSideBetLeft().getPayout(),gameManager.getSideBetLeft().getVersion(),gameManager.getSideBetLeft().getPosition());


        if(state==2) {
            gameManager.getOldSideBetRight().drawPayouts(batcher, glGraphics, gameManager.getOldSideBetRight().getPayout(),gameManager.getOldSideBetRight().getVersion(),gameManager.getOldSideBetRight().getPosition());
        }
        else
            gameManager.getSideBetRight().drawPayouts(batcher,glGraphics,gameManager.getSideBetRight().getPayout(),gameManager.getSideBetRight().getVersion(),gameManager.getSideBetRight().getPosition());


        glGraphics.getGl().glColor4f(1,1,1,1);
    }

    private void renderEdge() {
        batcher.beginBatch(Assets.edgeBackground);
        batcher.drawSprite(540,100,1080,200,Assets.edge);
        batcher.endBatch();
    }

    public void changeBackground(Texture t, TextureRegion tr) {
        backgroundTexture=t;
        backgroundTextureRegion=tr;
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


    public void loadData(SpriteBatcher batcher, GLGraphics glGraphics, GameManager gameManager, ChipManager chipManager, SlotMachine slotMachine, SettingsManager settingsManager, Camera2D cam) {

        this.batcher=batcher;
        this.glGraphics=glGraphics;
        this.gameManager=gameManager;
        this.chipManager=chipManager;
        this.slotMachine=slotMachine;
        this.settingsManager=settingsManager;
        this.cam=cam;
    }


}

