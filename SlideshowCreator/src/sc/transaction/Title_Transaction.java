/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sc.transaction;

import djf.transaction.Transaction;
import sc.SlideshowCreatorApp;
import sc.workspace.SlideshowCreatorController;

/**
 *
 * @author Yu Song
 */
public class Title_Transaction extends Transaction{
    String oldTitle;
    String newTitle;
    SlideshowCreatorApp app;
    public Title_Transaction(String oldValue, String newValue, SlideshowCreatorApp initApp){
        oldTitle = oldValue;
        newTitle = newValue;
        app = initApp;
    }
    @Override
    public void doTransaction(){
        SlideshowCreatorController controller = new SlideshowCreatorController(app);
        controller.handleSlideshowTitleHelper1(newTitle);
    }
    @Override
    public void undoTransaction(){
        SlideshowCreatorController controller = new SlideshowCreatorController(app);
        controller.handleSlideshowTitleHelper1(oldTitle);
    }
}