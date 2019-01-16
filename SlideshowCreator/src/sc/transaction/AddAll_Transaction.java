/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sc.transaction;

import djf.transaction.Transaction;
import javafx.collections.ObservableList;
import sc.SlideshowCreatorApp;
import sc.data.Slide;
import sc.data.SlideshowCreatorData;

/**
 *
 * @author Yu Song
 */
public class AddAll_Transaction extends Transaction{
    ObservableList<Slide> slides;
    SlideshowCreatorApp app;
    public AddAll_Transaction(ObservableList<Slide> initSlides, SlideshowCreatorApp initApp){
        slides = initSlides;
        app = initApp;
    }
    @Override
    public void doTransaction(){
        SlideshowCreatorData data = (SlideshowCreatorData)app.getDataComponent();
        for(int i = 0; i < slides.size(); i++){
            data.addSlide(slides.get(i));
        }
    }
    @Override
    public void undoTransaction(){
        SlideshowCreatorData data = (SlideshowCreatorData)app.getDataComponent();
        for(int i = 0; i < slides.size(); i++){
            data.getSlides().remove(slides.get(i));
        }
    }
}
