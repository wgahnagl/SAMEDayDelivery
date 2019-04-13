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