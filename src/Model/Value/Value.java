package Model.Value;

import Model.Type.Type;

public interface Value {
    boolean equals(Object another);
    Type getType();
    Value deepCopy();
}
