package com.k3k.unplat.service.slg;

import com.k3k.unplat.common.MultipleDataSource;
import com.k3k.unplat.dao.slg.SlgUserDao;
import com.k3k.unplat.entity.slg.SlgUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jef on 2017/4/6.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SlgUserService {

    @Autowired
    private SlgUserDao slgUserDao;

    @Transactional(readOnly = true)
    public List<SlgUser> getUserList(SlgUser filter){
        MultipleDataSource.setDataSourceKey("slgDBDS");//切换 slgDBDS datasource
        List<SlgUser> list = slgUserDao.getUserList(filter);
        MultipleDataSource.setDataSourceKey("gameloggerDBDS");//切回 GameLoggerDB datasource
        return list;
    }
}
