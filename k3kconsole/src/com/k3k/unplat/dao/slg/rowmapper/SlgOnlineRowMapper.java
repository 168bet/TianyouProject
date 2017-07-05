package com.k3k.unplat.dao.slg.rowmapper;

import com.k3k.unplat.entity.slg.SlgOnline;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jef on 2017/3/1.
 */
public class SlgOnlineRowMapper implements RowMapper<SlgOnline> {
    @Override
    public SlgOnline mapRow(ResultSet rs, int rowNum) throws SQLException {
        SlgOnline o = new SlgOnline();
        o.setOnlinenum(rs.getInt("onlinenum"));
        o.setTime(rs.getString("time"));
        return o;
    }

}
