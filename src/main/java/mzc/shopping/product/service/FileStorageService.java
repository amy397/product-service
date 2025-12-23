package mzc.shopping.product.service;

import lombok.RequiredArgsConstructor;
import mzc.shopping.product.config.FileConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final FileConfig fileConfig;
    private Path uploadPath;

    @PostConstruct
    public void init() {
        uploadPath = Paths.get(fileConfig.getUploadDir()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("업로드 디렉토리를 생성할 수 없습니다.", e);
        }
    }

    public String storeFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String storedFilename = UUID.randomUUID().toString() + extension;

        try {
            Path targetLocation = uploadPath.resolve(storedFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileConfig.getBaseUrl() + "/" + storedFilename;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장에 실패했습니다: " + originalFilename, e);
        }
    }
}
