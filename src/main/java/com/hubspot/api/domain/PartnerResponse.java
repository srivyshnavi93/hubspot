package com.hubspot.api.domain;

import java.util.List;

public class PartnerResponse {
    private List<Partner> partners;

    public List<Partner> getPartners() {
        return partners;
    }

    public void setPartners(List<Partner> partners) {
        this.partners = partners;
    }
}
