package cn.net.bluedata.qiniu.component.impl;

import cn.net.bluedata.qiniu.component.IQiniuComponent;
import cn.net.bluedata.qiniu.config.QiniuConfig;
import cn.net.bluedata.qiniu.result.DownFileResult;
import cn.net.bluedata.qiniu.result.QiniuResult;
import cn.net.bluedata.qiniu.result.UpLoadFileResult;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class QiniuComponentImpl implements IQiniuComponent {

    @Autowired
    private UploadManager uploadManager;

    @Autowired
    private BucketManager bucketManager;

    @Autowired
    private Auth auth;

    @Autowired
    private QiniuConfig qiniuConfig;

    /**
     * 上传文件
     *
     * @param file    填写上传文件File类型
     * @param key     添加上传的key值
     * @param existed 是否已经存在
     */
    @Override
    public QiniuResult<UpLoadFileResult> uploadFile(File file, String key, boolean existed) {
        try {
            Response response;
            QiniuResult<UpLoadFileResult> upLoadFileResultQiniuResult = new QiniuResult<>();
            QiniuResult<String> uploadToken = getUploadToken();
            if (!uploadToken.isSuccess()) {
                return upLoadFileResultQiniuResult.error(uploadToken.getMessage());
            }
            // 覆盖上传
            if (existed) {
                response = this.uploadManager.put(file, key, uploadToken.getData());
            } else {
                response = this.uploadManager.put(file, key, uploadToken.getData());
                int retry = 0;
                while (response.needRetry() && retry < 3) {
                    response = this.uploadManager.put(file, key, uploadToken.getData());
                    retry++;
                }
            }
            return null;
        } catch (Exception exception) {
            exception.printStackTrace();
            log.error("七牛上传文件错误,错误消息:{}", exception.getMessage());
            return null;
        }
    }

    /**
     * 文件删除 （文件地址）
     *
     * @param filePath 填写上传文件的位置
     * @param key      添加上传的key值
     * @param existed  是否已经存在
     * @return
     */
    @Override
    public QiniuResult<UpLoadFileResult> uploadFile(String filePath, String key, boolean existed) {
        QiniuResult<UpLoadFileResult> upLoadFileResultQiniuResult = new QiniuResult<>();
        try {
            Response response;
            QiniuResult<String> uploadToken = getUploadToken();
            if (!uploadToken.isSuccess()) {
                return upLoadFileResultQiniuResult.error(uploadToken.getMessage());
            }
            // 覆盖上传
            if (existed) {
                response = this.uploadManager.put(filePath, key, uploadToken.getData());
            } else {
                response = this.uploadManager.put(filePath, key, uploadToken.getData());
                int retry = 0;
                while (response.needRetry() && retry < 3) {
                    response = this.uploadManager.put(filePath, key, uploadToken.getData());
                    retry++;
                }
            }
            if(response.isOK()){
                UpLoadFileResult upLoadFileResult = new UpLoadFileResult();
                JsonParser parser = new JsonParser();
                JsonObject asJsonObject = parser.parse(response.bodyString()).getAsJsonObject();
                upLoadFileResult.setKey(asJsonObject.get("key").getAsString());
                upLoadFileResult.setUrl(qiniuConfig.getHost() + "/" + asJsonObject.get("key").getAsString());
                return upLoadFileResultQiniuResult.success(upLoadFileResult);
            }else{
                return upLoadFileResultQiniuResult.error(response.error);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            log.error("七牛上传文件错误,错误消息:{}", exception.getMessage());
            return upLoadFileResultQiniuResult.error(exception.getMessage());
        }

    }

    /**
     * 文件删除 （字节数据）
     *
     * @param data    填写上传文件的位置
     * @param key     添加上传的key值
     * @param existed 是否已经存在
     * @return
     */
    @Override
    public QiniuResult<UpLoadFileResult> uploadFile(byte[] data, String key, boolean existed) {
        QiniuResult<UpLoadFileResult> upLoadFileResultQiniuResult = new QiniuResult<>();
        try {
            Response response = null;
            QiniuResult<String> uploadToken = getUploadToken();
            if (!uploadToken.isSuccess()) {
                return upLoadFileResultQiniuResult.error(uploadToken.getMessage());
            }
            // 覆盖上传
            if (existed) {
                response = this.uploadManager.put(data, key, uploadToken.getData());
            } else {
                response = this.uploadManager.put(data, key, uploadToken.getData());
                int retry = 0;
                while (response.needRetry() && retry < 3) {
                    response = this.uploadManager.put(data, key, uploadToken.getData());
                    retry++;
                }
            }
            if(response.isOK()){
                UpLoadFileResult upLoadFileResult = new UpLoadFileResult();
                JsonParser parser = new JsonParser();
                JsonObject asJsonObject = parser.parse(response.bodyString()).getAsJsonObject();
                upLoadFileResult.setKey(asJsonObject.get("key").getAsString());
                upLoadFileResult.setUrl(qiniuConfig.getHost() + "/" + asJsonObject.get("key").getAsString());
                return upLoadFileResultQiniuResult.success(upLoadFileResult);
            }else{
                return upLoadFileResultQiniuResult.error(response.error);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            log.error("七牛上传文件错误,错误消息:{}", exception.getMessage());
            return upLoadFileResultQiniuResult.error(exception.getMessage());
        }
    }

    /**
     * 删除
     *
     * @param key 添加上传的key值
     */
    @Override
    public QiniuResult<Boolean> deleteFile(String key) {
         QiniuResult<Boolean> booleanQiniuResult = new QiniuResult<>();
        try {
             Response response = bucketManager.delete(qiniuConfig.getBucketName(), key);
            if(response.isOK()){
                return booleanQiniuResult.success(true);
            }else{
                return booleanQiniuResult.error(response.error);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            log.error("七牛删除文件错误,错误消息:{}", exception.getMessage());
            return booleanQiniuResult.error(exception.getMessage());
        }
    }


    /**
     * 获取上传凭证，普通上传
     */
    @Override
    public QiniuResult<String> getUploadToken() {
        QiniuResult<String> stringQiniuResult = new QiniuResult<>();
        String token = this.auth.uploadToken(qiniuConfig.getBucketName());
        return stringQiniuResult.success(token);
    }


    /**
     * 文件下载
     *
     * @param key
     * @return InputStream
     */
    @Override
    public QiniuResult<DownFileResult> downFile(String key) {
        QiniuResult<DownFileResult> downFileResultQiniuResult = new QiniuResult<>();
        try {
            String encodedFileName = URLEncoder.encode(key, "utf-8").replace("+", "%20");
            String url = qiniuConfig.getHost() + "/" + encodedFileName + "?token=" + getUploadToken();
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(300, TimeUnit.SECONDS)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            okhttp3.Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    DownFileResult downFileResult = new DownFileResult();
                    InputStream inputStream = responseBody.byteStream();
                    byte[] data = IOUtils.toByteArray(inputStream);
                    downFileResult.setInputStream(inputStream);
                    downFileResult.setData(data);
                    return downFileResultQiniuResult.success(downFileResult);
                } else {
                    log.error("七牛下载文件错误,错误消息:消息体为空");
                    return downFileResultQiniuResult.error("消息体为空");
                }
            } else {
                log.error("七牛下载文件错误,错误消息:{}", response.message());
                return downFileResultQiniuResult.error(response.message());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            log.error("七牛下载文件错误,错误消息:{}", exception.getMessage());
            return downFileResultQiniuResult.error(exception.getMessage());
        }
    }
}

