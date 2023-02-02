<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.2/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.2/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.1/font/bootstrap-icons.css">
<meta charset="UTF-8">
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<!-- 아이디, 패스워드 입력여부를 확인하기 위한 스크립트 -->
<script type="text/javascript">
function loginValidate(f)
{
   if(f.id.value==""){
      alert("아이디를 입력하세요");
      f.id.focus();
      return false;
   }
   if(f.pass.value==""){
      alert("패스워드를 입력하세요"); 
      f.pass.focus();
      return false;
   } 
}
</script>
<div class="container"> 
   <h3>방명록(로그인)</h3> 
   <!-- 로그인이 된 경우 회원정보와 로그아웃 버튼을 출력한다. -->
   <c:choose>
      <c:when test="${not empty sessionScope.siteUserInfo }">
         <div class="row" style="border:2px solid #cccccc;padding:10px;">         
            <!-- 로그인에 성공한 경우 DTO객체에 회원정보를 저장한 후
            세션영역에 저장할 것이므로, 출력시에는 getter()를 사용하기 위해
            멤버변수로 접근한다. EL에서는 멤버변수명만으로 getter()에 접근할 수 있다. -->
            <h4>아이디:${sessionScope.siteUserInfo.id }</h4>
            <h4>이름:${sessionScope.siteUserInfo.name }</h4>
            <br /><br />
            <button class="btn btn-danger" 
               onclick="location.href='logout.do';">
               로그아웃</button>
            &nbsp;&nbsp;
            <button class="btn btn-primary" 
               onclick="location.href='list.do';">
               방명록리스트</button>
         </div>
      </c:when>
      <c:otherwise>
      	<!-- 로그아웃 상태에서는 로그인폼을 출력한다. -->
      	<!-- 로그인에 실패한 경우 에러메세지를 출력하는 부분 -->
         <span style="font-size:1.5em; color:red;">${LoginNG }</span>
         <form name="loginForm" method="post" action="./loginAction.do" onsubmit="return loginValidate(this);">
         	<!-- 로그인에 성공한 경우 돌아갈 페이지의 경로를 파라미터로 받아온다.
         		네이버와 같은 포털사이트는 이미 이와 같은 처리가 되어있다. -->
            <input type="hidden" name="backUrl" value="${param.backUrl }"/>
            <table class="table" style="width:50%;">
               <tr>
                  <td><input type="text" class="form-control" id="id" name="id" placeholder="아이디" tabindex="1"></td>
                  <td rowspan="2" style="width:80px;">
                     <button type="submit" class="btn btn-primary" 
                     style="height:77px; width:77px;"  tabindex="3">
                     로그인
                     </button>
                  </td>
               </tr>
               <tr>
                  <td><input type="password" class="form-control" id="pass" name="pass" placeholder="패스워드" tabindex="2"></td>
               </tr>
            </table>
         </form>
      </c:otherwise>
   </c:choose>
</div>
</body>
</html>