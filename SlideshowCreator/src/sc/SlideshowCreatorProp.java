package sc;

/**
 * This enum provides a list of all the user interface
 * text that needs to be loaded from the XML properties
 * file. By simply changing the XML file we could initialize
 * this application such that all UI controls are provided
 * in another language.
 * 
 * @author Richard McKenna
 *          Yu Song
 * @version 1.0
 */
public enum SlideshowCreatorProp {
    // FOR SIMPLE OK/CANCEL DIALOG BOXES
    OK_PROMPT,
    CANCEL_PROMPT,
    
    // THESE ARE FOR TEXT PARTICULAR TO THE APP'S WORKSPACE CONTROLS
    SLIDES_HEADER_TEXT,
    ADD_ALL_IMAGES_BUTTON_TEXT,
    ADD_IMAGE_BUTTON_TEXT,
    REMOVE_IMAGE_BUTTON_TEXT,
    FILE_NAME_COLUMN_TEXT,
    CAPTION_COLUMN_TEXT,
    ORIGINAL_WIDTH_COLUMN_TEXT,
    ORIGINAL_HEIGHT_COLUMN_TEXT,
    CURRENT_WIDTH_COLUMN_TEXT,
    CURRENT_HEIGHT_COLUMN_TEXT,
    FILE_NAME_PROMPT_TEXT,
    PATH_PROMPT_TEXT,
    CAPTION_PROMPT_TEXT,
    ORIGINAL_WIDTH_PROMPT_TEXT,
    ORIGINAL_HEIGHT_PROMPT_TEXT,
    CURRENT_WIDTH_PROMPT_TEXT,
    CURRENT_HEIGHT_PROMPT_TEXT,
    UPDATE_BUTTON_TEXT,
    MOVE_SLIDE_UP_BUTTON_TEXT,
    MOVE_SLIDE_DOWN_BUTTON_TEXT,
    X_POSITION_PROMPT_TEXT,
    Y_POSITION_PROMPT_TEXT,
    X_POSITION_COLUMN_TEXT,
    Y_POSITION_COLUMN_TEXT,
    UNDO_BUTTON_ICON,
    REDO_BUTTON_ICON,
    PREVIOUS_BUTTON_TEXT,
    NEXT_BUTTON_TEXT,
    PLAY_BUTTON_TEXT,
    PAUSE_BUTTON_TEXT,
    
    // FOR OTHER INTERACTIONS
    APP_PATH_WORK,
    INVALID_IMAGE_PATH_TITLE,
    INVALID_IMAGE_PATH_MESSAGE
}
