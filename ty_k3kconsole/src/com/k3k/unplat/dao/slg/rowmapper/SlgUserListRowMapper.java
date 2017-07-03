package com.k3k.unplat.dao.slg.rowmapper;

import com.k3k.unplat.entity.slg.SlgUserlist;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jef on 2017/1/18.
 */
public class SlgUserListRowMapper implements RowMapper<SlgUserlist> {
    @Override
    public SlgUserlist mapRow(ResultSet rs, int rowNum) throws SQLException {
        SlgUserlist o = new SlgUserlist();
        o.setUid(String.valueOf(rs.getLong("uid")));
        return o;
    }
}

