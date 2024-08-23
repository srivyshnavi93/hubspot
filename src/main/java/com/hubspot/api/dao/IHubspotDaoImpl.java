package com.hubspot.api.dao;

import com.hubspot.api.domain.Invitation;
import com.hubspot.api.domain.Partner;
import com.hubspot.api.domain.PartnerResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.hubspot.api.utils.Constants.COUNTRIES;

@Repository
public class IHubspotDaoImpl implements IHubspotDao {

    @Value("${hubspot.api.get.partners.url}")
    private String getPartnersUrl;

    @Value("${hubspot.api.post.invitation.url}")
    private String postInvitationUrl;

    @Override
    public List<Partner> getPartnersAvailability() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PartnerResponse> responseEntity = restTemplate.exchange(getPartnersUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<PartnerResponse>() {
                });
        return Objects.requireNonNull(responseEntity.getBody()).getPartners();
    }

    @Override
    public String sendInvitations(List<Invitation> invitationList) {
        String response;
        try {
            Map<String, List<Invitation>> list = new HashMap<>();
            list.put(COUNTRIES, invitationList);

            HttpEntity<Map<String, List<Invitation>>> request = new HttpEntity<>(list);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Invitation> result = restTemplate.postForEntity(postInvitationUrl, request,
                    Invitation.class);
            response = result.getStatusCode().toString();

        } catch (HttpClientErrorException ex) {
            System.out.println("Exception status code: " + ex.getStatusCode());
            System.out.println("Exception response body: " + ex.getResponseBodyAsString());
            System.out.println("Exception during send invitations post request: " + ex.getMessage());
            response = ex.getResponseBodyAsString();
        }
        return response;

    }

}
