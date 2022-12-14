package com.key.win.report;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ReportCenteApplication {

    public static void main(String[] args) {
//		固定端口启动
        SpringApplication.run(ReportCenteApplication.class, args);
    }



    /**
     * 进行注册Servlet
     * 配置 UReport2 需要使用到的servlet
     */
//    @Bean
//    public ServletRegistrationBean buildUReportServlet() {
//
//        /**
//         * @param  servlet
//         * @param  urlMappings 值为“/ureport/*”的 urlMappings 是一定不能变的，否则系统将无法运行。
//         */
//        return new ServletRegistrationBean(new UReportServlet(), "/ureport/*");
//    }

}
