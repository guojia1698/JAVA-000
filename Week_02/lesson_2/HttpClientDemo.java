package java0.nio01;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpClientDemo {
    public static void main(String[] args) {
        String url = "http://127.0.0.1:8801";
        String responseContent = doGetRequest(url);
        System.out.println("请求返回信息：" + responseContent);
    }

    public static String doGetRequest(String url){
        String responseContent = "请求8801返回信息";
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        //创建GET请求
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode == 200){
                responseContent = EntityUtils.toString(response.getEntity(),"UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseContent;
    }


}
