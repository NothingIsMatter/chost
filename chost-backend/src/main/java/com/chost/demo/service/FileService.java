package com.chost.demo.service;

import com.chost.demo.controller.exceptions.NoSuchElementException;
import com.chost.demo.model.dto.UploadFileRequest;
import com.chost.demo.model.entity.File;
import com.chost.demo.model.entity.User;

import java.util.List;

public interface FileService {
    void saveFile(UploadFileRequest request, User user);
    void removeFile(File file) throws NoSuchElementException;
    List<File> getOpenFiles(User forUser);
    File updateFile(User user,UploadFileRequest request);
}
