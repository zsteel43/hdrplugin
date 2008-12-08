/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hdr_plugin.response;

import java.io.Serializable;

/**
 *
 * @author Alex
 */
public class ResponseFunction implements Serializable {
    private double[][] g;
    private int type;

    public ResponseFunction(double[][] g, int type) {
        this.g = g;
        this.type = type;
    }

    /**
     * @return the g
     */
    public double[][] getG() {
        return g;
    }

    public int getType() {
        return type;
    }

}
