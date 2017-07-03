package com.k3k.unplat.dao.slg;

import com.k3k.unplat.dao.slg.rowmapper.SlgUserRowMapper;
import com.k3k.unplat.entity.slg.SlgUser;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by jef on 2017/4/6.
 */
@Repository
public class SlgUserDao {
    private static final Log LOGGER = LogFactory.getLog(SlgUserDao.class);

    @Resource
    private JdbcTemplate jdbcTemplate;

    private static final String SQL_USER_FIND_PAGE = "select *  from cokdb1.userprofile  where 1=1 ";

    public  List<SlgUser> getUserList(SlgUser filter) {
        StringBuilder userSql = new StringBuilder(SQL_USER_FIND_PAGE);
        if(StringUtils.isNotBlank(filter.getKeyword())){
            if(StringUtils.isNumeric(filter.getKeyword())){
                userSql.append("and name like '%"+filter.getKeyword()+"%' OR uid = "+filter.getKeyword());
            }else {
                userSql.append("and name like '%"+filter.getKeyword()+"%'");
            }
        }else {
            userSql.append("and banTime > 0 ");
        }
        LOGGER.info("User SQL:"+userSql);
        List<SlgUser> result = jdbcTemplate.query(userSql.toString(),new SlgUserRowMapper());
        LOGGER.info("Result Size:"+result.size());
        return result;
    }
}
