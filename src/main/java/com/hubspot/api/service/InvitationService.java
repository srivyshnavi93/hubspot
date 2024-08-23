package com.hubspot.api.service;

import com.hubspot.api.dao.IHubspotDao;
import com.hubspot.api.domain.Invitation;
import com.hubspot.api.domain.Partner;
import com.hubspot.api.utils.InvitationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class InvitationService implements IInvitationService {

    @Autowired
    private IHubspotDao hubspotDao;

    @Override
    public List<Invitation> getInvitationsList(List<Partner> partnersList) {
        return InvitationHelper.planInvitations(partnersList);
    }

    @Override
    public String sendInvitations(List<Invitation> invitations) {
        return hubspotDao.sendInvitations(invitations);
    }
}
