package com.accounted4.am.rest.security.config;

import lombok.Getter;
import lombok.Setter;


/**
 * Configuration values used for creating a cache. Typically loaded from
 * configuration during container startup.
 *
 * @author gheinze
 */
@Getter @Setter
public class CacheProperties {

    private String cacheName;
    private int maxElementsInMemory;
    private boolean overFlowToDisk;
    private boolean eternal;
    private long timeToLiveSeconds;
    private long timeToIdleSeconds;

}
