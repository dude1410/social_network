package javapro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javapro.api.request.AddMessageInDialogRequest;
import javapro.api.request.CreateDialogRequest;
import javapro.api.request.MailSupportRequest;
import javapro.api.response.*;
import javapro.config.Config;
import javapro.config.exception.BadRequestException;
import javapro.config.exception.ValidationException;
import javapro.services.DialogsService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@Tag(name = "/api/v1/dialogs", description = "Диалоги")
public class DialogsController {

    private final DialogsService dialogsService;

    public DialogsController(DialogsService dialogsService) {
        this.dialogsService = dialogsService;
    }

    @GetMapping(value = "/api/v1/dialogs")
    @Operation(description = "Получение диалогов пользователя")
    public ResponseEntity<AllPersonDialogsResponse> getPersonDialogs(@RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
                                                           @RequestParam(value = "itemPerPage", required = false, defaultValue = "20") Integer itemPerPage,
                                                           Principal principal) throws BadRequestException {
        if (principal == null) {
            throw new BadRequestException(Config.STRING_AUTH_ERROR);
        }
        return dialogsService.getAllPersonDialogs(principal.getName(), offset, itemPerPage);
    }

    @PostMapping(value = "/api/v1/dialogs")
    @Operation(description = "Создать диалог")
    public ResponseEntity<CreateDialogResponse> addMessageInDialog(@Valid @RequestBody CreateDialogRequest createDialogRequest, Principal principal, Errors errors) throws BadRequestException, ValidationException {
        if (errors.hasErrors()) {
            throw new ValidationException(Config.STRING_FRONT_DATA_NOT_VALID);
        }
        return dialogsService.createDialog(createDialogRequest, principal.getName());
    }

    @GetMapping(value = "/api/v1/dialogs/{id}/messages")
    @Operation(description = "Получение сообщений в диалоге")
    public ResponseEntity<DialogMessagesResponse> getMessagesFromDialogs(@PathVariable Integer id,
                                                         @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
                                                         @RequestParam(value = "itemPerPage", required = false, defaultValue = "20") Integer itemPerPage,
                                                         Principal principal) throws BadRequestException {
        if (principal == null) {
            throw new BadRequestException(Config.STRING_AUTH_ERROR);
        }
        return dialogsService.getDialogMessages(id, offset, itemPerPage, principal.getName());
    }

    @GetMapping(value = "/api/v1/dialogs/unreaded")
    @Operation(description = "Получение количества непрочитанных сообщений в диалоге")
    public ResponseEntity<UnreadedCountResponse> getMessagesFromDialogs(Principal principal) throws BadRequestException {
        if (principal == null) {
            throw new BadRequestException(Config.STRING_AUTH_ERROR);
        }
        return dialogsService.getUnreadCount(principal.getName());
    }

    @PostMapping(value = "/api/v1/dialogs/{id}/messages")
    @Operation(description = "Отправка сообщения")
    public ResponseEntity<AddDialogMessageResponse> addMessageInDialog(@PathVariable Integer id,
                                                                       @Valid @RequestBody AddMessageInDialogRequest addMessageInDialogRequest, Principal principal, Errors errors) throws BadRequestException, ValidationException {
        if (errors.hasErrors()) {
            throw new ValidationException(Config.STRING_FRONT_DATA_NOT_VALID);
        }
        System.out.println("dialog id " + id);
        return dialogsService.addDialogMessage(id, addMessageInDialogRequest.getMessage(), principal.getName());
    }
}
