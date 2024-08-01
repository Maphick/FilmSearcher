<h1><b>FilmSearcher</b></h1>


<b>FilmSearcher<b> - приложение для поиска фильмов в сети

<h3>Добро пожаловать!</h3>

Приложение находится в процессе разработки...

<br><br><br>
| Main screen                                                                                                                                                                                                                    | Info screen                                             |
|:-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------|
| <img src ="/./app/src/main/res/drawable/0.png" width=400>| <img src ="/./app/src/main/res/drawable/1.png" width=400> |
| <img src ="/./app/src/main/res/drawable/2.png" width=400>| <img src ="/./app/src/main/res/drawable/3.png" width=400> |


|  20. Android Views:
|:------------------------------------------------------|
1. Добавлены кнопки меню;
2. Вставлены постеры фильмов;
3. Вставлены текстовые поля;
4. Применены Drawable для фонов;
5. Применены CardView для закругленных углов и левитации;
6. Добавлен функционал с показом Toast-сообщенийна на нажатие каждой кнопки .

| 21. Material Design, темы и стили:
|:------------------------------------------------------|
8. Сделан рефакторинг View на Material компоненты.
9. Добавлен в приложение MaterialToolbar.
10. Перенесены кнопки меню в Toolbar и панель навигации.
11. Создана темная тема приложения, с использованием квалификатора.

| 22. Анимации Pt1: 
|:------------------------------------------------------|
12. Несколько постеров были добавлены на экран с помощью GridLayout и анимированы с помощью LayoutAnimation во время появления.
13. Анимация нажатия постера была сделана с помощью objectAnimator и StateListAnimator.
14. Дополнительно были применены следующие подходы к созданию анимации: создание ObjectAnimator из кода и ViewPropertyAnimation.

| 23. RecyclerView(RV)
|:------------------------------------------------------|


| 24.  Coordinator Layout.
|:------------------------------------------------------|
15. Создан RecyclerView для главной странице с фильмами (пока нет данных по фильмам из сети, используются mock-данные, то есть вручную создается БД с фильмами.
16. Сделана заготовку под экран деталей фильма на основе CoordinatorLayout, в которой присутствует Collapsing Toolbar и «привязанные к нему» кнопки: "В избранное", "Посмотреть позже" и "Поделиться".
17. Добавлен DiffUtils для RV, на экране деталей добавлен Snackbar на нажатие кнопок "В избранное" и "Посмотреть позже".

| 25. Активити, фрагмент, диалог.
|:------------------------------------------------------|
18. Оставлено одно активити, в котором располагается панель навигации и placeholder для фрагментов.
19. Главный экран и экран с деталями переведены на фрагменты.
20. Добавлено всплывающее меню о том, что пользователь сейчас покинет приложение (учитывайте стэк фрагментов).

| 26. Навигация
|:------------------------------------------------------|
21. Создан новый фрагмент с Избранным.
22. Реализована логика добавления фильма в Избранное из экрана деталей.
23. На экране деталей реализована кнопка «Поделиться», то есть функционал, чтобы передать какие-либо данные из приложения в какой-нибудь мессенджер.

| 27. Пользовательский ввод
|:------------------------------------------------------|
24. Добавлено поисковое поле на главный экран.
25. Добавлена функция поиска фильмов среди фильмов, которые есть в БД.
26. Добавлены предложения по поиску.

| 28. Анимация Pt.2
|:------------------------------------------------------|
27. Добавлена анимацию появления главного экрана при помощи Scene
28. Сделано проявление фрагмента через Circular Reveal Animation.
29. Добавлен Splash Screen, использующий векторную анимацию.

| 29. Анимация Pt.3
|:------------------------------------------------------|
27. 
