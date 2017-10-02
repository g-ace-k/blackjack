package com.mikegacek.blackjack;

import com.mikegacek.blackjack.framework.gl.SpriteBatcher;
import com.mikegacek.blackjack.framework.gl.TextureRegion;
import com.mikegacek.blackjack.framework.impl.GLGraphics;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by Michael on 4/1/2017.
 */

public class CalibriFont{

    static int drawNumbers(String string, float x, float y,float r, float g, float b,float a,float scaleX, float scaleY,SpriteBatcher batcher, GLGraphics glGraphics) {
        glGraphics.getGl().glColor4f(r, g, b, a);
        int size = 0;
        //remove - if soft hand
        if(string.charAt(0)=='-' && string.charAt(1)!='$') {
            int tempInt = Integer.parseInt(string)*-1;
            if(tempInt!=21)
                string=tempInt-10 + "/" + tempInt;
            else
                string=""+tempInt;
        }
        if(string.length()>0) {
            TextureRegion temp = getCharacter(string.charAt(0));
            batcher.beginBatch(Assets.calibriText);
            for (int i = 0; i < string.length(); i++) {
                batcher.drawSprite(x, y, temp.width * scaleX, temp.height * scaleY, temp);
                size += (temp.width + 8) * scaleX;
                if (i < string.length() - 1) {
                    x +=(temp.width/2)*scaleX;
                    temp = getCharacter(string.charAt(i+1));
                    if(string.charAt(i+1)==',' || string.charAt(i)==',')
                        x-=6*scaleX;
                    x += (temp.width/2 +8) * scaleX;
                }
            }
            batcher.endBatch();
        }
        glGraphics.getGl().glColor4f(1, 1, 1, 1);

        return size;
    }

    static int drawNumberEqualDistance(String string, float x, float y, float r, float g, float b,float a, float scaleX, float scaleY, SpriteBatcher batcher, GLGraphics glGraphics) {
        glGraphics.getGl().glColor4f(r, g, b, a);
        int size = 0;
        //remove - if soft hand
        if(string.charAt(0)=='-') {
            int tempInt = Integer.parseInt(string)*-1;
            if(tempInt!=21)
                string=tempInt-10 + "/" + tempInt;
            else
                string=""+tempInt;
        }
        if(string.length()>0) {
            TextureRegion temp = getCharacter(string.charAt(0));
            batcher.beginBatch(Assets.calibriText);
            for (int i = 0; i < string.length(); i++) {
                batcher.drawSprite(x, y, temp.width * scaleX, temp.height * scaleY, temp);
                size += (temp.width + 8) * scaleX;
                if (i < string.length() - 1) {
                    temp = getCharacter(string.charAt(i+1));
                    if(string.charAt(i+1)==',' || string.charAt(i)==',') //adding commas to seperate large numbers, will need reduced spacing
                        x+=30*scaleX;
                    else
                        x+=46*scaleX;
                }
            }
            batcher.endBatch();
        }
        glGraphics.getGl().glColor4f(1, 1, 1, 1);

        return size;
    }

    static int drawNumbersBackwards(String string, float x, float y,float r, float g, float b,float a,float scaleX, float scaleY,SpriteBatcher batcher,GLGraphics glGraphics) {
        glGraphics.getGl().glColor4f(r,g,b,a);
        int size = 0;
        if(string.charAt(0)=='-') {
            int tempInt = Integer.parseInt(string)*-1;
            if(tempInt!=21)
                string=tempInt-10 + "/" + tempInt;
            else
                string=""+tempInt;
        }
        if(string.length()>0) {
            TextureRegion temp = getCharacter(string.charAt(string.length()-1));
            batcher.beginBatch(Assets.calibriText);
            for (int i = string.length()-1; i >= 0; i--) {
                batcher.drawSprite(x, y, temp.width * scaleX, temp.height * scaleY, temp);
                size += (temp.width + 8) * scaleX;
                if (i > 0) {
                    x -=(temp.width/2)*scaleX;
                    temp = getCharacter(string.charAt(i-1));
                    if(string.charAt(i-1)==',' || string.charAt(i)==',')
                        x-=4*scaleX;
                    x -= (temp.width/2 +8) * scaleX;
                }
            }
            batcher.endBatch();
        }
        glGraphics.getGl().glColor4f(r,g,b,1);

        return size;
    }

