<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>K3K</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="<%=basePath %>scripts/jquery/jquery-1.6.4.min.js"></script>
<script type="text/javascript">
	
</script>
</head>
<body>
<div class="chknumber">  
       <label>验证码：          
       <input name="kaptcha" type="text" id="kaptcha" maxlength="6" class="chknumber_input" />               
       </label>  
        <img src="<%=basePath%>captcha/image" width="155" height="45" 
        	 id="kaptchaImage" title="点击刷新" style="cursor: pointer;"/>   
       <script type="text/javascript">      
        $(function(){           
            $('#kaptchaImage').click(function () {//生成验证码  
            	$(this).hide().attr('src', '<%=basePath%>captcha/image?' + Math.floor(Math.random()*100) ).fadeIn(); 
            });    
        });
       </script>   
     </div>
</body>
</html>
