<#import "parts/common.ftl" as c>
<#import "parts/formLogin.ftl" as l>

<@c.page>

<h3>Login Page</h3>
<@l.login "/login" />
<a href="/registration">Create new user</a>

</@c.page>