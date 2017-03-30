package com.mikegacek.blackjack;

import com.mikegacek.blackjack.framework.math.Circle;
import com.mikegacek.blackjack.framework.math.Rectangle;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Michael on 12/26/2016.
 */
public class ChipManager  implements Serializable {

    private static final long serialVersionUID = 14L;

    static final int BET=1;
    static final int REMOVE=-1;

    private ArrayList<Chip> chips;
    private ArrayList<Chip> sideBetLeft;
    private ArrayList<Chip> sideBetRight;
    private ArrayList<Chip> mainBet;
    private int[] sideBetLeftChips = new int[7];
    private int[] sideBetRightChips = new int[7];
    private int[] mainBetChips= new int[7];
    private int[] repeatSBL = new int[7];
    private int[] repeatSBR = new int[7];
    private int[] repeatMainBet = new int[7];
    private int[] values = {1,5,25,100,500,1000,10000};

    private int money,shownMoney,sideBetLeftMoney,sideBetRightMoney,mainBetMoney,playerHandMoney,playerSplitOneMoney,playerSplitTwoMoney,playerSplitThreeMoney,insuranceMoney,surrenderMoney;
    private int chipSelection,chipLocation;
    private transient Button chip1,chip5,chip25,chip100,chip500,chip1000,chip10000,betChips,dealButton,repeat,previousArrow,freeChips;
    private Circle middle;
    private Rectangle left;
    private Rectangle right;
    private int direction;

    public ChipManager() {

        chip1= new Button(67,67,62,580,Assets.chip1);
        chip5= new Button(67,67,131 ,595,Assets.chip5);
        chip25= new Button(67,67,200,605,Assets.chip25);
        chip100 = new Button(67,67,269,610,Assets.chip100);
        chip500= new Button(67,67,338,605,Assets.chip500);
        chip1000=new Button(67,67,407,595,Assets.chip1000);
        chip10000=new Button(67,67,476,580,Assets.chip10000);
        betChips = new Button(64,64,101,510,Assets.addChips);
        repeat = new Button(64,64,437,510,Assets.repeatBet);
        dealButton = new Button(272,103,270,460,Assets.dealUnpressed);
        dealButton.setAlpha(0);
        freeChips = new Button(272,103,270,460,Assets.freeChips);
        freeChips.setAlpha(0);
        previousArrow = new Button(64,64,508,130,Assets.previousArrow);
        chipSelection=0;
        chipLocation=-1;
        money=500;
        shownMoney=money;
        sideBetLeftMoney=0;
        sideBetRightMoney=0;
        mainBetMoney=0;
        playerHandMoney=0;
        playerSplitOneMoney=0;
        playerSplitTwoMoney=0;
        playerSplitThreeMoney=0;
        insuranceMoney=0;
        surrenderMoney=0;
        direction=1;
        chips = new ArrayList<>();
        sideBetLeft = new ArrayList<>();
        sideBetRight = new ArrayList<>();
        mainBet=new ArrayList<>();
        middle = new Circle(270,230,75);
        left = new Rectangle(160-46,230-73.5f,80,147);
        right = new Rectangle(380-34,230-73.5f,80,147);
        direction=BET;
    }

    public Button getChip1() {
        return chip1;
    }

    public Button getChip5() {
        return chip5;
    }

    public Button getChip25() {
        return chip25;
    }

    public Button getChip100() {
        return chip100;
    }

    public Button getChip500() {
        return chip500;
    }

    public Button getChip1000() {
        return chip1000;
    }

    public Button getChip10000() {
        return chip10000;
    }

    public Button getBetChips() { return betChips;}

    public Button getRepeat() { return repeat;}

    public Button getDealButton() { return dealButton;}

    public Button getFreeChips() { return freeChips;}

    public Button getPreviousArrow() { return previousArrow;}

    public int getInsuranceMoney() { return insuranceMoney;}

    public void setInsuranceMoney(int m) { insuranceMoney=m;}

    public int getSurrenderMoney() { return surrenderMoney;}

    public void setSurrenderMoney(int m) { surrenderMoney=m;}

    public int getPlayerHandMoney() {
        return playerHandMoney;
    }

    public void setPlayerHandMoney(int playerHandMoney) {
        this.playerHandMoney = playerHandMoney;
    }

