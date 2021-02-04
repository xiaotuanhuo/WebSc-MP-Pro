package com.sc.mp.quartz;

import java.net.URI;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sc.mp.mapper.WebScGzhMediaMapper;
import com.sc.mp.model.WebScGzhMedia;
import com.sc.mp.util.HttpsUtil;

@Component
public class GzhMediaScheduleTask {
	private static final Logger logger = LoggerFactory.getLogger(GzhMediaScheduleTask.class);
	
	@Autowired
	WebScGzhMediaMapper webScGzhMediaMapper;
	@Value("${wx-gzh-appid}")
	private String gzhAppid;
	@Value("${wx-gzh-secret}")
	private String gzhSecret;
	@Value("${wx-gzh-api-uri}")
	private String gzhUri;
	
	@Scheduled(cron = "${gzh-media-cron-expression}")
    public void updGzhMedia(){
    	logger.info("------公众号图文信息更新任务已启动------");
    	try {
    		//获取accessToken
    		URI accessTokenUri = new URI(
    				gzhUri+"token?grant_type=client_credential&appid="+gzhAppid+"&secret="+gzhSecret);
			String accessTokenResponseMsg = HttpsUtil.getHttps(accessTokenUri);
			logger.info("accessToken :"+accessTokenResponseMsg);
			JSONObject tokenJsonObject = JSONObject.parseObject(accessTokenResponseMsg);
			String accessToken = tokenJsonObject.getString("access_token");
			
			//获取图文素材总数
			URI materialCountUri = new URI(
    				gzhUri+"material/get_materialcount?access_token="+accessToken);
			String materialCountResponseMsg = HttpsUtil.getHttps(materialCountUri);
			logger.info("accessToken :"+materialCountResponseMsg);
			JSONObject materialCountJsonObject = JSONObject.parseObject(materialCountResponseMsg);
			int newsCount = materialCountJsonObject.getInteger("news_count");
			
			//获取图文素材列表
			int pageCount = newsCount%20==0?(newsCount/20):(newsCount/20+1);
			for (int i = 0; i < pageCount; i++) {
				String materialsResponseMsg = HttpsUtil.postHttps(gzhUri+"material/batchget_material?access_token="+accessToken, 
						"{\r\n" + 
						"    \"type\":\"news\",\r\n" + 
						"    \"offset\":"+(i*20)+",\r\n" + 
						"    \"count\":20\r\n" + 
						"}");
				JSONObject materialsJsonObject = JSONObject.parseObject(materialsResponseMsg);
				JSONArray meterialsItemJsonArray = materialsJsonObject.getJSONArray("item");
				for (int j = 0; j < meterialsItemJsonArray.size(); j++) {
					
					JSONObject meterialsItem = (JSONObject) meterialsItemJsonArray.get(j);
					
					if (!"1".equals(webScGzhMediaMapper.isExists(meterialsItem.getString("media_id")))) {
						JSONObject contentJsonObject = meterialsItem.getJSONObject("content");
						JSONArray newsItemJsonArray = contentJsonObject.getJSONArray("news_item");
						JSONObject itemJsonObject = (JSONObject) newsItemJsonArray.get(0);
						
						WebScGzhMedia webScGzhMedia = new WebScGzhMedia();
						webScGzhMedia.setMediaId(meterialsItem.getString("media_id"));
						webScGzhMedia.setUpdateTime(new Date(meterialsItem.getLong("update_time")*1000));
						webScGzhMedia.setTitle(itemJsonObject.getString("title"));
						webScGzhMedia.setAuthor(itemJsonObject.getString("author"));
						webScGzhMedia.setDigest(itemJsonObject.getString("digest"));
//						webScGzhMedia.setContent(itemJsonObject.getString("content"));
						webScGzhMedia.setContentSourceUrl(itemJsonObject.getString("content_source_url"));
						webScGzhMedia.setThumbMediaId(itemJsonObject.getString("thumb_media_id"));
						webScGzhMedia.setShowCoverPic(itemJsonObject.getString("show_cover_pic"));
						webScGzhMedia.setUrl(itemJsonObject.getString("url"));
						webScGzhMedia.setThumbUrl(itemJsonObject.getString("thumb_url"));
						logger.info(webScGzhMedia.toString());
						
						webScGzhMediaMapper.insert(webScGzhMedia);
					}
				}
			}
			
		} catch (Exception e) {
			logger.error("公众号图文信息更新时出错，"+e.getMessage());
			e.printStackTrace();
		}
    	logger.info("------公众号图文信息更新任务已结束------");
    }
}
