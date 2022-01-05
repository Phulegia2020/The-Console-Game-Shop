<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
						<h2>Hiệu Chỉnh Sản Phẩm</h2>
						<div class="col-md-12 compose-right">
							<div class="inbox-details-default">
								<div class="inbox-details-heading">Form</div>
								<div class="inbox-details-body">
									<div class="alert alert-info">${message}</div>
									<form:form class="com-mail" modelAttribute="product" action="admin/product/update/${product.id}.html" method="POST" enctype="multipart/form-data">
										<label>Id</label> 
										<input type="text" name="id" value="${product.id}" readonly/>
										<label>Code</label>
										<input type="text" name="code" value="${product.code}"/>
										<label>Tên sản phẩm</label>
										<input type="text" name="name" value="${product.name}"/>
										<label>Giá</label> 
										<input type="number" name="price" value="${product.price}"/>
										<label>Số lượng</label>
										<input type="number" name="quantity" value="${product.quantity}"/>
										<br>
										<label>Nguồn gốc</label>
										<input type="text" name="madein" value="${product.madein}"/> 
										<label>Hình ảnh</label> 
										<img src="${product.url}">
										<input type="text" name="url" value="${product.url}" readonly/>
										<br>
										<label>Chỉnh sửa hình ảnh</label> 
										<input type="file" name="photo"/>
										<label>Description</label>
										<textarea rows="6" name="description"><c:out value="${product.description}"/></textarea>
										<label>Loại sản phẩm</label>
										<!-- Combobox -->
										<form:select path="category.id" items="${categories}" itemValue="id" itemLabel="name" />
										<br>
										<button type="submit">Cập nhật</button>
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
