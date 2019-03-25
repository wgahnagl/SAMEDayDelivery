<!DOCTYPE html>
<#include "components/header.ftl">

<body>
<div class="container-fluid">
    <div class="container">
        <h2 class="text-center" id="title">SAME Day Delivery</h2>
            <form role="form" method="post" action="/register">
                <fieldset>
                    <#include "components/addressInput.ftl">
                </fieldset>
            </form>
    </div>
</div>
</body>
</html>