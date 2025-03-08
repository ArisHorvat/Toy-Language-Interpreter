package Utils.Containers;

import Exceptions.DictException;
import Model.Value.StringValue;

import java.io.BufferedReader;
import java.util.Set;

public interface MyIFileTable {
    void open(StringValue key, BufferedReader value);
    BufferedReader getBuffer(StringValue key);
    boolean containsFile(StringValue key);
    BufferedReader close(StringValue key) throws DictException;
    Set<StringValue> getFiles();
}
