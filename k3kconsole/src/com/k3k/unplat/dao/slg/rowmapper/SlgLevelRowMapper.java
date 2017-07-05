package com.k3k.unplat.dao.slg.rowmapper;

import com.k3k.unplat.entity.slg.SlgLevel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jef on 2017/2/24.
 */
public class SlgLevelRowMapper implements RowMapper<SlgLevel> {
    @Override
    public SlgLevel mapRow(ResultSet rs, int rowNum) throws SQLException {
        SlgLevel o = new SlgLevel();
        o.setLevel(String.valueOf(rs.getInt("level")));
        o.setNickname(rs.getString("name"));
        o.setUid(rs.getLong("uid"));
        return o;
    }
}
