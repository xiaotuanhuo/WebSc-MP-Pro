var Slideicon = function(element, options) {
	this.element = element;
	// 控件宽度
	var eleWidth = this.element.width();
	// 子元素个数
	var len = this.element.children().length;
	// 每个子元素宽度
	var childWidth = eleWidth / len; 
	this.options = {
		cover : options.cover,
		index : options.index,
		callback : options.callback,
		childWidth : childWidth
	};
	this.init();
};
Slideicon.prototype.init = function() {
	var _this = this;
	var left = (_this.options.index) * (_this.options.childWidth);
	_this.options.cover.attr("style", "left:" + left + "px");
	this.element.on('click', 'li', function() {
		$(this).nextAll().removeClass('active');
		$(this).prevAll().removeClass('active');
		var width = $(this).width();
		var left = ($(this).index()) * width;
		_this.options.cover.attr("style", "left:" + left + "px");
		$(this).addClass("active");
		params = $(this).attr('data-type');
		_this.options.callback(params)
	});
};

