package com.mikegacek.blackjack;

import com.mikegacek.blackjack.framework.gl.TextureRegion;
import com.mikegacek.blackjack.framework.math.Rectangle;

import java.io.Serializable;

/**
 * Created by Michael on 2/13/2017.
 */

public class Settings  implements Serializable {

    private static final long serialVersionUID = 14L;


    // DECKS: (1,2,3,4,5,6,7,8) (Continuous shuffling machine) default 8
    // BLACKJACK PAYOUTS: (3:2, 7:5, 6:5, 1:1) default 3:2  (1.5,1.4,1.2,1)
    // DEALER (HITS OR STANDS) SOFT 17 default stand
    // SPLITTING: (2,3,4) hands, (resplit aces), default 4,no resplit
    // DOUBLE DOWN: (any 2, 9-11 only, 10-11 only), (double after split) , (double split aces) default any 2, double after split, no double split aces
    // SURRENDER: (yes,no) default yes
    // SIDE BETS (PERFECT PAIR (2-8 decks) on off, 21+3 (1-8 decks) on off) default on for both
    // DECK PENETRATION: (0-100%) default 70%

    // changing DECKS, DEALER, DECK PENETRATION reshuffles deck, changes will be made after hand finishes

    // Gameplay dealer hit stands soft 17,blackjack pays, insurance, surrender, split (2,3,4), double down, double after split, hit split aces, double split aces,resplit aces

    // DECKS (number, penetration), DEALER (hit stand soft 17), GAMEPLAY (payouts, double splitting, surrender), SIDE BETS

    private int page;
    private Rectangle deckButton,gameplayButton,sidebetButton,exitButton,csmSelect,greenTable,redTable,blueTable,purpleTable,insuranceButton,surrenderButton,dealerButton,resplitAcesButton,hitSplitAcesButton,doubleSplitAcesButton,doubleAfterSplitButton,perfectPairsButton,twentyOneV1Button,twentyOneV2Button,resetStats;
    private transient Button deckLeft,deckRight,penLeft,penRight,blackjackLeft,blackjackRight,splitLeft,splitRight,doubleLeft,doubleRight;
    private transient TextureRegion backgroundTexture;
    private int decks,penetration,background,dealer,split,doubleDown,sound;
    private double blackjackPays;
    private boolean csm,insurance,surrender,resplit,hitAces,doubleAces,das,perfectPairs,twentyOneV1,twentyOneV2;

    private int decksTemp,penetrationTemp,backgroundTemp,dealerTemp,splitTemp,doubleDownTemp;
    private double blackjackPaysTemp;
    private boolean csmTemp,insuranceTemp,surrenderTemp,resplitTemp,hitAcesTemp,doubleAcesTemp,dasTemp,perfectPairsTemp,twentyOneV1Temp,twentyOneV2Temp;


    //for stats page
    private int playerBlackjacks,dealerBlackjacks,handsPlayed,handsWon,handsLost,moneyBet,moneyWon,moneyLost;
    private int doubleDownTotal,doubleDownWon,doubleDownLost,splitsTotal,insuranceTotal,insuranceWon,surrenderTotal;
    private int perfectPairsTotal,perfectPairsWon,perfectPairsIncome,twentyOneTotal,twentyOneWon,twentyOneV1Income,twentyOneV2Income;

    private boolean newDeck,changeSideBets;


