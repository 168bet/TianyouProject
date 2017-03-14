package com.tianyou.packtool.dao;

import java.util.List;

import com.tianyou.packtool.domain.Channel;


public interface ChannelDao {

	/**
	 * 增加渠道信息
	 */
	public int addChannel(Channel channel);

	/**
	 * 删除所有渠道信息
	 */
	public int deleteAllChannel();

	/**
	 * 根据渠道名修改渠道Id
	 * @param channelName
	 * @param channelId
	 * @return
	 */
	public int updateChannel(String channelName, String channelId);

	/**
	 * 根据渠道号查询渠道信息
	 * @param channelId
	 * @return
	 */
	public Channel queryChannelByName(String channelId);
	
	/**
	 * 查询指定渠道信息
	 */
	public List<Channel> queryChannel(String channelName);

	/**
	 * 查询所有渠道信息
	 */
	public List<Channel> queryAllChannel();
	
	/**
	 * 查询指定页渠道信息
	 */
	public List<Channel> queryChannelByPage(int page);

	/**
	 * 查询所有指定字段值的渠道信息
	 */
	public List<Channel> queryCategoryChannel(boolean isSpecial, String packType, String adType);

	/**
	 * 重置所有渠道信息
	 */
	public int resetAllChannelInfo();

	/**
	 * 设置已打包标记
	 */
	public int resetChannelInfo(String channelId);
	
	/**
	 * 删除指定记录
	 */
	public int deleteSelectedChannel(String channelName);
}
