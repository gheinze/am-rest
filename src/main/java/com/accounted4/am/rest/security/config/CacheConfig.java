package com.accounted4.am.rest.security.config;

import javax.annotation.PreDestroy;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Creation of beans related to Cache object definitions.
 *
 * @author gheinze
 */
@Configuration
public class CacheConfig {


    /**
     * Properties for configuring a cache used to hold tokens during the handshake
     * mechanism to retrieve an Auth Code from an Authorization Server.
     *
     * @return
     */
    @Bean
    @ConfigurationProperties("security.oauth2.authCodeStateCache")
    CacheProperties authCodeState() {
        return new CacheProperties();
    }


    /**
     * A cache used to hold tokens during the handshake mechanism to retrieve an
     * Auth Code from an Authorization Server.
     */
    @Bean
    Cache authCodeStateCache() {

        CacheProperties config = authCodeState();
        Cache cache = new Cache(
                config.getCacheName(),
                config.getMaxElementsInMemory(),
                config.isOverFlowToDisk(),
                config.isEternal(),
                config.getTimeToLiveSeconds(),
                config.getTimeToIdleSeconds()
        );

        CacheManager cacheManager = CacheManager.create();
        cacheManager.addCache(cache);

        return cache;

    }


    @PreDestroy
    protected void destroy() {
        CacheManager.getInstance().shutdown();
    }

}
