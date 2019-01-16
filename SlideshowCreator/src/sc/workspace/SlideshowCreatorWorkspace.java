package sc.workspace;

import djf.components.AppDataComponent;
import djf.components.AppWorkspaceComponent;
import static djf.settings.AppStartupConstants.FILE_PROTOCOL;
import static djf.settings.AppStartupConstants.PATH_IMAGES;
import djf.transaction.Transaction;
import static djf.ui.AppGUI.CLASS_BORDERED_PANE;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import sc.SlideshowCreatorApp;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import properties_manager.PropertiesManager;
import static sc.SlideshowCreatorProp.ADD_ALL_IMAGES_BUTTON_TEXT;
import static sc.SlideshowCreatorProp.ADD_IMAGE_BUTTON_TEXT;
import static sc.SlideshowCreatorProp.CAPTION_PROMPT_TEXT;
import static sc.SlideshowCreatorProp.CURRENT_HEIGHT_COLUMN_TEXT;
import static sc.SlideshowCreatorProp.CURRENT_HEIGHT_PROMPT_TEXT;
import static sc.SlideshowCreatorProp.CURRENT_WIDTH_COLUMN_TEXT;
import static sc.SlideshowCreatorProp.CURRENT_WIDTH_PROMPT_TEXT;
import static sc.SlideshowCreatorProp.FILE_NAME_COLUMN_TEXT;
import static sc.SlideshowCreatorProp.FILE_NAME_PROMPT_TEXT;
import static sc.SlideshowCreatorProp.ORIGINAL_HEIGHT_PROMPT_TEXT;
import static sc.SlideshowCreatorProp.ORIGINAL_WIDTH_PROMPT_TEXT;
import static sc.SlideshowCreatorProp.PATH_PROMPT_TEXT;
import static sc.SlideshowCreatorProp.REMOVE_IMAGE_BUTTON_TEXT;
import static sc.SlideshowCreatorProp.UPDATE_BUTTON_TEXT;
import static sc.SlideshowCreatorProp.MOVE_SLIDE_UP_BUTTON_TEXT;
import static sc.SlideshowCreatorProp.MOVE_SLIDE_DOWN_BUTTON_TEXT;
import static sc.SlideshowCreatorProp.NEXT_BUTTON_TEXT;
import static sc.SlideshowCreatorProp.PAUSE_BUTTON_TEXT;
import static sc.SlideshowCreatorProp.PLAY_BUTTON_TEXT;
import static sc.SlideshowCreatorProp.PREVIOUS_BUTTON_TEXT;
import static sc.SlideshowCreatorProp.X_POSITION_PROMPT_TEXT;
import static sc.SlideshowCreatorProp.Y_POSITION_PROMPT_TEXT;
import static sc.SlideshowCreatorProp.X_POSITION_COLUMN_TEXT;
import static sc.SlideshowCreatorProp.Y_POSITION_COLUMN_TEXT;
import static sc.SlideshowCreatorProp.UNDO_BUTTON_ICON;
import static sc.SlideshowCreatorProp.REDO_BUTTON_ICON;
import sc.data.Slide;
import sc.data.SlideshowCreatorData;
import static sc.style.SlideshowCreatorStyle.CLASS_EDIT_BUTTON;
import static sc.style.SlideshowCreatorStyle.CLASS_EDIT_SLIDER;
import static sc.style.SlideshowCreatorStyle.CLASS_EDIT_TEXT_FIELD;
import static sc.style.SlideshowCreatorStyle.CLASS_PROMPT_LABEL;
import static sc.style.SlideshowCreatorStyle.CLASS_SLIDES_TABLE;
import static sc.style.SlideshowCreatorStyle.CLASS_UPDATE_BUTTON;
import static sc.style.SlideshowCreatorStyle.CLASS_EDIT_TITLE_TEXT_FIELD;
import sc.transaction.Title_Transaction;

/**
 * This class serves as the workspace component for the TA Manager
 * application. It provides all the user interface controls in 
 * the workspace area.
 * 
 * @author Richard McKenna
 *          Yu Song
 */
