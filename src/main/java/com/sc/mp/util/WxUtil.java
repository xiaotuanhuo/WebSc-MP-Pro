package com.sc.mp.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.sc.mp.model.WebScAccessToken;
import com.sc.mp.model.WebScUser;
import com.sc.mp.model.wxmessage.TextMessage;
import com.sc.mp.service.AccessTokenService;

@Configuration
@Component
public class WxUtil {
	private static final Logger log = LoggerFactory.getLogger(WxUtil.class);
	
	public static String sCorpid = "ww0a5757d175c0cfc6";
	
	public static String sCorpsecret = "tnRFaSEmGEXg1mWMpox527dQFhBQgWE2JBLRhZ7xyew";
	
	private Map<String, Date> AccessTokenMap = new HashMap<String, Date>();
	private String qAccessToken;
	
	@Resource
	private AccessTokenService accessTokenService;
	
	public void putAccessTokenMap(String str, Date d){
		AccessTokenMap.put(str, d);
	}
	public Date getAccessTokenMapByKey(String str){
		return AccessTokenMap.get(str);
	}
	public String getqAccessToken() {
		return qAccessToken;
	}
	public void setqAccessToken(String qAccessToken) {
		this.qAccessToken = qAccessToken;
	}  
	
	
	public void main(String[] args) {
		String strGetReq = sendGet2("https://qyapi.weixin.qq.com/cgi-bin/gettoken", "corpid=" + sCorpid + "&corpsecret=" + sCorpsecret);
		System.out.println(strGetReq);
	}
	
	public WebScUser getWxUserOpenid(String sCode, HttpSession session){
		WebScUser user = new WebScUser();
		
		try{
			String sAccessToken = this.getAccessToken(false, session);
			if(sAccessToken != null){
				String strGetReq = sendGet2("https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo", "access_token=" + sAccessToken + "&code=" + sCode);
				log.info("用户权限strGetReq：" + strGetReq);
				if(!strGetReq.equals("")){
					JSONObject json = JSONObject.parseObject(strGetReq);
					Integer iErrCode = (Integer) json.get("errcode");
					if(iErrCode == 0){
						String sUserId = (String) json.get("UserId");
						log.info("UserId:" + sUserId);
						if(sUserId == null){
							//非企业成员授权
							String sOpenId = (String) json.get("OpenId");
							log.info("非企业成员授权    OpenId:" + sOpenId);
							user.setWxUserid("");
							user.setWxOpenid(sOpenId);
						}else{
							//企业成员授权,根据userid转换成openid
							String sOpenId = changeUserIdtoOpenid(sUserId, session);
							log.info("企业成员授权    OpenId:" + sOpenId);
							user.setWxUserid(sUserId);
							user.setWxOpenid(sOpenId);
						}
					}else{
						String sErrMsg = (String) json.get("errmsg");
						log.error("获取用户权限失败：" + sErrMsg);
					}
				}
			}
		}catch (Exception e) {
			log.error("获取用户权限失败：" + e.getMessage());
		}
		
		return user;
	}
	
	public String getAccessToken(boolean bFlag, HttpSession session) {
		String sAccessToken = null;
		
		try{
			if(!bFlag){
				WebScAccessToken wsa = accessTokenService.getWebScAccessToken();
				log.info("wsa：" + wsa);
				if(wsa != null){
					if(wsa.getAccesstoken() != null && wsa.getOvertime() < 7200){
						sAccessToken = wsa.getAccesstoken();
						bFlag = false;
					}else{
						bFlag = true;
					}
				}else{
					bFlag = true;
				}
			}
			
		
			if(bFlag || sAccessToken == null || sAccessToken.trim().equals("")){
				//重新获取
				String strGetReq = sendGet2("https://qyapi.weixin.qq.com/cgi-bin/gettoken", "corpid=" + sCorpid + "&corpsecret=" + sCorpsecret);
				if(!strGetReq.equals("")){
					JSONObject json = JSONObject.parseObject(strGetReq);
					sAccessToken = (String) json.get("access_token");
					log.info("微信接口获取AccessToken：" + sAccessToken + "。");
					if(sAccessToken == null){
						Integer iErrCode = (Integer) json.get("errcode");
						String sErrMsg = (String) json.get("errmsg");
						log.error("获取微信授权错误！错误码：" + iErrCode + " 错误原因:" + sErrMsg);
					}else{
						//数据更新
						accessTokenService.updateWebScAccessToken(sAccessToken);
					}
				}
				
			}
		}catch (Exception e) {
			log.error("获取AccessToken失败：" + e.getMessage());
		}
		
		return sAccessToken;
	}
	
