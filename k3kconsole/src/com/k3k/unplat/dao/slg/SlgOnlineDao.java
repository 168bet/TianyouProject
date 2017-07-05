package com.k3k.unplat.dao.slg;


import com.k3k.unplat.dao.slg.rowmapper.SlgOnlineRowMapper;
import com.k3k.unplat.entity.slg.SlgOnline;
import com.k3k.unplat.utils.Page;
import com.k3k.unplat.utils.SlgUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by jef on 2016/12/8.
 */
@Repository
public class SlgOnlineDao {
    private static final Log LOGGER = LogFactory.getLog(SlgOnlineDao.class);

    @Resource
    private JdbcTemplate jdbcTemplate;

    private static final String SQL_ONLINE_USER = "select count(uid) as onlinenum ,time from (select a.uid, from_unixtime(a.time/1000,'%Y-%m-%d %H时') as time from cokdb1.stat_login_";
    private static final String SQL_ONLINE_DAY = "select count(uid) as onlinenum ,time from (select a.uid, from_unixtime(a.time/1000,'%Y年%m月%d日%H时') as time from cokdb1.stat_login_";

    public Page<SlgOnline> findPage(Page<SlgOnline> page, SlgOnline filter) {
        long day = Long.valueOf(filter.getDate());
        StringBuilder onlineSql = new StringBuilder(SQL_ONLINE_USER);

        String dbInfo = SlgUtils.getDataBase(day);
        String unionInfo = SlgUtils.getUnionInfo("b", filter.getEnter(), filter.getUnion(), filter.getPlat());

        onlineSql.append(dbInfo + " a INNER JOIN cokdb1.userprofile b ON a.uid = b.uid where 1=1 " + unionInfo);
        onlineSql.append(" and a.time >= " + day + " and a.time < " + (day + 86400000));
        onlineSql.append(" )c group by time order by time ");
        LOGGER.info("Online Num SQL:" + onlineSql);

        List<SlgOnline> results = jdbcTemplate.query(onlineSql.toString(), new SlgOnlineRowMapper());
        LOGGER.info("Result Size: " + results.size());
        page.setTotalRows(results.size());
        page.setResults(results);
        return page;
    }

    public Page<SlgOnline> findPageByDay(Page<SlgOnline> page, SlgOnline filter) {
        List<SlgOnline> results = new ArrayList<SlgOnline>();

        int startIndex = page.getStartIndex();
        int pageSize = page.getPageSize();
        int size =(int) ((filter.getEdate() - filter.getSdate())/86400000L);

        long day = filter.getSdate();
        if(size>pageSize){
            long sday = day + startIndex*86400000L;
            if(day < sday){
                day = sday;
                String sdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(day));
            }
            long eday = day+pageSize*86400000L;
            if(filter.getEdate() > eday){
                filter.setEdate(eday);
                String edate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(filter.getEdate()));
            }
        }

        try {
            while (day < filter.getEdate()) {
                StringBuilder sql = new StringBuilder(SQL_ONLINE_DAY);

                String dbInfo = SlgUtils.getDataBase(day);
                String unionInfo = SlgUtils.getUnionInfo("b", filter.getEnter(), filter.getUnion(), filter.getPlat());

                sql.append(dbInfo + " a INNER JOIN cokdb1.userprofile b ON a.uid = b.uid where 1=1 " + unionInfo);
                sql.append(" and a.time >= " + day + " and a.time < " + (day + 86400000L));
                sql.append(" )c group by time");

                List<SlgOnline> list = jdbcTemplate.query(sql.toString(), new SlgOnlineRowMapper());
                int min = 0;
                int max = 0;
                int all = 0;
                if (list.size() > 0) {
                    min = list.get(0).getOnlinenum();
                    max = list.get(0).getOnlinenum();
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getOnlinenum() < min) {
                            min = list.get(i).getOnlinenum();
                        }
                        if (list.get(i).getOnlinenum() > max) {
                            max = list.get(i).getOnlinenum();
                        }
                        all += list.get(i).getOnlinenum();
                    }
                }
                SlgOnline slg = new SlgOnline();
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(day));
                slg.setDate(date);
                slg.setMax(max);
                slg.setMin(min);
                slg.setAvg(all / 24);
                results.add(slg);
                day += 86400000L;
            }
        } catch (Exception e) {
            LOGGER.error("分时在线(按天):" + e.getMessage());
        }
        page.setTotalRows(size);
        page.setResults(results);
        return page;
    }

}
