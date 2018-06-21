/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.cat_exporter;

/**
 * Other classes use the IConfigListener by implementing this method
 */
public interface IConfigListener {

    /**
     * The existence status of the node has changed.
     *
     * @param data
     */
    void exists(byte data[]);

    /**
     * The ZooKeeper session is no longer valid.
     *
     * @param rc the ZooKeeper reason code
     */
    void closing(int rc);
}
