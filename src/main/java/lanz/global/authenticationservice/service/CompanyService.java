package lanz.global.authenticationservice.service;

import lanz.global.authenticationservice.exception.BadRequestException;
import lanz.global.authenticationservice.external.api.company.CompanyClient;
import lanz.global.authenticationservice.external.api.company.request.CreateCompanyRequest;
import lanz.global.authenticationservice.external.api.company.response.CompanyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CompanyService {

    private final CompanyClient companyClient;

    public UUID createCompany(String name, String country, UUID currencyId) throws BadRequestException {
        CreateCompanyRequest request = new CreateCompanyRequest(name, country, currencyId);

        return companyClient.createCompany(request).companyId();
    }

    public CompanyResponse getCompany(UUID companyId) {
        return companyClient.findCompanyById(companyId);
    }
}
