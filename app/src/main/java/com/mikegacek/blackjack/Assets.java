package com.mikegacek.blackjack;

import com.mikegacek.blackjack.framework.Sound;
import com.mikegacek.blackjack.framework.gl.Texture;
import com.mikegacek.blackjack.framework.gl.TextureRegion;
import com.mikegacek.blackjack.framework.impl.GLGame;

import java.io.Serializable;

/**
 * Created by Michael on 11/16/2015.
 */
public class Assets implements Serializable{

    public static Texture background;
    public static Texture edgeBackground;
    public static Texture ins;
    public static Texture cards;
    public static Texture chips;
    public static Texture sideBet;
    public static Texture bet;
    public static Texture buttons;
    public static Texture numbers;
    public static Texture highlight;
    public static Texture payouts;
    public static Texture slotMachine;
    public static Texture slotMachineNumbers;
    public static Texture menu;
    public static Texture settingsFont;
    public static Texture calibriText;
    public static Texture shoeMeter;

    public static TextureRegion green;
    public static TextureRegion blue;
    public static TextureRegion red;
    public static TextureRegion purple;
    public static TextureRegion edge;
    public static TextureRegion insurance;
    public static TextureRegion back;
    public static TextureRegion chip1,chip5,chip25,chip100,chip500,chip1000,chip10000;
    public static TextureRegion hitButton;
    public static TextureRegion standButton;
    public static TextureRegion doubleButton;
    public static TextureRegion splitButton;
    public static TextureRegion surrenderButton;
    public static TextureRegion dealButton;
    public static TextureRegion sideBetCircle;
    public static TextureRegion mainBet;
    public static TextureRegion addChips;
    public static TextureRegion removeChips;
    public static TextureRegion yes;
    public static TextureRegion no;
    public static TextureRegion doubleBetButton;
    public static TextureRegion repeatBetButton;
    public static TextureRegion previousArrow;
    public static TextureRegion payoutPP;
    public static TextureRegion payout3v1;
    public static TextureRegion payout3v2;
    public static TextureRegion slot;
    public static TextureRegion leftArrow;
    public static TextureRegion rightArrow;
    public static TextureRegion slotNumbers;
    public static TextureRegion arrow;
    public static TextureRegion arrowEnd;
    public static TextureRegion slotArmConnection;
    public static TextureRegion slotArm;
    public static TextureRegion slotHandle;
    public static TextureRegion settingsButton;
    public static TextureRegion freeChipsButton;
    public static TextureRegion rulesButton;
    public static TextureRegion soundOnButton;
    public static TextureRegion soundOffButton;
    public static TextureRegion menuButton;
    public static TextureRegion menuArrowRight;
    public static TextureRegion menuArrowLeft;
    public static TextureRegion deckMenu;
    public static TextureRegion gameplayMenu;
    public static TextureRegion sidebetMenu;
    public static TextureRegion radioButtonOn;
    public static TextureRegion settingsHit;
    public static TextureRegion settingsStand;
    public static TextureRegion anyTwo;
    public static TextureRegion nineToEleven;
    public static TextureRegion tenToEleven;
    public static TextureRegion freeChips;
    public static TextureRegion hint;
    public static TextureRegion play;
    public static TextureRegion highlightLeft;
    public static TextureRegion highlightRight;
    public static TextureRegion highlightMiddle;
    public static TextureRegion highlightGreen;
    public static TextureRegion highlightRed;
    public static TextureRegion exit;

    public static TextureRegion meterText;
    public static TextureRegion meterDecks;
    public static TextureRegion meter;
    public static TextureRegion meterStop;
    public static TextureRegion meterBackground;
    public static TextureRegion meterFilled;

    public static Sound slotMachineSpinner;
    public static Sound winningBet;
    public static Sound multiChips;
    public static Sound singleChip1;
    public static Sound singleChip2;
    public static Sound singleChip3;
    public static Sound push;
    public static Sound shuffle;
    public static Sound largeStackOfChips;
    public static Sound dealCard;
    public static Sound button;



    public static Texture tableSettings;
    public static TextureRegion blackjackPayout;
    public static TextureRegion dealerSetting;

