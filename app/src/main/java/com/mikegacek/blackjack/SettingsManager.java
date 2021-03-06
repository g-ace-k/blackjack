package com.mikegacek.blackjack;

import java.io.Serializable;

/**
 * Created by Michael on 9/27/2017.
 */

public class SettingsManager implements Serializable{

    private static final long serialVersionUID = 16L;

    private Slider deckPenetration;
    private Toggle CSMToggle,insuranceToggle,surrenderToggle,resplitAcesToggle,hitSplitAcesToggle,doubleSplitAcesToggle,doubleAfterSplitToggle;
    private Button deck1,deck2,deck3,deck4,deck5,deck6,deck7,deck8,blackjackPays32,blackjackPays75,blackjackPays65,blackjackPays11,dealerStand,dealerHit,split2,split3,split4,doubleAny2,double911,double1011,exitSettings;
    private Button listButton,sideBet1Arrow,sideBet2Arrow;

    private int decks,penetration,dealer,split,doubleDown,sound;
    private double blackjackPays;
    private boolean csm,insurance,surrender,resplit,hitAces,doubleAces,das;

    private int decksTemp,penetrationTemp,dealerTemp,splitTemp,doubleDownTemp,sideBetLeftIdTemp,sideBetRightIdTemp,sideBetLeftVersionTemp,sideBetRightVersionTemp;
    private double blackjackPaysTemp;
    private boolean csmTemp,insuranceTemp,surrenderTemp,resplitTemp,hitAcesTemp,doubleAcesTemp,dasTemp;

    private boolean newDeck,changeSideBets;
    //list location 0=off 1= side bet 1 2= side bet 2
    private int listLocation;

    public SettingsManager() {
        //Add buttons,toggles and slider here

        deckPenetration= new Slider(63,128,700,1498,140,940);
        CSMToggle = new Toggle(91,47,916,1675);
        insuranceToggle = new Toggle(91,47,916,1193);
        surrenderToggle = new Toggle(91,47,916,1099);
        resplitAcesToggle = new Toggle(91,47,916,817);
        hitSplitAcesToggle = new Toggle(91,47,916,723);
        doubleSplitAcesToggle = new Toggle(91,47,916,629);
        doubleAfterSplitToggle = new Toggle(91,47,916,535);

        deck1 = new Button(71,59,324,1769,Assets.settingsSmallHighlight);
        deck2 = new Button(71,59,410,1769,Assets.settingsSmallHighlight);
        deck3 = new Button(71,59,496,1769,Assets.settingsSmallHighlight);
        deck4 = new Button(71,59,582,1769,Assets.settingsSmallHighlight);
        deck5 = new Button(71,59,668,1769,Assets.settingsSmallHighlight);
        deck6 = new Button(71,59,754,1769,Assets.settingsSmallHighlight);
        deck7 = new Button(71,59,840,1769,Assets.settingsSmallHighlight);
        deck8 = new Button(71,59,926,1769,Assets.settingsSmallHighlight);

        blackjackPays32 = new Button(71,59,668,1288,Assets.settingsSmallHighlight);
        blackjackPays75 = new Button(71,59,754,1288,Assets.settingsSmallHighlight);
        blackjackPays65 = new Button(71,59,840,1288,Assets.settingsSmallHighlight);
        blackjackPays11 = new Button(71,59,926,1288,Assets.settingsSmallHighlight);

        dealerStand = new Button(123,59,762,1006,Assets.settingsLargeHighlight);
        dealerHit = new Button(123,59,900,1006,Assets.settingsLargeHighlight);

        split2 = new Button(71,59,754,911,Assets.settingsSmallHighlight);
        split3 = new Button(71,59,840,911,Assets.settingsSmallHighlight);
        split4 = new Button(71,59,926,911,Assets.settingsSmallHighlight);

        doubleAny2 = new Button(123,59,624,442,Assets.settingsLargeHighlight);
        double911 = new Button(123,59,762,442,Assets.settingsLargeHighlight);
        double1011 = new Button(123,59,900,442,Assets.settingsLargeHighlight);

        exitSettings = new Button(608,115,540,62,Assets.settingsExitButton);

        decks=8;
        deck8.setOn(true);
        penetration=60;
        blackjackPays=1.5;
        blackjackPays32.setOn(true);
        //dealer 1=stand -1=hit
        dealer=1;
        dealerStand.setOn(true);
        //1=any 2 2=9-11 only 3=10-11 only
        doubleDown=1;
        doubleAny2.setOn(true);
        sound=1;
        split=4;
        split4.setOn(true);

        csm=false;
        insurance=true;
        insuranceToggle.setOn(true);
        surrender=true;
        surrenderToggle.setOn(true);
        resplit=false;
        hitAces=false;
        doubleAces=false;
        das=true;
        doubleAfterSplitToggle.setOn(true);
        sideBetLeftIdTemp=1;
        sideBetLeftVersionTemp=1;
        sideBetRightIdTemp=2;
        sideBetRightVersionTemp=2;

        newDeck=false;
        changeSideBets=false;

        listLocation=0;
        listButton = new Button(317,458,817,471,Assets.listButton);
        sideBet1Arrow= new Button(35,35,943,262,Assets.settingsOrangeArrow);
        sideBet2Arrow= new Button(35,35,943,168,Assets.settingsOrangeArrow);
    }

