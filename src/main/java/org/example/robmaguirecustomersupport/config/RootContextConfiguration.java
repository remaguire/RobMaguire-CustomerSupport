package org.example.robmaguirecustomersupport.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

@Configuration
@ComponentScan(
        basePackages = "org.example.robmaguirecustomersupport.site",
        excludeFilters = @ComponentScan.Filter(Controller.class)
)
public class RootContextConfiguration {
}