	public String changeUserIdtoOpenid(String sUserId, HttpSession session) {
		String sOpenid = "";
		try{
			String sAccessToken = this.getAccessToken(false, session);
			log.info("AccessToken:" + sAccessToken);
			String strPostReq = sendPost("https://qyapi.weixin.qq.com/cgi-bin/user/convert_to_openid?access_token="+ sAccessToken, "{\"userid\":\"" + sUserId +"\"}");
			log.info("转换结果:" + strPostReq);
			if(!strPostReq.equals("")){
				JSONObject json = JSONObject.parseObject(strPostReq);
				Integer iErrCode = (Integer) json.get("errcode");
				log.info("企业用户转换：" + iErrCode);
				if(iErrCode == 0){
					sOpenid = (String) json.get("openid");
				}else{
					String sErrMsg = (String) json.get("errmsg");
					log.error("转换UserId错误：" + sErrMsg);
				}
			}
			
		}catch (Exception e) {
			log.error("转换UserId错误：" + e.getMessage());
		}
		
		return sOpenid;
	}
	
	/**
	 * 给企业微信应用中的成员发送文本消息
	 * @param tm
	 * @param session
	 * @author wangrui
	 * @return
	 */
	public String sendTextMessage(TextMessage tm, HttpSession session) {
		String sAccessToken = this.getAccessToken(false, session);
		String textMessage = tm.toString();
		System.out.println(textMessage);
//		String textMessage = JSONObject.parseObject(tm.toString()).toString();
//		url = url.replaceAll("ACCESS_TOKEN", sAccessToken);
		String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + sAccessToken;
		log.info("调用的URL：" + url + "    msg:" + textMessage);
		return sendPost(url, textMessage);
	}
	
	/**
	 * 给企业微信应用中的成员发送文本消息
	 * @param tm
	 * @param session
	 * @author wangrui
	 * @return
	 */
//	public  String sendTextMessage(TextMessage tm, String sAccessToken) {
//		String textMessage = JSONObject.parseObject(tm.toString()).toString();
////		System.out.println(textMessage);
////		url = url.replaceAll("ACCESS_TOKEN", sAccessToken);
//		String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + sAccessToken;
//		log.info("调用的URL：" + url + "    msg:" + textMessage);
//		return sendPost(url, textMessage);
//	}
	
	/**
	 * weixin 网页jsdk调用签名生成
	 * @param sUrl
	 * @param nonceStr
	 * @param timestamp
	 * @return
	 */
	public String getSignature(String sUrl, String nonceStr, String timestamp, HttpSession session){
		String sSignature = null;
		try{
			//获取AccessToken
	        String sAccessToken = this.getAccessToken(false,session);
	        if(sAccessToken != null){
	        	//获取JsTicket
	        	String sJsTicket = this.getJsTicket(sAccessToken,session);
	        	if(sJsTicket != null){
	        		//String sTmp = "jsapi_ticket=" + sJsTicket + "&noncestr=Wm3WZYTPz0wzccnW&timestamp=2018040309&url=" + sUrl;
	        		String sTmp = "jsapi_ticket=" + sJsTicket + "&noncestr="+nonceStr+"&timestamp="+timestamp+"&url=" + sUrl;
	        		log.info("微信签名：" + sTmp);
	        		sSignature = SHA1(sTmp);
	        		log.info("微信加密签名：" + sSignature);
	        	}
	        }
		}catch (Exception e) {
			log.error("获取微信签名失败：" + e.getMessage());
		}
		
		return sSignature;
	}
	
	public  String getJsTicket(String sAccessToken, HttpSession session){
		String sJsTicket = null;
		try{
			//加redis判断（Redis中没有wx_js_ticket和redis挂掉了，就要重新生成）
//			if(JRedisUtils.pool.existsKey("wx_js_ticket")){
//	    		return JRedisUtils.pool.getValue("wx_js_ticket");
//	    	}
			//先从session里获取
			sJsTicket = (String)session.getAttribute("WX_JSTICKET");
			if(sJsTicket == null){
				String strPostReq = sendGet2("https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket", "access_token=" + sAccessToken);
				if(!strPostReq.equals("")){
					JSONObject json = JSONObject.parseObject(strPostReq);
					Integer iErrCode = (Integer) json.get("errcode");
					if(iErrCode == 0){
						sJsTicket =  (String) json.get("ticket");
						log.info("JsTicket:" + sJsTicket);
						session.setAttribute("WX_JSTICKET", sJsTicket);
//						JRedisUtils.pool.setString("wx_js_ticket", sJsTicket, 7000);
					}else{
						String sErrMsg = (String) json.get("errmsg");
						log.error("获取微信JsTicket错误！错误码：" + iErrCode + " 错误原因:" + sErrMsg);
					}
				}
			}
			
		}catch (Exception e) {
			log.error("获取微信JsTicket失败：" + e.getMessage());
		}
		
		return sJsTicket;
	}
	
