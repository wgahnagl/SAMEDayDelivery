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
<div class="row">
    <div class="col-md-9 mb-3">
        <label for="address">Address</label>
        <input type="text" name="address" class="form-control" id="address" placeholder="1234 Main St" required="">
        <div class="invalid-feedback">
            Please enter your shipping address.
        </div>
    </div>
    <div class="col-md-3 mb-3">
        <label for="address">City</label>
        <input type="text" name="city" class="form-control" id="city" placeholder="Rochester" required="">
        <div class="invalid-feedback">
            Please enter your city
        </div>
    </div>
</div>

<div class="mb-3">
    <label for="address2">Address 2 <span class="text-muted">(Optional)</span></label>
    <input type="text" name="address2" class="form-control" id="address2" placeholder="Apartment or suite">
</div>


<div class="row">
    <div class="col-md-5 mb-3">
        <label for="country">Country</label>
        <select class="custom-select d-block w-100" name="country" id="country" required="">
            <option value="">Choose...</option>
            <option>United States</option>
        </select>
        <div class="invalid-feedback">
            Please select a valid country.
        </div>
    </div>
    <div class="col-md-4 mb-3">
        <label for="state">State</label>
        <select class="custom-select d-block w-100" name="state" id="state" required="">
            <option value="">Choose...</option>
            <option>California</option>
        </select>
        <div class="invalid-feedback">
            Please provide a valid state.
        </div>
    </div>
    <div class="col-md-3 mb-3">
        <label for="zip">Zip</label>
        <input type="text" class="form-control" name="zip" id="zip" placeholder="" required="">
        <div class="invalid-feedback">
            Zip code required.
        </div>
    </div>
</div>
