package lanz.global.authenticationservice.clients;

import lanz.global.authenticationservice.external.api.company.request.CreateCompanyRequest;
import lanz.global.authenticationservice.external.api.company.response.CompanyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "COMPANY-SERVICE")
public interface CompanyClient {

    @PostMapping("/company")
    ResponseEntity<CompanyResponse> createCompany(@RequestBody CreateCompanyRequest request);

    @GetMapping("/company/{companyId}")
    ResponseEntity<CompanyResponse> findCompanyById(@PathVariable UUID companyId);

}
