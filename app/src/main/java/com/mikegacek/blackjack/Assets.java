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
    public static Texture hit;
    public static Texture stand;
    public static Texture doub;
    public static Texture surr;
    public static Texture split;
    public static Texture sideBetLeft;
    public static Texture sideBetRight;
    public static Texture bet;
    public static Texture buttons;
    public static Texture numbers;
    public static Texture highlight;
    public static Texture payouts;
    public static Texture particle;
    public static Texture slotMachine;
    public static Texture slotMachineNumbers;
    public static Texture messages;
    public static Texture menu;
    public static Texture settingsFont;

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
    public static TextureRegion sbl;
    public static TextureRegion sbr;
    public static TextureRegion mainBet;
    public static TextureRegion addChips;
    public static TextureRegion removeChips;
    public static TextureRegion check;
    public static TextureRegion uncheck;
    public static TextureRegion dealUnpressed;
    public static TextureRegion repeatBet;
    public static TextureRegion previousArrow;
    public static TextureRegion payoutPP;
    public static TextureRegion payout3v1;
    public static TextureRegion payout3v2;
    public static TextureRegion p;
    public static TextureRegion slot;
    public static TextureRegion leftArrow;
    public static TextureRegion rightArrow;
    public static TextureRegion slotNumbers;
    public static TextureRegion arrow;
    public static TextureRegion arrowEnd;
    public static TextureRegion slotArmConnection;
    public static TextureRegion slotArm;
    public static TextureRegion slotHandle;
    public static TextureRegion messageAddChips;
    public static TextureRegion messageRemoveChips;
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

    public static void load(GLGame game) {


        background = new Texture(game, "background.png");
        green = new TextureRegion(background, 0, 0, 540, 960);
        blue = new TextureRegion(background, 540, 0, 540, 960);
        red = new TextureRegion(background, 1080, 0, 540, 960);
        purple = new TextureRegion(background, 1620, 0, 540, 960);

        settingsFont = new Texture(game, "settingsFont2.png");
        settingsHit = new TextureRegion(settingsFont, 0, 64, 82, 42);
        settingsStand = new TextureRegion(settingsFont, 98, 64, 169, 42);
        anyTwo = new TextureRegion(settingsFont, 284, 64, 153, 42);
        nineToEleven = new TextureRegion(settingsFont, 0, 112, 265, 42);
        tenToEleven = new TextureRegion(settingsFont, 286, 112, 294, 42);

        edgeBackground = new Texture(game, "edge.png");
        edge = new TextureRegion(edgeBackground, 0, 0, 540, 960);

        ins = new Texture(game, "insurance.png");
        insurance = new TextureRegion(ins, 0, 0, 540, 960);

        chips = new Texture(game, "chips.png");
        chip1 = new TextureRegion(chips, 0, 0, 67, 67);
        chip5 = new TextureRegion(chips, 67, 0, 67, 67);
        chip25 = new TextureRegion(chips, 134, 0, 67, 67);
        chip100 = new TextureRegion(chips, 201, 0, 67, 67);
        chip500 = new TextureRegion(chips, 268, 0, 67, 67);
        chip1000 = new TextureRegion(chips, 335, 0, 67, 67);
        chip10000 = new TextureRegion(chips, 402, 0, 67, 67);

        cards = new Texture(game, "card.png");
        back = new TextureRegion(cards, 0, 560, 100, 140);

        hit = new Texture(game, "hit.png");
        hitButton = new TextureRegion(hit, 0, 0, 150, 50);

        stand = new Texture(game, "stand.png");
        standButton = new TextureRegion(stand, 0, 0, 150, 50);

        doub = new Texture(game, "double.png");
        doubleButton = new TextureRegion(doub, 0, 0, 150, 100);

        split = new Texture(game, "split.png");
        splitButton = new TextureRegion(split, 0, 0, 150, 100);

        surr = new Texture(game, "surrender.png");
        surrenderButton = new TextureRegion(surr, 0, 0, 150, 100);

        sideBetLeft = new Texture(game, "sideBetLeft.png");
        sideBetRight = new Texture(game, "sideBetRight.png");
        bet = new Texture(game, "bet.png");
        sbl = new TextureRegion(sideBetLeft, 0, 0, 92, 147);
        sbr = new TextureRegion(sideBetRight, 0, 0, 92, 147);
        mainBet = new TextureRegion(bet, 0, 0, 151, 151);

        buttons = new Texture(game, "buttons.png");
        addChips = new TextureRegion(buttons, 0, 0, 64, 64);
        removeChips = new TextureRegion(buttons, 64, 0, 64, 64);
        repeatBet = new TextureRegion(buttons, 128, 0, 64, 64);
        dealUnpressed = new TextureRegion(buttons, 0, 64, 272, 103);
        previousArrow = new TextureRegion(buttons, 192, 0, 64, 64);
        check = new TextureRegion(buttons, 0, 174, 75, 75);
        uncheck = new TextureRegion(buttons, 75, 174, 75, 75);
        settingsButton = new TextureRegion(buttons, 0, 249, 191, 52);
        freeChipsButton = new TextureRegion(buttons, 0, 301, 191, 52);
        rulesButton = new TextureRegion(buttons, 0, 353, 191, 52);
        soundOnButton = new TextureRegion(buttons, 0, 405, 191, 52);
        soundOffButton = new TextureRegion(buttons, 0, 457, 191, 52);
        menuButton = new TextureRegion(buttons, 0, 509, 88, 23);
        menuArrowRight = new TextureRegion(buttons, 278, 66, 45, 60);
        menuArrowLeft = new TextureRegion(buttons, 278, 126, 45, 60);
        radioButtonOn = new TextureRegion(buttons, 150, 174, 40, 40);
        freeChips = new TextureRegion(buttons, 0, 539, 272, 103);
        hint = new TextureRegion(buttons, 190, 170, 64, 64);
        play = new TextureRegion(buttons, 256, 0, 64, 64);

        numbers = new Texture(game, "numbers.png");
        highlight = new Texture(game, "highlight.png");

        payouts = new Texture(game, "payouts.png");
        payoutPP = new TextureRegion(payouts, 0, 177, 125, 56);
        payout3v1 = new TextureRegion(payouts, 0, 97, 142, 76);
        payout3v2 = new TextureRegion(payouts, 0, 0, 159, 95);

        particle = new Texture(game, "particle.png");
        p = new TextureRegion(particle, 0, 0, 5, 5);

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

        messages = new Texture(game, "messages.png");
        messageAddChips = new TextureRegion(messages, 0, 0, 272, 33);
        messageRemoveChips = new TextureRegion(messages, 0, 33, 332, 34);

        menu = new Texture(game, "menu.png");
        deckMenu = new TextureRegion(menu, 0, 0, 540, 960);
        gameplayMenu = new TextureRegion(menu, 540, 0, 540, 960);
        sidebetMenu = new TextureRegion(menu, 1080, 0, 540, 960);

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
        hit.reload();
        stand.reload();
        doub.reload();
        surr.reload();
        split.reload();
        sideBetLeft.reload();
        sideBetRight.reload();
        bet.reload();
        buttons.reload();
        numbers.reload();
        highlight.reload();
        payouts.reload();
        particle.reload();
        slotMachine.reload();
        slotMachineNumbers.reload();
        messages.reload();
        menu.reload();
        settingsFont.reload();


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
