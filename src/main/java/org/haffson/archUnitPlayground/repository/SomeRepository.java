package org.haffson.archUnitPlayground.repository;

import org.haffson.archUnitPlayground.persistence.SomeDataObject;

public class SomeRepository {

    public SomeDataObject findById(String id) {
        return new SomeDataObject(id);
    }

    public Boolean willReturnAlwaysFalse() {
        return false;
    }
}
