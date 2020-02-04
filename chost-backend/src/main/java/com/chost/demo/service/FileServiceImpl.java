package com.chost.demo.service;

import com.chost.demo.controller.exceptions.FileStorageException;
import com.chost.demo.model.dto.UploadFileRequest;
import com.chost.demo.model.entity.File;
import com.chost.demo.model.entity.User;
import com.chost.demo.model.repository.FileRepository;
import com.chost.demo.model.repository.UserRepository;
import com.chost.demo.model.wrappers.UserWrapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class FileServiceImpl implements FileService{
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private UserRepository userRepository;
    @Value("${app.upload.path}")
    private String fileUploadingPath;
    @Override
    public void saveFile(UploadFileRequest uploadFileRequest, User user) {
        File fileEntity = new File();
        fileEntity.setDescription(uploadFileRequest.getDescription());
        fileEntity.setName(uploadFileRequest.getName());
        fileEntity.getUsers().add(user);
        MultipartFile icon = uploadFileRequest.getIcon();
        String newIconName = UUID.randomUUID().toString() + "." + icon.getOriginalFilename();
        Path iconTargetPath = Paths.get(fileUploadingPath).toAbsolutePath().normalize().resolve(newIconName);
        try {
            Files.copy(icon.getInputStream(), iconTargetPath, StandardCopyOption.REPLACE_EXISTING);
            fileEntity.setIconPath(newIconName);
        } catch (IOException e) {
            throw new FileStorageException("Cant save file! File name "+icon.getName());
        }
        for (MultipartFile  file : uploadFileRequest.getFiles()) {
            try {
                String newFileName = UUID.randomUUID().toString() + "." + file.getOriginalFilename();
                Path targetPath = Paths.get(fileUploadingPath).toAbsolutePath().normalize().resolve(newFileName);
                Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                fileEntity.getFilesNames().add(newFileName);
            } catch (IOException ex) {
                throw new FileStorageException("Could not save file! File name: " + file.getName());
            }
        }
        user.getFiles().add(fileEntity);
        userRepository.save(user);
        fileRepository.save(fileEntity);
    }
}
