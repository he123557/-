package com.hj.reggie.commons.controller;

import com.hj.reggie.commons.bean.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 进行文件上传和下载
 *
 * @create 2023-03-10 11:53
 */
@Controller
public class CommonController {
    @Value("${reggie.path}")
    private String bashPath;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping("/common/upload")
    @ResponseBody
    public Object upload(MultipartFile file) {
        //获取原始文件名的后缀，拼接在新生成的文件名后面
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID生成文件名，防止文件名重复造成文件覆盖
        String newFileName = UUID.randomUUID().toString() + suffix;
        File baseFile = new File(bashPath);
        if (!baseFile.exists()) {
            //目录不存在就创建
            baseFile.mkdirs();
        }
        try {
            file.transferTo(new File(bashPath + newFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(newFileName);
    }

    /**
     * 文件下载
     *
     * @param name
     * @param response
     */

    @GetMapping("/common/download")
    public void download(String name, HttpServletResponse response) {
        System.out.println(bashPath+name);
        try {
            FileInputStream fileInputStream = new FileInputStream(bashPath+name);
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            fileInputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
