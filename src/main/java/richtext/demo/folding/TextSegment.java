package richtext.demo.folding;

import java.util.Optional;

import org.fxmisc.richtext.TextExt;

import javafx.scene.Node;
import javafx.scene.text.Text;

public class TextSegment extends AbstractSegment<String>
{
	public TextSegment( String text )
	{
		super( text );
	}

	@Override
	public Node createNode( String style )
	{
		Text  textNode = new TextExt( getData() );
		if ( style != null && ! style.isEmpty() ) textNode.getStyleClass().add( style );
		return textNode;
	}

	@Override
	public char charAt( int index )
	{
        return getData().charAt( index );
	}

	@Override
	public String getText() { return getData(); }

	@Override
	public int length() { return getData().length(); }

	@Override
	public Optional<AbstractSegment<?>> subSequence( int start, int end )
	{
		if ( start == 0 && end == length() )  return Optional.of( this );
		return Optional.of
		(
			new TextSegment( getData().substring( start, end ) )
		);
	}

	@Override
	public Optional<AbstractSegment<?>> join( AbstractSegment<?> nextSeg )
	{
		if ( nextSeg instanceof FoldedSegment )
		{
			return Optional.of( new FoldedSegment( getData() + nextSeg.getText(), ((FoldedSegment) nextSeg).getFold() ));
		}
		if ( nextSeg instanceof TextSegment )
		{
			return Optional.of( new TextSegment( getData() + nextSeg.getText() ) );
		}
		return Optional.empty();
	}

}
