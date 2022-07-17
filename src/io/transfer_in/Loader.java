package io.transfer_in;

import java.io.IOException;

import data_structures.Board;

public interface Loader 
{
	Board load() throws IOException;
}
