<#-- @ftlvariable name="notice" type="ru.itmo.wm4.domain.Notice" -->
<#import "/spring.ftl" as spring/>
<#import "./common.ftlh" as c>

<#macro error field>
    <@spring.bind field/>
    <#list spring.status.errorMessages as error>
    <div class="error">${error}</div>
        <#break/>
    </#list>
</#macro>

<@c.page>
    <#if notice??>
    <div class="notice form-box" style="margin-top: 2rem">
        <div class="header">Notice by ${notice.user.login}</div>
        <div class="body" style="padding: 0.5rem">
        ${notice.text}
        </div>
        <div class="tags" style="padding: 0.5rem; font-size: small; color: #3B5998">
            Tags:
            <#list notice.tags as tag>
                <div style="border:1px solid #3B5998; border-radius: 3px; display: inline; padding: 1px">${tag.name}</div>
            </#list>
        </div>
    </div>
    <div class="add_comment form-box" style="margin-top: 2rem">
        <div class="header">Add comment</div>
        <div class="body">
            <form method="post" action="/notice/${notice.id}">
                <div class="field">
                    <div class="name">
                        <label for="text">Text</label>
                    </div>
                    <div class="value">
                        <textarea id="text" rows="6" name="text">${comment.text!?html}</textarea>
                    </div>
                    <@error 'comment.text'/>
                </div>
                <div class="button-field">
                    <input type="submit" value="Add">
                </div>
            </form>
        </div>
    </div>
    <div class="datatable" style="margin-top: 2rem">
        <div class="caption">Comments</div>
        <table>
            <thead>
            <tr>
                <th>Author</th>
                <th>Text</th>
                <th>Created at</th>
            </tr>
            </thead>
            <tbody>
                <#list notice.comments as table_comment>
                <tr>
                    <td>${table_comment.user.login}</td>
                    <td>${table_comment.text?html}</td>
                    <td>${table_comment.creationTime}</td>
                </tr>
                </#list>
            </tbody>
        </table>
    </div>
    <#else>
    <div style="color: red">No such notice</div>
    </#if>
</@c.page>
