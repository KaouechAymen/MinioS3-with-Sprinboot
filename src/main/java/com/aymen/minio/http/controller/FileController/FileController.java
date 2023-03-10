package com.aymen.minio.http.controller.FileController;



import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;

import com.aymen.minio.http.dto.FileDto.FileDto;
import com.aymen.minio.service.MinioService.MinioService;

import java.io.IOException;
import org.springframework.http.MediaType;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import static org.springframework.web.servlet.HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE;








@Slf4j
@RestController
@RequestMapping(value = "/file")
public class FileController {

    
	@Autowired
    private MinioService minioService;

    @GetMapping
    public ResponseEntity<Object> getFiles() {
        return ResponseEntity.ok(minioService.getListObjects());
    }
    
    @PostMapping(value = "/upload")
    public ResponseEntity<Object> upload(@ModelAttribute FileDto request) {
        return ResponseEntity.ok().body(minioService.uploadFile(request));
    }
    
    @GetMapping(value = "/**")
    public ResponseEntity<Object> getFile(HttpServletRequest request) throws IOException {
        String pattern = (String) request.getAttribute(BEST_MATCHING_PATTERN_ATTRIBUTE);
        String filename = new AntPathMatcher().extractPathWithinPattern(pattern, request.getServletPath());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(IOUtils.toByteArray(minioService.getObject(filename)));
    }
    
   /** @GetMapping(path = "/download")
    public ResponseEntity<ByteArrayResource> uploadFile(@RequestParam(value = "file") String file) throws IOException {
        byte[] data = minioService.getFile(file);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + file + "\"")
                .body(resource);

    }**/

}