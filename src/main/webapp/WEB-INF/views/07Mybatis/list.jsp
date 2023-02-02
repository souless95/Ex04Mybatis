<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.2/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.1/font/bootstrap-icons.css">
    <script src="../static/jquery/jquery-3.6.3.min.js"></script>
<title>Insert title here</title>
</head>
<body>
<div class="container">
	<h3 class="text-center">방명록(한줄게시판)</h3>
	<div class="text-right" >
		<c:choose>
			<c:when test="${not empty sessionScope.siteUserInfo }">
				<button class="btn btn-danger"
					onclick="location.href='logout.do';">
					로그아웃
				</button>
			</c:when>
			<c:otherwise>
				<button class="btn btn-info"
					onclick="location.href='login.do';">
					로그인
				</button>
			</c:otherwise>
		</c:choose>
		&nbsp;&nbsp;
		<button class="btn btn-success"
			onclick="location.href='write.do';" >
			방명록쓰기
		</button>
	</div>	
	<!-- 방명록 반복 부분 s -->
	<c:forEach items="${lists }" var="row">		
		<div class="border mt-2 mb-2">
			<div class="media">
				<div class="media-left mr-3">
					<img src="../images/img_avatar3.png" class="media-object" style="width:60px">
				</div>
				<div class="media-body">
					<h4 class="media-heading">작성자:${row.name }(${row.id })</h4>
					<p>${row.contents }</p>
				</div>	  
				<!--  수정,삭제버튼 -->
				<div class="media-right">
					<c:if test="${sessionScope.siteUserInfo.id eq row.id }">
						<button class="btn btn-primary"
							onclick="location.href='modify.do?idx=${row.idx }';">
							수정					
						</button>
						<button class="btn btn-danger"
							onclick="javascript:deleteRow(${row.idx });">
							삭제				
						</button>
					</c:if>
				</div>
			</div>
		</div>
	</c:forEach>
	
	<!-- 방명록 반복 부분 e -->
	<ul class="pagination justify-content-center">
		${pagingImg }
	</ul>
</div>
</body>
</html>