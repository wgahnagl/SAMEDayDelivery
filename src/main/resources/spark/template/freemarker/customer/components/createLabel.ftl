<h4 class="mb-3">Package Info</h4>
<div class="row">
    <div class="col-md-6 mb-3">
        <label for="expediency">Expediency</label>
        <select class="custom-select d-block w-100" name="expediency" id="expediency" required="">
            <option value="">Choose...</option>
            <option value="overnight">Overnight</option>
            <option value="two-day">Two Day</option>
            <option value="regular">Regular</option>
        </select>
        <div class="invalid-feedback">
            Please select a valid expediency.
        </div>
    </div>
    <div class="col-md-6 mb-3">
        <label for="packageType">Type</label>
        <select class="custom-select d-block w-100" name="packageType" id="packageType" required="">
            <option value="">Choose...</option>
            <option value="envelope-small">Small Envelope</option>
            <option value="envelope-large">Large Envelope</option>
            <option value="package-small">Small Package</option>
            <option value="package-medium">Medium Package</option>
            <option value="package-large">Large Package</option>
        </select>
        <div class="invalid-feedback">
            Please select a valid expediency.
        </div>
    </div>
</div>
<div class="row">
    <div class="col-md-3 mb-3">
        <label for="packageWeight">Weight</label>
        <input type="text" class="form-control" name="weight" id="packageWeight">
    </div>
</div>
<h4 class="mb-3">Payment</h4>
<div class="row">
    <div class="container">
        <ul class="nav nav-tabs">
            <li style="margin: 20px;"><a class="btn btn-primary btn-lg btn-block" data-toggle="tab" href="#menu1">Bank</a></li>
            <li style="margin: 20px;"><a class="btn btn-primary btn-lg btn-block" data-toggle="tab" href="#menu2">Credit Card</a></li>
        </ul>

        <div class="tab-content">
            <div id="menu1" class="tab-pane fade">
                <h3>Pay With Bank Account</h3>
                <p>
                <div class="form-check">
                    <label class="form-check-label" for="autoWithdraw">Auto Withdraw from linked bank</label>
                    <input type="checkbox" class="form-check-input" name="autoWithdraw" id="autoWithdraw">
                </div>
                </p>
            </div>
            <div id="menu2" class="tab-pane fade">
                <h3>Pay With Credit Card</h3>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="cc-name">Name on card</label>
                        <input type="text" name="cc-name" class="form-control" id="cc-name" placeholder="">
                        <small class="text-muted">Full name as displayed on card</small>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="cc-number">Credit card number</label>
                        <input type="text" name="cc-number" class="form-control" id="cc-number" placeholder="">
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-3 mb-3">
                        <label for="cc-expiration">Expiration</label>
                        <input type="text" name="cc-expiration" class="form-control" id="cc-expiration" placeholder="">
                    </div>
                    <div class="col-md-3 mb-3">
                        <label for="cc-expiration">CVV</label>
                        <input type="text" name="cc-cvv" class="form-control" id="cc-cvv" placeholder="">
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

