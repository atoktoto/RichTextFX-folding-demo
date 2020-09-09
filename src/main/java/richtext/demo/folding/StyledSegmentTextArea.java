package richtext.demo.folding;//package lightware.richtext;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.fxmisc.richtext.GenericStyledArea;
import org.fxmisc.richtext.model.Codec;
import org.fxmisc.richtext.model.ReadOnlyStyledDocument;
import org.fxmisc.richtext.model.StyledSegment;
import org.fxmisc.richtext.model.TextOps;

import javafx.scene.Node;
import javafx.scene.control.IndexRange;
import javafx.scene.text.TextFlow;

public class StyledSegmentTextArea extends GenericStyledArea<String,AbstractSegment<?>,String>
{
	private static final String  initialParStyle = "";  // May not be null, otherwise copy paste fails !
	private static final String  initialSegStyle = "";  // May not be null, otherwise copy paste fails !
	private static final boolean preserveStyle = true;  // May not be false, otherwise undo doesn't work on custom nodes !

	private static final BiConsumer<TextFlow,String>  applyParStyle = (txtflow,pstyle) -> txtflow.getStyleClass().add( pstyle );
	private static final TextOps<AbstractSegment<?>,String>  segmentOps = new MySegmentOps();

	public StyledSegmentTextArea( Function<StyledSegment<AbstractSegment<?>, String>, Node> nodeFactory )
	{
		super( initialParStyle, applyParStyle, initialSegStyle, segmentOps, preserveStyle, nodeFactory );
    	setStyleCodecs( Codec.STRING_CODEC, new MySegmentCodec() );	 // Needed for copy paste.
        setWrapText(true);
	}

	public StyledSegmentTextArea()
	{
		this( styledSeg -> styledSeg.getSegment().createNode( styledSeg.getStyle() ) );
	}

	public void append( AbstractSegment<?> customSegment )
	{
        insert( getLength(), customSegment );
	}

	public void insert( int pos, AbstractSegment<?> customSegment )
	{
		insert( pos, ReadOnlyStyledDocument.fromSegment( customSegment, initialParStyle, initialSegStyle, segmentOps ) );
	}

	public void fold( IndexRange range )
	{
		fold( range.getStart(), range.getEnd() );
	}

	public void fold( int start, int end )
	{
		String[] fold = subDocument( start, end ).getParagraphs().stream()
			// fancy gymnastics to include any already folded sections in the selection
			.flatMap( p -> p.getSegments().stream() )
			.map( seg -> seg.toString() )
			.collect( Collectors.joining( "\n" ) )
			.split( "(?=\n)", 2 );

		// fold[0] will be the first line, and fold[1] the rest of the selected text

		if ( fold.length > 1 ) {
    		FoldedSegment newFold = new FoldedSegment( fold[0], fold[1] );
    		replace( start, end, newFold, "" );
		}
	}

	public void unfold( int paragraph )
	{
		int start = 0;
		AbstractSegment<?> unfold = null;

		for ( AbstractSegment<?> seg : getParagraph( paragraph ).getSegments() )
		{
			if ( seg instanceof FoldedSegment ) {
				unfold = seg;
				break;
			}
			start += seg.length();
		}

		if ( unfold != null )
		{
			int end = start + unfold.length();
			unfold = new TextSegment( unfold.toString() );
			replace( paragraph, start, paragraph, end, unfold, "" );
		}
	}

	@Override // to expand folded sections to text
	public String getText()
	{
		return getParagraphs().stream()
			.flatMap( p -> p.getSegments().stream() )
			.map( seg -> seg.toString() )
			.collect( Collectors.joining("\n") );
	}
}
