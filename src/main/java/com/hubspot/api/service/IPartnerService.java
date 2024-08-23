package com.hubspot.api.service;

import com.hubspot.api.domain.Partner;

import java.util.List;

public interface IPartnerService {
    List<Partner> getPartnersAvailability();
}
