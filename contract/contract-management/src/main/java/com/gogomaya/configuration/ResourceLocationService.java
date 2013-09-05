package com.gogomaya.configuration;

public interface ResourceLocationService {

    public ResourceLocations getResources(long playerId, ResourceLocations resourceLocations);

    public String getGameActionServer(long playerId, long sessionId);

}
