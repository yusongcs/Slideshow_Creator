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
public class MoveUp_Transaction extends Transaction{
    Slide moveUpSlide;
    SlideshowCreatorApp app;
    public MoveUp_Transaction(Slide initSlide, SlideshowCreatorApp initApp){
        moveUpSlide = initSlide;
        app = initApp;
    }
    @Override
    public void doTransaction(){
        SlideshowCreatorData data = (SlideshowCreatorData)app.getDataComponent();
        int prePos = data.getSlides().indexOf(moveUpSlide);
        if(prePos > 0){
            Slide prevElement = data.getSlides().get(prePos - 1);
            data.getSlides().set(prePos - 1, moveUpSlide);
            data.getSlides().set(prePos, prevElement);
        }
    }
    @Override
    public void undoTransaction(){
        SlideshowCreatorData data = (SlideshowCreatorData)app.getDataComponent();
        int prePos = data.getSlides().indexOf(moveUpSlide);
        if(prePos < data.getSlides().size() - 1){
            Slide postElement = data.getSlides().get(prePos + 1);
            data.getSlides().set(prePos + 1, moveUpSlide);
            data.getSlides().set(prePos, postElement);
        }
    }
}
