package com.likelion.maydayspring.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "filter")
@Getter
@Setter
public class FilterProperties {

    private String categoryUrl;
    private String descriptionDetailUrl;

}
