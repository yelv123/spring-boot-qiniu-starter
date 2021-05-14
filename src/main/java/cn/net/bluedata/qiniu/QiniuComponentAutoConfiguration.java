package cn.net.bluedata.qiniu;

import cn.net.bluedata.qiniu.component.IQiniuComponent;
import cn.net.bluedata.qiniu.component.impl.QiniuComponentImpl;
import cn.net.bluedata.qiniu.config.QiniuConfig;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(QiniuConfig.class)
@ConditionalOnClass(IQiniuComponent.class)
public class QiniuComponentAutoConfiguration {

    @Autowired
    private QiniuConfig qiniuConfig;

    @Bean
    @ConditionalOnMissingBean(QiniuComponentImpl.class)
    public IQiniuComponent qiniuComponent() {
        return new QiniuComponentImpl();
    }

    @Bean
    public UploadManager uploadManager() {
        com.qiniu.storage.Configuration configuration = new com.qiniu.storage.Configuration();
        return new UploadManager(configuration);
    }

    // 认证信息实例
    @Bean
    public Auth auth() {
        return Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());
    }

    // 构建七牛空间管理实例
    @Bean
    public BucketManager bucketManager() {
        com.qiniu.storage.Configuration configuration = new com.qiniu.storage.Configuration();
        return new BucketManager(auth(),configuration);
    }
}