    public Settings() {

        page=1;
        deckButton= new Rectangle(0,909,164,51);
        gameplayButton = new Rectangle(164,909,163,51);
        sidebetButton = new Rectangle(327,909,163,51);
        exitButton = new Rectangle(490,909,50,51);
        csmSelect = new Rectangle(39,800,363,40);
        greenTable = new Rectangle(28,555,205,89);
        blueTable = new Rectangle(306,555,205,89);
        redTable = new Rectangle(28,418,205,89);
        purpleTable = new Rectangle(306,418,205,89);
        resetStats = new Rectangle(462,293,56,56);

        insuranceButton = new Rectangle(39,801,363,40);
        surrenderButton = new Rectangle(39,756,363,40);
        dealerButton = new Rectangle(39,689,469,40);
        resplitAcesButton = new Rectangle(39,585,363,40);
        hitSplitAcesButton = new Rectangle(39,540,363,40);
        doubleSplitAcesButton = new Rectangle(39,495,363,40);
        doubleAfterSplitButton = new Rectangle(39,450,363,40);

        perfectPairsButton = new Rectangle(39,764,363,116);
        twentyOneV1Button = new Rectangle(39,577,363,144);
        twentyOneV2Button= new Rectangle(39,381,363,172);
        
        deckLeft= new Button(45,60,290,866,Assets.menuArrowLeft);
        deckRight = new Button(45,60,472,866,Assets.menuArrowRight);

        penLeft = new Button(45,60,290,776,Assets.menuArrowLeft);
        penRight = new Button(45,60,472,776,Assets.menuArrowRight);

        blackjackLeft= new Button(45,60,290,866,Assets.menuArrowLeft);
        blackjackRight= new Button(45,60,472,866,Assets.menuArrowRight);

        splitLeft = new Button(45,60,290,650,Assets.menuArrowLeft);
        splitRight = new Button(45,60,472,650,Assets.menuArrowRight);

        doubleLeft = new Button(45,60,290,425,Assets.menuArrowLeft);
        doubleRight = new Button(45,60,472,425,Assets.menuArrowRight);


        decks=8;
        penetration=70;
        blackjackPays=1.5;
        backgroundTexture=Assets.green;
        background=1;
        //dealer 1=stand -1=hit
        dealer=1;
        //1=any 2 2=9-11 only 3=10-11 only
        doubleDown=1;
        sound=1;
        split=4;
        
        csm=false;
        insurance=true;
        surrender=true;
        resplit=false;
        hitAces=false;
        doubleAces=false;
        das=true;
        perfectPairs=true;
        twentyOneV2=true;
        twentyOneV1=false;

        playerBlackjacks=0;
        dealerBlackjacks=0;
        handsPlayed=0;
        handsWon=0;
        handsLost=0;
        moneyBet=0;
        moneyWon=0;
        moneyLost=0;
        doubleDownTotal=0;
        doubleDownWon=0;
        doubleDownLost=0;
        splitsTotal=0;
        insuranceTotal=0;
        insuranceWon=0;
        surrenderTotal=0;
        perfectPairsTotal=0;
        perfectPairsWon=0;
        perfectPairsIncome=0;
        twentyOneTotal=0;
        twentyOneWon=0;
        twentyOneV1Income=0;
        twentyOneV2Income=0;
        
        newDeck=false;
        changeSideBets=false;
    }

    public boolean getChangeSideBets() { return changeSideBets;}

    public void setChangeSideBets(boolean b) {changeSideBets=b; }

    public boolean getNewDeck() {return newDeck;}

    public void setNewDeck(boolean b) {newDeck=b;}

    public int getPage() { return page;}

    public void setPage(int p) {
        if(decks<3) {
            if(twentyOneV2==true) {
                twentyOneV2=false;
                twentyOneV1=true;
            }
            if(decks<2)
                perfectPairs=false;
        }
        page=p;
    }

    public Rectangle getExitButton() {
        return exitButton;
    }

    public Rectangle getDeckButton() {
        return deckButton;
    }

    public Rectangle getGameplayButton() {
        return gameplayButton;
    }

    public Rectangle getSidebetButton() {
        return sidebetButton;
    }

    public Rectangle getDoubleSplitAcesButton() {
        return doubleSplitAcesButton;
    }

    public Rectangle getInsuranceButton() {
        return insuranceButton;
    }

    public Rectangle getSurrenderButton() {
        return surrenderButton;
    }

    public Rectangle getDealerButton() { return dealerButton;}

    public Rectangle getResplitAcesButton() {
        return resplitAcesButton;
    }

    public Rectangle getHitSplitAcesButton() {
        return hitSplitAcesButton;
    }

    public Rectangle getDoubleAfterSplitButton() { return doubleAfterSplitButton;}

    public Rectangle getPerfectPairsButton() {
        return perfectPairsButton;
    }

    public Rectangle getTwentyOneV1Button() {
        return twentyOneV1Button;
    }

    public Rectangle getTwentyOneV2Button() {
        return twentyOneV2Button;
    }

    public Rectangle getResetStats() { return resetStats;}

    public TextureRegion getBackgroundTexture() { return backgroundTexture;}

    public void setBackgroundTexture(TextureRegion t) { backgroundTexture=t;}

    public void setBackground(int b) {
        background=b;
    }

    public int getBackground() {
        return background;
    }

    public Button getDeckLeft() {
        return deckLeft;
    }

    public Button getDeckRight() {
        return deckRight;
    }

    public Button getPenLeft() {
        return penLeft;
    }

