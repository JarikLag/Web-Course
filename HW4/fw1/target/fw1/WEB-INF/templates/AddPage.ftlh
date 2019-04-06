<#import "common.ftl" as c>

<@c.page>
<#if username??>
    <div class="form-box">
        <div class="header">Add news</div>
        <div class="body">

            <form method="post" action="/news/add">
                <input type="hidden" name="action" value="add">
                <div class="field">
                    <textarea id="news" name="news"></textarea>
                </div>
                <div class="button-field">
                    <input type="submit" value="Submit">
                </div>
                <#if error??>
                <div class="error">
                    ${error}
                </div>
                </#if>
            </form>
        </div>
    </div>
<#else>
    <p><strong>You are not authorized to view this page.</strong></p>
</#if>
</@c.page>
