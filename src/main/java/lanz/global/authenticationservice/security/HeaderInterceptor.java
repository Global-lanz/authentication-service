package lanz.global.authenticationservice.security;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HeaderInterceptor implements RequestInterceptor {

    private final HttpServletRequest request;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        String accept = request.getHeader(HttpHeaders.ACCEPT);
        String language = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);


        Map<String, Collection<String>> headers = requestTemplate.headers();

        put(HttpHeaders.AUTHORIZATION, authorization, headers);
        put(HttpHeaders.ACCEPT, accept, headers);
        put(HttpHeaders.ACCEPT_LANGUAGE, language, headers);

        requestTemplate.headers(headers);
    }

    private void put(String key, String value, Map<String, Collection<String>> headers) {
        if (key != null && value != null) {
            headers.put(key, List.of(value));
        }
    }
}
