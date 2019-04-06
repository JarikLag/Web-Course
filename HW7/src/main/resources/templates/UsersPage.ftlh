<#import "./common.ftlh" as c>

<@c.page>
<div class="datatable">
    <div class="caption">Users</div>
    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Login</th>
            <th>Created At</th>
            <th>Profile link</th>
            <th>Status</th>
        </tr>
        </thead>
        <tbody>
    <#list users as userp>
    <tr>
        <td>${userp.id}</td>
        <td>${userp.login}</td>
        <td>${userp.creationTime}</td>
        <td><a href="/user/${userp.id}">${userp.login}'s profile</a></td>
        <td>
            <#if userp.disabled == true>
                <form method="post" action="/update">
                    <input type="hidden" name="userId" value="${userp.id}">
                    <input type="submit" value="Enable">
                </form>
            <#else>
                <form method="post" action="/update">
                    <input type="hidden" name="userId" value="${userp.id}">
                    <input type="submit" value="Disable">
                </form>
            </#if>

        </td>
    </tr>
    </#list>
        </tbody>
    </table>
</div>
</@c.page>
