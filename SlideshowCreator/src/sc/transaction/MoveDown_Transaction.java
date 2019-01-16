/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sc.transaction;

import djf.transaction.Transaction;
import sc.SlideshowCreatorApp;
import sc.data.Slide;
import sc.data.SlideshowCreatorData;
import sc.workspace.SlideshowCreatorWorkspace;
/**
 *
 * @author songyu
 */
public class MoveDown_Transaction extends Transaction{
    Slide moveDownSlide;
    SlideshowCreatorApp app;
    public MoveDown_Transaction(Slide initSlide, SlideshowCreatorApp initApp){
        moveDownSlide = initSlide;
        app = initApp;
    }
    @Override
    public void doTransaction(){
        SlideshowCreatorData data = (SlideshowCreatorData) app.getDataComponent();
        int prePos = data.getSlides().indexOf(moveDownSlide);
        if(prePos < data.getSlides().size() - 1){
            Slide postElement = data.getSlides().get(prePos + 1);
            data.getSlides().set(prePos + 1, moveDownSlide);
            data.getSlides().set(prePos, postElement);
            app.getGUI().getFileController().markAsEdited(app.getGUI());
        }
    }
    @Override
    public void undoTransaction(){
        SlideshowCreatorData data = (SlideshowCreatorData)app.getDataComponent();
        int prePos = data.getSlides().indexOf(moveDownSlide);
        if(prePos > 0){
            Slide prevElement = data.getSlides().get(prePos - 1);
            data.getSlides().set(prePos - 1, moveDownSlide);
            data.getSlides().set(prePos, prevElement);
        }
    }
}