    public void updatePlayerHandMoney() {
        playerHandMoney=0;
        playerSplitOneMoney=0;
        playerSplitTwoMoney=0;
        playerSplitThreeMoney=0;
        for(int i=0;i<mainBetChips.length;i++) {
            playerHandMoney+=values[i]*mainBetChips[i];
        }
        surrenderMoney=mainBetMoney-mainBetMoney/2;
    }

    public int getPlayerSplitOneMoney() {
        return playerSplitOneMoney;
    }

    public void setPlayerSplitOneMoney(int playerSplitOneMoney) {
        this.playerSplitOneMoney = playerSplitOneMoney;
    }

    public int getPlayerSplitTwoMoney() {
        return playerSplitTwoMoney;
    }

    public void setPlayerSplitTwoMoney(int playerSplitTwoMoney) {
        this.playerSplitTwoMoney = playerSplitTwoMoney;
    }

    public int getPlayerSplitThreeMoney() {
        return playerSplitThreeMoney;
    }

    public void setPlayerSplitThreeMoney(int playerSplitThreeMoney) {
        this.playerSplitThreeMoney = playerSplitThreeMoney;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getShownMoney() { return shownMoney;}

    public void setShownMoney(int money) { shownMoney=money;}

    public int getMainBetMoney() { return mainBetMoney;}

    public void setMainBetMoney(int m) {mainBetMoney=m;}

    public int getSideBetLeftMoney() {
        return sideBetLeftMoney;
    }

    public void setSideBetLeftMoney(int sideBetLeftMoney) {
        this.sideBetLeftMoney = sideBetLeftMoney;
    }

    public int getSideBetRightMoney() {
        return sideBetRightMoney;
    }

    public void setSideBetRightMoney(int sideBetRightMoney) {
        this.sideBetRightMoney = sideBetRightMoney;
    }

    public int getTotalMoney() { return money+mainBetMoney+sideBetLeftMoney+sideBetRightMoney;}

    public ArrayList<Chip> getChips() {
        return chips;
    }

    public ArrayList<Chip> getSideBetLeft() {
        return sideBetLeft;
    }

    public ArrayList<Chip> getSideBetRight() {
        return sideBetRight;
    }

    public ArrayList<Chip> getMainBet() {
        return mainBet;
    }

    public int[] getMainBetChips() {
        return mainBetChips;
    }

    public int[] getSideBetLeftChips() {
        return sideBetLeftChips;
    }

    public int[] getSideBetRightChips() {
        return sideBetRightChips;
    }

    public Rectangle getLeft() {
        return left;
    }

    public Rectangle getRight() {
        return right;
    }

    public Circle getMiddle() {
        return middle;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getChipSelection() {
        return chipSelection;
    }

    public int getChipLocation() {
        return chipLocation;
    }

    public void setChipSelection(int s) {
        chipSelection=s;
    }
    public void setChipLocation(int s) {
        chipLocation=s;
    }

    public void updateChips() {
        for(Chip chip:sideBetLeft) {
            chip.move(.5f);
        }
        for(Chip chip:sideBetRight) {
            chip.move(.5f);
        }
        for(Chip chip: mainBet) {
            chip.move(.5f);
        }
        for(int i=0;i<chips.size();i++) {
            chips.get(i).move(.5f);
            if(chips.get(i).getCurrentY()<=-50 || chips.get(i).getCurrentY()>=1100) {
                if(chips.get(i).getCurrentY()<=-50 && chips.get(i).getMoneyChanged()==false) {
                    money+=chips.get(i).getValue();
                    chips.get(i).setMoneyChanged(true);
                }
                chips.remove(i);
                i--;
            }
        }
    }

    public void updateMoney() {
        mainBetMoney=0;
        sideBetLeftMoney=0;
        sideBetRightMoney=0;
        for(int i=0;i<mainBetChips.length;i++) {
            mainBetMoney+=mainBetChips[i]*values[i];
        }
        for(int i=0;i<sideBetLeftChips.length;i++) {
            sideBetLeftMoney+=sideBetLeftChips[i]*values[i];
        }
        for(int i=0;i<sideBetRightChips.length;i++) {
            sideBetRightMoney+=sideBetRightChips[i]*values[i];
        }
    }


    public void updateButtons() {
        // ADD/REMOVE CHIPS
        if(direction==BET && betChips.getScaleX()<1) {
            betChips.setScaleX(betChips.getScaleX()+.2f);
            if(betChips.getScaleX()>0)
                betChips.setTextureRegion(Assets.addChips);
            if(betChips.getScaleX()>1)
                betChips.setScaleX(1);
        }
        else if(direction==REMOVE && betChips.getScaleX()>-1) {
            betChips.setScaleX(betChips.getScaleX()-.2f);
            if(betChips.getScaleX()<0)
                betChips.setTextureRegion(Assets.removeChips);
            if(betChips.getScaleX()<-1)
                betChips.setScaleX(-1);
        }

        //DEAL BUTTON
        if(mainBet.size()==0) {
            if(dealButton.getAlpha()>0)
                dealButton.setAlpha(dealButton.getAlpha() - .2f);
            if(dealButton.getAlpha()<0)
                dealButton.setAlpha(0);
        }
        else {
            if(dealButton.getAlpha()<1)
                dealButton.setAlpha(dealButton.getAlpha()+.2f);
            if(dealButton.getAlpha()>1)
                dealButton.setAlpha(1);
        }
    }

    public void addChips(int selection, int loc)
    {
        if(values[loc]>money) {
            loc--;
            if(money>0)
                Assets.largeStackOfChips.play(MainScreen.sound);
            while(money>0) {
                if(values[loc]<=money)
                    createChip(selection,loc);
                else
                    loc--;
            }
        }
        else if(money>0) {
            createChip(selection, loc);
            int random=(int)(Math.random()*3+1);
            switch(random) {
                case 1:
                    Assets.singleChip1.play(MainScreen.sound);
                    break;
                case 2:
                    Assets.singleChip2.play(MainScreen.sound);
                    break;
                case 3:
                    Assets.singleChip3.play(MainScreen.sound);
                    break;
            }
        }
    }


    public void createChip(int selection,int loc) {
        float angle,h,x,y;

        if(selection<=2) {
            x = (int) (Math.random() * 27)-13;
            y = (int) (Math.random()* 103)-51;
        }
        else {
            angle = (float) Math.random()*361;
            h = (int) (Math.random() * 53);
            x=(float)Math.cos(angle)*h;
            y=(float)Math.sin(angle)*h;
        }

        switch(selection) {
            case 1:
                sideBetLeft.add(new Chip(loc,270,-16,149+x+(Math.abs(y)*.15f),230+y));
                sideBetLeftChips[loc]++;
                checkCreateChip(sideBetLeft,sideBetLeftChips,1,loc);
                break;
            case 2:
                sideBetRight.add(new Chip(loc,270,-16,391+x-(Math.abs(y)*.15f),230+y));
                sideBetRightChips[loc]++;
                checkCreateChip(sideBetRight,sideBetRightChips,2,loc);
                break;
            case 3:
                mainBet.add(new Chip(loc,270,-16,270+x,230+y));
                mainBetChips[loc]++;
                checkCreateChip(mainBet,mainBetChips,3,loc);
                break;
        }
        money-=values[loc];

    }

    public void createChip(int selection,int loc,int index) {
        float angle,h,x,y;

        if(selection<=2) {
            x = (int) (Math.random() * 27)-13;
            y = (int) (Math.random()* 103)-51;
        }
        else {
            angle = (float) Math.random()*361;
            h = (int) (Math.random() * 53);
            x=(float)Math.cos(angle)*h;
            y=(float)Math.sin(angle)*h;
        }

        switch(selection) {
            case 1:
                sideBetLeft.add(index,new Chip(loc,270,-16,149+x+(Math.abs(y)*.15f),230+y));
                sideBetLeftChips[loc]++;
                checkCreateChip(sideBetLeft,sideBetLeftChips,1,loc);
                break;
            case 2:
                sideBetRight.add(index,new Chip(loc,270,-16,391+x-(Math.abs(y)*.15f),230+y));
                sideBetRightChips[loc]++;
                checkCreateChip(sideBetRight,sideBetRightChips,2,loc);
                break;
            case 3:
                mainBet.add(index,new Chip(loc,270,-16,270+x,230+y));
                mainBetChips[loc]++;
                checkCreateChip(mainBet,mainBetChips,3,loc);
                break;
        }


    }

    public void payout(ArrayList<Chip> chips,int[] list,int selection, int payoutAmount) {
        if(payoutAmount>=0) {
            payoutChips(selection,payoutAmount);
        }
        else {
            payoutAmount*=-1;
            int i=6;
            while(payoutAmount!=0) {
                if(values[i]<=payoutAmount) {
                    removeChipsToDealer(chips, list, selection, i, values[i]);
                    payoutAmount-=values[i];
                }
                else
                    i--;
            }
        }
    }

    private void payoutChips(int selection,int payoutAmount) {
        float angle,h,x,y;
        int i=6;
        int[] create = new int[7];

        while(payoutAmount!=0) {
            if(values[i]<=payoutAmount) {
                create[i]++;
                payoutAmount-=values[i];
            }
            else
                i--;
        }

        switch(selection) {
            case 1:
                for(int j=0;j<create.length;j++) {
                    for(int k=create[j];k>0;k--) {
                        x = (int) (Math.random() * 27)-13;
                        y = (int) (Math.random()* 103)-51;
                        sideBetLeft.add(new Chip(j, 269+((j-3)*69), 1000, 149 + x + (Math.abs(y) * .15f), 230 + y));
                        sideBetLeftChips[j]++;
                    }
                }
                break;
            case 2:
                for(int j=0;j<create.length;j++) {
                    for(int k=create[j];k>0;k--) {
                        x = (int) (Math.random() * 27)-13;
                        y = (int) (Math.random()* 103)-51;
                        sideBetRight.add(new Chip(j, 269+((j-3)*69), 1000, 391 + x - (Math.abs(y) * .15f), 230 + y));
                        sideBetRightChips[j]++;
                    }
                }
                break;
            case 3:
                for(int j=0;j<create.length;j++) {
                    for(int k=create[j];k>0;k--) {
                        angle = (float) Math.random()*361;
                        h = (int) (Math.random() * 53);
                        x=(float)Math.cos(angle)*h;
                        y=(float)Math.sin(angle)*h;
                        mainBet.add(new Chip(j, 269+((j-3)*69), 1000, 270 + x, 230 + y));
                        mainBetChips[j]++;
                    }
                }
                break;
            case 4: //Directly to player chip pool from slot machine
                for(int j=0;j<create.length;j++) {
                    for(int k=create[j];k>0;k--) {
                        x=270;
                        y=-100;
                        chips.add(new Chip(j,269+((j-3)*69) +(int)(Math.random()*128)-64,1000,x,y));
                    }
                }
        }
    }

    public void doubleBet(int amount) {
        float angle,h,x,y;
        int i=6;
        int[] create = new int[7];
        money-=amount;
        Assets.largeStackOfChips.play(MainScreen.sound);
        while(amount!=0) {
            if(values[i]<=amount) {
                create[i]++;
                amount-=values[i];
            }
            else
                i--;
        }

        for(int j=0;j<create.length;j++) {
            for(int k=create[j];k>0;k--) {
                angle = (float) Math.random()*361;
                h = (int) (Math.random() * 53);
                x=(float)Math.cos(angle)*h;
                y=(float)Math.sin(angle)*h;
                mainBet.add(new Chip(j, 270, -16, 270 + x, 230 + y));
                mainBetChips[j]++;
            }
        }

    }

    private void checkCreateChip(ArrayList<Chip> c,int[] list, int selection,int loc) {
        //remove 5 add 1 of next
        if(loc==0 || loc==1 || loc==3) {
            if(list[loc]==11) {
                for(int i=0;list[loc]!=6;i++) {
                    if(c.get(i).getId()==loc) {
                        c.get(i).setNewPos(270,-50);
                        money+=c.get(i).getValue();
                        c.get(i).setMoneyChanged(true);
                        chips.add(c.get(i));
                        c.remove(i);
                        list[loc]--;
                        i--;
                    }
                }
                createChip(selection,loc+1);
            }
        }
        //remove 4 add 1
        else if(loc==2 || loc==4) {
            if(list[loc]==11) {
                for(int i=0;list[loc]!=7;i++) {
                    if(c.get(i).getId()==loc) {
                        c.get(i).setNewPos(270,-50);
                        money+=c.get(i).getValue();
                        c.get(i).setMoneyChanged(true);
                        chips.add(c.get(i));
                        c.remove(i);
                        list[loc]--;
                        i--;
                    }
                }
                createChip(selection,loc+1);
                if(loc==4)
                    createChip(selection,loc+1);
            }
        }
        //remove 10 add 1
        else if(loc==5) {
            if(list[loc]==11) {
                for(int i=0;list[loc]!=1;i++) {
                    if(c.get(i).getId()==loc) {
                        c.get(i).setNewPos(270,-50);
                        money+=c.get(i).getValue();
                        c.get(i).setMoneyChanged(true);
                        chips.add(c.get(i));
                        c.remove(i);
                        list[loc]--;
                        i--;
                    }
                }
                createChip(selection,loc+1);
            }
        }
    }

    public void removeChip(int selection, int loc) {
        int random=(int)(Math.random()*3+1);

        switch(selection) {
            case 1:
                if(!sideBetLeft.isEmpty() && values[loc]>sideBetLeftMoney && sideBetLeft.size()>1) {
                    Assets.largeStackOfChips.play(MainScreen.sound);
                } else if(!sideBetLeft.isEmpty()) {
                    switch(random) {
                        case 1:
                            Assets.singleChip1.play(MainScreen.sound);
                            break;
                        case 2:
                            Assets.singleChip2.play(MainScreen.sound);
                            break;
                        case 3:
                            Assets.singleChip3.play(MainScreen.sound);
                            break;
                    }
                }
                removeChips(sideBetLeft,sideBetLeftChips,selection,loc,chipSelection);
                break;
            case 2:
                if(!sideBetRight.isEmpty() && values[loc]>sideBetRightMoney && sideBetRight.size()>1) {
                    Assets.largeStackOfChips.play(MainScreen.sound);
                } else if(!sideBetRight.isEmpty()) {
                    switch(random) {
                        case 1:
                            Assets.singleChip1.play(MainScreen.sound);
                            break;
                        case 2:
                            Assets.singleChip2.play(MainScreen.sound);
                            break;
                        case 3:
                            Assets.singleChip3.play(MainScreen.sound);
                            break;
                    }
                }
                removeChips(sideBetRight, sideBetRightChips, selection, loc,chipSelection);
                break;
            case 3:
                if(!mainBet.isEmpty() && values[loc]>mainBetMoney && mainBet.size()>1) {
                    Assets.largeStackOfChips.play(MainScreen.sound);
                } else if(!mainBet.isEmpty()) {
                    switch(random) {
                        case 1:
                            Assets.singleChip1.play(MainScreen.sound);
                            break;
                        case 2:
                            Assets.singleChip2.play(MainScreen.sound);
                            break;
                        case 3:
                            Assets.singleChip3.play(MainScreen.sound);
                            break;
                    }
                }
                removeChips(mainBet,mainBetChips,selection,loc,chipSelection);
                break;
        }
    }

    public void removeAllChipsFromSelection(int selection) {
        switch(selection) {
            case 1:
                remove(sideBetLeft,sideBetLeftChips);
                break;
            case 2:
                remove(sideBetRight,sideBetRightChips);
                break;
            case 3:
                remove(mainBet,mainBetChips);
                break;
        }
    }

    private void removeChips(ArrayList<Chip> c,int[] list, int selection,int loc,int chipAmount) {
        int amount=chipAmount;
        int remaining=chipAmount;
        int total=0;
        int[] create = new int[7];
        int caseNum=0;
        ArrayList<Chip> temp = new ArrayList<>();
        //find which case to go to case 1) if there are enough chips = or lower than the loc, case 2) if there is a chip higher than loc that can be removed and add appropriate chips, case 0) not enough chips and remove everything
        for(int i=loc;i>=0 && caseNum==0;i--) {
            total+=list[i]*values[i];
            if(total>=amount)
                caseNum=1;
        }
        if(caseNum==0) {
            total=0;
            for(int i=loc;i<list.length && caseNum==0;i++) {
                total+=list[i]*values[i];
                if(total>=amount)
                    caseNum=2;
            }
        }
        if(caseNum==0) {
            total=0;
            for(int i=0;i<list.length;i++) {
                total+=list[i];
            }
            if(total==0)
                caseNum=3;
        }
        //cases
        switch(caseNum) {
            //remove all chips
            case 0:
                while(!c.isEmpty()) {
                    c.get(0).setNewPos(270,-50);
                    money+=c.get(0).getValue();
                    c.get(0).setMoneyChanged(true);
                    chips.add(c.get(0));
                    c.remove(0);
                }

                for(int i=0;i<list.length;i++){
                    list[i]=0;
                }
                break;
            //remove all lower chips until remaining=0
            case 1:
                while(remaining!=0) {
                    if(list[loc]!=0 && values[loc]<=remaining) {
                        list[loc]--;
                        remaining-=values[loc];
                        //finds a chip in the list with equal id
                        for(int i=0;i<c.size();i++) {
                            if(c.get(i).getId()==loc) {
                                c.get(i).setNewPos(270,-50);
                                money+=c.get(i).getValue();
                                c.get(i).setMoneyChanged(true);
                                chips.add(c.get(i));
                                c.remove(i);
                                i=c.size();
                            }
                        }
                    }
                    else {
                        loc--;
                    }
                }
                break;
            //find a bigger chip remove it and add appropriate chips to make up the difference
            case 2:
                for(int i=loc;i<list.length;i++) {
                    if(list[i]>0) {
                        list[i]--;
                        remaining=Math.abs(remaining-values[i]);
                        for(int j=0;j<c.size();j++) {
                            if(c.get(j).getId()==i) {
                                c.get(j).setNewPos(270, -50);
                                money+=c.get(j).getValue();
                                c.get(j).setMoneyChanged(true);
                                chips.add(c.get(j));
                                c.remove(j);
                                j=c.size();
                            }
                        }
                        loc=i;
                        i=list.length;
                    }
                }
                //find the chips to create
                while(remaining!=0) {
                    if(values[loc]<=remaining) {
                        remaining-=values[loc];
                        create[loc]++;
                    }
                    else if(values[loc]>remaining){
                        loc--;
                    }
                }
                //this is done so the larger chips can be shown on top for large breaks
                for(int i=0;i<create.length;i++) {
                    while(create[i]>0) {
                        //add a chip
                        createChip(selection, i);
                        create[i]--;
                    }
                }
                break;
            //there are no chips in the selection
            case 3:
                //popup to show that there are no chips to remove
        }



    }

    private void removeChipsToDealer(ArrayList<Chip> c,int[] list, int selection,int loc,int chipAmount) {
        int amount=chipAmount;
        int remaining=chipAmount;
        int total=0;
        int[] create = new int[7];
        int caseNum=0;
        ArrayList<Chip> temp = new ArrayList<>();
        //find which case to go to case 1) if there are enough chips = or lower than the loc, case 2) if there is a chip higher than loc that can be removed and add appropriate chips, case 0) not enough chips and remove everything
        for(int i=loc;i>=0 && caseNum==0;i--) {
            total+=list[i]*values[i];
            if(total>=amount)
                caseNum=1;
        }
        if(caseNum==0) {
            total=0;
            for(int i=loc;i<list.length && caseNum==0;i++) {
                total+=list[i]*values[i];
                if(total>=amount)
                    caseNum=2;
            }
        }
        if(caseNum==0) {
            total=0;
            for(int i=0;i<list.length;i++) {
                total+=list[i];
            }
            if(total==0)
                caseNum=3;
        }
        //cases
        switch(caseNum) {
            //remove all chips
            case 0:
                while(!c.isEmpty()) {
                    c.get(0).setNewPos(270,1100);
                    chips.add(c.get(0));
                    c.remove(0);
                }

                for(int i=0;i<list.length;i++){
                    list[i]=0;
                }
                break;
            //remove all lower chips until remaining=0
            case 1:
                while(remaining!=0) {
                    if(list[loc]!=0 && values[loc]<=remaining) {
                        list[loc]--;
                        remaining-=values[loc];
                        //finds a chip in the list with equal id
                        for(int i=0;i<c.size();i++) {
                            if(c.get(i).getId()==loc) {
                                c.get(i).setNewPos(270,1100);
                                chips.add(c.get(i));
                                c.remove(i);
                                i=c.size();
                            }
                        }
                    }
                    else {
                        loc--;
                    }
                }
                break;
            //find a bigger chip remove it and add appropriate chips to make up the difference
            case 2:
                for(int i=loc;i<list.length;i++) {
                    if(list[i]>0) {
                        list[i]--;
                        remaining=Math.abs(remaining-values[i]);
                        for(int j=0;j<c.size();j++) {
                            if(c.get(j).getId()==i) {
                                c.get(j).setNewPos(270, 1100);
                                chips.add(c.get(j));
                                c.remove(j);
                                j=c.size();
                            }
                        }
                        loc=i;
                        i=list.length;
                    }
                }
                //find the chips to create
                while(remaining!=0) {
                    if(values[loc]<=remaining) {
                        remaining-=values[loc];
                        create[loc]++;
                    }
                    else if(values[loc]>remaining){
                        loc--;
                    }
                }
                //this is done so the larger chips can be shown on top for large breaks
                for(int i=0;i<create.length;i++) {
                    while(create[i]>0) {
                        //add a chip

                        payoutChips(selection,values[i]);
                        create[i]--;
                    }
                }
                break;
            //there are no chips in the selection
            case 3:
                //popup to show that there are no chips to remove
        }



    }

    public void removeAllChips() {
        remove(mainBet,mainBetChips);
        remove(sideBetLeft,sideBetLeftChips);
        remove(sideBetRight,sideBetRightChips);
        sideBetLeftMoney=0;
        sideBetRightMoney=0;
        mainBetMoney=0;
    }

    private void remove(ArrayList<Chip> c, int[] list) {
        while(!c.isEmpty()) {
            c.get(0).setNewPos(270,-50);
            chips.add(c.get(0));
            c.remove(0);
        }

        for(int i=0;i<list.length;i++){
            list[i]=0;
        }
    }

    public void saveRepeat() {
        for(int i=0;i<repeatSBL.length;i++) {
            repeatSBL[i]=sideBetLeftChips[i];
        }
        for(int i=0;i<repeatSBR.length;i++) {
            repeatSBR[i]=sideBetRightChips[i];
        }
        for(int i=0;i<repeatMainBet.length;i++) {
            repeatMainBet[i]=mainBetChips[i];
        }

    }

    private void repeat(int[] r, int[] c,ArrayList<Chip> list,int loc) {
        for(int i=0;i<r.length;i++) {
            while(r[i]!=c[i]) {
                if (r[i] > c[i]) {
                    createChip(loc, i);
                } else if (r[i] < c[i]) {
                    for (int j = 0; j < list.size(); j++) {
                        if (list.get(j).getId() == i) {
                            list.get(j).setNewPos(270, -50);
                            chips.add(list.get(j));
                            list.remove(j);
                            j = list.size();
                            c[i]--;
                        }
                    }
                }
            }
        }
    }

    public void repeatBet() {
        //compare the old with current and match for each side
        //compare to left
        if(repeatTotal()!=0)
            Assets.largeStackOfChips.play(MainScreen.sound);

        repeat(repeatSBL,sideBetLeftChips,sideBetLeft,1);
        repeat(repeatSBR, sideBetRightChips, sideBetRight,2);
        repeat(repeatMainBet, mainBetChips, mainBet, 3);
    }

    public int repeatTotal() {
        int repeat=0;
        for(int i=0;i<repeatSBL.length;i++) {
            repeat+=values[i]*repeatSBL[i];
        }
        for(int i=0;i<repeatSBR.length;i++) {
            repeat+=values[i]*repeatSBR[i];
        }
        for(int i=0;i<repeatMainBet.length;i++) {
            repeat+=values[i]*repeatMainBet[i];
        }


        repeat-=sideBetLeftMoney+sideBetRightMoney+mainBetMoney;

        return repeat;
    }

    public void loadData() {
        chip1= new Button(67,67,62,580,Assets.chip1);
        chip5= new Button(67,67,131 ,595,Assets.chip5);
        chip25= new Button(67,67,200,605,Assets.chip25);
        chip100 = new Button(67,67,269,610,Assets.chip100);
        chip500= new Button(67,67,338,605,Assets.chip500);
        chip1000=new Button(67,67,407,595,Assets.chip1000);
        chip10000=new Button(67,67,476,580,Assets.chip10000);
        betChips = new Button(64,64,101,510,Assets.addChips);
        repeat = new Button(64,64,437,510,Assets.repeatBet);
        dealButton = new Button(272,103,270,460,Assets.dealUnpressed);
        dealButton.setAlpha(0);
        freeChips = new Button(272,103,270,460,Assets.freeChips);
        freeChips.setAlpha(0);
        previousArrow = new Button(64,64,508,130,Assets.previousArrow);

        for(Chip chip: chips) {
            chip.loadTexture();
        }
        for(Chip chip: sideBetLeft) {
            chip.loadTexture();
        }
        for(Chip chip: sideBetRight) {
            chip.loadTexture();
        }
        for(Chip chip: mainBet) {
            chip.loadTexture();
        }
    }

}
