/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.uva.sne.vre4eic.conf;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 *
 * @author S. Koulouzis
 */
@Configuration
@PropertySources({
    @PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
    ,
    @PropertySource(value = "file:etc/application.properties", ignoreResourceNotFound = true)
})
@ComponentScan("nl.uva.sne.vre4eic")
public class PropertiesConf {
    
}
