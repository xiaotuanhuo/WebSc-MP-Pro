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

function imgUpload(body){
	var ua = navigator.userAgent.toLowerCase();  
	var ios = false;
	if (ua.match(/iphone/i) == "micromessenger") {  
	   var ios = true;  
	}
	
	var photoCount = $("#photoCount").val();
	var photoIdx = 0;
	
	if(photoCount > 1){
		alert('只能上传2张照片！')
		return;
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
            
//            var localIdImg = localIds[0];
//            if(ios || window.wxjs_is_wkwebview){
//            	wx.getLocalImgData({
//            		localId: localIdImg, // 图片的localID
//            		success: function (res) {
//            			localIdImg = res.localData; // localData是图片的base64数据，可以用img标签显示
//            		}
//            	});
//            }
//            alert(ios);
//            alert(window.wxjs_is_wkwebview);
            if(ios || window.wxjs_is_wkwebview){
	            wx.getLocalImgData({
	                localId: localIds[0],
	                success: function (res) {
	                    const localData = res.localData;
	                    let imageBase64 = '';
	                    if (localData.indexOf('data:image') == 0) {
	                        //苹果的直接赋值，默认生成'data:image/jpeg;base64,'的头部拼接
	                        imageBase64 = localData;
	                    } else {
	                        //此处是安卓中的唯一得坑！在拼接前需要对localData进行换行符的全局替换
	                        //此时一个正常的base64图片路径就完美生成赋值到img的src中了
	                        imageBase64 = 'data:image/jpeg;base64,' + localData.replace(/\n/g, '');
	                    }
	                    
	                    $.ajax({
	                        url: "/uploadImg",
	                        type:"post",
	                        data:{'myPhoto':imageBase64, 'id': doc.documentId},
	                        dataType:"json",
	                        success:function(data){
	                            if(data.code > 0){
	                                console.log("上传成功");
	                                
	                                var html = '<div id="photo_' + photoIdx + '" name="'+ data.fileName +'" class="file-iteme" style="width: 149px; height: 180px; float: left;"">';
	            	         		html += '<div style="float: left;position: absolute;" id="delPhotoBtn' + photoIdx + '" onclick="deletePhoto(' + photoIdx + ')"><i class="icon-2x icon-times"></i></div>';
	            	        		html += '<img style="width: 150px;height: 180px;" src="/getPhotoByFileName?id=' + doc.documentId + '&FileName='+ data.fileName +'">';
	            	        		html += '</div>';
	
	                                body.find('#photo_list').append(html);
	                                
	                                photoCount++;
	                				photoIdx++;
	                				$("#photoCount").val(photoCount);
	                				
	                				if(photoCount > 1){
	                					body.find("#btnUpload").css("display", "none");
	                				}
	                				
	                				var photo = body.find("#doc_photo").val();
	                				if(photo != '')
	                					photo = photo + "," + data.fileName;
	                				else
	                					photo = data.fileName;
	                				doc.photo = photo;
	                				body.find("#doc_photo").val(photo); 
	                				
	                				//更新状态
	                                var dataJson = "{'type':'t', 'id':'" + doc.documentId + "', 'ds':'3', 'ods':'3', 'photo' : '" + photo + "'}";
	//                                console.info("photo:" + photo);
	                				$.ajax({
	                					type: "POST",
	                			 		url: "/updateDocInfo",
	                			  		data: "jsondata=" + dataJson,
	                					dataType: "json",
	                					success: function(data2){
	                						if(data2.code == 1){
	                							
	                						}else{
	                							alert("网络失败,请重试!");
	                						}
	                			       	},
	                			     	error: function () {
	                			     		alert("网络异常,请重试!");
	                				 	}
	                				});
	                            }else{
	                                console.log("上传失败");
	                            }
	                        },
	                        error:function(){
	                            console.log("上传失败");
	                        }
	                    });
	                },
	                fail: function (res) {
	                	alert('获取本地图片失败，请重试！')
	                }
	            });
            }else{
//            	alert("安卓2");
            	
            	wx.uploadImage({
            	    localId: localIds[0],
            	    isShowProgressTips: 1,
            	    success: function (res) {
            	        var serverId = res.serverId; // 返回图片的服务器端ID
            	        
            	        //获取微信服务器图片并保存到自己的文件服务器
            	        $.ajax({
        					type: "POST",
        			 		url: "/updateDocImage",
        			  		data: "id=" + doc.documentId + "&serverId=" + serverId,
        					dataType: "json",
        					success: function(data){
        						if(data.code == 1){
//        							alert(data.fileName);
        							
        							var html = '<div id="photo_' + photoIdx + '" name="'+ data.fileName +'" class="file-iteme" style="width: 149px; height: 180px; float: left;"">';
	            	         		html += '<div style="float: left;position: absolute;" id="delPhotoBtn' + photoIdx + '" onclick="deletePhoto(' + photoIdx + ')"><i class="icon-2x icon-times"></i></div>';
	            	        		html += '<img style="width: 150px;height: 180px;" src="/getPhotoByFileName?id=' + doc.documentId + '&FileName='+ data.fileName +'">';
	            	        		html += '</div>';
	
	                                body.find('#photo_list').append(html);
	                                
	                                photoCount++;
	                				photoIdx++;
	                				$("#photoCount").val(photoCount);
	                				
	                				if(photoCount > 1){
	                					body.find("#btnUpload").css("display", "none");
	                				}
	                				
	                				var photo = body.find("#doc_photo").val();
	                				if(photo != '')
	                					photo = photo + "," + data.fileName;
	                				else
	                					photo = data.fileName;
	                				doc.photo = photo;
	                				body.find("#doc_photo").val(photo); 
	                				
	                				//更新状态
	                                var dataJson = "{'type':'t', 'id':'" + doc.documentId + "', 'ds':'3', 'ods':'3', 'photo' : '" + photo + "'}";
	                				$.ajax({
	                					type: "POST",
	                			 		url: "/updateDocInfo",
	                			  		data: "jsondata=" + dataJson,
	                					dataType: "json",
	                					success: function(data2){
	                						if(data2.code == 1){
	                							
	                						}else{
	                							alert("网络失败,请重试!");
	                						}
	                			       	},
	                			     	error: function () {
	                			     		alert("网络异常,请重试!");
	                				 	}
	                				});
        						}else{
        							alert("网络失败,请重试!");
        						}
        			       	},
        			     	error: function () {
        			     		alert("网络异常,请重试!");
        				 	}
        				});
            	        
////            	        alert("serverId:" + serverId);
//            	        
//            	        wx.downloadImage({
//            	            serverId: serverId, // 需要下载的图片的服务器端ID，由uploadImage接口获得
//            	            isShowProgressTips: 1,
//            	            success: function (res) {
//            	                var localId = res.localId; // 返回图片下载后的本地ID
//            	                alert(localId);
//            	                if(localId.indexOf("wxlocalresource") != -1){
//            	                	localId = localId.replace("wxlocalresource", "wxLocalResource");
//            	                }
//            	                alert(localId);
//            	                wx.getLocalImgData({
//            		                localId: localId,
//            		                success: function (res) {
//            		                    const localData = res.data;
//            		                    alert(localData);
//            		                    let imageBase64 = '';
//            		                    if (localData.indexOf('data:image') == 0) {
//            		                        //苹果的直接赋值，默认生成'data:image/jpeg;base64,'的头部拼接
//            		                        imageBase64 = localData;
//            		                    } else {
//            		                        //此处是安卓中的唯一得坑！在拼接前需要对localData进行换行符的全局替换
//            		                        //此时一个正常的base64图片路径就完美生成赋值到img的src中了
//            		                        imageBase64 = 'data:image/jpeg;base64,' + localData.replace(/\n/g, '');
//            		                    }
//            		                    
//            		                    alert(imageBase64);
//            		                    
//            		                    
//            		                }
//            		        	});  
//            	            }
//            	        });
            	    },
            	    fail: function (res) {
	                	alert(res);
	                }
            	});
            }
        }
    });
}
		
