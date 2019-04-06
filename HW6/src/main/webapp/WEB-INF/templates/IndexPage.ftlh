<#-- @ftlvariable name="articles" type="ru.itmo.webmail.web.page.IndexPage.ArticleDisplay[]" -->

<#import "common.ftl" as c>

<@c.page>
<div class="articles">
    <div class="no-data">There are no articles, become first!</div>
</div>
<template class="article-template">
    <article>
        <div class="title">{{title}}</div>
        <div class="information">{{login}}</div>
        <div class="body">{{text}}</div>
        <div class="footer">
            <div class="date">{{date}}</div>
        </div>
    </article>
</template>
<script>
    $(function () {
        var $noData = $(".articles .no-data");
        $noData.text("Loading...");
        $.post("", {action: "find"}, function (articles) {
            var $body = $(".articles");
            if (articles.length > 0) {
                $body.empty();
                articles.forEach(function (articleDisplay) {
                    var $tmplt = cloneTemplate($("template.article-template"));
                    $tmplt.find(".title").text(articleDisplay.article.title);
                    $tmplt.find(".body").text(articleDisplay.article.text);
                    $tmplt.find(".date").text(articleDisplay.article.creationTime);
                    $tmplt.find(".information").text("By " + articleDisplay.user);
                    $body.append($tmplt);
                });
            } else {
                $noData.text("No data");
            }
        })
    })
</script>
</@c.page>
