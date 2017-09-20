package com.mikegacek.blackjack;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Michael on 11/28/2016.
 */
public class GameManager  implements Serializable{

    private static final long serialVersionUID = 14L;

    private ArrayList<Card> cards;
    public ArrayList<Card> shuffling;
    public ArrayList<Card> playerCards;
    public ArrayList<Card> playerSplitOne;
    public ArrayList<Card> playerSplitTwo;
    public ArrayList<Card> playerSplitThree;
    public int cardsPos,cardsSplitOnePos,cardsSplitTwoPos,cardsSplitThreePos;
    public ArrayList<Card> dealerCards;
    public Button hitButton,standButton,doubleButton,splitButton,surrenderButton,yes,no,menu,settings,rules,freeChips,sound,hintButton,playButton;
    //state 0=check dealer for blackjack,1=player main hand, 2=player split one, 3=player split 2, 4=player split 3, 5=dealer, 6=after dealer done drawing, 7=player surrender, 8=everything finished
    public int state,hands,waitTime=30;
    public boolean playerBJ,leftFlag;
    private boolean shuffleInProgress=false;
    private int shufflingWaitTime=0;
    private transient ChipManager chipManager;
    private transient Settings settingsManager;
    private int pointer;
    private int decks;
    private SideBets sideBetLeft,sideBetRight;
    private boolean insurance;
    private boolean surrender;

    private boolean playerBust;

