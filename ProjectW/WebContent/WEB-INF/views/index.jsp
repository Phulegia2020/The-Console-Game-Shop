<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix = "s"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<title>PS4 Shop</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/style.css">
<!-- Bootstrap CSS -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css">
<!-- Icon -->
<link
	href="${pageContext.request.contextPath}/resources/vendor/fontawesome-free/css/all.min.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/css/slideshow-product.css">
<!--Carousel-->
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/vendor/owlcarousel/css/owl.carousel.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/vendor/owlcarousel/css/owl.theme.default.min.css">
</head>
<body>
	<%@include file="../../resources/fragments/header.jsp"%>
	<main>
	<div class = "row">
	<div class = "container">
		<h1 class = "title page-title heading-1">Play Station 4</h1>
		<div class = "category-description">
			<img alt="Máy PS4" src="https://haloshop.vn/image/cache/catalog/banners/game/categories/ps4-km-t10-categories-1280x280.jpg" width = "1100px" class = "category-image" title = "Máy PS4">
		</div>
	</div>
	</div>
	<!--Slide Show-->
	<div class="section-1">
		<div class="container text-center">
			<div class="category-title">
				<h1 class="heading-1">SẢN PHẨM MỚI NHẤT</h1>
			</div>
		</div>
	</div>
	<div class="carousel slide carousel-multi-item mb-3" data-ride="carousel">
		<div class="carousel-inner">
			<div class="container">
				<div class="owl-carousel owl-theme">
					<c:forEach var="l" items="${listNewProduct}">
						<div class="col-md-4 item" style="width: 5rem;">
							<div class="card">
								<img src="${l.url}" alt="Img1" class="card-img-top">
								<div class="card-body">
									<h4 class="card-title">${l.name}</h4>
									<p class="card-text">
										<span>Giá: </span><fmt:formatNumber pattern ="#,###,###,###" value = "${l.price}"/> VNĐ
									</p>
									<a href="shopping-cart/add/${l.id}.html" class="btn btn-danger ml-3">Mua ngay</a> 
									<a href="detail/${l.id}.html" class="btn btn-success ml-3">Xem chi tiết</a>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>
	</div>
	<!--End Slide Show--> 
	 <!--Slide Show-->
	<div class="section-1">
		<div class="container text-center">
			<div class="category-title">
				<h1 class="heading-1">PS4 Pro</h1>
			</div>
		</div>
	</div>
	<div class="carousel slide carousel-multi-item mb-3" data-ride="carousel">
		<div class="carousel-inner">
			<div class="container">
				<div class="owl-carousel owl-theme">
					<c:forEach var="listPro" items="${listProductPro}">
						<div class="col-md-4 item" style="width: 5rem;">
							<div class="card">
								<img src="${listPro.url}" alt="Img1" class="card-img-top">
								<div class="card-body">
									<h4 class="card-title">${listPro.name}</h4>
									<p class="card-text">
										<span>Giá: </span><fmt:formatNumber pattern ="#,###,###,###" value = "${listPro.price}"/> VNĐ
									</p>
									<a href="shopping-cart/add/${listPro.id}.html" class="btn btn-danger ml-3">Mua ngay</a>  <a
										href="detail/${listPro.id}.html" class="btn btn-success ml-3">Xem chi tiết</a>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>
	</div>
	<!--End Slide Show-->
	<!--Slide Show-->
	<div class="section-1">
		<div class="container text-center">
			<div class="category-title">
				<h1 class="heading-1">PS4 Slim</h1>
			</div>
		</div>
	</div>
	<div class="carousel slide carousel-multi-item mb-3" data-ride="carousel">
		<div class="carousel-inner">
			<div class="container">
				<div class="owl-carousel owl-theme">
					<c:forEach var="listSlim" items="${listProductSlim}">
						<div class="col-md-4 item" style="width: 5rem;">
							<div class="card">
								<img src="${listSlim.url}" alt="Img1" class="card-img-top">
								<div class="card-body">
									<h4 class="card-title">${listSlim.name}</h4>
									<p class="card-text">
										<span>Giá: </span><fmt:formatNumber pattern ="#,###,###,###" value = "${listSlim.price }"/> VNĐ
									</p>
									<a href="shopping-cart/add/${listSlim.id}.html" class="btn btn-danger ml-3">Mua ngay</a>  
									<a href="detail/${listSlim.id}.html" class="btn btn-success ml-3">Xem chi tiết</a>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>
	</div>
	<!--End Slide Show-->

	<div class="section-4 bg-dark">
		<div class="container">
			<div class="row">
				<div class="col-md-7">
					<img
						src="https://hoanghamobile.com/tin-tuc/wp-content/uploads/2020/06/gia-playstation-5-1.jpg"
						alt="" height = "300px" width="590px">
				</div>
				<div class="col-md-5">
					<h1 class="text-white">Khám phá về PlayStation tại cửa hàng chúng tôi</h1>
					<a href="about.html" class="btn btn-success text-light">Tìm hiểu ngay</a>
				</div>
			</div>
		</div>
	</div>
	
	<div class="section-3">
		<div class="container">
			<div class="row">
				<div class="col-md-4 mt-3">
					<div class="d-flex flex-row">
						<i class="fas fa-phone-alt fa-3x m-2"></i>
						<div class="d-flex flex-column">
							<h3 class="m-2">Tư vấn tận tâm</h3>
							<p class="m-2">Gọi ngay: 18009543</p>
						</div>
					</div>
				</div>

				<div class="col-md-4 mt-3">
					<div class="d-flex flex-row">
						<i class="fas fa-truck fa-3x m-2"></i>
						<div class="d-flex flex-column">
							<h3 class="m-2">Giao hàng tận nơi</h3>
							<p class="m-2">Giao hàng miễn phí giao hàng nội thành tại
								TPHCM, Hà Nội, Đà Nẵng</p>
						</div>
					</div>
				</div>
				<div class="col-md-4 mt-3">
					<div class="d-flex flex-row">
						<i class="fas fa-thumbs-up fa-3x m-2"></i>
						<div class="d-flex flex-column">
							<h3 class="m-2">Sản phẩm</h3>
							<p class="m-2">Cam kết hàng chính hãng</p>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	</main>

	<%@include file="../../resources/fragments/footer.jsp"%>

	<!--Bootsrap 4 js-->
	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
	<!--Animation-->
	<script src="https://unpkg.com/scrollreveal"></script>
	<!--Carousel-->
	<script defer
		src="${pageContext.request.contextPath}/resources/vendor/owlcarousel/js/owl.carousel.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/vendor/owlcarousel/js/jquery.min.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/animation.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/slideshow-product.js"></script>
</body>
</html>