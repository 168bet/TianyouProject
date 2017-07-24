package com.k3k.unplat.dao.slg;

import com.k3k.unplat.entity.slg.SlgAccountStatis;
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
 * Created by jef on 2016/12/7.
 */
@Repository
public class SlgAccountDao {

    private static final Log LOGGER = LogFactory.getLog(SlgAccountDao.class);

    @Resource
    private JdbcTemplate jdbcTemplate;

    private static final String SQL_ACCOUNT_FIND_PAGE = "select count(*)  from  cokdb1.userprofile   where 1=1 ";
    private static final String SQL_ACCOUNT_LOGIN_FIND_PAGE = "select count(distinct a.uid)  from  cokdb1.stat_login_";
    private static final SimpleDateFormat df =new SimpleDateFormat("yyyy_MM");

    public Page<SlgAccountStatis> findPage(Page<SlgAccountStatis> page, SlgAccountStatis filter) {
        List<SlgAccountStatis> results = new ArrayList<SlgAccountStatis>();

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

        while (day < filter.getEdate()) {
            StringBuilder accountSql = new StringBuilder(SQL_ACCOUNT_FIND_PAGE);
            StringBuilder accountSql_old = new StringBuilder(SQL_ACCOUNT_FIND_PAGE);
            StringBuilder loginSql = new StringBuilder(SQL_ACCOUNT_LOGIN_FIND_PAGE);

            //根据查询的日期判断该查询的数据库表 如:日期为:2016_12_02 ,所查的表为stat_login_2016_12
            String dbInfo = SlgUtils.getDataBase(day);

            String unioninfo = SlgUtils.getUnionInfo("",filter.getEnter(),filter.getUnion(),filter.getPlat());
            accountSql.append(" and regTime >= " + day );
            accountSql.append(" and regTime < " + (day+86400000L) + unioninfo);
            accountSql_old.append(" and regTime < " + day + unioninfo);
            loginSql.append(dbInfo + " a INNER JOIN cokdb1.userprofile b on a.uid = b.uid where a.time >= " + day);
            loginSql.append(" and a.time < " + (day+86400000L) + SlgUtils.getUnionInfo("b",filter.getEnter(),filter.getUnion(),filter.getPlat()));

            SlgAccountStatis slg = new SlgAccountStatis();
          //  int accountnum = jdbcTemplate.queryForInt(accountSql.toString());
          //  int regnum = jdbcTemplate.queryForInt(accountSql.toString());
          //  int loginnum = jdbcTemplate.queryForInt(loginSql.toString());
            int activenum = jdbcTemplate.queryForInt(loginSql.toString());
            int newusernum = jdbcTemplate.queryForInt(accountSql.toString());
            int usernum = jdbcTemplate.queryForInt(accountSql_old.toString());

            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(day));
            slg.setActivenum(activenum);
            slg.setNewusernum(newusernum);
            slg.setUsernum(usernum);
            slg.setDay(date);
            slg.setChannel(SlgUtils.getUnion(filter.getEnter()));
            slg.setPlatform(SlgUtils.getPlatform(filter.getPlat()));
            slg.setEntry(SlgUtils.getEntry(filter.getUnion()));
            results.add(slg);
            day += 86400000L;
        }

        page.setTotalRows(size);
        page.setResults(results);
        return page;
    }

}
