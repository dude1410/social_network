package JavaPRO.config;

public class Config {
    public static final String STRING_FIELD_CANT_BE_BLANK = "Поле не может быть пустым.";
    public static final String STRING_AUTH_INVALID_EMAIL = "Адрес указан неверно.";
    public static final String STRING_AUTH_SHORT_PASSWORD = "Слишком короткий пароль.";
    public static final String STRING_AUTH_ERROR = "Ошибка аутентификации";
    public static final String STRING_AUTH_EMPTY_EMAIL_OR_PASSWORD = "Адрес или пароль не указаны.";
    public static final String STRING_AUTH_LOGIN_NO_SUCH_USER = "Пользователь не найден.";
    public static final String STRING_AUTH_WRONG_PASSWORD = "Пароль указан неверно.";
    public static final String STRING_AUTH_WRONG_FORMAT = "Неверный формат логина или пароля";
    public static final String STRING_MAIL_TO_SUPPORT_RESPONSE = "Ваше обращение в службу поддержки направлено";
    public static final String STRING_MAIL_TO_SUPPORT_SUBJECT = "Обращение в службу поддержки от ";
    public static final String STRING_MAIL_TO_SUPPORT_NO_EMAIL = "Не указана (указана неверно) почта для обратной связи";
    public static final String STRING_MAIL_TO_SUPPORT_NO_TEXT = "Поле для текста обращения должно быть заполнено";
    public static final String STRING_USER_NOTAPPRUVED_OR_BLOCKED = "Пользователь не активный или заблокирован";
    public static final String STRING_MAIL_TO_SUPPORT_SHORT_TEXT = "Текст сообщения должен быть не менее 20 символов";
    public static final String STRING_NO_SEARCH_TEXT = "Не введен запрос для поиска";
    public static final String STRING_NO_POSTS_IN_DB = "В базе данных не найдено постов";
    public static final String STRING_NO_POST_IN_DB = "В базе данных не найден пост";
    public static final String STRING_NO_COMMENT_IN_DB = "В базе данных не найден комментарий";
    public static final String STRING_NO_PERSON_IN_DB = "В базе данных не найден пользователь";
    public static final String STRING_NO_USER_ID = "Не передан id пользователя";
    public static final String STRING_NO_POST_ID = "Не передан id поста";
    public static final String STRING_NO_COMMENT_ID = "Не передан id коммента";
    public static final String STRING_NO_CONTENT_TYPE = "Не передан тип контента";
    public static final String STRING_FRONT_DATA_NOT_VALID = "Данные с фронта не прошли валидацию";
    public static final String STRING_LOGOUT_UNSUCCESSFUL = "Неудачная попытка разлогиниться";
    public static final String STRING_BAD_REQUEST = "Неверный запрос";
    public static final String STRING_INVALID_SET_PASSWORD = "Не удалось сменить пароль";
    public static final String STRING_REPEAT_EMAIL = "На этот почтовый ящик уже зарегестрирован другой аккаунт";
    public static final String STRING_INVALID_CONFIRM = "Неудачная попытка подтверждения регистрации";

    public static final int INT_AUTH_BCRYPT_STRENGTH = 12;
}
