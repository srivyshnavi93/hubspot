package com.hubspot.api.service;

import com.hubspot.api.domain.Invitation;
import com.hubspot.api.domain.Partner;

import java.util.List;
import java.util.Map;

public interface IInvitationService {

    List<Invitation> getInvitationsList(List<Partner> partnersList);

    String sendInvitations(List<Invitation> invitations);
}
