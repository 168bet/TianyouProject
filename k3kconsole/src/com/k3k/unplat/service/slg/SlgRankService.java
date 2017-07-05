package com.k3k.unplat.service.slg;

import com.k3k.unplat.common.MultipleDataSource;
import com.k3k.unplat.dao.slg.SlgLevelDao;
import com.k3k.unplat.entity.slg.SlgLevel;
import com.k3k.unplat.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jef on 2017/2/24.
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class SlgRankService {

    @Autowired
    private SlgLevelDao slgLevelDao;

    @Transactional(readOnly = true)
    public Page<SlgLevel> findPageLevel(Page<SlgLevel> page, SlgLevel filter){
        MultipleDataSource.setDataSourceKey("slgDBDS");//切换 slgDBDS datasource
        Page<SlgLevel> ret = slgLevelDao.findPageLevel(page, filter);
        MultipleDataSource.setDataSourceKey("gameloggerDBDS");//切回 GameLoggerDB datasource
        return ret;
    }
    @Transactional(readOnly = true)
    public Page<SlgLevel> findPageBuilding(Page<SlgLevel> page, SlgLevel filter){
        MultipleDataSource.setDataSourceKey("slgDBDS");//切换 slgDBDS datasource
        Page<SlgLevel> ret = slgLevelDao.findPageBuilding(page, filter);
        MultipleDataSource.setDataSourceKey("gameloggerDBDS");//切回 GameLoggerDB datasource
        return ret;
    }
    @Transactional(readOnly = true)
    public Page<SlgLevel> findPagePower(Page<SlgLevel> page, SlgLevel filter){
        MultipleDataSource.setDataSourceKey("slgDBDS");//切换 slgDBDS datasource
        Page<SlgLevel> ret = slgLevelDao.findPagePower(page, filter);
        MultipleDataSource.setDataSourceKey("gameloggerDBDS");//切回 GameLoggerDB datasource
        return ret;
    }
}
