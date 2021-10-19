package com.key.win.fileclient.controller;

import com.key.win.fileclient.service.UploadFeignClient;
import com.key.win.log.annotation.LogAnnotation;
import com.key.win.oss.model.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 作者 owen 
 * @version 创建时间：2017年11月12日 上午22:57:51
*  文件上传 同步oss db双写 目前仅实现了阿里云,七牛云
*  参考src/main/view/upload.html
*/
@RestController
public class FileController {

	@Autowired
	UploadFeignClient uploadFeignClient ;
	/**
	 * 文件上传
	 * 根据fileType选择上传方式
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@LogAnnotation(module = "file-center", recordRequestParam = false)
	@PostMapping(value = "/files-anon", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public FileInfo upload(@RequestPart("file") MultipartFile file) throws Exception {
		return uploadFeignClient.upload(file);
		 
	}

	 
}
