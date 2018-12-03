package dao;

import java.io.IOException;
import java.io.RandomAccessFile;

public interface IHandle {
	public void handle(String line) throws IOException;
}
