package com.k3k.unplat.dao.slg.rowmapper;

import com.k3k.unplat.entity.slg.SlgUser;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jef on 2017/1/19.
 */
public class SlgUserInfoRowMapper implements RowMapper<SlgUser> {

    @Override
    public SlgUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        SlgUser o = new SlgUser();
        o.setUsername(rs.getString("name"));
        o.setChannel(rs.getInt("unionId"));
        o.setCreatetime(rs.getLong("regTime"));
        o.setServer(rs.getInt("serverId"));
        o.setUserid(rs.getString("uid"));
        o.setLevel(rs.getInt("level"));
        return o;
    }

}
