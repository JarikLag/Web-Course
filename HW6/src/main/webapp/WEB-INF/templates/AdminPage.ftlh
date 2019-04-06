<#-- @ftlvariable name="users" type="ru.itmo.webmail.model.domain.User[]" -->
<#import "common.ftl" as c>

<@c.page>
<div class="datatable" style="margin-top: 1rem">
    <div class="caption">Users</div>
    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Login</th>
            <th>Privileges</th>
        </tr>
        </thead>
        <tbody>
            <#list users as user>
            <tr class="tr">
                <td>
                    ${user.id}
                </td>
                <td>
                    ${user.login}
                </td>
                <td>
                    <div class="change-button">
                        <div class="userId" style="display: none">${user.id}</div>
                        <div class="status">
                            <#if user.admin == true>
                                True
                            <#else>
                                False
                            </#if>
                        </div>
                        <#if user.admin == true>
                            <button>Disable</button>
                        <#else>
                            <button>Enable</button>
                        </#if>
                    </div>
                </td>
            </tr>
            </#list>
        </tbody>
    </table>
</div>
<script>
    $(".change-button").click(function () {
        var userId = $(this).find('.userId').text();
        var $value = $(this).find('button');
        var $status = $(this).find('.status');
        var update;
        if ($value.text() === 'Disable') {
            update = false;
        } else {
            update = true;
        }
        $.post("", {action: "update", userId: userId, update: update}, function (response) {
            if (response["success"]) {
                if (update === false) {
                    $value.text('Enable');
                    $status.text('False');
                } else {
                    $value.text('Disable');
                    $status.text('True');
                }
            } else {
                setMessage("You are not admin!");
                document.location.href = "/";
            }
        });
        return false;
    });
</script>
</@c.page>