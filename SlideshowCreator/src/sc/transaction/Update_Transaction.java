/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sc.transaction;

import djf.transaction.Transaction;
import sc.SlideshowCreatorApp;
import sc.data.Slide;
import sc.workspace.SlideshowCreatorController;
import sc.workspace.SlideshowCreatorWorkspace;

/**
 *
 * @author Yu Song
 */
public class Update_Transaction extends Transaction{
    Slide slide;
    Slide oldValue;
    Slide newValue;
    SlideshowCreatorApp app;
    public Update_Transaction(Slide slide, Slide oldValue, Slide newValue, SlideshowCreatorApp initApp){
        this.slide = slide;
        this.oldValue = oldValue;
        this.newValue = newValue;
        app = initApp;
    }
    @Override
    public void doTransaction(){
        SlideshowCreatorController controller = new SlideshowCreatorController(app);
        controller.handleUpdateSlideHelper1(slide, newValue);
    }
    @Override
    public void undoTransaction(){
        SlideshowCreatorController controller = new SlideshowCreatorController(app);
        controller.handleUpdateSlideHelper(slide, oldValue.getCaption(), oldValue.getCurrentWidth(), oldValue.getOriginalHeight(),
                oldValue.getXPosition(), oldValue.getYPosition());
    }
}
