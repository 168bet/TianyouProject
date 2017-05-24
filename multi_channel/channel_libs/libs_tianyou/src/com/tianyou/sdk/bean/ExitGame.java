package com.tianyou.sdk.bean;

import java.util.List;

public class ExitGame {

	/**
	 * result :
	 * {"code":200,"msg":"游戏不存在www.tianyouxi.com","productinfo":[{"id":"","name":"","filesize":"","icon":"","download":"","path":""}],"status":0}
	 */

	private ResultBean result;

	public ResultBean getResult() {
		return result;
	}

	public void setResult(ResultBean result) {
		this.result = result;
	}

	public static class ResultBean {
		/**
		 * code : 200 msg : 游戏不存在www.tianyouxi.com productinfo :
		 * [{"id":"","name":"","filesize":"","icon":"","download":"","path":""}]
		 * status : 0
		 */

		private int code;
		private String msg;
		private int status;
		private List<ProductinfoBean> productinfo;

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public List<ProductinfoBean> getProductinfo() {
			return productinfo;
		}

		public void setProductinfo(List<ProductinfoBean> productinfo) {
			this.productinfo = productinfo;
		}

		public static class ProductinfoBean {
			/**
			 * id : name : filesize : icon : download : path :
			 */

			private String id;
			private String name;
			private String filesize;
			private String icon;
			private String download;
			private String path;

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public String getFilesize() {
				return filesize;
			}

			public void setFilesize(String filesize) {
				this.filesize = filesize;
			}

			public String getIcon() {
				return icon;
			}

			public void setIcon(String icon) {
				this.icon = icon;
			}

			public String getDownload() {
				return download;
			}

			public void setDownload(String download) {
				this.download = download;
			}

			public String getPath() {
				return path;
			}

			public void setPath(String path) {
				this.path = path;
			}
		}
	}
}
