package com.mikegacek.blackjack;

import com.mikegacek.blackjack.framework.gl.SpriteBatcher;
import com.mikegacek.blackjack.framework.gl.TextureRegion;
import com.mikegacek.blackjack.framework.impl.GLGraphics;

import java.io.Serializable;

/**
 * Created by Michael on 1/18/2017.
 */
public class BetAndCount  implements Serializable {

    private float alpha;
    private transient GLGraphics glGraphics;
    private transient SpriteBatcher batcher;
    private boolean increaseAlpha;

    public BetAndCount(GLGraphics glGraphics, SpriteBatcher batcher) {
        this.glGraphics=glGraphics;
        this.batcher=batcher;
        alpha=0;
        increaseAlpha=false;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float a) {
        alpha=a;
    }

    public void setIncreaseAlpha(boolean b) {
        increaseAlpha=b;
    }

    public void resetAlpha() {
        alpha=0;
        increaseAlpha=false;
    }

    public void drawBetAndCount(String bet, String count, float x, float y, float scaleX, float scaleY,float r, float g, float b,boolean doubled) {
        if(increaseAlpha && alpha!=1) {
            alpha+=.1f;
            if(alpha>1)
                alpha=1;
        }
        else if(alpha!=0){
            alpha-=.1f;
            if(alpha<0)
                alpha=0;
        }

        TextureRegion highlightLeft= new TextureRegion(Assets.highlight,0,0,8,70);
        TextureRegion highlightRight = new TextureRegion(Assets.highlight,62,0,8,70);
        TextureRegion highlight = new TextureRegion(Assets.highlight,32,0,1,70);

        float size=((32+(bet.length()+1)*31+64+127)*scaleX);
        if(doubled ) {
            String originalBet="$"+Integer.parseInt(bet.substring(1))/2;
            if(originalBet.length()!=bet.length())
                size -= 31 * scaleX;
        }

        glGraphics.getGl().glColor4f(1,1,1,alpha);

        batcher.beginBatch(Assets.highlight);
        batcher.drawSprite(x - size / 2 - 4 * scaleX, y, 8 * scaleX, 70 * scaleY, highlightLeft);
        batcher.drawSprite(x, y, size, 70 * scaleY, highlight);
        batcher.drawSprite(x + size / 2 + 4 * scaleX, y, 8 * scaleX, 70 * scaleY, highlightRight);
        batcher.endBatch();

        glGraphics.getGl().glColor4f(1,1,1,1);

        drawNumbers(bet, x - size / 2 + 24 * scaleX, y, 1, 1, 0, scaleX, scaleY);
        //drawNumberEqualDistance(bet, x - size / 2 + 24 * scaleX, y, 1, 1, 0, scaleX, scaleY);
        drawNumbersBackwards(count, x + size / 2 - 24 * scaleX, y, r, g, b, scaleX, scaleY);

    }

    public void drawBet(String bet, float x, float y, float scaleX, float scaleY) {
        float size=findStringSize(bet)*scaleX;
        size+=(bet.length()-1)*6*scaleX;
        x-=size/2;
        x+=(findStringSize(bet.charAt(0)+"")/2)*scaleX;
        drawNumberEqualDistance(bet,x,y,0,1,0,scaleX,scaleY);
    }

    public void drawCount(String count, float x, float y, float scaleX, float scaleY,float size) {
        if(increaseAlpha && alpha!=1) {
            alpha+=.1f;
            if(alpha>1)
                alpha=1;
        }
        else if(alpha!=0){
            alpha-=.1f;
            if(alpha<0)
                alpha=0;
        }

        TextureRegion highlightLeft= new TextureRegion(Assets.highlight,0,0,8,70);
        TextureRegion highlightRight = new TextureRegion(Assets.highlight,62,0,8,70);
        TextureRegion highlight = new TextureRegion(Assets.highlight,32,0,1,70);

        glGraphics.getGl().glColor4f(1,1,1,alpha);

        batcher.beginBatch(Assets.highlight);
        batcher.drawSprite(x - size / 2 - 4 * scaleX, y, 8 * scaleX, 70 * scaleY, highlightLeft);
        batcher.drawSprite(x, y, size, 70 * scaleY, highlight);
        batcher.drawSprite(x + size / 2 + 4 * scaleX, y, 8 * scaleX, 70 * scaleY, highlightRight);
        batcher.endBatch();

        glGraphics.getGl().glColor4f(1,1,1,1);

        drawNumbersCentered(count,x,y,1,1,1,scaleX,scaleY);
    }

