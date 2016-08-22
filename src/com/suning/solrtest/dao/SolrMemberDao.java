package com.suning.solrtest.dao;

import com.suning.solrtest.util.SolrClientUtil;

public class SolrMemberDao extends SolrDao{
	
	public SolrMemberDao() {
		// 回复的core连接
		this.setReadSolrServer(SolrClientUtil.getReadServer());
		this.setWriteSolrServer(SolrClientUtil.getWriteServer());
	}
}
