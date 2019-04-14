<!DOCTYPE html>
<#include "../components/header.ftl">
<h2 style="margin: 20px;"> Create Label </h2>

<div class="jumbotron">
    <div class="container">
        <form role="form" method="POST" action="/create_label">
            <fieldset>
                <div class="row">
                    <div class="col-md-12 order-md-1" >
                        <#if error??>
                            <div class="alert alert-danger" role="alert">
                                ${error}
                            </div>
                        </#if>
                        <form class="needs-validation" novalidate="">
                            <h4 class="mb-3">Destination Address</h4>
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="dest_firstname">First Name</label>
                                    <input type="text" class="form-control" id="dest_firstname">
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="dest_lastname">Last Name</label>
                                    <input type="text" class="form-control" id="dest_lastname">
                                </div>
                            </div>
                            <#include "components/addAddress.ftl">
                            <#include "components/createLabel.ftl">
                            <button class="btn btn-primary btn-lg btn-block" type="submit" >Create Label</button>
                        </form>
                    </div>
                </div>
                <input name="receiverPays" value="false" style="display: none">
            </fieldset>
        </form>
    </div>
</div>
</html>