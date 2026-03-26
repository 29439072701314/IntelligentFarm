package com.example.intelligentfarmcore.controller;

import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.service.interfaces.IUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

@CrossOrigin
@RestController // 接口方法返回对象 ，默认返回JSON格式
@RequestMapping("/upload")
public class UploadController {
    @Autowired
    IUploadService uploadService;

    @PostMapping("/avatar")
    public ResponseMessage<String> uploadAvatar(@RequestParam MultipartFile file) throws IOException {
        return uploadService.uploadAvatar(file);
    }

    @GetMapping("/avatar/{filename}")
    public ResponseEntity<Resource> getAvatar(@PathVariable String filename)
            throws MalformedURLException, FileNotFoundException {
        return uploadService.getAvatar(filename);
    }
}
