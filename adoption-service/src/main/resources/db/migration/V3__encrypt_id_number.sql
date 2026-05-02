-- C-1 LOPDGDD: id_number now stores AES-256-GCM ciphertext.
-- Format: Base64(IV[12 bytes] || ciphertext || GCM_tag[16 bytes])
-- Key is injected via KITTIES_ID_NUMBER_KEY env var; never stored in DB.
COMMENT ON COLUMN adoption.adoption_forms.id_number
    IS 'AES-256-GCM encrypted. Base64(IV[12]||ciphertext||GCM_tag[16]). Key via KITTIES_ID_NUMBER_KEY.';
