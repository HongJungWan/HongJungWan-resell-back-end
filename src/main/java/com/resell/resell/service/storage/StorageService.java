package com.resell.resell.service.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    
    String upload(MultipartFile file, String destLocation);

}
