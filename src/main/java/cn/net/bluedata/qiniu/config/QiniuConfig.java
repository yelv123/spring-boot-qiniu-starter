package cn.net.bluedata.qiniu.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "bluedata.qiniu")
@Data
@ToString
public class QiniuConfig {
    /**
     * 七牛云的 accesskey
     */
    private String accessKey = "access-key";

    /**
     * 七牛云的secretKey
     */
    private String secretKey = "secret-key";

    /**
     * 存储空间"bucketName
     */
    private String bucketName = "bucket-name";

    /**
     * 一般设置为cdn
     */
    private String cdnPrefix = "cdn";

    /**
     * 空间的域名
     */
    private String host = "host";
}
