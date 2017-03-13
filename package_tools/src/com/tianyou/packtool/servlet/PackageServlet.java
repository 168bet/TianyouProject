package com.tianyou.packtool.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.tianyou.packtool.domain.Channel;
import com.tianyou.packtool.impl.ChannelDaoImpl;

@SuppressWarnings("serial")
public class PackageServlet extends BaseServlet {

	private ChannelDaoImpl daoImpl = new ChannelDaoImpl();
	private List<Channel> channelList = new ArrayList<Channel>();
	private Map<String, String> channelMap = new LinkedHashMap<String, String>();
	private int currentPage = 0;
	
	@Override
	public void doOperate(String op, HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		if ("pkg_dir_list".equals(op)) {
			displayApkDirlist(request, response);
		} else if ("last_page".equals(op)) {
			queryLastPage(request, response);
		} else if ("next_page".equals(op)) {
			queryNextPage(request, response);
		}
	}
	
	// 显示已完成的包目录列表
	private void displayApkDirlist(HttpServletRequest request, HttpServletResponse response) {
		File file = new File(PATH_PACKAGE);
		List<String> pkgDirList = new ArrayList<String>();
		File[] fs = file.listFiles();
		if (fs != null) {
			for (File f : fs) {
				pkgDirList.add(f.getName());
			}
		}
		System.out.println("打包目录列表：" + pkgDirList);
		request.getSession().setAttribute("pkg_dir_list", pkgDirList);
		try {
			request.getRequestDispatcher("/pkgDirList.jsp").forward(request,response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		List<String> channelIdLists = new ArrayList<String>();
		List<Channel> channelLists = new ArrayList<Channel>();
		if (checkboxValues != null) {
			for (String value : checkboxValues) {
				channelIdLists.add(value);
				Channel channel = daoImpl.queryChannelByName(value);
				channelLists.add(channel);
			}
		}
		System.out.println("已选渠道：" + channelLists);
		HttpSession session = request.getSession();
		List<Channel> selectChannels = (List<Channel>)session.getAttribute("select_channel");
		if (selectChannels != null) {
			for (Channel channel : channelLists) {
				boolean flag = true;
				for (Channel c : selectChannels) {
					if (c.getChannelId().equals(channel.getChannelId())) flag = false;
				}
				if (flag) selectChannels.add(channel);
			}
		} else {
			selectChannels = channelLists;
		}
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
		if (channelId != null) daoImpl.updateChannel(channelName, channelId);
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
