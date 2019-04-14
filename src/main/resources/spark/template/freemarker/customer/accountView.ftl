<!DOCTYPE html>
<#include "../components/header.ftl">
<script src = "js/customer/accountView.js"></script>
    <h2 style="margin: 20px;"> Account </h2>
    <div class="jumbotron">
        <div class="container">
            <h1 class="display-4">Address</h1>
            <div id="displayAddress" style="display: none">
                <p class="lead displayAddress">
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
                <div class="row">
                    <div class="col-sm-6">
                        <label for="address1">Address</label>
                        <input type="text" class="form-control" id="address1" readonly>
                    </div>
                    <div class="col-sm-6">
                        <label for="city">City</label>
                        <input type="text" class="form-control" id="city" readonly>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                        <label for="address2">Address 2</label>
                        <input type="text" class="form-control" id="address2" readonly>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-3">
                        <label for="country">Country</label>
                        <input type="text" class="form-control" id="country" readonly>
                    </div>
                    <div class="col-sm-3">
                        <label for="state">State</label>
                        <input type="text" class="form-control" id="state" readonly>
                    </div>
                    <div class="col-sm-3">
                        <label for="zip">Zip</label>
                        <input type="text" class="form-control" id="zip" readonly>
                    </div>
                </div>
                </p>
            </div>
            <div id="addAddress">
                <p class="lead">
                    <p class="lead">
                        No addresses found. You'll need to add one to recieve packages.
                    </p>
                    <a class="btn btn-primary btn-lg btn-block" id="addAddress" href="/add_address">Add Address</a>
                </p>
            </div>
        </div>
    </div>

<div class="jumbotron">
    <div class="container">
        <h1 class="display-4">Credit Card</h1>
        <div id="displayCreditCard" style="display: none">
            <p class="lead">
            <div class="row">
                <div class="col-sm-6">
                    address
                </div>
                <div class="col-sm-6">
                    addd
                </div>
            </div>
            <div class="row">
                <div class="col-sm-6">
                    dd
                </div>
                <div class="col-sm-6">
                    addd
                </div>
            </div>
            </p>
        </div>
        <div id="addCreditCard">
            <p class="lead">
            <p class="lead">
                No credit cards found. You'll need to add one to send packages.
            </p>
            <a class="btn btn-primary btn-lg btn-block" id="addCreditCard" href="/add_credit_card">Add Credit Card</a>
            </p>
        </div>

    </div>
</div>

<div class="jumbotron">
    <div class="container">
        <h1 class="display-4">Bank Account</h1>
        <div id="displayBankAccount" style="display: none">
            <p class="lead">
            <div class="row">
                <div class="col-sm-6">
                    bank
                </div>
                <div class="col-sm-6">
                    bank
                </div>
            </div>
            <div class="row">
                <div class="col-sm-6">
                    bank
                </div>
                <div class="col-sm-6">
                    bank
                </div>
            </div>
            </p>
        </div>
        <div id="addBankAccount">
            <p class="lead">
            <p class="lead">
                No bank accounts found. You'll need to add one to auto send packages.
            </p>
            <a class="btn btn-primary btn-lg btn-block" id="addBankAccount" href="/add_bank_account">Add Bank Account</a>
            </p>
        </div>

    </div>
</div>

</html>