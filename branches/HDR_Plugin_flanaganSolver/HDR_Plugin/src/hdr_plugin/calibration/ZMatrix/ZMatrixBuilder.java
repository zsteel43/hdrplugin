/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hdr_plugin.calibration.ZMatrix;

import hdr_plugin.Exceptions.TypeNotSupportedException;

/**
 *
 * @author Alexander Heidrich
 */
public interface ZMatrixBuilder {
  int[][][] getZ() throws TypeNotSupportedException;
}
