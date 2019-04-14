<!DOCTYPE html>
<#include "../components/header.ftl">
<div class="container-fluid">
    <div class="container">
        <form role="form" method="POST" action="/add_bank_account">
            <fieldset>
                <div class="row">
                    <div class="col-md-12 order-md-1" >
                        <#if error??>
                            <div class="alert alert-danger" role="alert">
                                ${error}
                            </div>
                        </#if>
                        <form class="needs-validation" novalidate="">
                            <#include "components/addBank.ftl">
                            <button class="btn btn-primary btn-lg btn-block" id="addAddressSubmit" type="submit" >Submit</button>
                        </form>
                    </div>
                </div>
            </fieldset>
        </form>
    </div>
</div>
</html>