package com.fkp.test;

import com.fkp.utils.QiniuUtils;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;



//public class QiNiuTest {
//
//    @Test
//    public void fileUploadTest(){
//        //构造一个带指定 Region 对象的配置类
//        Configuration cfg = new Configuration(Zone.zone0());
////...其他参数参考类注释
//        UploadManager uploadManager = new UploadManager(cfg);
////...生成上传凭证，然后准备上传
//        String accessKey = "LIsvq2hK7YJLyuyNO3W2RzgoNUVrFBDCQwY744Ln";
//        String secretKey = "D8NyVIQjU4QTkkYtHdjg3veFRAIDV_754SfSHASC";
//        String bucket = "fkp";
////如果是Windows情况下，格式是 D:\\qiniu\\test.png
//        String localFilePath = "E:\\Example\\Java\\HealthManagement\\health_parent\\health_common\\src\\main\\file\\泛项目管理系统交付转维需求V1.0.docx";
////默认不指定key的情况下，以文件内容的hash值作为文件名
//        String key = "泛项目管理系统交付转维需求V1.0.docx";
//        Auth auth = Auth.create(accessKey, secretKey);
//        String upToken = auth.uploadToken(bucket);
//        try {
//            Response response = uploadManager.put(localFilePath, key, upToken);
//            //解析上传成功的结果
//            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
//            System.out.println(putRet.key);
//            System.out.println(putRet.hash);
//        } catch (QiniuException ex) {
//            Response r = ex.response;
//            System.err.println(r.toString());
//            try {
//                System.err.println(r.bodyString());
//            } catch (QiniuException ex2) {
//                //ignore
//            }
//        }
//    }
//
//    @Test
//    public void deleteFileTest(){
////构造一个带指定 Region 对象的配置类
//        Configuration cfg = new Configuration(Zone.zone0());
////...其他参数参考类注释
//        String accessKey = "LIsvq2hK7YJLyuyNO3W2RzgoNUVrFBDCQwY744Ln";
//        String secretKey = "D8NyVIQjU4QTkkYtHdjg3veFRAIDV_754SfSHASC";
//        String bucket = "fkp";
//        String key = "泛项目管理系统交付转维需求V1.0.docx";
//        Auth auth = Auth.create(accessKey, secretKey);
//        BucketManager bucketManager = new BucketManager(auth, cfg);
//        try {
//            bucketManager.delete(bucket, key);
//        } catch (QiniuException ex) {
//            //如果遇到异常，说明删除失败
//            System.err.println(ex.code());
//            System.err.println(ex.response.toString());
//        }
//    }
//
//    @Test
//    public void QiNiuUtilsTest(){
//        QiniuUtils.upload2Qiniu("E:\\Example\\Java\\HealthManagement\\health_parent\\health_common\\src\\main\\file\\泛项目管理系统交付转维需求V1.0.docx","泛项目管理系统交付转维需求V1.0.docx");
//    }
//
//    @Test
//    public void QiNiuUtilsTest2(){
//        QiniuUtils.deleteFileFromQiniu("泛项目管理系统交付转维需求V1.0.docx");
//    }
//}
