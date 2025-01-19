package lanz.global.authenticationservice.service;

import lanz.global.authenticationservice.exception.BadRequestException;
import lanz.global.authenticationservice.repository.CompanyRepository;
import lanz.global.authenticationservice.repository.CurrencyRepository;
import lanz.global.authenticationservice.service.model.Company;
import lanz.global.authenticationservice.service.model.Currency;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CurrencyRepository currencyRepository;

    public List<Currency> findAllCurrencies() {
        return currencyRepository.findAll();
    }

    public Company register(String name, String country, UUID currencyId) throws BadRequestException {
        Currency currency = findCurrencyById(currencyId);

        Company company = new Company();
        company.setName(name);
        company.setCountry(country);
        company.setCurrency(currency);
        return companyRepository.save(company);
    }

    public Currency findCurrencyById(UUID currencyId) throws BadRequestException {
        if (currencyId == null) {
            return null;
        }

        return currencyRepository.findById(currencyId).orElseThrow(() -> new BadRequestException("exception.not-found.title", "exception.not-found.message", "Currency"));
    }

}
