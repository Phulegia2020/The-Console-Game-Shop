<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Trang Sản Phẩm</title>
</head>
<body>

	<div class="page-container">

		<div class="left-content">

			<div class="mother-grid-inner">

<%-- 				<jsp:include page="header.jsp"></jsp:include> --%>
				<%@include file="../../admin/header.jsp"%>

				<div class="inner-block">
					<div class="inbox">
						<h2>Chi Tiết Sản Phẩm</h2>
						<div class="col-md-12 compose-right">
							<div class="inbox-details-default">
								<div class="inbox-details-heading">Form</div>
								<div class="inbox-details-body">
									<div class="alert alert-info">${message }</div>
									<form:form class="com-mail" modelAttribute="product" action="admin/product/insert.html" method="POST" enctype="multipart/form-data">
										<label>Mã</label> 
										<form:input type="text" placeholder="PS001" path="code"/>
										<label>Tên sản phẩm</label> 
										<form:input type="text" placeholder="ABC" path="name"/>
										<label>Giá</label> 
										<form:input type="number" placeholder="123,000 VNĐ" path="price"/>
										<label>Số lượng</label> 
										<form:input type="number" placeholder="100" path="quantity"/>
										<br>
										<label>Nguồn gốc</label> 
										<form:input type="text" placeholder="..." path="madein"/>
										<label>Hình ảnh</label> 
										<input type="file" placeholder="..." name="photo"/>
										<label>Description</label>
										<form:textarea rows="6" placeholder="..." path="description"/>
										<label>Loại sản phẩm</label>
										<!-- Combobox -->
										<form:select path="category.id" items="${categories}" itemValue="id" itemLabel="name" />
										<br>
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
