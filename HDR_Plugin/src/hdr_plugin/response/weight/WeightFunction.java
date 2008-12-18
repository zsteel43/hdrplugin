/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hdr_plugin.response.weight;

import java.io.Serializable;

/**
 *
 * @author Alex
 */
public interface WeightFunction extends Serializable {
    public double w(int value);
}
