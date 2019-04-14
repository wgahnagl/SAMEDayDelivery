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
