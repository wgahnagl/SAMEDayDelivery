<!DOCTYPE html>
<#include "../components/header.ftl">

<body>
<div class="container-fluid">
    <div class="container">
        <form role="form" id="accountCreateForm" method="POST" action="/signup">
            <fieldset>
                <#include "components/createAccount.ftl">
            </fieldset>
        </form>
    </div>
</div>
</body>
</html>