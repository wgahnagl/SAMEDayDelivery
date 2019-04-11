<!DOCTYPE html>
<#include "components/header.ftl">

<body>
<div class="container-fluid">
    <div class="container">
        <form role="form" method="POST" action="/signup">
            <fieldset>
                <#include "components/customerData.ftl">
            </fieldset>
        </form>
    </div>
</div>
</body>
</html>