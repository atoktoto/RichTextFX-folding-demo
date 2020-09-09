package richtext.demo.folding;

import java.util.Optional;

import org.fxmisc.richtext.TextExt;

import javafx.scene.Node;
import javafx.scene.text.Text;

public class FoldedSegment extends AbstractSegment<Fold>
{
	public FoldedSegment( Fold data )
	{
		super( data );
	}
	public FoldedSegment( String text, String folded )
	{
		super( new Fold( text, folded ) );
	}

	@Override
	public Node createNode( String style )
	{
		Text  textNode = new TextExt( getData().getFirst() );
		if ( style != null && ! style.isEmpty() ) textNode.getStyleClass().add( style );
		return textNode;
	}

	@Override
	public char charAt( int index )
	{
        return getText().charAt( index );
	}

	@Override
	public String getText() { return getData().getFirst(); }
	public String getFold() { return getData().getFold(); }

	@Override
	public int length() { return getText().length(); }

	@Override
	public Optional<AbstractSegment<?>> subSequence( int start, int end )
	{
		if ( start == 0 && end == length() )  return Optional.of( this );

		if ( end == length() )  return Optional.of
		(
			new FoldedSegment( getText().substring( start, end ), getFold() )
		);

		return Optional.of
		(
			new TextSegment( getText().substring( start, end ) )
		);
	}

	@Override
	public Optional<AbstractSegment<?>> join( AbstractSegment<?> nextSeg )
	{
		if ( nextSeg instanceof FoldedSegment )
		{
			return Optional.of( new FoldedSegment( getText(), getFold() + nextSeg.toString() ) );
		}
		if ( nextSeg instanceof TextSegment )
		{
			return Optional.of( new FoldedSegment( getText() + nextSeg.getText(), getFold() ) );
		}
		return Optional.empty();
	}

	@Override
	public String toString()
	{
		return getText() + getFold();
	}
}
