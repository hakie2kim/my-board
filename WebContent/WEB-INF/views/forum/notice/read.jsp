<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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
</head>

<body class="preload home1 mutlti-vendor">
    <!--================================
            START DASHBOARD AREA
    =================================-->
    <section class="support_threads_area section--padding2">
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <div class="forum_detail_area ">
                        <div class="cardify forum--issue">
                            <div class="title_vote clearfix">
                                <h3>${board.title}</h3>
								<a href="<c:url value='/forum/notice/modifyPage.do?boardSeq=${board.boardSeq}&boardTypeSeq=${board.boardTypeSeq}'/>">
									<button class="btn btn--round btn--bordered btn-sm btn-secondary">수정</button>
								</a>
								<div class="vote">
                                    <a href="#" id="cThumbsUpAnchor" onClick="javascript:vote(${board.boardSeq}, ${board.boardTypeSeq}, 'Y')" <c:if test="${isLike eq 'Y'}">class="active"</c:if>>
                                        <span class="lnr lnr-thumbs-up"></span>
                                    </a>
                                    <a href="#" id="cThumbsDownAnchor" onClick="javascript:vote(${board.boardSeq}, ${board.boardTypeSeq}, 'N')" <c:if test="${isLike eq 'N'}">class="active"</c:if>>
                                        <span class="lnr lnr-thumbs-down"></span>
                                    </a>
                                </div>
                                <!-- end .vote -->
                            </div>
                            <!-- end .title_vote -->
                            <div class="suppot_query_tag">
                                <img class="poster_avatar" src="<%=ctx%>/assest/template/images/support_avat1.png" alt="Support Avatar">
                                ${board.memberId}
                                <span>${board.regDtm}</span>
                            </div>
                            <p style="margin-bottom: 0; margin-top: 19px;">${board.content}</p>
                            
                            <br/><br/><br/><br/>
                                                        
                            <c:forEach var="attFile" items="${attFiles}">
                            	<div>
									<a href='<%=ctx%>/forum/notice/download.do?attSeq=${attFile.attachSeq}'>
										${attFile.orgFileNm} (${attFile.fileSize}) 
									</a>
                            	</div>
                            </c:forEach>
                        </div>
                        
                        <c:if test="${fn:length(attFiles) ne 0}">
	                       	<form action="<%=ctx%>/forum/notice/downloadMultipleFiles.do" method="post">
	                       		<c:forEach var="attFile" items="${attFiles}">
	                       			<input type="hidden" name="attSeq" value="${attFile.attachSeq}">
	                       		</c:forEach>
	                       		<button type="submit">파일 한번에 다운 받기</button>
	                       	</form>
                       	</c:if>
                        <!-- end .forum_issue -->

                        <div class="forum--replays cardify">
                            <div class="area_title">
                                <h4>${fn:length(comments)} Replies</h4>
                            </div>
                            <!-- end .area_title -->
							<c:forEach var="comment" items="${comments}">
								<c:if test="${not empty comment.deleteDtm}">
									<div class="forum_single_reply">
										<div class="reply_content">삭제된 댓글입니다.</div>
									</div>
								</c:if>
								<c:if test="${empty comment.deleteDtm}">
		                            <div class="forum_single_reply" data-comment-seq="${comment.commentSeq}">
		                                <div class="reply_content">
		                                    <div class="name_vote">
		                                        <div class="pull-left">
		                                            <h4>${comment.memberNm}
		                                                <span>staff</span>
		                                            </h4>
		                                            <!-- <p>Answered 3 days ago</p> -->
		                                            <p>${comment.regDtm lt comment.updateDtm ? comment.updateDtm += ' (수정됨)' : comment.regDtm}</p>
		                                        </div>
		                                        <!-- end .pull-left -->
		
		                                        <div class="vote">
		                                            <a href="#" class="active">
		                                                <span class="lnr lnr-thumbs-up"></span>
		                                            </a>
		                                            <a href="#" class="">
		                                                <span class="lnr lnr-thumbs-down"></span>
		                                            </a>
		                                        </div>
		                                    </div>
		                                    <!-- end .vote -->
		                                    <p>${comment.content}</p>
		                                </div>
		                                <!-- end .reply_content -->
		                                <button data-lvl="${comment.lvl+1}" data-parent-comment-seq="${comment.commentSeq}" onClick="javascript:showCommentFormAreaReply(this)">답글 달기</button>
		                                <button onClick="javascript:deleteReply(${comment.commentSeq})">삭제</button>
		                                <button onClick="javascript:showCommentFormAreaEdit(this, ${comment.commentSeq}, '${comment.content}')">수정</button>
		                            </div>
		                            <!-- end .forum_single_reply -->
								</c:if>
							</c:forEach>
							
							<div class="comment-form-area edit" style="display:none;">
                                <h4>Edit your comment or reply</h4>
                                <!-- comment reply -->
                                <div class="media comment-form support__comment">
                                    <div class="media-left">
                                        <a href="#">
                                            <img class="media-object" src="<%=ctx%>/assest/template/images/m7.png" alt="Commentator Avatar">
                                        </a>
                                    </div>
                                    <div class="media-body">
                                    	<!-- form -> div -->
                                        <div class="comment-reply-form">
		                                    <div id="trumbowyg-edit"></div>
		                                    <button class="btn btn--sm btn--round" data-comment-seq=0 onClick="javascript:editReplyOrComment(this)">Edit</button>
		                                    <button class="btn btn--sm btn--round" onClick="javascript:cancelCommentFormAreaEdit(this)">Cancel</button>
                                        </div>
                                    </div>
                                </div>
                                <!-- comment reply -->
                            </div>
							
							<div class="comment-form-area reply" style="display:none;">
                                <h4>Leave a reply</h4>
                                <!-- comment reply -->
                                <div class="media comment-form support__comment">
                                    <div class="media-left">
                                        <a href="#">
                                            <img class="media-object" src="<%=ctx%>/assest/template/images/m7.png" alt="Commentator Avatar">
                                        </a>
                                    </div>
                                    <div class="media-body">
                                    	<!-- form -> div -->
                                        <div class="comment-reply-form">
		                                    <div id="trumbowyg-reply"></div>
		                                    <button class="btn btn--sm btn--round" data-lvl=0 onClick="javascript:leaveReplyOrComment(${board.boardSeq}, ${board.boardTypeSeq}, this, '#trumbowyg-reply')">Post Reply</button>
                                        </div>
                                    </div>
                                </div>
                                <!-- comment reply -->
                            </div>

                            <div class="comment-form-area">
                                <h4>Leave a comment</h4>
                                <!-- comment reply -->
                                <div class="media comment-form support__comment">
                                    <div class="media-left">
                                        <a href="#">
                                            <img class="media-object" src="<%=ctx%>/assest/template/images/m7.png" alt="Commentator Avatar">
                                        </a>
                                    </div>
                                    <div class="media-body">
                                        <form action="#" class="comment-reply-form">
                                            <div id="trumbowyg-demo"></div>
                                            <button class="btn btn--sm btn--round" data-lvl=0 onClick="javascript:leaveReplyOrComment(${board.boardSeq}, ${board.boardTypeSeq}, this, '#trumbowyg-demo')">Post Comment</button>
                                        </form>
                                    </div>
                                </div>
                                <!-- comment reply -->
                            </div>
                        </div>
                        <!-- end .forum_replays -->
                    </div>
                    <!-- end .forum_detail_area -->
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
    
    <script type="text/javascript">
		var ctx = '<%= request.getContextPath() %>';
	</script>

	<script src="<%=ctx%>/assest/js/page.js"></script>
    <script src="<%=ctx%>/assest/template/js/vendor/trumbowyg.min.js"></script>
    <script src="<%=ctx%>/assest/template/js/vendor/trumbowyg/ko.js"></script>
    <script type="text/javascript">
	    $('#trumbowyg-edit').trumbowyg({
	        lang: 'kr'
	    });
    
    	$('#trumbowyg-reply').trumbowyg({
	        lang: 'kr'
	    });
	    
	    $('#trumbowyg-demo').trumbowyg({
	        lang: 'kr'
	    });
	    
	    function vote(boardSeq, boardTypeSeq, isLike) {	    	
	    	let url = "<%=ctx%>/forum/notice";
	    	url += "/" + boardSeq;
	    	url += "/" + boardTypeSeq;
	    	url += "/vote.do?isLike=" + isLike;
	    	
	    	$.ajax({    
	    		type : "get",           
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
	    			// console.log(typeof result);
	    			if (result === '1' && isLike === 'Y') {
	    				$('a#cThumbsUpAnchor').addClass('active');
	    				$('a#cThumbsDownAnchor').removeClass('active');
	    			} else if (result === '1' && isLike === 'N') {
	    				$('a#cThumbsUpAnchor').removeClass('active');
	    				$('a#cThumbsDownAnchor').addClass('active');
	    			} else if (result === '2') {
	    				$('a#cThumbsUpAnchor').removeClass('active');
	    				$('a#cThumbsDownAnchor').removeClass('active');
	    			}
	    		},
    			// 결과 에러 콜백함수
	    		error : function(request, status, error) {
	    			console.log(error);
	    		}
	    	});
	    }
	    
	    function showCommentFormAreaReply(elem) {
	    	const dataLvl = elem.getAttribute('data-lvl');
	    	const parentCommentSeq = elem.getAttribute('data-parent-comment-seq');
	    	
	    	const forumSingleReply = elem.closest('.forum_single_reply');
	    	let commentFormAreaReply = document.querySelector('.comment-form-area.reply');
	    	commentFormAreaReply.style.display = 'block';
	    	
	    	commentFormAreaReply.querySelector('.btn').setAttribute('data-lvl', parseInt(dataLvl));
	    	commentFormAreaReply.querySelector('.btn').setAttribute('data-parent-comment-seq', parseInt(parentCommentSeq));
	    	
	    	forumSingleReply.append(commentFormAreaReply);
	    }
	    
	    function showCommentFormAreaEdit(elem, commentSeq, content) {
	    	const forumSingleReply = elem.closest('.forum_single_reply');
	    	
	    	let commentFormAreaEdit = document.querySelector('.comment-form-area.edit');
	    	commentFormAreaEdit.style.display = 'block';
	    	commentFormAreaEdit.querySelector('.comment-reply-form > button').setAttribute('data-comment-seq', commentSeq);
	    	commentFormAreaEdit.querySelector('#trumbowyg-edit').innerText = content;
	    	
	    	forumSingleReply.after(commentFormAreaEdit);
	    }
	    
	    function cancelCommentFormAreaEdit(elem) {
	    	const commentFormAreaEdit = elem.closest('.comment-form-area.edit');
	    	commentFormAreaEdit.previousElementSibling.style.display = 'block';
	    	commentFormAreaEdit.style.display = 'none';
	    }
	    
	    function leaveReplyOrComment(boardSeq, boardTypeSeq, buttonElem, contentId) {
	    	$.ajax({        
	    		type : 'post',
	    		url : '<%=ctx%>/forum/notice/reply.do',
	    		headers : {
	    			'content-type': 'application/json'
	    		},
	    		dataType : 'json',
	    		data : JSON.stringify ({
	    			lvl: buttonElem.getAttribute('data-lvl'),
	    			content: $(contentId).trumbowyg('html'),
	    			boardSeq : boardSeq,
	    			boardTypeSeq : boardTypeSeq,
	    			parentCommentSeq: buttonElem.getAttribute('data-parent-comment-seq')
	    		}),
	    		success : function(result) {
	    			if (result === 1) {
	    				window.location.replace('<%=ctx%>/forum/notice/readPage.do?boardSeq=${board.boardSeq}&boardTypeSeq=${board.boardTypeSeq}');
	    			}
	    		},
	    		error : function(request, status, error) {
	    			console.log(error);
	    		}
	    	});
	    }
	    
	    function editReplyOrComment(elem) {
	    	const commentSeq = elem.getAttribute('data-comment-seq');
	    	
	    	let url = '<%=ctx%>/forum/notice';
	    	url += '/' + commentSeq;
	    	url += '/reply.do';
	    	
	    	$.ajax({        
	    		type : 'patch',
	    		url : url,
	    		headers : {
	    			'content-type': 'application/json'
	    		},
	    		dataType : 'json',
	    		data : JSON.stringify({
	    			commentSeq: commentSeq,
	    			content: $('#trumbowyg-edit').trumbowyg('html')
	    		}),
	    		success : function(result) {
	    			if (result === 1) {
	    				window.location.replace('<%=ctx%>/forum/notice/readPage.do?boardSeq=${board.boardSeq}&boardTypeSeq=${board.boardTypeSeq}');
	    			}
	    		},
	    		error : function(request, status, error) {
	    			console.log(error);
	    		}
	    	});
	    }
	    
	    function deleteReply(commentSeq) {
	    	let url = '<%=ctx%>/forum/notice';
	    	url += '/' + commentSeq;
	    	url += '/reply.do';
	    	
	    	$.ajax({        
	    		type : 'delete',
	    		url : url,
	    		headers : {
	    			'content-type': 'application/json'
	    		},
	    		dataType : 'json',
	    		success : function(result) {
	    			if (result === 1) {
	    				window.location.replace('<%=ctx%>/forum/notice/readPage.do?boardSeq=${board.boardSeq}&boardTypeSeq=${board.boardTypeSeq}');
	    			}
	    		},
	    		error : function(request, status, error) {
	    			console.log(error);
	    		}
	    	});
	    }
	</script>
</body>

</html>
	