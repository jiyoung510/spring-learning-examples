<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	.uploadResult {
		width: 100%;
		background-color:lightgray;
	}
	
	.uploadResult ul {
	display:flex;
	flex-flow: row;
	justify-content: center;
	align-items: center;
	}
	
	.uploadResult ul li {
	list-style: none;
	padding: 10px;
	}
	
	.uploadResult ul li img {
		width: 100px;
	}
	
	.uploadResult ul li span {
		color : white;
	}
	
	.bigPictureWrapper {
		position: absolute;
		display: none;
		justify-content: center;
		align-item: center;
		top: 0%;
		width: 100%;
		height: 100%;
		background-color: gray;
		z-index: 100;
		background: rgba(225, 225, 225, 0.5);
	}
	
	.bigPicture {
		position: relative;
		display: flex;
		justify-content: center;
		align-items: center;
	}
	
	.bigPicture img {
		width: 600px;
	}
</style>
</head>
<body>
	<div class="bigPictureWrapper">
		<div class="bigPicture">
		</div>
	</div>
	<h1>Upload with Ajax</h1>
	
	<div class="uploadDiv">
		<input type="file" name="uploadFile" multiple>
	</div>
	<div class="uploadResult">
		<ul>
		
		</ul>
	</div>
	
	<button id="uploadBtn">upload</button>
	
	<script
  src="https://code.jquery.com/jquery-3.6.1.min.js"
  integrity="sha256-o88AwQnZB+VDvE9tvIXrMQaPlFFSUTR+nldQm1LuPXQ="
  crossorigin="anonymous"></script>
  <script>
  	function showImage(fileCallPath){
  		$(".bigPictureWrapper").css("display","flex").show();
  		
  		$(".bigPicture")
  			.html("<img src='/display?fileName=" + encodeURI(fileCallPath) + "'>'")
  			.animate({width: '100%', height:'100%'}, 1000);
  	}
  
  	$(document).ready(function() {
  		// 업로드 파일 확장자 제한
  		var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
  		var maxSize = 5242880; // 5MB
  		
  		function checkExtension(fileName, fileSize) {
  			if(fileSize >= maxSize){
  				alert("파일 사이즈 초과");
  				return false;
  			}
  			if(regex.test(fileName)){
  				alert("해당 종류의 파일은 업로드할 수 없습니다.");
  				return false;
  			}
  			return true;
  		}
  		
  		var cloneObj = $(".uploadDiv").clone();
  		// 첨부파일을 업로드하기 전, 아무 내용이 없는 <input type='file'> 객체가 포함된 <div>를 복사(clone)
  		
  		var uploadResult = $(".uploadResult ul");
  		
  		function showUploadedFile(uploadResultArr) {
  			var str = "";
  			
  			$(uploadResultArr).each(function(i, obj) {
  				if(!obj.image) {
  					
  					var fileCallPath = encodeURIComponent(obj.uploadPath + "/" + obj.uuid + "_" + obj.fileName);
  					str += "<li><div><a href='/download?fileName=" 
  							+ fileCallPath 
  							+ "'>" 
  							+ "<img src='/resources/img/attach.png'>" 
  							+ obj.fileName + "</a>"
  							+ "<span data-file=\'>"
  							+ fileCallPath
  							+ "\' data-type='file'> X </span>"
  							+"</div></li>";
  				} else {
	  				// str += "<li>" + obj.fileName + "</li>";
	  				
	  				var fileCallPath = encodeURIComponent(obj.uploadPath + "/s_" + obj.uuid + "_" + obj.fileName);
	  				var originalPath = obj.uploadPath + "\\" + obj.uuid + "_" + obj.fileName;
	  				originalPath = originalPath.replace(new RegExp(/\\/g), "/");
	  				
	  				str += "<li><a href=\"javascript:showImage(\'" 
	  						+ originalPath 
	  						+ "\')\">"
	  						+ "<img src='/display?fileName=" 
							+ fileCallPath 
							+ "'></a>"
							+"<span data-file=\'"
							+ fileCallPath
							+ "\' data-type='image'> X </span>" 
							+"</li>";
  				}
  			});
  			
  			uploadResult.append(str);
  		}
  		
  		$(".bigPictureWrapper").on("click", function(e) {
  			$(".bigPicture").animate({width: '0%', height: '0%'}, 1000);
  			setTimeout(() => {
  				$(this).hide();
  			}, 1000);
  		});
  		
  		$("#uploadBtn").on("click", function(e) {
  			var formData = new FormData();
  			
  			var inputFile = $("input[name='uploadFile']");
  			
  			var files = inputFile[0].files;
  			
  			console.log(files);
  			
  			// add File Data to formData
  			for(var i = 0; i < files.length; i++) {
  				if(!checkExtension(files[i].name, files[i].size)) {
  					return false;
  				}
  				formData.append("uploadFile", files[i]);
  			}
  			
  			$.ajax({
  				url : '/uploadAjaxAction',
  				processData : false,
  				contentType : false,
  				data : formData,
  				type: 'POST',
  				dataType: 'json',
  				success : function(result) {
  					console.log(result);
  					
  					showUploadedFile(result);
  					
  					$(".uploadDiv").html(cloneObj.html());
  					// 첨부파일을 업로드한 뒤 복사된 객체를 <div>내에 다시 추가하여 첨부파일 부분을 초기화
  				}
  			}); // $.ajax
  		});
  		
  		$(".uploadResult").on("click", "span", function(e) {
  			var targetFile = $(this).data("file");
  			var type = $(this).data("type");
  			console.log(targetFile);
  			
  			$.ajax({
  				url: '/deleteFile',
  				data: {fileName: targetFile, type:type},
  				dataType: 'text',
  				type: 'POST',
  				success: function(result){
  					alert(result);
  				}
  			}); // $.ajax
  		});
  		
  	});
  </script>
</body>
</html>