    public Slider getDeckPenetration() {
        return deckPenetration;
    }

    public Toggle getCSMToggle() {
        return CSMToggle;
    }

    public Toggle getInsuranceToggle() {
        return insuranceToggle;
    }

    public Toggle getSurrenderToggle() {
        return surrenderToggle;
    }

    public Toggle getResplitAcesToggle() {
        return resplitAcesToggle;
    }

    public Toggle getHitSplitAcesToggle() {
        return hitSplitAcesToggle;
    }

    public Toggle getDoubleSplitAcesToggle() { return doubleSplitAcesToggle;}

    public Toggle getDoubleAfterSplitToggle() {
        return doubleAfterSplitToggle;
    }

    public Button getDeck1() {
        return deck1;
    }

    public Button getDeck2() {
        return deck2;
    }

    public Button getDeck3() {
        return deck3;
    }

    public Button getDeck4() {
        return deck4;
    }

    public Button getDeck5() {
        return deck5;
    }

    public Button getDeck6() {
        return deck6;
    }

    public Button getDeck7() {
        return deck7;
    }

    public Button getDeck8() {
        return deck8;
    }

    public Button getBlackjackPays32() {
        return blackjackPays32;
    }

    public Button getBlackjackPays75() {
        return blackjackPays75;
    }

    public Button getBlackjackPays65() {
        return blackjackPays65;
    }

    public Button getBlackjackPays11() {
        return blackjackPays11;
    }

    public Button getDealerStand() {
        return dealerStand;
    }

    public Button getDealerHit() {
        return dealerHit;
    }

    public Button getSplit2() {
        return split2;
    }

    public Button getSplit3() {
        return split3;
    }

    public Button getSplit4() {
        return split4;
    }

    public Button getDoubleAny2() {
        return doubleAny2;
    }

    public Button getDouble911() {
        return double911;
    }

    public Button getDouble1011() {
        return double1011;
    }

    public Button getExitSettings() {
        return exitSettings;
    }

    public Button getSideBet1Arrow() {
        return sideBet1Arrow;
    }

    public Button getSideBet2Arrow() {
        return sideBet2Arrow;
    }

    public Button getListButton() {
        return listButton;
    }

    public int getListLocation() {
        return listLocation;
    }

    public void setListLocation(int listLocation) {
        if(listLocation==1) {
            listButton.setPos(817,471);
        }
        else if(listLocation==2) {
            listButton.setPos(817,377);
        }
        this.listLocation=listLocation;
    }

    public boolean getCSM() {
        return csm;
    }

    public int getPenetration() {
        return penetration;
    }

    public int getDecks() {
        return decks;
    }

    public void setDecks(int decks) {
        this.decks = decks;
    }

