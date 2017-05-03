<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring_form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<link rel="stylesheet" href="css/bootstrap.min.css"/>
<link rel="stylesheet" href="css/login/form-elements.css">
<link rel="stylesheet" href="css/login/style.css">
<link rel="stylesheet" href="css/font-awesome.min.css">
<link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">

<script src="js/bootstrap.min.js"></script>
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/jquery.backstretch.min.js"></script>

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Discipline Login</title>

</head>
<body>

<!-- Top content -->
<div class="top-content">

    <div class="inner-bg">
        <div class="container">
            <div class="row">
                <div class="col-sm-8 col-sm-offset-2 text">
                    <h1><strong>Discipline</strong></h1>
                    <div class="description">
                        <p>
                            <spring:message code="label.description"/>
                        </p>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-6 col-sm-offset-3 form-box">
                    <div class="form-top">
                        <div class="form-top-left">
                            <h3><spring:message code="label.please_login"/></h3>
                            <p><spring:message code="label.enter_user_name"/></p>
                        </div>
                        <div class="form-top-right">
                            <i class="fa fa-key"></i>
                        </div>
                    </div>
                    <div class="form-bottom">
                        <spring_form:form role="form" class="login-form" id="loginForm" method="post" action="login" modelAttribute="user">
                            <div class="form-group">
                                <label class="sr-only" for="form-username">Username</label>
                                <spring:message code='label.username' var="userName"/>
                                <spring_form:input path="login"
                                            type="text" name="form-username" placeholder="${userName}" class="form-username form-control" id="login"/>
                            </div>
                            <div class="form-group">
                                <label class="sr-only" for="password">Password</label>
                                <spring:message code='label.password' var="password"/>
                                <spring_form:password path="password"
                                               name="password" placeholder="${password}" class="form-password form-control" id="password"/>
                            </div>
                            <button type="submit" class="btn"><spring:message code="label.sign_in" /></button>
                        </spring_form:form>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-6 col-sm-offset-3 social-login">
                    <h3>...<spring:message code="label.or_login_with"/>:</h3>
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
<a href="${pageContext.request.contextPath}?lang=en">EN</a>
<a href="${pageContext.request.contextPath}?lang=ru">RU</a>
</body>
