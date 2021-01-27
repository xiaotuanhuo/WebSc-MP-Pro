//Brand开始
$(document).ready(function(){
    $(".Brand").click(function(){
        if ($('.Category-eject').hasClass('grade-w-roll')) {
            $('.Category-eject').removeClass('grade-w-roll');
			$(this).removeClass('current');
        } else {
            $('.Category-eject').addClass('grade-w-roll');
			$(this).addClass('current');
			$(".Sort").removeClass('current');
			$('.screening').attr('style','position: relative; top: 0;');
        }
    });
});

//Sort开始
$(document).ready(function(){
    $(".Sort").click(function(){
        if ($('.Sort-eject').hasClass('grade-w-roll')) {
            $('.Sort-eject').removeClass('grade-w-roll');
			$(this).removeClass('current');
			//$('.screening').attr('style','position: fixed;top:0;');
        } else {
        	$('.Sort-eject').addClass('grade-w-roll');
			$(this).addClass('current');
			$(".Brand").removeClass('current');
			$('.screening').attr('style','position: relative; top: 0;');
        }
    });
});

//判断页面是否有弹出
$(document).ready(function() {
	$(".Brand").click(function(){
		if ($('.Sort-eject').hasClass('grade-w-roll')) {
			$('.Sort-eject').removeClass('grade-w-roll');
		};
	});
});

$(document).ready(function(){
    $(".Sort").click(function(){
        if ($('.Category-eject').hasClass('grade-w-roll')){
            $('.Category-eject').removeClass('grade-w-roll');
        };
    });
});

//js点击事件监听开始
function Categorytw(wbj){
	$('.Categoryw').find('li').each(function() {
		if ($(this).hasClass('selected')) {
			$(this).removeClass('selected');
		}
	});
	$(wbj).addClass('selected');
	$('.Category-eject').removeClass('grade-w-roll');
	$('.Brand').attr("data-data", $(wbj).data("code"));
	$('.Brand').find('span').text($(wbj).text());
	$('.Brand').removeClass('current');
    /*var arr = document.getElementById("Categorytw").getElementsByTagName("li");
    for (var i = 0; i < arr.length; i++){
        var a = arr[i];
        a.style.background = "";
    };
	wbj.style.background = "#eee"*/
}

function Sorts(sbj) {
	if ($(sbj).data("type") == "sortCustom") {	// 点击自定义的处理
		$('.Sort-eject').removeClass('grade-w-roll');
		$('.Sort').removeClass('current');
	} else {
		$('.Sort-Sort').find('li').each(function() {
			if ($(this).hasClass('selected')) {
				$(this).removeClass('selected');
			}
		});
		$(sbj).addClass('selected');
		$('.Sort-eject').removeClass('grade-w-roll');
		$('.Sort').attr("data-data", $(sbj).data("type"));
		$('.Sort').find('span').text($(sbj).text());
		$('.Sort').removeClass('current');
	}
    /*var arr = document.getElementById("Sort-Sort").getElementsByTagName("li");
    for (var i = 0; i < arr.length; i++){
        var a = arr[i];
        a.removeClass('selected');
    };
    sbj.addClass('selected');*/
}
