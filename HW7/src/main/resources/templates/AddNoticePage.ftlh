<#import "/spring.ftl" as spring>
<#import "./common.ftlh" as c>

<#macro error field>
    <@spring.bind field/>
    <#list spring.status.errorMessages as error>
    <div class="error">${error}</div>
        <#break/>
    </#list>
</#macro>

<@c.page>
<div class="addNotice form-box">
    <div class="header">Add notice</div>
    <div class="body">
        <form method="post" action="/addNotice">
            <input type="hidden" name="action" value="addNotice">
            <div class="field">
                <div class="name">
                    <label for="name">Text</label>
                </div>
                <div class="value">
                    <textarea id="content" name="content">${addNoticeForm.content!}</textarea>
                </div>
                <@error 'addNoticeForm.content'/>
            </div>
            <div class="button-field">
                <input type="submit" value="Add notice">
            </div>
        </form>
    </div>
</div>
</@c.page>
