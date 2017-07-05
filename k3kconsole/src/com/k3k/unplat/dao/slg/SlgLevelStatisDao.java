package com.k3k.unplat.dao.slg;

import com.k3k.unplat.entity.slg.SlgLevelStatis;
import com.k3k.unplat.utils.Page;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jef on 2016/12/8.
 */
@Repository
public class SlgLevelStatisDao {
    private static final Log LOGGER = LogFactory.getLog(SlgLevelStatisDao.class);

    @Resource
    private JdbcTemplate jdbcTemplate;

    private static final String SQL_USER_LEVEL_FIND_PAGE = "select count(*)  from cokdb1.userprofile  where 1=1 ";
    private static final String SQL_USER_POWER_FIND_PAGE = "select count(*) from cokdb1.playerinfo a inner join cokdb1.userprofile b on a.uid = b.uid where 1=1 ";
    private static final String SQL_BD_LEVEL_FIND_PAGE = "select count(*)  from cokdb1.user_building  where 1=1 ";

    public Page<SlgLevelStatis> findPage(Page<SlgLevelStatis> page, SlgLevelStatis filter) {
        List<SlgLevelStatis> results = new ArrayList<SlgLevelStatis>();
        long day = Long.valueOf(filter.getDate());
        int num = 0;

        if(filter.getType().equals("1")){
            //玩家等级统计
            for (int i = 1; i <= 50; i += 5) {
                StringBuilder userlevelSql = new StringBuilder(SQL_USER_LEVEL_FIND_PAGE);
                userlevelSql.append(" and regTime < " + (day + 86400000L));
                int slevel = i;
                int elevel = i + 4;
                userlevelSql.append(" and level >= " + slevel);
                userlevelSql.append(" and level <= " + elevel);

                SlgLevelStatis slguserlv = new SlgLevelStatis();
                slguserlv.setUsernum(jdbcTemplate.queryForInt(userlevelSql.toString()));
                slguserlv.setUserlv(slevel+"到"+elevel+"级");
                results.add(slguserlv);
                num+=1;
            }
            LOGGER.info("User Level Result Size: "+results.size());
        }else if(filter.getType().equals("2")){
            //玩家战斗力统计
            //0~48000战斗力
            for(int i=0;i<48000;i+=3000){
                StringBuilder userpowerSql = new StringBuilder(SQL_USER_POWER_FIND_PAGE);
                userpowerSql.append(" and b.regTime < " + (day + 86400000L));
                int spower = i;
                int epower = i+2999;
                userpowerSql.append(" and a.power >= " + spower);
                userpowerSql.append(" and a.power <= " + epower);

                SlgLevelStatis slgpower = new SlgLevelStatis();
                slgpower.setUsernum(jdbcTemplate.queryForInt(userpowerSql.toString()));
                slgpower.setUserlv(spower+"到"+epower);
                results.add(slgpower);
                num+=1;
            }
            //48000战斗力以上
            StringBuilder userpowerSql_ex = new StringBuilder(SQL_USER_POWER_FIND_PAGE);
            userpowerSql_ex.append(" and b.regTime < " + (day + 86400000L));
            userpowerSql_ex.append(" and a.power >= " + 48000);
            SlgLevelStatis slgpower_ex = new SlgLevelStatis();
            slgpower_ex.setUsernum(jdbcTemplate.queryForInt(userpowerSql_ex.toString()));
            slgpower_ex.setUserlv("48000以上");
            results.add(slgpower_ex);
            num+=1;

            LOGGER.info("User Power Result Size: "+results.size());
        }else if(filter.getType().equals("3")){
            //建筑等级统计
            if(StringUtils.isNotBlank(filter.getBdid())){
                for (int i = 1; i <= 50; i += 5) {
                    StringBuilder bdlevelSql = new StringBuilder(SQL_BD_LEVEL_FIND_PAGE);
                    bdlevelSql.append(" and updateTime < " + (day + 86400000L));
                    int slevel = i;
                    int elevel = i + 4;
                    bdlevelSql.append(" and level >= " + slevel);
                    bdlevelSql.append(" and level <= " + elevel);
                    bdlevelSql.append(" and itemid = " + filter.getBdid());

                    SlgLevelStatis slgbdlv = new SlgLevelStatis();
                    slgbdlv.setBdnum(jdbcTemplate.queryForInt(bdlevelSql.toString()));
                    slgbdlv.setBdlv(slevel+"到"+elevel+"级");
                    results.add(slgbdlv);
                    num+=1;
                }
            }
            LOGGER.info("BD Level Result Size: "+results.size());
        }

        page.setTotalRows(num);
        page.setResults(results);
        return page;
    }

}
