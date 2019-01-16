package sc.workspace;

import djf.ui.AppMessageDialogSingleton;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;
import properties_manager.PropertiesManager;
import sc.SlideshowCreatorApp;
import sc.data.Slide;
import static sc.SlideshowCreatorProp.APP_PATH_WORK;
import static sc.SlideshowCreatorProp.INVALID_IMAGE_PATH_MESSAGE;
import static sc.SlideshowCreatorProp.INVALID_IMAGE_PATH_TITLE;
import sc.data.SlideshowCreatorData;
import sc.transaction.Add_Transaction;
import djf.transaction.Transaction_Stack;
import javafx.collections.FXCollections;
import sc.transaction.AddAll_Transaction;
import sc.transaction.MoveDown_Transaction;
import sc.transaction.MoveUp_Transaction;
import sc.transaction.Remove_Transaction;
import sc.transaction.Title_Transaction;
import sc.transaction.Update_Transaction;

/**
 * This class provides responses to all workspace interactions, meaning
 * interactions with the application controls not including the file
 * toolbar.
 * 
 * @author Richard McKenna
 *          Yu Song
 * @version 1.0
 */
public class SlideshowCreatorController {
    // THE APP PROVIDES ACCESS TO OTHER COMPONENTS AS NEEDED
    SlideshowCreatorApp app;
    Stage viewStage;
    int currentIndex;
    Timeline animation;
    Transaction_Stack transactions;
    
    /**
     * Constructor, note that the app must already be constructed.
     */
    public SlideshowCreatorController(SlideshowCreatorApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;
        transactions = new Transaction_Stack();
    }
    
