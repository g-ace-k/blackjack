package com.mikegacek.blackjack;

import com.mikegacek.blackjack.framework.gl.SpriteBatcher;
import com.mikegacek.blackjack.framework.impl.GLGraphics;
import com.mikegacek.blackjack.framework.math.Circle;

import java.io.Serializable;


/**
 * Created by Michael on 2/8/2017.
 */

public class SlotMachine implements Serializable {

    private static final long serialVersionUID = 16L;

    private transient GLGraphics glGraphics;
    private transient SpriteBatcher batcher;
    private transient ChipManager chipManager;

    private float oldNumbersYLoc,numbersYLoc;
    private float spinSpeed;
    private float arrowRotation,newArrowRotation;
    private float arrowRotationDirection;
    private float arrowRotationSpeed;
    private boolean buttonPressed;
    private int hold,slotArrowWaitTime;
    private float slotArmHeight;
    private Circle slotMachineHandle;
    private boolean touched;

    public SlotMachine(GLGraphics glGraphics, SpriteBatcher batcher, ChipManager chipManager) {
        this.glGraphics=glGraphics;
        this.batcher=batcher;
        this.chipManager=chipManager;
        oldNumbersYLoc=32;
        numbersYLoc=32;
        arrowRotation=0;
        newArrowRotation=20;
        arrowRotationDirection=1;
        spinSpeed=2;
        buttonPressed=false;
        hold=0;
        slotArmHeight=288;
        slotArrowWaitTime=0;
        touched=false;
        slotMachineHandle=new Circle(455,600+slotArmHeight,56);
    }

    public Circle getSlotMachineHandle() {return slotMachineHandle;}

    public void setTouched(boolean t) {touched=t;}

    public float getSpinSpeed() { return spinSpeed;}

    public void setSpinSpeed(float speed) { spinSpeed=speed;}

    public void setButtonPressed(boolean b) { buttonPressed=b;}

    public void drawSlotMachine() {

        drawArrows();

        glGraphics.getGl().glEnable(glGraphics.getGl().GL_BLEND);
        glGraphics.getGl().glBlendFunc( glGraphics.getGl().GL_SRC_COLOR,glGraphics.getGl().GL_DST_ALPHA);

        batcher.beginBatch(Assets.slotMachine);
        batcher.drawSprite(906,1200,92,116,Assets.slotArmConnection);
        batcher.drawSprite(930,1200+slotArmHeight/2+5*(slotArmHeight/288),26,286*(slotArmHeight/288f),Assets.slotArm);
        batcher.drawSprite(540,1200,670,480,Assets.slot);
        batcher.drawSprite(930,1200+slotArmHeight,112,112,Assets.slotHandle);
        batcher.endBatch();

        updateHandle();

        drawNumbers();
        drawSlotArrows();

        glGraphics.getGl().glDisable(glGraphics.getGl().GL_LIGHTING);
    }

    public void drawNumbers() {
        oldNumbersYLoc=numbersYLoc;
        numbersYLoc-=spinSpeed;
        if(numbersYLoc<=0)
            numbersYLoc+=1408;
        if(spinSpeed>.4f) {
            if(buttonPressed) {
                if(hold!=0)
                    hold--;
                else
                    spinSpeed -= spinSpeed / 50;
            }
            else {
                spinSpeed = 2f;
            }

        }
        else if(buttonPressed && spinSpeed!=0){
            if(((int)numbersYLoc-32+56)%64==0) {
                spinSpeed = 0;
                payout();
            }
        }
        glGraphics.getGl().glBlendFunc( glGraphics.getGl().GL_DST_COLOR,glGraphics.getGl().GL_ONE_MINUS_SRC_ALPHA);
        Assets.slotNumbers.changeValues(0,numbersYLoc,225,112);
        batcher.beginBatch(Assets.slotMachineNumbers);
        batcher.drawSprite(540,1200,450,224,Assets.slotNumbers);
        batcher.endBatch();
        glGraphics.getGl().glBlendFunc(glGraphics.getGl().GL_SRC_ALPHA, glGraphics.getGl().GL_ONE_MINUS_SRC_ALPHA);
    }

