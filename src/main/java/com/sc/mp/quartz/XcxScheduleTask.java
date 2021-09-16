package com.sc.mp.quartz;

import java.net.URI;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sc.mp.mapper.OperativeMapper;
import com.sc.mp.mapper.OrganizationMapper;
import com.sc.mp.mapper.WebOrgOperativesMapper;
import com.sc.mp.mapper.WebScDocOperativeMapper;
import com.sc.mp.mapper.WebScGzhMediaMapper;
import com.sc.mp.model.WebOrgOperatives;
import com.sc.mp.model.WebScGzhMedia;
import com.sc.mp.model.WebScOperative;
import com.sc.mp.model.WebScOrganization;
import com.sc.mp.util.HttpsUtil;
import com.sc.mp.util.StringUtil;

@Component
public class XcxScheduleTask {
	private static final Logger logger = LoggerFactory.getLogger(XcxScheduleTask.class);
	
	@Autowired
	WebScGzhMediaMapper webScGzhMediaMapper;
	@Autowired
	OrganizationMapper organizationMapper;
	@Autowired
	WebScDocOperativeMapper webScDocOperativeMapper;
	@Autowired
	OperativeMapper operativeMapper;
	@Autowired
	WebOrgOperativesMapper webOrgOperativesMapper;
	
	@Value("${wx-gzh-appid}")
	private String gzhAppid;
	@Value("${wx-gzh-secret}")
	private String gzhSecret;
	@Value("${wx-gzh-api-uri}")
	private String gzhUri;
	
	@Scheduled(cron = "${statistics-org-operative-cron-expression}")
	public void statisticsOrgOperative() {
		List<WebScOrganization> organizations = organizationMapper.getOrgs();
		for (WebScOrganization webScOrganization : organizations) {
			List<String> operativeIds = webScDocOperativeMapper.getCommonlyUsedOperative(webScOrganization.getOrgId(), 10);
			for (String operativeId : operativeIds) {
				WebScOperative operative = operativeMapper.selectOperativeById(operativeId);
				
				if(StringUtil.isNull(operative)) {
					WebOrgOperatives webOrgOperative = new WebOrgOperatives();
					webOrgOperative.setOrgId(webScOrganization.getOrgId());
					webOrgOperative.setOperativeId(operativeId);
					webOrgOperative.setOperativeName(operative.getOperativeName());
					webOrgOperativesMapper.insert(webOrgOperative);
				}
			}
		}
	}
	
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
