package Utils.Containers;

import Exceptions.ListException;
import Model.Value.Value;

public interface MyIOutput extends Iterable<Value>{
    void add(Value item);
    Value get(int index) throws ListException;
    int size();
}
