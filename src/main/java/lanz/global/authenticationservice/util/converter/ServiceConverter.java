package lanz.global.authenticationservice.util.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ServiceConverter {

    private final ConversionService conversionService;

    public <S, T> T convert(S source, Class<T> targetType) {
        return conversionService.convert(source, targetType);
    }

    public <S, T> List<T> convertList(List<S> sources, Class<T> targetType) {
        if (sources == null || sources.isEmpty()) {
            return Collections.emptyList();
        }

        return sources.stream()
                .map(source -> conversionService.convert(source, targetType))
                .toList();
    }

}
