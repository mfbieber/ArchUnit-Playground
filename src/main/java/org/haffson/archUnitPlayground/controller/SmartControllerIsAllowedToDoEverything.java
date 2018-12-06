package org.haffson.archUnitPlayground.controller;

import org.haffson.archUnitPlayground.persistence.SomeDataObject;

/**
 * @author Most Awesome Developer Ever
 */
public class SmartControllerIsAllowedToDoEverything {

    public void getUserPasswordFromPersistence(String userId) {
        SomeDataObject someDataObject = new SomeDataObject(userId);
    }
}