    public void drawSlotArrows() {

        if(((int)oldNumbersYLoc-32+56)%64<((int)numbersYLoc-32+56)%64) {
            Assets.slotMachineSpinner.play(MainScreen.sound);
            newArrowRotation=spinSpeed*10;
            if(newArrowRotation>20)
                newArrowRotation=20;
            arrowRotationDirection=1;
            arrowRotationSpeed=spinSpeed;
            if(spinSpeed>10)
                arrowRotationSpeed=10;

        }
        if(arrowRotation>=newArrowRotation) {
            if(arrowRotationDirection==-1)
                arrowRotation-=arrowRotationSpeed;
            else {
                if(newArrowRotation==0) {
                    arrowRotation = 0;
                    arrowRotationSpeed=0;
                }
                newArrowRotation=(int)-newArrowRotation/2;
                arrowRotationDirection=-1;
                arrowRotationSpeed/=2;
            }
        }
        else if(arrowRotation<=newArrowRotation) {
            if(arrowRotationDirection==1)
                arrowRotation+=arrowRotationSpeed;
            else {
                if(newArrowRotation==0) {
                    arrowRotation = 0;
                    arrowRotationSpeed=0;
                }
                newArrowRotation=(int)-newArrowRotation/2;
                arrowRotationDirection=1;
                arrowRotationSpeed/=2;
            }
        }

        batcher.beginBatch(Assets.slotMachine);
        batcher.drawSprite(240,1200,140,82,-arrowRotation,Assets.rightArrow);
        batcher.drawSprite(840,1200,140,82,arrowRotation,Assets.leftArrow);
        batcher.endBatch();
    }

    public void drawArrows() {
        if(buttonPressed==false)
            slotArrowWaitTime++;
        if(slotArrowWaitTime>200)
            slotArrowWaitTime=0;


        glGraphics.getGl().glColor4f(1,1,1,1);
        if(slotArrowWaitTime<40)
            glGraphics.getGl().glColor4f(.5f,.5f,.5f,1);
        batcher.beginBatch(Assets.slotMachine);
        batcher.drawSprite(1000,1300,72,44,Assets.arrowEnd);
        batcher.endBatch();

        if(slotArrowWaitTime<60)
            glGraphics.getGl().glColor4f(.5f,.5f,.5f,1);

        batcher.beginBatch(Assets.slotMachine);
        batcher.drawSprite(1000,1244,72,44,Assets.arrowEnd);
        batcher.endBatch();

        if(slotArrowWaitTime<80)
            glGraphics.getGl().glColor4f(.5f,.5f,.5f,1);

        batcher.beginBatch(Assets.slotMachine);
        batcher.drawSprite(1000,1188,72,44,Assets.arrowEnd);
        batcher.endBatch();

        if(slotArrowWaitTime<100)
            glGraphics.getGl().glColor4f(.5f,.5f,.5f,1);

        batcher.beginBatch(Assets.slotMachine);
        batcher.drawSprite(1000,1080,128,142,Assets.arrow);
        batcher.endBatch();

        glGraphics.getGl().glColor4f(1,1,1,1);

    }

    public void pressButton() {
        buttonPressed=true;
        slotArrowWaitTime=0;
        spinSpeed=(float)Math.random()*10f+15f;
        hold=(int)Math.random()*121+120;
    }

    public void setSlotArmHeight(float height) {
        if(touched==true) {
            if (height < 912) {
                height = 912;
                touched = false;
            } else if (height > 1488)
                height = 1488;
            else
                slotArmHeight = height - 1200;
            slotMachineHandle.setCirclePos(910, height);
        }
    }

