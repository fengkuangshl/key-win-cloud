package com.key.win;

import com.key.win.common.port.PortApplicationEnvironmentPreparedEventListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author 作者 owen 
 * @version 创建时间：2017年11月12日 上午22:57:51
 * 文件中心
*/
@EnableDiscoveryClient
@SpringBootApplication
public class FileCenterApp {

	public static void main(String[] args) {
		// 固定端口
		// SpringApplication.run(FileCenterApp.class, args);

		// 随机端口启动
		SpringApplication app = new SpringApplication(FileCenterApp.class);
		app.addListeners(new PortApplicationEnvironmentPreparedEventListener());
		app.run(args);

	}

}