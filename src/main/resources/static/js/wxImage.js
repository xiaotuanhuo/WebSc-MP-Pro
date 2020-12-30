/**
 * 微信订单抬头
 */
//图片上传成功标志
var uploadSuccesFlag = "FAIL";
var thisimg;
function previewImg(){
	
    wx.ready(function(){
        wx.previewImage({
            current: thisimg[0], // 当前显示图片的http链接
            urls: thisimg // 需要预览的图片http链接列表
        });
    });
}

function imgUpload(){
	var ua = navigator.userAgent.toLowerCase();  
	var ios = false;
	if (ua.match(/iphone/i) == "micromessenger") {  
	   var ios = true;  
	}
	
	wx.chooseImage({
        count: 1, // 默认9
        sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
        sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
        success: function (res) {
            var localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
            thisimg = localIds;
            if(res.localIds.indexOf("wxlocalresource") != -1){
                res.localIds = res.localIds.replace("wxlocalresource", "wxLocalResource");
            }
            
            var localIdImg = localIds[0];
            if(ios || window.wxjs_is_wkwebview){
            	wx.getLocalImgData({
            		localId: localIdImg, // 图片的localID
            		success: function (res) {
            		localIdImg = res.localData; // localData是图片的base64数据，可以用img标签显示
            		}
            	});
            }
            
            wx.uploadImage({
                localId: localIds.toString(), // 需要上传的图片的本地ID，由chooseImage接口获得
                isShowProgressTips: 1, // 默认为1，显示进度提示
                success: function (res) {
                    var mediaId = res.serverId; // 返回图片的服务器端ID，即mediaId
                    $(".layui-m-layercont #pic").attr("src",localIdImg);
                    $(".layui-m-layercont #mediaId").val(mediaId);
                    //var documentId = docment.documentId;
					//var formData = JSON.stringify({'mediaId':mediaId,'documentId':documentId});
                    //将获取到的 mediaId 传入后台 方法savePicture
                    /*$.ajax({
				        type: "POST",
					    data: formData,
					    dataType: "json",
					    contentType: "application/json"  ,//必须加
					    async:false,
				        url: 'savePictureWoapp',
				        success: function (data) {
				        	console.log(data.file);
				        	uploadSuccesFlag = "SUCCESS";
				        },
				        error: function (XMLHttpRequest, textStatus, errorThrown) {
		                    // 状态码
		                    console.log(XMLHttpRequest.status);
		                    // 状态
		                    console.log(XMLHttpRequest.readyState);
		                    // 错误信息   
		                    console.log(textStatus);
		                    
		                   // window.location.href="toXPDocExceptionError";
		                }
                    });*/ 
                },
                fail: function (res) {
                    alertModal('上传图片失败，请重试')
                }
            }); 
        }
    });
}
		
