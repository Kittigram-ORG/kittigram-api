package org.ciscoadiz.adoption.dto;

import org.ciscoadiz.adoption.entity.ActivityLevel;
import org.ciscoadiz.adoption.entity.HousingType;

public record AdoptionRequestFormCreateRequest(
        // Sección 1: Perfil del adoptante
        Boolean hasPreviousCatExperience,
        String previousPetsHistory,
        Integer adultsInHousehold,
        Boolean hasChildren,
        String childrenAges,
        Boolean hasOtherPets,
        String otherPetsDescription,
        Integer hoursAlonePerDay,
        Boolean stableHousing,
        String housingInstabilityReason,

        // Sección 2: La vivienda
        HousingType housingType,
        Integer housingSize,
        Boolean hasOutdoorAccess,
        Boolean isRental,
        Boolean rentalPetsAllowed,
        Boolean hasWindowsWithView,
        Boolean hasVerticalSpace,
        Boolean hasHidingSpots,
        ActivityLevel householdActivityLevel,

        // Sección 3: Comportamiento felino
        String whyCatsNeedToPlay,
        Integer dailyPlayMinutes,
        String plannedEnrichment,
        String reactionToUnwantedBehavior,
        Boolean hasScratchingPost,
        Boolean willingToEnrichEnvironment,

        // Sección 4: Compromiso
        String motivationToAdopt,
        Boolean understandsLongTermCommitment,
        Boolean hasVetBudget,
        Boolean allHouseholdMembersAgree,
        Boolean anyoneHasAllergies,
        String allergiesDetail
) {}