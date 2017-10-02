package com.mikegacek.blackjack;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.mikegacek.blackjack.framework.Game;
import com.mikegacek.blackjack.framework.Input.TouchEvent;
import com.mikegacek.blackjack.framework.gl.Camera2D;
import com.mikegacek.blackjack.framework.gl.SpriteBatcher;
import com.mikegacek.blackjack.framework.impl.GLScreen;
import com.mikegacek.blackjack.framework.impl.SerializableManager;
import com.mikegacek.blackjack.framework.math.OverlapTester;
import com.mikegacek.blackjack.framework.math.Vector2;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Michael on 11/12/2015.
 */
public class MainScreen extends GLScreen {

    static final int GAME_BETTING = 0;
    static final int GAME_RUNNING = 1;
    static final int GAME_LASTHAND = 2;
    static final int GAME_MENU = 3;
    static final int GAME_SLOTMACHINE = 4;
    static final int GAME_SETTINGS = 5;

    static int sound=1;

    int state;
    Camera2D guiCam;
    SpriteBatcher batcher;
    Vector2 touchPoint;
    Random random;
    int decks;
    int waitTime;
    int sideBetWaitTime;
    GameManager gameManager;
    GameRenderer gameRenderer;
    ChipManager chipManager;
    SettingsManager settingsManager;
    StatisticsManager statisticsManager;
    SlotMachine slotMachine;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    //Test

    int test;
    float r;
    float g;
    float b;

