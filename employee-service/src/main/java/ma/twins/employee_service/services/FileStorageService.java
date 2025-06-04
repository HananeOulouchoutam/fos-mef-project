package ma.twins.employee_service.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

//@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String storeFile(MultipartFile file) throws IOException {
        // Nettoyer le nom du fichier
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());

        // Générer un nom unique pour éviter les collisions
        String fileName = UUID.randomUUID().toString() + "_" + originalFilename;

        // Créer le chemin complet vers le dossier de stockage
        Path uploadPath = Paths.get(uploadDir);

        // Créer le dossier s'il n'existe pas
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Copier le fichier dans le dossier
        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        // Retourner le nom du fichier stocké (pour sauvegarder en base de données par ex.)
        return fileName;
    }

    // Optionnel : méthode pour récupérer un Path à partir du nom de fichier
    public Path loadFile(String fileName) {
        return Paths.get(uploadDir).resolve(fileName).normalize();
    }
}
