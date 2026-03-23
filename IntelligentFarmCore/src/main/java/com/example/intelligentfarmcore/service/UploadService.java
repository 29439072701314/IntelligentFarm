package com.example.intelligentfarmcore.service;

import com.example.intelligentfarmcore.pojo.model.ResponseMessage;
import com.example.intelligentfarmcore.service.interfaces.IUploadService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UploadService implements IUploadService {
    @Override
    public ResponseMessage<Map<String, String>> uploadAvatar(MultipartFile file) throws IOException {
        // 文件是否为空
        if (file.isEmpty()) {
            return ResponseMessage.error("文件为空");
        }
        // 获取文件名字（上传文件的原始名字）
        String fileName = file.getOriginalFilename();
        // 获取文件的后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 生成新的文件名（保存在服务器里使用的文件名）
        fileName = UUID.randomUUID().toString() + suffix;
        // 构造文件保存路径
        String savepath = ResourceUtils.getURL("upload/avatar").getPath();
        savepath = savepath + File.separator + fileName;
        // 保存文件
        file.transferTo(new File(savepath));
        Map<String, String> result = new HashMap<>();
        result.put("imageUrl", "/upload/avatar/" + fileName);
        return ResponseMessage.success(result, "上传成功");
    }

    @Override
    public ResponseEntity<Resource> getAvatar(String filename) throws MalformedURLException, FileNotFoundException {
        // 获取头像文件路径
        String filePath = ResourceUtils.getURL("upload/avatar").getPath() + File.separator + filename;
        File file = new File(filePath);

        // 检查文件是否存在
        if (!file.exists()) {
            throw new FileNotFoundException("头像文件不存在");
        }

        // 创建Resource对象
        Resource resource = new UrlResource(file.toURI());

        // 返回响应实体
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
