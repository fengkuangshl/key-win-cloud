package com.key.win.ribbon;

import com.key.win.ribbon.rule.DiscoveryEnabledRule;
import com.key.win.ribbon.rule.MetadataAwareRule;
import com.key.win.ribbon.core.concurrent.RibbonFilterContextConcurrencyStrategy;
import com.netflix.niws.loadbalancer.DiscoveryEnabledNIWSServerList;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * The Ribbon discovery filter auto configuration.
 *
 * @author Jakub Narloch
 */
@Configuration
@ConditionalOnClass(DiscoveryEnabledNIWSServerList.class)
@AutoConfigureBefore(RibbonClientConfiguration.class)
@ConditionalOnProperty(value = "ribbon.filter.metadata.enabled", havingValue="true" )
public class RibbonDiscoveryRuleAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public DiscoveryEnabledRule metadataAwareRule() {
        return new MetadataAwareRule();
    }
    
    @Bean
	public RibbonFilterContextConcurrencyStrategy requestAttributeHystrixConcurrencyStrategy() {
		return new RibbonFilterContextConcurrencyStrategy();
	}
}
