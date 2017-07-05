package com.k3k.unplat.dao.slg;

import com.k3k.unplat.dao.slg.rowmapper.SlgUserInfoRowMapper;
import com.k3k.unplat.dao.slg.rowmapper.SlgUserListRowMapper;
import com.k3k.unplat.entity.slg.SlgUser;
import com.k3k.unplat.entity.slg.SlgUserlist;
import com.k3k.unplat.utils.Page;
import com.k3k.unplat.utils.SlgUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jef on 2017/1/18.
 */
@Repository
public class SlgUserListDao {

    private static final Log LOGGER = LogFactory.getLog(SlgUserListDao.class);

    @Resource
    private JdbcTemplate jdbcTemplate;

    private static final String SQL_GET_USER_LIST = "select a.uid from cokdb1.userprofile a left join cokdb1.user_vip b on a.uid = b.uid where 1=1 ";

    private static final String SQL_GET_USER_ID = "select uid from cokdb1.userprofile  where 1=1 ";

    private static final String SQL_GET_USER_PAGE = "select * from cokdb1.userprofile  where 1=1 ";

    public List<SlgUserlist> getUserList(int viplevel, int slevel, int elevel,int unionid) {
        StringBuilder userSql = new StringBuilder(SQL_GET_USER_LIST);
        String unioninfo = SlgUtils.getUnionInfo("a",0,unionid,0);
        LOGGER.info("SLG USER LIST UNION: "+unioninfo);
        if(unionid != 0){
            userSql.append(unioninfo);
        }
        if (viplevel != -1) {
            userSql.append(" and b.level = " + viplevel);
        }
        if (slevel != 0) {
            userSql.append(" and a.level >= " + slevel);
        }
        if (elevel != 0) {
            userSql.append(" and a.level <= " + elevel);
        }
        LOGGER.info("GET USER SQL: " + userSql.toString());

        List<SlgUserlist> result = jdbcTemplate.query(userSql.toString(), new SlgUserListRowMapper());
        LOGGER.info("User List size:" + result.size());
        return result;
    }

    public String getUserID(String uname) {
        StringBuilder uidSql = new StringBuilder(SQL_GET_USER_ID);
        if (StringUtils.isNotBlank(uname)) {
            uidSql.append(" and name = '" + uname + "'");
        }
        LOGGER.info("GET USER SQL: " + uidSql.toString());
        String result = jdbcTemplate.queryForObject(uidSql.toString(),String.class);
        return result;
    }

    public Page<SlgUser> findPage(Page<SlgUser> page, SlgUser filter) {
        List<SlgUser> results = new ArrayList<SlgUser>();
        StringBuilder uSql = new StringBuilder(SQL_GET_USER_PAGE);
        if (StringUtils.isNotBlank(filter.getUsername())) {
            uSql.append(" and name = '" + filter.getUsername() + "'");
        }
        if (StringUtils.isNotBlank(filter.getUserid())) {
            uSql.append(" and uid = '" + filter.getUserid()+"'");
        }
        try {
            LOGGER.info("User Page SQL:" + uSql.toString());
            List<SlgUser> result = jdbcTemplate.query(uSql.toString(),new SlgUserInfoRowMapper());
            LOGGER.info("User Size: "+result.size());
            LOGGER.info("User Info: "+result.get(0).getUserid());
            page.setTotalRows(result.size());
            page.setResults(results);
        }catch (Exception e){
            LOGGER.error(e);
        }
        return page;
    }


}
