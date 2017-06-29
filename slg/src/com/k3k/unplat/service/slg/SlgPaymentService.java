package com.k3k.unplat.service.slg;

import com.k3k.unplat.common.MultipleDataSource;
import com.k3k.unplat.dao.slg.SlgPaymentDao;
import com.k3k.unplat.entity.slg.SlgOrder;
import com.k3k.unplat.entity.slg.SlgPaymentStatis;
import com.k3k.unplat.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jef on 2016/12/9.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SlgPaymentService {
    @Autowired
    private SlgPaymentDao slgPaymentDao;

    @Transactional(readOnly = true)
    public Page<SlgPaymentStatis> findPage(Page<SlgPaymentStatis> page, SlgPaymentStatis filter,int checkType){
        MultipleDataSource.setDataSourceKey("slgDBDS");//切换 slgDBDS datasource
        Page<SlgPaymentStatis> ret = slgPaymentDao.findPage(page,filter,checkType);
        MultipleDataSource.setDataSourceKey("gameloggerDBDS");//切回 GameLoggerDB datasource
        return ret;
    }

    @Transactional(readOnly = true)
    public Page<SlgOrder> findOrderPage(Page<SlgOrder> page, SlgOrder filter){
        MultipleDataSource.setDataSourceKey("slgDBDS");//切换 slgDBDS datasource
        Page<SlgOrder> ret = slgPaymentDao.findOrderPage(page,filter);
        MultipleDataSource.setDataSourceKey("gameloggerDBDS");//切回 GameLoggerDB datasource
        return ret;
    }

}
