package cn.net.bluedata.qiniu.component;

import cn.net.bluedata.qiniu.result.DownFileResult;
import cn.net.bluedata.qiniu.result.QiniuResult;
import cn.net.bluedata.qiniu.result.UpLoadFileResult;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public interface IQiniuComponent {
    /**
     * 上传文件
     * <p>文件上传</p>
     *
     * @param file    填写上传文件File类型
     * @param key     添加上传的key值
     * @param existed 是否已经存在
     */
    QiniuResult<UpLoadFileResult> uploadFile(File file, String key, boolean existed);
    /**
     * 文件删除 （文件地址）
     * @param filePath 填写上传文件的位置
     * @param key 添加上传的key值
     * @param existed 是否已经存在
     * @return
     */
    QiniuResult<UpLoadFileResult> uploadFile(String filePath, String key, boolean existed);
    /**
     * 文件删除 （字节数据）
     * @param data 填写上传文件的位置
     * @param key 添加上传的key值
     * @param existed 是否已经存在
     * @return
     */
    QiniuResult<UpLoadFileResult> uploadFile(byte[] data, String key, boolean existed);

    /**
     * 删除
     * @param key 添加上传的key值
     */
    QiniuResult<Boolean> deleteFile(String key);

    /**
     * 获取上传token
     * <p>获取token</p>
     * @return 返回String
     */
    QiniuResult<String> getUploadToken() ;

    /**
     * 文件下载
     *
     * @return byte
     */
    QiniuResult<DownFileResult>  downFile(String key) throws UnsupportedEncodingException;



}
