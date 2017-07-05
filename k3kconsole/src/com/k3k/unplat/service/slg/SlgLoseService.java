package com.k3k.unplat.service.slg;

import com.k3k.unplat.common.MultipleDataSource;
import com.k3k.unplat.dao.slg.SlgLoseDao;
import com.k3k.unplat.entity.slg.SlgLose;
import com.k3k.unplat.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jef on 2017/2/27.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SlgLoseService {

    @Autowired
    private SlgLoseDao SlgLoseDao;


    @Transactional(readOnly = true)
    public Page<SlgLose> findPage(Page<SlgLose> page, SlgLose filter){
        MultipleDataSource.setDataSourceKey("slgDBDS");//切换 slgDBDS datasource
        Page<SlgLose> ret = SlgLoseDao.findPage(page,filter);
        MultipleDataSource.setDataSourceKey("gameloggerDBDS");//切回 GameLoggerDB datasource
        return ret;
    }
}