    public Button getPenRight() {
        return penRight;
    }

    public Button getBlackjackLeft() {
        return blackjackLeft;
    }

    public Button getBlackjackRight() {
        return blackjackRight;
    }


    public Button getSplitLeft() {
        return splitLeft;
    }

    public Button getSplitRight() {
        return splitRight;
    }

    public Button getDoubleLeft() {
        return doubleLeft;
    }

    public Button getDoubleRight() {
        return doubleRight;
    }

    public Rectangle getCsmSelect() {
        return  csmSelect;
    }

    public Rectangle getGreenTable() {
        return greenTable;
    }

    public Rectangle getRedTable() {
        return redTable;
    }

    public Rectangle getBlueTable() {
        return blueTable;
    }

    public Rectangle getPurpleTable() {
        return purpleTable;
    }

    public int getTwentyOneV2Income() {
        return twentyOneV2Income;
    }

    public void setTwentyOneV2Income(int twentyOneV2Income) {
        this.twentyOneV2Income = twentyOneV2Income;
    }

    public int getTwentyOneTotal() {
        return twentyOneTotal;
    }

    public void setTwentyOneTotal(int twentyOneTotal) {
        this.twentyOneTotal = twentyOneTotal;
    }

    public int getTwentyOneWon() {
        return twentyOneWon;
    }

    public void setTwentyOneWon(int twentyOneWon) {
        this.twentyOneWon = twentyOneWon;
    }

    public int getTwentyOneV1Income() {
        return twentyOneV1Income;
    }

    public void setTwentyOneV1Income(int twentyOneV1Income) {
        this.twentyOneV1Income = twentyOneV1Income;
    }

    public int getPerfectPairsTotal() {
        return perfectPairsTotal;
    }

    public void setPerfectPairsTotal(int perfectPairsTotal) {
        this.perfectPairsTotal = perfectPairsTotal;
    }

    public int getPerfectPairsWon() {
        return perfectPairsWon;
    }

    public void setPerfectPairsWon(int perfectPairsWon) {
        this.perfectPairsWon = perfectPairsWon;
    }

    public int getPerfectPairsIncome() {
        return perfectPairsIncome;
    }

    public void setPerfectPairsIncome(int perfectPairsIncome) {
        this.perfectPairsIncome = perfectPairsIncome;
    }

    public int getInsuranceTotal() {
        return insuranceTotal;
    }

    public void setInsuranceTotal(int insuranceTotal) {
        this.insuranceTotal = insuranceTotal;
    }

    public int getInsuranceWon() {
        return insuranceWon;
    }

    public void setInsuranceWon(int insuranceWon) {
        this.insuranceWon = insuranceWon;
    }

    public int getSurrenderTotal() {
        return surrenderTotal;
    }

    public void setSurrenderTotal(int surrenderTotal) {
        this.surrenderTotal = surrenderTotal;
    }

    public int getSplitsTotal() {
        return splitsTotal;
    }

    public void setSplitsTotal(int splitsTotal) {
        this.splitsTotal = splitsTotal;
    }

    public int getDoubleDownTotal() {
        return doubleDownTotal;
    }

    public void setDoubleDownTotal(int doubleDownTotal) {
        this.doubleDownTotal = doubleDownTotal;
    }

    public int getDoubleDownWon() {
        return doubleDownWon;
    }

    public void setDoubleDownWon(int doubleDownWon) {
        this.doubleDownWon = doubleDownWon;
    }

    public int getDoubleDownLost() {
        return doubleDownLost;
    }

    public void setDoubleDownLost(int doubleDownLost) {
        this.doubleDownLost = doubleDownLost;
    }

    public int getMoneyBet() {
        return moneyBet;
    }

    public void setMoneyBet(int moneyBet) {
        this.moneyBet = moneyBet;
    }

    public int getMoneyWon() {
        return moneyWon;
    }

    public void setMoneyWon(int moneyWon) {
        this.moneyWon = moneyWon;
    }

    public int getMoneyLost() {
        return moneyLost;
    }

    public void setMoneyLost(int moneyLost) {
        this.moneyLost = moneyLost;
    }

    public int getHandsWon() {
        return handsWon;
    }

    public void setHandsWon(int handsWon) {
        this.handsWon = handsWon;
    }

    public int getHandsLost() {
        return handsLost;
    }

    public void setHandsLost(int handsLost) {
        this.handsLost = handsLost;
    }