    public MainScreen(Game game) {
        super(game);
        waitTime=180;
        sideBetWaitTime=60;
        guiCam = new Camera2D(glGraphics,1080,1920);
        batcher = new SpriteBatcher(glGraphics,2000);
        touchPoint = new Vector2();
        random = new Random();


        //Test
        test =0;
        r=1f;
        g=.416f;
        b=0;

        //loadData();

        sharedPreferences = glGame.getSharedPreferences("com.blackjack",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        state=sharedPreferences.getInt("com.blackjack.state",GAME_BETTING);

        if(sharedPreferences.getBoolean("adFinished",true)==false) {
            state=GAME_BETTING;
        }


        if(state==GAME_SETTINGS) {
            editor.putInt("disableBack",1);
            editor.commit();
        }
        else {
            editor.putInt("disableBack", 0);
            editor.commit();
        }
        //No load
        if(chipManager==null || settingsManager==null || gameManager==null || slotMachine==null || gameRenderer==null) {
            state=GAME_BETTING;
            editor.putInt("com.blackjack.state",state);
            editor.commit();
            decks=8;
            chipManager = new ChipManager();
            settingsManager = new SettingsManager();
            statisticsManager = new StatisticsManager();
            gameManager = new GameManager(decks, chipManager, settingsManager,statisticsManager);
            slotMachine = new SlotMachine(glGraphics, batcher, chipManager);
            gameRenderer = new GameRenderer(glGraphics, batcher, gameManager, chipManager, slotMachine, settingsManager);
            gameManager.shuffle();
            chipManager.setMoney(sharedPreferences.getInt("com.blackjack.money",500));
        }

    }
    @Override
    public void update(float deltaTime) {
        if(deltaTime > 0.1f)
            deltaTime = 0.1f;

        gameManager.updateDealerCards();
        gameManager.updatePlayButtons();
        gameManager.updateMenuButtons();

        chipManager.updateChips();
        chipManager.updateButtons();

        switch(state) {
            case GAME_BETTING:
                updateBetting(deltaTime);
                break;
            case GAME_RUNNING:
                updateRunning(deltaTime);
                break;
            case GAME_LASTHAND:
                updateLastHand(deltaTime);
                break;
            case GAME_MENU:
                updateMenu(deltaTime);
                break;
            case GAME_SLOTMACHINE:
                updateSlotMachine(deltaTime);
                break;
            case GAME_SETTINGS:
                updateSettings(deltaTime);
                break;
        }

    }

    public void updateBetting(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        int len= touchEvents.size();

        gameManager.updateFinishedPlayer();
        for(int i=0;i<len;i++) {
            TouchEvent event = touchEvents.get(i);
            touchPoint.set(event.x, event.y);
            guiCam.touchToWorld(touchPoint);
            if(event.type==TouchEvent.TOUCH_DRAGGED || event.type==TouchEvent.TOUCH_DOWN) {
                chipManager.getDealButton().setPressed(OverlapTester.pointInRectangle(chipManager.getDealButton(),touchPoint));
                chipManager.getRepeat().setPressed(OverlapTester.pointInRectangle(chipManager.getRepeat(),touchPoint));
                chipManager.getFreeChips().setPressed(OverlapTester.pointInRectangle(chipManager.getFreeChips(),touchPoint));
                chipManager.getDoubleBetButton().setPressed(OverlapTester.pointInRectangle(chipManager.getDoubleBetButton(),touchPoint));
            }
            if(event.type==TouchEvent.TOUCH_UP) {
                chipManager.getDealButton().setPressed(false);
                chipManager.getRepeat().setPressed(false);
                chipManager.getFreeChips().setPressed(false);
                chipManager.getDoubleBetButton().setPressed(false);
                if(OverlapTester.pointInRectangle(chipManager.getFreeChips(),touchPoint) && chipManager.getTotalMoney()==0) {
                    gameManager.cardsToDiscard();
                    Assets.button.play(sound);
                    slotMachine.reset();
                    waitTime = 180;
                    state = GAME_SLOTMACHINE;
                   //glGame.startActivity(new Intent(glGame,SlotMachineAd.class));
                }
                else if(OverlapTester.pointInRectangle(chipManager.getChip1(),touchPoint) && chipManager.getTotalMoney()!=0) {
                    Assets.button.play(sound);
                    if(chipManager.getChipLocation()!=0) {
                        chipManager.setChipSelection(1);
                        chipManager.setChipLocation(0);
                    }
                    else {
                        chipManager.setChipSelection(0);
                        chipManager.setChipLocation(-1);
                    }
                }
                else if(OverlapTester.pointInRectangle(chipManager.getChip5(), touchPoint) && chipManager.getTotalMoney()!=0) {
                    Assets.button.play(sound);
                    if(chipManager.getChipLocation()!=1) {
                        chipManager.setChipSelection(5);
                        chipManager.setChipLocation(1);
                    }
                    else {
                        chipManager.setChipSelection(0);
                        chipManager.setChipLocation(-1);
                    }
                }
                else if(OverlapTester.pointInRectangle(chipManager.getChip25(),touchPoint) && chipManager.getTotalMoney()!=0) {
                    Assets.button.play(sound);
                    if(chipManager.getChipLocation()!=2) {
                        chipManager.setChipSelection(25);
                        chipManager.setChipLocation(2);
                    }
                    else {
                        chipManager.setChipSelection(0);
                        chipManager.setChipLocation(-1);
                    }
                }
                else if(OverlapTester.pointInRectangle(chipManager.getChip100(),touchPoint) && chipManager.getTotalMoney()!=0) {
                    Assets.button.play(sound);
                    if(chipManager.getChipLocation()!=3) {
                        chipManager.setChipSelection(100);
                        chipManager.setChipLocation(3);
                    }
                    else {
                        chipManager.setChipSelection(0);
                        chipManager.setChipLocation(-1);
                    }
                }
                else if(OverlapTester.pointInRectangle(chipManager.getChip500(),touchPoint) && chipManager.getTotalMoney()!=0) {
                    Assets.button.play(sound);
                    if(chipManager.getChipLocation()!=4) {
                        chipManager.setChipSelection(500);
                        chipManager.setChipLocation(4);
                    }
                    else {
                        chipManager.setChipSelection(0);
                        chipManager.setChipLocation(-1);
                    }
                }
                else if(OverlapTester.pointInRectangle(chipManager.getChip1000(),touchPoint) && chipManager.getTotalMoney()!=0) {
                    Assets.button.play(sound);
                    if(chipManager.getChipLocation()!=5) {
                        chipManager.setChipSelection(1000);
                        chipManager.setChipLocation(5);
                    }
                    else {
                        chipManager.setChipSelection(0);
                        chipManager.setChipLocation(-1);
                    }
                }
                else if(OverlapTester.pointInRectangle(chipManager.getChip10000(),touchPoint) && chipManager.getTotalMoney()!=0) {
                    Assets.button.play(sound);
                    if(chipManager.getChipLocation()!=6) {
                        chipManager.setChipSelection(10000);
                        chipManager.setChipLocation(6);
                    }
                    else {
                        chipManager.setChipSelection(0);
                        chipManager.setChipLocation(-1);
                    }
                }
                else if(OverlapTester.pointInCircle(chipManager.getLeft(),touchPoint) && chipManager.getChipLocation()>=0 && gameManager.getSideBetLeft().getVersion()>0) {
                    if(chipManager.getDirection()==1 && chipManager.getChipLocation()>=0) {
                        chipManager.addChips(1, chipManager.getChipLocation());
                    }
                    else if(chipManager.getDirection()==-1) {
                        chipManager.removeChip(1, chipManager.getChipLocation());
                    }
                }
                else if(OverlapTester.pointInCircle(chipManager.getRight(),touchPoint) && chipManager.getChipLocation()>=0 && gameManager.getSideBetRight().getVersion()>0) {
                    if(chipManager.getDirection()==1 && chipManager.getChipLocation()>=0) {
                        chipManager.addChips(2, chipManager.getChipLocation());
                    }
                    else if(chipManager.getDirection()==-1) {
                        chipManager.removeChip(2,chipManager.getChipLocation());
                    }
                }
                else if(OverlapTester.pointInCircle(chipManager.getMiddle(),touchPoint) && chipManager.getChipLocation()>=0) {
                    if(chipManager.getDirection()==1 && chipManager.getChipLocation()>=0) {
                        chipManager.addChips(3, chipManager.getChipLocation());
                    }
                    else if(chipManager.getDirection()==-1) {
                        chipManager.removeChip(3,chipManager.getChipLocation());
                    }
                }
                else if((OverlapTester.pointInRectangle(chipManager.getBetChips(),touchPoint) || OverlapTester.pointInRectangle(chipManager.getRemoveChips(),touchPoint)) && chipManager.getTotalMoney()!=0) {
                    Assets.button.play(sound);
                    chipManager.setDirection(chipManager.getDirection()*-1);
                }
                else if(OverlapTester.pointInRectangle(chipManager.getRepeat(),touchPoint) && chipManager.getTotalMoney()!=0) {
                    if(chipManager.repeatTotal()<=chipManager.getMoney()) {
                        chipManager.repeatBet();
                        if(gameManager.getSideBetLeft().getVersion()==0)
                            chipManager.removeAllChipsFromSelection(1);
                        if(gameManager.getSideBetRight().getVersion()==0)
                            chipManager.removeAllChipsFromSelection(2);
                    }
                    else {
                        glGame.runOnUiThread(new Runnable(){
                            public void run() {
                                Toast.makeText(glGame,"Not enough chips to repeat bet.",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
                else if(OverlapTester.pointInRectangle(chipManager.getDoubleBetButton(),touchPoint)) {
                    if(chipManager.getMainBetMoney()>chipManager.getMoney())
                        chipManager.doubleBet(chipManager.getMoney());
                    else
                        chipManager.doubleBet(chipManager.getMainBetMoney());
                }
                else if (OverlapTester.pointInRectangle(gameManager.menu,touchPoint)) {
                    Assets.button.play(sound);
                    state=GAME_MENU;
                    editor.putInt("com.blackjack.state",state);
                    editor.commit();
                    gameManager.pullMenu();
                }
                else if(OverlapTester.pointInRectangle(chipManager.getDealButton(),touchPoint) && chipManager.getMainBet().size()>0) {
                    Assets.button.play(sound);
                    state=GAME_RUNNING;
                    editor.putInt("com.blackjack.state",state);
                    editor.commit();
                    waitTime=180;
                    chipManager.saveRepeat();
                    gameManager.clearCards();
                    gameManager.setState(0);
                    //maybe move this to after the previous hand finishes when the cards go to the side
                    if(((int)(gameManager.getDecks()*52*(settingsManager.getPenetration()/100f))-gameManager.getPointer())<=0 || settingsManager.getCSM()==true)
                        gameManager.shuffleDeck();
                    chipManager.updatePlayerHandMoney();
                    gameManager.addCard();
                    gameManager.addDealerCards();
                    gameManager.addCard();
                    gameManager.addDealerCards();
                    gameManager.checkSideBets();
                }
                else if(OverlapTester.pointInRectangle(chipManager.getPreviousArrow(),touchPoint) && gameManager.playerCards.size()!=0) {
                    Assets.button.play(sound);
                    state=GAME_LASTHAND;
                    editor.putInt("com.blackjack.state",state);
                    editor.commit();
                }
                chipManager.updateMoney();
            }
        }
    }

    public void updateRunning(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        int len= touchEvents.size();

        gameManager.updatePlayerCards();

        gameManager.updateGame();
        if(sideBetWaitTime>0)
            sideBetWaitTime--;
        else if(sideBetWaitTime==0) {
            chipManager.removeAllChipsFromSelection(1);
            chipManager.removeAllChipsFromSelection(2);
            sideBetWaitTime--;
        }
        if(gameManager.getState()>=6) {
            if(waitTime==180 && gameManager.getState()==6) {
                gameManager.payouts();
                gameManager.setState(8);
            }
                waitTime--;
        }
        if(waitTime==0) {
            chipManager.removeAllChips();
            state=GAME_BETTING;
            editor.putInt("com.blackjack.state",state);
            editor.commit();
            gameManager.cardsToDiscard();
            gameManager.setOldSideBetLeft(gameManager.getSideBetLeft());
            gameManager.setOldSideBetRight(gameManager.getSideBetRight());
            gameManager.getSideBetLeft().resetPayout();
            gameManager.getSideBetRight().resetPayout();
            waitTime=180;
            sideBetWaitTime=60;
        }


        for(int i=0;i<len;i++) {
            TouchEvent event = touchEvents.get(i);
            touchPoint.set(event.x,event.y);
            guiCam.touchToWorld(touchPoint);
            if(event.type==TouchEvent.TOUCH_DRAGGED || event.type==TouchEvent.TOUCH_DOWN) {
                gameManager.yes.setPressed(OverlapTester.pointInRectangle(gameManager.yes,touchPoint));
                gameManager.no.setPressed(OverlapTester.pointInRectangle(gameManager.no,touchPoint));
                gameManager.hitButton.setPressed(OverlapTester.pointInRectangle(gameManager.hitButton,touchPoint));
                gameManager.standButton.setPressed(OverlapTester.pointInRectangle(gameManager.standButton,touchPoint));
                gameManager.doubleButton.setPressed(OverlapTester.pointInUpperHalfRectangle(gameManager.doubleButton,touchPoint));
                gameManager.splitButton.setPressed(OverlapTester.pointInUpperHalfRectangle(gameManager.splitButton,touchPoint));
                gameManager.surrenderButton.setPressed(OverlapTester.pointInUpperHalfRectangle(gameManager.surrenderButton,touchPoint));
                gameManager.hintButton.setPressed(OverlapTester.pointInRectangle(gameManager.hintButton,touchPoint));
                gameManager.playButton.setPressed(OverlapTester.pointInRectangle(gameManager.playButton,touchPoint));
            }
            if(event.type==TouchEvent.TOUCH_UP) {
                gameManager.yes.setPressed(false);
                gameManager.no.setPressed(false);
                gameManager.hitButton.setPressed(false);
                gameManager.standButton.setPressed(false);
                gameManager.doubleButton.setPressed(false);
                gameManager.splitButton.setPressed(false);
                gameManager.surrenderButton.setPressed(false);
                gameManager.hintButton.setPressed(false);
                gameManager.playButton.setPressed(false);
                if(OverlapTester.pointInRectangle(gameManager.hitButton,touchPoint) && gameManager.getState()!=5) {
                    Assets.button.play(sound);
                    gameManager.addCard();
                }
                else if(OverlapTester.pointInRectangle(gameManager.standButton,touchPoint) && gameManager.getState()!=5) {
                    Assets.button.play(sound);
                    gameManager.changeHand();
                }
                else if (OverlapTester.pointInUpperHalfRectangle(gameManager.doubleButton,touchPoint) && gameManager.getState()!=5) {
                    Assets.button.play(sound);
                    gameManager.addDouble();
                    gameManager.changeHand();
                }
                else if(OverlapTester.pointInUpperHalfRectangle(gameManager.splitButton,touchPoint) && gameManager.getState()!=5) {
                    Assets.button.play(sound);
                    gameManager.addSplit();
                }
                else if(OverlapTester.pointInUpperHalfRectangle(gameManager.surrenderButton,touchPoint) && gameManager.surrenderButton.getYPos()==150) {
                    Assets.button.play(sound);
                    gameManager.surrender();
                }
                else if(OverlapTester.pointInRectangle(gameManager.hintButton,touchPoint) && gameManager.getState()<5 && gameManager.getState()>=0) {
                    Assets.button.play(sound);
                    glGame.runOnUiThread(new Runnable(){
                        public void run() {
                            if(gameManager.getCurrentHand().size()>=2) {
                                String text="not take insurance.";
                                if(gameManager.getState()>0)
                                    text=gameManager.findBasicStrategyAction(gameManager.getDecks(), gameManager.getCurrentHand())+".";
                                Toast toast = Toast.makeText(glGame, "You should " + text, Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.TOP,0,0);
                                toast.show();
                            }
                        }
                    });
                }
                else if(OverlapTester.pointInRectangle(gameManager.playButton,touchPoint) && gameManager.getState()<5 && gameManager.getState()>=0) {
                    Assets.button.play(sound);
                    if(gameManager.getCurrentHand().size()>=2) {
                        String action=gameManager.findBasicStrategyAction(gameManager.getDecks(),gameManager.getCurrentHand());
                        if(gameManager.getInsurance()==true && gameManager.getState()==0)
                            gameManager.insurance(false);
                        else if(action.equals("hit")) {
                            gameManager.addCard();
                        }
                        else if(action.equals("stand")) {
                            gameManager.changeHand();
                        }
                        else if(action.equals("double down")) {
                            gameManager.addDouble();
                            gameManager.changeHand();
                        }
                        else if(action.equals("split")) {
                            gameManager.addSplit();
                        }
                        else if(action.equals("surrender")) {
                            gameManager.surrender();
                        }
                    }
                }
                else if(OverlapTester.pointInRectangle(gameManager.yes,touchPoint) && gameManager.getInsurance()==true) {
                    Assets.button.play(sound);
                    gameManager.insurance(true);
                }
                else if(OverlapTester.pointInRectangle(gameManager.no,touchPoint) && gameManager.getInsurance()==true) {
                    Assets.button.play(sound);
                    gameManager.insurance(false);
                }
                else if(gameManager.getState()>=6) {
                    chipManager.removeAllChips();
                    state=GAME_BETTING;
                    editor.putInt("com.blackjack.state",state);
                    editor.commit();
                    gameManager.cardsToDiscard();
                    gameManager.getSideBetLeft().resetPayout();
                    gameManager.getSideBetRight().resetPayout();
                    waitTime=180;
                    sideBetWaitTime=60;
                }
                chipManager.updateMoney();
            }
        }
        if(state==GAME_BETTING)
            saveData();
    }

    public void updateLastHand(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        int len= touchEvents.size();

        gameManager.moveDealerDownCard();
        gameManager.updatePlayerCards();
        gameManager.updateLastHandDealerCards();
        for(int i=0;i<len;i++) {
            TouchEvent event = touchEvents.get(i);
            touchPoint.set(event.x,event.y);
            guiCam.touchToWorld(touchPoint);
            if(event.type==TouchEvent.TOUCH_UP) {
                Assets.button.play(sound);
                state=GAME_BETTING;
                editor.putInt("com.blackjack.state",state);
                editor.commit();
                gameManager.cardsToDiscard();
            }
        }
    }

    public void updateMenu(float deltaTime) {

        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        int len= touchEvents.size();

        gameManager.updateFinishedPlayer();
        for(int i=0;i<len;i++) {
            TouchEvent event = touchEvents.get(i);
            touchPoint.set(event.x, event.y);
            guiCam.touchToWorld(touchPoint);
            if(event.type==TouchEvent.TOUCH_DRAGGED || event.type==TouchEvent.TOUCH_DOWN) {
                gameManager.menuBackArrow.setPressed(OverlapTester.pointInRectangle(gameManager.menuBackArrow,touchPoint));
                gameManager.settings.setPressed(OverlapTester.pointInRectangle(gameManager.settings,touchPoint));
                gameManager.statistics.setPressed(OverlapTester.pointInRectangle(gameManager.statistics,touchPoint));
                gameManager.freeChips.setPressed(OverlapTester.pointInRectangle(gameManager.freeChips,touchPoint));
                gameManager.earnChips.setPressed(OverlapTester.pointInRectangle(gameManager.earnChips,touchPoint));
                gameManager.rules.setPressed(OverlapTester.pointInRectangle(gameManager.rules,touchPoint));
                gameManager.sound.setPressed(OverlapTester.pointInRectangle(gameManager.sound,touchPoint));

            }
            if(event.type==TouchEvent.TOUCH_UP) {
                gameManager.menuBackArrow.setPressed(false);
                gameManager.settings.setPressed(false);
                gameManager.statistics.setPressed(false);
                gameManager.freeChips.setPressed(false);
                gameManager.earnChips.setPressed(false);
                gameManager.rules.setPressed(false);
                gameManager.sound.setPressed(false);
                if(OverlapTester.pointInRectangle(gameManager.menuBackArrow,touchPoint)) {
                    Assets.button.play(sound);
                    state=GAME_BETTING;
                    editor.putInt("com.blackjack.state",state);
                    editor.commit();
                    gameManager.pushMenu();
                }
                else if(OverlapTester.pointInRectangle(gameManager.settings,touchPoint)) {
                    Assets.button.play(sound);
                    gameManager.pushMenu();
                    state=GAME_SETTINGS;
                    editor.putInt("com.blackjack.state",state);
                    editor.commit();
                    settingsManager.loadSettings();
                    settingsManager.enterSettingsCheck();
                    editor.putInt("disableBack",1);
                    editor.commit();
                }
                else if(OverlapTester.pointInRectangle(gameManager.rules,touchPoint)) {
                    Assets.button.play(sound);
                    state=GAME_BETTING;
                    editor.putInt("com.blackjack.state",state);
                    editor.commit();
                    gameManager.pushMenu();
                    gameManager.cardsToDiscard();
                    glGame.startActivity(new Intent(glGame,Rules.class));
                }
                else if(OverlapTester.pointInRectangle(gameManager.freeChips,touchPoint)) {
                    Assets.button.play(sound);
                    state = GAME_SLOTMACHINE;
                    editor.putInt("com.blackjack.state",state);
                    editor.commit();
                    gameManager.pushMenu();
                    gameManager.cardsToDiscard();
                    slotMachine.reset();
                    waitTime=180;
                    //glGame.startActivity(new Intent(glGame,SlotMachineAd.class));
                }
                else if(OverlapTester.pointInRectangle(gameManager.sound,touchPoint)) {
                    //change to if settings sound is on or off
                    if(settingsManager.getSound()==1) {
                        settingsManager.setSound(0);
                        sound=0;
                        gameManager.sound.setTextureRegion(Assets.soundOffMenuButton);
                    }
                    else {
                        settingsManager.setSound(1);
                        sound=1;
                        Assets.button.play(sound);
                        gameManager.sound.setTextureRegion(Assets.soundOnMenuButton);
                    }
                }else if(OverlapTester.pointInRectangle(gameManager.tableGreen,touchPoint)) {
                    gameRenderer.changeBackground(Assets.greenBackground,Assets.green);
                }
                else if(OverlapTester.pointInRectangle(gameManager.tableBlue,touchPoint)) {
                    gameRenderer.changeBackground(Assets.blueBackground,Assets.blue);
                }
                else if(OverlapTester.pointInRectangle(gameManager.tableRed,touchPoint)) {
                    gameRenderer.changeBackground(Assets.redBackground,Assets.red);
                }
                else if(OverlapTester.pointInRectangle(gameManager.tablePurple,touchPoint)) {
                    gameRenderer.changeBackground(Assets.purpleBackground,Assets.purple);
                }
            }
        }

    }

    public void updateSlotMachine(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        int len= touchEvents.size();

        gameManager.updateFinishedPlayer();

        for(int i=0;i<len;i++) {
            TouchEvent event = touchEvents.get(i);
            touchPoint.set(event.x,event.y);
            guiCam.touchToWorld(touchPoint);

            slotMachine.setSlotArmHeight(touchPoint.y);

            if(event.type==TouchEvent.TOUCH_DOWN) {
                if(OverlapTester.pointInCircle(slotMachine.getSlotMachineHandle(),touchPoint) && touchPoint.y>428 && touchPoint.y<772) {
                    slotMachine.setTouched(true);
                }
            }
            if(event.type==TouchEvent.TOUCH_UP) {
                slotMachine.setTouched(false);


            }
        }
    }

    public void updateSettings(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        int len= touchEvents.size();



        for(int i=0;i<len;i++) {
            TouchEvent event = touchEvents.get(i);
            touchPoint.set(event.x,event.y);
            guiCam.touchToWorld(touchPoint);
            if(event.type==TouchEvent.TOUCH_DRAGGED || event.type==TouchEvent.TOUCH_DOWN) {
                /*if(settingsManager.getPage()==1) {
                    settingsManager.getDeckLeft().setPressed(OverlapTester.pointInRectangle(settingsManager.getDeckLeft(), touchPoint));
                    settingsManager.getDeckRight().setPressed(OverlapTester.pointInRectangle(settingsManager.getDeckRight(), touchPoint));
                    settingsManager.getPenLeft().setPressed(OverlapTester.pointInRectangle(settingsManager.getPenLeft(), touchPoint));
                    settingsManager.getPenRight().setPressed(OverlapTester.pointInRectangle(settingsManager.getPenRight(), touchPoint));
                }
                else if(settingsManager.getPage()==2) {
                    settingsManager.getBlackjackLeft().setPressed(OverlapTester.pointInRectangle(settingsManager.getBlackjackLeft(),touchPoint));
                    settingsManager.getBlackjackRight().setPressed(OverlapTester.pointInRectangle(settingsManager.getBlackjackRight(),touchPoint));
                    settingsManager.getSplitLeft().setPressed(OverlapTester.pointInRectangle(settingsManager.getSplitLeft(),touchPoint));
                    settingsManager.getSplitRight().setPressed(OverlapTester.pointInRectangle(settingsManager.getSplitRight(),touchPoint));
                    settingsManager.getDoubleLeft().setPressed(OverlapTester.pointInRectangle(settingsManager.getDoubleLeft(),touchPoint));
                    settingsManager.getDoubleRight().setPressed(OverlapTester.pointInRectangle(settingsManager.getDoubleRight(),touchPoint));
                }*/

                if(OverlapTester.pointInRectangle(settingsManager.getDeckPenetration(),touchPoint)) {
                    if(touchPoint.x<=settingsManager.getDeckPenetration().getBarXStart()) {
                        settingsManager.getDeckPenetration().setxPos(settingsManager.getDeckPenetration().getBarXStart());
                    }
                    else if(touchPoint.x>=settingsManager.getDeckPenetration().getBarXEnd()) {
                        settingsManager.getDeckPenetration().setxPos(settingsManager.getDeckPenetration().getBarXEnd());
                    }
                    else {
                        settingsManager.getDeckPenetration().setxPos(touchPoint.x);
                    }
                    settingsManager.setPenetration((int)(settingsManager.getDeckPenetration().getSliderPlacement()*100));

                }

            }


            if(event.type==TouchEvent.TOUCH_UP) {
                /*
                if(settingsManager.getPage()==1) {
                    settingsManager.getDeckLeft().setPressed(false);
                    settingsManager.getDeckRight().setPressed(false);
                    settingsManager.getPenLeft().setPressed(false);
                    settingsManager.getPenRight().setPressed(false);
                }
                else if(settingsManager.getPage()==2) {
                    settingsManager.getBlackjackLeft().setPressed(false);
                    settingsManager.getBlackjackRight().setPressed(false);
                    settingsManager.getSplitLeft().setPressed(false);
                    settingsManager.getSplitRight().setPressed(false);
                    settingsManager.getDoubleLeft().setPressed(false);
                    settingsManager.getDoubleRight().setPressed(false);
                }*/
                if(OverlapTester.pointInRectangle(settingsManager.getDeck1(),touchPoint)) {
                    settingsManager.setDecks(1);
                    //thread to change color here
                    pressSettingButton(settingsManager.getDeck1(),settingsManager.getDeck2(),settingsManager.getDeck3(),settingsManager.getDeck4(),settingsManager.getDeck5(),settingsManager.getDeck6(),settingsManager.getDeck7(),settingsManager.getDeck8());
                }
                else if(OverlapTester.pointInRectangle(settingsManager.getDeck2(),touchPoint)) {
                    settingsManager.setDecks(2);
                    //thread to change color here
                    pressSettingButton(settingsManager.getDeck2(),settingsManager.getDeck1(),settingsManager.getDeck3(),settingsManager.getDeck4(),settingsManager.getDeck5(),settingsManager.getDeck6(),settingsManager.getDeck7(),settingsManager.getDeck8());
                }
                else if(OverlapTester.pointInRectangle(settingsManager.getDeck3(),touchPoint)) {
                    settingsManager.setDecks(3);
                    //thread to change color here
                    pressSettingButton(settingsManager.getDeck3(),settingsManager.getDeck2(),settingsManager.getDeck1(),settingsManager.getDeck4(),settingsManager.getDeck5(),settingsManager.getDeck6(),settingsManager.getDeck7(),settingsManager.getDeck8());
                }
                else if(OverlapTester.pointInRectangle(settingsManager.getDeck4(),touchPoint)) {
                    settingsManager.setDecks(4);
                    //thread to change color here
                    pressSettingButton(settingsManager.getDeck4(),settingsManager.getDeck2(),settingsManager.getDeck3(),settingsManager.getDeck1(),settingsManager.getDeck5(),settingsManager.getDeck6(),settingsManager.getDeck7(),settingsManager.getDeck8());
                }
                else if(OverlapTester.pointInRectangle(settingsManager.getDeck5(),touchPoint)) {
                    settingsManager.setDecks(5);
                    //thread to change color here
                    pressSettingButton(settingsManager.getDeck5(),settingsManager.getDeck2(),settingsManager.getDeck3(),settingsManager.getDeck4(),settingsManager.getDeck1(),settingsManager.getDeck6(),settingsManager.getDeck7(),settingsManager.getDeck8());
                }
                else if(OverlapTester.pointInRectangle(settingsManager.getDeck6(),touchPoint)) {
                    settingsManager.setDecks(6);
                    //thread to change color here
                    pressSettingButton(settingsManager.getDeck6(),settingsManager.getDeck2(),settingsManager.getDeck3(),settingsManager.getDeck4(),settingsManager.getDeck5(),settingsManager.getDeck1(),settingsManager.getDeck7(),settingsManager.getDeck8());
                }
                else if(OverlapTester.pointInRectangle(settingsManager.getDeck7(),touchPoint)) {
                    settingsManager.setDecks(7);
                    //thread to change color here
                    pressSettingButton(settingsManager.getDeck7(),settingsManager.getDeck2(),settingsManager.getDeck3(),settingsManager.getDeck4(),settingsManager.getDeck5(),settingsManager.getDeck6(),settingsManager.getDeck1(),settingsManager.getDeck8());
                }
                else if(OverlapTester.pointInRectangle(settingsManager.getDeck8(),touchPoint)) {
                    settingsManager.setDecks(8);
                    //thread to change color here
                    pressSettingButton(settingsManager.getDeck8(),settingsManager.getDeck2(),settingsManager.getDeck3(),settingsManager.getDeck4(),settingsManager.getDeck5(),settingsManager.getDeck6(),settingsManager.getDeck7(),settingsManager.getDeck1());
                }
                else if(OverlapTester.pointInRectangle(settingsManager.getCSMToggle(),touchPoint)) {
                    settingsManager.setCsm(!settingsManager.getCSM());
                    //thread to toggle color change and move toggle circle
                    pressToggle(settingsManager.getCSMToggle());
                }
                else if(OverlapTester.pointInRectangle(settingsManager.getDeckPenetration(),touchPoint)) {
                    if(touchPoint.x<=settingsManager.getDeckPenetration().getBarXStart()) {
                        settingsManager.getDeckPenetration().setxPos(settingsManager.getDeckPenetration().getBarXStart());
                    }
                    else if(touchPoint.x>=settingsManager.getDeckPenetration().getBarXEnd()) {
                        settingsManager.getDeckPenetration().setxPos(settingsManager.getDeckPenetration().getBarXEnd());
                    }
                    else {
                        settingsManager.getDeckPenetration().setxPos(touchPoint.x);
                    }

                    settingsManager.setPenetration((int)(settingsManager.getDeckPenetration().getSliderPlacement()*100));

                }
                else if(OverlapTester.pointInRectangle(settingsManager.getBlackjackPays32(),touchPoint)) {
                    settingsManager.setBlackjackPays(1.5);
                    //thread to change color here
                    pressSettingButton(settingsManager.getBlackjackPays32(),settingsManager.getBlackjackPays75(),settingsManager.getBlackjackPays65(),settingsManager.getBlackjackPays11());
                }
                else if(OverlapTester.pointInRectangle(settingsManager.getBlackjackPays75(),touchPoint)) {
                    settingsManager.setBlackjackPays(1.4);
                    //thread to change color here
                    pressSettingButton(settingsManager.getBlackjackPays75(),settingsManager.getBlackjackPays32(),settingsManager.getBlackjackPays65(),settingsManager.getBlackjackPays11());
                }
                else if(OverlapTester.pointInRectangle(settingsManager.getBlackjackPays65(),touchPoint)) {
                    settingsManager.setBlackjackPays(1.2);
                    //thread to change color here
                    pressSettingButton(settingsManager.getBlackjackPays65(),settingsManager.getBlackjackPays75(),settingsManager.getBlackjackPays32(),settingsManager.getBlackjackPays11());
                }
                else if(OverlapTester.pointInRectangle(settingsManager.getBlackjackPays11(),touchPoint)) {
                    settingsManager.setBlackjackPays(1);
                    //thread to change color here
                    pressSettingButton(settingsManager.getBlackjackPays11(),settingsManager.getBlackjackPays75(),settingsManager.getBlackjackPays65(),settingsManager.getBlackjackPays32());
                }
                else if(OverlapTester.pointInRectangle(settingsManager.getInsuranceToggle(),touchPoint)) {
                    settingsManager.setInsurance(!settingsManager.getInsurance());
                    //thread to toggle color change and move toggle circle
                    pressToggle(settingsManager.getInsuranceToggle());
                }
                else if(OverlapTester.pointInRectangle(settingsManager.getSurrenderToggle(),touchPoint)) {
                    settingsManager.setSurrender(!settingsManager.getSurrender());
                    //thread to toggle color change and move toggle circle
                    pressToggle(settingsManager.getSurrenderToggle());
                }
                else if(OverlapTester.pointInRectangle(settingsManager.getDealerStand(),touchPoint)) {
                    settingsManager.setDealer(1);
                    //thread to change color here
                    pressSettingButton(settingsManager.getDealerStand(),settingsManager.getDealerHit());
                }
                else if(OverlapTester.pointInRectangle(settingsManager.getDealerHit(),touchPoint)) {
                    settingsManager.setDealer(-1);
                    //thread to change color here
                    pressSettingButton(settingsManager.getDealerHit(),settingsManager.getDealerStand());
                }
                else if(OverlapTester.pointInRectangle(settingsManager.getSplit2(),touchPoint)) {
                    settingsManager.setSplit(2);
                    //thread to change color here
                    pressSettingButton(settingsManager.getSplit2(),settingsManager.getSplit3(),settingsManager.getSplit4());
                }
                else if(OverlapTester.pointInRectangle(settingsManager.getSplit3(),touchPoint)) {
                    settingsManager.setSplit(3);
                    //thread to change color here
                    pressSettingButton(settingsManager.getSplit3(),settingsManager.getSplit2(),settingsManager.getSplit4());
                }
                else if(OverlapTester.pointInRectangle(settingsManager.getSplit4(),touchPoint)) {
                    settingsManager.setSplit(4);
                    //thread to change color here
                    pressSettingButton(settingsManager.getSplit4(),settingsManager.getSplit3(),settingsManager.getSplit2());
                }
                else if(OverlapTester.pointInRectangle(settingsManager.getResplitAcesToggle(),touchPoint)) {
                    settingsManager.setResplit(!settingsManager.getResplit());
                    //thread to toggle color change and move toggle circle
                    pressToggle(settingsManager.getResplitAcesToggle());
                }
                else if(OverlapTester.pointInRectangle(settingsManager.getHitSplitAcesToggle(),touchPoint)) {
                    settingsManager.setHitAces(!settingsManager.getHitAces());
                    //thread to toggle color change and move toggle circle
                    pressToggle(settingsManager.getHitSplitAcesToggle());
                }
                else if(OverlapTester.pointInRectangle(settingsManager.getDoubleSplitAcesToggle(),touchPoint)) {
                    if(settingsManager.getDas()==false && settingsManager.getDoubleAces()==false) {
                        glGame.runOnUiThread(new Runnable(){
                            public void run() {
                                Toast.makeText(glGame,"Double after split must be allowed.",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else {
                        settingsManager.setDoubleAces(!settingsManager.getDoubleAces());
                        //thread to toggle color change and move toggle circle
                        pressToggle(settingsManager.getDoubleSplitAcesToggle());
                    }
                    Log.d("DOUBLEACES","DOUBLE ACES is " + settingsManager.getDoubleAces());
                }
                else if(OverlapTester.pointInRectangle(settingsManager.getDoubleAfterSplitToggle(),touchPoint)) {
                    settingsManager.setDas(!settingsManager.getDas());
                    if(settingsManager.getDas()==false && settingsManager.getDoubleAces()==true) {
                        settingsManager.setDoubleAces(false);
                        //thread to toggle color change and move toggle circle
                        pressToggle(settingsManager.getDoubleSplitAcesToggle());
                    }
                    //thread to toggle color change and move toggle circle
                    pressToggle(settingsManager.getDoubleAfterSplitToggle());
                    Log.d("DOUBLEACES","DOUBLE ACES is " + settingsManager.getDoubleAces());
                }
                else if(OverlapTester.pointInRectangle(settingsManager.getDoubleAny2(),touchPoint)) {
                    settingsManager.setDoubleDown(1);
                    pressSettingButton(settingsManager.getDoubleAny2(),settingsManager.getDouble911(),settingsManager.getDouble1011());
                }
                else if(OverlapTester.pointInRectangle(settingsManager.getDouble911(),touchPoint)) {
                    settingsManager.setDoubleDown(2);
                    pressSettingButton(settingsManager.getDouble911(),settingsManager.getDoubleAny2(),settingsManager.getDouble1011());
                }
                else if(OverlapTester.pointInRectangle(settingsManager.getDouble1011(),touchPoint)) {
                    settingsManager.setDoubleDown(3);
                    pressSettingButton(settingsManager.getDouble1011(),settingsManager.getDouble911(),settingsManager.getDoubleAny2());
                }
                else if(OverlapTester.pointInRectangle(settingsManager.getExitSettings(),touchPoint)) {

                    editor.putInt("disableBack",1);
                    editor.commit();
                    glGame.runOnUiThread(new Runnable(){
                        public void run() {
                            if(!settingsManager.wereSettingsChanged()) {
                                negativeButtonPress();
                            }
                            else if(settingsManager.isDeckShuffleNeeded()) {
                                new AlertDialog.Builder(glGame)
                                        .setTitle("Deck will be shuffled")
                                        .setMessage("Save settings and continue?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                positiveButtonPress();
                                                settingsManager.setNewDeck(true);
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                negativeButtonPress();
                                            }
                                        })
                                        .show();
                            }
                            else {
                                new AlertDialog.Builder(glGame)
                                        .setMessage("Save settings and continue?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                positiveButtonPress();
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                negativeButtonPress();
                                            }
                                        })
                                        .show();
                            }

                        }

                        public void positiveButtonPress() {
                            settingsManager.setChangeSideBets(true);
                            //change tableSettingsTextures
                            if(settingsManager.getBlackjackPays()==1.5)
                                Assets.blackjackPayout.changeValues(0,0,817,54);
                            else if(settingsManager.getBlackjackPays()==1.4)
                                Assets.blackjackPayout.changeValues(0,54,817,54);
                            else if(settingsManager.getBlackjackPays()==1.2)
                                Assets.blackjackPayout.changeValues(0,108,817,54);
                            else
                                Assets.blackjackPayout.changeValues(0,162,817,54);

                            if(settingsManager.getDealer()==1) //stand soft 17
                                Assets.dealerSetting.changeValues(0,390,1080,174);
                            else //hit soft 17
                                Assets.dealerSetting.changeValues(0,216,1080,174);
                            //save settings
                            state=GAME_BETTING;
                            editor.putInt("com.blackjack.state",state);
                            editor.commit();
                            editor.putInt("disableBack",0);
                            editor.commit();
                        }

                        public void negativeButtonPress() {
                            settingsManager.revertSettings();
                            state=GAME_BETTING;
                            editor.putInt("com.blackjack.state",state);
                            editor.commit();
                            editor.putInt("disableBack",0);
                            editor.commit();
                        }


                    });


                }
                /*
                else if(OverlapTester.pointInRectangle(settingsManager.getResetStats(),touchPoint)) {
                    glGame.runOnUiThread(new Runnable(){
                        public void run() {
                            new AlertDialog.Builder(glGame)
                                        .setTitle("All statistics will be permanently reset")
                                        .setMessage("Are you sure you want to continue?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                settingsManager.resetStats();
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .show();
                            }
                    });
                }
                */
                    /*if(OverlapTester.pointInRectangle(settingsManager.getPerfectPairsButton(),touchPoint)) {
                        if(settingsManager.getDecks()<2) {
                            glGame.runOnUiThread(new Runnable(){
                                public void run() {
                                    Toast.makeText(glGame,"Not enough decks to play this side bet.",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        settingsManager.setPerfectPairs(!settingsManager.getPerfectPairs());
                    }
                    else if(OverlapTester.pointInRectangle(settingsManager.getTwentyOneV1Button(),touchPoint)) {
                        settingsManager.setTwentyOneV1(!settingsManager.getTwentyOneV1());
                    }
                    else if(OverlapTester.pointInRectangle(settingsManager.getTwentyOneV2Button(),touchPoint)) {
                        if(settingsManager.getDecks()<3) {
                            glGame.runOnUiThread(new Runnable(){
                                public void run() {
                                    Toast.makeText(glGame,"Not enough decks to play this side bet.",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        settingsManager.setTwentyOneV2(!settingsManager.getTwentyOneV2());
                    }*/
            }

        }

    }


    @Override
    public void present(float deltaTime) {
        GL10 gl = glGraphics.getGl();
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        guiCam.setViewportAndMatrices();
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glColor4f(1, 1, 1, 1);

        if(settingsManager.getNewDeck()==true) {
            settingsManager.setNewDeck(false);

            gameManager.newDeck();
        }
        if(settingsManager.getChangeSideBets()==true) {
            settingsManager.checkSideBets();
            if(settingsManager.getPerfectPairs()==true)
                gameManager.getSideBetLeft().setVersion(1);
            else {
                gameManager.getSideBetLeft().setVersion(0);
                chipManager.removeAllChipsFromSelection(1);
            }

            if(settingsManager.getTwentyOneV1()==true)
                gameManager.getSideBetRight().setVersion(1);
            else if(settingsManager.getTwentyOneV2()==true)
                gameManager.getSideBetRight().setVersion(2);
            else {
                gameManager.getSideBetRight().setVersion(0);
                chipManager.removeAllChipsFromSelection(2);
            }

            settingsManager.setChangeSideBets(false);
        }

        gameRenderer.render(state);


        switch(state) {
            case GAME_BETTING:
                presentBetting(deltaTime);
                break;
            case GAME_RUNNING:
                presentRunning(deltaTime);
                break;
            case GAME_LASTHAND:
                presentLastHand(deltaTime);
                break;
            case GAME_MENU:
                presentMenu(deltaTime);
                break;
            case GAME_SLOTMACHINE:
                presentSlotMachine(deltaTime);
                break;
            case GAME_SETTINGS:
                presentSettings(deltaTime);
                break;
        }

        //Test for background

        gl.glDisable(GL10.GL_BLEND);
    }

    public void presentBetting(float deltaTime) {

    }

    public void presentRunning(float deltaTime) {

    }

    public void presentLastHand(float deltaTime) {

    }

    public void presentMenu(float deltaTime) {

    }

    public void presentSlotMachine(float deltaTime) {

        if(slotMachine.getSpinSpeed()==0) {
            waitTime--;
            if(waitTime==0) {
                waitTime=180;
                state=GAME_BETTING;
                editor.putInt("com.blackjack.state",state);
                editor.commit();
            }
        }
    }

    public void presentSettings(float deltaTime) {

        if(sharedPreferences.getInt("disableBack",0)==2) {
            editor.putInt("disableBack",1);
            editor.commit();

            glGame.runOnUiThread(new Runnable(){
                public void run() {
                    if(!settingsManager.wereSettingsChanged()) {
                        negativeButtonPress();
                    }
                    else if(settingsManager.isDeckShuffleNeeded()) {
                        new AlertDialog.Builder(glGame)
                                .setTitle("Deck will be shuffled")
                                .setMessage("Save settings and continue?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        positiveButtonPress();
                                        settingsManager.setNewDeck(true);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        negativeButtonPress();
                                    }
                                })
                                .show();
                        //shuffle deck here
                    }
                    else {
                        new AlertDialog.Builder(glGame)
                                .setMessage("Save settings and continue?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        positiveButtonPress();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        negativeButtonPress();
                                    }
                                })
                                .show();
                    }

                }

                public void positiveButtonPress() {
                    settingsManager.setChangeSideBets(true);
                    //change tableSettingsTextures
                    if(settingsManager.getBlackjackPays()==1.5)
                        Assets.blackjackPayout.changeValues(0,0,817,54);
                    else if(settingsManager.getBlackjackPays()==1.4)
                        Assets.blackjackPayout.changeValues(0,54,817,54);
                    else if(settingsManager.getBlackjackPays()==1.2)
                        Assets.blackjackPayout.changeValues(0,108,817,54);
                    else
                        Assets.blackjackPayout.changeValues(0,162,817,54);

                    if(settingsManager.getDealer()==1) //stand soft 17
                        Assets.dealerSetting.changeValues(0,390,1080,174);
                    else //hit soft 17
                        Assets.dealerSetting.changeValues(0,216,1080,174);
                    //save settings
                    state=GAME_BETTING;
                    editor.putInt("com.blackjack.state",state);
                    editor.commit();
                    editor.putInt("disableBack",0);
                    editor.commit();
                }

                public void negativeButtonPress() {
                    settingsManager.revertSettings();
                    state=GAME_BETTING;
                    editor.putInt("com.blackjack.state",state);
                    editor.commit();
                    editor.putInt("disableBack",0);
                    editor.commit();
                }


            });

        }
    }


    @Override
    public void resume() {
        //loadData();
    }

    @Override
    public void pause() {
        saveData();
    }

    @Override
    public void dispose() {
        saveData();
    }

    public void pressSettingButton(Button on,Button... off) {
        final Button button=on;
        final Button[] buttons=off;

        Thread thread;

        button.setOn(true);
        for (Button b: buttons) {
            b.setOn(false);
        }
        glGame.runOnUiThread(new Runnable(){
            public void run() {
                boolean stillRunning=true;

                while(stillRunning) {
                    stillRunning=false;
                    if(changeColors(button)==true) {
                        stillRunning=true;
                    }
                    for(Button b: buttons) {
                        if(changeColors(b)==true) {
                            stillRunning=true;
                        }
                    }
                    SystemClock.sleep(2);
                }
                Log.d("DONE","DONE");
            }

            public boolean changeColors(Button b) {
                boolean changed=false;
                if(b.getOn()==false) {
                    if (b.getRedf() > .25f) {
                        b.setRGBf(b.getRedf() - .0125f, b.getGreenf(), b.getBluef());
                        changed=true;
                    }
                    else
                        b.setRGBf(.25f,b.getGreenf(),b.getBluef());
                    if (b.getGreenf() > .25f) {
                        b.setRGBf(b.getRedf(), b.getGreenf() - .002766f, b.getBluef());
                        changed=true;
                    }
                    else
                        b.setRGBf(b.getRedf(),.25f,b.getBluef());
                    if (b.getBluef() < .25f) {
                        b.setRGBf(b.getRedf(), b.getGreenf(), b.getBluef() + .004166f);
                        changed=true;
                    }
                    else
                        b.setRGBf(b.getRedf(),b.getGreenf(),.25f);
                }
                else {
                    if (b.getRedf() < 1f) {
                        b.setRGBf(b.getRedf() + .0125f, b.getGreenf(), b.getBluef());
                        changed=true;
                    }
                    else
                        b.setRGBf(1f,b.getGreenf(),b.getBluef());
                    if (b.getGreenf() < .416f) {
                        b.setRGBf(b.getRedf(), b.getGreenf() + .002766f, b.getBluef());
                        changed=true;
                    }
                    else
                        b.setRGBf(b.getRedf(),.416f,b.getBluef());
                    if (b.getBluef() > 0) {
                        b.setRGBf(b.getRedf(), b.getGreenf(), b.getBluef() - .004166f);
                        changed=true;
                    }
                    else
                        b.setRGBf(b.getRedf(),b.getGreenf(),0f);
                }


                return changed;
            }
        });
    }

    public void pressToggle(Toggle t) {
        final Toggle toggle=t;
        toggle.setOn(!toggle.getOn());
        glGame.runOnUiThread(new Runnable(){
            public void run() {
                boolean stillRunning=true;

                while(stillRunning) {
                    stillRunning=false;
                    if(changeColorsMoveCircle(toggle)==true) {
                        stillRunning=true;
                    }
                    SystemClock.sleep(2);
                }
                Log.d("DONE","DONE");
            }

            public boolean changeColorsMoveCircle(Toggle b) {
                boolean changed=false;
                if(b.getOn()==false) {
                    if (b.getRedf() > .25f) {
                        b.setRGBf(b.getRedf() - .0125f, b.getGreenf(), b.getBluef());
                        changed=true;
                    }
                    else
                        b.setRGBf(.25f,b.getGreenf(),b.getBluef());
                    if (b.getGreenf() > .25f) {
                        b.setRGBf(b.getRedf(), b.getGreenf() - .002766f, b.getBluef());
                        changed=true;
                    }
                    else
                        b.setRGBf(b.getRedf(),.25f,b.getBluef());
                    if (b.getBluef() < .25f) {
                        b.setRGBf(b.getRedf(), b.getGreenf(), b.getBluef() + .004166f);
                        changed=true;
                    }
                    else
                        b.setRGBf(b.getRedf(),b.getGreenf(),.25f);
                    if(b.getCircleXPos()>b.getxPos()-33) {
                        b.setCircleXPos(b.getCircleXPos() - 1);
                        changed=true;
                    }
                    else {
                        b.setCircleXPos(b.getxPos()-33);
                    }
                }
                else {
                    if (b.getRedf() < 1f) {
                        b.setRGBf(b.getRedf() + .0125f, b.getGreenf(), b.getBluef());
                        changed=true;
                    }
                    else
                        b.setRGBf(1f,b.getGreenf(),b.getBluef());
                    if (b.getGreenf() < .416f) {
                        b.setRGBf(b.getRedf(), b.getGreenf() + .002766f, b.getBluef());
                        changed=true;
                    }
                    else
                        b.setRGBf(b.getRedf(),.416f,b.getBluef());
                    if (b.getBluef() > 0) {
                        b.setRGBf(b.getRedf(), b.getGreenf(), b.getBluef() - .004166f);
                        changed=true;
                    }
                    else
                        b.setRGBf(b.getRedf(),b.getGreenf(),0f);
                    if(b.getCircleXPos()<b.getxPos()+33) {
                        b.setCircleXPos(b.getCircleXPos() + 1);
                        changed=true;
                    }
                    else {
                        b.setCircleXPos(b.getxPos()+33);
                    }
                }


                return changed;
            }
        });
    }

    public void saveData() {
        SerializableManager.save(glGame,chipManager,"com.blackjack.chipManager");
        SerializableManager.save(glGame,settingsManager,"com.blackjack.settingsManager");
        SerializableManager.save(glGame,gameManager,"com.blackjack.gameManager");
        SerializableManager.save(glGame,slotMachine,"com.blackjack.slotMachine");
        SerializableManager.save(glGame,gameRenderer,"com.blackjack.gameRenderer");

        editor.putInt("com.blackjack.state",state);
        editor.putInt("com.blackjack.money",chipManager.getTotalMoney());
        editor.commit();
    }

    public void loadData() {
        chipManager=SerializableManager.load(glGame,"com.blackjack.chipManager");
        settingsManager=SerializableManager.load(glGame,"com.blackjack.settingsManager");
        gameManager=SerializableManager.load(glGame,"com.blackjack.gameManager");
        slotMachine=SerializableManager.load(glGame,"com.blackjack.slotMachine");
        gameRenderer=SerializableManager.load(glGame,"com.blackjack.gameRenderer");

        Assets.reloadSound(glGame);

        if(chipManager!=null && settingsManager!=null && gameManager!=null && slotMachine!=null && gameRenderer!=null) {
            chipManager.loadData();
            settingsManager.loadData();
            gameManager.loadData(chipManager, settingsManager,statisticsManager);
            slotMachine.loadData(glGraphics, batcher, chipManager);
            gameRenderer.loadData(batcher, glGraphics, gameManager, chipManager, slotMachine, settingsManager, guiCam);

            sound=settingsManager.getSound();
        }
    }


}
