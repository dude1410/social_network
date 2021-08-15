package javapro.config;

public class Config {
//так и не понял нафига этот приватный конструктор. сонаркуб заставил :)
    private Config(){
        throw new IllegalStateException("Utility class");
    }

    public static final String TIME_ZONE = "Europe/Moscow";
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
    public static final String STRING_NO_TAG_IN_DB = "В базе данных не найден тег";
    public static final String STRING_NO_USER_ID = "Не передан id пользователя";
    public static final String STRING_NO_POST_ID = "Не передан id поста";
    public static final String STRING_NO_COMMENT_ID = "Не передан id коммента";
    public static final String STRING_NO_TAG_ID = "Не передан id тега";
    public static final String STRING_NO_CONTENT_TYPE = "Не передан тип контента";
    public static final String STRING_NO_TAG_NAME = "Не передано название тега";
    public static final String STRING_TAG_EXISTS_IN_DB = "Тег уже существует в базе";
    public static final String STRING_FRONT_DATA_NOT_VALID = "Данные с фронта не прошли валидацию";
    public static final String STRING_LOGOUT_UNSUCCESSFUL = "Неудачная попытка разлогиниться";
    public static final String STRING_BAD_REQUEST = "Неверный запрос";
    public static final String STRING_INVALID_SET_PASSWORD = "Не удалось сменить пароль";
    public static final String STRING_REPEAT_EMAIL = "На этот почтовый ящик уже зарегистрирован другой аккаунт";
    public static final String STRING_INVALID_CONFIRM = "Неудачная попытка подтверждения регистрации";
    public static final String STRING_NO_FRIENDS_FOUND = "Друзей по запросу не найдено";
    public static final String STRING_USER_IS_ALREADY_YOUR_FRIEND = "Этот пользователь уже в списке ваших друзей";
    public static final String STRING_NO_FRIENDSHIP_REQUEST = "Заявка на добавление в друзья не найдена";
    public static final String STRING_NO_DATA_FOUND = "Запись не найдена";
    public static final String STRING_NO_RECOMMENDATIONS = "Список рекомендаций не удалось составить";
    public static final String STRING_REQUEST_REPEATED = "Запрос на добавление в друзья уже существует";
    public static final String STRING_TOKEN_ERROR = "Ошибка генерации токена";
    public static final String STRING_TOKEN_CHECK_ERROR = "Ошибка проверки токена";
    public static final String STRING_WRONG_DATA = "Неверно указана дата";
    public static final String STRING_NOT_FOUND_NOTIFICATION_SETUP = "Нет данных по настройкам уведомлений";
    public static final String STRING_PERSON_ISBLOCKED = "Пользователь заблокирован";
    public static final String STRING_PERSON_ISDELETED = "Пользователь удален";
    public static final String STRING_PERSON_EMPTY_FISTNAME = "Поле с именем не может быть пустым";
    public static final String STRING_PERSON_EMPTY_LASTNAME = "Поле с фамилией не может быть пустым";
    public static final String STRING_NOTIFICATION_ISDELETED = "Уведомление отсутствует или уже удалено";
    public static final String STRING_FILE_TOO_BIG = "Файл слишком большой (не больше 500 килобайт)";
    public static final String ERROR_MESSAGE = "string";
    public static final String TIMEZONE = "Europe/Moscow";
    public static final String LIKE_OBJECT_POST = "Post";
    public static final String LIKE_OBJECT_COMMENT = "Comment";
    public static final String DATEFORMAT = "MM-dd";
    public static final String WALL_RESPONSE = "successfully";
    public static final String STORAGE = "/storage/thumb/";

    public static final int INT_AUTH_BCRYPT_STRENGTH = 12;
}
