# Курс веб-программирования
## Практика №1
Контест на CF


## Практика №2
Знакомство с CSS - разметкой.


## Практика №3
Знакомство с Static Servlet


## Практика №4
Откройте проект в IDEA как maven-проект, запустите в локальном Tomcat, используя корневой контекст /.
Убедитесь в работоспособности. Должны отображаться страницы, похожие на Codeforces. Форма регистрации должна функционировать, по ссылке /users должен быть список зарегистрированных пользователей.
1. На форму регистрации добавьте еще одно поле passwordConfirmation и дополнительно валидируйте, что они совпали. То есть после выполнения этого задания на форме регистрации будет три поля: login, password, passwordConfirmation. И появится доп. сообщение об ошибке.
2. Добавьте пользователю long-поле с названием id. При регистрации пользователя устанавливайте ему id по порядку от 1 (потом это будет делать база, думать о race condition пока не надо). Аналогично, добавьте пользователю поле email. Он указывает email при регистрации. Проверьте уникальность и примерную корректность (мягко, например, содержит ровно один символ @). Добавьте такое поле в регистрацию. Возможно, вам понадобиться удалить файл UserRepositoryImpl из %TEMP%, чтобы почистить данные (ведь у старых пользователей не будет стоять id и email). Добавьте колонки id и email на страницу с пользователями.
3. Обратите внимание, что в настоящий момент для всех action-методов в страницах требуются два обязательных параметра HttpServletRequest request, Map<String, Object> view. В ряде случаев (часто) достаточно одного из них или они вообще не нужны. Измените исходный код FrontServlet так, чтобы в качестве action-метода подыскивался метод просто по названию (нет требования к точной сигнатуре как сейчас). При запуске надо смотреть на типа параметров метода (method.getParameterTypes()) и если параметр имеет тип Map.class, то ожидать что это view (передать), тоже самое сделать для HttpServletRequest. После этого следует удалить из всех страниц из всех action-методов лишние параметры и убедиться в работоспособности.
4. Добавьте в футер информацию об общем кол-ве зарегистрированных пользователей. Для этого сделайте методы findCount в UserRepository/UserService. Добавьте в macro для страницы (common.ftl) использование переменной шаблона ${userCount}. После этого вам надо как-то во view во всех страницах всегда класть по ключу “userCount” значение userService.findCount(). Для этого сделайте общий базовый класс для всех страниц Page и пару методов у него: 
    ```java
    void before(HttpServletRequest request, Map<String, Object> view) 
    void after(HttpServletRequest request, Map<String, Object> view)
    ``` 
    В потомках (всех страницах) можно будет переопределять этот метод (не забывайте вызывать super.before()/super.after()). Добавьте в код FrontServlet поиск и запуск before/after-методов до и после запуска action соответственно. После этого у базового класса Page в before (или after) следует добавить помещение во view нужного значения и теперь всюду в футере будет нужная информация.
5. Сделайте форму авторизации EnterPage. Она будет похожа на форму регистрации. После успешной авторизации текущего пользователя (прям объект User) можно сложить в сессию. Сделайте так, что если пользователь авторизован, то вместо Enter | Register показывается <UserLogin> | Logout. Сделайте простую logout-страницу, посещение которой трёт из сессии атрибут “user” и перенаправляет на главную с соответствующим сообщением. (Вообще, так делать плохо из соображений безопасности, но об этом в нашем курсе позже.)
6. Добавьте класс предметной области  News (новость). У него должны быть поля userId и text (кто создал её и сам текст). Для авторизованного пользователя сделайте еще одну ссылку в основном меню “Add News” с простой формой добавления (просто одна textarea + кнопка). Сами новости выводите в сайдбаре: добавьте блок News и там выводите все новости - её текст и от кого она (логин). Возможно вам понадобиться метод find(long id) в UserRepository/UserService для поиска нужного пользователя по userId из новости. Не переживайте за performance (что на каждую новость надо искать пользователя), это учебное задание, а не реальный проект.


## Практика №5
Скачайте проект с https://assets.codeforces.com/files/6e78d88da1176e86/5d/wm1.7z. Перейдите по http://wp.codeforces.com/phpMyAdmin/ в свою базу данных и накликайте там таблицу User с полями:
  * id (BIGINT до 18 знаков, autoincrement, primary key, not null)
  * login (VARCHAR до 255 знаков, ключ уникальности unique_User_login, not null)
  * passwordSha (VARCHAR до 255 знаков, not null)
  * creationTime (DATETIME, индекс index_User_creationTime, not null)
  
  Запустите проект (поправьте profile.properties), убедитесь, что всё работает - регистрация+вход+выход.

