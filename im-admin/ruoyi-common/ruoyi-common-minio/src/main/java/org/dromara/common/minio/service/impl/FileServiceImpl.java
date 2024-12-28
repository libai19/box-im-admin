package org.dromara.common.minio.service.impl;


import org.dromara.common.minio.client.MinioService;
import org.dromara.common.minio.enums.FileType;
import org.dromara.common.minio.properties.MinioProperties;
import org.dromara.common.minio.service.FileService;
import org.dromara.common.minio.util.FileUtil;
import org.dromara.common.minio.util.ImageUtil;
import org.dromara.common.minio.vo.UploadImageVO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dromara.common.core.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件上传服务
 *
 * @author  Blue
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final MinioService minioSerivce;

    private final MinioProperties minioProps;

    @PostConstruct
    public void init() {
        if (!minioSerivce.bucketExists(minioProps.getBucketName())) {
            // 创建bucket
            minioSerivce.makeBucket(minioProps.getBucketName());
            // 公开bucket
            minioSerivce.setBucketPublic(minioProps.getBucketName());
        }
    }

    @Override
    public String uploadFile(MultipartFile file) {
        // 上传
        String fileName = minioSerivce.upload(minioProps.getBucketName(), minioProps.getFilePath(), file);
        if (StringUtils.isEmpty(fileName)) {
            throw new ServiceException("文件上传失败");
        }
        String url = generUrl(FileType.FILE, fileName);
        log.info("文件文件成功,url:{}", url);
        return url;
    }

    @Override
    public UploadImageVO uploadImage(MultipartFile file,boolean withThumb) {
        try {
            // 上传原图
            UploadImageVO vo = new UploadImageVO();
            // 图片格式校验
            if (!FileUtil.isImage(file.getOriginalFilename())) {
                throw new ServiceException("图片格式不合法");
            }
            String fileName = minioSerivce.upload(minioProps.getBucketName(), minioProps.getImagePath(), file);
            if (StringUtils.isEmpty(fileName)) {
                throw new ServiceException( "图片上传失败");
            }
            vo.setOriginUrl(generUrl(FileType.IMAGE, fileName));
            // 大于30K的文件需上传缩略图
            if (file.getSize() > 30 * 1024 && withThumb) {
                byte[] imageByte = ImageUtil.compressForScale(file.getBytes(), 30);
                fileName = minioSerivce.upload(minioProps.getBucketName(), minioProps.getImagePath(),
                    file.getOriginalFilename(), imageByte, file.getContentType());
                if (StringUtils.isEmpty(fileName)) {
                    throw new ServiceException("图片上传失败");
                }
            }
            vo.setThumbUrl(generUrl(FileType.IMAGE, fileName));
            log.info("文件图片成功，url:{}",  vo.getOriginUrl());
            return vo;
        } catch (IOException e) {
            log.error("上传图片失败，{}", e.getMessage(), e);
            throw new ServiceException( "图片上传失败");
        }
    }


    private String generUrl(FileType fileTypeEnum, String fileName) {
        String url = minioProps.getDomain() + "/" + minioProps.getBucketName();
        switch (fileTypeEnum) {
            case FILE:
                url += "/" + minioProps.getFilePath() + "/";
                break;
            case IMAGE:
                url += "/" + minioProps.getImagePath() + "/";
                break;
            case VIDEO:
                url += "/" + minioProps.getVideoPath() + "/";
                break;
            default:
                break;
        }
        url += fileName;
        return url;
    }

}
