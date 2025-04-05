package lanz.global.authenticationservice.service;

import jakarta.ws.rs.InternalServerErrorException;
import lanz.global.authenticationservice.exception.BadRequestException;
import lanz.global.authenticationservice.external.api.company.request.CreateCompanyRequest;
import lanz.global.authenticationservice.external.api.company.response.CompanyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CompanyService {

    private final RestTemplate restTemplate;

    String URL = "http://company-service:80/company";

    public UUID createCompany(String name, String country, UUID currencyId) throws BadRequestException {
        CreateCompanyRequest request = new CreateCompanyRequest(name, country, currencyId);

        ResponseEntity<CompanyResponse> response = restTemplate.postForEntity(URL, request, CompanyResponse.class);

        return switch (response.getStatusCode()) {
            case HttpStatus.OK -> response.getBody().companyId();
            case HttpStatus.BAD_REQUEST -> throw new BadRequestException("exception.create-bad-request.title", "exception.create-bad-request.message", "Company");
            case null, default -> throw new InternalServerErrorException();
        };
    }

    public CompanyResponse getCompany(UUID companyId) {
        ResponseEntity<CompanyResponse> response = restTemplate.getForEntity(URL + String.format("/%s", companyId.toString()), CompanyResponse.class);

        return switch (response.getStatusCode()) {
            case HttpStatus.OK -> response.getBody();
            case HttpStatus.BAD_REQUEST -> throw new BadRequestException("exception.bad-request.title", "exception.bad-request.message", "Company");
            case null, default -> throw new InternalServerErrorException();
        };
    }
}
