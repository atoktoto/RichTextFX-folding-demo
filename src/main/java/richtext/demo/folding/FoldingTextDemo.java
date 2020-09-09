package richtext.demo.folding;

import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.NavigationActions.SelectionPolicy;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.IndexRange;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class FoldingTextDemo extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        StyledSegmentTextArea textArea = new StyledSegmentTextArea();
        textArea.setContextMenu( new DefaultContextMenu() );

        Scene scene = new Scene(new StackPane(new VirtualizedScrollPane<>(textArea)), 600, 400);
        primaryStage.setTitle("Fold Demo");
        primaryStage.setScene(scene);
        primaryStage.show();

        textArea.appendText( "Select parts of any two or more lines and right click.\n" +
        		"This is a second line of text\n" +
        		"And here is another line\n" +
        		"Now a fourth line to play with\n" +
        		"Unfold only works for the line the cursor is on." );
    }

    private class DefaultContextMenu extends ContextMenu
    {
    	private MenuItem fold, unfold, print;

    	public DefaultContextMenu()
    	{
    		fold = new MenuItem( "Fold" );
    		fold.setOnAction( AE -> { hide(); fold(); } );

    		unfold = new MenuItem( "Unfold" );
    		unfold.setOnAction( AE -> { hide(); unfold(); } );

    		print = new MenuItem( "Print" );
    		print.setOnAction( AE -> { hide(); print(); } );

    		getItems().addAll( fold, unfold, print );
    	}

    	/**
    	 * Folds multiple lines of selected text, only showing the first line and hiding the rest.
    	 * Any existing folded text in the selection is expanded and included in the new fold.
    	 */
    	private void fold()
    	{
    		StyledSegmentTextArea textArea = (StyledSegmentTextArea) getOwnerNode();

    		IndexRange range = textArea.getSelection();
    		if ( textArea.getAnchor() != range.getStart() ) {
    			// If this is a reverse selection convert it to a forward selection so lineEnd works
    			textArea.selectRange( range.getStart(), range.getEnd() );
    		}
    		textArea.lineEnd( SelectionPolicy.EXTEND );
    		textArea.fold( textArea.getSelection() );
    	}

    	/**
    	 * Unfold the CURRENT line/paragraph if it has a fold.
    	 */
    	private void unfold()
    	{
    		StyledSegmentTextArea textArea = (StyledSegmentTextArea) getOwnerNode();
    		textArea.unfold( textArea.getCurrentParagraph() );
    	}

    	private void print()
    	{
    		System.out.println( ((StyledSegmentTextArea) getOwnerNode()).getText() );
    	}
    }

}
