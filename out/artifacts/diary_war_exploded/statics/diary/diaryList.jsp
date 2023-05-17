<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="data_list">
	<div class="data_list_title">
	<img src="${pageContext.request.contextPath}/images/list_icon.png"/>
	日记列表
	</div>
	
	<div class="diary_datas">
		<ul>
			<c:forEach var="diary" items="${diaryList}">
				<li>
					『<fmt:formatDate value="${diary.releaseDate}" type="date" pattern="yyyy-MM-dd"/>』
					<span>&nbsp;<a href="#">${diary.title}</a></span>
				</li>
			</c:forEach>
		</ul>
	</div>
	
	<nav style="text-align:center">
	 <ul class="pagination">
	    ${pageCode}
	  </ul>
	</nav>
	
</div>































