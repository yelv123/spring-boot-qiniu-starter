package cn.net.bluedata.qiniu.result;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class QiniuResult<T> {

    /**
     * 消息code
     */
    private boolean code;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据
     */
    private T data;


    public QiniuResult<T> success(T data) {
        this.code = true;
        this.message = "ok";
        this.data = data;
        return this;
    }

    public QiniuResult<T> error(String message) {
        this.code = false;
        this.message = message;
        return this;
    }

    public Boolean isSuccess() {
        return this.code;
    }
}
