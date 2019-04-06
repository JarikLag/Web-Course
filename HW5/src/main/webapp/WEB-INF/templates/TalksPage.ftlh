<#-- @ftlvariable name="incomingTalks" type="String[][]" -->
<#-- @ftlvariable name="outgoingTalks" type="String[][] -->
<#import "common.ftl" as c>

<@c.page>
<div class="form-box">
    <div class="header">Enter</div>
    <div class="body">
        <form method="post" action="/talks">
            <input type="hidden" name="action" value="add">
            <div class="field">
                <div class="name">
                    <label for="targetUser">To: </label>
                </div>
                <div class="value">
                    <input id="targetUser" name="targetUser" value="${targetUser!}"/>
                </div>
            </div>
            <div class="field">
                <div class="name">
                    <label for="text">Text: </label>
                </div>
                <div class="value">
                    <#--<input id="text" type="text" name="text" value="${text!}"/>-->
                    <textarea id="text" name="text">${text!}</textarea>
                </div>
            </div>
            <div class="button-field">
                <input type="submit" value="Send message">
            </div>
            <#if error??>
                <div class="error">
                ${error}
                </div>
            </#if>
        </form>
    </div>
</div>

<div class="datatable" style="margin-top: 1rem">
    <div class="caption">Incoming messages</div>
    <table>
        <thead>
        <tr>
            <th>From</th>
            <th>Messages</th>
        </tr>
        </thead>
        <tbody>
            <#list incomingTalks as talk>
            <tr>
                <td>
                ${talk[0]}
                </td>
                <td>
                ${talk[1]}
                </td>
            </tr>
            </#list>
    </table>
</div>

<div class="datatable" style="margin-top: 1rem">
    <div class="caption">Outgoing messages</div>
    <table>
        <thead>
        <tr>
            <th>To</th>
            <th>Messages</th>
        </tr>
        </thead>
        <tbody>
            <#list outgoingTalks as talk>
            <tr>
                <td>
                ${talk[0]}
                </td>
                <td>
                ${talk[1]}
                </td>
            </tr>
            </#list>
    </table>
</div>
</@c.page>
