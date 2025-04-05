package lanz.global.authenticationservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;

import java.util.List;

@Configuration
public class ConverterConfig {

    @Bean
    public ConversionService conversionService(List<Converter<?, ?>> converters) {
        GenericConversionService service = new DefaultConversionService();
        converters.forEach(service::addConverter);
        return service;
    }
}