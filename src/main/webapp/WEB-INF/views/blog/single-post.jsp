<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zxx">

<head>
  <meta charset="UTF-8">
  <meta name="description" content="Foodeiblog Template">
  <meta name="keywords" content="Foodeiblog, unica, creative, html">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Foodeiblog | Template</title>

  <!-- Google Font -->
  <link href="https://fonts.googleapis.com/css?family=Nunito+Sans:300,400,600,700,800,900&display=swap"
        rel="stylesheet">
  <link href="https://fonts.googleapis.com/css?family=Unna:400,700&display=swap" rel="stylesheet">

  <!-- Css Styles -->
  <link rel="stylesheet" href="../../static/css/bootstrap.min.css" type="text/css">
  <link rel="stylesheet" href="../../static/css/font-awesome.min.css" type="text/css">
  <link rel="stylesheet" href="../../static/css/elegant-icons.css" type="text/css">
  <link rel="stylesheet" href="../../static/css/owl.carousel.min.css" type="text/css">
  <link rel="stylesheet" href="../../static/css/slicknav.min.css" type="text/css">
  <link rel="stylesheet" href="../../static/css/style_new.css" type="text/css">
   <link rel="stylesheet" href="../../static/css/dropdown.css" type="text/css">
</head>

<body>

<!-- Header Section Begin -->
<header class="header">
   <div class="header__top" style="position: fixed; top: 0; width: 100%; z-index: 999; transform: translate(0, 0);">
        <div class="container">
            <div class="row">
                <div class="col-lg-2 col-md-1 col-6 order-md-1 order-1"><br>
                     <a href="/"><img src="../static/img/logo.png" alt=""></a><br>
                </div>
                <div class="col-lg-8 col-md-10 order-md-2 order-3">
                    <nav class="header__menu">
                        <ul>
                            <li><a href="/">Trang chủ</a></li>


                            <c:set var="userRole" value="${sessionScope['user-role']}"/>
                            <c:if test="${userRole == 'ROLE_ADMIN'}"><li><a href="/admin">Admin</a></li></c:if>
                            <c:if test="${userRole == 'ROLE_USER'}"><li><a href="/admin/posts">Tạo bài viết</a></li></c:if>
                            <c:if test="${userRole == null }"><li><a href="/login">login</a></li></c:if>
                            <c:if test="${userRole != null }"><li><a href="/logout">Logout</a></li></c:if>

                        </ul>
                    </nav>
                </div>

            </div>
        </div>
    </div>
<br>

</header>
<!-- Header Section End -->

<!-- Single Post Section Begin -->
<section class="single-post spad">
  <input id="postId" value="${requestScope['postId']}" type="hidden"/>
  <input id="userId" value="${sessionScope['account-login'].accountId}" type="hidden"/>
  <div id="picture" class="single-post__hero set-bg" data-setbg="../../static/img/categories/single-post/single-post-hero.jpg"></div>
  <div class="container">
    <div class="row d-flex justify-content-center">
      <div class="col-lg-8">
        <div class="single-post__title">
          <div class="single-post__title__meta">
            <h2 id="day">08</h2>
            <span id="month">Aug</span>
          </div>
          <div class="single-post__title__text">

            <h4 id="title"></h4>
            <ul class="widget">
              <li>Người viết bài <span id="full-name"></span></li>
            </ul>
            <ul class="label">
                <li>Có <span class="numberOfComments"></span> nhận xét</li>

            </ul>

          </div>
        </div>
        <div class="single-post__social__item">
          <ul>
            <li><a href="#"><i class="fa fa-facebook"></i></a></li>
            <li><a href="#"><i class="fa fa-twitter"></i></a></li>
            <li><a href="#"><i class="fa fa-instagram"></i></a></li>
            <li><a href="#"><i class="fa fa-youtube-play"></i></a></li>
          </ul>
        </div>
        <div id="brief-content" class="single-post__top__text">
        </div>
        <div id="content" class="single-post__more__details">
        </div>
        <div class="single-post__tags">
          <hr>
        </div>
        <div class="single-post__comment">
          <div>
            <div class="form-group" id="rating-ability-wrapper">
              <input type="hidden" id="selected_rating" name="selected_rating" value="" required="required">
              <div class="widget__title">

                <h4>Đánh Giá Trung Bình:<span id="ratePost"></span></h4><br/>
                <span>Đánh giá của bạn: </span><span class="selected-rating">0</span><small> / 5</small>
              </div>
              <button type="button" class="btnrating btn btn-default btn-lg" data-attr="1" id="rating-star-1">
                <i class="fa fa-star" aria-hidden="true"></i>
              </button>
              <button type="button" class="btnrating btn btn-default btn-lg" data-attr="2" id="rating-star-2">
                <i class="fa fa-star" aria-hidden="true"></i>
              </button>
              <button type="button" class="btnrating btn btn-default btn-lg" data-attr="3" id="rating-star-3">
                <i class="fa fa-star" aria-hidden="true"></i>
              </button>
              <button type="button" class="btnrating btn btn-default btn-lg" data-attr="4" id="rating-star-4">
                <i class="fa fa-star" aria-hidden="true"></i>
              </button>
              <button type="button" class="btnrating btn btn-default btn-lg" data-attr="5" id="rating-star-5">
                <i class="fa fa-star" aria-hidden="true"></i>
              </button>
            </div>
          </div>
          <div class="widget__title">
            <h4> Có <span class="numberOfComments"></span> nhận xét</h4>
          </div>
          <div id="comments"></div>
        </div>
        <div class="single-post__leave__comment">
          <div class="widget__title">
            <h4>Viết nhận xét của bạn vào bên dưới: </h4>
          </div>
          <form action="#">
            <textarea id="comment" placeholder="Nhập nội dung nhận xét"></textarea>
            <button type="button" id="btnComment" class="site-btn">Gửi nhận xét</button>
          </form>
        </div>
      </div>
    </div>
  </div>
</section>
<!-- Single Post Section End -->

<!-- Footer Section Begin -->
<footer class="footer">
  <div class="container">
    <div class="row">
      <div class="col-lg-12">
        <div class="footer__text">
          <div class="footer__logo">
            <a href="#"><img src="../../static/img/logo.png" alt=""></a>
          </div>
          <div class="footer__social">
            <a href="#"><i class="fa fa-facebook"></i></a>
            <a href="#"><i class="fa fa-twitter"></i></a>
            <a href="#"><i class="fa fa-instagram"></i></a>
            <a href="#"><i class="fa fa-youtube-play"></i></a>
            <a href="#"><i class="fa fa-envelope-o"></i></a>
          </div>
        </div>
      </div>
    </div>
  </div>
</footer>
<!-- Footer Section End -->

<!-- Search Begin -->
<div class="search-model">
  <div class="h-100 d-flex align-items-center justify-content-center">
    <div class="search-close-switch">+</div>
    <form class="search-model-form">
      <input type="text" id="search-input" placeholder="Search here.....">
    </form>
  </div>
</div>
<!-- Search End/static/js/single_post.js -->

<!-- Js Plugins -->
<script src="../../static/js/jquery-3.3.1.min.js"></script>
<script src="../../static/js/bootstrap.min.js"></script>
<script src="../../static/js/jquery.slicknav.js"></script>
<script src="../../static/js/owl.carousel.min.js"></script>
<script src="../../static/js/main.js"></script>

<script src="/static/js/home.js"></script>
<script src="/static/js/new_single_post.js"></script>

</body>

</html>