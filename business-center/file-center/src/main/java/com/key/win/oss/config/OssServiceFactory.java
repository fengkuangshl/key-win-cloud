package com.key.win.oss.config;

import com.key.win.oss.service.FileService;
import com.key.win.oss.model.FileType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

 
/**
 * @author 作者 owen 
 * @version 创建时间：2017年11月12日 上午22:57:51
 * FileService工厂<br>
 * 将各个实现类放入map
*/
@Configuration
public class OssServiceFactory {

	private Map<FileType, FileService> map = new HashMap<>();

 
	@Autowired
	private FileService aliyunOssServiceImpl;
	
	@Autowired
	private FileService qiniuOssServiceImpl;
	@Autowired
	private FileService fastDfsOssServiceImpl;
	
	@Autowired
	private FileService localOssServiceImpl;

	@PostConstruct
	public void init() {
		map.put(FileType.ALIYUN,  aliyunOssServiceImpl);
		map.put(FileType.QINIU ,  qiniuOssServiceImpl);
		map.put(FileType.LOCAL ,  localOssServiceImpl);
		map.put(FileType.FASTDFS ,  fastDfsOssServiceImpl);
	}

	public FileService getFileService(String fileType) {
	   if (StringUtils.isBlank(fileType)) {
			return localOssServiceImpl;
		}

		return map.get(FileType.valueOf(fileType));
	}
}
