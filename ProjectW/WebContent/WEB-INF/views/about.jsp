<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri = "http://www.springframework.org/tags" prefix = "s"%>
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
	
<script>
$(function(){
	$("@[data-lang]").click(function(){
		var lang = $(this).attr("data-lang");
		$get("home/about.html?language=" + lang,function(){
			location.reload();
		});
		return false;
	});
});
</script>

</head>

<body>
<%-- 	<h1><s:message code="home.about.title"/></h1> --%>
	<%@include file="../../resources/fragments/header.jsp"%>
<%-- 	<a href = index.html><s:message code = "global.menu.index"/></a> --%>
<%-- 	<a href = about.html><s:message code = "global.menu.about"/></a> --%>
	<a href = "about.html?language=en" data-lang = "en">English</a>
	<a href = "about.html?language=vi" data-lang = "vi">Tiếng Việt</a>
	<main>
	<div class="site-section border-bottom mt-3" data-aos="fade">
		<div class="container">
			<div class="row mb-5">
				<div class="col-md-12 mb-4">
					<div class="block-16">
						<figure>
							<img
								src="https://cnet3.cbsistatic.com/img/WVv-bFSMaZ_1Mv0yeqLzYE3kB8s=/1200x675/2019/09/25/ecbbf358-089a-4235-8a91-80980ea1e867/playstation-store.jpg"
								alt="Image placeholder" class="img-fluid rounded">
							<a href="#" class="play-button popup-vimeo"><span
								class="ion-md-play"></span></a>
						</figure>
					</div>
				</div>
				<div class="col-md-12 mr-auto">
					<div class="site-section-heading mb-4">
						<h1><s:message code="home.about.title"/></h1>
<!-- 						<h2 class="text-black">GIỚI THIỆU</h2> -->
					</div>
						<p><s:message code="home.about.content"/></p>
<!-- 					<p>PLAYSTATION 4 mong muốn trở thành nơi nhập khẩu và phân phối hàng đầu các sản phẩm máy chơi game PS4. Đồng thời phát triển dịch vụ cung cấp và hỗ trợ các phụ kiện máy nhằm tạo cho khách hàng có thể thưởng thức và trải nghiệm những tinh túy của công nghệ được áp dụng vào máy PS4 tại Việt Nam. Định hướng phát triển của công ty là phát triển chuỗi bán lẻ hàng đầu mang thương hiệu Máy PS4 rộng khắp nhiều tỉnh thành trên cả nước, là nơi phân phối uy tín, đáng tin cậy nhiều đối tác và các đơn vị kinh doanh khác.</p> -->
				</div>
			</div>
		</div>
	</div>
	</main>

	<%@include file="../../resources/fragments/footer.jsp" %>

	<!--Bootsrap 4 js-->
	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
	<script
		src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
	<!--Animation-->
	<script src="https://unpkg.com/scrollreveal"></script>
	<script
		src="${pageContext.request.contextPath}/resources/js/animation.js"></script>
</body>

</html>