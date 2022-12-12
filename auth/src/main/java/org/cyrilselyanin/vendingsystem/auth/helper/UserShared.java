package org.cyrilselyanin.vendingsystem.auth.helper;

public class UserShared {

	public static final String USERNAME_REGEXP_PATTERN = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){0,48}[a-zA-Z0-9]$";
	public static final String NO_USER_ACCESS_MESSAGE = "Недостаточно прав.";
	public static final String WRONG_USER_MESSAGE = "Указан неверный пользователь.";
	public static final String USER_ALREADY_EXISTS_MESSAGE = "Данный пользователь уже существует.";
	public static final String NO_SUCH_USER_MESSAGE = "Данный пользователь не существует.";
	public static final String NO_EMPTY_PASSWORD_MESSAGE = "Пароль не может быть пустым.";


}
