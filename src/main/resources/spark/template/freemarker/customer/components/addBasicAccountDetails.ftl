<script src="js/customer/passwordVerify.js"></script>
    <div class="row">
        <div class="col-md-6 mb-3">
            <label for="firstName">First name</label>
            <input type="text" name="firstName" class="form-control" id="firstName" placeholder="" value="" required="">
            <div class="invalid-feedback">
                Valid first name is required.
            </div>
        </div>
        <div class="col-md-6 mb-3">
            <label for="lastName">Last name</label>
            <input type="text" name="lastName" class="form-control" id="lastName" placeholder="" value="" required="">
            <div class="invalid-feedback">
                Valid last name is required.
            </div>
        </div>
    </div>

    <div class="mb-3">
        <label for="email">Email</label>
        <input type="email" name="email" class="form-control" id="email" placeholder="you@example.com">
        <div class="invalid-feedback">
            Please enter a valid email address for shipping updates.
        </div>
    </div>

    <div class = "mb-3">
        <label for="password1">Password:</label>
        <input id="password1" class = "form-control" type=password required name=password>
        <label for="password2">Confirm password:</label>
        <input id="password2" class="form-control" type=password required>
        <div class="invalid-feedback">
            Passwords must match
        </div>
    </div>