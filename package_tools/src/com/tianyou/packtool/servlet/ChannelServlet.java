package com.tianyou.packtool.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tianyou.packtool.domain.Channel;
import com.tianyou.packtool.impl.ChannelDaoImpl;
import com.tianyou.packtool.util.ExcelReaderUtil;
import com.tianyou.packtool.util.HttpUtils;

@SuppressWarnings("serial")
public class ChannelServlet extends BaseServlet {

	private ChannelDaoImpl daoImpl = new ChannelDaoImpl();
	private List<Channel> channelList = new ArrayList<Channel>();
	private Map<String, String> channelMap = new LinkedHashMap<String, String>();
	private int currentPage = 0;
	
	@Override
	public void doOperate(String op, HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		if ("channel_list".equals(op)) {
			queryChannelList(request, response);
		} else if ("last_page".equals(op)) {
			queryLastPage(request, response);
		} else if ("next_page".equals(op)) {
			queryNextPage(request, response);
		} else if ("updateChannelID".equals(op)){
			updateChannelID(request,response,channelMap);
		} else if ("insertChannel".equals(op)){
			insertChannel(request,response);
		} else if ("delChannel".equals(op)){
			deletSelecteChannel(request,response,channelMap);
		} else if ("searchChannel".equals(op)){
			displaySearchChannellist(request,response,channelMap);
		} else if ("confirm".equals(op)) {
			confirmSelectChannel(request, response);
		} else if ("custom_channel".equals(op)) {
			customChannelList(request, response);
		}
	}

	//查询渠道列表
	private void customChannelList(HttpServletRequest request, HttpServletResponse response) {
		Map<String, List<String>> channelInfo = ExcelReaderUtil.readExcelContent2(new File(PATH_CHANNEL));
		System.out.println("channelInfo:" + channelInfo);
		List<String> nameList = channelInfo.get("channel_name");
		List<String> idList = channelInfo.get("channel_id");
		List<Channel> childChannels = new ArrayList<Channel>();
		for (int i = 0; i < nameList.size(); i++) {
			Channel channel = new Channel();
			channel.setChannelName(nameList.get(i));
			channel.setChannelId(idList.get(i));
			childChannels.add(channel);
		}
		Channel channel = new Channel();
		channel.setChannelName("百度");
		channel.setChildChannels(childChannels);
		channelList.add(channel);
		System.out.println("自定义渠道列表：" + channelList);
		refreshChannelList(request, response);
	}
		
	//查询渠道列表
	private void queryChannelList(HttpServletRequest request, HttpServletResponse response) {
		channelList = HttpUtils.getMainChannel();
		System.out.println("渠道列表：" + channelList);
		refreshChannelList(request, response);
	}
	
	//查询上一页渠道
	private void queryLastPage(HttpServletRequest request, HttpServletResponse response) {
		channelList = daoImpl.queryChannelByPage(currentPage == 0 ? 0 : --currentPage);
		refreshChannelList(request, response);
	}
	
	//查询下一页渠道
	private void queryNextPage(HttpServletRequest request, HttpServletResponse response) {
		List<Channel> list = daoImpl.queryChannelByPage(currentPage + 1);
		System.out.println("下一页数据:" + list);
		if (list.size() != 0) {
			channelList = list;
			currentPage++;
		}
		refreshChannelList(request, response);
	}
	
	// 返回确认选择渠道信息
	private void confirmSelectChannel(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String[] checkboxValues = request.getParameterValues("channel");
		HttpSession session = request.getSession();
		@SuppressWarnings("unchecked")
		List<String> selectChannels = (List<String>)session.getAttribute("select_channel");
		if (selectChannels == null) selectChannels = new ArrayList<String>();
		if (checkboxValues != null) {
			for (String value : checkboxValues) {
				if (!selectChannels.contains(value)) {					
					selectChannels.add(value);
				}
			}
		}
		System.out.println("已选渠道：" + selectChannels);
		session.setAttribute("select_channel", selectChannels);
		request.getRequestDispatcher("/index.jsp").forward(request, response);
	}
	
	// 显示搜索的渠道号
	private void displaySearchChannellist(HttpServletRequest request, HttpServletResponse response, Map<String, String> map) throws ServletException, IOException {
		ChannelDaoImpl daoImpl = new ChannelDaoImpl();
		String searchName = request.getParameterValues("search_channelname")[0].trim();
		System.out.println(searchName);
		List<Channel> searchChannel = daoImpl.queryChannel(searchName);
		System.out.println(searchChannel);
		if (searchChannel == null) {
			return;
		} else {
			map.clear();
			for (int i = 0; i < searchChannel.size(); i++) {
				Channel channel = searchChannel.get(i);
				String channelName = channel.getChannelName();
				String channelId = channel.getChannelId();
				System.out.println(channelName + "----" + channelId);
				String uuid = UUID.randomUUID().toString();
				map.put(uuid, channelName + "——" + channelId);
			}
			request.getSession().setAttribute("searchChannel", map);
			request.getRequestDispatcher("/channellist.jsp").forward(request, response);
		}
	}
	
	// 修改渠道号
	private void updateChannelID(HttpServletRequest request, HttpServletResponse response, Map<String, String> map) {
		ChannelDaoImpl daoImpl = new ChannelDaoImpl();
		String channelName = (String) request.getParameter("channelid");
		String channelId = (String) request.getParameter("vo");
		System.out.println("channelId:" + channelId + ",channelName:" + channelName);
		if (!"".equals(channelId)) daoImpl.updateChannel(channelName, channelId);
		refreshChannelList(request, response);
	}
	
	// 删除指定渠道号记录
	private void deletSelecteChannel(HttpServletRequest request, HttpServletResponse response, Map<String, String> map) {
		ChannelDaoImpl daoImpl = new ChannelDaoImpl();
		String uuid = (String) request.getParameter("uuid");
		String channelContent = map.get(uuid);
		String channelName = channelContent.split("——")[0];
		if ("".equals(channelName)) {
		} else {
			daoImpl.deleteSelectedChannel(channelName);
		}
		refreshChannelList(request, response);
	}
	
	// 添加渠道号记录
	private void insertChannel(HttpServletRequest request, HttpServletResponse response) {
		ChannelDaoImpl daoImpl = new ChannelDaoImpl();
		Channel channel = new Channel();
		String channelid = request.getParameterValues("insert_channelid")[0]
				.trim();
		String channelname = request.getParameterValues("insert_channelname")[0]
				.trim();
		if ("".equals(channelname) || "".equals(channelid)) {
		} else {
			channel.setChannelId(channelid);
			channel.setChannelName(channelname);
			daoImpl.addChannel(channel);
		}
		refreshChannelList(request, response);
	}
	
	//刷新渠道列表
	private void refreshChannelList(HttpServletRequest request, HttpServletResponse response) {
		channelMap.clear();
		request.getSession().setAttribute("channel_list", channelList);
		try {
			request.getRequestDispatcher("/channelList.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
