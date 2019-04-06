<#import "./common.ftlh" as c>

<@c.page>
<#if profile??>
<div class="datatable">
    <div class="caption">${profile.login}'s profile</div>
    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Login</th>
            <th>Created At</th>
        </tr>
        </thead>
        <tbody>
            <tr>
                <td>${profile.id}</td>
                <td>${profile.login}</td>
                <td>${profile.creationTime}</td>
            </tr>
        </tbody>
    </table>
</div>
<#else>
<div style="color: red">No such user</div>
</#if>
</@c.page>