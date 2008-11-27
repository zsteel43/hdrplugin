/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hdr_plugin.calibration;

/**
 *
 * @author Alex
 */
public interface ResponseFunctionCalculator {

    public void calcResponse(int Z[][]);

    public void saveResponse();

    public ResponseFunctionCalculatorSettings getResponseFunctionCalculatorSettings();
}
