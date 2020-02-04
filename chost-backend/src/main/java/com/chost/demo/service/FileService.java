package com.chost.demo.service;

import com.chost.demo.model.dto.UploadFileRequest;
import com.chost.demo.model.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    void saveFile(UploadFileRequest request, User user);

}
