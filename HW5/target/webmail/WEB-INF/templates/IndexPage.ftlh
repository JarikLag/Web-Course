<#-- @ftlvariable name="message" type="java.lang.String" -->
<#-- @ftlvariable name="secretConfirmation" type="java.lang.String" -->
<#import "common.ftl" as c>

<@c.page>
    <#if message??>
        <p>${message}</p>
        <#if secretConfirmation??>
        <form method="post" action="/confirm">
            <a href="/confirm?secret=${secretConfirmation}">Confirmation link</a>
        </form>
        </#if>
    </#if>

    <h1>Welcome to the index page</h1>
</@c.page>
