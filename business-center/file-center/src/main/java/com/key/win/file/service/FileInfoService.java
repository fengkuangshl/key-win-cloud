package com.key.win.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.key.win.common.web.PageRequest;
import com.key.win.common.web.PageResult;
import com.key.win.file.model.FileInfo;
import org.springframework.web.multipart.MultipartFile;

public interface FileInfoService extends IService<FileInfo> {

    FileInfo upload(MultipartFile file, String filePath, String bizType) throws Exception;

    void delete(FileInfo fileInfo);

    FileInfo getById(String id);

    FileInfo getFileInfoByMd5(String md5);

    PageResult<FileInfo> findFileInfoByPaged(PageRequest<FileInfo> t);





}