public class SlideshowCreatorWorkspace extends AppWorkspaceComponent {
    // THIS PROVIDES US WITH ACCESS TO THE APP COMPONENTS
    SlideshowCreatorApp app;

    // THIS PROVIDES RESPONSES TO INTERACTIONS WITH THIS WORKSPACE
    SlideshowCreatorController controller;

    // NOTE THAT EVERY CONTROL IS PUT IN A BOX TO HELP WITH ALIGNMENT
    HBox editImagesToolbar;
    Button addAllImagesInDirectoryButton;
    Button addImageButton;
    Button removeImageButton;
    Button moveSlideUpButton;
    Button moveSlideDownButton;
    Button undoButton;
    Button redoButton;
    
    // FOR THE SLIDES TABLE
    ScrollPane slidesTableScrollPane;
    ScrollPane editTabelScrollPane;
    TableView<Slide> slidesTableView;
    TableColumn<Slide, StringProperty> fileNameColumn;
    TableColumn<Slide, IntegerProperty> currentWidthColumn;
    TableColumn<Slide, IntegerProperty> currentHeightColumn;
    TableColumn<Slide, IntegerProperty> xPositionColumn;
    TableColumn<Slide, IntegerProperty> yPositionColumn;

    // THE EDIT PANE
    GridPane editPane;
    Label fileNamePromptLabel;
    TextField fileNameTextField;
    Label pathPromptLabel;
    TextField pathTextField;
    Label captionPromptLabel;
    TextField captionTextField;
    Label originalWidthPromptLabel;
    TextField originalWidthTextField;
    Label originalHeightPromptLabel;
    TextField originalHeightTextField;
    Label currentWidthPromptLabel;
    Slider currentWidthSlider;
    Label currentHeightPromptLabel;
    Slider currentHeightSlider;
    Button updateButton;
    // NEW ADDS FOR EDITPANE IN HOMEWORK2
    TextField slideShowTitleTextField;
    String initSlideshowTitle = "";
    Label xPositionPromptLabel;
    Slider xPositionSlider;
    Label yPositionPromptLabel;
    Slider yPositionSlider;
    BorderPane imageBorderPane;
    // VIEW IMAGE STAGE
    Scene scene;
    BorderPane viewSlideshowBorderPane;
    FlowPane viewToolBar;
    Label viewCaptionLabel;
    Pane   imageViewPane;
    Button prevButton;
    Button nextButton;
    Button playButton;
    Button pauseButton;
    ImageView imageView;
    /**
     * The constructor initializes the user interface for the
     * workspace area of the application.
     */
    public SlideshowCreatorWorkspace(SlideshowCreatorApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;

        // WE'LL NEED THIS TO GET LANGUAGE PROPERTIES FOR OUR UI
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        // LAYOUT THE APP
        initLayout();
        
        // HOOK UP THE CONTROLLERS
        initControllers();
        
        // AND INIT THE STYLE FOR THE WORKSPACE
        initStyle();
    }
    
