package com.chost.demo.controller;

import com.chost.demo.controller.exceptions.BalanceException;
import com.chost.demo.controller.exceptions.NoSuchElementException;
import com.chost.demo.model.dto.UploadFileRequest;
import com.chost.demo.model.dto.jsonview.View;
import com.chost.demo.model.entity.File;
import com.chost.demo.model.entity.User;
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

import java.util.List;

@RestController
@RequestMapping("/file/")
@AllArgsConstructor
public class FileController {
    private final FileService fileService;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;


    @PostMapping("upload")
    @Secured("ROLE_USER")
    public ResponseEntity<?> uploadFile(@AuthenticationPrincipal UserWrapper user, @RequestParam("name") String name,@RequestParam("title") MultipartFile img,@RequestParam("files") MultipartFile[] files,@RequestParam("desc") String description,@RequestParam(value = "price",required = false) String price,@RequestParam(value = "whitelist",required = false) String[] whiteListed){
            UploadFileRequest uploadFileRequest = new UploadFileRequest(0, name, description, price, files, img,whiteListed);
        fileService.saveFile(uploadFileRequest,userRepository.findByLogin(user.getLogin()).get());
        return  ResponseEntity.ok().body("File successfully uploaded!");
    }

    @PostMapping(value = "remove")
    @JsonView({View.FULLINFORMATION.class})
    @PreAuthorize("@authComponentImpl.isOwnerOf(#id, #user)")
    public ResponseEntity<?> removeFile(@AuthenticationPrincipal UserWrapper user, @RequestBody String id) throws NoSuchElementException {
        File file = fileRepository.findById(Integer.parseInt(id)).orElseThrow(()-> new NoSuchElementException("No such file with id "+id));
        fileService.removeFile(file);
        return ResponseEntity.ok().body("File "+file.getName()+" deleted!");
    }
    @PostMapping(value = "update")
    @JsonView({View.FULLINFORMATION.class})
    @PreAuthorize("@authComponentImpl.isOwnerOf(#id, #user)")
    public ResponseEntity<?> update(@AuthenticationPrincipal UserWrapper user, @RequestParam("name") String name,@RequestParam("title") MultipartFile img,@RequestParam("files") MultipartFile[] files,@RequestParam("desc") String description,@RequestParam("price") String price,@RequestParam("id") int id,@RequestParam(value = "whitelist",required = false)String[] whiteList) throws NoSuchElementException {
        UploadFileRequest uploadFileRequest = new UploadFileRequest(id,name,description,price,files,img,whiteList);
        File file = fileService.updateFile(userRepository.findById(user.getId()).get(), uploadFileRequest);
        return new ResponseEntity<File>(file,HttpStatus.OK);
    }

    @GetMapping("/marketplace")
    @Secured("ROLE_USER")
    @JsonView(View.MARKETPLACE.class)
    public List<File> marketplace(@AuthenticationPrincipal UserWrapper userWrapper){
        return fileService.getOpenFiles(userRepository.findById(userWrapper.getId()).get());
    }
    @PostMapping("/buy")
    @PreAuthorize("@authComponentImpl.canBuyFile(#fileId,#user)")
    public ResponseEntity<?> buyFile(@AuthenticationPrincipal UserWrapper user, @RequestBody String fileId) throws BalanceException
    {
        fileService.buyFile(userRepository.findById(user.getId()).get(),fileRepository.findById(Integer.parseInt(fileId)).orElseThrow(()->new NoSuchElementException("Cant file with id: "+ fileId)));
        return ResponseEntity.ok().body("File bought!");
    }
}
