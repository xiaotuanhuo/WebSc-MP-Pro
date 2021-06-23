package com.sc.mp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

/**
 * HTTP和HTTPS请求工具类
 * @author pp
 *
 */
public class HttpsUtil {
	
	public static String URL = "https://localhost";
	public static CloseableHttpClient httpClient = null;
	private static final String charset = "UTF-8";
	
	static{
		try {
	        HttpClientBuilder builder = HttpClientBuilder.create().useSystemProperties();
	        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null,
	                (TrustStrategy) (X509Certificate[] arg0, String arg1) -> true).build();
	        builder.setSSLContext(sslContext);
	        HostnameVerifier hostnameVerifier = new NoopHostnameVerifier();
	        SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
	        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
	                .register("http", PlainConnectionSocketFactory.getSocketFactory())
	                .register("https", sslSocketFactory)
	                .build();
	        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
	        builder.setConnectionManager(connMgr);
	        httpClient = builder.build();
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	}
	
	/**
	 * https GET请求
	 * @param uri
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getHttps(URI uri) throws Exception{
		HttpGet httpGet = new HttpGet(uri);
		String result = null;
		
		CloseableHttpResponse httpresponse = HttpsUtil.httpClient.execute(httpGet);
		int code = httpresponse.getStatusLine().getStatusCode();
		if(HttpStatus.SC_OK == code){
			HttpEntity entity = httpresponse.getEntity();
			if(entity != null){
				long len = entity.getContentLength();
				if(len!= -1 && len <2048){
					result = EntityUtils.toString(entity,charset);
				} else {
					//流式传输内容
					result = getMessage(entity.getContent(), charset);
				}
			}
		}else{
			result = EntityUtils.toString(httpresponse.getEntity(),charset);
			throw new Exception("HttpStatus："+code+"，errorMsg："+result);
		}

		httpresponse.close();
		
		return result;
	}
	
	/**
	 * https delete请求
	 * @param uri
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static String deleteHttps(URI uri) throws Exception{
		HttpDelete httpDelete = new HttpDelete(uri);
		String result = null;
		
		CloseableHttpResponse httpresponse = HttpsUtil.httpClient.execute(httpDelete);
		int code = httpresponse.getStatusLine().getStatusCode();
		if(HttpStatus.SC_OK == code){
			HttpEntity entity = httpresponse.getEntity();
			if(entity != null){
				long len = entity.getContentLength();
				if(len!= -1 && len <2048){
					result = EntityUtils.toString(entity,charset);
				} else {
					//流式传输内容
					result = getMessage(entity.getContent(), charset);
				}
			}
		}else{
			result = EntityUtils.toString(httpresponse.getEntity(),charset);
			throw new Exception("HttpStatus："+code+"，errorMsg："+result);
		}
		
		httpresponse.close();
		
		return result;
	}

	/**
	 * https post请求
	 * @param url
	 * @param data
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String postHttps(String url, String data) throws Exception{
		HttpPost httpPost = new HttpPost(url);
		String result = null;
		
		//构建超时等配置信息
        RequestConfig config = RequestConfig.custom()
        		.setConnectTimeout(5000) //连接超时时间
                .setConnectionRequestTimeout(5000) //从连接池中取的连接的最长时间
                .setSocketTimeout(10000) //数据传输的超时时间
                .build();
        //设置请求配置时间
        httpPost.setConfig(config);
		httpPost.setEntity(new StringEntity(data, ContentType.create("application/json", charset)));
		CloseableHttpResponse httpresponse = HttpsUtil.httpClient.execute(httpPost);
		int code = httpresponse.getStatusLine().getStatusCode();
		if(HttpStatus.SC_OK == code){
			HttpEntity entity = httpresponse.getEntity();
			if(entity != null){
				long len = entity.getContentLength();
				if(len!= -1 && len <2048){
					result = EntityUtils.toString(entity,charset);
				} else {
					//流式传输内容
					result = getMessage(entity.getContent(), charset);
				}
			}
		}else{
			result = EntityUtils.toString(httpresponse.getEntity(),charset);
			throw new Exception("HttpStatus："+code+"，errorMsg："+result);
		}

		httpresponse.close();
		
		return result;
	}
	
	/**
	 * HttpPost发送Form请求
	 * @param url 请求地址
	 * @param paraMap 参数
	 * @return
	 * @throws Exception
	 */
	public static String postHttpsForm(String url, Map<String, String> paraMap) throws Exception{
		String result = null;
		HttpPost httpPost = new HttpPost(url);
		
		RequestConfig requestConfig = RequestConfig.custom().
				setConnectTimeout(30000).
				setConnectionRequestTimeout(30000).
				setSocketTimeout(30000).
				build();
		httpPost.setConfig(requestConfig);
		httpPost.setHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		List<BasicNameValuePair> paraList = new ArrayList<BasicNameValuePair>();
		paraMap.forEach((k,v)->{
			paraList.add(new BasicNameValuePair(k, v));
		});
		
		httpPost.setEntity(new UrlEncodedFormEntity(paraList));
		
		CloseableHttpResponse httpresponse = HttpsUtil.httpClient.execute(httpPost);
		int code = httpresponse.getStatusLine().getStatusCode();
		if(HttpStatus.SC_OK == code){
			HttpEntity entity = httpresponse.getEntity();
			if(entity != null){
				long len = entity.getContentLength();
				if(len!= -1 && len <2048){
					result = EntityUtils.toString(entity,charset);
				} else {
					//流式传输内容
					result = getMessage(entity.getContent(), charset);
				}
			}
		}else{
			throw new Exception("code = "+code+" ， message = "+httpresponse.getStatusLine().getReasonPhrase());
		}

		httpresponse.close();
		
		return result;
	}
	
	/**
     * 从InputStream获取报文信息.
     * 
     * @param input
     * @return 报文
     * @throws IOException
     */
    public  static String getMessage(InputStream input,String charset) throws IOException {
        StringBuffer buffer = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(input, charset));
        String line;
        while (true) {
            line = br.readLine();
            if (line == null) {
                break;
            }
            buffer.append(line + "\n");
        }
        return buffer.toString();
    }
	
	public static void main(String[] args) {
		try {
//			URI uri = new URI(
//					"https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx5d6d3e879d4eb8c8&secret=0dabfe7f043b7cfa6fb6ace566f9c596");
//			String responseMsg = getHttps(uri);
//			System.out.println(responseMsg);
			
//			String materials = postHttps("https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=41_AW-3nwpbmQDXFW_YEjYwqujwYmM-MkbGd8DOMwWkIEHr0sLGP29XGc-7-dwZwzXLhxyJBzBkVVR1x9gh4gSaOtV9BZpUMDOaT7yyrTbarWs7zgylSG3JJetFHZ-Hi4GLHnq1D5_dlumlq5M1DMTjAHAXGN", "{\r\n" + 
//					"    \"type\":\"news\",\r\n" + 
//					"    \"offset\":0,\r\n" + 
//					"    \"count\":20\r\n" + 
//					"}");
//			System.out.println(materials);
			
			System.out.println(new Date(1612230340l*1000));
			System.out.println(new Date().getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