1. Добавить поддержку email (уникальный) при регистрации.

2. Сделать, что входить в систему можно по логину или email (а не только по логину, что хочешь, то и вводишь).
3. Поддержать новую сущность Event - события от пользователя с полями id, userId, type, creationTime. Поле userId надо сделать внешним ключом на User: ALTER TABLE `Event` ADD CONSTRAINT `fk_Event_userId` FOREIGN KEY (`userId`) REFERENCES `User` (`id`).  Поле type должно быть enum с пока двумя значениями ENTER, LOGOUT. Вставлять записи в таблицу Event на каждый удачный вход/выход.

4. Сделать якобы подтверждение email для нового аккаунта. Добавить в User новое поле confirmed, по-умолчанию оно false. С таким значением логиниться нельзя. Сделать таблицу EmailConfirmation с полями id, userId, secret (строковый случайный секрет), creationTime. При регистрации добавлять туда запись (и как будто отправлять письмо с просьбой зайти на страницу /confirm?secret=blablabla). Сделать ConfirmPage на которую если зайти с параметром secret и если есть соответствующий EmailConfirmation, то пользователь подтверждается.

5. Сделать сущность Talk (id, sourceUserId, targetUserId, text, creationTime) - сообщение от одного пользователя другому. Сделать страницу /talks (только для авторизованных). Там простая форма с 2 полями "Send Message" и список всех сообщений, где заданный пользователь автор или адресат  в порядке от более поздних к более новым.

6. Обратите внимание, что сейчас в RepositoryImpl очень много похожего кода. Проведите рефакторинг (сами придумайте какой), чтобы уменьшить размер кода, переиспользовав его. Возможно, вам понадобится создавать дополнительные удобные методы в DatabaseUtils


## Практика №6
Перейдите по http://wp.codeforces.com/phpMyAdmin/ в свою базу данных (рекомендую использовать u??b) и накликайте там таблицу User с полями:
  * id (BIGINT до 18 знаков, autoincrement, primary key, not null)
  * login (VARCHAR до 255 знаков, ключ уникальности unique_User_login, not null)
  * passwordSha (VARCHAR до 255 знаков, not null)
  * creationTime (DATETIME, индекс index_User_creationTime, not null)

Запустите проект (поправьте profile.properties), убедитесь, что всё работает - регистрация+вход+выход, просмотр пользователей.

1. Переделайте форму входа в систему на использование AJAX.
2. Создайте сущность Article (статья, пост) c полями id, userId, title, text, creationTime (помним про внешний ключ и прочие детали) и соответствующие ей ArticleRepository, ArticleRepositoryImpl, ArticleService. По ссылке /article добавьте форму создания статьи (два поля: title типа input и text типа textarea, кнопка Create), которая будет работать посредством AJAX. Добавьте ссылку на создание статьи в меню, если пользователь авторизован.
3. На главной странице (IndexPage) добавьте вывод всех статей в обратном хронологическом порядке. Используйте AJAX для асинхронной подгрузке статей (используйте элемент template для задания шаблона статьи). Если вы справились с заданием из 2-го дз про разметку статьи, то используйте разметку/css из того задания.
4. Добавьте в Article поле/колонку hidden. Отображайте на главной только нескрытые статьи. Сделайте страницу /myArticles с таблицей своих статей (только id, title и колонка с кнопкой Hide/Show). Пусть таблица загружается без AJAX, просто выводится в шаблоне. Третий столбец должен содержать либо кнопку Hide, либо кнопку Show. При нажатии на кнопку должен уходить AJAX-запрос на изменение поля hidden и при успешном выполнении операции текст на кнопке и её действие должно актуализироваться. То есть сама страница должна работать без перезагрузок. Не забудьте проверить в бэкенде, что запрос пришел от автора статьи.
5. обавьте пользователю булевское свойство/колонку admin (по умолчанию false). Если страницу /admin просматривает админ, то добавьте в таблицу колонку Admin. В этой колонке должен отображаться текущее значение admin пользователя и ссылка либо enable (для false), либо disable (для true). По нажатию на такую ссылку в бэкенд должен уходить AJAX-запрос с id пользователя, менять значение admin у этого пользователя, а в обработчике на успех надо перерисовать false<->true и enable<->disable. Иными словами управление добавить/удалить из админов должно происходить без перезагрузки страницы. Не забудьте проверить в бэкенде, что запрос пришел от админа.


