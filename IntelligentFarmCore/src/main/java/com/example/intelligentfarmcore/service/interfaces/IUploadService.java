package com.example.intelligentfarmcore.service.interfaces;

import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

public interface IUploadService {
    ResponseMessage uploadAvatar(MultipartFile file) throws IOException;

    ResponseEntity<Resource> getAvatar(String filename) throws MalformedURLException, FileNotFoundException;
}
