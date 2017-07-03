package com.k3k.unplat.service.slg;

import com.k3k.unplat.common.MultipleDataSource;
import com.k3k.unplat.dao.slg.SlgAccountDao;
import com.k3k.unplat.entity.slg.SlgAccountStatis;
import com.k3k.unplat.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by jef on 2016/12/7.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SlgAccountService {

    @Autowired
    private SlgAccountDao slgAccountDao;


    @Transactional(readOnly = true)
    public Page<SlgAccountStatis> findPage(Page<SlgAccountStatis> page, SlgAccountStatis filter){
            MultipleDataSource.setDataSourceKey("slgDBDS");//切换 slgDBDS datasource
            Page<SlgAccountStatis> ret = slgAccountDao.findPage(page,filter);
            MultipleDataSource.setDataSourceKey("gameloggerDBDS");//切回 GameLoggerDB datasource
            return ret;
    }
}
