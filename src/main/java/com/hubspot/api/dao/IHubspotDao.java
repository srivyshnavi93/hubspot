package com.hubspot.api.dao;

import com.hubspot.api.domain.Invitation;
import com.hubspot.api.domain.Partner;

import java.util.List;
import java.util.Map;

public interface IHubspotDao {
    List<Partner> getPartnersAvailability();

    String sendInvitations(List<Invitation> invitationList);
}
