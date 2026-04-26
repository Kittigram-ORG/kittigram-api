package es.kitti.storage.service;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import es.kitti.storage.exception.InvalidFileException;
import es.kitti.storage.provider.StorageProvider;

import java.util.Set;
import java.util.UUID;

@ApplicationScoped
public class StorageService {

    private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/png", "image/webp");
    private static final long MAX_SIZE = 5 * 1024 * 1024;

    private static final byte[] JPEG_MAGIC = {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};
    private static final byte[] PNG_MAGIC  = {(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};
    private static final byte[] WEBP_RIFF  = {0x52, 0x49, 0x46, 0x46};
    private static final byte[] WEBP_MARKER = {0x57, 0x45, 0x42, 0x50};

    @Inject
    StorageProvider storageProvider;

    public Uni<String> upload(byte[] data, String contentType, String originalFilename) {
        if (!ALLOWED_TYPES.contains(contentType)) {
            return Uni.createFrom().failure(
                    new InvalidFileException("File type not allowed. Only JPG, PNG and WebP are accepted")
            );
        }

        if (data.length > MAX_SIZE) {
            return Uni.createFrom().failure(
                    new InvalidFileException("File size exceeds the 5MB limit")
            );
        }

        if (!hasValidMagicBytes(data, contentType)) {
            return Uni.createFrom().failure(
                    new InvalidFileException("File content does not match the declared type")
            );
        }

        String extension = switch (contentType) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> throw new InvalidFileException("Unsupported content type");
        };
        String key = UUID.randomUUID() + extension;

        return storageProvider.upload(key, data, contentType);
    }

    private boolean hasValidMagicBytes(byte[] data, String contentType) {
        return switch (contentType) {
            case "image/jpeg" -> validateJpegMagic(data);
            case "image/png" -> validatePngMagic(data);
            case "image/webp" -> validateWebpMagic(data);
            default -> false;
        };
    }

    private boolean validateJpegMagic(byte[] data) {
        if (data.length < JPEG_MAGIC.length) return false;
        for (int i = 0; i < JPEG_MAGIC.length; i++) {
            if (data[i] != JPEG_MAGIC[i]) return false;
        }
        return true;
    }

    private boolean validatePngMagic(byte[] data) {
        if (data.length < PNG_MAGIC.length) return false;
        for (int i = 0; i < PNG_MAGIC.length; i++) {
            if (data[i] != PNG_MAGIC[i]) return false;
        }
        return true;
    }

    private boolean validateWebpMagic(byte[] data) {
        if (data.length < 12) return false;
        for (int i = 0; i < WEBP_RIFF.length; i++) {
            if (data[i] != WEBP_RIFF[i]) return false;
        }
        for (int i = 0; i < WEBP_MARKER.length; i++) {
            if (data[8 + i] != WEBP_MARKER[i]) return false;
        }
        return true;
    }

    public Uni<Void> delete(String key) {
        return storageProvider.delete(key);
    }
}