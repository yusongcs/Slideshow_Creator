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
 * @author Yu Song
 */
public class Remove_Transaction extends Transaction{
    Slide removedSlide;
    int position;
    SlideshowCreatorApp app;
    public Remove_Transaction(Slide initSlide, SlideshowCreatorApp initApp){
        removedSlide = initSlide;
        app = initApp;
    }
    @Override
    public void doTransaction(){
        SlideshowCreatorData data = (SlideshowCreatorData)app.getDataComponent();
        position = data.getSlides().indexOf(removedSlide);
        data.removeSlide(removedSlide);
        SlideshowCreatorWorkspace workspace = (SlideshowCreatorWorkspace)app.getWorkspaceComponent();
        workspace.getSlideshowTableView().getSelectionModel().clearSelection();
        workspace.resetDescription();
    }
    @Override
    public void undoTransaction(){
        SlideshowCreatorData data = (SlideshowCreatorData)app.getDataComponent();
        data.addSlide(removedSlide, position);
        SlideshowCreatorWorkspace workspace = (SlideshowCreatorWorkspace)app.getWorkspaceComponent();
        workspace.getSlideshowTableView().getSelectionModel().select(removedSlide);
    }
}
