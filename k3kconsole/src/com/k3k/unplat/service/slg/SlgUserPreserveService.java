package com.k3k.unplat.service.slg;

import com.k3k.unplat.common.MultipleDataSource;
import com.k3k.unplat.dao.slg.SlgUserPreserveDao;
import com.k3k.unplat.entity.slg.SlgUserPreserve;
import com.k3k.unplat.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jef on 2016/12/8.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SlgUserPreserveService {

    @Autowired
    private SlgUserPreserveDao slgUserPreserveDao;

    @Transactional(readOnly = true)
    public Page<SlgUserPreserve> findPage(Page<SlgUserPreserve> page, SlgUserPreserve filter){
        MultipleDataSource.setDataSourceKey("slgDBDS");//切换 slgDBDS datasource
        Page<SlgUserPreserve> ret = slgUserPreserveDao.findPage(page, filter);
        MultipleDataSource.setDataSourceKey("gameloggerDBDS");//切回 GameLoggerDB datasource
        return ret;
    }


}
