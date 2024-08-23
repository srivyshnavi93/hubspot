package com.hubspot.api.service;

import com.hubspot.api.dao.IHubspotDao;
import com.hubspot.api.domain.Partner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartnerService implements IPartnerService {

    @Autowired
    private IHubspotDao hubspotDao;

    @Override
    public List<Partner> getPartnersAvailability() {
        List<Partner> partnersList = hubspotDao.getPartnersAvailability();
        return partnersList;
    }
}
