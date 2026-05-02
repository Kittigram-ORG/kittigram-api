-- C-2 LOPDGDD: explicit consent for processing health-category data (Art. 9 RGPD).
-- Existing rows default to false so they are flagged as lacking consent until
-- re-submitted; this is intentional — they pre-date the compliance requirement.
ALTER TABLE adoption.adoption_forms
    ADD COLUMN consent_health_data BOOLEAN NOT NULL DEFAULT false;

COMMENT ON COLUMN adoption.adoption_forms.consent_health_data
    IS 'Explicit consent (Art. 9 RGPD) for processing health-category data (allergies, etc.). Must be true before the form is accepted.';
