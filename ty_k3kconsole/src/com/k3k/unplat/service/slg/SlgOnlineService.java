package com.k3k.unplat.service.slg;

import com.k3k.unplat.common.MultipleDataSource;
import com.k3k.unplat.dao.slg.SlgOnlineDao;
import com.k3k.unplat.entity.slg.SlgOnline;
import com.k3k.unplat.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jef on 2016/12/8.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SlgOnlineService {

    @Autowired
    private SlgOnlineDao slgOnlineDao;

    @Transactional(readOnly = true)
    public Page<SlgOnline> findPage(Page<SlgOnline> page, SlgOnline filter){
        MultipleDataSource.setDataSourceKey("slgDBDS");//切换 slgDBDS datasource
        Page<SlgOnline> ret = slgOnlineDao.findPage(page,filter);
        MultipleDataSource.setDataSourceKey("gameloggerDBDS");//切回 GameLoggerDB datasource
        return ret;
    }

    @Transactional(readOnly = true)
    public Page<SlgOnline> findPageByDay(Page<SlgOnline> page, SlgOnline filter){
        MultipleDataSource.setDataSourceKey("slgDBDS");//切换 slgDBDS datasource
        Page<SlgOnline> ret = slgOnlineDao.findPageByDay(page,filter);
        MultipleDataSource.setDataSourceKey("gameloggerDBDS");//切回 GameLoggerDB datasource
        return ret;
    }


}
