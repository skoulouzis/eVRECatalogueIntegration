/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.service;

import java.util.concurrent.Callable;


public class RetDouble implements Callable<String> {

    @Override
    public String call() throws Exception {
        Thread.sleep(5000);
        return "Done";
    }

}
