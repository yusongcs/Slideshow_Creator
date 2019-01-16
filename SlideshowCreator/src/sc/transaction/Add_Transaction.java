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

/**
 *
 * @author Yu Song
 */
public class Add_Transaction extends Transaction{
    Slide addedSlide;
    SlideshowCreatorApp app;
    
    public Add_Transaction(Slide initSlide, SlideshowCreatorApp initApp){
        addedSlide = initSlide;
        app = initApp;
    }
    @Override
    public void doTransaction(){
        SlideshowCreatorData data = (SlideshowCreatorData)app.getDataComponent();
        data.addSlide(addedSlide);
    }
    @Override
    public void undoTransaction(){
        SlideshowCreatorData data = (SlideshowCreatorData)app.getDataComponent();
        data.getSlides().remove(addedSlide);
    }
}
