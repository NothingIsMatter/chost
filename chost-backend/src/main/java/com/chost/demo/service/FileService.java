package com.chost.demo.service;

import com.chost.demo.controller.exceptions.NoSuchElementException;
import com.chost.demo.model.dto.UploadFileRequest;
import com.chost.demo.model.entity.File;
import com.chost.demo.model.entity.User;

public interface FileService {
    void saveFile(UploadFileRequest request, User user);
    void removeFile(File file) throws NoSuchElementException;

    File updateFile(User user,UploadFileRequest request);
}
