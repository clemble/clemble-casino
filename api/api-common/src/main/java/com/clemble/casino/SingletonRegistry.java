package com.clemble.casino;

public class SingletonRegistry implements ServerRegistry {

    /**
     * Generated 
     */
    private static final long serialVersionUID = -8555108439816572727L;

    final private String baseUrl;

    public SingletonRegistry(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public String findBase() {
        return baseUrl;
    }

    @Override
    public String findById(String identifier) {
        return baseUrl;
    }

    @Override
    public String findByIdAndType(String identifier, Object type) {
        return baseUrl;
    }

}
