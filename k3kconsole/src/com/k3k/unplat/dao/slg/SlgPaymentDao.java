package com.k3k.unplat.dao.slg;

import com.k3k.unplat.dao.slg.rowmapper.SlgOrderRowmapper;
import com.k3k.unplat.dao.slg.rowmapper.SlgPaymentRowmapper;
import com.k3k.unplat.entity.slg.SlgOrder;
import com.k3k.unplat.entity.slg.SlgPaymentStatis;
import com.k3k.unplat.utils.Page;
import com.k3k.unplat.utils.SlgUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jef on 2016/12/9.
 */
@Repository
public class SlgPaymentDao {
    private static final Log LOGGER = LogFactory.getLog(SlgPaymentDao.class);

    @Resource
    private JdbcTemplate jdbcTemplate;

    private static final String SQL_NEW_PAY_USER = "SELECT  count(distinct a.uid) from cokdb1.payment_log a INNER JOIN cokdb1.userprofile b on a.uid = b.uid where a.uid not in (select p.uid from cokdb1.payment_log p where p.uid = uid  ";
    private static final String SQL_ORDER_NUM = "SELECT count(a.orderId) from cokdb1.payment_log a INNER JOIN cokdb1.userprofile b on a.uid = b.uid where 1=1 and a.status = 1";
    private static final String SQL_ALL_MONEY = "select  sum(a.money) from cokdb1.payment_log a INNER JOIN cokdb1.userprofile b on a.uid = b.uid where 1=1 and a.status = 1 ";
    private static final String SQL_ALL_ACCOUNT = "select count(distinct a.uid) from cokdb1.payment_log a INNER JOIN cokdb1.userprofile b on a.uid = b.uid where 1=1 and a.status = 1 ";
    private static final String SQL_ALL_LOGIN = "select count(*) from cokdb1.userprofile where lastOnlineTime >= regTime ";
    private static final String SQL_USER_PAY_LIST = "select a.uid,sum(a.money) as smoney,b.name,c.level from cokdb1.payment_log a INNER JOIN cokdb1.userprofile b on a.uid = b.uid INNER JOIN cokdb1.user_building c on a.uid = c.uid and c.itemId = 400000 where 1=1 ";
    private static final String SQL_USER_ORDER_LIST = "select a.uid,a.orderId,a.money,a.orderTime,b.name,c.uuid from cokdb1.payment_log a INNER JOIN cokdb1.userprofile b on a.uid = b.uid LEFT JOIN cokdb_global.account_new c ON a.uid = c.gameUid where 1=1 and a.status = 1  ";
    private static final String SQL_USER_ORDER_ALL = "select count(c.orderId) from (select a.uid,a.orderId,a.money,a.orderTime,b.name from cokdb1.payment_log a INNER JOIN cokdb1.userprofile b on a.uid = b.uid where 1=1 and a.status = 1 ";


