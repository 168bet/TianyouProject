function trim(str){  
    return str.replace(/(^\s*)|(\s*$)/g, "");  
}  
function isEmpty (str) {
	if ((str==null)||(trim(str).length==0)) return true;
	else return(false);
}
//校验是否全由数字组成
function isDigit(s)
{
	var patrn=/^[0-9]{1,20}$/;
	if (!patrn.exec(s)) return false
	return true
}
//校验登录名：只能输入5-20个以字母开头、可带数字、“_”、“.”的字串
function isRegisterUserName(s)
{
	var patrn=/^[a-zA-Z]{1}([a-zA-Z0-9]|[._]){4,19}$/;
	if (!patrn.exec(s)) return false
	return true
}
//校验用户姓名：只能输入1-30个以字母开头的字串
function isTrueName(s)
{
	var patrn=/^[a-zA-Z]{1,30}$/;
	if (!patrn.exec(s)) return false
	return true
}
//校验密码：只能输入6-20个字母、数字、下划线
function isPasswd(s)
{
	var patrn=/^(\w){6,20}$/;
	if (!patrn.exec(s)) return false
	return true
}
//校验普通电话、传真号码：可以“+”开头，除数字外，可含有“-”
function isTel(s)
{ 
	var patrn=/^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/;
	if (!patrn.exec(s)) return false
	return true
}
//校验手机号码：必须以数字开头，除数字外，可含有“-”
function isMobil(s)
{
	var patrn=/^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/;
	if (!patrn.exec(s)) return false
	return true
}
//校验邮政编码
function isPostalCode(s)
{
	var patrn=/^[a-zA-Z0-9 ]{3,12}$/;
	if (!patrn.exec(s)) return false
	return true
}
 
function isIP(s) 
{
	var patrn=/^[0-9.]{1,20}$/;
	if (!patrn.exec(s)) return false
	return true
}

function isBetween (val, lo, hi) {
	if ((val < lo) || (val > hi)) { return(false); }
	else { return(true); }
}

function isEmail (theStr) {
	var atIndex = theStr.indexOf('@');
	var dotIndex = theStr.indexOf('.', atIndex);
	var flag = true;
	theSub = theStr.substring(0, dotIndex+1)
	if ((atIndex < 1)||(atIndex != theStr.lastIndexOf('@'))||(dotIndex < atIndex + 2)||(theStr.length <= theSub.length))
	{ return(false); }
	else { return(true); }
}

function isDate (theStr) {
	var the1st = theStr.indexOf('-');
	var the2nd = theStr.lastIndexOf('-');
	if (the1st == the2nd) { 
		return(false); 
	}
	else {
		var y = theStr.substring(0,the1st);
		var m = theStr.substring(the1st+1,the2nd);
		var d = theStr.substring(the2nd+1,theStr.length);
		var maxDays = 31;
		if (isInt(m)==false || isInt(d)==false || isInt(y)==false) {
		return(false); }
		else if (y.length < 4) { return(false); }
		else if (!isBetween (m, 1, 12)) { return(false); }
		else if (m==4 || m==6 || m==9 || m==11) maxDays = 30;
		else if (m==2) {
		if (y % 4 > 0) maxDays = 28;
		else if (y % 100 == 0 && y % 400 > 0) maxDays = 28;
		else maxDays = 29;
		}
		if (isBetween(d, 1, maxDays) == false) {return(false); }
		else { return(true); }
	}
}

"^\\d+$"　　//非负整数（正整数 + 0）
"^[0-9]*[1-9][0-9]*$"　　// 正整数
"^((-\\d+)|(0+))$"　　//非正整数（负整数 + 0）
"^-[0-9]*[1-9][0-9]*$"　　 //负整数
"^-?\\d+$"　　　　//整数
"^\\d+(\\.\\d+)?$"　　//非负浮点数（正浮点数 + 0）
"^(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*))$" 　　//正浮点数
"^((-\\d+(\\.\\d+)?)|(0+(\\.0+)?))$"　　//非正浮点数（负浮点数 + 0）
"^(-(([0-9]+\\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\\.[0-9]+)|([0-9]*[1-9][0-9]*)))$" 　　//负浮点数
"^(-?\\d+)(\\.\\d+)?$"　　//浮点数
"^[A-Za-z]+$"　　//由26个英文字母组成的字符串
"^[A-Z]+$"　　//由26个英文字母的大写组成的字符串
"^[a-z]+$"　　//由26个英文字母的小写组成的字符串
"^[A-Za-z0-9]+$"　　//由数字和26个英文字母组成的字符串
"^\\w+$"　　//由数字、26个英文字母或者下划线组成的字符串
"^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$"　　　　 //email地址
"^[a-zA-z]+://(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*(\\?\\S*)?$" 　　//url 