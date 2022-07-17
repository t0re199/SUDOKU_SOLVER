package data_structures;

public interface Subsection
{
	int getId();
	
	boolean contains(int element);
	
	int getRowMin();
	
	int getRowMax();
	
	int getColMin();
	
	int getColMax();
}
