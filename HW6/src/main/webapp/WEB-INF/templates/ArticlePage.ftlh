<#import "common.ftl" as c>

<@c.page>
<div class="article form-box">
    <div class="header">Add news</div>
    <div class="body">
        <form method="post" action="/add">
            <input type="hidden" name="action" value="add">
            <div class="field">
                <div class="name">
                    <label for="title">Title: </label>
                </div>
                <div class="value">
                    <input id="title" name="title" value="${title!}"/>
                </div>
            </div>
            <div class="field">
                <div class="name">
                    <label for="articleText">Text: </label>
                </div>
                <div class="value">
                    <textarea id="articleText" name="articleText">${articleText!}</textarea>
                </div>
            </div>
            <div class="button-field">
                <input type="submit" value="Create">
            </div>
            <div class="error"></div>
        </form>
    </div>
</div>
<script>
    $(function() {
        $(".article form").submit(function () {
            var title = $(this).find("input[name='title']").val();
            var articleText = $(this).find("textarea[name='articleText']").val();
            var $error = $(this).find(".error").text("");
            $.post("", {action: "add", title: title, articleText: articleText}, function (response) {
                if (response["success"]) {
                    setMessage("Your article with title " + title + " was created!");
                    document.location.href = "/";
                } else {
                    $error.text(response["error"]);
                }
            });
            return false;
        });
    })
</script>
</@c.page>