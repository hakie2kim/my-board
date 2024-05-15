<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">

    <!-- viewport meta -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="MartPlace - Complete Online Multipurpose Marketplace HTML Template">
    <meta name="keywords" content="marketplace, easy digital download, digital product, digital, html5">
    <title>포트폴리오</title>

    <!-- inject:css -->
    <link rel="stylesheet" href="<%=ctx%>/assest/template/css/animate.css">
    <link rel="stylesheet" href="<%=ctx%>/assest/template/css/font-awesome.min.css">
    <link rel="stylesheet" href="<%=ctx%>/assest/template/css/fontello.css">
    <link rel="stylesheet" href="<%=ctx%>/assest/template/css/jquery-ui.css">
    <link rel="stylesheet" href="<%=ctx%>/assest/template/css/lnr-icon.css">
    <link rel="stylesheet" href="<%=ctx%>/assest/template/css/owl.carousel.css">
    <link rel="stylesheet" href="<%=ctx%>/assest/template/css/slick.css">
    <link rel="stylesheet" href="<%=ctx%>/assest/template/css/trumbowyg.min.css">
    <link rel="stylesheet" href="<%=ctx%>/assest/template/css/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="<%=ctx%>/assest/template/css/style.css">
	<link rel="stylesheet" href="<%=ctx%>/assest/template/css/trumbowyg.min.css">
    <!-- endinject -->

    <!-- Favicon -->
    <link rel="icon" type="image/png" sizes="16x16" href="<%=ctx%>/assest/template/images/favicon.png">    
	<script type="text/javascript">
		var ctx = '<%= request.getContextPath() %>';
	</script>
	<!-- 추가 시작 -->
	<script src="http://code.jquery.com/jquery-latest.js"></script>
	<!-- 추가 끝 -->
	<script src="<%=ctx%>/assest/js/page.js"></script>
	<link rel="stylesheet" href="<%=ctx%>/assest/template/css/trumbowyg.min.css">
    <script src="<%=ctx%>/assest/template/js/vendor/trumbowyg.min.js"></script>
    <script src="<%=ctx%>/assest/template/js/vendor/trumbowyg/ko.js"></script>
    <script type="text/javascript">
	    $('#trumbowyg-demo')
	    .trumbowyg({
	        lang: 'kr'
	    })
	    
	    window.onload = function() {
	    	$('#trumbowyg-demo').text('${board.content}');
	    	$('#trumbowyg-demo').on('tbwchange', function(){ 
				$('#content').val($(this).text());
	    	});
	    }
	    
	    function removeFile(attachSeq) {
	    	let url = "<%=ctx%>/forum/notice";
	    	url += "/" + attachSeq;
	    	url += "/removeFile.do";
	    	
	    	$.ajax({    
	    		type : "delete",           
	    		// 타입 (get, post, put 등등)    
	    		url : url,
	    		// 요청할 서버url
	    		// async : true,
	    		// 비동기화 여부 (default : true)
	    		headers : {
	    			// Http header
	    			"Content-Type" : "application/json"
	    			// "X-HTTP-Method-Override" : "POST"
	    		},
	    		dataType : "text",
    			// 결과 성공 콜백함수 
	    		success : function(result) {
	    			if (result === '1') {
	    				window.location.reload();
	    			}
	    		},
    			// 결과 에러 콜백함수
	    		error : function(request, status, error) {
	    			console.log(error);
	    		}
	    	});
	    }
	</script>
</head>

