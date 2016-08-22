package com.suning.solrtest.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.suning.solrtest.dao.SolrMemberDao;
import com.suning.solrtest.entity.SolrMember;

public class SolrMemberTest {

    SolrMemberDao solrMemberDao = new SolrMemberDao();

    /**
     * 保存数据
     */
    public void saveData() {
        try {
            List<SolrMember> list = new ArrayList<SolrMember>();
            SolrMember solrMember = new SolrMember();
            solrMember.setCustNum("6115007689");
            list.add(solrMember);
            
            solrMember = new SolrMember();
            solrMember.setCustNum("6115008198");
            list.add(solrMember);
            
            solrMember = new SolrMember();
            solrMember.setCustNum("6115008729");
            list.add(solrMember);
            
            solrMemberDao.saveOrUpdate(list);
            solrMemberDao.commit();
            System.out.println("当前结束时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询所有
     */
    public void getAllData() {
        try {
            List<SolrMember> memberList = solrMemberDao.queryForList("", "", SolrMember.class);
            for (SolrMember member : memberList) {
                System.out.println(member.getCustNum()+ ":" + member.getLoginId());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 根据查询条件 查询
     * @param ql
     * @param fql
     */
    public void getDataByQuery(String ql, String fql) {
        try {
            List<SolrMember> memberList = solrMemberDao.queryForList(ql, fql, SolrMember.class);
            for (SolrMember member : memberList) {
                System.out.println(member.getCustNum()+ ":" + member.getLoginId());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        SolrMemberTest mySolrTest = new SolrMemberTest();
        mySolrTest.saveData();
        mySolrTest.getAllData();
    }
}
