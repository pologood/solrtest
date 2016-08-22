package com.suning.solrtest.dao;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.ModifiableSolrParams;

import com.mysql.jdbc.StringUtils;

public class SolrDao {

    // 获取Solr服务的连接-读
    private HttpSolrServer readSolrServer;
    // 获取Solr服务的连接-写
    private HttpSolrServer writeSolrServer;

    public static void main(String[] args) {
        String labelType = "label_type_desc,labelName_ASC";

        String[] sorts = labelType.split(",");
        for (String str : sorts) {
            String field = str.substring(0, str.lastIndexOf("_"));
            String sortType = str.substring(str.lastIndexOf("_") + 1);
            System.out.println(field + "########" + sortType);
        }
    }

    /**
     * 根据对象查询Solr对象
     * 
     * @param ql q模糊查询（commodityCode:* OR commodityReviewId:* AND shopFlag:*）
     * @param fql fq精准查询（commodityCode:00000000012345678 OR commodityReviewId:12345678 AND shopFlag:1）
     * @param sortMap key 列名；value 升/降序 String labelType = "label_type_desc,labelName_ASC"
     * @param showFields 显示指定的筛选字段 commodityCode,commodityReviewId
     * @param startRow 起始页
     * @param rowsNum 每页显示行数
     * @return
     * @throws SolrServerException
     */
    public QueryResponse queryForResponseByFacet(String ql, String fql, String sortFields, String showFields,
            String facetField, int startRow, int rowsNum) {
        SolrQuery query = new SolrQuery();
        // q查询
        if (StringUtils.isNullOrEmpty(ql)) {
            ql = "*:*";
        }
        query.setQuery(ql);
        // fq查询
        if (StringUtils.isNullOrEmpty(ql)) {
            fql = "";
        }
        query.setFilterQueries(fql);
        // 排序
        String[] sorts = sortFields.split(",");
        for (String str : sorts) {
            if (!StringUtils.isNullOrEmpty(str)) {
                String sortField = str.substring(0, str.lastIndexOf("_"));
                String sortType = str.substring(str.lastIndexOf("_") + 1);
                if (!StringUtils.isNullOrEmpty(sortField) && !StringUtils.isNullOrEmpty(sortType)) {
                    if (("DESC").equalsIgnoreCase(sortType)) {
                        query.addSort(sortField, ORDER.desc);
                    } else {
                        query.addSort(sortField, ORDER.asc);
                    }
                }
            }
        }
        // 如果有指定的筛选字段则设置fields
        if (!StringUtils.isNullOrEmpty(showFields)) {
            query.setFields(showFields);
        }
        if (!StringUtils.isNullOrEmpty(facetField)) {
            query.setFacet(true);
            query.set("facet.field", facetField);
        }
        // 起始页
        query.setStart(startRow);
        // 每页行数
        query.setRows(rowsNum);
        // 执行查询操作
        QueryResponse response;
        try {
            response = readSolrServer.query(query);
            if (response != null) {
                return response;
            }
        } catch (SolrServerException e) {
            return null;
        }
        return null;
    }

    /**
     * 根据对象查询Solr对象
     * 
     * @param ql q模糊查询（commodityCode:* OR commodityReviewId:* AND shopFlag:*）
     * @param fql fq精准查询（commodityCode:00000000012345678 OR commodityReviewId:12345678 AND shopFlag:1）
     * @param sortMap key 列名；value 升/降序 String labelType = "label_type_desc,labelName_ASC"
     * @param showFields 显示指定的筛选字段 commodityCode,commodityReviewId
     * @param startRow 起始页
     * @param rowsNum 每页显示行数
     * @return
     * @throws SolrServerException
     */
    public QueryResponse queryForResponse(String ql, String fql, String sortFields, String showFields, int startRow,
            int rowsNum) throws SolrServerException {
        SolrQuery query = new SolrQuery();
        // q查询
        if (StringUtils.isNullOrEmpty(ql)) {
            ql = "*:*";
        }
        query.setQuery(ql);
        // fq查询
        if (StringUtils.isNullOrEmpty(ql)) {
            fql = "";
        }
        query.setFilterQueries(fql);
        // 排序
        if (StringUtils.isNullOrEmpty(ql)) {
            String[] sorts = sortFields.split(",");
            for (String str : sorts) {
                String sortField = str.substring(0, str.lastIndexOf("_"));
                String sortType = str.substring(str.lastIndexOf("_") + 1);
                if (!StringUtils.isNullOrEmpty(sortField) && !StringUtils.isNullOrEmpty(sortType)) {
                    if (("DESC").equalsIgnoreCase(sortType)) {
                        query.addSort(sortField, ORDER.desc);
                    } else {
                        query.addSort(sortField, ORDER.asc);
                    }
                }
            }
        }
        // 如果有指定的筛选字段则设置fields
        if (!StringUtils.isNullOrEmpty(showFields)) {
            query.setFields(showFields);
        }
        // 起始页
        query.setStart(startRow);
        // 每页行数
        query.setRows(rowsNum);
        // 执行查询操作
        QueryResponse response = readSolrServer.query(query);
        if (response != null) {
            return response;
        }
        return null;
    }