<body class="preload home1 mutlti-vendor">
    <!--================================
            START DASHBOARD AREA
    =================================-->
    <section class="support_threads_area section--padding2">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <div class="question-form cardify p-4">
                        <form action="<c:url value='/forum/notice/modify.do?boardSeq=${board.boardSeq}&boardTypeSeq=${board.boardTypeSeq}' />" method="post">
                            <div class="form-group">
                                <label>제목</label>
                                <input type="text" name="title" placeholder="Enter title here" value="${board.title}" required>
                            </div>
                            <div class="form-group">
                                <label>Description</label>
                                <div id="trumbowyg-demo"></div>
                                <input type="hidden" id="content" name="content" />
                            </div>
                            <c:forEach var="attFile" items="${attFiles}">
                            	<div class="form-group" style="display:flex; justify-content:space-between;">
									<a href='<%=ctx%>/forum/notice/downloadFile.do?attSeq=${attFile.attachSeq}'>
										${attFile.orgFileNm} (${attFile.fileSize}) 
									</a>
									<div onClick="javascript:removeFile(${attFile.attachSeq})">X</div>
									<%-- <a href='<%=ctx%>/forum/notice/deleteFile.do?attSeq=${attFile.attachSeq}'>X</a> --%>
                            	</div>
                            </c:forEach>
                            <c:forEach begin="1" end="${3-fn:length(attFiles)}">
	                            <div class="form-group">
	                                <div class="attachments">
	                                    <label>Attachments</label>
	                                    <label>
	                                        <span class="lnr lnr-paperclip"></span> Add File
	                                        <span>or Drop Files Here</span>
	                                        <input type="file" style="display:inline-block;">
	                                    </label>
	                                </div>
	                            </div>
                            </c:forEach>
                            <div class="form-group">
                                <button type="submit" class="btn btn--md btn-primary">Submit Request</button>
                            	<a href="<c:url value='/forum/notice/listPage.do'/>" class="btn btn--md btn-light">Cancel</a>
                            </div>
                        </form>
                    </div><!-- ends: .question-form -->
                </div>
                <!-- end .col-md-12 -->
            </div>
            <!-- end .row -->
        </div>
        <!-- end .container -->
    </section>
    <!--================================
            END DASHBOARD AREA
    =================================-->
   	<!--//////////////////// JS GOES HERE ////////////////-->
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyA0C5etf1GVmL_ldVAichWwFFVcDfa1y_c"></script>
    <!-- inject:js -->
    <script src="<%=ctx%>/assest/template/js/vendor/jquery/jquery-1.12.3.js"></script>
    <script src="<%=ctx%>/assest/template/js/vendor/jquery/popper.min.js"></script>
    <script src="<%=ctx%>/assest/template/js/vendor/jquery/uikit.min.js"></script>
    <script src="<%=ctx%>/assest/template/js/vendor/bootstrap.min.js"></script>
    <script src="<%=ctx%>/assest/template/js/vendor/chart.bundle.min.js"></script>
    <script src="<%=ctx%>/assest/template/js/vendor/grid.min.js"></script>
    <script src="<%=ctx%>/assest/template/js/vendor/jquery-ui.min.js"></script>
    <script src="<%=ctx%>/assest/template/js/vendor/jquery.barrating.min.js"></script>
    <script src="<%=ctx%>/assest/template/js/vendor/jquery.countdown.min.js"></script>
    <script src="<%=ctx%>/assest/template/js/vendor/jquery.counterup.min.js"></script>
    <script src="<%=ctx%>/assest/template/js/vendor/jquery.easing1.3.js"></script>
    <script src="<%=ctx%>/assest/template/js/vendor/owl.carousel.min.js"></script>
    <script src="<%=ctx%>/assest/template/js/vendor/slick.min.js"></script>
    <script src="<%=ctx%>/assest/template/js/vendor/tether.min.js"></script>
    <script src="<%=ctx%>/assest/template/js/vendor/trumbowyg.min.js"></script>
    <script src="<%=ctx%>/assest/template/js/vendor/waypoints.min.js"></script>
    <script src="<%=ctx%>/assest/template/js/dashboard.js"></script>
    <script src="<%=ctx%>/assest/template/js/main.js"></script>
    <script src="<%=ctx%>/assest/template/js/map.js"></script>
    <!-- endinject -->
</body>
</html>
	