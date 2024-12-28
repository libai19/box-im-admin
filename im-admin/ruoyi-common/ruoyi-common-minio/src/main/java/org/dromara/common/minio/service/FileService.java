package org.dromara.common.minio.service;


import org.dromara.common.minio.vo.UploadImageVO;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String uploadFile(MultipartFile file);

    UploadImageVO uploadImage(MultipartFile file,boolean withThumb);

}