    public int getHandsPlayed() {
        return handsPlayed;
    }

    public void setHandsPlayed(int handsPlayed) {
        this.handsPlayed = handsPlayed;
    }

    public int getPlayerBlackjacks() {
        return playerBlackjacks;
    }

    public void setPlayerBlackjacks(int playerBlackjacks) {
        this.playerBlackjacks = playerBlackjacks;
    }

    public int getDealerBlackjacks() {
        return dealerBlackjacks;
    }

    public void setDealerBlackjacks(int dealerBlackjacks) {
        this.dealerBlackjacks = dealerBlackjacks;
    }

    public boolean getCSM() { return csm;}

    public void setCSM(boolean flag) { csm=flag;}

    public boolean getInsurance() { return insurance;}

    public void setInsurance(boolean flag) { insurance=flag;}

    public boolean getSurrender() {
        return surrender;
    }

    public void setSurrender(boolean surrender) {
        this.surrender = surrender;
    }

    public boolean getResplit() {
        return resplit;
    }

    public void setResplit(boolean resplit) {
        this.resplit = resplit;
    }

    public boolean getHitAces() {
        return hitAces;
    }

    public void setHitAces(boolean hitAces) {
        this.hitAces = hitAces;
    }

    public boolean getDoubleAces() {
        return doubleAces;
    }

    public void setDoubleAces(boolean doubleAces) {
        if(das==true)
            this.doubleAces = doubleAces;
    }

    public boolean getDas() { return das;}

    public void setDas(boolean flag) {
        das=flag;
        if(das==false)
            doubleAces=false;
    }

    public int getDecks() { return decks;}

    public void setDecks(int d) {
        if(d<1)
            d=8;
        else if(d>8)
            d=1;

        decks=d;
    }

    public int getPenetration() { return penetration;}

    public void setPenetration(int p) {
        if(p<0)
            p+=101;
        else if(p>100)
            p-=101;

        penetration=p;
    }

    public double getBlackjackPays() { return  blackjackPays;}

    public String getBlackjackPaysString() {
        if(blackjackPays==1.5)
            return "3:2";
        else if(blackjackPays==1.4)
            return "7:5";
        else if(blackjackPays==1.2)
            return "6:5";
        else
            return "1:1";
    }

    public void leftBlackjackPays() {
        if(blackjackPays==1.5)
            blackjackPays=1;
        else if(blackjackPays==1.4)
            blackjackPays=1.5;
        else
            blackjackPays+=.2;
    }

    public void rightBlackjackPays() {
        if(blackjackPays==1)
            blackjackPays=1.5;
        else if(blackjackPays==1.5)
            blackjackPays=1.4;
        else
            blackjackPays-=.2;
    }

    public void setBlackjackPays(double d) { blackjackPays=d;}

    public int getSplitHands() { return split;}

    public void setSplitHands(int i) {
        if(i<2)
            i=4;
        else if(i==5)
            i=2;

        split=i;
    }

    public int getDoubleDown() { return doubleDown;}

    public void setDoubleDown(int i) {
        if(i>3)
            i=1;
        else if(i<1)
            i=3;
        doubleDown=i;
    }

    public TextureRegion getDoubleDownFont() {
        if(doubleDown==1)
            return Assets.anyTwo;
        else if(doubleDown==2)
            return Assets.nineToEleven;
        else
            return Assets.tenToEleven;
    }

    public int getDealer() { return dealer;}

    public String getDealerString() {
        if(dealer==-1)
            return "HITS";
        else
            return "STANDS";
    }

    public void reverseDealer() { dealer=-dealer;}

    public TextureRegion getDealerAction() {
        if(dealer==-1)
            return Assets.settingsHit;
        else
            return Assets.settingsStand;
    }

    public boolean getPerfectPairs() { return perfectPairs;}

    public boolean getTwentyOneV1() { return twentyOneV1;}

    public boolean getTwentyOneV2() { return twentyOneV2;}

    public void setPerfectPairs(boolean flag) {
        if(decks<2)
            flag=false;
        perfectPairs=flag;}

    public void setTwentyOneV1(boolean flag) {
        if(flag==true)
            twentyOneV2=false;
        twentyOneV1=flag;
    }

    public void setTwentyOneV2(boolean flag) {
        if(decks<3) {
            if(flag==true)
                twentyOneV1=true;
            flag = false;
        }
        if(flag==true)
            twentyOneV1=false;

        twentyOneV2=flag;
    }

