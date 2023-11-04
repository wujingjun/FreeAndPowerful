package com.miejiang.freeandpowerful.controller.tools;

import com.miejiang.freeandpowerful.tools.GBKToUTF8Converter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileController {

    /**
     * 转化格式 GBK 到 UTF-8
     */
    @PostMapping("/gbkToUtf8")
    public void gbkToUtf8(@RequestBody Map<String, Object> params) {
        String inputFilePath = (String) params.get("inputFilePath");
        String outputFilePath = (String) params.get("outputFilePath");
        GBKToUTF8Converter.convert(inputFilePath, outputFilePath);
    }

    /**
     * 转化格式 GBK 到 UTF-8,并进行替换
     */
    @PostMapping("/gbkToUtf8AndReplace")
    public void gbkToUtf8AndReplace(@RequestBody Map<String, Object> params) {
        String inputFilePath = (String) params.get("inputFilePath");
        String outputFilePath = (String) params.get("outputFilePath");
        GBKToUTF8Converter.convertAndReplace(inputFilePath, outputFilePath);
    }

    /**
     * 转换格式 GBK 到 UTF-8 的具体文件夹
     */
    @PostMapping("/gbkToUtf8Path")
    public void gbkToUtf8Path(@RequestBody Map<String, Object> params) {
        String folderPath = (String) params.get("folderPath");
        GBKToUTF8Converter.convertPath(folderPath);
    }
}