    /**
     * 根据相应查询条件，查询Solr数量
     * 
     * @param ql
     * @param fql
     * @param sortMap
     * @param showFields
     * @param startRow
     * @param rowsNum
     * @return
     * @throws SolrServerException
     */
    public Long queryForCount(String ql, String fql, String sortMap, String showFields, int startRow, int rowsNum)
            throws SolrServerException {
        Long resultCount = 0L;
        QueryResponse response = queryForResponse(ql, fql, sortMap, showFields, startRow, rowsNum);
        if (response != null) {
            resultCount = response.getResults().getNumFound();
        }
        return resultCount;
    }

    /**
     * 根据对象查询列表
     * 
     * @param ql q模糊查询（commodityCode:* OR commodityReviewId:* AND shopFlag:*）
     * @param fql fq精准查询（commodityCode:00000000012345678 OR commodityReviewId:12345678 AND shopFlag:1）
     * @param sortMap key 列名；value 升/降序
     * @param showFields 显示指定的筛选字段 commodityCode,commodityReviewId
     * @param startRow 起始页 0
     * @param rowsNum 每页显示行数 10
     * @param entityClass 实体类型
     * @return
     * @throws SolrServerException
     */
    public <T> List<T> queryForList(String ql, String fql, String sortMap, String showFields, int startRow, int rowsNum,
            Class<T> entityClass) throws SolrServerException {
        List<T> list = new ArrayList<T>();
        QueryResponse response = queryForResponse(ql, fql, sortMap, showFields, startRow, rowsNum);
        if (response != null) {
            list = response.getBeans(entityClass);
        }
        return list;
    }

    /**
     * 根据对象查询列表
     * 
     * @param ql q模糊查询（commodityCode:* OR commodityReviewId:* AND shopFlag:*）
     * @param fql fq精准查询（commodityCode:00000000012345678 OR commodityReviewId:12345678 AND shopFlag:1）
     * @return
     */
    public <T> List<T> queryForList(String ql, String fql, Class<T> entityClass) throws SolrServerException {
        return queryForList(ql, fql, null, "", 0, 10, entityClass);
    }

    public <T> List<T> queryForListByCore(String ql, String fql, Class<T> entityClass) throws SolrServerException {
        // 查询a和b下面的数据，
        HttpSolrServer sc = new HttpSolrServer("http://10.27.43.43:9080/solr/labels");
        SolrQuery query = new SolrQuery();
        query.set("q", "*:*");
        //String shards = "http://10.27.43.45:9080/solr/labels,http://10.27.43.43:9080/solr/labels";
        //query.set("shards", shards);// 设置shard
        query.addSort("labelId", ORDER.asc);
        QueryResponse response = sc.query(query);

        // ModifiableSolrParams solrParams = new ModifiableSolrParams();
        // solrParams.set("q", "*:*");
        // solrParams.set("shards", shards);//设置shard
        // solrParams.set("sort", "labelId","desc");
        // solrParams.set("q.op", "AND");//设置查询关系
        // solrParams.set("fl", "*,score");//设置过滤

        // QueryResponse response = sc.query(solrParams);
        System.out.println("命中数量：" + response.getResults().getNumFound());
        List<T> list = null;
        if (response != null) {
            list = response.getBeans(entityClass);
        }
        return list;
    }

