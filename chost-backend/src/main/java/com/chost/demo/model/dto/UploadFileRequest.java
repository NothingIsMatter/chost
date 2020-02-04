package com.chost.demo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadFileRequest {
    private String name;
    private String description;
    private MultipartFile[] files;
    private MultipartFile icon;
}
