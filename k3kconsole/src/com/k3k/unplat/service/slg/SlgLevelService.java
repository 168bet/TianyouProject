package com.k3k.unplat.service.slg;

/**
 * Created by jef on 2016/12/9.
 */

import com.k3k.unplat.common.MultipleDataSource;
import com.k3k.unplat.dao.slg.SlgLevelStatisDao;
import com.k3k.unplat.dao.slg.SlgUserListDao;
import com.k3k.unplat.entity.slg.SlgLevelStatis;
import com.k3k.unplat.entity.slg.SlgUser;
import com.k3k.unplat.entity.slg.SlgUserlist;
import com.k3k.unplat.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class SlgLevelService {
    @Autowired
    private SlgLevelStatisDao slgLevelStatisDao;

    @Autowired
    private SlgUserListDao slgUserListDao;


    @Transactional(readOnly = true)
    public Page<SlgLevelStatis> findPage(Page<SlgLevelStatis> page, SlgLevelStatis filter){
        MultipleDataSource.setDataSourceKey("slgDBDS");//切换 slgDBDS datasource
        Page<SlgLevelStatis> ret = slgLevelStatisDao.findPage(page, filter);
        MultipleDataSource.setDataSourceKey("gameloggerDBDS");//切回 GameLoggerDB datasource
        return ret;
    }

    @Transactional(readOnly = true)
    public List<SlgUserlist> getUserList(int viplevel,int slevel,int elevel,int unionid){
        MultipleDataSource.setDataSourceKey("slgDBDS");//切换 slgDBDS datasource
        List<SlgUserlist> list = slgUserListDao.getUserList(viplevel,slevel,elevel,unionid);
        MultipleDataSource.setDataSourceKey("gameloggerDBDS");//切回 GameLoggerDB datasource
        return list;
    }

    @Transactional(readOnly = true)
    public String getUserID(String uname){
        MultipleDataSource.setDataSourceKey("slgDBDS");//切换 slgDBDS datasource
        String uid = slgUserListDao.getUserID(uname);
        MultipleDataSource.setDataSourceKey("gameloggerDBDS");//切回 GameLoggerDB datasource
        return uid;
    }

    @Transactional(readOnly = true)
    public Page<SlgUser> getUser(Page<SlgUser> page, SlgUser filter){
        MultipleDataSource.setDataSourceKey("slgDBDS");//切换 slgDBDS datasource
        Page<SlgUser> ret = slgUserListDao.findPage(page, filter);
        MultipleDataSource.setDataSourceKey("gameloggerDBDS");//切回 GameLoggerDB datasource
        return ret;
    }


}
