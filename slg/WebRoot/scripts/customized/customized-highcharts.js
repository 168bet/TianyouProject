function draw(containerId, xAxis, data, title, subtitle, seriesname, contextPath){
	
	$('#'+containerId).highcharts({
    	chart: {
            type: 'line'
        },
        noData: {
            style: {
                fontWeight: 'bold',
                fontSize: '15px',
                color: '#FFFFFF'
            }
        },
        lang: {
            noData: "没有数据符合该检索条件！",
            loading: 'Loading ... ',
    		printChart: '保存统计图',
    		downloadJPEG: '保存为JPEG图片',
    		downloadPDF: '保存为PDF文档',
    		downloadPNG: '保存为PNG图片',
    		downloadSVG: '保存为SVG矢量图片'
        },
        title: {
            text: title
        },
        subtitle: {
            text: subtitle
        },
        credits: {
	        text: 'http://un.k3k.com',
			href: 'http://un.k3k.com'
        },
        exporting: {
			enabled: true,
			filename: 'charts',  
            url: contextPath + 'highcharts/export'//这里是一个重点哦,也可以修改exporting.js中对应的url 
        },
        xAxis: {
            categories: xAxis,
            labels: { 
                rotation: -75, 
                style: { 
                    fontSize: '13px', 
                    fontFamily: 'Verdana, sans-serif'
                }
            }
        },
        yAxis: {
            title: {
                text: '数量'
            },
            labels: {
                formatter: function() {
                    return this.value
                }
            }
        },	
        tooltip: {
            crosshairs: true,
            shared: true
        },
        plotOptions: {
            spline: {
                marker: {
                    radius: 4,
                    lineColor: '#666666',
                    lineWidth: 1
                }
            }
        },
        series: [{
            name: seriesname,
            marker: {
                symbol: 'square'
            },
            data: data
        }]
    }); 
}

function draw1(containerId, xAxis, dataArr, title, subtitle, seriesNameArr, contextPath){
	$('#'+containerId).highcharts({
    	chart: {
            type: 'line'
        },
        noData: {
            style: {
                fontWeight: 'bold',
                fontSize: '15px',
                color: '#FFFFFF'
            }
        },
        lang: {
            noData: "没有数据符合该检索条件！",
            loading: 'Loading ... ',
    		printChart: '保存统计图',
    		downloadJPEG: '保存为JPEG图片',
    		downloadPDF: '保存为PDF文档',
    		downloadPNG: '保存为PNG图片',
    		downloadSVG: '保存为SVG矢量图片'
        },
        title: {
            text: title
        },
        subtitle: {
            text: subtitle
        },
        credits: {
	        text: 'http://un.k3k.com',
			href: 'http://un.k3k.com'
        },
        exporting: {
			enabled: true,
			filename: 'charts',  
            url: contextPath + 'highcharts/export'//这里是一个重点哦,也可以修改exporting.js中对应的url 
        },
        xAxis: {
            categories: xAxis,
            labels: { 
                rotation: -75, 
                style: { 
                    fontSize: '13px', 
                    fontFamily: 'Verdana, sans-serif'
                }
            }
        },
        yAxis: {
            title: {
                text: '数量'
            },
            labels: {
                formatter: function() {
                    return this.value
                }
            }
        },	
        tooltip: {
            crosshairs: true,
            shared: true
        },
        plotOptions: {
            spline: {
                marker: {
                    radius: 4,
                    lineColor: '#666666',
                    lineWidth: 1
                }
            }
        },
        series: [{
            name: seriesNameArr[0],
            data: dataArr[0]
        },{
        	name: seriesNameArr[1],
        	data: dataArr[1]
        },{
        	name: seriesNameArr[2],
        	data: dataArr[2]
        }]
    }); 

}



function draw2(containerId, xAxis, title, subtitle, seriesObj, contextPath){
	$('#'+containerId).highcharts({
    	chart: {
            type: 'line'
        },
        noData: {
            style: {
                fontWeight: 'bold',
                fontSize: '15px',
                color: '#FFFFFF'
            }
        },
        lang: {
            noData: "没有数据符合该检索条件！",
            loading: 'Loading ... ',
    		printChart: '保存统计图',
    		downloadJPEG: '保存为JPEG图片',
    		downloadPDF: '保存为PDF文档',
    		downloadPNG: '保存为PNG图片',
    		downloadSVG: '保存为SVG矢量图片'
        },
        title: {
            text: title
        },
        subtitle: {
            text: subtitle
        },
        credits: {
	        text: 'http://un.k3k.com',
			href: 'http://un.k3k.com'
        },
        exporting: {
			enabled: true,
			filename: 'charts',  
            url: contextPath + 'highcharts/export'//这里是一个重点哦,也可以修改exporting.js中对应的url 
        },
        xAxis: {
            categories: xAxis,
            labels: { 
                rotation: -75, 
                style: { 
                    fontSize: '13px', 
                    fontFamily: 'Verdana, sans-serif'
                }
            }
        },
        yAxis: {
            title: {
                text: '数量'
            },
            labels: {
                formatter: function() {
                    return this.value
                }
            }
        },	
        tooltip: {
            crosshairs: true,
            shared: true
        },
        plotOptions: {
            spline: {
                marker: {
                    radius: 4,
                    lineColor: '#666666',
                    lineWidth: 1
                }
            }
        },
        series: seriesObj
    }); 

}