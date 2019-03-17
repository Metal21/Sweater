<#import "parts/common.ftl" as c>
<#import "parts/formLogin.ftl" as l>

<@c.page>
<div>
    <@l.logout />
    <span><a href="/user">User list</a></span>
</div>

<div>
    <form method="post" action = "addMessage">
        <input type="text" name = "text" placeholder="Enter message"/>
        <input type="text" name = "tag" placeholder="Tag"/>
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button type="submit">Добавить</button>
    </form>
</div>
<div>
    <form method="post" action="filter">
        <input type="text" name = "filter" />
        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <button type="submit">Найти</button>
    </form>
</div>


<div>Список сообщений</div>
<#list messages as message>
<div>
    <b>${message.id}</b>
    <span>${message.text}</span>
    <i>${message.tag}</i>
    <strong>${message.authorName}</strong>
</div>
<#else>
No message
</#list>

</@c.page>