    private void initLayout() {
        // WE'LL USE THIS TO GET UI TEXT
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        // FIRST MAKE ALL THE COMPONENTS
        editImagesToolbar = new HBox();
        addAllImagesInDirectoryButton = new Button(props.getProperty(ADD_ALL_IMAGES_BUTTON_TEXT));
        addImageButton = new Button(props.getProperty(ADD_IMAGE_BUTTON_TEXT));
        removeImageButton = new Button(props.getProperty(REMOVE_IMAGE_BUTTON_TEXT));
        slidesTableScrollPane = new ScrollPane();
        editTabelScrollPane = new ScrollPane();
        slidesTableView = new TableView();
        
        editPane = new GridPane();
        fileNamePromptLabel = new Label(props.getProperty(FILE_NAME_PROMPT_TEXT));
        fileNameTextField = new TextField();
        pathPromptLabel = new Label(props.getProperty(PATH_PROMPT_TEXT));
        pathTextField = new TextField();
        captionPromptLabel = new Label(props.getProperty(CAPTION_PROMPT_TEXT));
        captionTextField = new TextField();
        originalWidthPromptLabel = new Label(props.getProperty(ORIGINAL_WIDTH_PROMPT_TEXT));
        originalWidthTextField = new TextField();
        originalHeightPromptLabel = new Label(props.getProperty(ORIGINAL_HEIGHT_PROMPT_TEXT));
        originalHeightTextField = new TextField();
        currentWidthPromptLabel = new Label(props.getProperty(CURRENT_WIDTH_PROMPT_TEXT));
        currentWidthSlider = new Slider(0, 1000, 0);
        currentHeightPromptLabel = new Label(props.getProperty(CURRENT_HEIGHT_PROMPT_TEXT));
        currentHeightSlider = new Slider(0, 1000, 0);
        updateButton = new Button(props.getProperty(UPDATE_BUTTON_TEXT));
       
        moveSlideUpButton = new Button(props.getProperty(MOVE_SLIDE_UP_BUTTON_TEXT));
        moveSlideDownButton = new Button(props.getProperty(MOVE_SLIDE_DOWN_BUTTON_TEXT));
        undoButton = new Button();
        redoButton = new Button();
        //ADD IMAGES FOR UNDO_BUTTON AND REDO_BUTTON
        String undoImagePath = FILE_PROTOCOL + PATH_IMAGES + props.getProperty(UNDO_BUTTON_ICON);
        Image undoButtonImage = new Image(undoImagePath);
        undoButton.setGraphic(new ImageView(undoButtonImage));
        String redoImagePath = FILE_PROTOCOL + PATH_IMAGES + props.getProperty(REDO_BUTTON_ICON);
        Image redoButtonImage = new Image(redoImagePath);
        redoButton.setGraphic(new ImageView(redoButtonImage));
        
        xPositionPromptLabel = new Label(props.getProperty(X_POSITION_PROMPT_TEXT));
        yPositionPromptLabel = new Label(props.getProperty(Y_POSITION_PROMPT_TEXT));
        xPositionSlider = new Slider(0, 1000, 0);
        yPositionSlider = new Slider(0, 1000, 0);
        slideShowTitleTextField = new TextField();
        slideShowTitleTextField.setPromptText("Slideshow Title");
        imageBorderPane = new BorderPane();
        imageBorderPane.setMaxSize(100, 100);
        
        //ARRANGE THE VIEW STAGE
        prevButton = new Button(props.getProperty(PREVIOUS_BUTTON_TEXT));
        nextButton = new Button(props.getProperty(NEXT_BUTTON_TEXT));
        playButton = new Button(props.getProperty(PLAY_BUTTON_TEXT));
        pauseButton = new Button(props.getProperty(PAUSE_BUTTON_TEXT));
        imageView = new ImageView();
        imageViewPane = new Pane();
        viewSlideshowBorderPane = new BorderPane();
        viewToolBar = new FlowPane();
        viewToolBar.getChildren().addAll(prevButton, playButton, pauseButton, nextButton);
        viewCaptionLabel = new Label();
        scene = new Scene(viewSlideshowBorderPane);
        imageViewPane.getChildren().add(imageView);
        viewSlideshowBorderPane.setTop(viewToolBar);
        viewSlideshowBorderPane.setCenter(imageViewPane);
        viewSlideshowBorderPane.setBottom(viewCaptionLabel);
        
        // ARRANGE THE TABLE
        fileNameColumn = new TableColumn(props.getProperty(FILE_NAME_COLUMN_TEXT));
        currentWidthColumn = new TableColumn(props.getProperty(CURRENT_WIDTH_COLUMN_TEXT));
        currentHeightColumn = new TableColumn(props.getProperty(CURRENT_HEIGHT_COLUMN_TEXT));
        xPositionColumn = new TableColumn(props.getProperty(X_POSITION_COLUMN_TEXT));
        yPositionColumn = new TableColumn(props.getProperty(Y_POSITION_COLUMN_TEXT));
        
        slidesTableView.getColumns().add(fileNameColumn);
        slidesTableView.getColumns().add(currentWidthColumn);
        slidesTableView.getColumns().add(currentHeightColumn);
        slidesTableView.getColumns().add(xPositionColumn);
        slidesTableView.getColumns().add(yPositionColumn);
        
        fileNameColumn.prefWidthProperty().bind(slidesTableView.widthProperty().divide(3));
        currentWidthColumn.prefWidthProperty().bind(slidesTableView.widthProperty().divide(5));
        currentHeightColumn.prefWidthProperty().bind(slidesTableView.widthProperty().divide(5));
        xPositionColumn.prefWidthProperty().bind(slidesTableView.widthProperty().divide(8));
        yPositionColumn.prefWidthProperty().bind(slidesTableView.widthProperty().divide(8));
        
        fileNameColumn.setCellValueFactory(
                new PropertyValueFactory<Slide, StringProperty>("fileName")
        );
        currentWidthColumn.setCellValueFactory(
                new PropertyValueFactory<Slide, IntegerProperty>("currentWidth")
        );
        currentHeightColumn.setCellValueFactory(
                new PropertyValueFactory<Slide, IntegerProperty>("CurrentHeight")
        );
        xPositionColumn.setCellValueFactory(
                new PropertyValueFactory<Slide, IntegerProperty>("xPosition")
        );
        yPositionColumn.setCellValueFactory(
                new PropertyValueFactory<Slide, IntegerProperty>("yPosition")
        );
        // HOOK UP THE TABLE TO THE DATA
        SlideshowCreatorData data = (SlideshowCreatorData)app.getDataComponent();
        ObservableList<Slide> model = data.getSlides();
        slidesTableView.setItems(model);
        
        // THEM ORGANIZE THEM
        editImagesToolbar.getChildren().add(slideShowTitleTextField);
        editImagesToolbar.getChildren().add(addAllImagesInDirectoryButton);
        editImagesToolbar.getChildren().add(addImageButton);
        editImagesToolbar.getChildren().add(removeImageButton);
        editImagesToolbar.getChildren().add(moveSlideUpButton);
        editImagesToolbar.getChildren().add(moveSlideDownButton);
        editImagesToolbar.getChildren().add(undoButton);
        editImagesToolbar.getChildren().add(redoButton);
        slidesTableScrollPane.setContent(slidesTableView);
        editPane.add(fileNamePromptLabel, 0, 0);
        editPane.add(fileNameTextField, 1, 0);
        editPane.add(pathPromptLabel, 0, 1);
        editPane.add(pathTextField, 1, 1);
        editPane.add(captionPromptLabel, 0, 2);
        editPane.add(captionTextField, 1, 2);
        editPane.add(originalWidthPromptLabel, 0, 3);
        editPane.add(originalWidthTextField, 1, 3);
        editPane.add(originalHeightPromptLabel, 0, 4);
        editPane.add(originalHeightTextField, 1, 4);
        editPane.add(currentWidthPromptLabel, 0, 5);
        editPane.add(currentWidthSlider, 1, 5);
        editPane.add(currentHeightPromptLabel, 0, 6);
        editPane.add(currentHeightSlider, 1, 6);
        editPane.add(updateButton, 0, 9);
        editPane.add(xPositionPromptLabel, 0, 7);
        editPane.add(xPositionSlider, 1, 7);
        editPane.add(yPositionPromptLabel, 0, 8);
        editPane.add(yPositionSlider, 1, 8);
        editPane.add(imageBorderPane, 1, 9);
        editTabelScrollPane.setContent(editPane);
        editTabelScrollPane.setMinWidth(600); 
        
        // DISABLE BUTTONS
        addAllImagesInDirectoryButton.setDisable(true);
        addImageButton.setDisable(true);
        removeImageButton.setDisable(true);
        updateButton.setDisable(true);
        moveSlideDownButton.setDisable(true);
        moveSlideUpButton.setDisable(true);
        xPositionSlider.setDisable(true);
        yPositionSlider.setDisable(true);
        
        // DISABLE THE DISPLAY TEXT FIELDS
        fileNameTextField.setDisable(true);
        pathTextField.setDisable(true);
        originalWidthTextField.setDisable(true);
        originalHeightTextField.setDisable(true);
        captionTextField.setDisable(true);
        currentWidthSlider.setDisable(true);
        currentHeightSlider.setDisable(true);
        undoButton.setDisable(true);
        redoButton.setDisable(true);
        slideShowTitleTextField.setDisable(true);
        
        // AND THEN PUT EVERYTHING INSIDE THE WORKSPACE
        app.getGUI().getTopToolbarPane().getChildren().add(editImagesToolbar);
        BorderPane workspaceBorderPane = new BorderPane();
        workspaceBorderPane.setCenter(slidesTableScrollPane);
        slidesTableScrollPane.setFitToWidth(true);
        slidesTableScrollPane.setFitToHeight(true);
        workspaceBorderPane.setRight(editTabelScrollPane);
        editTabelScrollPane.setFitToWidth(true);
        editTabelScrollPane.setFitToHeight(true);
        
        //(SY) SET CUSTOMIZED TICK MARKS
        currentWidthSlider.setMajorTickUnit(200);
        currentWidthSlider.setShowTickMarks(true);
        currentWidthSlider.setShowTickLabels(true);
        currentHeightSlider.setMajorTickUnit(200);
        currentHeightSlider.setShowTickMarks(true);
        currentHeightSlider.setShowTickLabels(true);
        xPositionSlider.setMajorTickUnit(100);
        xPositionSlider.setShowTickLabels(true);
        yPositionSlider.setMajorTickUnit(100);
        yPositionSlider.setShowTickLabels(true);
        // AND SET THIS AS THE WORKSPACE PANE
        workspace = workspaceBorderPane;
    }
    
