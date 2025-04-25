package lanz.global.authenticationservice.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lanz.global.authenticationservice.exception.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FeignErrorDecoder {

    private final ObjectMapper objectMapper;

    public ErrorResponse decode(FeignException exception) {
        return exception.responseBody()
                .map(buffer -> {
                    try {
                        return objectMapper.readValue(toByteArray(buffer), ErrorResponse.class);
                    } catch (Exception e) {
                        // Log se quiser
                        return null;
                    }
                })
                .orElse(null);
    }

    private byte[] toByteArray(ByteBuffer buffer) {
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return bytes;
    }

}
