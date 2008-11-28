/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hdr_plugin.response;

/**
 *
 * @author Alex
 */
public interface ResponseFunctionCalculator {

    public void calcResponse(int channel);

    public void saveResponse();

    public ResponseFunctionCalculatorSettings getResponseFunctionCalculatorSettings();

    public String getAlgorithm();

    public String getAlgorithmReference();
}
