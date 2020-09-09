package richtext.demo.folding;

class Fold
{
	private String first, folded;

	public Fold() {}
	public Fold( String firstLine, String foldedText )
	{
		folded = foldedText;
		first = firstLine;
	}

	public String getFirst() { return first; }
	public String getFold() { return folded; }

	public void setFirst( String line )
	{
		first = line;
	}

	public void setFold( String text )
	{
		folded = text;
	}

    @Override public boolean equals( Object obj )
    {
    	if ( obj == this )  return true;
    	else if ( obj instanceof Fold )
        {
    		Fold  other = (Fold) obj;
            return first.equals( other.getFirst() )
              && folded.equals( other.getFold() );
        }

        return false;
    }
}
