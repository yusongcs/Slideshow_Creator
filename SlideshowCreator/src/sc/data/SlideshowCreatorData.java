package sc.data;

import javafx.collections.ObservableList;
import djf.components.AppDataComponent;
import javafx.collections.FXCollections;
import javafx.scene.chart.XYChart;
import sc.SlideshowCreatorApp;

/**
 * This is the data component for SlideshowCreatorApp. It has all the data needed
 * to be set by the user via the User Interface and file I/O can set and get
 * all the data from this object
 * 
 * @author Richard McKenna
 *          Yu Song
 */
public class SlideshowCreatorData implements AppDataComponent {

    // WE'LL NEED ACCESS TO THE APP TO NOTIFY THE GUI WHEN DATA CHANGES
    SlideshowCreatorApp app;

    // NOTE THAT THIS DATA STRUCTURE WILL DIRECTLY STORE THE
    // DATA IN THE ROWS OF THE TABLE VIEW
    ObservableList<Slide> slides;

    /**
     * This constructor will setup the required data structures for use.
     * 
     * @param initApp The application this data manager belongs to. 
     */
    public SlideshowCreatorData(SlideshowCreatorApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;
        
        // MAKE THE SLIDES MODEL
        slides = FXCollections.observableArrayList();
    }
    
    // ACCESSOR METHOD
    public ObservableList<Slide> getSlides() {
        return slides;
    }
    
    /**
     * Called each time new work is created or loaded, it resets all data
     * and data structures such that they can be used for new values.
     */
    @Override
    public void resetData() {
        slides.removeAll();
    }
    
    public void addSlide(Slide slide){
        Slide slideToAdd = slide;
        if (!hasSlide(slide.getFileName(), slide.getPath()))
            slides.add(slideToAdd);
    }
    public void addSlide(Slide slide, int position){
        if (!hasSlide(slide.getFileName(), slide.getPath()))
            slides.add(position, slide);
    }
    // FOR ADDING A SLIDE WHEN THERE ISN'T A CUSTOM SIZE
    public void addSlide(String fileName, String path, String caption, int originalWidth, int originalHeight) {
        Slide slideToAdd = new Slide(fileName, path, caption, originalWidth, originalHeight);
        slides.add(slideToAdd);
    }

    // FOR ADDING A SLIDE WITH A CUSTOM SIZE
    public void addSlide(String fileName, String path, String caption, int originalWidth, int originalHeight, int currentWidth, int currentHeight) {
        Slide slideToAdd = new Slide(fileName, path, caption, originalWidth, originalHeight);
        slideToAdd.setCurrentWidth(currentWidth);
        slideToAdd.setCurrentHeight(currentHeight);
        slides.add(slideToAdd);
    }
    
    public void addSlide(String fileName, String path, String caption, int originalWidth, int originalHeight, 
            int currentWidth, int currentHeight, int xPosition, int yPosition) {
        Slide slideToAdd = new Slide(fileName, path, caption, originalWidth, originalHeight);
        slideToAdd.setCurrentWidth(currentWidth);
        slideToAdd.setCurrentHeight(currentHeight);
        slideToAdd.setXPosition(xPosition);
        slideToAdd.setYPosition(yPosition);
        slides.add(slideToAdd);
    }
    
    public void addNonDuplicateSlide(String fileName, String path, String caption, int originalWidth, int originalHeight) {
        if (!hasSlide(fileName, path))
            addSlide(fileName, path, caption, originalWidth, originalHeight);        
    }
    
    public void addNonDuplicateSlide(String fileName, String path, String caption, int originalWidth, int originalHeight, int currentWidth, int currentHeight) {
        if (!hasSlide(fileName, path))
            addSlide(fileName, path, caption, originalWidth, originalHeight, currentWidth, currentHeight);        
    }
    
    public void addNonDuplicateSlide(String fileName, String path, String caption, int originalWidth, int originalHeight, 
            int currentWidth, int currentHeight, int x, int y) {
        if (!hasSlide(fileName, path))
            addSlide(fileName, path, caption, originalWidth, originalHeight, currentWidth, currentHeight, x, y);        
    }
    
    public boolean hasSlide(String testFileName, String testPath) {
        for (Slide s : slides) {
            if (s.getFileName().equals(testFileName)
                    && s.getPath().equals(testPath))
                return true;
        }
        return false;
    }
    
    public void removeSlide(Slide slideToRemove) {
        slides.remove(slideToRemove);
    }
}