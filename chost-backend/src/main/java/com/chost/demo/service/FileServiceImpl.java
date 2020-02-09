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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class FileServiceImpl implements FileService{
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private UserRepository userRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);
    @Value("${app.upload.path}")
    private String fileUploadingPath;

    @Override
    public File updateFile(User user,UploadFileRequest request) {
        LOGGER.info("File updating request file id : "+request.getId()+" owner id "+user.getId());
        File prev = fileRepository.findById(request.getId()).orElseThrow(()-> {
            LOGGER.error(" Error during file updating: Can`t find file with ID = "+request.getId());
           return new NoSuchElementException("Cant find file with name "+request.getName()+ " and id "+ request.getId());});
        prev.setName(request.getName());
        prev.setDescription(request.getDescription());
        prev.setPrice(new BigInteger(request.getPrice()));
        if (request.getWhiteList()!=null){
            for (String login: request.getWhiteList()) {
                prev.getWhiteList().add(userRepository.findByLogin(login).orElseThrow(()->new NoSuchElementException("Cant find user with login = "+login)));
            }
        }
        prev.setIconPath(saveFileToDir(request.getIcon()));
        for (MultipartFile f : request.getFiles()) {
            prev.getFilesNames().add(saveFileToDir(f));
        }
        fileRepository.save(prev);
        LOGGER.info("Successfully updated file with ID : "+ request.getId());
        return prev;
    }

    @Override
    public void buyFile(User user, File file) {
        file.getUsers().add(user);
        user.getFiles().add(file);
        file.getWhiteList().remove(user);
        user.getOpenToBuyFiles().remove(file);
        userRepository.save(user);
    }

    private String saveFileToDir( MultipartFile file) throws FileStorageException{
if (file == null) throw new FileStorageException("File is null!");
        LOGGER.info("Starting saving file  : "+file.getOriginalFilename());

        try {
            String newFileName = UUID.randomUUID().toString() + "." + file.getOriginalFilename();
            Path targetPath = Paths.get(fileUploadingPath).toAbsolutePath().normalize().resolve(newFileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return newFileName;
        } catch (IOException exc){
            LOGGER.error("Error during saving file :"+file.getOriginalFilename());
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
        if (uploadFileRequest.getWhiteList()!=null){
            fileEntity.setWhiteListed(true);
            for (String login: uploadFileRequest.getWhiteList()) {
                User e = userRepository.findByLogin(login).orElseThrow(() -> new NoSuchElementException("Cant find user with login = " + login));
                fileEntity.getWhiteList().add(e);
                e.getOpenToBuyFiles().add(fileEntity);
            }
        }
        fileRepository.save(fileEntity);

    }

    @Override
    public void removeFile(File file)  {
        LOGGER.info("Request to remove file with id = "+file.getId());
        file.getUsers().forEach((u)->{
            u.getFiles().remove(file);
        });
        file.getOwner().getOwnedFiles().remove(file);
        file.getWhiteList().forEach(u->{
            u.getOpenToBuyFiles().remove(file);
        });
        fileRepository.delete(file);
        LOGGER.info("File "+file.getId()+" successfully removed ");
    }

    @Override
    public List<File> getOpenFiles(User forUser) {
       return fileRepository.findAll().stream().filter(file -> {
            return !((file.isWhiteListed()) || file.getWhiteList().contains(forUser) )&& file.getOwner()!=forUser && !file.getUsers().contains(forUser);
        }).collect(Collectors.toList());
    }

}
