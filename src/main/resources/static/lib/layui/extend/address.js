layui.define(['form'],function(exports) {
	var $ = layui.$;
	var form = layui.form,
	Address = function(){};
	
	Address.prototype.provinces = function(provinceCode, cityCode, areaCode) {
		//加载省数据
		var proHtml = '';
		var that = this;
		var citys;
		layui.jquery.get("/lib/layui/json/address.json", function (data) {
			var index = 0;
			if (provinceCode != null) {
				for (var i = 0; i < data.length; i++) {
					if (data[i].code == provinceCode) {
						proHtml += '<option value="' + data[i].code + '">' + data[i].name + '</option>';
						citys = data[i].childs;
						break;
					}
				}
			} else {
				for (var i = 0; i < data.length; i++) {
					proHtml += '<option value="' + data[i].code + '">' + data[i].name + '</option>';
				}
			}
			
			//初始化省数据
			$("select[name=province]").append(proHtml);
			form.render();
			form.on('select(province)', function (proData) {
				$("select[name=area]").html('<option value="">请选择县/区</option>');
				var value = proData.value;
				if (value > 0) {
					if (provinceCode != null) {
						that.citys(citys, cityCode, areaCode);
					} else {
						that.citys(data[$(this).index() - 1].childs, cityCode, areaCode);
					}
				} else {
					$("select[name=city]").attr("disabled", "disabled");
				}
			});
		})
	};
	
	Address.prototype.citys = function(citys, cityCode, areaCode) {
    	var cityHtml = '<option value="">请选择市</option>';
        var that = this;
        var areas;
        if (cityCode != null) {
        	for (var i = 0; i < citys.length; i++) {
        		if (citys[i].code == cityCode) {
        			cityHtml += '<option value="' + citys[i].code + '">' + citys[i].name + '</option>';
        			areas = citys[i].childs;
        			break;
        		}
        	}
        } else {
    		for (var i = 0; i < citys.length; i++) {
    			cityHtml += '<option value="' + citys[i].code + '">' + citys[i].name + '</option>';
    		}
    	}
        
        layui.jquery("select[name=city]").html(cityHtml).removeAttr("disabled");
        form.render();
        form.on('select(city)', function (cityData) {
        	var value = cityData.value;
            if (value > 0) {
            	if (cityCode != null) {
        			that.areas(areas, areaCode);
            	} else {
            		that.areas(citys[$(this).index() - 1].childs);
            	}
            } else {
            	$("select[name=area]").attr("disabled", "disabled");
            }
        });
	};
    
	//加载县/区数据
	Address.prototype.areas = function(areas, areaCode) {
    	var areaHtml = '<option value="">请选择县/区</option>';
        if (areaCode != null) {
        	for (var i = 0; i < areas.length; i++) {
        		if (areas[i].code == areaCode) {
        			areaHtml += '<option value="' + areas[i].code + '">' + areas[i].name + '</option>';
            		break;
            	}
        	}
        } else {
        	for (var i = 0; i < areas.length; i++) {
    			areaHtml += '<option value="' + areas[i].code + '">' + areas[i].name + '</option>';
    		}
        }
        layui.jquery("select[name=area]").html(areaHtml).removeAttr("disabled");
        form.render();
	};
    
	var address = new Address();
	exports("address", function(province, city, area) {
    	address.provinces(province, city, area);
	});
});