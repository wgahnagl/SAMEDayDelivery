<ul class="navbar-nav mr-auto">
    <li class="nav-item active">
        <a class="nav-link" href="/">Home <span class="sr-only">(current)</span></a>
    </li>
    <li class="nav-item">
        <a class="nav-link" href="/tracking">Tracking</a>
    </li>
    <li class="nav-item">
        <a class="nav-link" href="/account">Account</a>
    </li>
</ul>
<#if currentUser??>
    <form class="form-inline my-2 my-lg-0">
        <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Sign Out</button>
    </form>
<#else>
    <a href="/signin">
        <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Sign In</button>
    </a>
</#if>