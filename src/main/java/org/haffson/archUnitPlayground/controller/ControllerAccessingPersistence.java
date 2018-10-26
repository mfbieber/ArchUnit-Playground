package org.haffson.archUnitPlayground.controller;

import org.haffson.archUnitPlayground.persistence.SomeDataObject;

public class ControllerAccessingPersistence {

    public void getUserPasswordFromPersistence(String userId) {
        SomeDataObject someDataObject = new SomeDataObject(userId);
    }

}