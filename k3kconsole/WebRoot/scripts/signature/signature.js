/*attachId 附件id，  editType操作类型 0、查看  1、修改无痕迹2、修改有痕迹3、手写 4、签章 */
function sckanDocument(attachId,editType){
	var url = null;
	switch(editType){
	  case 0 :{
		  url = "/DocumentEdit.jsp?RecordID="+attachId+"&EditType=0,0&ShowType=1&visibledType=0&UserName=";
	  }
	  case 1 : {
		  url = "/DocumentEdit.jsp?RecordID="+attachId+"&EditType=1,1&ShowType=1&visibledType=1&UserName=";
	  }
	  case 2 : {
		  url = "/DocumentEdit.jsp?RecordID="+attachId+"&EditType=2,1&ShowType=1&visibledType=1&UserName=";
		  
	  }
	  case 3 :{
		  url = "/DocumentEdit.jsp?RecordID="+attachId+"&EditType=3,1&ShowType=2&visibledType=1&UserName=";
	  }
	  case 4 :{
		  url = "/DocumentEdit.jsp?RecordID="+attachId+"&EditType=3,0&ShowType=0&visibledType=1&UserName=";
	  }
	  default : {
		  url = "/DocumentEdit.jsp?RecordID="+attachId+"&EditType=0,0&ShowType=1&visibledType=0&UserName=";
	  }
	
	}
	$.ajax({
		url : "/EBMS/signature/getFileType",
		type : "POST",
		dataType : "json",
		async : false,
		data : "recordId=" + attachId,
		success : function(data) {
			if(data.isSuccess){
				url = "signature/" + data.fileType + url;
				openDocument(url);
			}else{
				alert(data.message);
			}
		}
	});
}

function openDocument(url){
	/*var diag = new Dialog();
	diag.Width = window.screen.width;
	diag.Height = window.screen.height;
	diag.Title = "文档浏览";
	diag.URL = url;
	diag.show();  */
	$.ajax({
		url : "/EBMS/signature/getSystemData",
		type : "POST",
		dataType : "json",
		async : false,
		success : function(data) {
			url = data.basePath+url+data.accountId;
			window.showModelessDialog(url,"","dialogWidth="+window.screen.width+";dialogHeight="+window.screen.height+";minimize=yes;maximize=yes;status=no;resizable=yes;location=no");
		}
	});
	
}