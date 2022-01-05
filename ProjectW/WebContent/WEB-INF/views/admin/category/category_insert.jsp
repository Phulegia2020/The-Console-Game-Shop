<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Trang Loại Sản Phẩm</title>
</head>
<body>
	<div class="page-container">

		<div class="left-content">

			<div class="mother-grid-inner">

<%-- 				<jsp:include page="header.jsp"></jsp:include> --%>
				<%@include file="../../admin/header.jsp"%>

				<div class="inner-block">
					<div class="inbox">
						<h2>Thêm Loại Sản Phẩm</h2>
						<div class="col-md-12 compose-right">
							<div class="inbox-details-default">
								<div class="inbox-details-heading">Form</div>
								<div class="inbox-details-body">
									<div class="alert alert-info">${message}</div>
									<form:form class="com-mail" modelAttribute="category">
										<label>Tên Sản Phẩm</label> 
										<form:input type="text" placeholder="Tên..." path="name"/>
										<button type="submit">Thêm mới</button>
									</form:form>
								</div>
							</div>
						</div>

						<div class="clearfix"></div>
					</div>
				</div>

<%-- 				<jsp:include page="footer.jsp"></jsp:include> --%>
				<%@include file="../../admin/footer.jsp"%>

			</div>

		</div>

<%-- 		<jsp:include page="navigation.jsp"></jsp:include> --%>
		<%@include file="../../admin/navigation.jsp"%>

	</div>
</body>
</html>
