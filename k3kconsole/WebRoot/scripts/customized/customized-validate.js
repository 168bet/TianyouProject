$.extend($.fn.validatebox.defaults.rules, {
	equalTo : {
		validator : function(value, param) {
			return value == $(param[0]).val();
		},
		message : '密码不一致'
	},
	validateUnionExist : {
		validator : function(value, param) {
			var postdata = {};
			postdata['unionId'] = value;
			var result = $.ajax({
				url: param[0],
				data: postdata,
				type: 'post',
				dataType: 'json',
				async: false,
				cache: false
			}).responseText;
			if(result == 'true'){
				$.fn.validatebox.defaults.rules.validateUnionExist.message = '主联盟ID已存在';
				return false;
			}
			return true;
		},
		message : '主联盟ID已存在'
	},
	validateUnionNotExist : {
		validator : function(value, param) {
			var postdata = {};
			postdata['unionId'] = value;
			var result = $.ajax({
				url: param[0],
				data: postdata,
				type: 'post',
				dataType: 'json',
				async: false,
				cache: false
			}).responseText;
			if(result == 'false'){
				$.fn.validatebox.defaults.rules.validateUnionNotExist.message = '主联盟ID不存在';
				return false;
			}
			return true;
		},
		message : '主联盟ID已存在'
	},
	validateChildUnionExist : {
		validator : function(value, param) {
			var postdata = {};
			var unionId = $(param[1]).val();
			if(unionId==null || unionId==''){
				$.fn.validatebox.defaults.rules.validateChildUnionExist.message = '请填写主联盟ID';
				return false;
			}
			postdata['unionId'] = $(param[1]).val();			
			postdata['childUnionId'] = value;
			result = $.ajax({
				url: param[0],
				data: postdata,
				type: 'post',
				dataType: 'json',
				async: false,
				cache: false
			}).responseText;
			if(result == 'true'){
				$.fn.validatebox.defaults.rules.validateChildUnionExist.message = '子联盟ID已存在';
				return false;
			}
			return true;
		},
		message : ''		
	}
});

$.extend($.fn.validatebox.defaults.rules, {
	integer : {// 验证整数
		validator : function(value) {
			return /^[+]?[0-9]+\d*$/i.test(value);
		},
		message : '请输入整数'
	},
	floatOrInt : {// 验证是否为小数或整数
		validator : function(value) {
			return /^(\d{1,3}(,\d\d\d)*(\.\d{1,3}(,\d\d\d)*)?|\d+(\.\d+))?$/i.test(value);
		},
		message : '请输入整数或小数'
	},
	minValue : {//验证最小值
		validator : function(value, param) {
			$.fn.validatebox.defaults.rules.minValue.message = '最小值为'+param[0]+'！';
			return value >= param[0];
		},
		message : ''
	}
});

$.extend($.fn.validatebox.defaults.rules, {
	userName: {
	// param 参数集合
		validator: function (value, param) {
			if (value.length < param[0]) {
				$.fn.validatebox.defaults.rules.userName.message = '用户名长度最低' + param[0] + '位！';
				return false;
			} else {
				if (!/^[\w]+$/.test(value)) {
					$.fn.validatebox.defaults.rules.userName.message = '用户名只能英文字母、数字及下划线的组合！';
					return false;
				} else {
					var postdata = {};
					if (param[3]) {
						postdata[param[2]] = param[3];
					} else {
						postdata[param[2]] = value;
					}
					postdata['editMode'] = $('#editMode').val();
					var result = $.ajax({
						url: param[1],
						data: postdata,
						type: 'post',
						dataType: 'json',
						async: false,
						cache: false
					}).responseText;
					if (result == 'false') {
						$.fn.validatebox.defaults.rules.userName.message = '用户名已存在！';
						return false;
					} else {
						return true;
					}
				}
			}
		},
		message: ''
	}
});