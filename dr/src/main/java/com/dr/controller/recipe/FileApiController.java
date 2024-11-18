package com.dr.controller.recipe;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:8888") // CORS 허용
@RestController
@RequestMapping("/tui-editor") // 경로 설정
public class FileApiController {

    // 이미지가 저장될 디렉토리 경로 설정
    private final String uploadDir = Paths.get("C:", "DR", "dr", "src", "main", "resources", "static", "image", "photo").toString();
    // 이미지 업로드 처리 후 JSON 응답 보내기
    @PostMapping("/image-upload")
    public ResponseEntity<Map<String, Object>> uploadEditorImage(@RequestParam("image") final MultipartFile image) {
        if (image.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "파일이 비어 있습니다."));
        }
        String orgFilename = image.getOriginalFilename();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String extension = orgFilename.substring(orgFilename.lastIndexOf(".") + 1);
        String saveFilename = uuid + "." + extension;
        String fileFullPath = Paths.get(uploadDir, saveFilename).toString();
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            File uploadFile = new File(fileFullPath);
            image.transferTo(uploadFile);
            // 응답에 이미지 정보 포함
            Map<String, Object> response = new HashMap<>();
            response.put("photoOriginal", orgFilename);
            response.put("photoLocal", "/image/photo/" + saveFilename);
            response.put("photoSize", image.getSize());

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "파일 저장 실패: " + e.getMessage()));
        }
    }

    // 이미지 출력 처리
    @GetMapping(value = "/image-print", produces = {MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> printEditorImage(@RequestParam final String filename) {
        String fileFullPath = Paths.get(uploadDir, filename).toString();
        File uploadedFile = new File(fileFullPath);

        if (!uploadedFile.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        try {
            byte[] imageBytes = Files.readAllBytes(uploadedFile.toPath());
            String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
            MediaType mediaType = switch (extension) {
                case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
                case "png" -> MediaType.IMAGE_PNG;
                case "gif" -> MediaType.IMAGE_GIF;
                default -> MediaType.APPLICATION_OCTET_STREAM;
            };

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(imageBytes);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
