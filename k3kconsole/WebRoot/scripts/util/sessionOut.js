$.ajaxSetup({   
	contentType:"application/x-www-form-urlencoded;charset=utf-8",  
	complete:function(xhr,textStatus){ 
		var sessionStatus=xhr.getResponseHeader("sessionStatus");   
		if(sessionStatus == "sessionOut"){   
    		top.location.replace(_ctx+"/");   
    	}
	}
});
		