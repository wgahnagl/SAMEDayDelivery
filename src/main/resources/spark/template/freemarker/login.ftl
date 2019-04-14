<!DOCTYPE html>
<#include "components/header.ftl">
<body>
<div class="container" style="padding-top: 10%">
    <div class="d-flex justify-content-center h-100">
        <div class="card">
            <div class="card-header">
                <h3>Sign In</h3>
            </div>
            <div class="card-body">
                <form action="/signin" id="signInForm"  method="POST">
                    <div class="input-group form-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text"><i class="fas fa-user"></i></span>
                        </div>
                        <input type="text" name="username" class="form-control" placeholder="email" id="email">

                    </div>
                    <div class="input-group form-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text"><i class="fas fa-key"></i></span>
                        </div>
                        <input type="password" name="password" class="form-control" placeholder="password" id="password">
                    </div>
                    <div class="form-group">
                        <input type="submit" value="Login" class="btn float-right login_btn">
                    </div>
                </form>
            </div>
            <div class="card-footer">
                <div class="d-flex justify-content-center links">
                    <#if view != "admin" && view != "delivery">
                        Don't have an account?<a href="/signup"> Sign Up</a>
                    </#if>
                </div>
                <div class="d-flex justify-content-center">
                    <a href="/password_recover">Forgot your password?</a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>