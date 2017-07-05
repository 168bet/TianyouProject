package com.k3k.unplat.dao.slg.rowmapper;

import com.k3k.unplat.entity.slg.SlgOrder;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

/**
 * Created by jef on 2017/2/23.
 */
public class SlgOrderRowmapper implements RowMapper<SlgOrder> {
    @Override
    public SlgOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
        SimpleDateFormat df =new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        SlgOrder o = new SlgOrder();
        o.setOrdertime(df.format(rs.getLong("orderTime")*1000));
        o.setNickname(rs.getString("name"));
        o.setOrderid(String.valueOf(rs.getLong("orderId")));
        o.setMoney(rs.getString("money"));
        o.setUid(rs.getLong("uid"));
        o.setUuid(rs.getString("uuid"));
        o.setPaytype("天游");
        return o;
    }
}