    public void setPenetration(int penetration) {
        this.penetration = penetration;
    }

    public int getDealer() {
        return dealer;
    }

    public void setDealer(int dealer) {
        this.dealer = dealer;
    }

    public int getSplit() {
        return split;
    }

    public void setSplit(int split) {
        this.split = split;
    }

    public int getDoubleDown() {
        return doubleDown;
    }

    public void setDoubleDown(int doubleDown) {
        this.doubleDown = doubleDown;
    }

    public int getSound() {
        return sound;
    }

    public void setSound(int sound) {
        this.sound = sound;
    }

    public double getBlackjackPays() {
        return blackjackPays;
    }

    public void setBlackjackPays(double blackjackPays) {
        this.blackjackPays = blackjackPays;
    }

    public boolean getCsm() {
        return csm;
    }

    public void setCsm(boolean csm) {
        this.csm = csm;
    }

    public boolean getInsurance() {
        return insurance;
    }

    public void setInsurance(boolean insurance) {
        this.insurance = insurance;
    }

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
        this.doubleAces = doubleAces;
    }

    public boolean getDas() {
        return das;
    }

    public void setDas(boolean das) {
        this.das = das;
    }

    public int getDecksTemp() {
        return decksTemp;
    }

    public void setDecksTemp(int decksTemp) {
        this.decksTemp = decksTemp;
    }

    public int getPenetrationTemp() {
        return penetrationTemp;
    }

    public void setPenetrationTemp(int penetrationTemp) {
        this.penetrationTemp = penetrationTemp;
    }

    public int getDealerTemp() {
        return dealerTemp;
    }

    public void setDealerTemp(int dealerTemp) {
        this.dealerTemp = dealerTemp;
    }

    public int getSplitTemp() {
        return splitTemp;
    }

    public void setSplitTemp(int splitTemp) {
        this.splitTemp = splitTemp;
    }

    public int getDoubleDownTemp() {
        return doubleDownTemp;
    }

    public void setDoubleDownTemp(int doubleDownTemp) {
        this.doubleDownTemp = doubleDownTemp;
    }

    public double getBlackjackPaysTemp() {
        return blackjackPaysTemp;
    }

    public void setBlackjackPaysTemp(double blackjackPaysTemp) {
        this.blackjackPaysTemp = blackjackPaysTemp;
    }

    public boolean getCsmTemp() {
        return csmTemp;
    }

    public void setCsmTemp(boolean csmTemp) {
        this.csmTemp = csmTemp;
    }

    public boolean getInsuranceTemp() {
        return insuranceTemp;
    }

    public void setInsuranceTemp(boolean insuranceTemp) {
        this.insuranceTemp = insuranceTemp;
    }

    public boolean getSurrenderTemp() {
        return surrenderTemp;
    }

    public void setSurrenderTemp(boolean surrenderTemp) {
        this.surrenderTemp = surrenderTemp;
    }

    public boolean getResplitTemp() {
        return resplitTemp;
    }

    public void setResplitTemp(boolean resplitTemp) {
        this.resplitTemp = resplitTemp;
    }

    public boolean getHitAcesTemp() {
        return hitAcesTemp;
    }

    public void setHitAcesTemp(boolean hitAcesTemp) {
        this.hitAcesTemp = hitAcesTemp;
    }

    public boolean getDoubleAcesTemp() {
        return doubleAcesTemp;
    }

    public void setDoubleAcesTemp(boolean doubleAcesTemp) {
        this.doubleAcesTemp = doubleAcesTemp;
    }

    public boolean getDasTemp() {
        return dasTemp;
    }

    public void setDasTemp(boolean dasTemp) {
        this.dasTemp = dasTemp;
    }



    public boolean getNewDeck() {
        return newDeck;
    }

    public void setNewDeck(boolean newDeck) {
        this.newDeck = newDeck;
    }

    public boolean getChangeSideBets() {
        return changeSideBets;
    }

    public void setChangeSideBets(boolean changeSideBets) {
        this.changeSideBets = changeSideBets;
    }

    public int getSideBetRightVersionTemp() {
        return sideBetRightVersionTemp;
    }

    public int getSideBetLeftIdTemp() {
        return sideBetLeftIdTemp;
    }

    public int getSideBetRightIdTemp() {
        return sideBetRightIdTemp;
    }

    public int getSideBetLeftVersionTemp() {
        return sideBetLeftVersionTemp;
    }

    public void enterSettingsCheck() {
        checkButton(deck1);
        checkButton(deck2);
        checkButton(deck3);
        checkButton(deck4);
        checkButton(deck5);
        checkButton(deck6);
        checkButton(deck7);
        checkButton(deck8);
        checkToggle(CSMToggle);
        checkSlider(deckPenetration);
        checkButton(blackjackPays32);
        checkButton(blackjackPays75);
        checkButton(blackjackPays65);
        checkButton(blackjackPays11);
        checkToggle(insuranceToggle);
        checkToggle(surrenderToggle);
        checkButton(dealerStand);
        checkButton(dealerHit);
        checkButton(split2);
        checkButton(split3);
        checkButton(split4);
        checkToggle(resplitAcesToggle);
        checkToggle(hitSplitAcesToggle);
        checkToggle(doubleSplitAcesToggle);
        checkToggle(doubleAfterSplitToggle);
        checkButton(doubleAny2);
        checkButton(double911);
        checkButton(double1011);
        listButton.setScaleY(0);
        listLocation=0;

    }

    public void checkButton(Button b) {
        if(b.getOn()==true)
            b.setRGBf(1,.416f,0);
        else
            b.setRGBf(.25f,.25f,.25f);
    }

    public void checkToggle(Toggle t) {
        if(t.getOn()==true) {
            t.setRGBf(1,.416f,0);
            t.setCircleXPos(t.getxPos()+33);
        }
        else {
            t.setRGBf(.25f,.25f,.25f);
            t.setCircleXPos(t.getxPos()-33);
        }
    }

    public void checkSlider(Slider s) {
        //0, 600, 800
        s.setxPos(penetration*(s.getBarXEnd()-s.getBarXStart())/100+s.getBarXStart());
    }

    public void loadSettings(SideBets left, SideBets right) {
        decksTemp=decks;
        penetrationTemp=penetration;
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
        sideBetLeftVersionTemp=left.getVersion();
        sideBetLeftIdTemp=left.getId();
        sideBetRightVersionTemp=right.getVersion();
        sideBetRightIdTemp=right.getId();
    }

    public void revertSettings() {
        decks=decksTemp;
        penetration=penetrationTemp;
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
        switch(decks) {
            case 1:
                revertButtons(deck1,deck2,deck3,deck4,deck5,deck6,deck7,deck8);
                break;
            case 2:
                revertButtons(deck2,deck1,deck3,deck4,deck5,deck6,deck7,deck8);
                break;
            case 3:
                revertButtons(deck3,deck2,deck1,deck4,deck5,deck6,deck7,deck8);
                break;
            case 4:
                revertButtons(deck4,deck2,deck3,deck1,deck5,deck6,deck7,deck8);
                break;
            case 5:
                revertButtons(deck5,deck2,deck3,deck4,deck1,deck6,deck7,deck8);
                break;
            case 6:
                revertButtons(deck6,deck2,deck3,deck4,deck5,deck1,deck7,deck8);
                break;
            case 7:
                revertButtons(deck7,deck2,deck3,deck4,deck5,deck6,deck1,deck8);
                break;
            case 8:
                revertButtons(deck8,deck2,deck3,deck4,deck5,deck6,deck7,deck1);
                break;
        }
        if(blackjackPays==1.5)
            revertButtons(blackjackPays32,blackjackPays75,blackjackPays65,blackjackPays11);
        else if(blackjackPays==1.4)
            revertButtons(blackjackPays75,blackjackPays32,blackjackPays65,blackjackPays11);
        else if(blackjackPays==1.2)
            revertButtons(blackjackPays65,blackjackPays75,blackjackPays32,blackjackPays11);
        else if(blackjackPays==1)
            revertButtons(blackjackPays11,blackjackPays75,blackjackPays65,blackjackPays32);

        if(dealer==1)
            revertButtons(dealerStand,dealerHit);
        else
            revertButtons(dealerHit,dealerStand);

        switch(split) {
            case 2:
                revertButtons(split2,split3,split4);
                break;
            case 3:
                revertButtons(split3,split2,split4);
                break;
            case 4:
                revertButtons(split4,split3,split2);
                break;
        }

        if(doubleDown==1) {
            revertButtons(doubleAny2,double911,double1011);
        }
        else if(doubleDown==2) {
            revertButtons(double911,doubleAny2,double1011);
        }
        else if(doubleDown==3) {
            revertButtons(double1011,doubleAny2,double911);
        }

        CSMToggle.setOn(csm);
        insuranceToggle.setOn(insurance);
        surrenderToggle.setOn(surrender);
        resplitAcesToggle.setOn(resplit);
        hitSplitAcesToggle.setOn(hitAces);
        doubleSplitAcesToggle.setOn(doubleAces);
        doubleAfterSplitToggle.setOn(das);
    }

    private void revertButtons(Button b, Button... bs) {
        b.setOn(true);
        for(Button button: bs) {
            button.setOn(false);
        }
    }

    public boolean isDeckShuffleNeeded() {
        if(decksTemp==decks && penetrationTemp==penetration && csmTemp==csm)
            return false;
        else
            return true;
    }

    public boolean wereSettingsChanged(SideBets left, SideBets right) {
        if(decks==decksTemp && penetration==penetrationTemp && dealer==dealerTemp && split==splitTemp && doubleDown==doubleDownTemp &&
                blackjackPays==blackjackPaysTemp && csm==csmTemp && insurance==insuranceTemp && surrender==surrenderTemp && resplit==resplitTemp && hitAces==hitAcesTemp &&
                doubleAces==doubleAcesTemp && das==dasTemp && sideBetLeftIdTemp==left.getId() && sideBetLeftVersionTemp==left.getVersion() && sideBetRightIdTemp==right.getId() && sideBetRightVersionTemp==right.getVersion())
            return false;
        else
            return true;
    }


    public void loadData() {
        deck1.setTextureRegion(Assets.settingsSmallHighlight);
        deck2.setTextureRegion(Assets.settingsSmallHighlight);
        deck3.setTextureRegion(Assets.settingsSmallHighlight);
        deck4.setTextureRegion(Assets.settingsSmallHighlight);
        deck5.setTextureRegion(Assets.settingsSmallHighlight);
        deck6.setTextureRegion(Assets.settingsSmallHighlight);
        deck7.setTextureRegion(Assets.settingsSmallHighlight);
        deck8.setTextureRegion(Assets.settingsSmallHighlight);

        blackjackPays32.setTextureRegion(Assets.settingsSmallHighlight);
        blackjackPays75.setTextureRegion(Assets.settingsSmallHighlight);
        blackjackPays65.setTextureRegion(Assets.settingsSmallHighlight);
        blackjackPays11.setTextureRegion(Assets.settingsSmallHighlight);

        dealerStand.setTextureRegion(Assets.settingsLargeHighlight);
        dealerHit.setTextureRegion(Assets.settingsLargeHighlight);

        split2.setTextureRegion(Assets.settingsSmallHighlight);
        split3.setTextureRegion(Assets.settingsSmallHighlight);
        split4.setTextureRegion(Assets.settingsSmallHighlight);

        doubleAny2.setTextureRegion(Assets.settingsLargeHighlight);
        double911.setTextureRegion(Assets.settingsLargeHighlight);
        double1011.setTextureRegion(Assets.settingsLargeHighlight);

        exitSettings.setTextureRegion(Assets.settingsExitButton);

        listButton.setTextureRegion(Assets.listButton);
        sideBet1Arrow.setTextureRegion(Assets.settingsOrangeArrow);
        sideBet2Arrow.setTextureRegion(Assets.settingsOrangeArrow);
    }
}
