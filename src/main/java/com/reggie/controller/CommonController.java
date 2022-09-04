package com.reggie.controller;


import com.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;


/**
 * 文件上上传和下载的管理
 */


@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String BasePath;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        log.info(file.toString());

//        文件原始名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
//        使用UUID重新生成文件名，防止文件名重复造成文件覆盖
        String fileName = UUID.randomUUID().toString() + suffix;

//        创建一个目录对象
        File dir = new File(BasePath);
        if (!dir.exists()) {
//            目录不存在就创建
            dir.mkdirs();
        }

        try {
//            将临时文件存到指定位置
            file.transferTo(new File(BasePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    /**
     * 文件下载
     *
     * @param response
     * @param name
     */

    @GetMapping("/download")
    public void download(HttpServletResponse response, String name) {
        try {

//        输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(BasePath + name));
//        输出流，通过输出流将文件写回浏览器，显示在浏览器上
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
//            关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
