<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
    <!-- endinject -->

    <!-- Favicon -->
    <link rel="icon" type="image/png" sizes="16x16" href="<%=ctx%>/assest/template/images/favicon.png">    
	<script type="text/javascript">
		var ctx = '<%= request.getContextPath() %>';
	</script>	
	<script src="<%=ctx%>/assest/js/page.js"></script>
</head>

<body class="preload home1 mutlti-vendor">
    <section class="section--padding2">
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <div class="">
                        <div class="modules__content">
                            <div class="withdraw_module withdraw_history">
                                <div class="withdraw_table_header">
                                    <h3>공지사항</h3>
                                </div>
                                <div class="table-responsive">
                                    <table class="table withdraw__table">
                                        <thead>
                                            <tr>
                                            	<th>No</th>
                                                <th>제목</th>
                                                <th>Date</th>
                                                <th>작성자</th>
                                            </tr>
                                        </thead>

                                        <tbody>
                                        	<c:forEach var="board" items="${list}">
	                                        	<tr>
	                                                <td>${board.boardSeq}</td>
	                                                <td>
	                                                	<a href="<c:url value='/forum/notice/readPage.do?boardSeq=${board.boardSeq}&boardTypeSeq=${board.boardTypeSeq}'/>">
	                                                		${board.title}
	                                                	</a>
	                                                </td>
	                                                <td>${board.regDtm}</td>
	                                                <td>${board.memberId}</td>
	                                            </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                    <div style="display: inline-block; margin: 0 5px; float: right; padding-right:10px;">
		                                <a href="<c:url value='/forum/notice/writePage.do'/>">
		                                	<button class="btn btn--round btn--bordered btn-sm btn-secondary">작성</button>
		                                </a>
		                            </div>
                                    <div class="pagination-area" style="padding-top: 45px;">
				                        <nav class="navigation pagination" role="navigation">
				                            <div class="nav-links">
				                                <c:if test="${pagination.prev}">
					                                <a class="prev page-numbers" href="<c:url value='/forum/notice/listPage.do?page=${pagination.startPage-1}&size=${pagination.postsPerPage}'/>">
					                                    <span class="lnr lnr-arrow-left"></span>
					                                </a>
				                                </c:if>
				                                <c:forEach var="pageNum" begin="${pagination.startPage}" end="${pagination.endPage}">
				                                	<a class="page-numbers <c:if test="${pagination.currentPage eq pageNum}">current</c:if>" href="<c:url value='/forum/notice/listPage.do?page=${pageNum}&size=${pagination.postsPerPage}'/>">${pageNum}</a>
				                                </c:forEach>
				                                <c:if test="${pagination.next}">
					                                <a class="next page-numbers" href="<c:url value='/forum/notice/listPage.do?page=${pagination.endPage+1}&size=${pagination.postsPerPage}'/>">
					                                    <span class="lnr lnr-arrow-right"></span>
					                                </a>
				                                </c:if>
				                            </div>
				                        </nav>
				                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- end .col-md-6 -->
            </div>
            <!-- end .row -->
        </div>
        <!-- end .container -->
    </section>
    
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