## Практика №7
Запустите проект (поправьте src/main/resources/application.properties). Используйте базы данных вида uXXc (в конце буква c) . Запуск создаст таблицу user. Добавьте к ней колонку passwordSha, которая по-умолчанию NULL. Убедитесь, что всё работает - регистрация+вход+выход, просмотр пользователей. 

1. Сделайте небольшую страницу с профилем пользователя по ссылке /user/{id}. Для этого используйте параметр в методе с аннотацией @PathVariable (погуглите, это просто). Просто отобразите id, login и creation на отдельной странице (или надпись “No such user”, если такого нет).

2. Добавьте сущность простейшую Notice с тремя полями id, content (используйте аннотацию @Lob), creationTime. Обратите внимание, что Notice не имеет связи с User (пока всё делаем без связей). Добавьте NoticeRepository, NoticeService. В классе Page аналогично с getUser сделайте getNotices (не забудьте аннотацию @ModelAttribute), чтобы все потомки Page добавляли в модель переменную notices. Измените common.ftlh, чтобы в блоке “Pay Attention” отображал список всех notices. Добавьте несколько notices через базу, убедитесь, что на всех страницах теперь отображаются ваши notices в сайдбаре.

3. Добавьте форму добавления Notice. Это должна быть просто одна textarea с кнопкой “Add”.

4. Добавьте пользователям колонку/поле disabled, по умолчанию равно false. На страницу /users добавьте колонку с действиями. Там в каждой строчке будет ровно одна кнопка (Disable или Enable в зависимости от текущего статуса). При нажатии кнопки отправляется маленькая форма, которая меняет статус пользователя. После этого страница перегружается и там уже кнопка для обновлённого статуса. Пока не используйте javascript, просто сделайте такие небольшие формы в каждой строке. Добавьте в форму входа сообщение “User is disabled” при попытке входа заблокированного пользователя.


## Практика №8
Запустите проект (поправьте src/main/resources/application.properties). Используйте базы данных вида uXXd (в конце буква d) . Запуск создаст таблицы user, notice, role, user_role. Добавьте к ней колонку passwordSha, которая по-умолчанию NULL. Убедитесь, что работает - регистрация+вход+выход notice. Просмотр пользователей и переходы на страницы про notices выкидывают на /enter, из-за аннотаций уровня доступа. Добавьте роли с именами 0, 1 (это порядковые номера для enum) через phpMyAdmin. Создайте нескольких пользователей, дайте им разные права (просто руками добавьте записи в user_role). Полезный справочный ресурс https://en.wikibooks.org/wiki/Java_Persistence/Relationships

1. Добавьте в класс Notice поле user (автора). И добавьте мэппинг many-to-one. Вам придется и модифицировать обратный мэппинг (поле User#notices). Прочтите по ссылке как сделать двунаправленный мэппинг many-to-one и one-to-many: https://en.wikibooks.org/wiki/Java_Persistence/OneToMany#Example_of_a_OneToMany_relationship_and_inverse_ManyToOne_annotations
После этого в common.ftlh допишите в конце каждого Notice в сайдбаре “by <login>” (например, by mike).

2. Добавьте сущность простейшую Comment с четырьмя полями id, text (используйте аннотацию @Lob), user, notice. Добавьте и List<Comment> comments в User и Notice. Настройте двунаправленные мэппинги many-to-one и one-to-many. Сделайте простейшую страницу с отображением одного Notice и списка его комментариев под ним (по урлу /notice/{id}). Перед списком комментариев для залогинённого пользователя добавьте форму добавления нового комментария (просто одна textarea и кнопка). Пусть в сайдбаре ссылки View all ведут на индивидуальную страницу этого Notice.

3. Добавьте сущность Tag (тег). У него есть только id и name. Теги должны находиться с Notice в отношении многие-ко-многим. Сделайте однонаправленный мэппинг многие-ко-многим, то есть поле Notice#tags. Надо делать по образу и подобию как связаны пользователи и роли. Добавьте руками несколько тегов (записи в таблицу tag) и несколько тегов к notices (записи в таблицу notice_tag). Отображайте список тегов и в сайдбаре и на отдельной странице для Notice.

4. Добавьте поле Tags в форму создания Notice. Считайте, что туда надо ввести список тегов через пробельные символы (whitespaces). Валидируйте, если ввели что-то, что не является простым словом из латинских букв. При сохранении нового Notice надо сохранить его вместе с тегами (возможно, в таблице tag появятся новые записи, возможно будут использованы старые).