/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hdr_plugin.helper;

import java.io.Serializable;

/**
 *
 * @author Alex
 */
public class Pixel implements Serializable {
    private int x;
    private int y;
    
    public Pixel(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Pixel)) {
            return false;
        }
        Pixel other = (Pixel) object;
        if ((this.x == other.x) && (this.y == other.y)) { 
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.x;
        hash = 97 * hash + this.y;
        return hash;
    }
}
