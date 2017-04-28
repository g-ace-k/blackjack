package com.mikegacek.blackjack.framework.gl;

import com.mikegacek.blackjack.framework.impl.GLGraphics;
import com.mikegacek.blackjack.framework.math.Vector2;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Michael on 2/6/2017.
 */

// NEEDS WORK

public class ParticleGroup  implements Serializable {

    static final int CIRCLE=1;
    static final int LINE=2;

    private float time;
    private transient Texture texture;
    private transient TextureRegion textureRegion;
    private float r,g,b,a;
    float distance;
    Vector2 pos1;
    Vector2 pos2;
    float radius;
    float angle;
    int version;
    boolean on;
    ArrayList<Particle> particles;

    public ParticleGroup(Texture texture, TextureRegion textureRegion,float time) {
        this.texture=texture;
        this.textureRegion=textureRegion;
        this.time=time*60;
        r=1;
        g=1;
        b=1;
        a=1;
        angle=360;
        on=false;
        particles = new ArrayList<Particle>();
    }

    public ParticleGroup(Texture texture, TextureRegion textureRegion, float time, float r, float g, float b, float a) {
        this.texture=texture;
        this.textureRegion=textureRegion;
        this.time=time*60;
        this.r=r;
        this.g=g;
        this.b=b;
        this.a=a;
        on=false;
        angle=360;
        particles = new ArrayList<Particle>();
    }

    public void loadTextures(Texture t, TextureRegion tr) {
        texture=t;
        textureRegion=tr;
        for(Particle p: particles) {
            p.setTextureRegion(tr);
        }
    }

    public void setRGBA(float r, float g, float b, float a) {
        this.r=r;
        this.g=g;
        this.b=b;
        this.a=a;
    }

    public void setTime(float t) {
        time=t*60;
    }

    public void setCircle(Vector2 vector, float rad,float d) {
        version=CIRCLE;
        pos1=vector;
        radius=rad;
        distance=d;
        on=true;
        for(int i=particles.size()-1;i>=0;i--)
            particles.remove(i);
        for(float i=time;i>0;i--) {
            for(int j=0;j<3;j++) {
                float x = pos1.x + (float) Math.cos(angle) * radius;
                float y = pos1.y + (float) Math.sin(angle) * radius;
                particles.add(new Particle(textureRegion, x, y, textureRegion.width, textureRegion.height, r, g, b, i/time, angle));
                angle += .05f;
                if (angle >=360)
                    angle = 0;
            }
        }
        //Initial particles
    }

    public void setLine(Vector2 vector1, Vector2 vector2,float d) {
        version=LINE;
        pos1=vector1;
        pos2=vector2;
        distance=d;
        on=true;
    }

    public void setOn(boolean b) {
        on=b;
    }

    public void drawParticles(GLGraphics glGraphics, SpriteBatcher spriteBatcher) {
        switch(version) {
            case CIRCLE:
                drawInCircle(glGraphics,spriteBatcher);
                break;
            case LINE:
                drawInLine(glGraphics,spriteBatcher);
                break;
        }
    }

    private void drawInCircle(GLGraphics glGraphics, SpriteBatcher spriteBatcher) {

        if(on==true) {
            for(int i=0;i<3;i++) {
                float x = pos1.x + (float) Math.cos(angle) * radius;
                float y = pos1.y + (float) Math.sin(angle) * radius;
                particles.add(new Particle(textureRegion, x, y, textureRegion.width, textureRegion.height, r, g, b, a, angle));
                angle -= .05f;
                if (angle <=0)
                    angle = 360;
            }
        }

        for(int i=particles.size()-1;i>=0;i--) {
            Particle particle=particles.get(i);
            glGraphics.getGl().glColor4f(particle.getR(),particle.getG(),particle.getB(),particle.getA());
            spriteBatcher.beginBatch(texture);
            spriteBatcher.drawSprite(particle.getxPos(),particle.getyPos(),particle.getWidth()*1f,particle.getHeight()*1f,particle.getAngle(),particle.getTextureRegion());
            spriteBatcher.endBatch();
            particle.setA(particle.getA()-1/time);
            if(particle.getA()<=0)
                particles.remove(i);
            
        }


        glGraphics.getGl().glColor4f(1,1,1,1);

    }

    private void drawInLine(GLGraphics glGraphics, SpriteBatcher spriteBatcher) {

    }


}