    public static void load(GLGame game) {

        calibriText= new Texture(game,"calibriText.png");

        tableSettings=new Texture(game,"tableSettings.png");
        blackjackPayout=new TextureRegion(tableSettings,0,0,817,54); // this is for 3:2 payout
        dealerSetting = new TextureRegion(tableSettings,0,390,1080,174);// this is for dealer stand on all 17

        background = new Texture(game, "redBackground.png");
        green = new TextureRegion(background, 0, 0, 1080, 1920);
        blue = new TextureRegion(background, 0, 0, 1080, 1920);
        red = new TextureRegion(background, 0, 0, 1080, 1920);
        purple = new TextureRegion(background, 0, 0, 1080, 1920);

        settingsFont = new Texture(game, "settingsFont2.png");
        settingsHit = new TextureRegion(settingsFont, 0, 64, 82, 42);
        settingsStand = new TextureRegion(settingsFont, 98, 64, 169, 42);
        anyTwo = new TextureRegion(settingsFont, 284, 64, 153, 42);
        nineToEleven = new TextureRegion(settingsFont, 0, 112, 265, 42);
        tenToEleven = new TextureRegion(settingsFont, 286, 112, 294, 42);

        edgeBackground = new Texture(game, "edge.png");
        edge = new TextureRegion(edgeBackground, 0, 0, 1080, 200);

        ins = new Texture(game, "insurance.png");
        insurance = new TextureRegion(ins, 0, 0, 986, 383);
        yes = new TextureRegion(ins,0,383,150,82);
        no = new TextureRegion(ins,150,383,133,82);

        chips = new Texture(game, "chips.png");
        chip1 = new TextureRegion(chips, 0, 0, 128,128);
        chip5 = new TextureRegion(chips, 128, 0, 128, 128);
        chip25 = new TextureRegion(chips, 256, 0, 128, 128);
        chip100 = new TextureRegion(chips, 384, 0, 128, 128);
        chip500 = new TextureRegion(chips, 512, 0, 128, 128);
        chip1000 = new TextureRegion(chips, 640, 0, 128, 128);
        chip10000 = new TextureRegion(chips, 768, 0, 128, 128);
        highlightGreen= new TextureRegion(chips,0,256,146,146);
        highlightRed = new TextureRegion(chips,146,256,146,146);

        cards = new Texture(game, "card.png");
        back = new TextureRegion(cards, 0, 840, 150, 210);

        sideBet= new Texture(game,"sideBet.png");
        bet = new Texture(game, "bet.png");
        sideBetCircle= new TextureRegion(sideBet,0,0,146,146);
        mainBet = new TextureRegion(bet, 0, 0, 281, 281);

        buttons = new Texture(game, "buttons.png");
        dealButton = new TextureRegion(buttons,0,0,756,277);
        freeChipsButton = new TextureRegion(buttons,0,277,756,277);
        hitButton = new TextureRegion(buttons,0,554,363,109);
        standButton = new TextureRegion(buttons,0,663,363,109);
        doubleButton = new TextureRegion(buttons,0,772,363,145);
        splitButton = new TextureRegion(buttons,0,917,363,145);
        surrenderButton = new TextureRegion(buttons,0,1062,363,145);
        doubleBetButton = new TextureRegion(buttons,0,1207,363,109);
        repeatBetButton = new TextureRegion(buttons,0,1316,363,109);
        addChips = new TextureRegion(buttons, 483, 554, 252, 122);
        removeChips = new TextureRegion(buttons, 483, 676,252, 122);
        previousArrow = new TextureRegion(buttons, 363, 798, 128, 128);

        settingsButton = new TextureRegion(buttons, 0, 249, 191, 52);
        freeChipsButton = new TextureRegion(buttons, 0, 301, 191, 52);
        rulesButton = new TextureRegion(buttons, 0, 353, 191, 52);
        soundOnButton = new TextureRegion(buttons, 0, 405, 191, 52);
        soundOffButton = new TextureRegion(buttons, 0, 457, 191, 52);
        menuArrowRight = new TextureRegion(buttons, 278, 66, 45, 60);
        menuArrowLeft = new TextureRegion(buttons, 278, 126, 45, 60);
        radioButtonOn = new TextureRegion(buttons, 150, 174, 40, 40);
        freeChips = new TextureRegion(buttons, 0, 539, 272, 103);

        exit=new TextureRegion(buttons,297,1425,99,99);
        menuButton=new TextureRegion(buttons,198,1425,99,99);
        hint = new TextureRegion(buttons, 99, 1425, 99, 99);
        play = new TextureRegion(buttons, 0, 1425, 99, 99);

        numbers = new Texture(game, "numbers.png");
        highlight = new Texture(game, "highlight.png");
        highlightLeft= new TextureRegion(highlight,0,0,12,70);
        highlightRight = new TextureRegion(highlight,58,0,12,70);
        highlightMiddle = new TextureRegion(highlight,32,0,1,70);

        payouts = new Texture(game, "payouts.png");
        payoutPP = new TextureRegion(payouts, 0, 177, 125, 56);
        payout3v1 = new TextureRegion(payouts, 0, 97, 142, 76);
        payout3v2 = new TextureRegion(payouts, 0, 0, 159, 95);

        slotMachine = new Texture(game, "slotMachine.png");
        slot = new TextureRegion(slotMachine, 0, 0, 335, 240);
        leftArrow = new TextureRegion(slotMachine, 0, 240, 70, 42);
        rightArrow = new TextureRegion(slotMachine, 42, 240, 70, 42);
        arrow = new TextureRegion(slotMachine, 334, 228, 64, 71);
        arrowEnd = new TextureRegion(slotMachine, 348, 203, 36, 22);
        slotArmConnection = new TextureRegion(slotMachine, 352, 0, 46, 58);
        slotArm = new TextureRegion(slotMachine, 335, 0, 18, 143);
        slotHandle = new TextureRegion(slotMachine, 335, 143, 56, 56);

        slotMachineNumbers = new Texture(game, "SlotMachineNumbers.png");
        slotNumbers = new TextureRegion(slotMachineNumbers, 0, 0, 225, 107);

        menu = new Texture(game, "menu.png");
        deckMenu = new TextureRegion(menu, 0, 0, 540, 960);
        gameplayMenu = new TextureRegion(menu, 540, 0, 540, 960);
        sidebetMenu = new TextureRegion(menu, 1080, 0, 540, 960);

        shoeMeter = new Texture(game,"shoeMeter.png");
        meterText = new TextureRegion(shoeMeter,0,0,300,41);
        meterDecks = new TextureRegion(shoeMeter,0,41,28,40); //Default 8
        meter = new TextureRegion(shoeMeter,0,81,324,54);
        meterBackground = new TextureRegion(shoeMeter,6,135,1,42);
        meterStop = new TextureRegion(shoeMeter,0,135,3,42);
        meterFilled = new TextureRegion(shoeMeter,12,135,70,42);

        slotMachineSpinner = game.getAudio().newSound("SlotMachineSpinner.wav");
        winningBet=game.getAudio().newSound("WinningBet3.wav");
        push = game.getAudio().newSound("WinningBet2.wav");
        multiChips=game.getAudio().newSound("LosingChips.wav");
        singleChip1=game.getAudio().newSound("BetChip1.wav");
        singleChip2=game.getAudio().newSound("BetChip2.wav");
        singleChip3=game.getAudio().newSound("BetChip3.wav");
        shuffle=game.getAudio().newSound("Shuffle.wav");
        largeStackOfChips=game.getAudio().newSound("LargeStackOfChips.wav");
        dealCard=game.getAudio().newSound("DealCard.wav");
        button=game.getAudio().newSound("Button.wav");
    }

