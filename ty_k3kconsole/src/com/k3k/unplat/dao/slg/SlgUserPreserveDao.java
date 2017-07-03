package com.k3k.unplat.dao.slg;

import com.k3k.unplat.entity.slg.SlgUserPreserve;
import com.k3k.unplat.utils.Page;

import com.k3k.unplat.utils.SlgUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by jef on 2016/12/8.
 */
@Repository
public class SlgUserPreserveDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    private static final Log LOGGER = LogFactory.getLog(SlgUserPreserveDao.class);

    DecimalFormat df = new DecimalFormat("######0.00");

    private static final String SQL_USER_PRESERVE_FIND_PAGE = "select count(distinct a.uid)  from  cokdb1.stat_login_";
    private static final String SQL_USER_REG_FIND_PAGE = "select count(distinct a.uid) from  cokdb1.stat_login_";


    public Page<SlgUserPreserve> findPage(Page<SlgUserPreserve> page, SlgUserPreserve filter) {
        List<SlgUserPreserve> results = new ArrayList<SlgUserPreserve>();

        int startIndex = page.getStartIndex();
        int pageSize = page.getPageSize();
        int size =(int) ((filter.getEdate() - filter.getSdate())/86400000L);
        long day = filter.getSdate();
        if(size>pageSize){
            long sday = day + startIndex*86400000L;
            if(day < sday){
                day = sday;
            }
            long eday = day+pageSize*86400000L;
            if(filter.getEdate() > eday){
                filter.setEdate(eday);
            }
        }

        try {
            while (day < filter.getEdate()) {
                StringBuilder userregSql = new StringBuilder(SQL_USER_REG_FIND_PAGE);
                StringBuilder userlogSql = new StringBuilder(SQL_USER_REG_FIND_PAGE);

                StringBuilder active2 = new StringBuilder(SQL_USER_PRESERVE_FIND_PAGE);
                StringBuilder active3 = new StringBuilder(SQL_USER_PRESERVE_FIND_PAGE);
                StringBuilder active4 = new StringBuilder(SQL_USER_PRESERVE_FIND_PAGE);
                StringBuilder active5 = new StringBuilder(SQL_USER_PRESERVE_FIND_PAGE);
                StringBuilder active6 = new StringBuilder(SQL_USER_PRESERVE_FIND_PAGE);
                StringBuilder active7 = new StringBuilder(SQL_USER_PRESERVE_FIND_PAGE);

                //根据查询的日期判断该查询的数据库表 如:日期为:2016_12_02 ,所查的表为stat_login_2016_12
                String dbInfo = SlgUtils.getDataBase(day);
                String unionInfo = SlgUtils.getUnionInfo("b", filter.getEnter(), filter.getUnion(), filter.getPlat());

                userregSql.append(dbInfo + " a INNER JOIN cokdb1.userprofile b on a.uid = b.uid where a.regTime >= " + day);
                userregSql.append(" and a.regTime < " + (day + 86400000L));
                userregSql.append(unionInfo);

                userlogSql.append(dbInfo + " a INNER JOIN cokdb1.userprofile b on a.uid = b.uid where a.time >= " + day);
                userlogSql.append(" and a.time < " + (day + 86400000L));
                userlogSql.append(unionInfo);

                active2.append(dbInfo + " a INNER JOIN cokdb1.userprofile b on a.uid = b.uid where a.regTime >= " + day);
                active2.append(" and a.regTime < " + (day + 86400000L));
                active2.append(" and a.time >= " + (day + 86400000L));
                active2.append(" and a.time < " + (day + 86400000L * 2));
                active2.append(unionInfo);

                active3.append(dbInfo + " a INNER JOIN cokdb1.userprofile b on a.uid = b.uid where a.regTime >= " + day);
                active3.append(" and a.regTime < " + (day + 86400000L));
                active3.append(" and a.time >= " + (day + 86400000L * 2));
                active3.append(" and a.time < " + (day + 86400000L * 3));
                active3.append(unionInfo);

                active4.append(dbInfo + " a INNER JOIN cokdb1.userprofile b on a.uid = b.uid where a.regTime >= " + day);
                active4.append(" and a.regTime < " + (day + 86400000L));
                active4.append(" and a.time >= " + (day + 86400000L * 3));
                active4.append(" and a.time < " + (day + 86400000L * 4));
                active4.append(unionInfo);

                active5.append(dbInfo + " a INNER JOIN cokdb1.userprofile b on a.uid = b.uid where a.regTime >= " + day);
                active5.append(" and a.regTime < " + (day + 86400000L));
                active5.append(" and a.time >= " + (day + 86400000L * 4));
                active5.append(" and a.time < " + (day + 86400000L * 5));
                active5.append(unionInfo);

                active6.append(dbInfo + " a INNER JOIN cokdb1.userprofile b on a.uid = b.uid where a.regTime >= " + day);
                active6.append(" and a.regTime < " + (day + 86400000L));
                active6.append(" and a.time >= " + (day + 86400000L * 5));
                active6.append(" and a.time < " + (day + 86400000L * 6));
                active6.append(unionInfo);

                active7.append(dbInfo + " a INNER JOIN cokdb1.userprofile b on a.uid = b.uid where a.regTime >= " + day);
                active7.append(" and a.regTime < " + (day + 86400000L));
                active7.append(" and a.time >= " + (day + 86400000L * 6));
                active7.append(" and a.time < " + (day + 86400000L * 7));
                active7.append(unionInfo);

                LOGGER.info("userregSql: " + userregSql.toString());
                int regnum = jdbcTemplate.queryForInt(userregSql.toString());
                int lognum = jdbcTemplate.queryForInt(userlogSql.toString());
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(day));
                SlgUserPreserve slg = new SlgUserPreserve();
                slg.setRegnum(regnum);
                slg.setLognum(lognum);
                slg.setDay(date);
                if (slg.getRegnum() == 0) {
                    slg.setRate2("0");
                    slg.setRate3("0");
                    slg.setRate4("0");
                    slg.setRate5("0");
                    slg.setRate6("0");
                    slg.setRate7("0");
                } else {
                    int active2num = jdbcTemplate.queryForInt(active2.toString());
                    int active3num = jdbcTemplate.queryForInt(active3.toString());
                    int active4num = jdbcTemplate.queryForInt(active4.toString());
                    int active5num = jdbcTemplate.queryForInt(active5.toString());
                    int active6num = jdbcTemplate.queryForInt(active6.toString());
                    int active7num = jdbcTemplate.queryForInt(active7.toString());
                    if (active2num == 0) {
                        slg.setRate2("0");
                    } else {
                        double rate2 = (double) active2num / (double) regnum;
                        slg.setRate2(active2num + " (" + df.format(rate2 * 100) + "%" + ")");
                    }
                    if (active3num == 0) {
                        slg.setRate3("0");
                    } else {
                        double rate3 = (double) active3num / (double) regnum;
                        slg.setRate3(active3num + " (" + df.format(rate3 * 100) + "%" + ")");
                    }
                    if (active4num == 0) {
                        slg.setRate4("0");
                    } else {
                        double rate4 = (double) active4num / (double) regnum;
                        slg.setRate4(active4num + " (" + df.format(rate4 * 100) + "%" + ")");
                    }
                    if (active5num == 0) {
                        slg.setRate5("0");
                    } else {
                        double rate5 = (double) active5num / (double) regnum;
                        slg.setRate5(active5num + " (" + df.format(rate5 * 100) + "%" + ")");
                    }
                    if (active6num == 0) {
                        slg.setRate6("0");
                    } else {
                        double rate6 = (double) active6num / (double) regnum;
                        slg.setRate6(active6num + " (" + df.format(rate6 * 100) + "%" + ")");
                    }
                    if (active7num == 0) {
                        slg.setRate7("0");
                    } else {
                        double rate7 = (double) active7num / (double) regnum;
                        slg.setRate7(active7num + " (" + df.format(rate7 * 100) + "%" + ")");
                    }
                }
                slg.setEntry(SlgUtils.getEntry(filter.getUnion()));
                slg.setPlatform(SlgUtils.getPlatform(filter.getPlat()));
                slg.setChannel(SlgUtils.getUnion(filter.getEnter()));
                results.add(slg);
                day += 86400000L;
            }
            page.setTotalRows(size);
            page.setResults(results);
        } catch (Exception e) {
            LOGGER.error("用户留存" + e.getMessage());
        }
        return page;
    }


}
