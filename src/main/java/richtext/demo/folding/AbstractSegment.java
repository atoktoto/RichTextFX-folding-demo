package richtext.demo.folding;

import java.util.Optional;

import javafx.scene.Node;

/**
 * Most of these methods are derived from the TextOps interface implemented by MySegmentedOps,
 * which delegates its method calls (here) to instances of AbstractSegment subclasses.
 *
 * Only createNode() and getData() are not directly from RichTextFX. The former is used
 * by a node factory function which we have to pass to RichTextFX and the latter is
 * required to extract any data out of a custom segment.
 *
 * @author Jurgen
 */
public abstract class AbstractSegment<T>
{
	protected final T data;

	public AbstractSegment( T data )
	{
		this.data = data;
	}

	public String getText() { return "\ufffc"; }
	public T getData() { return data; }
	public int length() { return 1; }
	public char charAt( int index )
	{
        return getText().charAt(0);
	}

	public Optional<AbstractSegment<?>> subSequence( int start, int end )
	{
		if ( start == 0 )  return Optional.of( this );
		return Optional.empty();
	}

	public Optional<AbstractSegment<?>> join( AbstractSegment<?> nextSeg )
	{
		return Optional.empty();
	}

	public abstract Node createNode( String style );

	/**
	 * RichTextFX uses this for undo and redo.
	 */
    @Override public boolean equals( Object obj )
    {
    	if ( obj == this )  return true;
    	else if ( obj instanceof AbstractSegment && getClass().equals( obj.getClass() ) )
        {
            return getData().equals( ((AbstractSegment<?>) obj).getData() );
        }

        return false;
    }

    @Override
	public String toString()
    {
    	return getText();
    }
}
