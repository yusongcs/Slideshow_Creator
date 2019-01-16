/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package djf.transaction;

import java.util.ArrayList;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
/**
 *
 * @author Yu Song
 */
public class Transaction_Stack {
    private ArrayList<Transaction> transactions = new ArrayList();
    private IntegerProperty mostRecentTransaction = new SimpleIntegerProperty(-1);
    
    public Transaction_Stack() {}
    
    public void addTransaction(Transaction transaction) {
        // IS THIS THE FIRST TRANSACTION?
        if (mostRecentTransaction.getValue() < 0) {
            // DO WE HAVE TO CHOP THE LIST?
            if (transactions.size() > 0) {
                transactions = new ArrayList();
            }
            transactions.add(transaction);
        }
        // ARE WE ERASING ALL THE REDO TRANSACTIONS?
        else if (mostRecentTransaction.getValue() < (transactions.size()-1)) {
            transactions.set(mostRecentTransaction.getValue() + 1, transaction);
            transactions = new ArrayList(transactions.subList(0, mostRecentTransaction.getValue() + 2));
        }
        // IS IT JUST A TRANSACTION TO APPEND TO THE END?
        else {
            transactions.add(transaction);
        }
        doTransaction();
    }
    public void doTransaction() {
        if (mostRecentTransaction.getValue() < (transactions.size()-1)) {
            Transaction transaction = transactions.get(mostRecentTransaction.getValue() + 1);
            transaction.doTransaction();
            mostRecentTransaction.setValue(mostRecentTransaction.getValue() + 1);
        }
    }   
    public void undoTransaction() {
        if (mostRecentTransaction.getValue() >= 0) {
            Transaction transaction = transactions.get(mostRecentTransaction.getValue());
            transaction.undoTransaction();
            mostRecentTransaction.setValue(mostRecentTransaction.getValue() - 1);
        }
    }
    public IntegerProperty getMostRecentTransaction(){
        return mostRecentTransaction;
    }
    public int getTransactionSize(){
        return transactions.size();
    }
}
