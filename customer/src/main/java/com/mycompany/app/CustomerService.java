package com.mycompany.app;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
@Service
public class CustomerService{


    private final CustomerRepository customerRepository;
    private final RestTemplate restTemplate;
    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .email(request.email())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .build();
        customerRepository.saveAndFlush(customer);
        FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
                "http://FRAUD/api/v1/fraud-check/{customerId}",
                FraudCheckResponse.class,
                customer.getId()
        );
        if(fraudCheckResponse.isFraudster()){
            throw new IllegalArgumentException("fraudster");
        }

    }
}
