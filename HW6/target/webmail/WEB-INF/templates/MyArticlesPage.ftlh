<#-- @ftlvariable name="articles" type="ru.itmo.webmail.model.domain.Article[]" -->
<#import "common.ftl" as c>

<@c.page>
<div class="datatable" style="margin-top: 1rem">
    <div class="caption">Your articles</div>
    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Change visibility</th>
        </tr>
        </thead>
        <tbody>
            <#list articles as article>
            <tr>
                <td>
                ${article.id}
                </td>
                <td>
                ${article.title}
                </td>
                <td>
                    <div class="change-button">
                        <div class="id" style="display: none">${article.id}</div>
                        <div class="userId" style="display: none">${article.userId}</div>
                        <#if article.hidden == true>
                            <button>Show</button>
                        <#else>
                            <button>Hide</button>
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
        var id = $(this).find('.id').text();
        var userId = $(this).find('.userId').text();
        var $value = $(this).find('button');
        var update;
        if ($value.text() === 'Show') {
            update = false;
        } else {
            update = true;
        }
        $.post("", {action: "update", id: id, userId: userId, update: update}, function (response) {
            if (response["success"]) {
                if (update === false) {
                    $value.text('Hide');
                } else {
                    $value.text('Show');
                }
            }
        });
        return false;
    });
</script>
</@c.page>