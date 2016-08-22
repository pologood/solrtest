package com.suning.solrtest.entity;

/**
 * 功能描述：保存数据到索引文件entity类
 * @author 15031496
 * @version 1.0.0
 */
import java.io.Serializable;
import java.util.Date;

import org.apache.solr.client.solrj.beans.Field;

public class SolrMember implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /** 会员编号 */
    @Field
    private String custNum;
    /** 易购登陆名 */
    @Field
    private String loginId;
    /** 门店卡号 */
    @Field
    private String cardId;
    /** 手机号 */
    @Field
    private String phone;
    /** 邮箱 */
    @Field
    private String email;
    
    /** 分表的序号 */
    @Field
    private String tableNum;
    
    /**最后更新时间（不创建Solr索引所以不用加@Field）*/
    private Date updateTime;
    /**执行状态(0未执行、1执行中、2执行完、3执行异常)（不创建Solr索引所以不用加@Field）*/
    private int executeStatus;
    
    public String getCustNum() {
        return custNum;
    }
    public void setCustNum(String custNum) {
        this.custNum = custNum;
    }
    public String getLoginId() {
        return loginId;
    }
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
    public String getCardId() {
        return cardId;
    }
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getTableNum() {
        return tableNum;
    }
    public void setTableNum(String tableNum) {
        this.tableNum = tableNum;
    }
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    public int getExecuteStatus() {
        return executeStatus;
    }
    public void setExecuteStatus(int executeStatus) {
        this.executeStatus = executeStatus;
    }
}
