package com.chost.demo.controller;

import com.chost.demo.controller.exceptions.NoSuchElementException;
import com.chost.demo.model.dto.UploadFileRequest;
import com.chost.demo.model.dto.jsonview.View;
import com.chost.demo.model.entity.File;
import com.chost.demo.model.repository.FileRepository;
import com.chost.demo.model.repository.UserRepository;
import com.chost.demo.model.wrappers.UserWrapper;
import com.chost.demo.service.FileService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file/")
@AllArgsConstructor
public class FileController {
    private final FileService fileService;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;


    @PostMapping("upload")
    @Secured("ROLE_USER")
    public ResponseEntity<?> uploadFile(@AuthenticationPrincipal UserWrapper user, @RequestParam("name") String name,@RequestParam("title") MultipartFile img,@RequestParam("files") MultipartFile[] files,@RequestParam("desc") String description){
        UploadFileRequest uploadFileRequest = new UploadFileRequest(name,description,files,img);
        fileService.saveFile(uploadFileRequest,userRepository.findByLogin(user.getLogin()).get());
        return  ResponseEntity.ok().body("File successfully uploaded!");
    }

    @PostMapping(value = "remove")
    @JsonView({View.FULLINFORMATION.class})
    //@PreAuthorize("#user.login.equals(fileRepository.getOne(#file.id).owner)")
    public ResponseEntity<?> removeFile(@AuthenticationPrincipal UserWrapper user, @RequestBody String id) throws NoSuchElementException {
        File file = fileRepository.findById(Integer.parseInt(id)).orElseThrow(()-> new NoSuchElementException("No such file with id "+id));
        fileService.removeFile(file);
        return ResponseEntity.ok().body("File "+file.getName()+" deleted!");
    }

}
