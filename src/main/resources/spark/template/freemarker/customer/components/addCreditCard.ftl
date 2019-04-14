
<h4 class="mb-3">Payment</h4>
<div class="row">
    <div class="col-md-6 mb-3">
        <label for="cc-name">Name on card</label>
        <input type="text" name="cc-name" class="form-control" id="cc-name" placeholder="" required="">
        <small class="text-muted">Full name as displayed on card</small>
        <div class="invalid-feedback">
            Name on card is required
        </div>
    </div>
    <div class="col-md-6 mb-3">
        <label for="cc-number">Credit card number</label>
        <input type="text" name="cc-number" class="form-control" id="cc-number" placeholder="" required="">
        <div class="invalid-feedback">
            Credit card number is required
        </div>
    </div>
</div>
<div class="row">
    <div class="col-md-3 mb-3">
        <label for="cc-expiration">Expiration</label>
        <input type="text" name="cc-expiration" class="form-control" id="cc-expiration" placeholder="" required="">
        <div class="invalid-feedback">
            Expiration date required
        </div>
    </div>
    <div class="col-md-3 mb-3">
        <label for="cc-expiration">CVV</label>
        <input type="text" name="cc-cvv" class="form-control" id="cc-cvv" placeholder="" required="">
        <div class="invalid-feedback">
            Security code required
        </div>
    </div>
</div>
<hr class="mb-4">