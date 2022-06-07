package com.df.configuration;

import com.df.converter.OwnerDtoReadConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.dialect.H2Dialect;

import java.util.List;

@Configuration
public class ConverterConfig {

    @Bean
    public R2dbcCustomConversions getCustomConverters() {
        List<Converter<?, ?>> converters = List.of(
                new OwnerDtoReadConverter()
        );

        return R2dbcCustomConversions.of(H2Dialect.INSTANCE, converters);
    }

}
