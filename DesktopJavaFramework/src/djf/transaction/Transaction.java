/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package djf.transaction;

/**
 *
 * @author Yu Song
 */
public abstract class Transaction {
    public abstract void doTransaction();
    public abstract void undoTransaction();
}
