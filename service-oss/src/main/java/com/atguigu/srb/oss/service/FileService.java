package com.atguigu.srb.oss.service;

import java.io.InputStream;

/**
 * @Author wangjun
 * @Date 2021/9/14 16:32
 * @Description
 */
public interface FileService {

    String upload(InputStream inputStream, String module, String fileName);

    void removeFile(String url);
}
