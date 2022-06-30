package com.kr.realworldspringboot.config;

import com.kr.realworldspringboot.util.LocalDateUtcParser;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        return modelMapper;
    }

    @Bean
    public LocalDateUtcParser localDateUtcParser(){
        return new LocalDateUtcParser();
    }
}
