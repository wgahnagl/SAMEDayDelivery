<!DOCTYPE html>
<#include "../components/header.ftl">
    <div class="container-fluid">
        <div class="container">
            <form role="form" id="accountCreateForm" method="POST" action="/add_address">
                <fieldset>
                    <div class="row">
                        <div class="col-md-12 order-md-1" >
                            <#if error??>
                                <div class="alert alert-danger" role="alert">
                                    ${error}
                                </div>
                            </#if>
                            <form class="needs-validation" novalidate="">
                                <h4 class="mb-3">Address</h4>
                                <div class="row">
                                    <div class="col-md-6">
                                        <label for="firstname">First Name</label>
                                        <input type="text" class="form-control" id="firstname" value="${firstName ! "none"}" readonly>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="firstname">Last Name</label>
                                        <input type="text" class="form-control" id="lastname" value="${lastName ! "none"}" readonly>
                                    </div>
                                </div>
                                <#include "components/addAddress.ftl">
                                <button class="btn btn-primary btn-lg btn-block" id="addAddressSubmit" type="submit" >Submit</button>
                            </form>
                        </div>
                    </div>
                </fieldset>
            </form>
        </div>
    </div>
</html>