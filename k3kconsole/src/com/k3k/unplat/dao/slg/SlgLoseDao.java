package com.k3k.unplat.dao.slg;

import com.k3k.unplat.entity.slg.SlgLose;
import com.k3k.unplat.utils.Page;
import com.k3k.unplat.utils.SlgUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by jef on 2017/2/27.
 */
@Repository
public class SlgLoseDao {
    private static final Log LOGGER = LogFactory.getLog(SlgLoseDao.class);
    DecimalFormat df = new DecimalFormat("######0.00");
    private static final SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");


    @Resource
    private JdbcTemplate jdbcTemplate;

    private static final String SQL_REG_NUM = "select count(distinct a.uid) from cokdb1.stat_login_";
    private static final String SQL_LOSS_NUM = "select count(uid) from (select distinct a.uid from cokdb1.stat_login_";

    public Page<SlgLose> findPage(Page<SlgLose> page, SlgLose filter) {
        List<SlgLose> results = new ArrayList<SlgLose>();

        int startIndex = page.getStartIndex();
        int pageSize = page.getPageSize();
        int size = (int) ((filter.getEdate() - filter.getSdate()) / 86400000L);
        long day = filter.getSdate();
        if (size > pageSize) {
            long sday = day + startIndex * 86400000L;
            if (day < sday) {
                day = sday;
            }
            long eday = day + pageSize * 86400000L;
            if (filter.getEdate() > eday) {
                filter.setEdate(eday);
            }
        }

        LOGGER.info("太守府等级:" + filter.getBuildinglevel());
        try {
            //当天日期
            Calendar instance = Calendar.getInstance();
            instance.add(Calendar.DATE, 1);
            String tdate = dff.format(instance.getTime());
            long today = dff.parse(tdate).getTime();

            while (day < filter.getEdate()) {
                StringBuilder regSql = new StringBuilder(SQL_REG_NUM);

                String dbInfo = SlgUtils.getDataBase(day);
                String unionInfo = SlgUtils.getUnionInfo("b", filter.getEnter(), 0, filter.getPlat());

                regSql.append(dbInfo + " a INNER JOIN cokdb1.userprofile b on a.uid = b.uid where a.regTime >= " + day);
                regSql.append(" and a.regTime < " + (day + 86400000L));
                regSql.append(unionInfo);

                SlgLose slg = new SlgLose();
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(day));
                int regnum = jdbcTemplate.queryForInt(regSql.toString());
                slg.setRegnum(regnum);
                slg.setChannel(SlgUtils.getUnion(filter.getEnter()));
                slg.setDate(date);
                if (regnum == 0) {
                    slg.setLoserate2("0");
                    slg.setLoserate3("0");
                    slg.setLoserate4("0");
                    slg.setLoserate5("0");
                    slg.setLoserate6("0");
                    slg.setLoserate7("0");
                    results.add(slg);
                    day += 86400000L;
                    continue;
                } else {
                    int loss2num ;
                    int loss3num ;
                    int loss4num ;
                    int loss5num ;
                    int loss6num ;
                    int loss7num ;
                    if (day + 86400000L < today) {
                        String sql2 = sqlCreate(2, SQL_LOSS_NUM, filter, day);
                        loss2num = jdbcTemplate.queryForInt(sql2);
                        double rate2 = (double) (loss2num) / (double) regnum;
                        slg.setLoserate2(loss2num + " (" + df.format(rate2 * 100) + "%" + ")");
                        if (rate2 == 1) {
                            slg.setLoserate3("0");
                            slg.setLoserate4("0");
                            slg.setLoserate5("0");
                            slg.setLoserate6("0");
                            slg.setLoserate7("0");
                            results.add(slg);
                            day += 86400000L;
                            continue;
                        }
                    } else {
                        slg.setLoserate2(" ");
                        slg.setLoserate3(" ");
                        slg.setLoserate4(" ");
                        slg.setLoserate5(" ");
                        slg.setLoserate6(" ");
                        slg.setLoserate7(" ");
                        results.add(slg);
                        day += 86400000L;
                        continue;
                    }

                    if (day + 86400000L * 2 < today) {
                        String sql3 = sqlCreate(3, SQL_LOSS_NUM, filter, day);
                        loss3num = jdbcTemplate.queryForInt(sql3);
                        double rate3 = (double) Math.abs(loss3num - loss2num) / (double) regnum;
                        slg.setLoserate3(Math.abs(loss3num - loss2num) + " (" + df.format(rate3 * 100) + "%" + ")");
                        if (loss3num == regnum) {
                            slg.setLoserate4("0");
                            slg.setLoserate5("0");
                            slg.setLoserate6("0");
                            slg.setLoserate7("0");
                            results.add(slg);
                            day += 86400000L;
                            continue;
                        }
                    } else {
                        slg.setLoserate3(" ");
                        slg.setLoserate4(" ");
                        slg.setLoserate5(" ");
                        slg.setLoserate6(" ");
                        slg.setLoserate7(" ");
                        results.add(slg);
                        day += 86400000L;
                        continue;
                    }

                    if (day + 86400000 * 3 < today) {
                        String sql4 = sqlCreate(4, SQL_LOSS_NUM, filter, day);
                        loss4num = jdbcTemplate.queryForInt(sql4);
                        double rate4 = (double) Math.abs(loss4num - loss3num) / (double) regnum;
                        slg.setLoserate4(Math.abs(loss4num - loss3num) + " (" + df.format(rate4 * 100) + "%" + ")");
                        if (loss4num == regnum) {
                            slg.setLoserate5("0");
                            slg.setLoserate6("0");
                            slg.setLoserate7("0");
                            results.add(slg);
                            day += 86400000L;
                            continue;
                        }
                    } else {
                        slg.setLoserate4(" ");
                        slg.setLoserate5(" ");
                        slg.setLoserate6(" ");
                        slg.setLoserate7(" ");
                        results.add(slg);
                        day += 86400000L;
                        continue;
                    }

                    if (day + 86400000L * 4 < today) {
                        String sql5 = sqlCreate(5, SQL_LOSS_NUM, filter, day);
                        loss5num = jdbcTemplate.queryForInt(sql5);
                        double rate5 = (double) Math.abs(loss5num - loss4num) / (double) regnum;
                        slg.setLoserate5(Math.abs(loss5num - loss4num) + " (" + df.format(rate5 * 100) + "%" + ")");
                        if (loss5num == regnum) {
                            slg.setLoserate6("0");
                            slg.setLoserate7("0");
                            results.add(slg);
                            day += 86400000L;
                            continue;
                        }
                    } else {
                        slg.setLoserate5(" ");
                        slg.setLoserate6(" ");
                        slg.setLoserate7(" ");
                        results.add(slg);
                        day += 86400000L;
                        continue;
                    }

                    if (day + 86400000L * 5 < today) {
                        String sql6 = sqlCreate(6, SQL_LOSS_NUM, filter, day);
                        loss6num = jdbcTemplate.queryForInt(sql6);
                        double rate6 = (double) Math.abs(loss6num-loss5num) / (double) regnum;
                        slg.setLoserate6(Math.abs(loss6num-loss5num) + " (" + df.format(rate6 * 100) + "%" + ")");
                        if(loss6num == regnum){
                            slg.setLoserate7("0");
                            results.add(slg);
                            day += 86400000L;
                            continue;
                        }
                    } else {
                        slg.setLoserate6(" ");
                        slg.setLoserate7(" ");
                        results.add(slg);
                        day += 86400000;
                        continue;
                    }

                    if (day + 86400000L * 6 < today) {
                        String sql7 = sqlCreate(7, SQL_LOSS_NUM, filter, day);
                        loss7num = jdbcTemplate.queryForInt(sql7);
                        double rate7 = (double) Math.abs(loss7num-loss6num) / (double) regnum;
                        slg.setLoserate7(Math.abs(loss7num-loss6num) + " (" + df.format(rate7 * 100) + "%" + ")");
                    } else {
                        slg.setLoserate7(" ");
                        results.add(slg);
                        day += 86400000L;
                        continue;
                    }
                    results.add(slg);
                    day += 86400000L;
                }
            }
            page.setTotalRows(size);
            page.setResults(results);
        } catch (Exception e) {
            LOGGER.error("流失率计算:" + e.getMessage());
        }
        return page;
    }

    public String sqlCreate(int num, String sql, SlgLose filter, long day) {
        String dbInfo = SlgUtils.getDataBase(day);

        String unionInfo = SlgUtils.getUnionInfo("b", filter.getEnter(), filter.getUnion(), filter.getPlat());

        StringBuilder result = new StringBuilder(sql);
        result.append(dbInfo + " a INNER JOIN cokdb1.userprofile b ON a.uid = b.uid INNER JOIN cokdb1.user_building c on a.uid = c.uid where a.uid NOT IN (select distinct uid from cokdb1.stat_login_" + dbInfo + " where time >=" + (day+86400000));
        result.append(" and time < " + (day + 86400000L * num) + " )");
        result.append(" and a.regTime >= " + day);
        result.append(" and a.regTime < " + (day + 86400000L));
        if (StringUtils.isNotBlank(filter.getBuildinglevel())) {
            result.append(" and c.itemId = 400000 and c.level = " + filter.getBuildinglevel());
        }
        result.append(unionInfo + ")d");
        LOGGER.info("Day"+num+" SQL:"+result.toString());
        return result.toString();

    }
}