    // CONTROLLER METHOD THAT HANDLES ADDING A DIRECTORY OF IMAGES
    public void handleAddAllImagesInDirectory() {
        try {
            // ASK THE USER TO SELECT A DIRECTORY
            DirectoryChooser dirChooser = new DirectoryChooser();
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            dirChooser.setInitialDirectory(new File(props.getProperty(APP_PATH_WORK)));
            File dir = dirChooser.showDialog(app.getGUI().getWindow());
            if (dir != null) {
                File[] files = dir.listFiles();
                SlideshowCreatorData data = (SlideshowCreatorData)app.getDataComponent();
                ObservableList<Slide> slides = FXCollections.observableArrayList();
                boolean flag = true;
                for (File f : files) {
                    String fileName = f.getName();
                    if ((fileName.toLowerCase().endsWith(".png") ||
                            fileName.toLowerCase().endsWith(".jpg") ||
                            fileName.toLowerCase().endsWith(".gif"))  && flag) {
                        String path = f.getPath();
                        String caption = "";
                        Image slideShowImage = loadImage(path);
                        int originalWidth = (int)slideShowImage.getWidth();
                        int originalHeight = (int)slideShowImage.getHeight();
                        Slide slide = new Slide(fileName, path, caption, originalWidth, originalHeight);
                        slides.add(slide);
                        if(data.hasSlide(fileName, path))
                            flag = false;
                    }
                }
                if(flag){
                    app.getGUI().getFileToolbar().getChildren().get(5).setDisable(false);
                    AddAll_Transaction transaction = new AddAll_Transaction(slides, app);
                    transactions.addTransaction(transaction);
                }
            }
        }
        catch(MalformedURLException murle) {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String title = props.getProperty(INVALID_IMAGE_PATH_TITLE);
            String message = props.getProperty(INVALID_IMAGE_PATH_MESSAGE);
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(title, message);
        }
    }
    public void handleAddSingleImageInDirectory() {
        try {
            // ASK THE USER TO SELECT A SINGLE FILE
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            SlideshowCreatorData data = (SlideshowCreatorData)app.getDataComponent();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(props.getProperty(APP_PATH_WORK)));
            fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
            );
            File f = fileChooser.showOpenDialog(app.getGUI().getWindow());
            boolean flag = true;
            if(f != null){
                String fileName = f.getName();
                if (fileName.toLowerCase().endsWith(".png") ||
                            fileName.toLowerCase().endsWith(".jpg") ||
                            fileName.toLowerCase().endsWith(".gif")) {
                        String path = f.getPath();
                        String caption = "";
                        Image slideShowImage = loadImage(path);
                        int originalWidth = (int)slideShowImage.getWidth();
                        int originalHeight = (int)slideShowImage.getHeight();
                        if(!data.hasSlide(fileName, path))
                            data.addNonDuplicateSlide(fileName, path, caption, originalWidth, originalHeight);
                        else
                            flag = true;
                }
                if(flag){
                    app.getGUI().getFileToolbar().getChildren().get(5).setDisable(false);
                    Add_Transaction addTransaction = new Add_Transaction(data.getSlides().get(data.getSlides().size() - 1), app);
                    transactions.addTransaction(addTransaction);
                }
            }
        }
        catch(MalformedURLException murle) {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String title = props.getProperty(INVALID_IMAGE_PATH_TITLE);
            String message = props.getProperty(INVALID_IMAGE_PATH_MESSAGE);
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(title, message);
        }
    }
    //(SY)  CONTROLLER METHOD THAT HANDLES REMOVING A SINGLE IMAGE
    public void handleRemoveSlide(Slide removedSlide){
        SlideshowCreatorData data = (SlideshowCreatorData) app.getDataComponent();
        SlideshowCreatorWorkspace workspace = (SlideshowCreatorWorkspace)app.getWorkspaceComponent();
        Remove_Transaction removeTransaction = new Remove_Transaction(removedSlide, app);
        transactions.addTransaction(removeTransaction);
        workspace.resetDescription();
        app.getGUI().getFileController().markAsEdited(app.getGUI());   
    }
    
    public void handleSelectSlide(Slide newItem){
        SlideshowCreatorWorkspace workspace = (SlideshowCreatorWorkspace)app.getWorkspaceComponent();
        workspace.fileNameTextField.setText(newItem.getFileName());
        workspace.pathTextField.setText(newItem.getPath());
        workspace.originalHeightTextField.setText(String.valueOf(newItem.getOriginalHeight()));
        workspace.originalWidthTextField.setText(String.valueOf(newItem.getOriginalWidth()));
        workspace.captionTextField.setText(newItem.getCaption());
        workspace.currentWidthSlider.setValue(newItem.getCurrentWidth());
        workspace.currentHeightSlider.setValue(newItem.getCurrentHeight());
        workspace.xPositionSlider.setValue(newItem.getXPosition());
        workspace.yPositionSlider.setValue(newItem.getYPosition());
        try{
            Image image = loadImage(newItem.getPath());
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
            imageView.setX(0);
            imageView.setY(0);
            imageView.setPreserveRatio(true);
            workspace.imageBorderPane.setRight(imageView);
            BorderPane.setAlignment(imageView, Pos.TOP_RIGHT);
        }
        catch(MalformedURLException e){}
    }
    
    public void handleUpdateSlide(){
        SlideshowCreatorWorkspace workspace = (SlideshowCreatorWorkspace)app.getWorkspaceComponent();
        Slide slide = workspace.slidesTableView.getSelectionModel().getSelectedItem();
        Slide oldValue = new Slide(slide.getFileName(), slide.getPath(), slide.getCaption(), slide.getOriginalWidth(), slide.getOriginalHeight());
        oldValue.setCurrentHeight(slide.getCurrentHeight());
        oldValue.setCurrentWidth(slide.getCurrentWidth());
        oldValue.setXPosition(slide.getXPosition());
        oldValue.setYPosition(slide.getYPosition());
        
        slide.setCaption(workspace.captionTextField.getText());
        slide.setCurrentHeight((int)workspace.currentHeightSlider.getValue());
        slide.setCurrentWidth((int)workspace.currentWidthSlider.getValue());
        slide.setXPosition((int)workspace.xPositionSlider.getValue());
        slide.setYPosition((int)workspace.yPositionSlider.getValue());
        
        Slide newValue = new Slide(slide.getFileName(), slide.getPath(), slide.getCaption(), slide.getOriginalWidth(), slide.getOriginalHeight());
        newValue.setCurrentHeight(slide.getCurrentHeight());
        newValue.setCurrentWidth(slide.getCurrentWidth());
        newValue.setXPosition(slide.getXPosition());
        newValue.setYPosition(slide.getYPosition());
        Update_Transaction transaction = new Update_Transaction(slide, oldValue, newValue, app);
        transactions.addTransaction(transaction);
        
        workspace.slidesTableView.refresh();
        app.getGUI().getFileController().markAsEdited(app.getGUI());
        workspace.updateButton.setDisable(true);
    }
    public void handleUpdateSlideHelper1(Slide slide, Slide newValue){
        SlideshowCreatorWorkspace workspace = (SlideshowCreatorWorkspace)app.getWorkspaceComponent();
        slide.setCaption(newValue.getCaption());
        slide.setCurrentHeight(newValue.getCurrentHeight());
        slide.setCurrentWidth(newValue.getCurrentWidth());
        slide.setXPosition(newValue.getXPosition());
        slide.setYPosition(newValue.getYPosition());
        if(slide == workspace.slidesTableView.getSelectionModel().getSelectedItem()){
            workspace.captionTextField.setText(slide.getCaption());
            workspace.currentWidthSlider.setValue(slide.getCurrentWidth());
            workspace.currentHeightSlider.setValue(slide.getCurrentHeight());
            workspace.xPositionSlider.setValue(slide.getXPosition());
            workspace.yPositionSlider.setValue(slide.getYPosition());
        }
    }
    public void handleUpdateSlideHelper(Slide slide, String caption, int currentWidth, int currentHeight, int xPosition, int yPosition){
        SlideshowCreatorWorkspace workspace = (SlideshowCreatorWorkspace)app.getWorkspaceComponent();
        slide.setCaption(caption);
        slide.setCurrentHeight(currentHeight);
        slide.setCurrentWidth(currentWidth);
        slide.setXPosition(xPosition);
        slide.setYPosition(yPosition);
        if(slide == workspace.slidesTableView.getSelectionModel().getSelectedItem()){
            workspace.captionTextField.setText(caption);
            workspace.currentWidthSlider.setValue(currentWidth);
            workspace.currentHeightSlider.setValue(currentHeight);
            workspace.xPositionSlider.setValue(xPosition);
            workspace.yPositionSlider.setValue(yPosition);
        }
    }
    // THIS METHOD HANLE MOVE THE SELECTED SLIDE ONE POSITION UP EACH TIME
    public void handleMoveUpSlide(){
        SlideshowCreatorWorkspace workspace = (SlideshowCreatorWorkspace)app.getWorkspaceComponent();
        SlideshowCreatorData data = (SlideshowCreatorData) app.getDataComponent();
        int prePos = data.getSlides().indexOf(workspace.slidesTableView.getSelectionModel().getSelectedItem());
        MoveUp_Transaction transaction = new MoveUp_Transaction(data.getSlides().get(prePos), app);
        transactions.addTransaction(transaction);
    }
    // THIS METHOD HANDLE MOVE THE SELECTED SLIDE ONE POSITION DOWN EACH TIME
    public void handleMoveDownSlide(){
        SlideshowCreatorWorkspace workspace = (SlideshowCreatorWorkspace)app.getWorkspaceComponent();
        SlideshowCreatorData data = (SlideshowCreatorData) app.getDataComponent();
        int prePos = data.getSlides().indexOf(workspace.slidesTableView.getSelectionModel().getSelectedItem());
        MoveDown_Transaction transaction = new MoveDown_Transaction(data.getSlides().get(prePos), app);
        transactions.addTransaction(transaction);
    }
    public void handleSlideshowTitle(String oldValue, String newValue){
        Title_Transaction tansaction = new Title_Transaction(oldValue, newValue, app);
        transactions.addTransaction(tansaction);
    }
    public void handleSlideshowTitleHelper1(String newValue){
        SlideshowCreatorWorkspace workspace = (SlideshowCreatorWorkspace)app.getWorkspaceComponent();
        workspace.slideShowTitleTextField.setText(newValue);
    }
    public void handleSlideshowTitleHelper2(String oldValue){
        SlideshowCreatorWorkspace workspace = (SlideshowCreatorWorkspace)app.getWorkspaceComponent();
        workspace.slideShowTitleTextField.setText(oldValue);
    }
    // THIS METHOD HANDLE UNDO SLIDE OPERATION
    public void handleUndoSlide(){
        transactions.undoTransaction();
    }
    public void handleRedoSlide(){
        transactions.doTransaction();
    }
    
    public void handleViewSlideshow() {
        SlideshowCreatorWorkspace workspace = (SlideshowCreatorWorkspace)app.getWorkspaceComponent();
        SlideshowCreatorData data = (SlideshowCreatorData) app.getDataComponent();
        viewStage = app.getGUI().getSlideshowWindow();
        currentIndex = 0;
        viewStage.setTitle(workspace.getSlideshowTextField().getText());   
        displayImageHelper(data.getSlides().get(currentIndex));
        workspace.viewCaptionLabel.setText(data.getSlides().get(0).getCaption());
        viewStage.setScene(workspace.scene);
        viewStage.show();
    }
    public void handlePrevButton(){
        SlideshowCreatorData data = (SlideshowCreatorData) app.getDataComponent();
        SlideshowCreatorWorkspace workspace = (SlideshowCreatorWorkspace)app.getWorkspaceComponent();
        if(currentIndex == 0)
            currentIndex = data.getSlides().size() - 1;
        else
            currentIndex -=1;
        displayImageHelper(data.getSlides().get(currentIndex));
        workspace.viewCaptionLabel.setText(data.getSlides().get(currentIndex).getCaption());
    }
    public void handleNextButton(){
        SlideshowCreatorData data = (SlideshowCreatorData) app.getDataComponent();
        SlideshowCreatorWorkspace workspace = (SlideshowCreatorWorkspace)app.getWorkspaceComponent();
        currentIndex = (currentIndex + 1) % data.getSlides().size();
        displayImageHelper(data.getSlides().get(currentIndex));
        workspace.viewCaptionLabel.setText(data.getSlides().get(currentIndex).getCaption());
    }
    public void play1(){
        SlideshowCreatorWorkspace workspace = (SlideshowCreatorWorkspace)app.getWorkspaceComponent();
        EventHandler<ActionEvent> eventHandler = e ->{     
            handleNextButton();
        };
        animation = new Timeline(new KeyFrame(Duration.seconds(2), eventHandler));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
        workspace.playButton.setDisable(true);
        workspace.pauseButton.setDisable(false);
        workspace.pauseButton.setOnAction(e -> {
            animation.pause();
            workspace.pauseButton.setDisable(true);
            workspace.playButton.setDisable(false);
        });
    }
    
    // THIS HELPER METHOD LOADS AN IMAGE SO WE CAN SEE IT'S SIZE
    private Image loadImage(String imagePath) throws MalformedURLException{
            File file = new File(imagePath);
            URL fileURL = file.toURI().toURL();
            Image image = new Image(fileURL.toExternalForm());
            return image;
    }
    
    public void displayImageHelper(Slide newItem){
        try{
            SlideshowCreatorData data = (SlideshowCreatorData) app.getDataComponent();
            SlideshowCreatorWorkspace workspace = (SlideshowCreatorWorkspace)app.getWorkspaceComponent();
            workspace.imageView.setImage(loadImage(data.getSlides().get(currentIndex).getPath()));
            workspace.imageView.setFitHeight(data.getSlides().get(currentIndex).getCurrentHeight());
            workspace.imageView.setFitWidth(data.getSlides().get(currentIndex).getCurrentWidth());
            workspace.imageView.setX(data.getSlides().get(currentIndex).getXPosition());
            workspace.imageView.setY(data.getSlides().get(currentIndex).getYPosition());
        }
        catch(MalformedURLException e){
            System.out.println("Image path error");
        }
    }
    public Timeline getAnimation(){
        return animation;
    }
    public Transaction_Stack getTransaction_Stack(){
        return transactions;
    }
}