    public Page<SlgPaymentStatis> findPage(Page<SlgPaymentStatis> page, SlgPaymentStatis filter, int checkType) {
        List<SlgPaymentStatis> results = new ArrayList<SlgPaymentStatis>();
        DecimalFormat df = new DecimalFormat("#0.00");
        long day = filter.getEdate();
        String unionInfo = SlgUtils.getUnionInfo("b", filter.getEnter(), filter.getUnion(), filter.getPlat());
        LOGGER.info("UnionInfo: " + unionInfo);
        LOGGER.info("findPage Sdate:"+filter.getSdate()+" Edate:"+filter.getEdate());

        try {
            if (checkType == 1) {
                LOGGER.info("Check ");
                StringBuilder newSql = new StringBuilder(SQL_NEW_PAY_USER); //新增用户
                StringBuilder ordernumSql = new StringBuilder(SQL_ORDER_NUM); //订单数
                StringBuilder moneySql = new StringBuilder(SQL_ALL_MONEY);   //总金额
                StringBuilder accountSql = new StringBuilder(SQL_ALL_ACCOUNT); //充值账号数
                StringBuilder loginSql = new StringBuilder(SQL_ALL_LOGIN); //登录账号数


                newSql.append(" and p.orderTime < "+ day + " ) and a.orderTime < " + day/1000 + unionInfo);
                ordernumSql.append(" and a.orderTime < " + day/1000 + unionInfo);
                moneySql.append(" and a.orderTime < " + day/1000 + unionInfo);
                accountSql.append(" and a.orderTime < " + day/1000+ unionInfo);
                loginSql.append(" and regTime <" + day+ " and lastOnlineTime < " + day + SlgUtils.getUnionInfo("", filter.getEnter(), filter.getUnion(), filter.getPlat()));

                if(filter.getSdate() != 0L){
                    newSql.append(" and a.orderTime >= "+filter.getSdate()/1000);
                    ordernumSql.append(" and a.orderTime >=  "+filter.getSdate()/1000);
                    moneySql.append(" and a.orderTime >=  "+filter.getSdate()/1000);
                    accountSql.append(" and a.orderTime >=  "+filter.getSdate()/1000);
                    loginSql.append(" and regTime >= "+filter.getSdate()+" and lastOnlineTIme >= "+filter.getSdate());
                }

                if(StringUtils.isNotBlank(filter.getUsername())){
                    newSql.append(" and b.name like '%"+filter.getUsername()+"%'");
                    ordernumSql.append(" and b.name like '%"+filter.getUsername()+"%'");
                    moneySql.append(" and b.name like '%"+filter.getUsername()+"%'");
                }

                LOGGER.info("New User SQL: " + newSql);
                LOGGER.info("All Order SQL: " + ordernumSql);
                LOGGER.info("All Money SQL: " + moneySql);
                LOGGER.info("All Login SQL: " + loginSql);
                LOGGER.info("All Account SQL: " + accountSql);

                try {
                    int newuser = jdbcTemplate.queryForInt(newSql.toString());
                    int ordernum = jdbcTemplate.queryForInt(ordernumSql.toString());
                    String allmoeny = jdbcTemplate.queryForObject(moneySql.toString(), String.class);
                    int accountnum = jdbcTemplate.queryForInt(accountSql.toString());
                    int loginnum = jdbcTemplate.queryForInt(loginSql.toString());
                    SlgPaymentStatis slg = new SlgPaymentStatis();
                    slg.setNewnum(newuser);
                    slg.setOrdernum(ordernum);
                    Double money = 0D ;
                    if(!StringUtils.isBlank(allmoeny)&&allmoeny!=null){
                        money = Double.valueOf(allmoeny);
                    }
                    slg.setAllmoney(money);
                    if (accountnum == 0) {
                        slg.setArppu("0");
                    } else {
                        slg.setArppu(df.format(money / ((double) accountnum)));
                    }
                    if (loginnum == 0) {
                        slg.setPayrate("0");
                        slg.setArpu("0");
                    } else {
                        slg.setPayrate(df.format(((double) accountnum) / ((double) loginnum)));
                        slg.setArpu(df.format(money / ((double) loginnum)));
                    }
                    slg.setChannel(SlgUtils.getUnion(filter.getEnter()));
                    slg.setEntry(SlgUtils.getEntry(filter.getUnion()));
                    slg.setPlatform(SlgUtils.getPlatform(filter.getPlat()));
                    results.add(slg);
                    page.setTotalRows(1);
                    page.setResults(results);
                } catch (Exception e) {
                    LOGGER.error("充值统计"+e);
                }
            } else {
                StringBuilder paylistSql = new StringBuilder(SQL_USER_PAY_LIST); //充值排行
                paylistSql.append(" and a.orderTime>= "+ filter.getSdate()/1000 +" and a.orderTime< " + day/1000 + unionInfo + " group by a.uid order by smoney desc limit 50");
                LOGGER.info("Pay List SQL: " + paylistSql);

                int rank = 1;
                List<SlgPaymentStatis> payList = jdbcTemplate.query(paylistSql.toString(), new SlgPaymentRowmapper());
                LOGGER.info("Check Type:" + checkType);
                for (int j = 0; j < payList.size(); j++) {
                    payList.get(j).setRank(rank);
                    rank += 1;
                }
                LOGGER.info("Pay list Size: " + payList.size());
                //分页
                int startIndex = page.getStartIndex();
                int pageSize = page.getPageSize();
                int pageNo = (page.getStartIndex() + 10) / 10;
                LOGGER.info("Start Index: " + startIndex);
                LOGGER.info("Page No.: " + pageNo);
                List<SlgPaymentStatis> subResults = payList;
                if (pageSize * pageNo < payList.size()) {
                    subResults = payList.subList((pageNo - 1) * pageSize, pageSize * pageNo);
                } else {
                    subResults = payList.subList((pageNo - 1) * pageSize, payList.size());
                }
                page.setTotalRows(payList.size());
                page.setResults(subResults);
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return page;
    }

    public Page<SlgOrder> findOrderPage(Page<SlgOrder> page, SlgOrder filter) {
        LOGGER.info("findOrderPage Sdate:"+filter.getSdate()+" Edate:"+filter.getEdate());

        StringBuilder resultSql = new StringBuilder(SQL_USER_ORDER_LIST);
        StringBuilder findall = new StringBuilder(SQL_USER_ORDER_ALL);
        if (filter.getSdate() != 0L) {
            resultSql.append(" and a.orderTime >= " + filter.getSdate() / 1000);
            findall.append(" and a.orderTime >= " + filter.getSdate() / 1000);
            LOGGER.info("Search sDate Info: " + filter.getSdate() / 1000);
        }
        if (filter.getEdate() != 0L) {
            resultSql.append(" and a.orderTime < " + filter.getEdate() / 1000);
            findall.append(" and a.orderTime < " + filter.getEdate() / 1000);
            LOGGER.info("Search eDate Info: " + filter.getEdate() / 1000);
        }
        String extra_sql = SlgUtils.getUnionInfo("b", filter.getEnter(), filter.getUnion(), filter.getPlat());
        LOGGER.info("Union info:" + extra_sql);
        resultSql.append(extra_sql);
        findall.append(extra_sql);

        if(StringUtils.isNotBlank(filter.getUsername())){
            resultSql.append((" and b.name like '%"+filter.getUsername()+"%'"));
            findall.append((" and b.name like '%"+filter.getUsername()+"%'"));
            LOGGER.info("Search name Info: " + filter.getUsername());
        }

        resultSql.append(" order by a.orderTime desc limit ?,? ");
        findall.append(" )c");
        LOGGER.info("resultSQL : " + resultSql);
        LOGGER.info("findALL : " + findall);
        try {
            int totalrows = jdbcTemplate.queryForInt(findall.toString());
            LOGGER.info("Total Row:" + totalrows);
            List<SlgOrder> results = jdbcTemplate.query(resultSql.toString(), new Object[]{page.getStartIndex(), page.getPageSize()}, new SlgOrderRowmapper());
            LOGGER.info("Result Size:" + results.size());
            for(int i = 0 ;i<results.size();i++){
                String uuid = results.get(i).getUuid();
                if(StringUtils.isNotBlank(uuid)&&uuid!=null){
                    results.get(i).setUuid(uuid.substring(7));
                }
            }
            page.setTotalRows(totalrows);
            page.setResults(results);
        } catch (Exception e) {
            LOGGER.error("订单列表:"+e.getMessage());
        }
        return page;
    }

}