    static int drawNumbersBackwardsAndEqualDistance(String string, float x, float y,float r, float g, float b,float a,float scaleX, float scaleY,SpriteBatcher batcher,GLGraphics glGraphics) {
        glGraphics.getGl().glColor4f(r,g,b,a);
        int size = 0;
        if(string.charAt(0)=='-') {
            int tempInt = Integer.parseInt(string)*-1;
            if(tempInt!=21)
                string=tempInt-10 + "/" + tempInt;
            else
                string=""+tempInt;
        }
        if(string.length()>0) {
            TextureRegion temp = getCharacter(string.charAt(string.length()-1));
            batcher.beginBatch(Assets.calibriText);
            for (int i = string.length()-1; i >= 0; i--) {
                batcher.drawSprite(x, y, temp.width * scaleX, temp.height * scaleY, temp);
                size += (temp.width + 8) * scaleX;
                if (i > 0) {
                    temp = getCharacter(string.charAt(i-1));
                    if(string.charAt(i-1)==',' || string.charAt(i)==',') //adding commas to seperate large numbers, will need reduced spacing
                        x-=30*scaleX;
                    else
                        x-=46*scaleX;
                }
            }
            batcher.endBatch();
        }

        glGraphics.getGl().glColor4f(r,g,b,1);

        return size;
    }

    static void drawNumbersCentered(String string, float x, float y, float r, float g, float b, float a,float scaleX, float scaleY, SpriteBatcher batcher, GLGraphics glGraphics) {

        //remove - when looking for soft hands
        if(string.charAt(0)=='-' && string.charAt(1)!='$') {
            int tempInt = Integer.parseInt(string)*-1;
            if(tempInt!=21)
                string=tempInt-10 + "/" + tempInt;
            else
                string=""+tempInt;
        }

        float size=getStringSize(string)*scaleX;
        size+=(string.length()-1)*8*scaleX;
        x-=size/2;
        x+=(getStringSize(string.charAt(0)+"")/2)*scaleX;
        drawNumbers(string,x,y,r,g,b,a,scaleX,scaleY,batcher,glGraphics);

    }

    static float getStringSize(String string) {
        float size=0;

        for(int i=0;i<string.length();i++) {
            size+=getCharacter(string.charAt(i)).width;
        }

        return size;
    }