    public int drawNumbers(String string, float x, float y,float r, float g, float b,float scaleX, float scaleY) {
        glGraphics.getGl().glColor4f(r, g, b, alpha);
        int size = 0;
        if(string.charAt(0)=='-') {
            int tempInt = Integer.parseInt(string)*-1;
            if(tempInt!=21)
                string=tempInt-10 + "/" + tempInt;
            else
                string=""+tempInt;
        }
        if(string.length()>0) {
            TextureRegion temp = findNumber(string.charAt(0));
            batcher.beginBatch(Assets.numbers);
            for (int i = 0; i < string.length(); i++) {
                batcher.drawSprite(x, y, temp.width * scaleX, temp.height * scaleY, temp);
                size += (temp.width + 6) * scaleX;
                if (i < string.length() - 1) {
                    x +=(temp.width/2)*scaleX;
                    temp = findNumber(string.charAt(i+1));
                    x += (temp.width/2 +6) * scaleX;
                }
            }
            batcher.endBatch();
        }
        glGraphics.getGl().glColor4f(1, 1, 1, 1);

        return size;
    }

    public int drawNumberEqualDistance(String string, float x, float y,float r, float g, float b,float scaleX, float scaleY) {
        glGraphics.getGl().glColor4f(r, g, b, alpha);
        int size = 0;
        if(string.charAt(0)=='-') {
            int tempInt = Integer.parseInt(string)*-1;
            if(tempInt!=21)
                string=tempInt-10 + "/" + tempInt;
            else
                string=""+tempInt;
        }
        if(string.length()>0) {
            TextureRegion temp = findNumber(string.charAt(0));
            batcher.beginBatch(Assets.numbers);
            for (int i = 0; i < string.length(); i++) {
                batcher.drawSprite(x, y, temp.width * scaleX, temp.height * scaleY, temp);
                size += (temp.width + 6) * scaleX;
                if (i < string.length() - 1) {
                    temp = findNumber(string.charAt(i+1));
                    x+=34*scaleX;
                }
            }
            batcher.endBatch();
        }
        glGraphics.getGl().glColor4f(1, 1, 1, 1);

        return size;
    }

    public int drawNumbersBackwards(String string, float x, float y,float r, float g, float b,float scaleX, float scaleY) {
        glGraphics.getGl().glColor4f(r,g,b,alpha);
        int size = 0;
        if(string.charAt(0)=='-') {
            int tempInt = Integer.parseInt(string)*-1;
            if(tempInt!=21)
                string=tempInt-10 + "/" + tempInt;
            else
                string=""+tempInt;
        }
        if(string.length()>0) {
            TextureRegion temp = findNumber(string.charAt(string.length()-1));
            batcher.beginBatch(Assets.numbers);
            for (int i = string.length()-1; i >= 0; i--) {
                batcher.drawSprite(x, y, temp.width * scaleX, temp.height * scaleY, temp);
                size += (temp.width + 6) * scaleX;
                if (i > 0) {
                    x -=(temp.width/2)*scaleX;
                    temp = findNumber(string.charAt(i-1));
                    x -= (temp.width/2 +6) * scaleX;
                }
            }
            batcher.endBatch();
        }
        glGraphics.getGl().glColor4f(r,g,b,1);

        return size;
    }

    public void drawNumbersCentered(String string, float x, float y, float r, float g, float b, float scaleX, float scaleY) {

        if(string.charAt(0)=='-') {
            int tempInt = Integer.parseInt(string)*-1;
            if(tempInt!=21)
                string=tempInt-10 + "/" + tempInt;
            else
                string=""+tempInt;
        }

        float size=findStringSize(string)*scaleX;
        size+=(string.length()-1)*6*scaleX;
        x-=size/2;
        x+=(findStringSize(string.charAt(0)+"")/2)*scaleX;
        drawNumbers(string,x,y,r,g,b,scaleX,scaleY);

    }

    private float findStringSize(String string) {
        float size=0;

        for(int i=0;i<string.length();i++) {
            switch(string.charAt(i)) {
                case '1':
                    size+=22;
                    break;
                case '2':
                    size+=29;
                    break;
                case '-':
                case '+':
                case '3':
                    size+=30;
                    break;
                case '$':
                case '4':
                    size+=32;
                     break;
                case '5':
                case '6':
                case '8':
                case '9':
                case '0':
                    size+=31;
                    break;
                case '7':
                    size+=24;
                    break;
                case '/':
                    size+=25;
                    break;
            }
        }

        return size;
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
            default:
                r= null;
        }

        return r;
    }

    public void load(GLGraphics glGraphics, SpriteBatcher batcher) {
        this.glGraphics=glGraphics;
        this.batcher=batcher;

    }
}