    public static void reload() {
        background.reload();
        edgeBackground.reload();
        ins.reload();
        cards.reload();
        chips.reload();
        sideBet.reload();
        bet.reload();
        buttons.reload();
        numbers.reload();
        highlight.reload();
        payouts.reload();
        slotMachine.reload();
        slotMachineNumbers.reload();
        menu.reload();
        settingsFont.reload();
        calibriText.reload();
    }

    public static void reloadSound(GLGame game) {
        slotMachineSpinner.dispose();
        winningBet.dispose();
        push.dispose();
        multiChips.dispose();
        singleChip1.dispose();
        singleChip2.dispose();
        singleChip3.dispose();
        shuffle.dispose();
        largeStackOfChips.dispose();
        dealCard.dispose();
        button.dispose();

        slotMachineSpinner = game.getAudio().newSound("SlotMachineSpinner.wav");
        winningBet=game.getAudio().newSound("WinningBet3.wav");
        push = game.getAudio().newSound("WinningBet2.wav");
        multiChips=game.getAudio().newSound("LosingChips.wav");
        singleChip1=game.getAudio().newSound("BetChip1.wav");
        singleChip2=game.getAudio().newSound("BetChip2.wav");
        singleChip3=game.getAudio().newSound("BetChip3.wav");
        shuffle=game.getAudio().newSound("Shuffle.wav");
        largeStackOfChips=game.getAudio().newSound("LargeStackOfChips.wav");
        dealCard=game.getAudio().newSound("DealCard.wav");
        button=game.getAudio().newSound("Button.wav");
    }
}
