package com.hubspot.api.controller;

import com.hubspot.api.domain.Invitation;
import com.hubspot.api.domain.Partner;
import com.hubspot.api.service.PartnerService;
import com.hubspot.api.service.InvitationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hubspot/invitation/")
public class InvitationController {

    @Autowired
    private InvitationService invitationService;

    @Autowired
    private PartnerService partnerService;

    private List<Partner> partnersList;
    private List<Invitation> invitationsList;

    @ApiOperation(value = "method to get list of partners with availability, transform the data and send post request for invitations.")
    @RequestMapping(value = "/" + "partners", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String getPartnersAndSendInvitations() {
        partnersList = partnerService.getPartnersAvailability();

        if (CollectionUtils.isEmpty(partnersList)) {
            System.out.println("Unable to get partners list information");
            return "Unable to get partners list.";
        }

        invitationsList = invitationService.getInvitationsList(partnersList);
        return invitationService.sendInvitations(invitationsList);
    }
}
