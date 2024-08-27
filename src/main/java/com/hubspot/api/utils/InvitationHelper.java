package com.hubspot.api.utils;

import com.hubspot.api.domain.Invitation;
import com.hubspot.api.domain.Partner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.hubspot.api.utils.Constants.DATE_FORMAT;
import static com.hubspot.api.utils.Constants.DIFF_IN_DAYS;

public class InvitationHelper {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    /**
     * Generate country to available dates map as following: 1. Create unique country & their partners map from list of
     * partners. 2. For each unique country, loop and do the following: 2.a for each partner which matches the country,
     * create list of dates which has diff b/w end and start date is exactly one day. 3. from list of above dates, count
     * the duplicates and get the maximum duplicate counts 4. for that maximum count dates, find out the lowest one and
     * add to that country. 5. This added country to date map for each country.
     *
     * @param partners
     * @return returns list of invitation.
     *
     */
    public static List<Invitation> planInvitations(List<Partner> partners) {
        Map<String, List<Partner>> partnersByCountry = partners.stream()
                .collect(Collectors.groupingBy(Partner::getCountry));

        Map<String, String> countryEvents = new HashMap<>();

        for (Map.Entry<String, List<Partner>> entry : partnersByCountry.entrySet()) {
            String country = entry.getKey();
            List<Partner> countryPartners = entry.getValue();
            String earliestMaxDate = findBestEventDates(countryPartners);
            countryEvents.put(country, earliestMaxDate);
        }

        return createInvitations(countryEvents, partnersByCountry);
    }

    /**
     * Create invitation request post object.
     * 
     * @param partnersList
     * @return returns the best date to meet for that country.
     */
    public static String findBestEventDates(List<Partner> partnersList) {
        List<String> datesList = new ArrayList<>();

        for (Partner partner : partnersList) {
            List<String> availableDates = partner.getAvailableDates();
            for (int i = 0; i < availableDates.size() - 1; i++) {
                if (getDiffInDates(availableDates.get(i), availableDates.get(i + 1)) == DIFF_IN_DAYS) {
                    datesList.add(availableDates.get(i));
                }
            }
        }

        // Count occurrences of each date in the list.
        Map<String, Long> datesCountMap = datesList.stream()
                .collect(Collectors.groupingBy(date -> date, Collectors.counting()));

        // Find the maximum count of dates.
        long maxDateCount = Collections.max(datesCountMap.values());

        // Get the list of dates with the maximum count and sort them.
        List<String> similarCountDatesList = datesCountMap.entrySet().stream()
                .filter(entry -> entry.getValue() == maxDateCount).map(Map.Entry::getKey).sorted()
                .collect(Collectors.toList());

        // The first date in the sorted list is the lowest date with the maximum count.
        return similarCountDatesList.get(0);
    }

    /**
     * Create invitation request post object.
     * 
     * @param countryToDateMap
     * @param countriesToIndexMap
     * @return returns list of invitations
     */
    public static List<Invitation> createInvitations(Map<String, String> countryToDateMap,
            Map<String, List<Partner>> countriesToIndexMap) {

        List<Invitation> invitationsList = new ArrayList<>();

        for (String country : countriesToIndexMap.keySet()) {
            Set<String> attendeesList = new HashSet<>();

            for (Partner partner : countriesToIndexMap.get(country)) {
                List<String> datesList = partner.getAvailableDates();
                String eventDate = countryToDateMap.get(country);

                if (datesList.contains(eventDate)) {
                    try {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(dateFormat.parse(eventDate));
                        cal.add(Calendar.DATE, DIFF_IN_DAYS);

                        if (datesList.contains(dateFormat.format(cal.getTime()))) {
                            attendeesList.add(partner.getEmail());
                        }
                    } catch (ParseException e) {
                        System.err.println("Unable to parse date: " + e.getMessage());
                    }
                }
            }

            Invitation invitation = new Invitation();

            invitation.setStartDate(countryToDateMap.get(country));
            invitation.setName(country);
            invitation.setAttendeeCount(attendeesList.size());
            List<String> attendeesListArray = new ArrayList<>(attendeesList);
            invitation.setAttendees(attendeesListArray);

            invitationsList.add(invitation);
        }
        return invitationsList;
    }

    private static long getDiffInDates(String startDateStr, String endDateStr) {
        long diff = 0;
        try {
            long diffInMilliSeconds = Math
                    .abs(dateFormat.parse(endDateStr).getTime() - dateFormat.parse(startDateStr).getTime());
            diff = TimeUnit.DAYS.convert(diffInMilliSeconds, TimeUnit.MILLISECONDS);
        } catch (ParseException ex) {
            System.out.println("Error parsing dates: " + ex.getMessage());
        }
        return diff;
    }
}
