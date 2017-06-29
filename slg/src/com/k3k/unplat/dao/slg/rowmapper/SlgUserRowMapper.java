package com.k3k.unplat.dao.slg.rowmapper;

import com.k3k.unplat.entity.slg.SlgUser;
import com.k3k.unplat.utils.SlgUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jef on 2017/4/6.
 */
public class SlgUserRowMapper implements RowMapper<SlgUser> {
    @Override
    public SlgUser mapRow(ResultSet rs, int rowNum) throws SQLException {
        SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SlgUser o = new SlgUser();
        o.setNickname(rs.getString("name"));
        o.setUserid(String.valueOf(rs.getLong("uid")));
        o.setLevel(rs.getInt("level"));
        o.setGold(rs.getLong("gold"));
        o.setUnion(SlgUtils.getUnion(rs.getInt("unionId")));
        o.setEntry(SlgUtils.getEntry(rs.getInt("subUnionId")));
        Date reg = new Date(rs.getLong("regTime"));
        Date log = new Date(rs.getLong("lastOnlineTime"));
        Long ban = rs.getLong("banTime");
        if(ban == 0){
            o.setBantime("-");
            o.setCstate("正常");
        }else {
            Date banTime = new Date(ban);
            o.setBantime(df.format(banTime));
            o.setCstate("封禁");
        }
        o.setRegtime(df.format(reg));
        o.setLogtime(df.format(log));
        return o;
    }

}
