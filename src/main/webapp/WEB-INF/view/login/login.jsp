<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/login/form-elements.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/login/style.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/font-awesome.min.css">

<script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/jquery-1.11.1.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/jquery.backstretch.min.js"></script>

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Bootstrap Colored Login Form Template</title>

    <!-- CSS -->
    <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>

<!-- Top content -->
<div class="top-content">

    <div class="inner-bg">
        <div class="container">
            <div class="row">
                <div class="col-sm-8 col-sm-offset-2 text">
                    <h1><strong>Bootstrap</strong> Colored Login Form</h1>
                    <div class="description">
                        <p>
                            This is a free responsive colored login form made with Bootstrap.
                            Download it on <a href="http://azmind.com"><strong>AZMIND</strong></a>, customize and use it as you like!
                        </p>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-6 col-sm-offset-3 form-box">
                    <div class="form-top">
                        <div class="form-top-left">
                            <h3>Login to our site</h3>
                            <p>Enter your username and password to log on:</p>
                        </div>
                        <div class="form-top-right">
                            <i class="fa fa-key"></i>
                        </div>
                    </div>
                    <div class="form-bottom">
                        <form:form role="form" class="login-form" id="loginForm" method="post" action="login" modelAttribute="user">
                            <div class="form-group">
                                <label class="sr-only" for="form-username">Username</label>
                                <form:input path="login"
                                            type="text" name="form-username" placeholder="Username..." class="form-username form-control" id="login"/>
                            </div>
                            <div class="form-group">
                                <label class="sr-only" for="password">Password</label>
                                <form:password path="password"
                                               name="password" placeholder="Password..." class="form-password form-control" id="password"/>
                            </div>
                            <button type="submit" class="btn">Sign in!</button>
                        </form:form>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-6 col-sm-offset-3 social-login">
                    <h3>...or login with:</h3>
                    <div class="social-login-buttons">
                        <a class="btn-link-1" href="#"><i class="fa fa-facebook"></i></a>
                        <a class="btn-link-1" href="#"><i class="fa fa-twitter"></i></a>
                        <a class="btn-link-1" href="#"><i class="fa fa-google-plus"></i></a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