    private void initControllers() {
        // NOW LET'S SETUP THE EVENT HANDLING
        controller = new SlideshowCreatorController(app);
        SlideshowCreatorData data = (SlideshowCreatorData)app.getDataComponent();

        addAllImagesInDirectoryButton.setOnAction(e->{
            int prevSize = data.getSlides().size();
            controller.handleAddAllImagesInDirectory();
            if(data.getSlides().size() - prevSize > 0){
                app.getGUI().getFileController().markAsEdited(app.getGUI());
                slidesTableView.getSelectionModel().clearSelection();
                resetDescription();
            }
        });
        addImageButton.setOnAction(e -> {
            int prevSize = data.getSlides().size();
            controller.handleAddSingleImageInDirectory();
            if(data.getSlides().size() - prevSize > 0){
                slidesTableView.getSelectionModel().select(data.getSlides().get(data.getSlides().size() - 1));
                app.getGUI().getFileController().markAsEdited(app.getGUI());
            }
        });
        slidesTableView.getSelectionModel().selectedItemProperty().addListener((v, oldItem, newItem) -> {
            if(newItem != null){
                controller.handleSelectSlide(newItem);
                removeImageButton.setDisable(false);
                captionTextField.setDisable(false);
                currentWidthSlider.setDisable(false);
                currentHeightSlider.setDisable(false);
                xPositionSlider.setDisable(false);
                yPositionSlider.setDisable(false);
                imageBorderPane.setVisible(true);
                if(data.getSlides().indexOf(slidesTableView.getSelectionModel().getSelectedItem()) == 0){
                    moveSlideUpButton.setDisable(true);
                    moveSlideDownButton.setDisable(false);
                }
                else if(data.getSlides().indexOf(slidesTableView.getSelectionModel().getSelectedItem()) == data.getSlides().size() - 1){
                    moveSlideUpButton.setDisable(false);
                    moveSlideDownButton.setDisable(true);
                }
                else{
                    moveSlideUpButton.setDisable(false);
                    moveSlideDownButton.setDisable(false);
                }
            }
            else{
                removeImageButton.setDisable(true);
                updateButton.setDisable(true);
                captionTextField.setDisable(true);
                currentWidthSlider.setDisable(true);
                currentHeightSlider.setDisable(true);
                moveSlideUpButton.setDisable(true);
                moveSlideDownButton.setDisable(true);
                xPositionSlider.setDisable(true);
                yPositionSlider.setDisable(true);
                imageBorderPane.setVisible(false);
            }
        });
        captionTextField.textProperty().addListener((v, oldText, newText) ->{
            if(!slidesTableView.getSelectionModel().getSelectedItem().getCaption().equals(newText))
                updateButton.setDisable(false);
            else
                updateButton.setDisable(true);
        });
        currentHeightSlider.valueProperty().addListener((v, oldValue, newValue) -> {
            if(newValue != null && slidesTableView.getSelectionModel().getSelectedItem().getCurrentHeight() == newValue.intValue())
                updateButton.setDisable(true);
            else
                updateButton.setDisable(false);
        });
        currentWidthSlider.valueProperty().addListener((v, oldValue, newValue) -> {
            if(newValue != null && slidesTableView.getSelectionModel().getSelectedItem().getCurrentWidth() == newValue.intValue())
                updateButton.setDisable(true);
            else
                updateButton.setDisable(false);
        });
        
        updateButton.setOnAction(e -> {
            controller.handleUpdateSlide();
            undoButton.setDisable(false);
        });
        removeImageButton.setOnAction(e -> {
            controller.handleRemoveSlide(slidesTableView.getSelectionModel().getSelectedItem());
        });
        app.getGUI().getFileToolbar().getChildren().get(0).setOnMousePressed(e -> {
            addAllImagesInDirectoryButton.setDisable(false);
            addImageButton.setDisable(false);
            slideShowTitleTextField.setDisable(false);
            slideShowTitleTextField.setText("");
        });
        slideShowTitleTextField.textProperty().addListener((v, oldText, newText) -> {
            if(!newText.equals(getInitSlideshowTitle())){
                app.getGUI().getFileController().markAsEdited(app.getGUI());
            }
            else
                app.getGUI().getFileController().markAsSaved(app.getGUI());
        });
        slideShowTitleTextField.focusedProperty().addListener( new ChangeListener<Boolean>() {
            String oldText, newText;
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if(newValue){
                        oldText = slideShowTitleTextField.getText();
                    }
                    else{
                        newText = slideShowTitleTextField.getText();
                        Transaction transaction = new Title_Transaction(oldText, newText, app);
                        controller.getTransaction_Stack().addTransaction(transaction);
                    }
                }
        });
        controller.getTransaction_Stack().getMostRecentTransaction().addListener(e -> {
            if(controller.getTransaction_Stack().getMostRecentTransaction().getValue() < 0)
                undoButton.setDisable(true);
            else
                undoButton.setDisable(false);
            if(controller.getTransaction_Stack().getMostRecentTransaction().getValue() >= controller.getTransaction_Stack().getTransactionSize() - 1)
                redoButton.setDisable(true);
            else
                redoButton.setDisable(false);
        });
        
