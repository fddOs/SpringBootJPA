/*package com.example.config;

import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.bull.javamelody.MonitoredSpringAsyncAndScheduledPointcut;
import net.bull.javamelody.MonitoredSpringControllerAndServicePointcut;
import net.bull.javamelody.MonitoredWithAnnotationPointcut;
import net.bull.javamelody.MonitoringSpringAdvisor;
import net.bull.javamelody.SpringContext;
import net.bull.javamelody.SpringDataSourceBeanPostProcessor;
import net.bull.javamelody.SpringRestTemplateBeanPostProcessor;

@Configuration
public class JavaMelodyConfig {
	@Bean
	public MonitoringSpringAdvisor monitoringSpringAdvisor() {
		return new MonitoringSpringAdvisor(new MonitoredWithAnnotationPointcut());
	}

	@Bean
	public MonitoringSpringAdvisor monitoringAsyncAndScheduledAdvisor() {
		return new MonitoringSpringAdvisor(new MonitoredSpringAsyncAndScheduledPointcut());
	}

	@Bean
	public MonitoringSpringAdvisor monitoringControllerAndServiceAdvisor() {
		return new MonitoringSpringAdvisor(new MonitoredSpringControllerAndServicePointcut());
	}

	@Bean
	public SpringDataSourceBeanPostProcessor springDataSourceBeanPostProcessor() {
		return new SpringDataSourceBeanPostProcessor();
	}

	@Bean
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		return new DefaultAdvisorAutoProxyCreator();
	}

	@Bean
	public SpringRestTemplateBeanPostProcessor springRestTemplateBeanPostProcessor() {
		return new SpringRestTemplateBeanPostProcessor();
	}

	@Bean
	public SpringContext springContext() {
		return new SpringContext();
	}

	@Bean
	public MonitoringSpringAdvisor facadeMonitoringAdvisor() {
		JdkRegexpMethodPointcut pointCut = new JdkRegexpMethodPointcut();
		pointCut.setPatterns("com\\.example\\..*");
		return new MonitoringSpringAdvisor(pointCut);
	}
}
*/