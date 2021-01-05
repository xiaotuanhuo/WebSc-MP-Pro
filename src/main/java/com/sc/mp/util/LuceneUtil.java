package com.sc.mp.util;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.sc.mp.model.WebScOperative;
import com.sc.mp.model.WebScUser;

public class LuceneUtil {
	public static final String operativeNameFieldName = "operativeName";
	public static final String operativeIdFieldName = "operativeId";
	public static final String userIdFieldName = "userId";
	public static final String userNameFieldName = "userName";
	public static final String roleNameFieldName = "roleName";
	
	/**
	 * 创建手术名称索引
	 * @param indexPath 索引库路径
	 * @param webScOperatives 待索引的手术名
	 * @throws Exception
	 */
	public static void createOperativeNameIndex(String indexPath, List<WebScOperative> webScOperatives) throws Exception {
		
		//指定索引库的存放位置Directory对象
		Directory directory = FSDirectory.open(Paths.get(indexPath));
		//指定一个分析器，对文档内容进行分析
		Analyzer analyzer = new SmartChineseAnalyzer();
		//创建indexwriterCofig对象
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		//创建一个indexwriter对象
		try(IndexWriter indexWriter = new IndexWriter(directory, config);){
			//删除全部索引
			indexWriter.deleteAll();
			
			for (WebScOperative webScOperative : webScOperatives) {
				//创建document对象
				Document document = new Document();
				//创建field对象，将field添加到document对象中
				Field operativeNameField = new TextField(
						operativeNameFieldName, webScOperative.getOperativeName(), Store.YES);
				document.add(operativeNameField);
				//operativeId径域（不分析、不索引、只存储）
				Field operativeIdField = new StoredField(
						operativeIdFieldName, webScOperative.getOperativeId());
				document.add(operativeIdField);
				//使用indexwriter对象将document对象写入索引库，此过程进行索引创建。并将索引和document对象写入索引库。
				indexWriter.addDocument(document);
			}
		}catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 检索手术名称
	 * @param indexPath 索引库路径
	 * @param key 搜索关键字
	 * @param hits 最高返回命中数
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, String>> searchOperativeNames(String indexPath, String key, int hits) throws Exception{
		//创建一个Directory对象，指定索引库存放的路径
        Directory directory = FSDirectory.open(Paths.get(indexPath));
        //创建IndexReader对象，需要指定Directory对象
        try(IndexReader indexReader = DirectoryReader.open(directory);){
        	//创建Indexsearcher对象，需要指定IndexReader对象
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            //创建queryparser对象
            //第一个参数默认搜索的域
            //第二个参数就是分析器对象
            QueryParser queryParser = new QueryParser(operativeNameFieldName, new SmartChineseAnalyzer());
            //使用默认的域,这里用的是语法，下面会详细讲解一下
            Query query = queryParser.parse(key);
            //不使用默认的域，可以自己指定域
            //Query query = queryParser.parse("fileContent:apache");
            //第一个参数是查询对象，第二个参数是查询结果返回的最大值
            TopDocs topDocs = indexSearcher.search(query, hits);
            //查询结果的总条数
            System.out.println("查询结果的总条数："+ topDocs.totalHits);
            List<Map<String, String>> hitList = new ArrayList<Map<String, String>>();
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            	Map<String, String> hitMap = new HashMap<String, String>();
                //scoreDoc.doc属性就是document对象的id
                //根据document的id找到document对象
                Document document = indexSearcher.doc(scoreDoc.doc);
                //手术名称
                hitMap.put("text", document.get(operativeNameFieldName));
                hitMap.put("value", document.get(operativeIdFieldName));
                hitList.add(hitMap);
                
            }
            
            return hitList;
        }catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 创建用户名称索引
	 * @param indexPath
	 * @param users
	 * @throws Exception
	 */
	public static void createUserNameIndex(String indexPath, List<WebScUser> users) throws Exception {
		// 指定索引库的存放位置Directory对象
		Directory directory = FSDirectory.open(Paths.get(indexPath));
		// 指定一个分析器，对文档内容进行分析
		Analyzer analyzer = new SmartChineseAnalyzer();
		// 创建indexwriterCofig对象
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		// 创建一个indexwriter对象
		try (IndexWriter indexWriter = new IndexWriter(directory, config);) {
			// 删除全部索引
			indexWriter.deleteAll();
			for (WebScUser user : users) {
				// 创建document对象
				Document document = new Document();
				// 创建field对象，将field添加到document对象中
				Field operativeNameField = new TextField(userNameFieldName, user.getUserName(), Store.YES);
				document.add(operativeNameField);
				// operativeId径域（不分析、不索引、只存储）
				Field operativeIdField = new StoredField(userIdFieldName, user.getUserId());
				document.add(operativeIdField);
				Field operativeRoleField = new StoredField(roleNameFieldName, user.getRoleName());
				document.add(operativeRoleField);
				// 使用indexwriter对象将document对象写入索引库，此过程进行索引创建。并将索引和document对象写入索引库。
				indexWriter.addDocument(document);
			}
		} catch (Exception e) {
			throw new Exception("创建用户索引失败:" + e.getMessage());
		}
	}
	
	/**
	 * 检索用户名称
	 * @param indexPath
	 * @param key
	 * @param hits
	 * @return
	 * @throws Exception
	 */
	public static String searchUserNames(String indexPath, String key, int hits)
			throws Exception {
		// 创建一个Directory对象，指定索引库存放的路径
		Directory directory = FSDirectory.open(Paths.get(indexPath));
		// 创建IndexReader对象，需要指定Directory对象
		try (IndexReader indexReader = DirectoryReader.open(directory);) {
			// 创建Indexsearcher对象，需要指定IndexReader对象
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);
			// 创建queryparser对象
			// 第一个参数默认搜索的域
			// 第二个参数就是分析器对象
			QueryParser queryParser = new QueryParser(userNameFieldName, new SmartChineseAnalyzer());
			// 使用默认的域,这里用的是语法，下面会详细讲解一下
			Query query = queryParser.parse(key);
			// 不使用默认的域，可以自己指定域
			// Query query = queryParser.parse("fileContent:apache");
			// 第一个参数是查询对象，第二个参数是查询结果返回的最大值
			TopDocs topDocs = indexSearcher.search(query, hits);
			String result = "[";
			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
				// scoreDoc.doc属性就是document对象的id
				// 根据document的id找到document对象
				Document document = indexSearcher.doc(scoreDoc.doc);
				// 拼接前端autocomplete.js可以解析的标准格式用户信息字符串
				result += "{\"label\":\"" + document.get(userNameFieldName) + "\",\"id\":\"" + document.get(userIdFieldName)
				+ "\",\"hex\":\"" + document.get(roleNameFieldName) +  "\"},";
			}
			if (!result.equals("[")) {
				result = result.substring(0, result.length() - 1) + "]";
			} else {
				result = "";
			}
			return result;
		} catch (Exception e) {
			throw e;
		}
	}
}
