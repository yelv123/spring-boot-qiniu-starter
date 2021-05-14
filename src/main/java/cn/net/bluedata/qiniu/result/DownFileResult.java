package cn.net.bluedata.qiniu.result;

import lombok.Data;
import lombok.ToString;

import java.io.InputStream;

@Data
@ToString
public class DownFileResult{

    /**
     * 文件字节下载
     */
    private byte[] data;


    /**
     *文件流
     */
    private InputStream inputStream;


}