	/**
	   * 根据内容类型判断文件扩展名
	   *
	   * @param contentType 内容类型
	   * @return
	   */
	  public String getFileexpandedName(String contentType) {
	    String fileEndWitsh = "";
	    if ("image/jpeg".equals(contentType))
	      fileEndWitsh = ".jpg";
	    else if ("audio/mpeg".equals(contentType))
	      fileEndWitsh = ".mp3";
	    else if ("audio/amr".equals(contentType))
	      fileEndWitsh = ".amr";
	    else if ("video/mp4".equals(contentType))
	      fileEndWitsh = ".mp4";
	    else if ("video/mpeg4".equals(contentType))
	      fileEndWitsh = ".mp4";
	    return fileEndWitsh;
	  }
	  /**
	   * 获取媒体文件
	   * @param accessToken 接口访问凭证
	   * @param mediaId 媒体文件id
	   * @param savePath 文件在本地服务器上的存储路径
	   * */
	  public String downloadMedia(String dirPath, String documentId, String mediaId, HttpSession session) {
		  String sFileName = "";
			String accessToken = this.getAccessToken(false, session);
		    // 拼接请求地址
		    String requestUrl = "https://qyapi.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
		    requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("MEDIA_ID", mediaId);
		    byte[] b = null;
		    try {
		    	//当前年月
	        	Date d = new Date();  
	            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");  
	            String dateNowStr = sdf.format(d);  
	            log.info("文件夹：" + dirPath  + File.separator + dateNowStr);              
	            
	            File path = new File(dirPath  + File.separator + dateNowStr);
	            if (!path.exists()) {
	            	path.mkdir();
				}
	            
	            URL url = new URL(requestUrl);
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	            conn.setDoInput(true);
	            conn.setRequestMethod("GET");
		      	// 根据内容类型获取扩展名
	            String hz = this.getFileexpandedName(conn.getHeaderField("Content-Type"));
		      
	            File file = new File(dirPath + File.separator + dateNowStr + File.separator + documentId + "_" + mediaId + hz);
	            if (!file.exists())
	            {
		    	  file.createNewFile();
	            }
	            OutputStream os = new FileOutputStream(file);
	            
	            // 将mediaId作为文件名
	            InputStream is = conn.getInputStream();
	            ByteArrayOutputStream baos = new ByteArrayOutputStream();
	            byte[] buffer = new byte[1024];
	            int len = 0;
	            while ((len = is.read(buffer)) != -1) {
	            	baos.write(buffer, 0, len);
	            }
	            b = baos.toByteArray();
				is.close();
				baos.close();
			 	conn.disconnect();
		      
			 	os.write(b);
			 	os.close();
		      
			 	sFileName = File.separator + dateNowStr + File.separator + documentId + "_" + mediaId + hz;
			 	log.info(sFileName);
		      
	    	} catch (Exception e) {
	    		String error = String.format("下载媒体文件失败：%s", e);
	    		System.out.println(error);
	    	}
	    	return sFileName;
	  }
	
//	public  String getAccessToken(){
//    	String sAccessToken = null;
//		try{
//			//加redis判断（Redis中没有AccessToken和redis挂掉了，就要重新生成）
//	    	if(JRedisUtils.pool.existsKey("wx_token")){
//	    		return JRedisUtils.pool.getValue("wx_token");
//	    	}
//			String strPostReq = sendPost("https://qyapi.weixin.qq.com/cgi-bin/token", "grant_type=client_credential&appid=&secret=");
//			if(!strPostReq.equals("")){
//				JSONObject json = JSONObject.fromObject(strPostReq);
//				sAccessToken = (String) json.get("access_token");
//				if(sAccessToken == null){
//					Integer iErrCode = (Integer) json.get("errcode");
//					String sErrMsg = (String) json.get("errmsg");
//					log.error("获取微信授权错误！错误码：" + iErrCode + " 错误原因:" + sErrMsg);
//				}else{
//					JRedisUtils.pool.setString("wx_token", sAccessToken, 7000);
//				}
//			}
//		
//		}catch (Exception e) {
//			log.error("获取用户权限失败：" + e.getMessage());
//		}
//		
//		return sAccessToken;
//	}
	