    public GameManager(int decks, ChipManager chipManager,Settings settingsManager) {
        this.chipManager=chipManager;
        this.settingsManager=settingsManager;
        this.decks=this.settingsManager.getDecks();
        cards = new ArrayList<>(52*decks);
        shuffling = new ArrayList<>(26);
        playerCards = new ArrayList<>(15);
        playerSplitOne = new ArrayList<>(15);
        playerSplitTwo = new ArrayList<>(15);
        playerSplitThree = new ArrayList<>(15);
        cardsPos=1;
        cardsSplitOnePos=2;
        cardsSplitTwoPos=3;
        cardsSplitThreePos=4; //894,77 186,77
        dealerCards = new ArrayList<>(15);
        hitButton = new Button(363,109,186,-100,Assets.hitButton);
        standButton = new Button(363,109,894,-100,Assets.standButton);
        doubleButton = new Button(363,145,186,-100,Assets.doubleButton);
        splitButton = new Button(363,145,894,-100,Assets.splitButton);
        surrenderButton = new Button(330,145,540,-100,Assets.surrenderButton);
        hintButton= new Button(99,99,60,1840,Assets.hint);
        playButton= new Button(99,99,60,1720,Assets.play);
        settings= new Button(286,78,-500,700,Assets.settingsButton);
        rules = new Button(286,78,-400,615,Assets.rulesButton);
        freeChips=new Button(286,78,-300,530,Assets.freeChipsButton);
        sound= new Button(286,78,-200,445,Assets.soundOnButton);
        yes=new Button(150,82,326,1093,Assets.yes);
        no=new Button(133,82,760,1093,Assets.no);
        menu = new Button(99,99,60,1840,Assets.menuButton);
        int card=0;
        playerBust=false;
        playerBJ=false;
        insurance=false;
        surrender=false;
        state=6;
        hands=1;
        leftFlag=false;
        pointer=0;
        for(int i=0;i<52*decks;i++,card++) {
            cards.add(i,new Card(card%52+1));
            if(i<26) {
                shuffling.add(i, new Card(card % 52 + 1));
                shuffling.get(i).setVisable(false);
                shuffling.get(i).setCurrentPos(-120, 1880);
            }
        }

        if(decks>1)
            sideBetLeft= new PerfectPairs(1);
        else
            sideBetLeft=null;

        if(decks>2)
            sideBetRight= new TwentyOnePlusThree(2);
        else
            sideBetRight = new TwentyOnePlusThree(1);

    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public int getDecks() { return decks;}

    public boolean getPlayerBust() {
        return playerBust;
    }

    public void setPlayerBust(boolean p) {
        playerBust=p;
    }

    public int getState() {
        return state;
    }

    public void setState(int s) {
        state=s;
    }

    public boolean getShuffleInProgress() {
        return shuffleInProgress;
    }

    public int getHands() {
        return hands;
    }

    public void setHands(int h) {
        hands=h;
    }

    public void setLeftFlag(boolean b) {
        leftFlag=b;
    }

    public boolean getInsurance() { return insurance;}

    public boolean getSurrender() {return surrender;}

    public SideBets getSideBetRight() {
        return sideBetRight;
    }

    public void setSideBetRight(SideBets sideBetRight) {
        this.sideBetRight = sideBetRight;
    }

    public SideBets getSideBetLeft() {
        return sideBetLeft;
    }

    public void setSideBetLeft(SideBets sideBetLeft) {
        this.sideBetLeft = sideBetLeft;
    }

    public int getPointer() { return pointer;}

    public void addCard() {
        int count=1;
        int size=playerCards.size();
        int xPos;
        Assets.dealCard.play(MainScreen.sound);
        switch(state) {
            case 2:
                addPlayerSplitOne();
                for(Card card: playerSplitOne) {
                    xPos=-32*size+572;
                    card.setNewPos(xPos+(count-1)*64,800);
                    count++;
                }
                break;
            case 3:
                addPlayerSplitTwo();
                for(Card card: playerSplitTwo) {
                    xPos=-32*size+572;
                    card.setNewPos(xPos+(count-1)*64,800);
                    count++;
                }
                break;
            case 4:
                addPlayerSplitThree();
                for(Card card: playerSplitThree) {
                    xPos=-32*size+572;
                    card.setNewPos(xPos+(count-1)*64,800);
                    count++;
                }
                break;
            default:
                addPlayerCard();
                for(Card card: playerCards) {
                    xPos=-32*size+572;
                    card.setNewPos(xPos+(count-1)*64,800);
                    count++;
                }
                break;
        }
        if(playerCards.size()>2)
            surrenderButton.setNewPos(540,-100);
    }

    public void addPlayerCard() {
        playerCards.add(cards.get(pointer));
        pointer++;
        checkEndOfDeck();
        if(calcHand(playerCards)>21 && playerSplitOne.size()==0)
            setPlayerBust(true);
    }

    public void addPlayerSplitOne() {
        playerSplitOne.add(cards.get(pointer));
        pointer++;
        checkEndOfDeck();
        if(calcHand(playerSplitOne)>21 && calcHand(playerCards)>21 && playerSplitTwo.size()==0)
            setPlayerBust(true);

    }

    public void addPlayerSplitTwo() {
        playerSplitTwo.add(cards.get(pointer));
        pointer++;
        checkEndOfDeck();
        if(calcHand(playerSplitTwo)>21 && calcHand(playerSplitOne)>21 && calcHand(playerCards)>21 && playerSplitThree.size()==0)
            setPlayerBust(true);
    }

    public void addPlayerSplitThree() {
        playerSplitThree.add(cards.get(pointer));
        pointer++;
        checkEndOfDeck();
        if(calcHand(playerSplitTwo)>21 && calcHand(playerSplitOne)>21 && calcHand(playerCards)>21 && calcHand(playerSplitThree)>21)
            setPlayerBust(true);
    }

    public void addDealerCards() {
        dealerCards.add(cards.get(pointer));
        pointer++;
        Assets.dealCard.play(MainScreen.sound);
        checkEndOfDeck();
        if(dealerCards.size()==2)
            dealerCards.get(1).setVisable(false);
        int count=1;
        int size=dealerCards.size();
        int xPos;

        for(Card card:dealerCards) {
            xPos=-32*size+572;
            card.setNewPos(xPos + (count - 1) * 64, 1560);
            count++;
        }

    }

    public void updateShuffling() {

        if(shuffleInProgress) {
            shufflingWaitTime++;
            for(int i=0;i<shuffling.size();i++) {
                if(shufflingWaitTime>i*2) {
                    shuffling.get(i).move(shuffling.get(i).getSpeed());
                    if(shuffling.get(i).getSpeed()>.15f)
                        shuffling.get(i).setSpeed(shuffling.get(i).getSpeed()-.012f);
                }
                else {
                    i=shuffling.size()-1;
                }
                if(shuffling.get(i).getCurrentX()==shuffling.get(i).getNewX()) {
                    //If last card is in spot
                    if(i==shuffling.size()-1) {
                        resetShuffling();
                    }
                }

            }
        }
    }

    public void resetShuffling() {
        for(int i=0;i<shuffling.size();i++) {
            shuffling.get(i).setCurrentPos(-120,1880);
            shuffling.get(i).setSpeed(.5f);
        }
        shuffleInProgress=false;
        shufflingWaitTime=0;
    }

    public void updatePlayerCards() {
        leftFlag=false;
        moveSplitCards(playerCards,cardsPos);
        moveSplitCards(playerSplitOne,cardsSplitOnePos);
        moveSplitCards(playerSplitTwo,cardsSplitTwoPos);
        moveSplitCards(playerSplitThree,cardsSplitThreePos);

        for(Card card:playerCards) {
            card.moveAndScale(.75f);
        }
        for(Card card:playerSplitOne) {
            card.moveAndScale(.75f);
        }
        for(Card card:playerSplitTwo) {
            card.moveAndScale(.75f);
        }
        for(Card card:playerSplitThree) {
            card.moveAndScale(.75f);
        }
        updateShuffling();
    }

    public void updateLastHandDealerCards() {
        int count=1;
        int size=dealerCards.size();
        int xPos;

        for(Card card:dealerCards) {
            xPos=-32*size+572;
            card.setNewPos(xPos + (count - 1) * 64, 1560);
            count++;
        }
    }

    public void updateFinishedPlayer() {
        for(Card card:playerCards) {
            card.move(.75f);
        }
        for(Card card:playerSplitOne) {
            card.move(.75f);
        }
        for(Card card:playerSplitTwo) {
            card.move(.75f);
        }
        for(Card card:playerSplitThree) {
            card.move(.75f);
        }
        updateShuffling();
    }


    public void updateDealerCards() {
        for(Card card:dealerCards) {
            card.move(.75f);
        }
    }

    public void updateHitButton() {
        hitButton.move(.5f);
    }

    public void updateStandButton() {
        standButton.move(.5f);
    }

    public void updateDoubleButton() {
        doubleButton.move(.5f);
    }

    public void updateSplitButton() {
        splitButton.move(.5f);
    }

    public void updateSurrenderButton() { surrenderButton.move(.5f);}

    public void updateSettingsButton() {settings.move(.5f);}

    public void updateRulesButton() { rules.move(.5f);}

    public void updateFreeChipsButton() { freeChips.move(.5f);}

    public void updateSoundButton() { sound.move(.5f);}

    public void updateGame() {

        switch(state) {
            //checks for dealer blackjack, if ace is showing ask for insurance
            case 0:
                if(dealerCards.get(0).getValue()==1 && chipManager.getMoney()>chipManager.getMainBetMoney()/2 && chipManager.getMainBetMoney()>1 && settingsManager.getInsurance()==true) {
                    // ask for insurance
                    insurance=true;
                }
                else if(Math.abs(calcHand(dealerCards))==21) {
                    state=5;
                    settingsManager.setDealerBlackjacks(settingsManager.getDealerBlackjacks()+1);
                    if(Math.abs(calcHand(playerCards))==21 && playerCards.size()==2 && playerSplitOne.size()==0) {
                        settingsManager.setPlayerBlackjacks(settingsManager.getPlayerBlackjacks()+1);
                    }
                    insurance=false;
                    surrenderButton.setNewPos(540,-100);
                }
                else {
                    state=1;
                    insurance=false;
                    hitButton.setNewPos(186,77);
                    standButton.setNewPos(894,77);
                    if(settingsManager.getSurrender()==true)
                        surrenderButton.setNewPos(540,150);
                }
                break;
            //checks for if player hits 21 or is over
            case 1:
                if(Math.abs(calcHand(playerCards))>=21) {
                    if(Math.abs(calcHand(playerCards))==21 && playerCards.size()==2 && playerSplitOne.size()==0) {
                        playerBJ = true;
                        settingsManager.setPlayerBlackjacks(settingsManager.getPlayerBlackjacks()+1);
                    }
                    changeHand();
                }
                else if(playerCards.size()==2 && chipManager.getPlayerHandMoney()<=chipManager.getMoney()) {
                    checkDoubSplit(playerCards);
                    if(hitButton.getNewY()==-100 && doubleButton.getNewY()==-100 && splitButton.getNewY()==-100)
                        changeHand();
                }
                else if(playerCards.size()==1 ) {
                    addCard();
                }
                else {
                    doubleButton.setNewPos(110,-100);
                    splitButton.setNewPos(894,-100);
                    if(hitButton.getNewY()==-100)
                        changeHand();
                }
                break;
            case 2:
                if(Math.abs(calcHand(playerSplitOne))>=21 ) {
                    changeHand();
                }
                else if(playerSplitOne.size()==2 && chipManager.getPlayerSplitOneMoney()<=chipManager.getMoney()) {
                    checkDoubSplit(playerSplitOne);
                    if(hitButton.getNewY()==-100 && doubleButton.getNewY()==-100 && splitButton.getNewY()==-100)
                        changeHand();
                }
                else if(playerSplitOne.size()==1 ) {
                    addCard();
                }
                else {
                    doubleButton.setNewPos(186,-100);
                    splitButton.setNewPos(894,-100);
                    if(hitButton.getNewY()==-100)
                        changeHand();
                }
                break;
            case 3:
                if(Math.abs(calcHand(playerSplitTwo))>=21) {
                    changeHand();
                }
                else if(playerSplitTwo.size()==2 && chipManager.getPlayerSplitTwoMoney()<=chipManager.getMoney() ) {
                    checkDoubSplit(playerSplitTwo);
                    if(hitButton.getNewY()==-100)
                        changeHand();
                }
                else if(playerSplitTwo.size()==1 ) {
                    addCard();
                }
                else {
                    doubleButton.setNewPos(186,-100);
                    splitButton.setNewPos(894,-100);
                    if(hitButton.getNewY()==-100)
                        changeHand();
                }
                break;
            case 4:
                if(Math.abs(calcHand(playerSplitThree))>=21) {
                    state=5;
                }
                else if(playerSplitThree.size()==2 && chipManager.getPlayerSplitThreeMoney()<=chipManager.getMoney()) {
                    checkDoubSplit(playerSplitThree);
                    if(hitButton.getNewY()==-100 && doubleButton.getNewY()==-100 && splitButton.getNewY()==-100)
                        changeHand();
                }
                else if(playerSplitThree.size()==1 ) {
                    addCard();
                }
                else {
                    doubleButton.setNewPos(186,-100);
                    splitButton.setNewPos(894,-100);
                    if(hitButton.getNewY()==-100 && doubleButton.getNewY()==-100 && splitButton.getNewY()==-100)
                        changeHand();
                }
                break;
            case 5:
            case 6:
            case 7:
            case 8:
                hitButton.setNewPos(186,-100);
                standButton.setNewPos(894,-100);
                doubleButton.setNewPos(186,-100);
                splitButton.setNewPos(894,-100);
                moveDealerDownCard();
                if(dealerCards.get(1).getVisable()==true) {
                    if(Math.abs(calcHand(dealerCards))<17 && getPlayerBust()==false && playerBJ==false && settingsManager.getDealer()==1) {
                        if (waitTime == 30) {
                            addDealerCards();
                            checkEndOfDeck();
                            waitTime = 0;
                        } else
                            waitTime++;
                    }
                    else if((Math.abs(calcHand(dealerCards))<17 || calcHand(dealerCards)==-17) && getPlayerBust()==false && playerBJ==false && settingsManager.getDealer()==-1) {
                        if (waitTime == 30) {
                            addDealerCards();
                            checkEndOfDeck();
                            waitTime = 0;
                        } else
                            waitTime++;
                    }
                    else {
                        state=6;
                    }
                }
                break;
        }
    }

    public void changeHand() {
        state++;
        surrenderButton.setNewPos(540,-100);
        if(state==2 && playerSplitOne.size()==0)
            state=5;
        else if(state==3 && playerSplitTwo.size()==0)
            state=5;
        else if(state==4 && playerSplitThree.size()==0)
            state=5;
        if(state!=5) {
            if(moveSplitCards(playerCards,--cardsPos))
                cardsPos=4;
            if(moveSplitCards(playerSplitOne,--cardsSplitOnePos))
                cardsSplitOnePos=4;
            if(moveSplitCards(playerSplitTwo,--cardsSplitTwoPos))
                cardsSplitTwoPos=4;
            if(moveSplitCards(playerSplitThree,--cardsSplitThreePos))
                cardsSplitThreePos=4;
        }
    }

    public void checkDoubSplit(ArrayList<Card> list) {
        int tempCard1,tempCard2;

        tempCard1=list.get(0).getValue();
        tempCard2=list.get(1).getValue();

        if(tempCard1>10)
            tempCard1=10;

        if(tempCard2>10)
            tempCard2=10;

        //1=any 2 2=9-11 only 3=10-11 only
        //Checks the double down rule
        if(settingsManager.getDas()==false && hands>1) //double after split rule
            doubleButton.setNewPos(186,-100);
        else if(settingsManager.getDoubleAces()==false && hands>1 && tempCard1==1) //double aces rule
            doubleButton.setNewPos(186,-100);
        else if(settingsManager.getDoubleDown()==2 && tempCard1+tempCard2>=9 && tempCard1+tempCard2<=11 && tempCard1!=1 && tempCard2!=1) //9-11 rule
            doubleButton.setNewPos(186,170);
        else if(settingsManager.getDoubleDown()==3 && tempCard1+tempCard2>=10 && tempCard1+tempCard2<=11 && tempCard1!=1 && tempCard2!=1) //10-11 rule
            doubleButton.setNewPos(186,170);
        else if(settingsManager.getDoubleDown()==1) //any 2 rule
            doubleButton.setNewPos(186,170);
        else
            doubleButton.setNewPos(186,-100); //no allowable double down found



        //Checks for maximum split hands
        if((tempCard1==tempCard2 && hands!=settingsManager.getSplitHands())) {
            if(settingsManager.getResplit()==false && tempCard1==1 && hands>1) //resplit aces rule
                splitButton.setNewPos(894,-100);
            else
                splitButton.setNewPos(894,170);
        }
        else
            splitButton.setNewPos(894,-100);
    }

    public void addDouble() {
        surrenderButton.setNewPos(540,-100);
        settingsManager.setDoubleDownTotal(settingsManager.getDoubleDownTotal()+1);
        switch (state) {
            case 1:
                if(playerCards.size()==2) {
                    addPlayerCard();
                    playerCards.get(playerCards.size()-1).setRotation(90);
                    chipManager.doubleBet(chipManager.getPlayerHandMoney());
                    chipManager.setPlayerHandMoney(chipManager.getPlayerHandMoney()*2);
                }
                break;
            case 2:
                if(playerSplitOne.size()==2) {
                    addPlayerSplitOne();
                    playerSplitOne.get(playerSplitOne.size()-1).setRotation(90);
                    chipManager.doubleBet(chipManager.getPlayerSplitOneMoney());
                    chipManager.setPlayerSplitOneMoney(chipManager.getPlayerSplitOneMoney() * 2);
                }
                break;
            case 3:
                if(playerSplitTwo.size()==2) {
                    addPlayerSplitTwo();
                    playerSplitTwo.get(playerSplitTwo.size()-1).setRotation(90);
                    chipManager.doubleBet(chipManager.getPlayerSplitTwoMoney());
                    chipManager.setPlayerSplitTwoMoney(chipManager.getPlayerSplitTwoMoney() * 2);
                }
                break;
            case 4:
                if(playerSplitThree.size()==2) {
                    addPlayerSplitThree();
                    playerSplitThree.get(playerSplitThree.size() - 1).setRotation(90);
                    chipManager.doubleBet(chipManager.getPlayerSplitThreeMoney());
                    chipManager.setPlayerSplitThreeMoney(chipManager.getPlayerSplitThreeMoney() * 2);
                }
                break;
        }

    }

    public void addSplit() {
        surrenderButton.setNewPos(540,-100);
        int tempCard1,tempCard2;
        settingsManager.setSplitsTotal(settingsManager.getSplitsTotal()+1);
        switch (state) {
            case 1:
                if(playerCards.size()==2 && hands<settingsManager.getSplitHands()) {
                    hands++;
                    tempCard1=playerCards.get(0).getValue();
                    tempCard2=playerCards.get(1).getValue();
                    if(tempCard1>10)
                        tempCard1=10;
                    if(tempCard2>10)
                        tempCard2=10;
                    if(tempCard1==1 && settingsManager.getHitAces()==false)
                        hitButton.setNewPos(186,-100);
                    if(tempCard1==tempCard2) {
                        chipManager.doubleBet(chipManager.getPlayerHandMoney());
                        switch (hands) {
                            case 2:
                                playerSplitOne.add(playerCards.get(1));
                                playerCards.remove(1);
                                addCard();
                                chipManager.setPlayerSplitOneMoney(chipManager.getPlayerHandMoney());
                                break;
                            case 3:
                                playerSplitTwo.add(playerCards.get(1));
                                playerCards.remove(1);
                                addCard();
                                chipManager.setPlayerSplitTwoMoney(chipManager.getPlayerHandMoney());
                                break;
                            case 4:
                                playerSplitThree.add(playerCards.get(1));
                                playerCards.remove(1);
                                addCard();
                                chipManager.setPlayerSplitThreeMoney(chipManager.getPlayerHandMoney());
                                break;
                        }
                    }
                }
                break;
            case 2:
                if(playerSplitOne.size()==2 && hands<settingsManager.getSplitHands()) {
                    hands++;
                    tempCard1=playerSplitOne.get(0).getValue();
                    tempCard2=playerSplitOne.get(1).getValue();
                    if(tempCard1>10)
                        tempCard1=10;
                    if(tempCard2>10)
                        tempCard2=10;
                    if(tempCard1==tempCard2) {
                        chipManager.doubleBet(chipManager.getPlayerSplitOneMoney());
                        switch (hands) {
                            case 3:
                                playerSplitTwo.add(playerSplitOne.get(1));
                                playerSplitOne.remove(1);
                                addCard();
                                chipManager.setPlayerSplitTwoMoney(chipManager.getPlayerSplitOneMoney());
                                break;
                            case 4:
                                playerSplitThree.add(playerSplitOne.get(1));
                                playerSplitOne.remove(1);
                                addCard();
                                chipManager.setPlayerSplitThreeMoney(chipManager.getPlayerSplitOneMoney());
                                break;
                        }
                    }
                }
                break;
            case 3:
                if(playerSplitTwo.size()==2 && hands<settingsManager.getSplitHands()) {
                    hands++;
                    tempCard1=playerSplitTwo.get(0).getValue();
                    tempCard2=playerSplitTwo.get(1).getValue();
                    if(tempCard1>10)
                        tempCard1=10;
                    if(tempCard2>10)
                        tempCard2=10;
                    if(tempCard1==tempCard2) {
                        chipManager.doubleBet(chipManager.getPlayerSplitTwoMoney());
                        playerSplitThree.add(playerSplitTwo.get(1));
                        playerSplitTwo.remove(1);
                        addCard();
                        chipManager.setPlayerSplitThreeMoney(chipManager.getPlayerSplitTwoMoney());
                    }
                }
                break;
        }
    }

    public int calcHand(ArrayList<Card> list) {
        int value,total=0;
        int largeAces=0;
        for(Card card:list) {
            value=card.getValue();

            if(value>10) {
                total+=10;
            }
            else if(value==1) {
                total+=11;
                largeAces++;
            }
            else
                total+=value;

            if(total>21 && largeAces>0) {
                total-=10;
                largeAces--;
            }
        }

        //Return as Negative Number for purposes of showing a x/x value of hand
        if(largeAces>0)
            return -total;
        else
            return total;
    }

    public void moveDealerDownCard() {

        if(dealerCards.get(1).getVisable()==false)
            dealerCards.get(1).setNewPos(720,1560);
        else if(dealerCards.get(1).getNewX()==720 || dealerCards.get(1).getNewX()==572)
            dealerCards.get(1).setNewPos(572,1560);
        dealerCards.get(1).flipCard();
    }

    public boolean moveSplitCards(ArrayList<Card> list, int position) {
        //Position 1 main hand
        //Position 2 left
        //Position 3 middle
        //Position 4 right
        //Position 5 middle left
        //Position 6 middle right
        boolean flag=false;
        int count=1;
        int size=list.size();
        int offset=36-size;
        int halfOffset=offset/2;
        int xPos;
        //adjust if position=0
        if(position==0) {
            position = 4;
            flag=true;
        }

        //adjust position if only 2 or 3 hands present
        if(position!=1 && hands==2)
            position=3;
        else if(position==2&& hands==3) {
            position = 5;
            leftFlag=true;
        }
        else if(position==4 && hands==3)
            position=6;

        if(position==3 && hands==3 && leftFlag==true)
            position=6;
        else if(position==3 && hands==3 && leftFlag==false) {
            position=5;
            leftFlag=true;
        }

        switch(position) {
            case 1:
                for(Card card:list) {
                    xPos=-32*size+572;
                    card.setNewScaleX(4f/3f);
                    card.setNewScaleY(4f/3f);
                    card.setNewPos(xPos+(count-1)*64,800);
                    count++;
                }
                 break;
            case 2:
                for(Card card:list) {
                    xPos=-halfOffset*size+270+halfOffset;
                    card.setNewScaleX(.75f);
                    card.setNewScaleY(.75f);
                    card.setNewPos(xPos+(count-1)*offset,1100);
                    count++;
                }
                break;
            case 3:
                for(Card card:list) {
                    xPos=-halfOffset*size+540+halfOffset;
                    card.setNewScaleX(.75f);
                    card.setNewScaleY(.75f);
                    card.setNewPos(xPos+(count-1)*offset,1100);
                    count++;
                }
                break;
            case 4:
                for(Card card:list) {
                    xPos=-halfOffset*size+810+halfOffset;
                    card.setNewScaleX(.75f);
                    card.setNewScaleY(.75f);
                    card.setNewPos(xPos+(count-1)*offset,1100);
                    count++;
                }
                break;
            case 5:
                for(Card card:list) {
                    xPos=-halfOffset*size+360+halfOffset;
                    card.setNewScaleX(.75f);
                    card.setNewScaleY(.75f);
                    card.setNewPos(xPos+(count-1)*offset,1100);
                    count++;
                }
                break;
            case 6:
                for(Card card:list) {
                    xPos=-halfOffset*size+720+halfOffset;
                    card.setNewScaleX(.75f);
                    card.setNewScaleY(.75f);
                    card.setNewPos(xPos+(count-1)*offset,1100);
                    count++;
                }
                break;
        }

        return flag;
    }

    public void surrender() {
        Assets.multiChips.play(MainScreen.sound);
        state=7;
        settingsManager.setSurrenderTotal(settingsManager.getSurrenderTotal()+1);
        surrenderButton.setNewPos(540,-100);
        surrender=true;
        playerBust=true;
        int originalBet=chipManager.getMainBetMoney()-chipManager.getInsuranceMoney();
        int total=originalBet-originalBet/2+chipManager.getInsuranceMoney();
        settingsManager.setMoneyBet(settingsManager.getMoneyBet()+chipManager.getMainBetMoney());
        settingsManager.setMoneyLost(settingsManager.getMoneyLost()+total);
        chipManager.payout(chipManager.getMainBet(),chipManager.getMainBetChips(),3,-total);
    }

    public float checkAgainstDealer(ArrayList<Card> hand) {
        // 0=push, -1=lost, 1=won, 1.5=blackjack
        int handValue=Math.abs(calcHand(hand));
        int dealerValue=Math.abs(calcHand(dealerCards));
        float flag=0;
        //check blackjack
        if(playerBJ==true && dealerValue!=21) {
            flag=(float)settingsManager.getBlackjackPays();
        }//check other hands    player busts, dealer bust, player higher than dealer,dealer higher than player, player equal to dealer
        else if((handValue>21)) {
            flag=-1;
        }
        else if(dealerValue>21 || handValue>dealerValue) {
            flag=1;
        }
        else if(dealerValue>handValue) {
            flag=-1;
        }
        else if(handValue==dealerValue) {
            flag=0;
        }

        return flag;
    }

    public void payouts() {
        int total=0;
        total= (int)(checkAgainstDealer(playerCards)*chipManager.getPlayerHandMoney()) + (int)checkAgainstDealer(playerSplitOne)*chipManager.getPlayerSplitOneMoney()+ (int)checkAgainstDealer(playerSplitTwo)*chipManager.getPlayerSplitTwoMoney()+ (int)checkAgainstDealer(playerSplitThree)*chipManager.getPlayerSplitThreeMoney();
        if(insurance) {
            settingsManager.setMoneyBet(settingsManager.getMoneyBet()+chipManager.getInsuranceMoney());
            settingsManager.setInsuranceTotal(settingsManager.getInsuranceTotal()+1);
            if(Math.abs(calcHand(dealerCards))==21 && dealerCards.size()==2) {
                total += chipManager.getInsuranceMoney() * 2;
                settingsManager.setMoneyWon(settingsManager.getMoneyWon()+chipManager.getInsuranceMoney()*2);
                settingsManager.setInsuranceWon(settingsManager.getInsuranceWon()+1);
            }
            else {
                total -= chipManager.getInsuranceMoney();
                settingsManager.setMoneyLost(settingsManager.getMoneyLost()+chipManager.getInsuranceMoney());
            }
        }

        settingsManager.setHandsPlayed(settingsManager.getHandsPlayed()+hands);

        handStats(playerCards,chipManager.getPlayerHandMoney());
        handStats(playerSplitOne,chipManager.getPlayerSplitOneMoney());
        handStats(playerSplitTwo,chipManager.getPlayerSplitTwoMoney());
        handStats(playerSplitThree,chipManager.getPlayerSplitThreeMoney());

        if(total<0)
            Assets.multiChips.play(MainScreen.sound);
        else if(total==0)
            Assets.push.play(MainScreen.sound);
        else
            Assets.winningBet.play(MainScreen.sound);

        chipManager.payout(chipManager.getMainBet(),chipManager.getMainBetChips(),3,total);
    }

    private void handStats(ArrayList<Card> hand,int money) {
        if(money>0) {
            settingsManager.setMoneyBet(settingsManager.getMoneyBet()+money);
            if (checkAgainstDealer(hand) > 0) {
                settingsManager.setHandsWon(settingsManager.getHandsWon() + 1);
                settingsManager.setMoneyWon(settingsManager.getMoneyWon()+(int)(money*checkAgainstDealer(hand)));
                if((int)hand.get(hand.size()-1).getRotation()==90) {
                    settingsManager.setDoubleDownWon(settingsManager.getDoubleDownWon()+1);
                }

            } else if (checkAgainstDealer(hand) < 0) {
                settingsManager.setHandsLost(settingsManager.getHandsLost() + 1);
                settingsManager.setMoneyLost(settingsManager.getMoneyLost()+money);
                if((int)hand.get(hand.size()-1).getRotation()==90) {
                    settingsManager.setDoubleDownLost(settingsManager.getDoubleDownLost()+1);
                }
            }
        }
    }

    public int checkPayout() {
        int total=0;
        if(surrender==false)
            total= (int)(checkAgainstDealer(playerCards)*chipManager.getPlayerHandMoney()) + (int)checkAgainstDealer(playerSplitOne)*chipManager.getPlayerSplitOneMoney()+ (int)checkAgainstDealer(playerSplitTwo)*chipManager.getPlayerSplitTwoMoney()+ (int)checkAgainstDealer(playerSplitThree)*chipManager.getPlayerSplitThreeMoney();
        else
            total=-chipManager.getSurrenderMoney();
        if(insurance) {
            if(Math.abs(calcHand(dealerCards))==21 && dealerCards.size()==2)
                total+=chipManager.getInsuranceMoney()*2;
            else
                total-=chipManager.getInsuranceMoney();
        }

        return total;
    }

    public void checkEndOfDeck() {
        if(pointer==cards.size()) {
            pointer = 0;

            shuffleInProgress=true;

            for(Card card: playerCards) {
                cards.remove(card);
            }
            for(Card card: playerSplitOne) {
                cards.remove(card);
            }
            for(Card card: playerSplitTwo) {
                cards.remove(card);
            }
            for(Card card: playerSplitThree) {
                cards.remove(card);
            }
            for(Card card: dealerCards) {
                cards.remove(card);
            }
            shuffle();
            for(Card card: cards) {
                card.reset();
            }
            Assets.shuffle.play(MainScreen.sound);
            for(Card card:shuffling) {
                card.setNewPos(2120, 1880);
            }
            for(Card card:playerCards) {
                cards.add(0,card);
                pointer++;
            }
            for(Card card:playerSplitOne) {
                cards.add(0,card);
                pointer++;
            }
            for(Card card:playerSplitTwo) {
                cards.add(0,card);
                pointer++;
            }
            for(Card card:playerSplitThree) {
                cards.add(0,card);
                pointer++;
            }
            for(Card card:dealerCards) {
                cards.add(0,card);
                pointer++;
            }
        }

    }

    public void newDeck() {
        int card=0;
        cards.clear();
        clearCards();
        decks=settingsManager.getDecks();
        for(int i=0;i<52*settingsManager.getDecks();i++,card++) {
            cards.add(i,new Card(card%52+1));
        }
        Assets.meterDecks.changeValues((settingsManager.getDecks()-1)*29,41,29,40);
        shuffle();
        pointer=0;
    }

    public void shuffleDeck() {
        shuffle();
        for(Card card: cards) {
            card.reset();
        }
        pointer=0;
        if(settingsManager.getCSM()==false) {
            Assets.shuffle.play(MainScreen.sound);
            resetShuffling();
            for (Card card : shuffling) {
                card.setNewPos(2120, 1880);
            }
            shuffleInProgress = true;
        }
    }

    public void cardsToDiscard() {
        for(Card card: playerCards) {
            card.setNewPos(-200,1880);
        }
        for(Card card: playerSplitOne) {
            card.setNewPos(-200,1880);
        }
        for(Card card: playerSplitTwo) {
            card.setNewPos(-200,1880);
        }
        for(Card card: playerSplitThree) {
            card.setNewPos(-200,1880);
        }
        for(Card card: dealerCards) {
            card.setNewPos(-200,1880);
        }
    }

    public void clearCards() {
        playerCards.clear();
        playerSplitOne.clear();
        playerSplitTwo.clear();
        playerSplitThree.clear();
        dealerCards.clear();
        playerBust=false;
        playerBJ=false;
        waitTime=30;
        hands=1;
        cardsPos=1;
        chipManager.setInsuranceMoney(0);
        cardsSplitOnePos=2;
        cardsSplitTwoPos=3;
        cardsSplitThreePos=4;
        leftFlag=false;
        surrender=false;
        insurance=false;
    }

    public void checkSideBets() {
        sideBetLeft.setOldVersion(sideBetLeft.getVersion());
        sideBetRight.setOldVersion(sideBetRight.getVersion());
        if(chipManager.getSideBetLeftMoney()!=0) {
            chipManager.payout(chipManager.getSideBetLeft(), chipManager.getSideBetLeftChips(), 1, sideBetLeft.payout(playerCards, dealerCards, chipManager.getSideBetLeftMoney(),settingsManager) * chipManager.getSideBetLeftMoney());
        }
        else {
            sideBetLeft.setOldPayout(0);
            sideBetLeft.setPayout(0);
        }

        if(chipManager.getSideBetRightMoney()!=0) {
            chipManager.payout(chipManager.getSideBetRight(), chipManager.getSideBetRightChips(), 2, sideBetRight.payout(playerCards, dealerCards, chipManager.getSideBetRightMoney(),settingsManager) * chipManager.getSideBetRightMoney());
        }
        else {
            sideBetRight.setOldPayout(0);
            sideBetRight.setPayout(0);
        }


    }

    public void insurance(boolean takeInsurance) {
        if (takeInsurance) {
            chipManager.doubleBet(chipManager.getMainBetMoney()/2);
            chipManager.setInsuranceMoney(chipManager.getMainBetMoney()/2);
            surrenderButton.setNewPos(540,150);
        }
        else {
            insurance=false;
            surrenderButton.setNewPos(540,150);
        }

        if(Math.abs(calcHand(dealerCards))==21) {
            state=5;
            settingsManager.setDealerBlackjacks(settingsManager.getDealerBlackjacks()+1);
            if(Math.abs(calcHand(playerCards))==21 && playerCards.size()==2 && playerSplitOne.size()==0) {
                settingsManager.setPlayerBlackjacks(settingsManager.getPlayerBlackjacks()+1);
            }
            surrenderButton.setNewPos(540,-100);
        }
        else {
            state=1;
            hitButton.setNewPos(186,77);
            standButton.setNewPos(894,77);
        }
    }

    public ArrayList<Card> getCurrentHand() {
        switch(state) {
            default:
                return playerCards;
            case 2:
                return playerSplitOne;
            case 3:
                return playerSplitTwo;
            case 4:
                return playerSplitThree;
        }
    }

    public String findBasicStrategyAction(int deck,ArrayList<Card> hand) {

        String action;

        switch(deck) {
            case 1:
                //1 deck stand on soft 17
                if(settingsManager.getDealer()==1) {
                                           // 2   3   4   5   6   7   8   9   T   A
                    String[][] hardHands = {{"H","H","H","H","H","H","H","H","H","H"}, //4-7
                                            {"H","H","H","DH","DH","H","H","H","H","H"}, //8
                                            {"DH","DH","DH","DH","DH","H","H","H","H","H"}, //9
                                            {"DH","DH","DH","DH","DH","DH","DH","DH","H","H"}, //10
                                            {"DH","DH","DH","DH","DH","DH","DH","DH","DH","DH"}, //11
                                            {"H","H","S","S","S","H","H","H","H","H"}, //12
                                            {"S","S","S","S","S","H","H","H","H","H"}, //13
                                            {"S","S","S","S","S","H","H","H","H","H"}, //14
                                            {"S","S","S","S","S","H","H","H","H","H"}, //15
                                            {"S","S","S","S","S","H","H","H","RH","RH"}, //16
                                            {"S","S","S","S","S","S","S","S","S","S"}, //17
                                            {"S","S","S","S","S","S","S","S","S","S"}};//18+
                                            //2   3   4   5   6   7   8   9   T   A
                    String[][] softHands = {{"H","H","DH","DH","DH","H","H","H","H","H"},//13
                                            {"H","H","DH","DH","DH","H","H","H","H","H"},//14
                                            {"H","H","DH","DH","DH","H","H","H","H","H"},//15
                                            {"H","H","DH","DH","DH","H","H","H","H","H"},//16
                                            {"DH","DH","DH","DH","DH","H","H","H","H","H"},//17
                                            {"S","DS","DS","DS","DS","S","S","H","H","S"},//18
                                            {"S","S","S","S","DS","S","S","S","S","S"},//19
                                            {"S","S","S","S","S","S","S","S","S","S"}};//20
                                            //2   3   4   5   6   7   8   9   T   A
                    String[][] splitHands= {{"PH","P","P","P","P","P","H","H","H","H"},//2,2
                                            {"PH","PH","P","P","P","P","PH","H","H","H"},//3,3
                                            {"H","H","PH","PD","PD","H","H","H","H","H"},//4,4
                                            {"DH","DH","DH","DH","DH","DH","DH","DH","H","H"},//5,5
                                            {"P","P","P","P","P","PH","H","H","H","H"},//6,6
                                            {"P","P","P","P","P","P","PH","H","RS","H"},//7,7
                                            {"P","P","P","P","P","P","P","P","P","P"},//8,8
                                            {"P","P","P","P","P","S","P","P","S","S"},//9,9
                                            {"S","S","S","S","S","S","S","S","S","S"},//T,T
                                            {"P","P","P","P","P","P","P","P","P","P"}};//A,A
                    action=basicStrategy(hardHands,softHands,splitHands,hand);
                } //1 deck hit on soft 17
                else {
                                           // 2   3   4   5   6   7   8   9   T   A
                    String[][] hardHands = {{"H","H","H","H","H","H","H","H","H","H"}, //4-7
                                            {"H","H","H","DH","DH","H","H","H","H","H"}, //8
                                            {"DH","DH","DH","DH","DH","H","H","H","H","H"}, //9
                                            {"DH","DH","DH","DH","DH","DH","DH","DH","H","H"}, //10
                                            {"DH","DH","DH","DH","DH","DH","DH","DH","DH","DH"}, //11
                                            {"H","H","S","S","S","H","H","H","H","H"}, //12
                                            {"S","S","S","S","S","H","H","H","H","H"}, //13
                                            {"S","S","S","S","S","H","H","H","H","H"}, //14
                                            {"S","S","S","S","S","H","H","H","H","RH"}, //15
                                            {"S","S","S","S","S","H","H","H","RH","RH"}, //16
                                            {"S","S","S","S","S","S","S","S","S","RS"}, //17
                                            {"S","S","S","S","S","S","S","S","S","S"}};//18+
                                            //2   3   4   5   6   7   8   9   T   A
                    String[][] softHands = {{"H","H","DH","DH","DH","H","H","H","H","H"},//13
                                            {"H","H","DH","DH","DH","H","H","H","H","H"},//14
                                            {"H","H","DH","DH","DH","H","H","H","H","H"},//15
                                            {"H","H","DH","DH","DH","H","H","H","H","H"},//16
                                            {"DH","DH","DH","DH","DH","H","H","H","H","H"},//17
                                            {"S","DS","DS","DS","DS","S","S","H","H","H"},//18
                                            {"S","S","S","S","DS","S","S","S","S","S"},//19
                                            {"S","S","S","S","S","S","S","S","S","S"}};//20
                                            //2   3   4   5   6   7   8   9   T   A
                    String[][] splitHands= {{"PH","P","P","P","P","P","H","H","H","H"},//2,2
                                            {"PH","PH","P","P","P","P","PH","H","H","H"},//3,3
                                            {"H","H","PH","PD","PD","H","H","H","H","H"},//4,4
                                            {"DH","DH","DH","DH","DH","DH","DH","DH","H","H"},//5,5
                                            {"P","P","P","P","P","PH","H","H","H","H"},//6,6
                                            {"P","P","P","P","P","P","PH","H","RS","RH"},//7,7
                                            {"P","P","P","P","P","P","P","P","P","P"},//8,8
                                            {"P","P","P","P","P","S","P","P","S","PS"},//9,9
                                            {"S","S","S","S","S","S","S","S","S","S"},//T,T
                                            {"P","P","P","P","P","P","P","P","P","P"}};//A,A
                    action=basicStrategy(hardHands,softHands,splitHands,hand);
                }

                break;
            case 2:
                //2 decks stand on soft 17
                if(settingsManager.getDealer()==1) {
                                           // 2   3   4   5   6   7   8   9   T   A
                    String[][] hardHands = {{"H","H","H","H","H","H","H","H","H","H"}, //4-7
                                            {"H","H","H","H","H","H","H","H","H","H"}, //8
                                            {"DH","DH","DH","DH","DH","H","H","H","H","H"}, //9
                                            {"DH","DH","DH","DH","DH","DH","DH","DH","H","H"}, //10
                                            {"DH","DH","DH","DH","DH","DH","DH","DH","DH","DH"}, //11
                                            {"H","H","S","S","S","H","H","H","H","H"}, //12
                                            {"S","S","S","S","S","H","H","H","H","H"}, //13
                                            {"S","S","S","S","S","H","H","H","H","H"}, //14
                                            {"S","S","S","S","S","H","H","H","RH","H"}, //15
                                            {"S","S","S","S","S","H","H","H","RH","RH"}, //16
                                            {"S","S","S","S","S","S","S","S","S","S"}, //17
                                            {"S","S","S","S","S","S","S","S","S","S"}};//18+
                                            //2   3   4   5   6   7   8   9   T   A
                    String[][] softHands = {{"H","H","H","DH","DH","H","H","H","H","H"},//13
                                            {"H","H","H","DH","DH","H","H","H","H","H"},//14
                                            {"H","H","DH","DH","DH","H","H","H","H","H"},//15
                                            {"H","H","DH","DH","DH","H","H","H","H","H"},//16
                                            {"H","DH","DH","DH","DH","H","H","H","H","H"},//17
                                            {"S","DS","DS","DS","DS","S","S","H","H","H"},//18
                                            {"S","S","S","S","S","S","S","S","S","S"},//19
                                            {"S","S","S","S","S","S","S","S","S","S"}};//20
                                            //2   3   4   5   6   7   8   9   T   A
                    String[][] splitHands= {{"PH","PH","P","P","P","P","H","H","H","H"},//2,2
                                            {"PH","PH","P","P","P","P","H","H","H","H"},//3,3
                                            {"H","H","H","PH","PH","H","H","H","H","H"},//4,4
                                            {"DH","DH","DH","DH","DH","DH","DH","DH","H","H"},//5,5
                                            {"P","P","P","P","P","PH","H","H","H","H"},//6,6
                                            {"P","P","P","P","P","P","PH","H","H","H"},//7,7
                                            {"P","P","P","P","P","P","P","P","P","P"},//8,8
                                            {"P","P","P","P","P","S","P","P","S","S"},//9,9
                                            {"S","S","S","S","S","S","S","S","S","S"},//T,T
                                            {"P","P","P","P","P","P","P","P","P","P"}};//A,A
                    action=basicStrategy(hardHands,softHands,splitHands,hand);
                } //2 decks hit on soft 17
                else {
                                           // 2   3   4   5   6   7   8   9   T   A
                    String[][] hardHands = {{"H","H","H","H","H","H","H","H","H","H"}, //4-7
                                            {"H","H","H","H","H","H","H","H","H","H"}, //8
                                            {"DH","DH","DH","DH","DH","H","H","H","H","H"}, //9
                                            {"DH","DH","DH","DH","DH","DH","DH","DH","H","H"}, //10
                                            {"DH","DH","DH","DH","DH","DH","DH","DH","DH","DH"}, //11
                                            {"H","H","S","S","S","H","H","H","H","H"}, //12
                                            {"S","S","S","S","S","H","H","H","H","H"}, //13
                                            {"S","S","S","S","S","H","H","H","H","H"}, //14
                                            {"S","S","S","S","S","H","H","H","RH","RH"}, //15
                                            {"S","S","S","S","S","H","H","H","RH","RH"}, //16
                                            {"S","S","S","S","S","S","S","S","S","RS"}, //17
                                            {"S","S","S","S","S","S","S","S","S","S"}};//18+
                                            //2   3   4   5   6   7   8   9   T   A
                    String[][] softHands = {{"H","H","H","DH","DH","H","H","H","H","H"},//13
                                            {"H","H","DH","DH","DH","H","H","H","H","H"},//14
                                            {"H","H","DH","DH","DH","H","H","H","H","H"},//15
                                            {"H","H","DH","DH","DH","H","H","H","H","H"},//16
                                            {"H","DH","DH","DH","DH","H","H","H","H","H"},//17
                                            {"DS","DS","DS","DS","DS","S","S","H","H","H"},//18
                                            {"S","S","S","S","DS","S","S","S","S","S"},//19
                                            {"S","S","S","S","S","S","S","S","S","S"}};//20
                                            //2   3   4   5   6   7   8   9   T   A
                    String[][] splitHands= {{"PH","PH","P","P","P","P","H","H","H","H"},//2,2
                                            {"PH","PH","P","P","P","P","H","H","H","H"},//3,3
                                            {"H","H","H","PH","PH","H","H","H","H","H"},//4,4
                                            {"DH","DH","DH","DH","DH","DH","DH","DH","H","H"},//5,5
                                            {"P","P","P","P","P","PH","H","H","H","H"},//6,6
                                            {"P","P","P","P","P","P","PH","H","H","H"},//7,7
                                            {"P","P","P","P","P","P","P","P","P","RP"},//8,8
                                            {"P","P","P","P","P","S","P","P","S","S"},//9,9
                                            {"S","S","S","S","S","S","S","S","S","S"},//T,T
                                            {"P","P","P","P","P","P","P","P","P","P"}};//A,A
                    action=basicStrategy(hardHands,softHands,splitHands,hand);
                }
                break;

            default:
                //3+ decks stand on soft 17
                if(settingsManager.getDealer()==1) {
                                           // 2   3   4   5   6   7   8   9   T   A
                    String[][] hardHands = {{"H","H","H","H","H","H","H","H","H","H"}, //4-7
                                            {"H","H","H","H","H","H","H","H","H","H"}, //8
                                            {"H","DH","DH","DH","DH","H","H","H","H","H"}, //9
                                            {"DH","DH","DH","DH","DH","DH","DH","DH","H","H"}, //10
                                            {"DH","DH","DH","DH","DH","DH","DH","DH","DH","H"}, //11
                                            {"H","H","S","S","S","H","H","H","H","H"}, //12
                                            {"S","S","S","S","S","H","H","H","H","H"}, //13
                                            {"S","S","S","S","S","H","H","H","H","H"}, //14
                                            {"S","S","S","S","S","H","H","H","RH","H"}, //15
                                            {"S","S","S","S","S","H","H","RH","RH","RH"}, //16
                                            {"S","S","S","S","S","S","S","S","S","S"}, //17
                                            {"S","S","S","S","S","S","S","S","S","S"}};//18+
                                            //2   3   4   5   6   7   8   9   T   A
                    String[][] softHands = {{"H","H","H","DH","DH","H","H","H","H","H"},//13
                                            {"H","H","H","DH","DH","H","H","H","H","H"},//14
                                            {"H","H","DH","DH","DH","H","H","H","H","H"},//15
                                            {"H","H","DH","DH","DH","H","H","H","H","H"},//16
                                            {"H","DH","DH","DH","DH","H","H","H","H","H"},//17
                                            {"S","DS","DS","DS","DS","S","S","H","H","H"},//18
                                            {"S","S","S","S","S","S","S","S","S","S"},//19
                                            {"S","S","S","S","S","S","S","S","S","S"}};//20
                                            //2   3   4   5   6   7   8   9   T   A
                    String[][] splitHands= {{"PH","PH","P","P","P","P","H","H","H","H"},//2,2
                                            {"PH","PH","P","P","P","P","H","H","H","H"},//3,3
                                            {"H","H","H","PH","PH","H","H","H","H","H"},//4,4
                                            {"DH","DH","DH","DH","DH","DH","DH","DH","H","H"},//5,5
                                            {"PH","P","P","P","P","H","H","H","H","H"},//6,6
                                            {"P","P","P","P","P","P","H","H","H","H"},//7,7
                                            {"P","P","P","P","P","P","P","P","P","P"},//8,8
                                            {"P","P","P","P","P","S","P","P","S","S"},//9,9
                                            {"S","S","S","S","S","S","S","S","S","S"},//T,T
                                            {"P","P","P","P","P","P","P","P","P","P"}};//A,A
                    action=basicStrategy(hardHands,softHands,splitHands,hand);
                } //3+ decks hit on soft 17
                else {
                                           // 2   3   4   5   6   7   8   9   T   A
                    String[][] hardHands = {{"H","H","H","H","H","H","H","H","H","H"}, //4-7
                                            {"H","H","H","H","H","H","H","H","H","H"}, //8
                                            {"H","DH","DH","DH","DH","H","H","H","H","H"}, //9
                                            {"DH","DH","DH","DH","DH","DH","DH","DH","H","H"}, //10
                                            {"DH","DH","DH","DH","DH","DH","DH","DH","DH","DH"}, //11
                                            {"H","H","S","S","S","H","H","H","H","H"}, //12
                                            {"S","S","S","S","S","H","H","H","H","H"}, //13
                                            {"S","S","S","S","S","H","H","H","H","H"}, //14
                                            {"S","S","S","S","S","H","H","H","RH","RH"}, //15
                                            {"S","S","S","S","S","H","H","RH","RH","RH"}, //16
                                            {"S","S","S","S","S","S","S","S","S","RS"}, //17
                                            {"S","S","S","S","S","S","S","S","S","S"}};//18+
                                            //2   3   4   5   6   7   8   9   T   A
                    String[][] softHands = {{"H","H","H","DH","DH","H","H","H","H","H"},//13
                                            {"H","H","H","DH","DH","H","H","H","H","H"},//14
                                            {"H","H","DH","DH","DH","H","H","H","H","H"},//15
                                            {"H","H","DH","DH","DH","H","H","H","H","H"},//16
                                            {"H","DH","DH","DH","DH","H","H","H","H","H"},//17
                                            {"DS","DS","DS","DS","DS","S","S","H","H","H"},//18
                                            {"S","S","S","S","DS","S","S","S","S","S"},//19
                                            {"S","S","S","S","S","S","S","S","S","S"}};//20
                                            //2   3   4   5   6   7   8   9   T   A
                    String[][] splitHands= {{"PH","PH","P","P","P","P","H","H","H","H"},//2,2
                                            {"PH","PH","P","P","P","P","H","H","H","H"},//3,3
                                            {"H","H","H","PH","PH","H","H","H","H","H"},//4,4
                                            {"DH","DH","DH","DH","DH","DH","DH","DH","H","H"},//5,5
                                            {"PH","P","P","P","P","H","H","H","H","H"},//6,6
                                            {"P","P","P","P","P","P","H","H","H","H"},//7,7
                                            {"P","P","P","P","P","P","P","P","P","RP"},//8,8
                                            {"P","P","P","P","P","S","P","P","S","S"},//9,9
                                            {"S","S","S","S","S","S","S","S","S","S"},//T,T
                                            {"P","P","P","P","P","P","P","P","P","P"}};//A,A
                    action=basicStrategy(hardHands,softHands,splitHands,hand);
                }
                break;
        }

        return action;
    }

    public String basicStrategy(String[][] hardHands, String[][] softHands, String[][] splitHands,ArrayList<Card> hand) {
        String action="";
        int value=0;

        // H=hit
        // S=stand
        // DH=doubled if allowed,else hit
        // DS=doubled if allowed,else stand
        // P=split
        // PH=split if double after split allowed,else hit
        // R=surrender
        // RH=surrender if allowed, else hit
        // RS=surrender if allowed, else stand
        // RP=surrender if allowed, else split

        //If cant split anymore, use hard hand table, unless its A,A hit to double to stand for dealer 2,3,4,7+, double hit stand for 5,6 or on 4+ decks for 6 only

        //1 for hardHand, 2 for softHand, 3 for splitHand
        int whichTable=1;
        if(hand.size()==2) {
            int tempCard1=hand.get(0).getValue();
            int tempCard2=hand.get(1).getValue();

            if(tempCard1>10)
                tempCard1=10;

            if(tempCard2>10)
                tempCard2=10;
            //if able to split
            if(tempCard1==tempCard2) {
                if(hands<settingsManager.getSplitHands() && splitButton.getNewY()!=-100) {
                    whichTable=3;
                }
                else {
                    whichTable=1;
                }
            }
            else {
                //soft Hand
                if(calcHand(hand)<0)
                    whichTable=2;
            }


        }
        else {
            //Soft Hand
            if(calcHand(hand)<0)
                whichTable=2;
        }

        //dealers up card
        int dealerCard=dealerCards.get(0).getValue();
        if(dealerCard>10)
            dealerCard=10;
        dealerCard-=2;
        if(dealerCard<0)
            dealerCard=9;

        switch(whichTable) {
            case 1:

                value=calcHand(hand);
                value-=7;

                if(value<0)
                    value=0;
                else if(value>11)
                    value=11;
                action=hardHands[value][dealerCard];
                break;
            case 2:
                value=Math.abs(calcHand(hand));
                value-=13;
                if(value<0)
                    value=0;
                else if(value>7)
                    value=7;

                action=softHands[value][dealerCard];
                break;

            case 3:
                value=hand.get(0).getValue();
                if(value>10)
                    value=10;
                else if(value==1)
                    value=11;
                value-=2;

                action=splitHands[value][dealerCard];
        }

        if(action.length()==2)
            action=basicStrategyMultiValue(hand,action);
        else
            action=basicStrategyExceptions(hand,action);


        switch(action) {
            case "H":
                action = "hit";
                break;
            case "S":
                action = "stand";
                break;
            case "D":
                action = "double down";
                break;
            case "P":
                action = "split";
                break;
            case "R":
                action ="surrender";
                break;
        }



        return action;
    }

    public String basicStrategyMultiValue(ArrayList<Card> hand,String action) {
        if(action.charAt(0)=='R') {
            if(surrenderButton.getNewY()!=-100) {
                if (decks == 2 && action.charAt(1)== 'P' && settingsManager.getDas()==true) //2 deck exception for 8,8 v A
                    action = "P";
                else
                    action = "R";
            }
            else {
                action = basicStrategyExceptions(hand,action.substring(1));
            }
        }
        else if(action.charAt(0)=='D') {
            if(doubleButton.getNewY()!=-100)
                action="D";
            else
                action=action.substring(1);
        }
        else if(action.charAt(0)=='P') {
            if(settingsManager.getDas()==true)
                action="P";
            else
                action=action.substring(1);
        }

        return action;
    }

    public String basicStrategyExceptions(ArrayList<Card> hand,String action) {
        int dealerCard=dealerCards.get(0).getValue();
        if(dealerCard>10)
            dealerCard=10;

        switch(decks) {
            case 1:
                //12 v3
                if(calcHand(hand)==12 && dealerCard==3) {
                    switch (hand.size()) {
                        case 2:
                            action="H";
                            break;
                        case 3:
                        case 4:
                        case 5:
                            action="S";
                            break;
                        default:
                            if(settingsManager.getDealer()==1)
                                action="H";
                            else
                                action="S";
                            break;
                    }
                }
                //16v7 or 16v9
                if(calcHand(hand)==16 && (dealerCard==7 || dealerCard==9)) {
                    switch (hand.size()) {
                        case 2:
                        case 3:
                        case 4:
                            action="H";
                            break;
                        default:
                            action="S";
                            break;
                    }
                }
                //16v8
                if(calcHand(hand)==16 && dealerCard==8) {
                    switch (hand.size()) {
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                            action="H";
                            break;
                        default:
                            action="S";
                            break;
                    }
                }
                //16v10
                if(calcHand(hand)==16 && dealerCard==10) {
                    switch (hand.size()) {
                        case 2:
                            action="H";
                            break;
                        default:
                            action="S";
                            break;
                    }
                }
                //16vA
                if(calcHand(hand)==16 && dealerCard==1) {
                    switch (hand.size()) {
                        case 2:
                        case 3:
                        case 4:
                            action="H";
                            break;
                        default:
                            if(settingsManager.getDealer()==1)
                                action="H";
                            else
                                action="S";
                            break;
                    }
                }
                //soft 18v10
                if(calcHand(hand)==-18 && dealerCard==10) {
                    switch (hand.size()) {
                        case 5:
                            action="S";
                            break;
                        default:
                            action="H";
                            break;
                    }
                }
                //soft 18vA
                if(calcHand(hand)==-18 && dealerCard==1) {
                    switch (hand.size()) {
                        case 2:
                        case 3:
                        case 4:
                            if(settingsManager.getDealer()==1)
                                action="S";
                            else
                                action="H";
                            break;
                        case 5:
                            action="S";
                            break;
                        default:
                            action="H";
                            break;
                    }
                }
                break;
            //2-8 decks
            default:
                if(calcHand(hand)==16 && hand.size()>=3)
                    action="S";
                //for 2 decks
                if(decks==2) {
                    if(settingsManager.getDealer()==1 && calcHand(hand)==-18 && dealerCard==1 && hand.size()>=3)
                        action="S";
                }
                else if(decks>=3 && decks<=6) {
                    if(settingsManager.getDealer()==1 && calcHand(hand)==-18 && dealerCard==1 && hand.size()>=4)
                        action="S";
                }
                break;
        }
        //A,A exception when unable to split
        if(hand.get(0).getValue()==1 && hand.get(1).getValue()==1 && hand.size()==2) {
            if(hands==settingsManager.getSplitHands()) {
                switch(decks) {
                    case 1:
                    case 2:
                        if(dealerCard<=4 || dealerCard>=7) {
                            if(hitButton.getNewY()!=-100)
                                action="H";
                            else if(doubleButton.getNewY()!=-100) {
                                action="D";
                            }
                            else
                                action="S";
                        }
                        else {
                            if(doubleButton.getNewY()!=-100)
                                action="D";
                            else if(hitButton.getNewY()!=-100) {
                                action="H";
                            }
                            else
                                action="S";
                        }
                        break;
                    default:
                        if(dealerCard<=5 || dealerCard>=7) {
                            if(hitButton.getNewY()!=-100)
                                action="H";
                            else if(doubleButton.getNewY()!=-100) {
                                action="D";
                            }
                            else
                                action="S";
                        }
                        else {
                            if(doubleButton.getNewY()!=-100)
                                action="D";
                            else if(hitButton.getNewY()!=-100) {
                                action="H";
                            }
                            else
                                action="S";
                        }
                        break;
                }
            }
        }

        return action;
    }

    public void loadData(ChipManager chipManager,Settings settingsManager) {
        this.chipManager=chipManager;
        this.settingsManager=settingsManager;

        sideBetLeft.load();
        sideBetRight.load();

        settings.loadTexture(1);
        rules.loadTexture(2);
        freeChips.loadTexture(3);
        sound.loadTexture(4);
        if(settingsManager.getSound()==0)
            sound.setTextureRegion(Assets.soundOffButton);


        for(Card card:cards) {
            card.loadTexture();
        }
        for(Card card: playerCards) {
            card.loadTexture();
        }
        for(Card card: playerSplitOne) {
            card.loadTexture();
        }
        for(Card card: playerSplitTwo) {
            card.loadTexture();
        }
        for(Card card: playerSplitThree) {
            card.loadTexture();
        }
        for(Card card: dealerCards) {
            card.loadTexture();
        }
        for(Card card: shuffling) {
            card.loadTexture();
        }


    }



}
