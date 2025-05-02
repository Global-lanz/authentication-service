package lanz.global.authenticationservice.exception;

import lombok.Getter;

import java.io.Serial;

@Getter
public class ServiceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 7926009157034245038L;

    private final String title;
    private final String message;
    private final String[] arguments;

    public ServiceException(String title, String message, String... arguments) {
        super(message);
        this.title = title;
        this.message = message;
        this.arguments = arguments;
    }

  public ServiceException(String title, String message) {
    super(message);
    this.title = title;
    this.message = message;
    this.arguments = null;
  }

}
