package lanz.global.authenticationservice.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ServiceConverter {

    private final Map<Class<?>, BaseConverter<?, ?>> converters = new HashMap<>();

    @Autowired
    public ServiceConverter(List<BaseConverter<?, ?>> converterList) {
        for (BaseConverter<?, ?> converter : converterList) {
            Class<?> sourceType = (Class<?>) ((java.lang.reflect.ParameterizedType) converter.getClass()
                    .getGenericInterfaces()[0]).getActualTypeArguments()[0];
            converters.put(sourceType, converter);
        }
    }

    public <S, T> T convert(S source) {
        if (source == null) {
            return null;
        }

        BaseConverter<S, T> converter = (BaseConverter<S, T>) converters.get(source.getClass());
        if (converter == null) {
            throw new IllegalArgumentException("No converter found for type: " + source.getClass());
        }

        return converter.convertToDto(source);
    }

    public <S, T> List<T> convertList(List<S> sources) {
        if (sources == null) {
            return Collections.emptyList();
        }
        return (List<T>) sources.stream().map(this::convert).toList();
    }
}
