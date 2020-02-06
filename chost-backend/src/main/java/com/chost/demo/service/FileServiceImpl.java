package com.chost.demo.service;

import com.chost.demo.controller.exceptions.FileStorageException;
import com.chost.demo.controller.exceptions.NoSuchElementException;
import com.chost.demo.model.dto.UploadFileRequest;
import com.chost.demo.model.entity.File;
import com.chost.demo.model.entity.User;
import com.chost.demo.model.repository.FileRepository;
import com.chost.demo.model.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
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

    @Value("${app.upload.path}")
    private String fileUploadingPath;

    @Override
    public File updateFile(User user,UploadFileRequest request) {

        File prev = fileRepository.findById(request.getId()).orElseThrow(()-> new NoSuchElementException("Cant find file with name "+request.getName()+ " and id "+ request.getId()));
        prev.setName(request.getName());
        prev.setDescription(request.getDescription());
        prev.setPrice(new BigInteger(request.getPrice()));
        prev.setIconPath(saveFileToDir(request.getIcon()));
        for (MultipartFile f : request.getFiles()) {
            prev.getFilesNames().add(saveFileToDir(f));
        }
        fileRepository.save(prev);
        return prev;
    }

    private String saveFileToDir(MultipartFile file) throws FileStorageException{

        try {
            String newFileName = UUID.randomUUID().toString() + "." + file.getOriginalFilename();
            Path targetPath = Paths.get(fileUploadingPath).toAbsolutePath().normalize().resolve(newFileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return newFileName;
        } catch (IOException exc){
            throw new FileStorageException("Cant save file! File name "+file.getOriginalFilename());
        }
    }
    @Override
    public void saveFile(UploadFileRequest uploadFileRequest, User user) {
        File fileEntity = new File();
        fileEntity.setDescription(uploadFileRequest.getDescription());
        fileEntity.setName(uploadFileRequest.getName());
        fileEntity.getUsers().add(user);
        MultipartFile icon = uploadFileRequest.getIcon();
        fileEntity.setIconPath(saveFileToDir(icon));
        for (MultipartFile  file : uploadFileRequest.getFiles()) {
           fileEntity.getFilesNames().add(saveFileToDir(file));
        }
        user.getFiles().add(fileEntity);
        fileEntity.setOwner(user);
        fileRepository.save(fileEntity);

    }

    @Override
    public void removeFile(File file)  {
        file.getUsers().forEach((u)->{
            u.getFiles().remove(file);
        });
        file.getOwner().getOwnedFiles().remove(file);
            fileRepository.delete(file);
    }
}