	/**
     * 获取时间戳(秒)
     */
    public  String getTimestamp() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }
    
    /** 
     * 取出一个指定长度大小的随机正整数. 
     * @param length 
     *            int 设定所取出随机数的长度。length小于11 
     * @return int 返回生成的随机数。 
     */  
    public  int buildRandom(int length) {  
        int num = 1;  
        double random = Math.random();  
        if (random < 0.1) {  
            random = random + 0.1;  
        }  
        for (int i = 0; i < length; i++) {  
            num = num * 10;  
        }  
        return (int) ((random * num));  
    }  

    /** 
     * 获取当前时间 yyyyMMddHHmmss 
     */  
    public  String getCurrTime() {  
        Date now = new Date();  
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");  
        String s = outFormat.format(now);  
        return s;  
    }  
    
    /**
     * 生成随机字符串
     */ 
    public  String getNonceStr() {
        String currTime = getCurrTime();  
        String strTime = currTime.substring(8, currTime.length());  
        String strRandom = buildRandom(4) + "";  
        return strTime + strRandom;
    }
    
/**************************************************************************************************************************/	
	
	
	/**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url 	发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 		所代表远程资源的响应结果
     */
    public  String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            HttpURLConnection httpURLConn = (HttpURLConnection)conn;
            // 设置通用的请求属性
            httpURLConn.setDoOutput(true);
            httpURLConn.setDoInput(true);
            httpURLConn.setRequestMethod("POST");
            httpURLConn.setRequestProperty("Accept-Charset", "utf-8");
            httpURLConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConn.setRequestProperty("Content-Length", String.valueOf(param.length()));

            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(httpURLConn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(httpURLConn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            log.info("发送 POST 请求出现异常！" + e.getMessage());
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
            	log.info("发送 POST 请求出现异常！" + ex.getMessage());
                ex.printStackTrace();
            }
        }
        return result;
    }
    
    public  String sendGet2(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
    
    /**
     * 向指定 URL 发送Get方法的请求
     * 
     * @param url 	发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 		所代表远程资源的响应结果
     */
    public  String sendGet(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            HttpURLConnection httpURLConn = (HttpURLConnection)conn;
            // 设置通用的请求属性
            httpURLConn.setDoOutput(true);
            httpURLConn.setDoInput(true);
            httpURLConn.setRequestMethod("GET");
            httpURLConn.setRequestProperty("Accept-Charset", "utf-8");
            httpURLConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConn.setRequestProperty("Content-Length", String.valueOf(param.length()));

            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(httpURLConn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(httpURLConn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            log.info("发送 GET 请求出现异常！" + e.getMessage());
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
            	log.info("发送 GET 请求出现异常！" + ex.getMessage());
                ex.printStackTrace();
            }
        }
        return result;
    }
    
    /**
     * POST 请求（HTTP），JSON形式
     * 
     * @param url 	发送请求的 URL
     * @param json  请求参数，请求参数应该是json的形式。
     * @return 		所代表远程资源的响应结果
     * @throws IOException 
     * @throws ClientProtocolException 
     */
    public  String sendPostJson(String url, String json) throws Exception {
    	CloseableHttpClient httpclient = HttpClients.createDefault();
    	StringBuffer result = new StringBuffer();
    	
    	HttpPost httpPost = new HttpPost(url);
    	log.info(url);
    	httpPost.setHeader("Content-type","application/json; charset=utf-8");
    	httpPost.setHeader("Accept", "application/json");
    	httpPost.setEntity(new StringEntity(json, Charset.forName("UTF-8")));
    	
		CloseableHttpResponse response = httpclient.execute(httpPost);
		HttpEntity entity = response.getEntity();
		InputStream is = entity.getContent();
			
		byte[] b = new byte[4096];
		for (int n; (n = is.read(b)) != -1;) {
			result.append(new String(b, 0, n));
		}

		System.out.println(result.toString());

        return result.toString();
    }
    
    /** 
     * SHA1 安全加密算法 
     * @param maps 参数key-value map集合 
     * @return 
     * @throws DigestException  
     */  
    public  String SHA1(String decrypt) throws DigestException {  
        try {  
            //指定sha1算法  
            MessageDigest digest = MessageDigest.getInstance("SHA-1");  
            digest.update(decrypt.getBytes());  
            //获取字节数组  
            byte messageDigest[] = digest.digest();  
            // Create Hex String  
            StringBuffer hexString = new StringBuffer();  
            // 字节数组转换为 十六进制 数  
            for (int i = 0; i < messageDigest.length; i++) {  
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);  
                if (shaHex.length() < 2) {  
                    hexString.append(0);  
                }  
                hexString.append(shaHex);  
            }  
            return hexString.toString().toUpperCase();  
  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
            throw new DigestException("签名错误！");  
        }  
    }
}
