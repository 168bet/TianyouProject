package com.k3k.unplat.dao.slg;

import com.k3k.unplat.dao.slg.rowmapper.SlgLevelRowMapper;
import com.k3k.unplat.entity.slg.SlgLevel;
import com.k3k.unplat.utils.Page;
import com.k3k.unplat.utils.SlgUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 领主排行
 * Created by jef on 2017/2/24.
 */
@Repository
public class SlgLevelDao {
    private static final Log LOGGER = LogFactory.getLog(SlgLevelDao.class);

    @Resource
    private JdbcTemplate jdbcTemplate;

    private static final String SQL_USER_LEVEL_RANK = "select * from (select uid , level ,name from cokdb1.userprofile  where 1=1 ";
    private static final String SQL_USER_LEVEL_RANK_FIND_ALL = "select count(a.uid) from (select uid from cokdb1.userprofile  where 1=1 ";
    private static final String SQL_USER_BUILDING_RANK = "select * from (select a.uid , a.level ,b.name from cokdb1.user_building a INNER JOIN cokdb1.userprofile b on a.uid = b.uid where 1=1 and a.itemId = 400000 ";
    private static final String SQL_USER_BUILDING_RANK_FIND_ALL = "select count(*) from (select a.uid , a.level ,b.name from cokdb1.user_building a INNER JOIN cokdb1.userprofile b on a.uid = b.uid where 1=1 and a.itemId = 400000 ";
    private static final String SQL_USER_POWER_RANK = "select * from (select a.uid , a.power as level ,b.name from cokdb1.playerinfo a INNER JOIN cokdb1.userprofile b on a.uid = b.uid where 1=1 ";
    private static final String SQL_USER_POWER_FINDALL = "select count(*) from (select a.uid , a.power as level,b.name from cokdb1.playerinfo a INNER JOIN cokdb1.userprofile b on a.uid = b.uid where 1=1 ";


    public Page<SlgLevel> findPageLevel(Page<SlgLevel> page, SlgLevel filter) {
        List<SlgLevel> results = new ArrayList<SlgLevel>();

        StringBuilder rankSQL = new StringBuilder(SQL_USER_LEVEL_RANK);
        StringBuilder findAll = new StringBuilder(SQL_USER_LEVEL_RANK_FIND_ALL);
        String unioninfo = SlgUtils.getUnionInfo("",filter.getEnter(),0,0);

        rankSQL.append(unioninfo);
        rankSQL.append(" order by level desc limit 50)a ");
        rankSQL.append("limit ?,?");

        findAll.append(unioninfo);
        findAll.append(" limit 50)a");
        try {
            int totalRows = jdbcTemplate.queryForInt(findAll.toString());
            results = jdbcTemplate.query(rankSQL.toString(), new Object[]{page.getStartIndex(), page.getPageSize()}, new SlgLevelRowMapper());
            for(int i = 0;i<results.size();i++){
                results.get(i).setLevel("LV "+results.get(i).getLevel());
                results.get(i).setRank(i+1+page.getStartIndex());
            }
            page.setTotalRows(totalRows);
            page.setResults(results);
        }catch (Exception e){
            LOGGER.error("领主等级排行"+e.getMessage());
        }
        return page;
    }

    public Page<SlgLevel> findPageBuilding(Page<SlgLevel> page, SlgLevel filter) {
        List<SlgLevel> results = new ArrayList<SlgLevel>();

        StringBuilder rankSQL = new StringBuilder(SQL_USER_BUILDING_RANK);
        StringBuilder findAll = new StringBuilder(SQL_USER_BUILDING_RANK_FIND_ALL);
        String unioninfo = SlgUtils.getUnionInfo("b",filter.getEnter(),0,0);

        rankSQL.append(unioninfo);
        rankSQL.append(" order by a.level desc limit 50)a ");
        rankSQL.append("limit ?,?");

        findAll.append(unioninfo);
        findAll.append(" order by a.level desc limit 50)a");
        try {
            int totalRows = jdbcTemplate.queryForInt(findAll.toString());
            results = jdbcTemplate.query(rankSQL.toString(), new Object[]{page.getStartIndex(), page.getPageSize()}, new SlgLevelRowMapper());
            for(int i = 0;i<results.size();i++){
                results.get(i).setRank(i+1+page.getStartIndex());
                results.get(i).setLevel("LV "+results.get(i).getLevel());
            }
            page.setTotalRows(totalRows);
            page.setResults(results);
        }catch (Exception e){
            LOGGER.error("领主城堡等级排行"+e.getMessage());
        }
        return page;
    }

    public Page<SlgLevel> findPagePower(Page<SlgLevel> page, SlgLevel filter) {
        List<SlgLevel> results = new ArrayList<SlgLevel>();

        StringBuilder rankSQL = new StringBuilder(SQL_USER_POWER_RANK);
        StringBuilder findAll = new StringBuilder(SQL_USER_POWER_FINDALL);
        String unioninfo = SlgUtils.getUnionInfo("b",filter.getEnter(),0,0);

        rankSQL.append(unioninfo);
        rankSQL.append(" order by a.power desc limit 50)a ");
        rankSQL.append("limit ?,?");

        findAll.append(unioninfo);
        findAll.append(" order by a.power desc limit 50)a");
        try {
            int totalRows = jdbcTemplate.queryForInt(findAll.toString());
            results = jdbcTemplate.query(rankSQL.toString(), new Object[]{page.getStartIndex(), page.getPageSize()}, new SlgLevelRowMapper());
            for(int i = 0;i<results.size();i++){
                results.get(i).setRank(i+1+page.getStartIndex());
            }
            page.setTotalRows(totalRows);
            page.setResults(results);
        }catch (Exception e){
            LOGGER.error("领主战力排行"+e.getMessage());
        }
        return page;
    }

}