    static TextureRegion getCharacter(char c) {
        TextureRegion r;

        int y;

        if((c>='a' && c<='z') || c==',' || c=='.')
            y=178;
        else if(c>='A' && c<='Z')
            y=92;
        else
            y=0;
        if(y==178) {
            switch(c) {
                case 'a':
                    r=new TextureRegion(Assets.calibriText,0,y,34,86);
                    break;
                case 'b':
                    r=new TextureRegion(Assets.calibriText,46,y,40,86);
                    break;
                case 'c':
                    r=new TextureRegion(Assets.calibriText,94,y,34,86);
                    break;
                case 'd':
                    r=new TextureRegion(Assets.calibriText,134,y,42,86);
                    break;
                case 'e':
                    r=new TextureRegion(Assets.calibriText,186,y,40,86);
                    break;
                case 'f':
                    r=new TextureRegion(Assets.calibriText,230,y,30,86);
                    break;
                case 'g':
                    r=new TextureRegion(Assets.calibriText,260,y,42,86);
                    break;
                case 'h':
                    r=new TextureRegion(Assets.calibriText,306,y,38,86);
                    break;
                case 'i':
                    r=new TextureRegion(Assets.calibriText,356,y,10,86);
                    break;
                case 'j':
                    r=new TextureRegion(Assets.calibriText,370,y,20,86);
                    break;
                case 'k':
                    r=new TextureRegion(Assets.calibriText,402,y,36,86);
                    break;
                case 'l':
                    r=new TextureRegion(Assets.calibriText,446,y,8,86);
                    break;
                case 'm':
                    r=new TextureRegion(Assets.calibriText,468,y,64,86);
                    break;
                case 'n':
                    r=new TextureRegion(Assets.calibriText,544,y,38,86);
                    break;
                case 'o':
                    r=new TextureRegion(Assets.calibriText,592,y,44,86);
                    break;
                case 'p':
                    r=new TextureRegion(Assets.calibriText,646,y,40,86);
                    break;
                case 'q':
                    r=new TextureRegion(Assets.calibriText,694,y,40,86);
                    break;
                case 'r':
                    r=new TextureRegion(Assets.calibriText,746,y,26,86);
                    break;
                case 's':
                    r=new TextureRegion(Assets.calibriText,774,y,32,86);
                    break;
                case 't':
                    r=new TextureRegion(Assets.calibriText,808,y,30,86);
                    break;
                case 'u':
                    r=new TextureRegion(Assets.calibriText,846,y,38,86);
                    break;
                case 'v':
                    r=new TextureRegion(Assets.calibriText,892,y,42,86);
                    break;
                case 'w':
                    r=new TextureRegion(Assets.calibriText,936,y,66,86);
                    break;
                case 'x':
                    r=new TextureRegion(Assets.calibriText,1004,y,40,86);
                    break;
                case 'y':
                    r=new TextureRegion(Assets.calibriText,1046,y,42,86);
                    break;
                case 'z':
                    r=new TextureRegion(Assets.calibriText,1090,y,32,86);
                    break;
                case ',':
                    r=new TextureRegion(Assets.calibriText,1126,y,18,86);
                    break;
                case '.':
                    r=new TextureRegion(Assets.calibriText,1156,y,12,86);
                    break;
                default:
                    r= new TextureRegion(Assets.calibriText,756,0,20,86);
            }
        }
        else if(y==92){
            switch(c) {
                case 'A':
                    r=new TextureRegion(Assets.calibriText,0,y,50,86);
                    break;
                case 'B':
                    r=new TextureRegion(Assets.calibriText,58,y,42,86);
                    break;
                case 'C':
                    r=new TextureRegion(Assets.calibriText,108,y,46,86);
                    break;
                case 'D':
                    r=new TextureRegion(Assets.calibriText,162,y,48,86);
                    break;
                case 'E':
                    r=new TextureRegion(Assets.calibriText,222,y,34,86);
                    break;
                case 'F':
                    r=new TextureRegion(Assets.calibriText,268,y,34,86);
                    break;
                case 'G':
                    r=new TextureRegion(Assets.calibriText,308,y,50,86);
                    break;
                case 'H':
                    r=new TextureRegion(Assets.calibriText,372,y,46,86);
                    break;
                case 'I':
                    r=new TextureRegion(Assets.calibriText,432,y,10,86);
                    break;
                case 'J':
                    r=new TextureRegion(Assets.calibriText,448,y,24,86);
                    break;
                case 'K':
                    r=new TextureRegion(Assets.calibriText,486,y,42,86);
                    break;
                case 'L':
                    r=new TextureRegion(Assets.calibriText,536,y,34,86);
                    break;
                case 'M':
                    r=new TextureRegion(Assets.calibriText,576,y,68,86);
                    break;
                case 'N':
                    r=new TextureRegion(Assets.calibriText,658,y,48,86);
                    break;
                case 'O':
                    r=new TextureRegion(Assets.calibriText,718,y,56,86);
                    break;
                case 'P':
                    r=new TextureRegion(Assets.calibriText,784,y,40,86);
                    break;
                case 'Q':
                    r=new TextureRegion(Assets.calibriText,830,y,64,86);
                    break;
                case 'R':
                    r=new TextureRegion(Assets.calibriText,898,y,42,86);
                    break;
                case 'S':
                    r=new TextureRegion(Assets.calibriText,944,y,40,86);
                    break;
                case 'T':
                    r=new TextureRegion(Assets.calibriText,986,y,46,86);
                    break;
                case 'U':
                    r=new TextureRegion(Assets.calibriText,1040,y,48,86);
                    break;
                case 'V':
                    r=new TextureRegion(Assets.calibriText,1094,y,54,86);
                    break;
                case 'W':
                    r=new TextureRegion(Assets.calibriText,1150,y,82,86);
                    break;
                case 'X':
                    r=new TextureRegion(Assets.calibriText,1234,y,48,86);
                    break;
                case 'Y':
                    r=new TextureRegion(Assets.calibriText,1284,y,44,86);
                    break;
                case 'Z':
                    r=new TextureRegion(Assets.calibriText,1332,y,42,86);
                    break;
                default:
                    r= new TextureRegion(Assets.calibriText,756,0,20,86);
            }
        }
        else {
            switch(c) {
                case '1':
                    r=new TextureRegion(Assets.calibriText,0,0,34,86);
                    break;
                case '2':
                    r=new TextureRegion(Assets.calibriText,44,0,38,86);
                    break;
                case '3':
                    r=new TextureRegion(Assets.calibriText,92,0,38,86);
                    break;
                case '4':
                    r=new TextureRegion(Assets.calibriText,138,0,44,86);
                    break;
                case '5':
                    r=new TextureRegion(Assets.calibriText,188,0,40,86);
                    break;
                case '6':
                    r=new TextureRegion(Assets.calibriText,238,0,40,86);
                    break;
                case '7':
                    r=new TextureRegion(Assets.calibriText,286,0,40,86);
                    break;
                case '8':
                    r=new TextureRegion(Assets.calibriText,334,0,42,86);
                    break;
                case '9':
                    r=new TextureRegion(Assets.calibriText,382,0,42,86);
                    break;
                case '0':
                    r=new TextureRegion(Assets.calibriText,430,0,44,86);
                    break;
                case '$':
                    r=new TextureRegion(Assets.calibriText,480,0,40,86);
                    break;
                case '%':
                    r=new TextureRegion(Assets.calibriText,528,0,64,86);
                    break;
                case '+':
                    r=new TextureRegion(Assets.calibriText,596,0,44,86);
                    break;
                case '-':
                    r=new TextureRegion(Assets.calibriText,644,0,44,86);
                    break;
                case ':':
                    r=new TextureRegion(Assets.calibriText,696,0,14,86);
                    break;
                case '/':
                    r=new TextureRegion(Assets.calibriText,716,0,36,86);
                    break;
                default:
                    r= new TextureRegion(Assets.calibriText,756,0,20,86);
            }
        }

        return r;
    }

}
