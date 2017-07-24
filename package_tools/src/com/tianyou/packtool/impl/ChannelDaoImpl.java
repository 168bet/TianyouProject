package com.tianyou.packtool.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.tianyou.packtool.dao.ChannelDao;
import com.tianyou.packtool.domain.Channel;
import com.tianyou.packtool.util.DbcpUtils;

/**
 * 渠道数据库接口实现
 * 
 * @author itstrong
 *
 */
public class ChannelDaoImpl implements ChannelDao {

	private QueryRunner qr = new QueryRunner(DbcpUtils.getDataSource());
	
	@Override
	public int addChannel(Channel channel) {
		int resule = 0;
		try {
			String sql = "insert into channel(channelName,channelId) values(?,?)";
			resule = qr.update(sql,channel.getChannelName(),channel.getChannelId());
		} catch (SQLException e) {
			System.out.println("添加渠道失败.........");
			e.printStackTrace();
		}
		return resule;
	}

	@Override
	public int deleteAllChannel() {
		int resule = 0;
		try {
			String sql = "delete from channel";
			resule = qr.update(sql);
		} catch (SQLException e) {
			System.out.println("删除所有渠道失败.........");
		}
		return resule;
	}

	@Override
	public int updateChannel(String channelName, String channelId) {
		int resule = 0;
		try {
			String sql = "update channel set channelId=?"
					+ "where channelName=?";
			resule = qr.update(sql, channelId, channelName);
		} catch (SQLException e) {
			System.out.println("更新渠道失败.........");
		}
		return resule;
	}

	@Override
	public List<Channel> queryChannel(String channelName) {
		List<Channel> channelLists = null;
		try {
			String sql = "select * from channel where channelName like ?";
			channelLists = qr.query(sql, new BeanListHandler<Channel>(
					Channel.class),"%"+channelName+"%");
		} catch (SQLException e) {
			System.out.println("查询渠道失败...........");
		}
		return channelLists;
	}
	
	@Override
	public Channel queryChannelByName(String channelName) {
		Channel channel = null;
		try {
			String sql = "select * from channel where channelName= ?";
			channel = qr.query(sql, new BeanHandler<Channel>(Channel.class),
					channelName);
		} catch (SQLException e) {
			System.out.println("查询渠道失败...........");
		}
		return channel;
	}
	
	@Override
	public List<Channel> queryAllChannel() {
		List<Channel> channelLists = null;
		try {
			String sql = "select * from channel";
			channelLists = qr.query(sql, new BeanListHandler<Channel>(
					Channel.class));
		} catch (SQLException e) {
			System.out.println(e.toString());
			System.out.println("查询所有渠道失败...........");
		}
		return channelLists;
	}
	
	@Override
	public List<Channel> queryChannelByPage(int page) {
		System.out.println("查询页数：" + page);
		List<Channel> channelLists = null;
		try {
			String sql = "select * from channel limit " + page * 10 + ",10";
			channelLists = qr.query(sql, new BeanListHandler<Channel>(
					Channel.class));
		} catch (SQLException e) {
			System.out.println(e.toString());
			System.out.println("查询所有渠道失败...........");
		}
		return channelLists;
	}

	@Override
	public List<Channel> queryCategoryChannel(boolean isSpecial, String packType, String adType) {
		return null;
//		List<Channel> channelLists = null;
//		String sql = null;
//		System.out.println(isSpecial);
//		if (isSpecial) {
//			sql = "select * from channel where packType = ? and adType = ? and specialDemand is not null";
//		} else {
//			sql = "select * from channel where packType = ? and adType = ? and specialDemand is null";
//		}
//		try {
//			channelLists = qr.query(sql, new BeanListHandler<Channel>(Channel.class), packType, adType);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return channelLists;
	}

	@Override
	public int resetAllChannelInfo() {
		int resule = 0;
//		try {
//			String sql = "update channel set isPack = 0";
//			resule = qr.update(sql);
//		} catch (SQLException e) {
//			System.out.println("更新渠道失败.........");
//		}
		return resule;
	}

	@Override
	public int resetChannelInfo(String channelId) {
		int resule = 0;
//		try {
//			String sql = "update channel set isPack = 1 where channelId = ?";
//			resule = qr.update(sql, channelId);
//		} catch (SQLException e) {
//			System.out.println("更新渠道失败.........");
//		}
		return resule;
	}

	@Override
	public int deleteSelectedChannel(String channelName) {
		int resule = 0;
		try {
			String sql = "delete from channel where channelName=?";
			resule = qr.update(sql,channelName);
		} catch (SQLException e) {
			System.out.println("删除指定渠道失败.........");
		}
		return resule;
	}
}