        xPositionSlider.valueProperty().addListener((v, oldValue, newValue) -> {
            if(newValue != null && slidesTableView.getSelectionModel().getSelectedItem().getXPosition() == newValue.intValue())
                updateButton.setDisable(true);
            else
                updateButton.setDisable(false);
        });
        yPositionSlider.valueProperty().addListener((v, oldValue, newValue) -> {
            if(slidesTableView.getSelectionModel().getSelectedItem().getYPosition() == newValue.intValue())
                updateButton.setDisable(true);
            else
                updateButton.setDisable(false);
        });
        
        // MOVE SLIDE UP
        moveSlideUpButton.setOnAction(e ->{
            controller.handleMoveUpSlide();
        });
        // MOVE SLIDE DOWN
        moveSlideDownButton.setOnAction(e -> {
            controller.handleMoveDownSlide();
        });
        undoButton.setOnAction(e -> {
            controller.handleUndoSlide();
        });
        redoButton.setOnAction(e -> {
            controller.handleRedoSlide();
        });
        // VIEW SLIDESHOW
        app.getGUI().getViewButton().setOnAction(e ->{
            playButton.setDisable(false);
            controller.handleViewSlideshow();
            prevButton.setOnAction(e1 -> {
                controller.handlePrevButton();
            });
            nextButton.setOnAction(e2 -> {
                controller.handleNextButton();
            });
            playButton.setOnAction(e3 -> {
                controller.play1();
            });
            controller.getAnimation().pause();
        });
        app.getGUI().getPrimaryScene().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.Z && e.isControlDown()) {
                controller.handleUndoSlide();
            }
            if (e.getCode() == KeyCode.Y && e.isControlDown()) 
                controller.handleRedoSlide();
        });
    } 
    
    // WE'LL PROVIDE AN ACCESSOR METHOD FOR EACH VISIBLE COMPONENT
    // IN CASE A CONTROLLER OR STYLE CLASS NEEDS TO CHANGE IT
    
    private void initStyle() {
        editImagesToolbar.getStyleClass().add(CLASS_BORDERED_PANE);
        addAllImagesInDirectoryButton.getStyleClass().add(CLASS_EDIT_BUTTON);
        addImageButton.getStyleClass().add(CLASS_EDIT_BUTTON);
        removeImageButton.getStyleClass().add(CLASS_EDIT_BUTTON);
        moveSlideUpButton.getStyleClass().add(CLASS_EDIT_BUTTON);
        moveSlideDownButton.getStyleClass().add(CLASS_EDIT_BUTTON);
        undoButton.getStyleClass().add(CLASS_EDIT_BUTTON);
        redoButton.getStyleClass().add(CLASS_EDIT_BUTTON);
        slideShowTitleTextField.getStyleClass().add(CLASS_EDIT_TITLE_TEXT_FIELD);

        // THE SLIDES TABLE
        slidesTableView.getStyleClass().add(CLASS_SLIDES_TABLE);
        for (TableColumn tc : slidesTableView.getColumns())
            tc.getStyleClass().add(CLASS_SLIDES_TABLE);
        editPane.getStyleClass().add(CLASS_BORDERED_PANE);
        fileNamePromptLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        fileNameTextField.getStyleClass().add(CLASS_EDIT_TEXT_FIELD);
        pathPromptLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        pathTextField.getStyleClass().add(CLASS_EDIT_TEXT_FIELD);
        captionPromptLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        captionTextField.getStyleClass().add(CLASS_EDIT_TEXT_FIELD);
        originalWidthPromptLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        originalWidthTextField.getStyleClass().add(CLASS_EDIT_TEXT_FIELD);
        originalHeightPromptLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        originalHeightTextField.getStyleClass().add(CLASS_EDIT_TEXT_FIELD);
        currentWidthPromptLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        currentWidthSlider.getStyleClass().add(CLASS_EDIT_SLIDER);
        currentHeightPromptLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        currentHeightSlider.getStyleClass().add(CLASS_EDIT_SLIDER);
        updateButton.getStyleClass().add(CLASS_UPDATE_BUTTON);
        xPositionPromptLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        xPositionSlider.getStyleClass().add(CLASS_EDIT_SLIDER);
        yPositionPromptLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        yPositionSlider.getStyleClass().add(CLASS_EDIT_SLIDER);
    }
    
    public void resetDescription(){
        fileNameTextField.setText("");
        pathTextField.setText("");
        captionTextField.setText("");
        originalHeightTextField.setText("");
        originalWidthTextField.setText("");
        currentWidthSlider.setValue(0);
        currentHeightSlider.setValue(0);
    }
    @Override
    public void resetWorkspace() {
        addAllImagesInDirectoryButton.setDisable(false);
        addImageButton.setDisable(false);
        slidesTableView.getItems().clear();
        slideShowTitleTextField.setText(null);
        undoButton.setDisable(true);
        redoButton.setDisable(true);
        resetDescription();
    }
    @Override
    public void reloadWorkspace(AppDataComponent dataComponent) {}
    //(SY)
    public void setSlideshowTextField(String title){
        slideShowTitleTextField.setText(title);
    }
    public TextField getSlideshowTextField(){
        return slideShowTitleTextField;
    }
    public void setInitSlideshowTitle(String initTitle){
        initSlideshowTitle = initTitle;
    }
    public String getInitSlideshowTitle(){
        return initSlideshowTitle;
    }
    public HBox getEditImageToolBar(){
        return editImagesToolbar;
    }
    public TextField getSlideShowTitleTextField(){
        return slideShowTitleTextField;
    }
    public TableView getSlideshowTableView(){
        return slidesTableView;
    }
}