    public void updateHandle() {
        if((slotArmHeight<288 && touched==false)) {
            slotArmHeight+=40;
            if(slotArmHeight>288)
                slotArmHeight=288;
            slotMachineHandle.setCirclePos(910,slotArmHeight+1200);
        }
        if(((slotArmHeight<0 && touched==false) || slotArmHeight<=-288) && buttonPressed==false)
            pressButton();
    }

    public void reset() {
        spinSpeed=2;
        buttonPressed=false;
        hold=0;
        slotArmHeight=144;
        slotMachineHandle.setCirclePos(455,slotArmHeight+600);
        slotArrowWaitTime=0;
        touched=false;
        numbersYLoc-=1;
        oldNumbersYLoc-=1;
    }

    public void payout() {
        Assets.largeStackOfChips.play(MainScreen.sound);
        switch(((int)numbersYLoc-32+56)/64) {
            case 22: //this case will always be the last one in the list, but in the texture it is shown at first. This is because when Yloc hits 0 it gets turned into 1408 before running this method
                chipManager.payout(chipManager.getMainBet(),chipManager.getMainBetChips(),4,100);
                break;
            case 1:
                chipManager.payout(chipManager.getMainBet(),chipManager.getMainBetChips(),4,5000);
                break;
            case 2:
                chipManager.payout(chipManager.getMainBet(),chipManager.getMainBetChips(),4,25);
                break;
            case 3:
                chipManager.payout(chipManager.getMainBet(),chipManager.getMainBetChips(),4,400);
                break;
            case 4:
                chipManager.payout(chipManager.getMainBet(),chipManager.getMainBetChips(),4,800);
                break;
            case 5:
                chipManager.payout(chipManager.getMainBet(),chipManager.getMainBetChips(),4,150);
                break;
            case 6:
                chipManager.payout(chipManager.getMainBet(),chipManager.getMainBetChips(),4,75);
                break;
            case 7:
                chipManager.payout(chipManager.getMainBet(),chipManager.getMainBetChips(),4,1000);
                break;
            case 8:
                chipManager.payout(chipManager.getMainBet(),chipManager.getMainBetChips(),4,350);
                break;
            case 9:
                chipManager.payout(chipManager.getMainBet(),chipManager.getMainBetChips(),4,750);
                break;
            case 10:
                chipManager.payout(chipManager.getMainBet(),chipManager.getMainBetChips(),4,500);
                break;
            case 11:
                chipManager.payout(chipManager.getMainBet(),chipManager.getMainBetChips(),4,200);
                break;
            case 12:
                chipManager.payout(chipManager.getMainBet(),chipManager.getMainBetChips(),4,900);
                break;
            case 13:
                chipManager.payout(chipManager.getMainBet(),chipManager.getMainBetChips(),4,50);
                break;
            case 14:
                chipManager.payout(chipManager.getMainBet(),chipManager.getMainBetChips(),4,650);
                break;
            case 15:
                chipManager.payout(chipManager.getMainBet(),chipManager.getMainBetChips(),4,2000);
                break;
            case 16:
                chipManager.payout(chipManager.getMainBet(),chipManager.getMainBetChips(),4,550);
                break;
            case 17:
                chipManager.payout(chipManager.getMainBet(),chipManager.getMainBetChips(),4,700);
                break;
            case 18:
                chipManager.payout(chipManager.getMainBet(),chipManager.getMainBetChips(),4,300);
                break;
            case 19:
                chipManager.payout(chipManager.getMainBet(),chipManager.getMainBetChips(),4,250);
                break;
            case 20:
                chipManager.payout(chipManager.getMainBet(),chipManager.getMainBetChips(),4,600);
                break;
            case 21:
                chipManager.payout(chipManager.getMainBet(),chipManager.getMainBetChips(),4,450);
                break;
            default:
                chipManager.payout(chipManager.getMainBet(),chipManager.getMainBetChips(),4,9999);
                break;

        }
    }

    public void loadData(GLGraphics glGraphics, SpriteBatcher batcher, ChipManager chipManager) {
        this.glGraphics=glGraphics;
        this.batcher=batcher;
        this.chipManager=chipManager;
    }

}
