<#import "parts/common.ftl" as c>
<#import "parts/formLogin.ftl" as l>

<@c.page>
<div class = "mb-1"><h3>Add New User</h3></div>
${message?ifExists}
<@l.login "/registration" true />

</@c.page>