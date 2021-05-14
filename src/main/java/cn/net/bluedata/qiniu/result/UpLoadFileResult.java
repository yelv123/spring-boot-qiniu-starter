package cn.net.bluedata.qiniu.result;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UpLoadFileResult {
    /**
     * 文件key
     */
    private String key;

    /**
     * 文件url
     */
    private String url;
}
