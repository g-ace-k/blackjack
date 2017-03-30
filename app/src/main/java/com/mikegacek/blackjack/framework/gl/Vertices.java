package com.mikegacek.blackjack.framework.gl;

import com.mikegacek.blackjack.framework.impl.GLGraphics;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Michael on 4/30/2015.
 */
public class Vertices implements Serializable{
    final GLGraphics glGraphics;
    final boolean hasColor;
    final boolean hasTextCoords;
    final int vertexSize;
    final IntBuffer vertices;
    final ShortBuffer indices;
    final int[] tmpBuffer;

    public Vertices(GLGraphics glGraphics, int maxVertices, int maxIndices, boolean hasColor, boolean hasTexCoords) {
        this.glGraphics=glGraphics;
        this.hasColor = hasColor;
        this.hasTextCoords=hasTexCoords;
        this.vertexSize = (2+(hasColor?4:0)+(hasTextCoords?2:0))*4;
        this.tmpBuffer = new int[maxVertices*vertexSize/4];
        ByteBuffer buffer = ByteBuffer.allocateDirect(maxVertices * vertexSize);
        buffer.order(ByteOrder.nativeOrder());
        //vertices=buffer.asFloatBuffer();
        vertices = buffer.asIntBuffer();

        if(maxIndices>0) {
            buffer = ByteBuffer.allocateDirect(maxIndices * Short.SIZE / 8);
            buffer.order(ByteOrder.nativeOrder());
            indices = buffer.asShortBuffer();
        } else {
            indices = null;
        }
    }

    public void setVertices(float[] vertices, int offset, int length) {
        this.vertices.clear();
        //this.vertices.put(vertices, offset, length);
        int len = offset + length;
        for(int i = offset, j=0;i<len;i++,j++)
            tmpBuffer[j] = Float.floatToRawIntBits(vertices[i]);
        this.vertices.put(tmpBuffer,0,length);
        this.vertices.flip();
    }

    public void setIndices(short[] indices, int offset, int length) {
        this.indices.clear();
        this.indices.put(indices,offset,length);
        this.indices.flip();
    }

    public void bind() {
        GL10 gl = glGraphics.getGl();

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        vertices.position(0);
        gl.glVertexPointer(2, GL10.GL_FLOAT, vertexSize, vertices);

        if(hasColor) {
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
            vertices.position(2);
            gl.glColorPointer(4, GL10.GL_FLOAT,vertexSize,vertices);
        }

        if(hasTextCoords) {
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            vertices.position(hasColor?6:2);
            gl.glTexCoordPointer(2, GL10.GL_FLOAT,vertexSize,vertices);
        }
    }

    public void draw(int primitiveType,int offset,int numVertices) {
        GL10 gl = glGraphics.getGl();

        if(indices != null) {
            indices.position(offset);
            gl.glDrawElements(primitiveType,numVertices, GL10.GL_UNSIGNED_SHORT,indices);
        } else {
            gl.glDrawArrays(primitiveType,offset,numVertices);
        }
    }

    public void unbind() {
        GL10 gl=glGraphics.getGl();
        if(hasTextCoords)
            gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        if(hasColor)
            gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    }
}
