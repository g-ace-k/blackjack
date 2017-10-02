package com.mikegacek.blackjack;

import java.io.Serializable;

/**
 * Created by Michael on 10/1/2017.
 */

public class StatisticsManager implements Serializable {

    private int playerBlackjacks,dealerBlackjacks,handsPlayed,handsWon,handsLost,moneyBet,moneyWon,moneyLost;
    private int doubleDownTotal,doubleDownWon,doubleDownLost,splitsTotal,insuranceTotal,insuranceWon,surrenderTotal;
    private int perfectPairsTotal,perfectPairsWon,perfectPairsIncome,twentyOneTotal,twentyOneWon,twentyOneV1Income,twentyOneV2Income;

    public StatisticsManager() {
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

    public int getHandsPlayed() {
        return handsPlayed;
    }

    public void setHandsPlayed(int handsPlayed) {
        this.handsPlayed = handsPlayed;
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

    public int getSplitsTotal() {
        return splitsTotal;
    }

    public void setSplitsTotal(int splitsTotal) {
        this.splitsTotal = splitsTotal;
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

    public int getTwentyOneV2Income() {
        return twentyOneV2Income;
    }

    public void setTwentyOneV2Income(int twentyOneV2Income) {
        this.twentyOneV2Income = twentyOneV2Income;
    }
}