    public void checkSideBets() {
        setPerfectPairs(perfectPairs);
        setTwentyOneV1(twentyOneV1);
        setTwentyOneV2(twentyOneV2);

    }


    public int getSound() { return sound;}

    public void setSound(int s) { sound=s;}

    public void loadSettings() {
        decksTemp=decks;
        penetrationTemp=penetration;
        backgroundTemp=background;
        dealerTemp=dealer;
        splitTemp=split;
        doubleDownTemp=doubleDown;
        blackjackPaysTemp=blackjackPays;
        csmTemp=csm;
        insuranceTemp=insurance;
        surrenderTemp=surrender;
        resplitTemp=resplit;
        hitAcesTemp=hitAces;
        doubleAcesTemp=doubleAces;
        dasTemp=das;
        perfectPairsTemp=perfectPairs;
        twentyOneV1Temp=twentyOneV1;
        twentyOneV2Temp=twentyOneV2;
    }

    public void revertSettings() {
        decks=decksTemp;
        penetration=penetrationTemp;
        background=backgroundTemp;
        loadBackgroundTexture(background);
        dealer=dealerTemp;
        split=splitTemp;
        doubleDown=doubleDownTemp;
        blackjackPays=blackjackPaysTemp;
        csm=csmTemp;
        insurance=insuranceTemp;
        surrender=surrenderTemp;
        resplit=resplitTemp;
        hitAces=hitAcesTemp;
        doubleAces=doubleAcesTemp;
        das=dasTemp;
        perfectPairs=perfectPairsTemp;
        twentyOneV1=twentyOneV1Temp;
        twentyOneV2=twentyOneV2Temp;
    }

    public void resetStats() {
        playerBlackjacks=0;
        dealerBlackjacks=0;
        handsPlayed=0;
        handsWon=0;
        handsLost=0;
        moneyBet=0;
        moneyWon=0;
        moneyLost=0;
        doubleDownTotal=0;
        doubleDownWon=0;
        doubleDownLost=0;
        splitsTotal=0;
        insuranceTotal=0;
        insuranceWon=0;
        surrenderTotal=0;
        perfectPairsTotal=0;
        perfectPairsWon=0;
        perfectPairsIncome=0;
        twentyOneTotal=0;
        twentyOneWon=0;
        twentyOneV1Income=0;
        twentyOneV2Income=0;
    }

    public boolean isDeckShuffleNeeded() {
        if(decksTemp==decks && penetrationTemp==penetration && csmTemp==csm)
            return false;
        else
            return true;
    }
    
    public boolean wereSettingsChanged() {
        if(decks==decksTemp && penetration==penetrationTemp && background==backgroundTemp && dealer==dealerTemp && split==splitTemp && doubleDown==doubleDownTemp &&
                blackjackPays==blackjackPaysTemp && csm==csmTemp && insurance==insuranceTemp && surrender==surrenderTemp && resplit==resplitTemp && hitAces==hitAcesTemp &&
                doubleAces==doubleAcesTemp && das==dasTemp && perfectPairs==perfectPairsTemp && twentyOneV1==twentyOneV1Temp && twentyOneV2==twentyOneV2Temp)
            return false;
        else
            return true;
    }

    public void loadBackgroundTexture(int b) {
        switch(b) {
            case 1:
                backgroundTexture=Assets.green;
                break;
            case 2:
                backgroundTexture=Assets.blue;
                break;
            case 3:
                backgroundTexture=Assets.red;
                break;
            case 4:
                backgroundTexture=Assets.purple;
                break;
        }
    }

    public void loadData() {
        loadBackgroundTexture(background);
        deckLeft= new Button(45,60,290,866,Assets.menuArrowLeft);
        deckRight = new Button(45,60,472,866,Assets.menuArrowRight);

        penLeft = new Button(45,60,290,776,Assets.menuArrowLeft);
        penRight = new Button(45,60,472,776,Assets.menuArrowRight);

        blackjackLeft= new Button(45,60,290,866,Assets.menuArrowLeft);
        blackjackRight= new Button(45,60,472,866,Assets.menuArrowRight);

        splitLeft = new Button(45,60,290,650,Assets.menuArrowLeft);
        splitRight = new Button(45,60,472,650,Assets.menuArrowRight);

        doubleLeft = new Button(45,60,290,425,Assets.menuArrowLeft);
        doubleRight = new Button(45,60,472,425,Assets.menuArrowRight);
    }
}
