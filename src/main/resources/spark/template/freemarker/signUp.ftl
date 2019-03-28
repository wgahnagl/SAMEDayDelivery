<!DOCTYPE html>
<#include "components/header.ftl">

<body>
<div class="container-fluid">
    <div class="container">
        <form role="form" method="post" action="/register">
            <fieldset>
                <#include "components/addressInput.ftl">
            </fieldset>
        </form>
    </div>
</div>
</body>
</html>