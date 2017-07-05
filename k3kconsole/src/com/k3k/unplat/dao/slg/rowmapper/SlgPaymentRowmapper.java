package com.k3k.unplat.dao.slg.rowmapper;

import com.k3k.unplat.entity.slg.SlgPaymentStatis;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SlgPaymentRowmapper implements RowMapper<SlgPaymentStatis> {
    @Override
    public SlgPaymentStatis mapRow(ResultSet rs, int rowNum) throws SQLException {
        SlgPaymentStatis o = new SlgPaymentStatis();
        o.setUid(rs.getLong("uid"));
        o.setPaymoney(rs.getDouble("smoney"));
        o.setName(rs.getString("name"));
        o.setLevel(rs.getInt("level"));
        return o;
    }
}