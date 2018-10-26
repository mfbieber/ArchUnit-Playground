package org.haffson.archUnitPlayground.controller;

import org.haffson.archUnitPlayground.repository.SomeRepository;

public class ControllerAccessingRepository {

    private final SomeRepository someRepository;

    public ControllerAccessingRepository(SomeRepository someRepository) {
        this.someRepository = someRepository;
    }

    public void getUserPasswordFromPersistence(String userId) {
        someRepository.findById(userId);
    }

}
