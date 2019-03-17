<#import "parts/common.ftl" as c>
<#import "parts/formLogin.ftl" as l>

<@c.page>
<h3>Add New User</h3>
${message}
<@l.login "/registration" />

</@c.page>