    /**
     * 根据对象查询列表
     * 
     * @param ql q模糊查询（commodityCode:* OR commodityReviewId:* AND shopFlag:*）
     * @param fql fq精准查询（commodityCode:00000000012345678 OR commodityReviewId:12345678 AND shopFlag:1）
     * @param entityClass
     * @param startRow
     * @param rowsNum
     * @return
     * @throws SolrServerException
     */
    public <T> T queryForObject(String ql, String fql, Class<T> entityClass) throws SolrServerException {
        List<T> list = queryForList(ql, fql, entityClass);
        // 如果结果集中存在多条记录，返回第一条
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 存储或更新
     * 
     * @param list 要存储的列表
     * @param entityClass 列表类型
     * @return 0：保存失败 1：保存成功
     */
    public <T> int saveOrUpdate(List<T> list) {
        try {
            if (list != null && list.size() > 0) {
                writeSolrServer.addBeans(list);
            }
        } catch (Exception e) {
            return 0;
        }
        return 1;
    }
    
    /**
     * 保存或更新数据
     * 
     * @param obj 要存储的对象
     * @return 0：保存失败 1：保存成功
     */
    public int saveBySID(List<SolrInputDocument> sidList) {
        try {
            if (sidList.size()>0) {
                writeSolrServer.add(sidList);
            }
        } catch (Exception e) {
            return 0;
        }
        return 1;
    }
    
    public int saveBySID(SolrInputDocument sid) {
        try {
            if (sid != null) {
                writeSolrServer.add(sid);
            }
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        }
        return 1;
    }
    

    /**
     * 保存或更新数据
     * 
     * @param obj 要存储的对象
     * @return 0：保存失败 1：保存成功
     */
    public int saveOrUpdate(Object obj) {
        try {
            if (obj != null) {
                writeSolrServer.addBean(obj);
            }
        } catch (Exception e) {
            return 0;
        }
        return 1;
    }
    

    /**
     * 删除全部Solr数据，不可以恢复；不建议使用，请谨慎操作。
     * 
     * @return 0：删除失败 1：删除成功
     */
    @Deprecated
    public int removeAll() {
        try {
            // 把查询出来的数据全部删除
            writeSolrServer.deleteByQuery("*:*");
            writeSolrServer.commit();
            writeSolrServer.optimize(false, true, 2);
        } catch (Exception e) {
            return 0;
        }
        return 1;
    }

    /**
     * 根据相关条件删除数据
     * 
     * @param ql 删除条件（commodityCode:00000000012345678 OR commodityReviewId:12345678 AND shopFlag:1）
     * @return 0：删除失败 1：删除成功
     */
    public int removeByQuery(String ql) {
        try {
            if (!StringUtils.isNullOrEmpty(ql)) {
                // 删除操作
                writeSolrServer.deleteByQuery(ql);
            }
        } catch (Exception e) {
            return 0;
        }
        return 1;
    }

    /**
     * 根据ID集合进行删除
     * 
     * @param ql 删除条件（commodityCode:00000000012345678 OR commodityReviewId:12345678 AND shopFlag:1）
     * @return 0：删除失败 1：删除成功
     */
    public int deleteById(List<String> ids) {
        try {
            if (ids.size() > 0 && null != ids) {
                // 删除操作
                writeSolrServer.deleteById(ids);
            }
        } catch (Exception e) {
            return 0;
        }
        return 1;
    }

    /**
     * 根据ID进行删除
     * 
     * @param ql 删除条件（commodityCode:00000000012345678 OR commodityReviewId:12345678 AND shopFlag:1）
     * @return 0：删除失败 1：删除成功
     */
    public int deleteById(String id) {
        try {
            if (!StringUtils.isNullOrEmpty(id)) {
                // 删除操作
                writeSolrServer.deleteById(id);
            }
        } catch (Exception e) {
            return 0;
        }
        return 1;
    }

    /**
     * 索引提交
     * 
     * @return 0：提交失败 1：提交成功
     */
    public int commit() {
        try {
            // commit提交
            writeSolrServer.commit();
        } catch (Exception e) {
            return 0;
        }
        return 1;
    }

    /**
     * 索引优化,保留一个索引分片
     * 
     * @return 0：优化失败 1：优化成功
     */
    public int optimizeOne() {
        try {
            // 优化操作
            writeSolrServer.optimize();
        } catch (Exception e) {
            return 0;
        }
        return 1;
    }

    /**
     * 索引优化，保留2个索引分片
     * 
     * @return 0：优化失败 1：优化成功
     */
    public int optimizeTwo() {
        try {
            // 优化操作
            writeSolrServer.optimize(false, true, 2);
        } catch (Exception e) {
            return 0;
        }
        return 1;
    }

    public QueryResponse queryForResponseByCore(String ql, String fql, String sortFields, String showFields,
            String facetField, int startRow, int rowsNum) {
        SolrQuery query = new SolrQuery();
        // q查询
        if (StringUtils.isNullOrEmpty(ql)) {
            ql = "*:*";
        }
        query.setQuery(ql);
        // fq查询
        if (StringUtils.isNullOrEmpty(ql)) {
            fql = "";
        }
        query.setFilterQueries(fql);
        // 排序
        String[] sorts = sortFields.split(",");
        for (String str : sorts) {
            if (!StringUtils.isNullOrEmpty(str)) {
                String sortField = str.substring(0, str.lastIndexOf("_"));
                String sortType = str.substring(str.lastIndexOf("_") + 1);
                if (!StringUtils.isNullOrEmpty(sortField) && !StringUtils.isNullOrEmpty(sortType)) {
                    if (("DESC").equalsIgnoreCase(sortType)) {
                        query.addSort(sortField, ORDER.desc);
                    } else {
                        query.addSort(sortField, ORDER.asc);
                    }
                }
            }
        }
        // 如果有指定的筛选字段则设置fields
        if (!StringUtils.isNullOrEmpty(showFields)) {
            query.setFields(showFields);
        }
        if (!StringUtils.isNullOrEmpty(facetField)) {
            query.setFacet(true);
            query.set("facet.field", facetField);
        }
        // 起始页
        query.setStart(startRow);
        // 每页行数
        query.setRows(rowsNum);
        // query.set("", "");
        // 执行查询操作
        QueryResponse response;
        try {
            response = readSolrServer.query(query);
            if (response != null) {
                return response;
            }
        } catch (SolrServerException e) {
            return null;
        }
        return null;
    }

    protected HttpSolrServer getReadSolrServer() {
        return readSolrServer;
    }

    protected void setReadSolrServer(HttpSolrServer readSolrServer) {
        this.readSolrServer = readSolrServer;
    }

    protected HttpSolrServer getWriteSolrServer() {
        return writeSolrServer;
    }

    protected void setWriteSolrServer(HttpSolrServer writeSolrServer) {
        this.writeSolrServer = writeSolrServer;
    }
}
