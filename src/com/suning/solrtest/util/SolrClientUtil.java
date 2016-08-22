package com.suning.solrtest.util;

import org.apache.solr.client.solrj.impl.HttpSolrServer;

/**
 * 功能描述：获取评价core的连接
 * 
 * @author 13073007
 * 
 */
public class SolrClientUtil {
	
    // 评价数据SOLR读服务器地址 
    public static final String WRITE_URL = "http://solr:solr@10.27.43.43:9080/solr/member";
    // 评价数据SOLR服务器地址 
    public static final String READ_URL = "http://solr:solr@10.27.43.43:9080/solr/member";
    // SOLR so超时时间 
    public static final int SOLR_SOTIMEOUT = 5000;
    // SOLR连接超时时间 
    public static final int SOLR_CONNECTIONTIMEOUT = 2000;
    // SOLR最大连接数
    public static final int SOLR_MAXCONNECTIONSPERHOST = 100;
    // SOLR最大重试次数 
    public static final int SOLR_MAXRETRIES = 1;
    // SOLR所有最大连接数 
    public static final int SOLR_MAXTOTALCONNECTIONS = 100;
    // SOLR是否followRedirects 
    public static final boolean SOLR_FOLLOWREDIRECTS = true;
    // SOLR是否允许压缩 
    public static final boolean SOLR_ALLOWCOMPRESSION = false;
    
    // 采用单例模式获取对solr服务的连接-读
    private static HttpSolrServer readServer;
    // 采用单例模式获取对solr服务的连接-写
    private static HttpSolrServer writeServer;
    
    /**
     * 设置HTTP连接的公共属性
     * @param server
     */
    private static HttpSolrServer init(String baseURL) {
    	HttpSolrServer server = new HttpSolrServer(baseURL);
    	// 设置超时时间
    	server.setSoTimeout(SOLR_SOTIMEOUT);
        // 设置连接超时时间
    	server.setConnectionTimeout(SOLR_CONNECTIONTIMEOUT);
        // 设置每个路由的最大连接数
    	server.setDefaultMaxConnectionsPerHost(SOLR_MAXCONNECTIONSPERHOST);
        // 设置连接失败的重试次数
    	server.setMaxRetries(SOLR_MAXRETRIES);
        // 设置最大连接数
    	server.setMaxTotalConnections(SOLR_MAXTOTALCONNECTIONS);
        // 设置是否可压缩
    	server.setAllowCompression(SOLR_ALLOWCOMPRESSION);
        // 设置是否自动转发
    	server.setFollowRedirects(SOLR_FOLLOWREDIRECTS);
    	return server;
    }
    
    /**
     * 采用线程安全的单例模式获取对solr查詢服务的连接
     * 读
     * @return
     */
    public synchronized static HttpSolrServer getReadServer() {
        if (readServer == null) {
            // 创建连接，指定该连接所操作的core-评价core
        	readServer = init(READ_URL);
        }
        return readServer;
    }

    /**
     * 采用线程安全的单例模式获取对solr索引服务的连接
     * 写
     * @return
     */
    public synchronized static HttpSolrServer getWriteServer() {
        if (writeServer == null) {
            // 创建连接，指定该连接所操作的core-评价core
        	writeServer = init(WRITE_URL);
        }
        return writeServer;
    }
}
