<!DOCTYPE html>
<#include "../components/header.ftl">

<body>
<div class="jumbotron">
    <div class="container">
        <h1 class="display-4">Create Account</h1>
        <form role="form" id="accountCreateForm" method="POST" action="/signup">
            <fieldset>
                <#include "components/createAccount.ftl">
            </fieldset>
        </form>
    </div>
</div>
</body>
</html>