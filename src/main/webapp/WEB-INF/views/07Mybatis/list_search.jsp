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
    <script src="../resources/jquery/jquery-3.6.3.min.js"></script>
<title>Insert title here</title>
<script type="text/javascript">
/*function deleteRow(idx) {
	if(confirm("정말로 삭제하시겠습니까?")){
		//삭제 요청명으로 이동한다.
		location.href="delete.do?idx="+idx;
	}
}*/
$(function(){
	$('.del').click(function(e){
		if(confirm("정말로 삭제하시겠습니까?")){
			var idx = e.target.value;
			location.href="delete.do?idx="+idx;
		}	
	});
});
</script>
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
	<div class="text-center">
		<form method="get">
			<select name="searchField">
				<option value="contents">내용</option>
				<option value="name">작성자</option>
			</select>
			<input type="text" name="searchTxt" />
			<input type="submit" value="검색" />
		</form>
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
						<!-- onclick="javascript:deleteRow(${row.idx });" -->
						<button value="${row.idx }" class="btn btn-danger del">
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